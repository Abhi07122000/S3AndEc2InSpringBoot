package com.aws.Nimesa.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.aws.Nimesa.domain.DiscoveryResult;
import com.aws.Nimesa.domain.Job;
import com.aws.Nimesa.domain.S3ObjectDetail;
import com.aws.Nimesa.repository.DiscoveryResultRepository;
import com.aws.Nimesa.repository.JobRepository;
import com.aws.Nimesa.repository.S3ObjectDetailRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AwsService {

	@Autowired
	private EC2Service ec2Service;

	@Autowired
	private S3Service s3Service;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private DiscoveryResultRepository discoveryResultRepository;

	@Autowired
	private S3ObjectDetailRepository s3ObjectDetailRepository;

	public Map<String, String> discoverServices(List<String> services) {
		Map<String, String> map = new HashMap<>();
		for (String service : services) {
			String jobId = createJob(service);
			if (services.contains("EC2")) {
				discoverEC2Instances(jobId);
				map.put(service, jobId);
			}
			if (services.contains("S3")) {
				discoverS3Buckets(jobId);
				map.put(service, jobId);
			}
		}
		return map;
	}

	@Async
	public void discoverEC2Instances(String jobId) {
		try {
			List<String> instances = ec2Service.discoverEC2Instances();
			DiscoveryResult result = new DiscoveryResult(jobId, "EC2", instances.toString());
			discoveryResultRepository.save(result);
			updateJobStatus(jobId, "Success");
		} catch (Exception e) {
			updateJobStatus(jobId, "Failed");
		}
	}

	@Async
	public void discoverS3Buckets(String jobId) {
		try {
			List<String> buckets = s3Service.discoverS3Buckets();
			DiscoveryResult result = new DiscoveryResult(jobId, "S3", buckets.toString());
			discoveryResultRepository.save(result);
			updateJobStatus(jobId, "Success");
		} catch (Exception e) {
			updateJobStatus(jobId, "Failed");
		}
	}

	public Object getDiscoveryResult(String service) {
        if (service.equalsIgnoreCase("S3")) {
            return s3Service.discoverS3Buckets();
        } else if (service.equalsIgnoreCase("EC2")) {
            return ec2Service.discoverEC2Instances();
        } else {
            return "Service not recognized";
        }
    }
	
	public String getS3BucketObjects(String bucketName) {
		String jobId = UUID.randomUUID().toString();
		Job job = new Job(jobId, "S3", "In Progress");
		jobRepository.save(job);

		CompletableFuture.runAsync(() -> {
			try {
				List<String> objectKeys = s3Service.getBucketObjects(bucketName);
				objectKeys.forEach(key -> {
					S3ObjectDetail detail = new S3ObjectDetail(jobId, bucketName, key);
					s3ObjectDetailRepository.save(detail);
				});
				job.setStatus("Success");
			} catch (Exception e) {
				log.info(e);
				job.setStatus("Failed");
			} finally {
				jobRepository.save(job);
			}
		});

		return jobId;
	}

	public long getS3BucketObjectCount(String bucketName) {
		return s3ObjectDetailRepository.countByBucketName(bucketName);
	}

	public List<String> getS3BucketObjectsLike(String bucketName, String pattern) {
		return s3ObjectDetailRepository.findByBucketName(bucketName).stream().map(S3ObjectDetail::getObjectKey)
				.filter(key -> key.contains(pattern)).collect(Collectors.toList());
	}

	// Job Related Buisness logic//

	public String createJob(String service) {
		String jobId = UUID.randomUUID().toString();
		Job job = new Job(jobId, service, "In Progress");
		jobRepository.save(job);
		return jobId;
	}

	public void updateJobStatus(String jobId, String status) {
		Optional<Job> jobOptional = jobRepository.findById(jobId);
		if (jobOptional.isPresent()) {
			Job job = jobOptional.get();
			job.setStatus(status);
			jobRepository.save(job);
		}
	}

	public String getJobStatus(String jobId) {
		return jobRepository.findById(jobId).map(Job::getStatus).orElse("Job not found");
	}
}
