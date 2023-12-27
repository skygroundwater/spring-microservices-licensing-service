package com.springmicroservices.client.organizationservice;

import com.springmicroservices.exception.NotFoundDataException;
import com.springmicroservices.model.Organization;
import com.springmicroservices.repository.OrganizationRedisRepository;
import com.springmicroservices.utils.usercontext.UserContext;
import lombok.AllArgsConstructor;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrganizationRestTemplateClient {

    private final Logger logger = LoggerFactory.getLogger(OrganizationRestTemplateClient.class);

    private final KeycloakRestTemplate restTemplate;

    private final OrganizationRedisRepository organizationRedisRepository;

    private Organization checkRedisCache(String organizationId) {
        try {
            return organizationRedisRepository.findById(organizationId).orElseThrow(
                    () -> new NotFoundDataException("Организация id " + organizationId + " не была найдена"));
        } catch (NotFoundDataException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    private void cacheOrganizationObject(Organization organization) {
        try {
            organizationRedisRepository.save(organization);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public Organization getOrganization(String organizationId) {
        logger.debug("In Licensing Service.getOrganization: {}",
                UserContext.CORRELATION_ID);

        Organization organization = checkRedisCache(organizationId);

        if (organization != null) {
            logger.debug("I have successfully retrieved an organization {} from the redis cache: {}",
                    organizationId, organization);
            return organization;
        }
        logger.debug("Unable to locate organization from the redis cache: {}", organizationId);
        ResponseEntity<Organization> restExchange = restTemplate.exchange(
                "http://api-gateway:8040/organization/v1/organization/{organizationId}",
                HttpMethod.GET,
                null,
                Organization.class,
                organizationId);

        organization = restExchange.getBody();
        if (organization != null) {
            cacheOrganizationObject(organization);
        }
        return restExchange.getBody();
    }
}