package com.aws.Nimesa.api.v1.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aws.Nimesa.service.AwsService;

@RestController
@RequestMapping("/api/v1/aws")
public class AwsController {

	@Autowired
	private AwsService awsService;

	@PostMapping("/discoverServices")
	public ResponseEntity<Map<String, String>> discoverServices(@RequestBody List<String> services) {
		return ResponseEntity.ok(awsService.discoverServices(services));
	}

	@GetMapping("/getJobStatus/{jobId}")
	public ResponseEntity<String> getJobStatus(@PathVariable String jobId) {
		String status = awsService.getJobStatus(jobId);
		return ResponseEntity.ok(status);
	}

	@GetMapping("/getDiscoveryResult/{service}")
	public ResponseEntity<?> getDiscoveryResult(@PathVariable String service) {
		Object result = awsService.getDiscoveryResult(service);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/getS3BucketObjects/{bucketName}")
	public ResponseEntity<String> getS3BucketObjects(@PathVariable String bucketName) {
		String jobId = awsService.getS3BucketObjects(bucketName);
		return ResponseEntity.ok(jobId);
	}

	@GetMapping("/getS3BucketObjectCount/{bucketName}")
	public ResponseEntity<Long> getS3BucketObjectCount(@PathVariable String bucketName) {
		long count = awsService.getS3BucketObjectCount(bucketName);
		return ResponseEntity.ok(count);
	}

	@GetMapping("/getS3BucketObjectsLike")
	public ResponseEntity<List<String>> getS3BucketObjectsLike(@RequestParam(name = "bucketName") String bucketName,
			@RequestParam(name = "pattern") String pattern) {
		List<String> objectKeys = awsService.getS3BucketObjectsLike(bucketName, pattern);
		return ResponseEntity.ok(objectKeys);
	}
}
