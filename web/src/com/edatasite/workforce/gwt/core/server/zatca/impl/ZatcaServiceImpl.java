package com.edatasite.workforce.gwt.core.server.zatca.impl;

import com.edatasite.workforce.appContext.SpringPropertiesUtil;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsZatcaSettings;
import com.edatasite.workforce.core.domain.enums.ZatcaState;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ZatcaSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.settings.CompanySettingsManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.zatca.KSAEInvoiceService;
import com.edatasite.workforce.gwt.core.server.zatca.ZatcaService;
import com.edatasite.workforce.gwt.core.server.zatca.configuration.ZatcaServiceGenerator;
import com.edatasite.workforce.gwt.core.server.zatca.service.ZatcaInitService;
import com.edatasite.workforce.gwt.core.server.zatca.service.ZatcaInvoiceService;
import com.edatasite.workforce.gwt.core.server.zatca.service.dto.*;
import com.edatasite.workforce.gwt.core.server.zatca.service.errors.ZatcaException;
import com.google.gson.Gson;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import retrofit2.Response;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Service
public class ZatcaServiceImpl implements ZatcaService {

    private static final Logger log = LoggerFactory.getLogger(ZatcaServiceImpl.class);

    @Autowired
    private KSAEInvoiceService ksaInvoiceService;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private ZatcaSettingsManager zatcaSettingsManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private CompanySettingsManager companyManager;

    @Transactional
    public void initSettings() throws ZatcaException {
        EdsZatcaSettings zatcaSettings = zatcaSettingsManager.getZatcaSettings();
        if (zatcaSettings != null) {
            throw new ZatcaException("Zatca is configured");
        }
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        EdsCompanySettings companySettings = companyManager.getCompanySettings(SecurityContext.getCompanyID());
        // 310568043800003 -> 4030379626 for test
        //todo change with real data
        SettingsRequest request = new SettingsRequest();
        request.setCommonName("KPI-" + financialSettings.getTaxIdNumber());
        request.setSerialNumber("1-KPI|2-123|3-93fda1de-800d-4265-9207-16e971bd8ef6");
        request.setOrganizationIdentifier(financialSettings.getTaxIdNumber());
        request.setOrganizationUnitName(financialSettings.getTinNumber());
        request.setOrganizationName(companySettings.getName());
        request.setCountryName("SA");
        request.setInvoiceType("1100");
        request.setLocation("https:/kpi.com");
        request.setIndustry("ERP System");
        request.setOtpNumber(financialSettings.getOtpNumber());
        System.out.println("=================================================== Zatca Retrofit base url start ===================================================");
        System.out.println(SpringPropertiesUtil.getProperty("kpi.zatca.url"));
        System.out.println("=================================================== Zatca Retrofit base url end ===================================================");
        ZatcaInitService initService = ZatcaServiceGenerator.createService(ZatcaInitService.class);

        SettingsResponse settingsResponse = null;
        try {
            Response<SettingsResponse> response = initService.init(request).execute();
            if (response.isSuccessful()) {
                settingsResponse = response.body();
            } else {
                String error = null;
                if (response.errorBody() != null) {
                    error = response.errorBody().string();
                    System.out.println("============== " + error + " ==============");
                }
                throw new ZatcaException(error);
            }
        } catch (IOException e) {
            throw new ZatcaException(e);
        }

        if (settingsResponse == null) {
            throw new ZatcaException("Zatca Onboarding Compliance CSID response is null");
        }

        zatcaSettings = new EdsZatcaSettings();
        zatcaSettings.setCsr(settingsResponse.getCsr());
        zatcaSettings.setCertificate(settingsResponse.getCertificate());
        zatcaSettings.setPrivateKey(settingsResponse.getPrivateKey());
        zatcaSettings.setComplianceSecurityToken(settingsResponse.getComplianceSecurityToken());
        zatcaSettings.setComplianceSecret(settingsResponse.getComplianceSecret());
        zatcaSettings.setSecurityToken(settingsResponse.getSecurityToken());
        zatcaSettings.setSecret(settingsResponse.getSecret());
        zatcaSettings.setState(ZatcaState.ACTIVE);
        zatcaSettingsManager.create(zatcaSettings);
        System.out.println("Zatca settings created successfully settings id ===============> " + zatcaSettings.getObjectID());
    }

