package com.springmicroservices.controller;

import java.util.List;
import java.util.Locale;

import com.springmicroservices.model.License;
import com.springmicroservices.service.interfaces.LicenseService;
import com.springmicroservices.utils.usercontext.UserContextHolder;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "v1/organization/{organizationId}/license")
@AllArgsConstructor
public class LicenseController {

    private final Logger logger = LoggerFactory.getLogger(LicenseController.class);

    private LicenseService licenseService;

    @GetMapping("/{licenseId}")
    public ResponseEntity<License> getLicense(@PathVariable String organizationId,
                                              @PathVariable String licenseId,
                                              @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok(licenseService.getLicense(licenseId, organizationId, "", locale));
    }

    @GetMapping
    public ResponseEntity<List<License>> getLicensesByOrganizationId(@PathVariable String organizationId) {

        logger.debug("LicenseController Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
        return ResponseEntity.ok(licenseService.getLicensesByOrganization(organizationId));
    }

    @GetMapping(value = "/{licenseId}/{clientType}")
    public License getLicensesWithClient(@PathVariable("organizationId") String organizationId,
                                         @PathVariable("licenseId") String licenseId,
                                         @PathVariable("clientType") String clientType,
                                         @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        return licenseService.getLicense(licenseId, organizationId, clientType, locale);
    }

    @PutMapping
    public ResponseEntity<License> updateLicense(@PathVariable String organizationId,
                                                 @RequestBody License license,
                                                 @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok(licenseService.updateLicense(license, locale));
    }

    @PostMapping
    public ResponseEntity<License> createLicense(@PathVariable String organizationId,
                                                 @RequestBody License license,
                                                 @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok(licenseService.createLicense(license, locale));
    }

    @DeleteMapping("/{licenseId}")
    public ResponseEntity<String> deleteLicense(@PathVariable String organizationId,
                                                @PathVariable String licenseId,
                                                @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok(licenseService.deleteLicense(licenseId, locale));
    }
}