package com.aws.Nimesa.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class S3ObjectDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String jobId;
    private String bucketName;
    private String objectKey;
    
    public S3ObjectDetail(String jobId, String bucketName, String objectKey) {
        this.jobId = jobId;
        this.bucketName = bucketName;
        this.objectKey = objectKey;
    }
}
