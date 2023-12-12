package com.springmicroservices.client.organizationservice;

import com.springmicroservices.model.Organization;
import lombok.AllArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@AllArgsConstructor
public class OrganizationDiscoveryClient {

    private final DiscoveryClient discoveryClient;

    private final RestTemplate restTemplate;

    public Organization getOrganization(String organizationId) {
        List<ServiceInstance> instances = discoveryClient
                .getInstances("organization-service");

        if (instances.isEmpty()) return null;
        String serviceUri = String.format("%s/v1/organization/%s",
                instances.get(0).getUri().toString(), organizationId);

        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(
                        serviceUri,
                        HttpMethod.GET,
                        null, Organization.class, organizationId);

        return restExchange.getBody();
    }
}
