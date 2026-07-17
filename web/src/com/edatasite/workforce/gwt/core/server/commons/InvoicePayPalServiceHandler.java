package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.EdsSubscriptionPayment;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.gwt.core.client.enums.PaymentTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.SubscriptionPaymentManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 27.11.2008
 * Time: 16:06:59
 * To change this template use File | Settings | File Templates.
 */
public class InvoicePayPalServiceHandler extends PayPalUtils implements HttpRequestHandler {

    private static final Logger log = LoggerFactory.getLogger(InvoicePayPalServiceHandler.class);
    private static final String SUBSCR_CANCEL = "subscr_cancel";
    private static final String SUBSCR_SIGNUP = "subscr_signup";
    private static final String SUBSCR_MODIFY = "subscr_modify";
    private static final String SUBSCR_PAYMENT = "subscr_payment";
    private static final String COMPLETED = "COMPLETED";

    @Autowired
    private InvoiceServiceLocal invoiceServiceLocal;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private SubscriptionPaymentManager subscriptionPaymentManager;

    @Transactional
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Enumeration<String> en = request.getParameterNames();
        log.info("-------- PayPal Invoice Payment Notification: --------");
        StringBuilder mes = new StringBuilder("<html><body>");
        log.info("Request URL: {}", request.getRequestURI());
        String charset = request.getParameter("charset");
        if ("".equals(charset) || charset == null) {
            mes.append("CharserNULL");
            charset = "UTF-8";
        }
        while (en.hasMoreElements()) {
            String paramName = URLDecoder.decode(en.nextElement(), charset);
            String paramValue = URLDecoder.decode(request.getParameter(paramName), charset);
            mes.append("<p>").append(paramName).append("=").append(paramValue).append("</p>");
            log.info("{} = {}", paramName, paramValue);
        }

        String paypalCheckResponse = getPayPalIpnVerification(request);

        log.info("PAYPAL_CHECK_RESPONSE:{}", paypalCheckResponse);

        StringBuilder messageSubject = new StringBuilder();
        String first_name = request.getParameter("first_name");
        messageSubject.append("Invoice Payment: ");
        messageSubject.append(first_name + ",");
        String last_name = request.getParameter("last_name");
        messageSubject.append(last_name + ",");
        String address_name = request.getParameter("address_name");
        String subscr_id = request.getParameter("subscr_id");
        String txn_type = request.getParameter("txn_type");
        String address_city = request.getParameter("address_city");
        String residence_country = request.getParameter("residence_country");
        String test_ipn = request.getParameter("test_ipn");
        String payment_gross = request.getParameter("payment_gross");
        String payment_date = request.getParameter("payment_date");
        String address_zip = request.getParameter("address_zip");
        String address_street = request.getParameter("address_street");
        String protection_eligibility = request.getParameter("protection_eligibility");
        String payer_id = request.getParameter("payer_id");
        String verify_sign = request.getParameter("verify_sign");
        String business = request.getParameter("business");
        String address_country_code = request.getParameter("address_country_code");
        String mc_fee = request.getParameter("mc_fee");
        String address_status = request.getParameter("address_status");
        String transaction_subject = request.getParameter("transaction_subject");
        String notify_version = request.getParameter("notify_version");
        String address_state = request.getParameter("address_state");
        String payment_fee = request.getParameter("payment_fee");
        String item_name = request.getParameter("item_name");
        String item_number = request.getParameter("item_number");
        String payment_status = request.getParameter("payment_status");
        String mc_gross = request.getParameter("mc_gross");
        String mc_currency = request.getParameter("mc_currency");
        String txn_id = request.getParameter("txn_id");
        String receiver_email = request.getParameter("receiver_email");
        String payer_email = request.getParameter("payer_email");
        String payer_status = request.getParameter("payer_status");
        String receiver_id = request.getParameter("receiver_id");
        String address_country = request.getParameter("address_country");
        String custom = request.getParameter("custom");
        String exchange_rate = request.getParameter("exchange_rate");
        String settle_currency = request.getParameter("settle_currency");
        String amount3 = request.getParameter("amount3");
        String mc_amount3 = request.getParameter("mc_amount3");

