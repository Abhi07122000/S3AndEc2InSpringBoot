package com.aws.Nimesa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aws.Nimesa.domain.S3ObjectDetail;

public interface S3ObjectDetailRepository extends JpaRepository<S3ObjectDetail, Long> {

	List<S3ObjectDetail> findByBucketName(String bucketName);

	List<S3ObjectDetail> findByBucketNameAndObjectKeyLike(String bucketName, String pattern);

	Long countByBucketName(String bucketName);

}
