package com.aws.Nimesa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aws.Nimesa.domain.S3ObjectDetail;

public interface S3ObjectDetailRepository extends JpaRepository<S3ObjectDetail, Long> {

}
