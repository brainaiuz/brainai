package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.EdsInvoicingSettings;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.InvoicingSettingsManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpRequestHandler;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 1/28/14
 * Time: 3:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class ElavonPaymentServiceHandler implements HttpRequestHandler {

    private static Logger log = LoggerFactory.getLogger(ElavonPaymentServiceHandler.class);


    @Autowired
    private GlobalAuthJdbcSpringManager jdbcSpringManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private InvoicingSettingsManager invoicingSettingsManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private InvoiceServiceLocal invoiceServiceLocal;

    @Override
    @Transactional
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("-------- Elavon Payment Notification: --------");
        log.info("Request URL: " + request.getRequestURI());

        String command = request.getParameter("cmd");

        String requiredParameter = request.getParameter("rp");
        if (requiredParameter == null || requiredParameter.trim().isEmpty()) {
            response.getWriter().print("Invalid link");
            return;
        }

        Integer companyID = null, userID = null, invoiceID = null;
        try {
            String decryptedRP = EncryptionHelper.decryptURL(requiredParameter);
            String[] params = decryptedRP.split("_");
            companyID = Integer.parseInt(params[0]);
            userID = Integer.parseInt(params[1]);
            invoiceID = Integer.parseInt(params[2]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (companyID == null || userID == null || invoiceID == null) {
            response.getWriter().print("Invalid link");
            return;
        }

        ServerSecurityContext.getInstance().setCompanyId(companyID);
        ServerSecurityContext.getInstance().setDatabase(jdbcSpringManager.getCompanyClusterType(companyID));
        ServerSecurityContext.getInstance().setStaticUserID(userID);

        EdsInvoicingSettings invoicingSettings = invoicingSettingsManager.getInvoiceSettings(companyManager.get(companyID));

        if (invoicingSettings == null || isEmpty(invoicingSettings.getElavonMerchandID()) || isEmpty(invoicingSettings.getElavonUserID()) || isEmpty(invoicingSettings.getElavonPIN())) {
            response.getWriter().print("Merchant settings not set yet.");
            return;
        }

        EdsSaleInvoice saleInvoice = invoiceManager.getSaleInvoice(invoiceID);
        if (saleInvoice == null) {
            response.getWriter().write("Invoice not found");
            return;
        }

        if (saleInvoice.isDeleted()) {
            response.getWriter().write("Invoice is deleted");
            return;
        }

        if (Constants.PAID.equals(saleInvoice.getStatus().getCode())) {
            response.getWriter().write("Invoice is already paid");
            return;
        }

        if (!(Constants.APPROVE.equals(saleInvoice.getStatus().getCode()) || Constants.OPEN.equals(saleInvoice.getStatus().getCode()))) {
            response.getWriter().write("Invoice is not approved");
            return;
        }

        if ("gc".equals(command)) {
            BigDecimal dueAmount = saleInvoice.getDueAmount().setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
            String content = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<title>Elavon Payment</title>" +
                    "</head>" +
                    "<body>" +
                    "<form action=\"" + ServerUtils.getElavonLink() + "\" method=post>" +
                    "<input type=\"hidden\" name=\"ssl_merchant_id\" value=\"" + invoicingSettings.getElavonMerchandID() + "\">" +
                    "<input type=\"hidden\" name=\"ssl_user_id\" value=\"" + invoicingSettings.getElavonUserID() + "\">" +
                    "<input type=\"hidden\" name=\"ssl_pin\" value=\"" + invoicingSettings.getElavonPIN() + "\">" +
                    "<input type=\"hidden\" name=\"ssl_transaction_type\" value=\"ccsale\">" +
                    "<input type=\"hidden\" name=\"ssl_amount\" value=\"" + dueAmount.toString() + "\">" +
                    "<input type=\"hidden\" name=\"ssl_receipt_link_method\" value=\"REDG\">" +
                    "<input type=\"hidden\" name=\"ssl_receipt_decl_get_url\" value=\"" + getPaymentApprovedNotifyURL() + "\">" +
                    "<input type=\"hidden\" name=\"ssl_receipt_apprvl_get_url\" value=\"" + getPaymentApprovedNotifyURL() + "\">" +
                    "<input type=\"hidden\" name=\"cmd\" value=\"payment\">" +
                    "<input type=\"hidden\" name=\"rp\" value=\"" + EncryptionHelper.encodeURL(requiredParameter) + "\">" +
                    "<span>Click continue to proceed payment</span><br/><br/>" +
                    "<input type=submit value=\"Continue\">" +
                    "</form>" +
                    "</body>" +
                    "</html>";
            response.setContentType("text/html");
            response.getWriter().print(content);
        }

        if("payment".equals(command)){
            String txtResult = request.getParameter("ssl_result");
            String txnID = request.getParameter("ssl_txn_id");
            if ("0".equals(txtResult) && !isEmpty(txnID)) {
                StringBuilder urlBuilder = new StringBuilder();
                urlBuilder.append(ServerUtils.getElavonLink());
                urlBuilder.append("?ssl_merchant_id=" + invoicingSettings.getElavonMerchandID());
                urlBuilder.append("&ssl_user_id=" + invoicingSettings.getElavonUserID());
                urlBuilder.append("&ssl_pin=" + invoicingSettings.getElavonPIN());
                urlBuilder.append("&ssl_transaction_type=txnquery");
                urlBuilder.append("&ssl_txn_id=" + txnID);

                URL siteUrl = new URL(urlBuilder.toString());
                HttpsURLConnection conn = (HttpsURLConnection) siteUrl.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.flush();
                out.close();
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String tempString = "";
                HashMap<String, String> responseDataMap = new HashMap<>();
                while ((tempString = in.readLine()) != null) {
                    if (!isEmpty(tempString)) {
                        String[] tempData = tempString.split("=");
                        if (tempData.length >= 2) {
                            responseDataMap.put(tempData[0], tempData[1]);
                        }
                    }
                }
                in.close();

                if (responseDataMap.containsKey("ssl_result_message") && responseDataMap.containsKey("ssl_amount")) {
                    String statusCode = responseDataMap.get("ssl_result_message");
                    String amountString = responseDataMap.get("ssl_amount");
                    BigDecimal paymentAmount = new BigDecimal(amountString);

                    System.out.println("STATUS:" + statusCode);
                    System.out.println("AMOUNT:" + amountString);

                    if (("APPROVAL".equals(statusCode) || "PARTIAL APPROVAL".equals(statusCode)) && paymentAmount.compareTo(BigDecimal.ZERO) > 0) {
                        if (paymentAmount.setScale(2, BigDecimal.ROUND_HALF_UP).compareTo(saleInvoice.getDueAmount().setScale(2, BigDecimal.ROUND_HALF_UP)) > 0) {
                            response.getWriter().print("Due amount exceeded! Cannot pay automatically to invoice " + saleInvoice.getNumber());
                            return;
                        }

                        String gatewayReturnedURL = request.getRequestURL().append("?").append(request.getQueryString()).toString();

                        PaymentData paymentData = new PaymentData();
                        paymentData.setInvoiceID(saleInvoice.getObjectID());
                        paymentData.setPaymentAmount(paymentAmount);
                        paymentData.setDate(new DateNonConvertable(new Date()));
                        paymentData.setReferenceNumber(txnID);
                        paymentData.setValidateReference(false);
                        paymentData.setExchangeRate(saleInvoice.getExchangeRate());
                        paymentData.setType(Constants.RECEIVABLE);
                        paymentData.setTotal(saleInvoice.getTotalInInvoiceCurrency());
                        paymentData.setGatewayReturnedURL(gatewayReturnedURL);

                        Integer invoicePaymentID = invoiceServiceLocal.saveGatewayPaymentData(paymentData, Constants.ELAVON_PAYMENT);

                        if (invoicePaymentID != null && invoicePaymentID > 0) {
                            response.getWriter().print("Payment with amount " + amountString + " for invoice " + saleInvoice.getNumber() + " proceeded successfully");
                            return;
                        }
                    }
                }

                response.getWriter().print("Automatic payment not proceeded");
            }
        }
    }

    private boolean isEmpty(String s) {
        return s==null || s.trim().isEmpty();
    }

    private String getPaymentApprovedNotifyURL(){
        return EdsContextParams.getFullHost() + CommandConstants.COMMON_URL + "/payWithElavon";
    }
    private String getPaymentDeclinedNotifyURL(){
        return EdsContextParams.getFullHost() + CommandConstants.COMMON_URL + "/payWithElavon";
    }
}
