package com.springmicroservices.client.organizationservice;

import com.springmicroservices.model.Organization;
import lombok.AllArgsConstructor;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class OrganizationDiscoveryClient {

    private final DiscoveryClient discoveryClient;

    private final KeycloakRestTemplate restTemplate;

    public Organization getOrganization(String organizationId) {
        List<ServiceInstance> instances = discoveryClient
                .getInstances("spring-cloud-api-gateway");

        if (instances.isEmpty()) return null;

        String serviceUri = String.format("%s/organization/v1/organization/%s",
                instances.stream().findAny(), organizationId);

        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(
                        serviceUri,
                        HttpMethod.GET,
                        null,
                        Organization.class,
                        organizationId);

        return restExchange.getBody();
    }
}