    @Transactional
    public void renewSettings() throws ZatcaException {
        EdsZatcaSettings oldZatcaSettings = zatcaSettingsManager.getZatcaSettings();
        if (oldZatcaSettings == null) {
            initSettings();
            return;
        }

        ZatcaInitService renewService = ZatcaServiceGenerator.createService(ZatcaInitService.class);

        RenewRequest renewRequest = new RenewRequest();
        byte[] bytes = oldZatcaSettings.getCsr().getBytes(StandardCharsets.UTF_8);
        String csrBase64 = Base64.getEncoder().encodeToString(bytes);
        renewRequest.setCsr(csrBase64);
        RenewResponse renewResponse = null;
        try {
            Response<RenewResponse> response = renewService.renew(renewRequest).execute();
            if (response.isSuccessful()) {
                renewResponse = response.body();
            }
            else {
                String error = null;
                if (response.errorBody() != null) {
                    error = response.errorBody().string();
                }
                throw new ZatcaException(error);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (renewResponse == null) {
            throw new ZatcaException("Zatca Renewal Compliance CSID response is null");
        }

        oldZatcaSettings.setState(ZatcaState.REVOKED);
        zatcaSettingsManager.update(oldZatcaSettings);

        EdsZatcaSettings newZatcaSettings = new EdsZatcaSettings();
        newZatcaSettings.setCsr(oldZatcaSettings.getCsr());
        newZatcaSettings.setCertificate(oldZatcaSettings.getCertificate());
        newZatcaSettings.setPrivateKey(oldZatcaSettings.getPrivateKey());
        newZatcaSettings.setComplianceSecurityToken(oldZatcaSettings.getComplianceSecurityToken());
        newZatcaSettings.setComplianceSecret(oldZatcaSettings.getComplianceSecret());
        newZatcaSettings.setSecurityToken(renewResponse.getSecurityToken());
        newZatcaSettings.setSecret(renewResponse.getSecret());
        newZatcaSettings.setState(ZatcaState.ACTIVE);
        zatcaSettingsManager.create(newZatcaSettings);
    }

    @Transactional
    public String complianceInvoice(Integer invoiceID, String type) throws ZatcaException {
        EdsZatcaSettings zatcaSettings = zatcaSettingsManager.getZatcaSettings();
        if (zatcaSettings == null) {
            throw new ZatcaException("Zatca is not configured");
        }

        EdsInvoice saleInvoice = invoiceManager.get(invoiceID);
        InvoiceRequest invoiceRequest = null;
        try {
            invoiceRequest = ksaInvoiceService.generateInvoiceDtoItem(invoiceID, type);
        } catch (ZatcaException e) {
            throw new RuntimeException(e);
        }

        if (invoiceRequest == null) {
            throw new ZatcaException("Cannot generate invoice dto");
        }
        invoiceRequest.setCertificate(zatcaSettings.getCertificate());
        invoiceRequest.setPrivateKey(zatcaSettings.getPrivateKey());
        invoiceRequest.setSecurityToken(zatcaSettings.getComplianceSecurityToken());
        invoiceRequest.setSecret(zatcaSettings.getComplianceSecret());

        ZatcaInvoiceService complianceInvoiceService = ZatcaServiceGenerator.createService(ZatcaInvoiceService.class);

        InvoiceResponse complianceResponse = null;
        try {
            Response<InvoiceResponse> response = complianceInvoiceService.compliance(invoiceRequest).execute();
            if (response.isSuccessful()) {
                complianceResponse = response.body();
            } else {
                InvoiceResponse error = null;
                if (response.errorBody() != null) {
                    error = new Gson().fromJson(response.errorBody().charStream(), InvoiceResponse.class);
                }
                throw new ZatcaException(new Gson().toJson(error));
            }
        } catch (IOException e) {
            throw new ZatcaException(e);
        }

        if (complianceResponse == null) {
            throw new ZatcaException("Cannot compliance invoice through Zatca");
        }

        if ("CLEARED".equals(complianceResponse.getClearanceStatus())) {
            byte[] bytes = complianceResponse.getClearedInvoice().getBytes(StandardCharsets.UTF_8);
            byte[] decodedBytes = Base64.getDecoder().decode(bytes);
            String clearedInvoiceXml = new String(decodedBytes);

            saleInvoice.setZatcaHash(complianceResponse.getInvoiceHash());
            saleInvoice.setZatcaXml(clearedInvoiceXml);
            saleInvoice.setZatcaStatus(complianceResponse.getClearanceStatus());
            saleInvoice.setZatcaQRCode(complianceResponse.getQrCode());
            saleInvoice.setReportedDate(new Date());
            invoiceManager.update(saleInvoice);
            return "Invoice is cleared " + saleInvoice.getZatcaHash();
        }

        throw new ZatcaException("Cannot clear invoice through Zatca");
    }

    @Transactional
    public String clearanceInvoice(Integer invoiceID, String type) throws ZatcaException {
        EdsZatcaSettings zatcaSettings = zatcaSettingsManager.getZatcaSettings();
        if (zatcaSettings == null) {
            throw new ZatcaException("Zatca is not configured");
        }

        EdsInvoice saleInvoice = invoiceManager.get(invoiceID);
        InvoiceRequest request = null;
        try {
            request = ksaInvoiceService.generateInvoiceDtoItem(invoiceID, type);
        } catch (ZatcaException e) {
            throw new RuntimeException(e);
        }

        if (request == null) {
            throw new ZatcaException("Cannot generate invoice dto");
        }
        request.setCertificate(zatcaSettings.getCertificate());
        request.setPrivateKey(zatcaSettings.getPrivateKey());
        request.setSecurityToken(zatcaSettings.getSecurityToken());
        request.setSecret(zatcaSettings.getSecret());

        ZatcaInvoiceService clearanceService = ZatcaServiceGenerator.createService(ZatcaInvoiceService.class);
        InvoiceResponse clearanceResponse = null;
        try {
            Response<InvoiceResponse> response = clearanceService.clearance(request).execute();
            if (response.isSuccessful()) {
                clearanceResponse = response.body();
            }
            else {
                String error = null;
                if (response.errorBody() != null) {
                    error = response.errorBody().string();
                }
                throw new ZatcaException(error);
            }
        } catch (IOException e) {
            throw new ZatcaException(e);
        }

        if (clearanceResponse == null) {
            throw new ZatcaException("Cannot clear invoice through Zatca");
        }

        if ("CLEARED".equals(clearanceResponse.getClearanceStatus())) {
            byte[] bytes = clearanceResponse.getClearedInvoice().getBytes(StandardCharsets.UTF_8);
            byte[] decodedBytes = Base64.getDecoder().decode(bytes);
            String clearedInvoiceXml = new String(decodedBytes);

            saleInvoice.setZatcaHash(clearanceResponse.getInvoiceHash());
            saleInvoice.setZatcaXml(clearedInvoiceXml);
            saleInvoice.setZatcaStatus(clearanceResponse.getClearanceStatus());
            saleInvoice.setZatcaQRCode(clearanceResponse.getQrCode());
            saleInvoice.setReportedDate(new Date());
            invoiceManager.update(saleInvoice);
            return "Invoice is cleared " + clearanceResponse.getInvoiceHash();
        }

        throw new ZatcaException("Cannot clear invoice through Zatca");
    }

    @Override
    public String getZatcaSettingsStatus() {
        EdsZatcaSettings zatcaSettings = zatcaSettingsManager.getZatcaSettings();
        return zatcaSettings != null ? zatcaSettings.getState().name() : null;
    }
}
