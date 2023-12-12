package com.springmicroservices.service;

import com.springmicroservices.client.organizationservice.OrganizationDiscoveryClient;
import com.springmicroservices.client.organizationservice.OrganizationFeignClient;
import com.springmicroservices.client.organizationservice.OrganizationRestTemplateClient;
import com.springmicroservices.controller.LicenseController;
import com.springmicroservices.exception.NotFoundDataException;
import com.springmicroservices.exception.NotValidDtoException;
import com.springmicroservices.model.License;
import com.springmicroservices.model.Organization;
import com.springmicroservices.repository.LicenseRepository;
import com.springmicroservices.service.interfaces.LicenseService;
import com.springmicroservices.service.interfaces.OrganizationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Random;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@AllArgsConstructor
public class LicenseServiceImpl implements LicenseService {


    private final MessageSource messageSource;

    private final LicenseRepository licenseRepository;

    private final OrganizationService organizationService;

    @Override
    public License getLicense(String licenseId, String organizationId, String clientType, Locale locale) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);

        if (license == null) {
            throw new NotFoundDataException(String.format(messageSource.getMessage("license.search.error.message", null, locale)));
        }

        fillOrganizationInfoForLicense(organizationService.retrieveOrganizationInfo(organizationId, clientType), license);

        license.add(linkTo(methodOn(LicenseController.class)
                        .getLicense(organizationId, license.getLicenseId(), locale))
                        .withSelfRel(),
                linkTo(methodOn(LicenseController.class)
                        .createLicense(organizationId, license, null))
                        .withRel("createLicense"),
                linkTo(methodOn(LicenseController.class)
                        .updateLicense(organizationId, license, locale))
                        .withRel("updateLicense"),
                linkTo(methodOn(LicenseController.class)
                        .deleteLicense(organizationId, license.getLicenseId(), locale)).withRel("deleteLicense"));

        return license;
    }

    private void fillOrganizationInfoForLicense(Organization organization, License license) {
        if (null != organization) {
            license.setOrganizationName(organization.getName());
            license.setContactName(organization.getContactName());
            license.setContactEmail(organization.getContactEmail());
            license.setContactPhone(organization.getContactPhone());
        }
    }

    @Override
    public License createLicense(License license, Locale locale) {
        if (license != null) {
            licenseRepository.save(license);
        }
        throw new NotValidDtoException(String.format(messageSource.getMessage("license.create.error.message", null, locale)));
    }

    @Override
    public License updateLicense(License license, Locale locale) {
        if (license != null) {
            licenseRepository.save(license);
        }
        throw new NotValidDtoException(messageSource.getMessage("license.update.error.message", null, locale));
    }

    @Override
    public String deleteLicense(String licenseId, Locale locale) {
        if (licenseId != null) {
            licenseRepository.deleteById(licenseId);
            return String.format(messageSource.getMessage("license.delete.message", null, locale), licenseId);
        }
        throw new NotValidDtoException(String.format(messageSource.getMessage("license.delete.error.message", null, locale), licenseId));
    }
}