        if (txn_type != null) {
            messageSubject.append("txn_type:" + txn_type + ",");
        }
        if (mc_gross != null) {
            messageSubject.append("mc_gross:" + mc_gross + ",");
        }
        if (payment_fee != null) {
            messageSubject.append("payment_fee:" + payment_fee + ",");
        }
        if (mc_fee != null) {
            messageSubject.append("mc_fee:" + mc_fee + ",");
        }
        if (payer_email != null) {
            messageSubject.append("payer_email:" + payer_email + ",");
        }
        EdsSubscriptionPayment newSubscriptionPayment = new EdsSubscriptionPayment();
        newSubscriptionPayment.setPaymentType(PaymentTypeEnum.PAYPAL);
        newSubscriptionPayment.setAddress_city(address_city);
        newSubscriptionPayment.setAddress_country(address_country);
        newSubscriptionPayment.setAddress_country_code(address_country_code);
        newSubscriptionPayment.setAddress_name(address_name);
        newSubscriptionPayment.setAddress_status(address_status);
        newSubscriptionPayment.setAddress_street(address_street);
        newSubscriptionPayment.setAddress_zip(address_zip);
        newSubscriptionPayment.setAddress_state(address_state);
        newSubscriptionPayment.setBusiness(business);
        newSubscriptionPayment.setCharset(charset);
        newSubscriptionPayment.setCustom(custom);
        newSubscriptionPayment.setExchange_rate(exchange_rate);
        newSubscriptionPayment.setFirst_name(first_name);
        newSubscriptionPayment.setItem_name(item_name);
        newSubscriptionPayment.setItem_number(item_number);
        newSubscriptionPayment.setLast_name(last_name);
        newSubscriptionPayment.setMc_currency(mc_currency);
        newSubscriptionPayment.setMc_fee(mc_fee);
        newSubscriptionPayment.setMc_gross(mc_gross);
        newSubscriptionPayment.setNotify_version(notify_version);
        newSubscriptionPayment.setPayer_email(payer_email);
        newSubscriptionPayment.setPayer_id(payer_id);
        newSubscriptionPayment.setPayer_status(payer_status);
        newSubscriptionPayment.setPayment_date(payment_date);
        newSubscriptionPayment.setPayment_fee(payment_fee);
        newSubscriptionPayment.setPayment_gross(payment_gross);
        newSubscriptionPayment.setPayment_status(payment_status);
        newSubscriptionPayment.setProtection_eligibility(protection_eligibility);
        newSubscriptionPayment.setReceiver_email(receiver_email);
        newSubscriptionPayment.setReceiver_id(receiver_id);
        newSubscriptionPayment.setResidence_country(residence_country);
        newSubscriptionPayment.setSettle_currency(settle_currency);
        newSubscriptionPayment.setSubscr_id(subscr_id);
        newSubscriptionPayment.setTest_ipn(test_ipn);
        newSubscriptionPayment.setTransaction_subject(transaction_subject);
        newSubscriptionPayment.setTxn_id(txn_id);
        newSubscriptionPayment.setVerify_sign(verify_sign);
        newSubscriptionPayment.setTxn_type(txn_type);
        newSubscriptionPayment.setAmount3(amount3);
        newSubscriptionPayment.setMc_amount3(mc_amount3);

        //check notification validation
        if (payment_status != null) {
            payment_status = payment_status.toUpperCase();
        }
        if (paypalCheckResponse.equals("VERIFIED")) {
            newSubscriptionPayment.setVerified(true);
            mes.append("<p>NOTIFICATION_VALIDATION = VERIFIED<p>");
            if (custom != null && !"".equals(custom.trim()) && custom.startsWith("_")) {
                Integer companyID = null;
                Integer invoiceID = null;
                String databaseType = null;
                try {
                    String decryptedData = EncryptionHelper.decryptURL(custom.substring(1));
                    String[] idArray = decryptedData.split("_");//companyid,invoiceid
                    companyID = Integer.valueOf(idArray[0]);
                    databaseType = idArray[1];
                    invoiceID = Integer.valueOf(idArray[2]);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (companyID == null || companyID <= 0) {
                    log.info("PAYPAL_INCOMPATIBLE_COMPANY_ID:{}", companyID);
                    return;
                }
                if (StringUtils.isBlank(databaseType)) {
                    log.info("PAYPAL_DATABASE_TYPE_IS_NULL");
                    return;
                }
                if (invoiceID == null || invoiceID <= 0) {
                    log.info("PAYPAL_INCOMPATIBLE_INVOICE_ID:{}", invoiceID);
                    return;
                }

                log.info("PAYPAL_COMPANY_ID:{}", companyID);
                log.info("PAYPAL_INVOICE_ID:{}", invoiceID);
                log.info("PAYPAL_DATABASE_TYPE:{}", databaseType);

                ServerSecurityContext.getInstance().setCompanyId(companyID);
                ServerSecurityContext.getInstance().setDatabase(databaseType);

                log.info("PAYPAL_DATABASE_TYPE_AFTER_SET:{}", ServerSecurityContext.getInstance().getDatabase());

                EdsInvoice invoice = invoiceManager.get(invoiceID);
                if (invoice != null) {
                    String paypalReturnedURL = request.getRequestURL().append("?").append(request.getQueryString()).toString();
                    PaymentData paymentData = new PaymentData();
                    paymentData.setInvoiceID(invoiceID);
                    if(StringUtils.isNotBlank(mc_gross)) {
                        paymentData.setPaymentAmount(new BigDecimal(mc_gross/*amount3*/));
                    } else {
                        paymentData.setPaymentAmount(BigDecimal.ZERO);
                    }

                    Date paymentDate = null;
                    try {
                        paymentDate = new Date(payment_date);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    paymentData.setDate(new DateNonConvertable(paymentDate != null ? paymentDate : new Date()));
                    paymentData.setReferenceNumber(txn_id);
                    paymentData.setValidateReference(false);
                    paymentData.setExchangeRate(invoice.getExchangeRate());
                    paymentData.setType(Constants.RECEIVABLE);
                    paymentData.setTotal(invoice.getTotalInInvoiceCurrency());
                    paymentData.setGatewayReturnedURL(paypalReturnedURL);
                    invoiceServiceLocal.saveGatewayPaymentData(paymentData, PAYPAL_PAYMENT);

                    newSubscriptionPayment.setInvoiceId(invoiceID);
                }
            }
        } else if (paypalCheckResponse.equals("INVALID")) {
            mes.append("<p>NOTIFICATION_VALIDATION = INVALID</p>");
        }
        mes.append("<p>HOST =").append(EdsContextParams.getHostname()).append("</p>");
        mes.append("</body></html>");
        log.info(mes.toString());
//        messageManager.sendPayPalNotification(mes, messageSubject.toString());
        subscriptionPaymentManager.create(newSubscriptionPayment);
    }
}
