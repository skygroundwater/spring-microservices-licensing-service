package com.springmicroservices.service;

import com.springmicroservices.client.organizationservice.OrganizationDiscoveryClient;
import com.springmicroservices.client.organizationservice.OrganizationFeignClient;
import com.springmicroservices.client.organizationservice.OrganizationRestTemplateClient;
import com.springmicroservices.model.Organization;
import com.springmicroservices.service.interfaces.OrganizationService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationFeignClient organizationFeignClient;

    private final OrganizationRestTemplateClient organizationRestTemplateClient;

    private final OrganizationDiscoveryClient organizationDiscoveryClient;

    @Override
    @CircuitBreaker(name = "organizationService")
    public Organization retrieveOrganizationInfo(String organizationId,
                                                 String clientType) {
        return switch (clientType) {
            case "feign" -> {
                System.out.println("I am using the feign client");
                yield organizationFeignClient.getOrganization(organizationId);
            }
            case "rest" -> {
                System.out.println("I am using the rest client");
                yield organizationRestTemplateClient.getOrganization(organizationId);
            }
            case "discovery" -> {
                System.out.println("I am using the discovery client");
                yield organizationDiscoveryClient.getOrganization(organizationId);
            }
            default -> organizationRestTemplateClient.getOrganization(organizationId);
        };
    }
}