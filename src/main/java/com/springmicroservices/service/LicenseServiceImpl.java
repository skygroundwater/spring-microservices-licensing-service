package com.springmicroservices.service;

import com.springmicroservices.controller.LicenseController;
import com.springmicroservices.exception.NotFoundDataException;
import com.springmicroservices.exception.NotValidDtoException;
import com.springmicroservices.model.License;
import com.springmicroservices.model.Organization;
import com.springmicroservices.repository.LicenseRepository;
import com.springmicroservices.service.interfaces.LicenseService;
import com.springmicroservices.service.interfaces.OrganizationService;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeoutException;

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

    @Override
    @CircuitBreaker(name = "licenseService", fallbackMethod = "buildFallbackLicenseList")
    @Bulkhead(name = "bulkheadLicenseService", fallbackMethod = "buildFallbackLicenseList",
            type = Bulkhead.Type.THREADPOOL)
    @Retry(name = "retryLicenseService", fallbackMethod = "buildFallbackLicenseList")
    @RateLimiter(name = "licenseService", fallbackMethod = "buildFallbackLicenseList")
    public List<License> getLicensesByOrganization(String organizationId) {
        System.out.println("getLicensesByOrganization Correlation: {}");
        randomlyRunLong();
        return licenseRepository.findByOrganizationId(organizationId);
    }

    private List<License> buildFallbackLicenseList(String organizationId, Throwable t) {
        List<License> fallbackList = new ArrayList<>();

        License license = new License();
        license.setLicenseId("0000000-00-00000");
        license.setOrganizationId(organizationId);
        license.setProductName("Sorry no licensing information currently available");
        fallbackList.add(license);
        return fallbackList;
    }

    private void randomlyRunLong() {
        Random random = new Random();
        int randomInt = random.nextInt(3) + 1;
        if (randomInt == 3) {
            try {
                Thread.sleep(5000);
                throw new TimeoutException();
            } catch (InterruptedException | TimeoutException e) {
                throw new RuntimeException(e);
            }
        }
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