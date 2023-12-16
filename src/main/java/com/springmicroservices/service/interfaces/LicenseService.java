package com.springmicroservices.service.interfaces;

import com.springmicroservices.model.License;

import java.util.List;
import java.util.Locale;

public interface LicenseService {
    License getLicense(String licenseId, String organizationId, String clientType, Locale locale);

    List<License> getLicensesByOrganization(String organizationId);

    License createLicense(License license, Locale locale);

    License updateLicense(License license, Locale locale);

    String deleteLicense(String licenseId, Locale locale);
}
