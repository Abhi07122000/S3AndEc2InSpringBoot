package com.aws.Nimesa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aws.Nimesa.domain.DiscoveryResult;

public interface DiscoveryResultRepository extends JpaRepository<DiscoveryResult, Long> {
	Optional<DiscoveryResult> findByService(String service);
}
