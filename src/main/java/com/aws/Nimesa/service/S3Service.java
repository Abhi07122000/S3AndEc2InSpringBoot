package com.aws.Nimesa.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@Service
public class S3Service {

    @Autowired
    private AmazonS3 amazonS3;

    public List<String> discoverS3Buckets() {
        return amazonS3.listBuckets().stream()
                .map(bucket -> bucket.getName())
                .collect(Collectors.toList());
    }

    public List<String> getBucketObjects(String bucketName) {
        ObjectListing objectListing = amazonS3.listObjects(bucketName);
        return objectListing.getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
    }
}
