package com.springmicroservices.controller;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Random;

import com.springmicroservices.model.License;
import com.springmicroservices.service.interfaces.LicenseService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "v1/organization/{organizationId}/license")
@AllArgsConstructor
public class LicenseController {

    private LicenseService licenseService;

    @GetMapping("/{licenseId}")
    public ResponseEntity<License> getLicense(@PathVariable String organizationId,
                                              @PathVariable String licenseId,
                                              @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok(licenseService.getLicense(licenseId, organizationId, "", locale));
    }

    @RequestMapping(value = "/{licenseId}/{clientType}", method = RequestMethod.GET)
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