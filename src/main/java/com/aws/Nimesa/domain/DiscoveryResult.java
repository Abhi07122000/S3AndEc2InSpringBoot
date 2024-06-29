package com.aws.Nimesa.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscoveryResult {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String jobId;
	private String service;
	private String result;

	public DiscoveryResult(String jobId, String service, String result) {
		this.jobId = jobId;
		this.service = service;
		this.result = result;
	}
}
