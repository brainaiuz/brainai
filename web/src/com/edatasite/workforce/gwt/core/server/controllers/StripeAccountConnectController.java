package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsInvoicingSettings;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsQuote;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.InvoicingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 11.12.2009
 * Time: 11:47:12
 * To change this template use File | Settings | File Templates.
 */

@Controller
public class StripeAccountConnectController implements Constants {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private InvoiceServiceLocal invoiceServiceLocal;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private InvoicingSettingsManager invoicingSettingsManager;
    @Autowired
    private QuoteManager quoteManager;

    @RequestMapping(value = "/stripe-accounting-authorize")
    protected ModelAndView stipeAuthorizeFromAccountingSetupPage(@RequestParam(value = "code", required = false) String code,
                                         @RequestParam(value = "error", required = false) String error,
                                         @RequestParam(value = "error_description", required = false) String errorDescription,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception {

        ServerUtils.fillHostParameters(request);

        if (StringUtils.isNotBlank(code)) {
            Integer invoiceSettingsId = invoiceServiceLocal.saveStripeAccount(code);
            log.info("Stripe Account Settings has been saved. InvoiceSettinsId: " + invoiceSettingsId);
        } else {
            log.info("------------------ StripeAuthorize Error: " + error + " - " + errorDescription + " ---------------------------------------");
        }

        return new ModelAndView("redirect:/Accounting.html");
    }

    @RequestMapping(value = "/stripe-authorize")
    protected ModelAndView handleRequest(@RequestParam(value = "code", required = false) String code,
                                         @RequestParam(value = "error", required = false) String error,
                                         @RequestParam(value = "error_description", required = false) String errorDescription,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception {

        ServerUtils.fillHostParameters(request);

        if (StringUtils.isNotBlank(code)) {
            Integer invoiceSettingsId = invoiceServiceLocal.saveStripeAccount(code);
            log.info("Stripe Account Settings has been saved. InvoiceSettinsId: " + invoiceSettingsId);
        } else {
            log.info("------------------ StripeAuthorize Error: " + error + " - " + errorDescription + " ---------------------------------------");
        }

        return new ModelAndView("redirect:/Settings.html#invoiceSettingsHome|invoiceSettings");
    }

    @RequestMapping(value = "/stripe-deauthorize")
    protected ModelAndView stripeDeauthorize(@RequestParam(value = "client_id", required = false) String clientId,
                                             HttpServletRequest request, HttpServletResponse response) throws Exception {

        ServerUtils.fillHostParameters(request);

        if (StringUtils.isNotBlank(clientId)) {
            EdsCompany company = invoicingSettingsManager.getUser().getCompany();
            EdsInvoicingSettings invoiceSettings = invoicingSettingsManager.getInvoiceSettings(company);
            if (invoiceSettings != null && StringUtils.isNotBlank(invoiceSettings.getStripeUserId())) {
                invoiceSettings.getStripeUserId();

                // Using Apache HttpComponents: https://hc.apache.org/
                CloseableHttpClient httpclient = null;
                try {
                    httpclient = HttpClients.createDefault();
                    HttpPost httpPost = new HttpPost("https://connect.stripe.com/oauth/deauthorize");
                    httpPost.setHeader("Authorization", "Bearer " + EdsContextParams.getStripeSecretKey());
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    if (StringUtils.isNotBlank(EdsContextParams.getStripePublicKey()) && EdsContextParams.getStripePublicKey().startsWith("pk_live_")) {
                        nvps.add(new BasicNameValuePair("client_id", "ca_EVfkOl28v40YPw8EPxaxnYSm7z8Os6oo"));
                    } else {
                        nvps.add(new BasicNameValuePair("client_id", "ca_EVfk8GBFFAo0x4BPqKgtpv43csnCmgBa"));
                    }
                    nvps.add(new BasicNameValuePair("stripe_user_id", invoiceSettings.getStripeUserId()));
                    httpPost.setEntity(new UrlEncodedFormEntity(nvps));
                    CloseableHttpResponse responseDeauth = httpclient.execute(httpPost);

                    Integer invoiceSettingsId = invoiceServiceLocal.removeStripeAccount();
                    log.info("Stripe Account Settings has been deauthorized. InvoiceSettinsId: " + company.getObjectID() + " = " + invoiceSettings.getObjectID());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (httpclient != null) {
                        httpclient.close();
                    }
                }
            }

        }

        return new ModelAndView("redirect:/Settings.html#invoiceSettingsHome|invoiceSettings");
    }

    @RequestMapping(value = "/stripe-payment.html")
    protected ModelAndView collectCustomerData(@RequestParam(value = "customtoken", required = false) String customtoken,
                                               HttpServletRequest request, HttpServletResponse response) throws Exception {

        ServerUtils.fillHostParameters(request);
        ModelAndView modelAndView = new ModelAndView("stripe-payment");
        modelAndView.addObject("customtoken", customtoken);

        if (StringUtils.isNotBlank(EdsContextParams.getStripePublicKey())) {
            modelAndView.addObject("stripe_public_key", EdsContextParams.getStripePublicKey());
//            log.info(EdsContextParams.getStripePublicKey());
//            log.info("pk_test_1TTzcqsxivyda68MwsgYd28g");
        } else {
            modelAndView.addObject("error", "Stripe not configured");
            return modelAndView;
        }

        if (StringUtils.isNotBlank(customtoken)) {
            Integer companyID = null;
            Integer invoiceID = null;
            String databaseType = null;
            boolean isInvoice = true;
            try {
                String decryptedData = EncryptionHelper.decryptURL(customtoken.substring(1));
                String[] idArray = decryptedData.split("_");//companyid,invoiceid
                companyID = Integer.valueOf(idArray[0]);
                databaseType = idArray[1];
                invoiceID = Integer.valueOf(idArray[2]);
                if (idArray.length > 3) {
                    isInvoice = Boolean.parseBoolean(idArray[3]);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            if (companyID == null || companyID <= 0) {
                log.info("PAYPAL_INCOMPATIBLE_COMPANY_ID:" + companyID);
            }
            if (databaseType == null) {
                log.info("PAYPAL_DATABASE_TYPE_IS_NULL");
            }
            if (invoiceID == null || invoiceID <= 0) {
                log.info("PAYPAL_INCOMPATIBLE_INVOICE_ID:" + invoiceID);
            }

            log.info("PAYPAL_COMPANY_ID:" + companyID);
            log.info("PAYPAL_INVOICE_ID:" + invoiceID);
            log.info("PAYPAL_DATABASE_TYPE:" + databaseType);

            ServerSecurityContext.getInstance().setCompanyId(companyID);
            ServerSecurityContext.getInstance().setDatabase(databaseType);

            log.info("PAYPAL_DATABASE_TYPE_AFTER_SET:" + ServerSecurityContext.getInstance().getDatabase());

            if (isInvoice) {
                EdsInvoice invoice = invoiceManager.get(invoiceID);
                if (invoice == null) {
                    return modelAndView;
                }
                NewInvoice invoiceData = EdsInvoice.getInvoiceData(invoice);
                String invoiceCurrency = invoiceData.getCurrencyName();

                BigDecimal amount = invoice.getDueAmount().setScale(2, RoundingMode.HALF_UP);

                modelAndView.addObject("dueamount", amount);
                modelAndView.addObject("exchange_rate", invoice.getExchangeRate());
                modelAndView.addObject("totalininvoicecurrency", invoice.getDueAmount());
                modelAndView.addObject("invoicenumber", invoiceData.getInvoiceNumber());
                if (StringUtils.isNotBlank(invoiceCurrency)) {
                    modelAndView.addObject("currency", invoiceCurrency);
                }
            } else {
                EdsQuote edsQuote = quoteManager.get(invoiceID);
                if (edsQuote == null) {
                    return modelAndView;
                }
                NewInvoice invoiceData = EdsQuote.getQuoteData(edsQuote);
                String invoiceCurrency = invoiceData.getCurrencyName();

                BigDecimal amount = edsQuote.getTotal().setScale(2, RoundingMode.HALF_UP);

                modelAndView.addObject("dueamount", amount);
                modelAndView.addObject("exchange_rate", edsQuote.getExchangeRate());
                modelAndView.addObject("totalininvoicecurrency", edsQuote.getTotal());
                modelAndView.addObject("invoicenumber", invoiceData.getInvoiceNumber());
                if (StringUtils.isNotBlank(invoiceCurrency)) {
                    modelAndView.addObject("currency", invoiceCurrency);
                }
            }
        } else {
            log.info("------------------ Custom Token is empty: " + customtoken + " ---------------------------------------");
            modelAndView.addObject("error", "Invoice not found");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/stripe-charge")
    protected ModelAndView createCharge(@RequestParam(value = "customtoken", required = false) String customtoken,
                                        @RequestParam(value = "stripeToken", required = false) String stripeToken,
                                        @RequestParam(value = "stripeTokenType", required = false) String stripeTokenType,
                                        @RequestParam(value = "stripeEmail", required = false) String stripeEmail,
                                        HttpServletRequest request, HttpServletResponse response) throws Exception {

        ServerUtils.fillHostParameters(request);

        ModelAndView modelAndView = new ModelAndView("stripe-charge");
        modelAndView.addObject("customtoken", customtoken);
        if (StringUtils.isNotBlank(EdsContextParams.getStripePublicKey())) {
            modelAndView.addObject("stripe_public_key", EdsContextParams.getStripePublicKey());
//            log.info(EdsContextParams.getStripePublicKey());
//            log.info("pk_test_1TTzcqsxivyda68MwsgYd28g");
        }

        if (StringUtils.isNotBlank(customtoken) && StringUtils.isNotBlank(stripeToken)) {
            Integer companyID = null;
            Integer invoiceID = null;
            String databaseType = null;
            boolean isInvoice = true;
            try {
                String decryptedData = EncryptionHelper.decryptURL(customtoken.substring(1));
                String[] idArray = decryptedData.split("_");//companyid,invoiceid
                companyID = Integer.valueOf(idArray[0]);
                databaseType = idArray[1];
                invoiceID = Integer.valueOf(idArray[2]);
                isInvoice = Boolean.parseBoolean(idArray[3]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            if (companyID == null || companyID <= 0) {
                log.info("PAYPAL_INCOMPATIBLE_COMPANY_ID:" + companyID);
            }
            if (databaseType == null) {
                log.info("PAYPAL_DATABASE_TYPE_IS_NULL");
            }
            if (invoiceID == null || invoiceID <= 0) {
                log.info("PAYPAL_INCOMPATIBLE_INVOICE_ID:" + invoiceID);
            }

            log.info("PAYPAL_COMPANY_ID:" + companyID);
            log.info("PAYPAL_INVOICE_ID:" + invoiceID);
            log.info("PAYPAL_DATABASE_TYPE:" + databaseType);

            ServerSecurityContext.getInstance().setCompanyId(companyID);
            ServerSecurityContext.getInstance().setDatabase(databaseType);

            log.info("PAYPAL_DATABASE_TYPE_AFTER_SET:" + ServerSecurityContext.getInstance().getDatabase());


            String companyStripeAccount = null;
            EdsCompany company = companyManager.get(companyID);
            EdsInvoicingSettings invSettings = invoicingSettingsManager.getInvoiceSettings(company);
            if (invSettings != null && invSettings.getStripeUserId() != null) {
                companyStripeAccount = invSettings.getStripeUserId();
            }

            if (StringUtils.isBlank(companyStripeAccount)) {
                modelAndView.addObject("error", "Stripe not configured for your company.");
                return modelAndView;
            }

            // Set your secret key: remember to change this to your live secret key in production
            // See your keys here: https://dashboard.stripe.com/account/apikeys
            Stripe.apiKey = EdsContextParams.getStripeSecretKey();//"sk_test_LjJzzD0OQE9RydPRgocD5oQf";

            HashMap<String, Object> genericMetadata = new HashMap<>();
            genericMetadata.put("company_id", SecurityContext.getCompanyID());
            genericMetadata.put("dbtype", databaseType);
            genericMetadata.put("invoice_id", invoiceID);

            Map<String, Object> params = new HashMap<String, Object>();

            if (isInvoice) {
                EdsInvoice invoice = invoiceManager.get(invoiceID);
                if (invoice == null) {
                    return modelAndView;
                }

                NewInvoice invoiceData = EdsInvoice.getInvoiceData(invoice);
                String invoiceCurrency = invoiceData.getCurrencyName();

                BigDecimal amount = invoice.getDueAmount().setScale(2, RoundingMode.HALF_UP);

                modelAndView.addObject("dueamount", amount);
                modelAndView.addObject("exchange_rate", invoice.getExchangeRate());
                modelAndView.addObject("totalininvoicecurrency", invoice.getDueAmount());
                modelAndView.addObject("invoicenumber", invoiceData.getInvoiceNumber());
                if (StringUtils.isNotBlank(invoiceCurrency)) {
                    modelAndView.addObject("currency", invoiceCurrency);
                }

                params.put("amount", invoice.getDueAmount().multiply(new BigDecimal("100")).intValueExact());
                params.put("currency", invoiceCurrency);
                params.put("description", "Invoice payment for " + invoiceData.getInvoiceNumber());

            } else {
                EdsQuote edsQuote = quoteManager.get(invoiceID);
                if (edsQuote == null) {
                    return modelAndView;
                }

                NewInvoice invoiceData = EdsQuote.getQuoteData(edsQuote);
                String invoiceCurrency = invoiceData.getCurrencyName();

                BigDecimal amount = edsQuote.getTotal().setScale(2, RoundingMode.HALF_UP);

                modelAndView.addObject("dueamount", amount);
                modelAndView.addObject("exchange_rate", edsQuote.getExchangeRate());
                modelAndView.addObject("totalininvoicecurrency", edsQuote.getTotal());
                modelAndView.addObject("invoicenumber", invoiceData.getInvoiceNumber());
                if (StringUtils.isNotBlank(invoiceCurrency)) {
                    modelAndView.addObject("currency", invoiceCurrency);
                }

                params.put("amount", edsQuote.getTotal().multiply(new BigDecimal("100")).intValueExact());
                params.put("currency", invoiceCurrency);
                params.put("description", "Invoice payment for " + invoiceData.getInvoiceNumber());
            }

            params.put("source", stripeToken);
            params.put("metadata", genericMetadata);

            RequestOptions requestOptions = RequestOptions.builder().setStripeAccount(companyStripeAccount).build();

            try {
                Charge charge = Charge.create(params, requestOptions);

                /*Map<String, Object> destinationParams = new HashMap<String, Object>();
                destinationParams.put("account", companyStripeAccount);
                params.put("destination", destinationParams);
                Charge charge = Charge.create(params);*/

                if (charge != null && StringUtils.isNotBlank(charge.getStatus()) && charge.getStatus().toLowerCase().contains("succeeded")) {
                    log.info("Stripe Invoice payment for companyid={} invoiceid={} charge:{} ", companyID, invoiceID, charge.toJson());
                    //Transaction completed successfully
                    modelAndView.addObject("message", """
                                                Your payment has <br>
                                                been successfully <br>
                                                processed."""/*charge.getStatus()*/);

                    PaymentData paymentData = new PaymentData();
                    if (isInvoice) {
                        EdsInvoice invoice = invoiceManager.get(invoiceID);
                        if (invoice == null) {
                            return modelAndView;
                        }
                        paymentData.setInvoiceID(invoiceID);
                        paymentData.setPaymentAmount(invoice.getDueAmount());

                        paymentData.setDate(new DateNonConvertable(new Date()));
                        paymentData.setValidateReference(false);
                        paymentData.setExchangeRate(invoice.getExchangeRate());
                        paymentData.setType(Constants.RECEIVABLE);
                        paymentData.setTotal(invoice.getDueAmount());
                        paymentData.setCurrency(new SelectItem(invoice.getCurrency().getObjectID()));
                    } else {
                        EdsQuote edsQuote = quoteManager.get(invoiceID);
                        if (edsQuote == null) {
                            return modelAndView;
                        }
                        paymentData.setInvoiceID(invoiceID);
                        paymentData.setPaymentAmount(edsQuote.getTotal());

                        paymentData.setDate(new DateNonConvertable(new Date()));
                        paymentData.setValidateReference(false);
                        paymentData.setExchangeRate(edsQuote.getExchangeRate());
                        paymentData.setType(Constants.RECEIVABLE);
                        paymentData.setTotal(edsQuote.getTotal());
                    }
                    invoiceServiceLocal.saveGatewayPaymentData(paymentData, STRIPE_PAYMENT);
                } else {
                    //Error occured during the charge
                    modelAndView.addObject("error", "Stripe: " + (charge != null ? charge.getStatus() : ""));
                }
            } catch (StripeException e) {
                log.error("", e);
                modelAndView.addObject("error", e.getMessage());
            }
        } else {
            log.info("------------------ Custom Token is empty: " + customtoken + " ---------------------------------------");
            modelAndView.addObject("error", "Invoice not found");
        }

        return modelAndView;
    }
}