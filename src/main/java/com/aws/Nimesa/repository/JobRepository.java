package com.aws.Nimesa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aws.Nimesa.domain.Job;

public interface JobRepository extends JpaRepository<Job, String> {

}
