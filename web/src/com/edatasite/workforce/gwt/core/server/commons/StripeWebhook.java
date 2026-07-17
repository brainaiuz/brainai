package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.enums.PaymentTypeEnum;
import com.edatasite.workforce.gwt.myaccount.server.app.MyAccountServiceLocal;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import com.stripe.model.Event;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceLineItem;
import com.stripe.model.InvoiceLineItemPeriod;
import com.stripe.net.Webhook;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Anvar Akramov on 10/18/2017.
 */
public class StripeWebhook implements HttpRequestHandler {

    private static Logger log = LoggerFactory.getLogger(StripeWebhook.class);

    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private MyAccountServiceLocal myAccountServiceLocal;


    //we need to move this into hostbasedsetting
    private String webhook_endpoint_secret = "whsec_bmdokoluZPHB7wCTfXxEN2oIiTEVG8C6";
    private String webhook_endpoint_secret_local = "whsec_FPkA2RTjeDY4f00mPQbhwvAxu0RTZWLk";//"whsec_TnE97VrdDmACtoF2wlgefB8yMWzt1yg2";
    private String webhook_endpoint_secret_staging = "whsec_R0P06ByMNJ9sviYVofE1ktXqkhDxsHzX";
    private String webhook_endpoint_secret_production = "whsec_ntZlHkJrccWUR0Jav7Xdc73L7B8rDOD6";

    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String sigHeader = request.getHeader("Stripe-Signature");
        Event event = null;

        try {
            String stripeEventPayload = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            if (request.getServerName().toLowerCase().contains("dev.kpi.com")) {
                event = Webhook.constructEvent(
                        stripeEventPayload, sigHeader, webhook_endpoint_secret
                );
            } else if (request.getServerName().toLowerCase().contains("ngrok.io")) {
                event = Webhook.constructEvent(
                        stripeEventPayload, sigHeader, webhook_endpoint_secret_local
                );
            } else if (request.getServerName().toLowerCase().contains("staging.kpi.com")) {
                event = Webhook.constructEvent(
                        stripeEventPayload, sigHeader, webhook_endpoint_secret_staging
                );
            } else {
                event = Webhook.constructEvent(
                        stripeEventPayload, sigHeader, webhook_endpoint_secret_production
                );
            }
            if (event != null) {
                log.info("Event Type: " + event.getType());
            }
            log.info("Event body: " + stripeEventPayload);
            log.info("---------------------------------------");
        } catch (Exception e) {
            // Invalid payload or Invalid signature
            response.setStatus(400);
            log.error("", e);
        }
        // Retrieve the request's body and parse it as JSON
        // Event eventJson = APIResource.GSON.fromJson(stripEventString, Event.class);

        //Do something with eventJson
//        log.info(stripEventPayload);

        if (event != null && "invoice.payment_succeeded".equalsIgnoreCase(event.getType())) {
            Invoice invoice = ((Invoice) event.getData().getObject());
            InvoiceLineItem invoiceLineItem = invoice.getLines().getData().get(0);

            if (invoiceLineItem != null) {
                InvoiceLineItemPeriod invoicePeriod = invoiceLineItem.getPeriod();
                log.info("period.start: " + new Date(invoicePeriod.getStart() * 1000));
                log.info("period.end: " + new Date(invoicePeriod.getEnd() * 1000));

                Map<String, String> metadata = invoiceLineItem.getMetadata();
                if (metadata != null && metadata.containsKey("company_id") && metadata.containsKey("usageplan_id")) {
                    ServerSecurityContext.getInstance().setCompanyId(metadata.get("company_id"));
                    String databaseName = globalAuthJdbcSpringManager.getCompanyDatabaseName(Integer.valueOf(metadata.get("company_id")));
                    if (databaseName == null) {
                        log.info("|||||||||||||||||||||||||||||>> UPS! Company = " + metadata.get("company_id") + " not found.");
                    } else {
                        ServerSecurityContext.getInstance().setDatabase(databaseName);
                        myAccountServiceLocal.stripeSubscriptionInvoicePaid(invoice);
                        //Send Email Notification
                        if(invoiceLineItem!=null) {
                            myAccountServiceLocal.sendPaidStripeWebhookNotification(invoiceLineItem.getMetadata(), PaymentTypeEnum.STRIPE);
                        }

                        for (Map.Entry<String, String> entry : metadata.entrySet()) {
                            log.info(entry.getKey() + " : " + entry.getValue());
                        }
                    }
                }
            }
            /*log.info("PeriodStart: " + new Date(((Invoice) event.getData().getObject()).getPeriodStart()*1000L));
            log.info("PeriodEnd: " + new Date(((Invoice) event.getData().getObject()).getPeriodEnd()*1000L));*/

        }

        response.setStatus(200);
//        return "";
    }

}
