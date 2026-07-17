/*
 * Copyright (c) 2022.
 * Bilol Boydedayev
 */

package com.edatasite.workforce.gwt.core.server.app.hmrc.service.impl;

import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsVatReturn;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.uk.FraudPreventionData;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.uk.UKVatReturn;
import com.edatasite.workforce.gwt.core.client.rpc.hmrc.HmrcMtdService;
import com.edatasite.workforce.gwt.core.server.app.hmrc.dto.*;
import com.edatasite.workforce.gwt.core.server.app.hmrc.service.HmrcAuthService;
import com.edatasite.workforce.gwt.core.server.app.hmrc.service.HmrcMtdServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.hmrc.service.HmrcUserCredentialsService;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.vatreturn.VatReturnManager;
import com.edatasite.workforce.utils.EdsContextParams;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.edatasite.workforce.gwt.core.server.app.hmrc.constants.HmrcConstants.*;

@Service("hmrcMtdService")
public class HmrcMtdServiceImpl implements HmrcMtdService, HmrcMtdServiceLocal {
    private static final Logger log = LoggerFactory.getLogger(HmrcMtdServiceImpl.class);
    @Autowired
    private RestTemplate hmrcRestTemplate;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private VatReturnManager vatReturnManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private HmrcAuthService hmrcAuthService;
    @Autowired
    private HmrcUserCredentialsService hmrcUserCredentialsService;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @Override
    public String checkVatNumber(String vatNumber) {
        String URL = EdsContextParams.getHmrcEndpointDomain() + VRN_LOOKUP_URL.replace("${vrn}", vatNumber);

        try {
            CheckVatNumberDTO checkVatNumberDTO = hmrcRestTemplate.getForObject(URL, CheckVatNumberDTO.class);
            return checkVatNumberDTO.getTarget().getName();
        } catch (HttpClientErrorException clientErrorException) {
            if (HttpStatus.BAD_REQUEST.equals(clientErrorException.getStatusCode())) {
                return "Invalid VRN - VRN parameters should be 9 or 12 digits";
            } else if (HttpStatus.NOT_FOUND.equals(clientErrorException.getStatusCode())) {
                return "Invalid VRN - VRN does not match a registered company";
            }
        }
        return null;
    }

