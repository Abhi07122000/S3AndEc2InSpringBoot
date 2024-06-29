package com.aws.Nimesa.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;

@Service
public class EC2Service {

    @Autowired
    private AmazonEC2 amazonEC2;

    public List<String> discoverEC2Instances() {
        DescribeInstancesRequest request = new DescribeInstancesRequest();
        DescribeInstancesResult response = amazonEC2.describeInstances(request);
        return response.getReservations().stream()
                .flatMap(reservation -> reservation.getInstances().stream())
                .map(instance -> instance.getInstanceId())
                .collect(Collectors.toList());
    }
}
