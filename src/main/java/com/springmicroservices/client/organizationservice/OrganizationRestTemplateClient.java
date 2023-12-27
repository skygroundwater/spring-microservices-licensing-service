package com.springmicroservices.client.organizationservice;

import com.springmicroservices.model.Organization;
import lombok.AllArgsConstructor;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrganizationRestTemplateClient {

    private final KeycloakRestTemplate restTemplate;

    public Organization getOrganization(String organizationId) {
        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(
                        "http://organization-service:8090/v1/organization/{organizationId}",
                        HttpMethod.GET,
                        null,
                        Organization.class,
                        organizationId);
        System.out.println(restExchange.getHeaders().get("Authorization"));

        return restExchange.getBody();
    }
}