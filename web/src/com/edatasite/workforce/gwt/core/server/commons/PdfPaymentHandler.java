package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;


/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Jan 16, 2010
 * Time: 7:36:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class PdfPaymentHandler implements HttpRequestHandler {

    @Autowired
    private InvoiceService invoiceService;

    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DecimalFormat numberFormat = new DecimalFormat(",##0.00");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String type = request.getParameter("paymentType");
        String invoiceId;
        String accountDetail;

        try {
            invoiceId = EncryptionHelper.decrypt(request.getParameter("invoiceId"));
            accountDetail = EncryptionHelper.decrypt(request.getParameter("accountDetail"));
        } catch (Exception e) {
            e.printStackTrace();
            invoiceId = null;
            accountDetail = null;
        }

        if (accountDetail == null || invoiceId == null) {
            out.print("Invalid link");
            out.close();
            return;
        }


        if (type.equals("googleCheckout")) {

            Integer invId = Integer.parseInt(invoiceId);
            NewInvoice invoiceData = invoiceService.getInvoice(invId);

            StringBuilder buffer = new StringBuilder();
            buffer.append("<html>");
            buffer.append("<head>");
            buffer.append("<title>Payment</title>");
            buffer.append("</head>");
            buffer.append("<body onLoad=\"document.payment.submit()\">");
            buffer.append("<form method=POST action='" + ServerUtils.getGoogleCheckoutLink() + accountDetail + "' name=\"payment\">");

            buffer.append("<input type=\"hidden\" name=\"item_name_1\" value=\"" + getItemNames(invoiceData) + "\"/>");
            buffer.append("<input type=\"hidden\" name=\"item_description_1\" value=\"\"/>");
            buffer.append("<input type=\"hidden\" name=\"item_price_1\" value=\"" + numberFormat.format(invoiceData.getTotalInInvoiceCurrency()) + "\"/>");
            buffer.append("<input type=\"hidden\" name=\"item_currency_1\" value=\"" + invoiceData.getCurrencyName() + "\"/>");
            buffer.append("<input type=\"hidden\" name=\"item_quantity_1\" value=\"1\"/>");

            // if script is not enabled
            buffer.append("<noscript>");
            buffer.append("Java is not enabled in the web browser. Please continue by clicking payment button. <p>");
            buffer.append("<input type=\"image\" src=\"http://sandbox.google.com/checkout/buttons/checkout.gif?merchant_id=sherali&ov@gmail.com&w=180&h=46&style=white&variant=text&loc=en_US\"\n" +
                    "height=\"46\" width=\"180\" name=\"Google Checkout\" alt=\"Fast checkout through Google\"/>");
            buffer.append("</noscript>");

            buffer.append("</form>");
            buffer.append("</body>");
            buffer.append("</html>");

            out.print(buffer.toString());
            out.close();
        }
    }


    private String getItemNames(NewInvoice invoiceData) {
        StringBuilder buffer = new StringBuilder();
        for (NewInvoiceItem invoiceItem : invoiceData.getItems()) {
            buffer.append(invoiceItem.getItemName());
            buffer.append(",");
        }
        buffer.deleteCharAt(buffer.lastIndexOf(","));
        return buffer.toString();
    }
}