    @Transactional
    public void loadVatReturnsFromHMRC(FraudPreventionData fraudPreventionData) {
        List<VatObligationsDTO> vatObligations = retrieveVatObligations(fraudPreventionData);
        if (vatObligations != null) {
            for (VatObligationsDTO vatObligation : vatObligations) {
                try {
                    EdsVatReturn vatReturn = vatReturnManager.findByPeriodKey(vatObligation.getPeriodKey()).orElse(new EdsVatReturn());

                    if (EdsVatReturn.FILED_SHORT.equalsIgnoreCase(vatObligation.getStatus())) {
                        vatReturn.setStatus(referenceManager.findReference(EdsVatReturn.VAT_RETURN_STATUS, EdsVatReturn.FILED));
                    } else if (EdsVatReturn.OPEN_SHORT.equalsIgnoreCase(vatObligation.getStatus())) {
                        vatReturn.setStatus(referenceManager.findReference(EdsVatReturn.VAT_RETURN_STATUS, EdsVatReturn.OPEN));
                    }
                    if (vatObligation.getStart() != null) {
                        vatReturn.setFromDate(simpleDateFormat.parse(vatObligation.getStart()));
                    }
                    if (vatObligation.getEnd() != null) {
                        vatReturn.setToDate(simpleDateFormat.parse(vatObligation.getEnd()));
                    }
                    if (vatObligation.getReceived() != null) {
                        vatReturn.setFiledOn(simpleDateFormat.parse(vatObligation.getReceived()));
                    }
                    if (vatObligation.getDue() != null) {
                        vatReturn.setDueDate(simpleDateFormat.parse(vatObligation.getDue()));
                    }
                    vatReturn.setPeriodKey(vatObligation.getPeriodKey());
                    vatReturnManager.create(vatReturn);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public VatReturnResponseDTO submitVatReturnForPeriod(UKVatReturn ukVatReturn, EdsVatReturn edsVatReturn, FraudPreventionData fraudPreventionData) {
        VatReturnDTO vatReturnDTO = getVatReturnDTO(ukVatReturn, edsVatReturn);

        HmrcUserCredentialsDTO hmrcUserCredentials = Optional.ofNullable(hmrcUserCredentialsService.getCredentials()).orElseThrow(() -> new RuntimeException("No Settings found"));
        try {
            EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();

            String URL = EdsContextParams.getHmrcEndpointDomain() + VAT_RETURNS_URL.replace("{vrn}", financialSettings.getTaxIdNumber());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Accept", "application/vnd.hmrc.1.0+json");
            httpHeaders.add("Authorization", "Bearer " + hmrcUserCredentials.getAccessToken());
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            fillInFphHeaders(httpHeaders, fraudPreventionData);

            ResponseEntity<VatReturnResponseDTO> responseEntity = restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<>(vatReturnDTO, httpHeaders), VatReturnResponseDTO.class);
            if (responseEntity.hasBody()) {
                return responseEntity.getBody();
            }
        } catch (HttpClientErrorException httpClientErrorException) {
            if (HttpStatus.UNAUTHORIZED.equals(httpClientErrorException.getStatusCode())) {
                HmrcUserCredentialsDTO hmrcUserCredentialsDTO = hmrcAuthService.exchangeToken(hmrcUserCredentials.getRefreshToken(), true);
                hmrcUserCredentialsService.saveCredentials(hmrcUserCredentialsDTO);
                return submitVatReturnForPeriod(ukVatReturn, edsVatReturn, fraudPreventionData);
            }

            throw new RuntimeException(httpClientErrorException.getMessage(), httpClientErrorException.getCause());
        }
        return null;
    }

    private VatReturnDTO getVatReturnDTO(UKVatReturn ukVatReturn, EdsVatReturn edsVatReturn) {
        VatReturnDTO vatReturnDTO = new VatReturnDTO();
        vatReturnDTO.setPeriodKey(edsVatReturn.getPeriodKey());
        vatReturnDTO.setVatDueSales(ukVatReturn.getVatOnSales());
        vatReturnDTO.setTotalVatDue(ukVatReturn.getVatOnSales());
        vatReturnDTO.setVatReclaimedCurrPeriod(ukVatReturn.getVatOnPurchase());
        vatReturnDTO.setNetVatDue(ukVatReturn.getPayableOrReclaimableTax());
        vatReturnDTO.setTotalValueSalesExVAT(ukVatReturn.getTotalSales());
        vatReturnDTO.setTotalValuePurchasesExVAT(ukVatReturn.getTotalPurchase());
        return vatReturnDTO;
    }

    @Override
    public List<VatObligationsDTO> retrieveVatObligations(FraudPreventionData fraudPreventionData) {
        HmrcUserCredentialsDTO hmrcUserCredentials = Optional.ofNullable(hmrcUserCredentialsService.getCredentials()).orElseThrow(() -> new RuntimeException("No Settings found"));
        try {
            EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
            String URL = EdsContextParams.getHmrcEndpointDomain() + VAT_OBLIGATIONS_URL.replace("{vrn}", financialSettings.getTaxIdNumber());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Accept", "application/vnd.hmrc.1.0+json");
            httpHeaders.add("Authorization", "Bearer " + hmrcUserCredentials.getAccessToken());
            URL = UriComponentsBuilder.fromHttpUrl(URL)
                    .queryParam("from", dateTimeFormatter.format(LocalDate.now().minusYears(1)))
                    .queryParam("to", dateTimeFormatter.format(LocalDate.now()))
                    .toUriString();

            fillInFphHeaders(httpHeaders, fraudPreventionData);

            ResponseEntity<VatObligationsResponseDTO> responseEntity = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<>(null, httpHeaders), VatObligationsResponseDTO.class);
            if (responseEntity.hasBody()) {
                return responseEntity.getBody().getObligations();
            }
        } catch (HttpClientErrorException httpClientErrorException) {
            if (HttpStatus.UNAUTHORIZED.equals(httpClientErrorException.getStatusCode())) {
                HmrcUserCredentialsDTO hmrcUserCredentialsDTO = hmrcAuthService.exchangeToken(hmrcUserCredentials.getRefreshToken(), true);
                hmrcUserCredentialsService.saveCredentials(hmrcUserCredentialsDTO);
                return retrieveVatObligations(fraudPreventionData);
            }

            throw new RuntimeException(httpClientErrorException.getMessage(), httpClientErrorException.getCause());
        }
        return null;
    }

    private void fillInFphHeaders(HttpHeaders headers, FraudPreventionData fraudPreventionData) {
        fillServerHeaders(fraudPreventionData);
        HashMap<String, String> fphHeaders = fraudPreventionData.getValuesAsMap();
        fphHeaders.forEach(headers::add);
    }

    private void fillServerHeaders(FraudPreventionData fraudPreventionData) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        fraudPreventionData.setGovClientPublicPort(attributes.getRequest().getRemotePort());
        try {
            InetAddress address = InetAddress.getByName(EdsContextParams.getHostname());
            fraudPreventionData.setGovVendorPublicIP(address.getHostAddress());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        Optional.ofNullable(vatReturnManager.getUser()).ifPresent(edsUser -> {
            try {
                String email = "kpi=" + URLEncoder.encode(edsUser.getEmail(), "UTF-8");
                String id = "kpi=" + URLEncoder.encode(String.valueOf(edsUser.getObjectID()), "UTF-8");
                fraudPreventionData.setGovClientUserIDs(String.join("&", id, email));
                fraudPreventionData.setGovVendorProductName(URLEncoder.encode(EdsContextParams.getProductName(), "UTF-8"));

                String serverPublicIp = restTemplate.getForObject("https://checkip.amazonaws.com/", String.class);

                String firstForwarding = String.format("by=%s&for=%s", URLEncoder.encode(fraudPreventionData.getGovVendorPublicIP(), "UTF-8"), URLEncoder.encode(fraudPreventionData.getGovClientPublicIP(), "UTF-8"));
                String secondForwarding = String.format("by=%s&for=%s", URLEncoder.encode(serverPublicIp.trim(), "UTF-8"), URLEncoder.encode(fraudPreventionData.getGovVendorPublicIP(), "UTF-8"));
                fraudPreventionData.setGovVendorForwarded(String.join(",", firstForwarding, secondForwarding));
            } catch (UnsupportedEncodingException e) {
                log.error("Cannot encode fph headers", e);
            }
        });
    }
}
