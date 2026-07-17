package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceCircularResolver;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/16/12
 * Time: 4:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class MastercardRedirectHandler implements HttpRequestHandler {

    @Autowired
    private InvoiceCircularResolver invoiceCircularResolver;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        createHeader(out);

        String encryptedAmount = request.getParameter("user_amount");//payment amount
        String encryptedCompanyID = request.getParameter("user_cid");
        String encryptedKeyID = request.getParameter("user_key");//invoice id, booking item id, etc...
        String encryptedPaymentType = request.getParameter("user_type");//Invoice, Booking Item
        String definedUserUrl = request.getParameter("user_url");//Invoice, Booking Item

        if (encryptedCompanyID == null || encryptedKeyID == null || encryptedAmount == null || encryptedPaymentType == null) {
            createContent(out, "Invalid link");
            return;
        }

        Integer companyID = null, keyID=null;
        BigDecimal paymentAmount = null;
        String paymentType = null;
        try {
            companyID = Integer.parseInt(EncryptionHelper.decrypt(encryptedCompanyID));
            keyID = Integer.parseInt(EncryptionHelper.decrypt(encryptedKeyID));
            paymentAmount = new BigDecimal(EncryptionHelper.decrypt(encryptedAmount));
            paymentType = EncryptionHelper.decrypt(encryptedPaymentType);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (companyID == null || keyID == null || paymentAmount == null || paymentType == null) {
            createContent(out, "Invalid parameters included in URL");
            return;
        }

        ServerSecurityContext.getInstance().setCompanyId(companyID);
        ServerSecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(companyID));

        String validationResponseMessage = invoiceCircularResolver.validateMasterCardLinkParameters(companyID, keyID, paymentAmount, paymentType);

        if (validationResponseMessage != null) {
            out.print(validationResponseMessage);
            out.close();
            return;
        }

        String masterCardPaymentGeneratedURL = invoiceCircularResolver.getMasterCardPaymentURL(keyID, companyID, paymentAmount, paymentType, definedUserUrl);
        if (masterCardPaymentGeneratedURL == null) {
            createContent(out, "Mastercard URL not generated");
            return;
        }

        response.sendRedirect(masterCardPaymentGeneratedURL);
        createContent(out, "Redirecting...");
    }

    private void createHeader(PrintWriter out) {

        out.print("<title> Mastercard </title>");
        out.print("<link rel='stylesheet' type='text/css' href='/landing/css/welcomepage.css' />");
        out.print("<link rel='stylesheet' type='text/css' href='/loginpage/kpi/afterdeletedshell.css' />");
        out.print("<link rel='shortcut icon' href='/customisation/kpi.com/images/favicon.ico' type='image/x-icon'/>");

    }

    public void createContent(PrintWriter out, String message) {
        out.print("<div style='width:860px; margin:10px auto; padding:10px; id='index-page'>");
        out.print("<h2 class=\"title\"> " + message + "</h2>");
        out.print("</div>");
        out.close();
    }
}
