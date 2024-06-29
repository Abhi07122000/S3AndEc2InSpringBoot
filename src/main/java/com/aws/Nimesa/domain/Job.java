package com.aws.Nimesa.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Job {

	@Id
    private String id;
    private String status;
}
