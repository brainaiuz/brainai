package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by IntelliJ IDEA.
 * User: Shohruh
 * Date: 17.09.15
 * Time: 18:32:35
 * To change this template use File | Settings | File Templates.
 */
public class InvoiceClientApproveHandler implements HttpRequestHandler, Constants{

    private static Logger log = LoggerFactory.getLogger(InvoiceClientApproveHandler.class);

    @Autowired
    private InvoiceServiceLocal invoiceServiceLocal;
    @Autowired
    private InvoiceManager invoiceManager;

    @Transactional
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        log.info("-------- Invoice Client Approve Notification: --------");
        log.info("Request URL: " + request.getRequestURI());

        String custom = request.getParameter("custom");
        String invoice_status = request.getParameter("invoice_status");
        if (invoice_status != null) {
            invoice_status = invoice_status.toUpperCase();
        }

        if (!invoice_status.equals("DRAFT")) {
            if (custom != null && !"".equals(custom.trim()) && custom.startsWith("_")) {
                Integer companyID = null;
                Integer invoiceID = null;
                String databaseType = null;
                try {
                    String decryptedData = EncryptionHelper.decryptURL(custom.substring(1));
                    String idArray[] = decryptedData.split("_");//companyid,invoiceid
                    companyID = Integer.valueOf(idArray[0]);
                    databaseType = idArray[1];
                    invoiceID = Integer.valueOf(idArray[2]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                if (companyID == null || companyID <= 0 || databaseType == null || invoiceID == null || invoiceID <= 0) {
                    out.print("Invalid link");
                    out.close();
                    return;
                }
                //THESE LOGS FOR TEST ONLY, DELETE ON LIVE
                log.info("INVOICE_APPROVE_COMPANY_ID:" + companyID);
                log.info("INVOICE_APPROVE_INVOICE_ID:" + invoiceID);
                log.info("INVOICE_APPROVE_DATABASE_TYPE:" + databaseType);

                ServerSecurityContext.getInstance().setCompanyId(companyID);
                ServerSecurityContext.getInstance().setDatabase(databaseType);

//                NewInvoice invoice = invoiceService.getInvoice(invoiceID);
                EdsInvoice edsInvoice = invoiceManager.get(invoiceID);
                NewInvoice invoice = EdsInvoice.getInvoiceData(edsInvoice);

                if (invoice == null || invoice.isDeleted()) {
                    out.print("Invoice is deleted or unavailable");
                    out.close();
                    return;
                }
//                if (invoice.getStatus().equals(Constants.PAID)) {
//                    out.print("Invoice is already paid");
//                    out.close();
//                    return;
//                }

                invoice.setClientApproved(true);

                invoiceServiceLocal.updateGatewaySaleInvoice(invoice);
                //invoiceService.updateSaleInvoice(invoice);

                StringBuilder buffer = new StringBuilder();
                buffer.append("<html>");
                buffer.append("<head>");
                buffer.append("<title>Invoice Client Approvement</title>");
                buffer.append("</head>");
                buffer.append("<body>");
                buffer.append("<p>Invoice has been approved by the client<p>");
                buffer.append("</body>");
                buffer.append("</html>");
                out.print(buffer.toString());
                out.close();
            }
        } else {
            out.print("Invoice cannot be approved while it's draft");
            out.close();
        }
    }
}
