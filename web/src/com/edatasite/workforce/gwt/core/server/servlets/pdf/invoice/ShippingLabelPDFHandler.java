package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.gwt.core.server.usps.USPSDeliveryConfirmation;
import com.edatasite.workforce.gwt.core.server.usps.USPSExpressMailLabel;
import com.edatasite.workforce.gwt.core.server.usps.USPSWebService;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/25/12
 * Time: 4:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShippingLabelPDFHandler implements HttpRequestHandler {

    @Autowired
    private InvoiceServiceLocal invoiceServiceLocal;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer invoiceID = Integer.parseInt(request.getParameter("invoiceID"));
        String serviceName = request.getParameter("serviceName");
        String fromZipCode = request.getParameter("fromZip");
        String toZipCode = request.getParameter("toZip");
        String weightInOunces = request.getParameter("weightInOunces");

        String responseXML;
        if (serviceName.startsWith("Express Mail")) {
            USPSExpressMailLabel expressMailLabel = invoiceServiceLocal.getUSPSExpressMailLabel(invoiceID, fromZipCode, toZipCode, weightInOunces);
            if (expressMailLabel.getValidationStatus() != null && USPSDeliveryConfirmation.PRIMARY_CONTACT_IS_NOT_EXIST.equals(expressMailLabel.getValidationStatus())) {
                response.setContentType("text/html;charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.write("Primary contact is required to generate shipping label");
                out.close();
                return;
            }
            USPSWebService uspsWebService = new USPSWebService("ExpressMailLabel", true, USPSWebService.USPS_USER_ID, USPSWebService.USPS_PASSWORD);
            responseXML = uspsWebService.submitRequestAndGetResponse(expressMailLabel.toXML());
        } else {
            USPSDeliveryConfirmation deliveryConfirmation = invoiceServiceLocal.getUSPSDeliveryConfirmation(invoiceID, serviceName, fromZipCode, toZipCode, weightInOunces);
            if (deliveryConfirmation.getValidationStatus() != null && USPSDeliveryConfirmation.PRIMARY_CONTACT_IS_NOT_EXIST.equals(deliveryConfirmation.getValidationStatus())) {
                response.setContentType("text/html;charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.write("Primary contact is required to generate shipping label");
                out.close();
                return;
            }
            USPSWebService uspsWebService = new USPSWebService("DeliveryConfirmationV3", true, USPSWebService.USPS_USER_ID, USPSWebService.USPS_PASSWORD);
            responseXML = uspsWebService.submitRequestAndGetResponse(deliveryConfirmation.toXML());
        }

        if (responseXML.contains("<Error>")) {
            String errorData = responseXML.substring(responseXML.indexOf("<Error>") + 7, responseXML.indexOf("</Error>"));
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.write(errorData.substring(errorData.indexOf("<Description>") + 13, errorData.indexOf("</Description>")));
            out.close();
            return;
        }

        byte[] pdfStream = decodePDFData(responseXML);

        OutputStream out = response.getOutputStream();
        response.setContentType("application/pdf");
        out.write(pdfStream);
        out.close();
    }

    public static byte[] decodePDFData(String responseXML) {
        String labelData = responseXML.substring(responseXML.indexOf("<DeliveryConfirmationLabel>") + 27, responseXML.indexOf("</DeliveryConfirmationLabel>"));
        return Base64.decode(labelData);
    }
}
