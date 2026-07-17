package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.EdsInvoicingSettings;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.InvoicingSettingsManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.edatasite.workforce.gwt.invoice.server.app.MasterCardSecureHashGenerator;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/15/12
 * Time: 10:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class MastercardPaymentHandler implements HttpRequestHandler{

    private static Logger log = LoggerFactory.getLogger(MastercardPaymentHandler.class);

    public static final String INVOICE = "INVOICE";
    public static final String COURSE_BOOKING = "COURSE_BOOKING";

    public static final Integer COURSE_BOOKING_ALREADY_PAID = -5;

    @Autowired
    private InvoiceServiceLocal invoiceServiceLocal;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private InvoicingSettingsManager invoicingSettingsManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String mastercardReturnedURL = request.getRequestURL().append("?").append(request.getQueryString()).toString();

        String encryptedAmount = request.getParameter("user_amount");
        String encryptedCompanyID = request.getParameter("user_cid");
        String encryptedKeyID = request.getParameter("user_key");
        String encryptedPaymentType = request.getParameter("user_type");
        String encryptedUserUrl = request.getParameter("user_url");
        String paymentStatus = request.getParameter("vpc_TxnResponseCode");
        String mastercardTransactionNumber = request.getParameter("vpc_TransactionNo");
        String responseMessage = request.getParameter("vpc_Message");
        String secureHashFromURL = request.getParameter("vpc_SecureHash");

        log.info("MASTERCARD_RETURNED_URL:" + mastercardReturnedURL);
        log.info("MASTERCARD_RESPONSE_MESSAGE:" + responseMessage);
        log.info("MASTERCARD_PAYMENT_STATUS:" + paymentStatus);
        log.info("MASTERCARD_TRANSACTION_NO:" + mastercardTransactionNumber);
        log.info("MASTERCARD_URL_SECURE_HASH:" + secureHashFromURL);

        if (encryptedCompanyID == null || encryptedKeyID == null || encryptedAmount == null || encryptedPaymentType == null) {
            out.print("Invalid link");
            out.close();
            return;
        }

        Integer companyID = null, keyID = null;
        BigDecimal paymentAmount = null;
        String paymentType = null;
        String userDefinedUrl = null;
        try {
            companyID = Integer.parseInt(EncryptionHelper.decrypt(encryptedCompanyID));
            keyID = Integer.parseInt(EncryptionHelper.decrypt(encryptedKeyID));
            paymentAmount = new BigDecimal(Integer.parseInt(EncryptionHelper.decrypt(encryptedAmount)) / 1000);
            paymentType = EncryptionHelper.decrypt(encryptedPaymentType);

            if (encryptedUserUrl != null && !encryptedUserUrl.isEmpty()) {
                userDefinedUrl = EncryptionHelper.decrypt(encryptedUserUrl);
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (companyID == null || keyID == null || paymentAmount == null || paymentType == null) {
            out.print("URL is not valid. Invalid parameters included.");
            out.close();
            return;
        }

        ServerSecurityContext.getInstance().setCompanyId(companyID);
        ServerSecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(companyID));


        EdsInvoicingSettings invoicingSettings = invoicingSettingsManager.getInvoiceSettings(companyManager.get(companyID));
        if (!"0".equals(paymentStatus) || !validateSecureHash(request, secureHashFromURL, invoicingSettings.getMasterCardSecretKey())) {
            out.print("<span>URL parameters are not valid.</span>");
            out.close();
            return;
        }

        EdsInvoice invoice;
        PaymentData paymentData = new PaymentData();
        if (MastercardPaymentHandler.COURSE_BOOKING.equals(paymentType)) {
            Integer invoiceID = invoiceServiceLocal.createInvoiceFromCourseBooking(keyID);

            if (COURSE_BOOKING_ALREADY_PAID.equals(invoiceID)) {
                out.print("<span>This course booking is already paid</span>");
                out.close();
                return;
            }

            invoice = invoiceManager.get(invoiceID);
            paymentData.setInvoiceID(invoiceID);
        } else {
            invoice = invoiceManager.get(keyID);
            paymentData.setInvoiceID(keyID);
        }

        paymentData.setPaymentAmount(paymentAmount);
        paymentData.setDate(new DateNonConvertable(new Date()));
        paymentData.setReferenceNumber(mastercardTransactionNumber);
        paymentData.setValidateReference(false);
        paymentData.setExchangeRate(invoice.getExchangeRate());
        paymentData.setType(Constants.RECEIVABLE);
        paymentData.setTotal(invoice.getTotalInInvoiceCurrency());
        paymentData.setGatewayReturnedURL(mastercardReturnedURL);

        Integer invoicePaymentID = invoiceServiceLocal.saveGatewayPaymentData(paymentData, Constants.MASTERCARD_PAYMENT);

        if (invoicePaymentID != null && invoicePaymentID > 0) {
            invoiceServiceLocal.sendMastercardReceiptToClient(paymentData.getInvoiceID());

            if (userDefinedUrl != null && !userDefinedUrl.isEmpty()) {
                response.sendRedirect(userDefinedUrl);
            }
        }
        out.print("<span>Your payment in the amount of " + new DecimalFormat(",##0.00").format(paymentAmount) + " against invoice " + invoice.getNumber() + " was successful</span>");
        out.close();
    }

    private boolean validateSecureHash(HttpServletRequest request, String secureHashFromURL, String secretKey) {
        Map parametersMap = request.getParameterMap();
        Set keySet = parametersMap.keySet();
        List<String> keysList = new LinkedList<>();
        for (Object key : keySet) {
            String stringKey = (String) key;
            if ((stringKey.startsWith("user_") || stringKey.startsWith("vpc_")) && !(stringKey.equals("vpc_SecureHash") || stringKey.equals("vpc_SecureHashType"))) {
                keysList.add(stringKey);
            }
        }

        keysList.sort((o1, o2) -> o1.compareTo(o2));

        SortedMap<String, String> sortedParameters = new TreeMap<>();
        for (String key : keysList) {
            sortedParameters.put(key, request.getParameter(key));
        }

        String generatedSecureHash = new MasterCardSecureHashGenerator(secretKey, sortedParameters, false).getGeneratedSecureHash();

        return generatedSecureHash.equalsIgnoreCase(secureHashFromURL);

    }
}
