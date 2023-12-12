package com.springmicroservices.service.interfaces;

import com.springmicroservices.model.Organization;

public interface OrganizationService {
    Organization retrieveOrganizationInfo(String organizationId, String clientType);
}
