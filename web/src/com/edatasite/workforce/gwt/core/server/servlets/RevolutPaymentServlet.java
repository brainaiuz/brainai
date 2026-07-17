package com.edatasite.workforce.gwt.core.server.servlets;

import com.edatasite.workforce.core.domain.EdsInvoicingSettings;
import com.edatasite.workforce.core.domain.EdsUsagePlan;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.social.revolut.dto.RevolutPaymentCallbackDto;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ReceivePaymentData;
import com.edatasite.workforce.gwt.profile.client.rpc.RevolutFeeDto;
import com.edatasite.workforce.gwt.profile.client.rpc.RevolutOrderDto;
import com.edatasite.workforce.gwt.profile.client.rpc.RevolutPaymentDto;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RevolutPaymentServlet extends PaymentCallbackServlet {

    private final RestTemplate restTemplate = new RestTemplate();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String company = req.getParameter("company");
        if (StringUtils.isNotBlank(company)) {
            SecurityContext.getInstance().setCompanyId(company);
        } else {
            return;
        }
        String user = req.getParameter("user");
        if (StringUtils.isNotBlank(user)) {
            SecurityContext.getInstance().setStaticUserID(Integer.valueOf(user));
        } else {
            return;
        }
        String schemaType = req.getParameter("schema");
        if (StringUtils.isNotBlank(schemaType)) {
            SecurityContext.getInstance().setDatabase(schemaType);
        } else {
            return;
        }

        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new FormHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter());
        //Add the Jackson Message converter
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        // Note: here we are making this converter to process any kind of response,
        // not only application/*json, which is the default behaviour
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);

        Gson gson = new Gson();
        BufferedReader reader = req.getReader();
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        RevolutPaymentCallbackDto response = gson.fromJson(stringBuilder.toString(), RevolutPaymentCallbackDto.class);

        if (response.getEvent().equals("ORDER_COMPLETED")) {
            EdsInvoicingSettings settings = invoicingSettingsManager.getInvoiceSettings(invoiceManager.getUser().getCompany());
            EdsInvoice edsInvoice = invoiceManager.getByIntegrationId(response.getOrder_id());
            updateUsagePlan(edsInvoice);
            if (settings.getRevolutSecretApiKey() == null) {
                return;
            }
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + settings.getRevolutSecretApiKey());

            String url = "https://sandbox-merchant.revolut.com/api/1.0/orders/" + response.getOrder_id();
            HttpEntity<String> entity = new HttpEntity<>("", headers);

            RevolutOrderDto orderDto = restTemplate.exchange(url, HttpMethod.GET, entity, RevolutOrderDto.class).getBody();
            if (orderDto == null || orderDto.getPayments() == null || orderDto.getPayments().isEmpty()) {
                return;
            }
            Integer feeAmount = 0;
            for (RevolutPaymentDto payment : orderDto.getPayments()) {
                if (payment.getFees() != null && !payment.getFees().isEmpty()) {
                    for (RevolutFeeDto fee : payment.getFees()) {
                        if (fee.getAmount() != null && edsInvoice.getCurrency() != null && edsInvoice.getCurrency().getName().equals(fee.getAmount().getCurrency())) {
                            feeAmount += fee.getAmount().getValue();
                        }
                    }
                }
            }
            BigDecimal fee = BigDecimal.valueOf(feeAmount).divide(BigDecimal.valueOf(100));
            if (edsInvoice != null) {
                NewInvoice invoice = invoiceService.getInvoiceSummaryData(edsInvoice.getObjectID());
                if (!Constants.PAID.equals(invoice.getStatusCode())) {

                    ReceivePaymentData receivePaymentData = new ReceivePaymentData();
                    receivePaymentData.setBatchPayment(true);
                    receivePaymentData.setCrmAccount(invoice.getTypeItem());
                    receivePaymentData.setAccount(getBankAccount(Constants.PAYMENT_TYPES.REVOLUT_BANK, invoiceManager.getUser().getCompany()));
                    receivePaymentData.setExRate(invoice.getExchageRate());
                    receivePaymentData.setCurrency(new CurrencyItem(invoice.getCurrencyID(), null, null));
                    receivePaymentData.setReference(invoice.getInvoiceNumber());
                    receivePaymentData.setDate(new DateNonConvertable());
                    receivePaymentData.setTotalAmount(invoice.getAmount().subtract(fee));
                    receivePaymentData.setValidateReferences(false);
                    receivePaymentData.setType(Constants.RECEIVABLE);
                    receivePaymentData.setPaymentTarget(AccountingConstants.PAYMENT_TARGET_INVOICE);
                    receivePaymentData.setPayments(new PaymentData[]{getPaymentData(invoice, receivePaymentData.getAccount(), invoice.getAmount().subtract(fee))});

                    invoiceService.saveReceivePaymentData(receivePaymentData, true);

                    if (feeAmount > 0) {
                        ReceivePaymentData expenseData = new ReceivePaymentData();
                        expenseData.setBatchPayment(true);
                        expenseData.setCrmAccount(invoice.getTypeItem());
                        expenseData.setAccount(getBankAccount(Constants.PAYMENT_TYPES.REVOLUT_EXPENSE, invoiceManager.getUser().getCompany()));
                        expenseData.setExRate(invoice.getExchageRate());
                        expenseData.setCurrency(new CurrencyItem(invoice.getCurrencyID(), null, null));
                        expenseData.setReference(invoice.getInvoiceNumber());
                        expenseData.setDate(new DateNonConvertable());
                        expenseData.setTotalAmount(fee);
                        expenseData.setValidateReferences(false);
                        expenseData.setType(Constants.RECEIVABLE);
                        expenseData.setPaymentTarget(AccountingConstants.PAYMENT_TARGET_INVOICE);
                        expenseData.setPayments(new PaymentData[]{getPaymentData(invoice, expenseData.getAccount(), fee)});

                        invoiceService.saveReceivePaymentData(expenseData, true);
                    }
                }
            }
        }
    }


    @Transactional
    public void updateUsagePlan(EdsInvoice invoice) {
        Integer companyID = Integer.valueOf(invoice.getReference());
        usagePlanManager.updatePaidStatus(true, referenceManager.findReference(EdsUsagePlan._PAYMENT_STATUS, EdsUsagePlan.ACTIVE).getObjectID(), companyID);
        companyManager.updateCompanyActive(true, companyID);

    }
}
