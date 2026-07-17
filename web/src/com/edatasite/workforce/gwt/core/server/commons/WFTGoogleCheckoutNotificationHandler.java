package com.edatasite.workforce.gwt.core.server.commons;


import com.edatasite.workforce.core.domain.EdsGoogleCheckoutOrder;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.GoogleCheckoutOrderManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.google.checkout.sdk.commands.ApiContext;
import com.google.checkout.sdk.commands.Environment;
import com.google.checkout.sdk.domain.AnyMultiple;
import com.google.checkout.sdk.domain.AuthorizationAmountNotification;
import com.google.checkout.sdk.domain.OrderSummary;
import com.google.checkout.sdk.notifications.BaseNotificationDispatcher;
import com.google.checkout.sdk.notifications.Notification;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpRequestHandler;
import org.w3c.dom.Element;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 21, 2011
 * Time: 1:42:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class WFTGoogleCheckoutNotificationHandler implements HttpRequestHandler, Constants {
    private static Logger log = LoggerFactory.getLogger(WFTGoogleCheckoutNotificationHandler.class);
    private MessageManager messageManager;
    private GoogleCheckoutOrderManager googleCheckoutOrderManager;


    public static String SANDBOX_MERCHANT_ID = "533352259832376";
    public static String SANDBOX_MERCHANT_KEY = "TORD6rsknkdAwPXaLu_Uuw";
    //SANDBOX_AUTHORIZATION_KEY is Base64 encoded value of SANDBOX_MERCHANT_ID:SANDBOX_MERCHANT_KEY
    public static String SANDBOX_AUTHORIZATION_KEY = "NTMzMzUyMjU5ODMyMzc2OlRPUkQ2cnNrbmtkQXdQWGFMdV9VdXc=";
    public static String CURRENCY = "USD";
    public static String SANDBOX_URL = "https://sandbox.google.com/checkout/api/checkout/v2/merchantCheckout/Merchant/" + SANDBOX_MERCHANT_ID;


    public static ApiContext SANDBOX_API_CONTEXT = new ApiContext(Environment.SANDBOX, SANDBOX_MERCHANT_ID, SANDBOX_MERCHANT_KEY, CURRENCY);


    @Transactional
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Enumeration en = request.getParameterNames();
        log.debug("-------- WFT Google Checkout Notification handler started : --------");
        StringBuilder str = new StringBuilder("cmd=_notify-validate");   //\r\n
        StringBuilder mes = new StringBuilder("<html><body>");
        log.debug("Request URL: " + request.getRequestURI());
        String charset = request.getParameter("charset");
        if ("".equals(charset) || charset == null) {
            charset = "UTF-8";
        }
        while (en.hasMoreElements()) {
            String paramName = URLDecoder.decode((String) en.nextElement(), charset);
            String paramValue = URLDecoder.decode(request.getParameter(paramName), charset);
            str.append("&").append(paramName).append("=").append(paramValue);
            mes.append("<p>").append(paramName).append("=").append(paramValue).append("</p>");
            //log.debug("WFTGoogleCheckoutNotificationHandler: " + paramName + " = " + paramValue);
        }
        SANDBOX_API_CONTEXT.handleNotification(
                new BaseNotificationDispatcher(request, response) {
                    @Override
                    public void onAllNotifications(OrderSummary orderSummary,
                                                   Notification notification) {
                        log.debug("WFTGoogleCheckoutNotificationHandler.onAllNotifications() orderSummary.getGoogleOrderNumber() = " + orderSummary.getGoogleOrderNumber());
                    }

                    @Override
                    public void onAuthorizationAmountNotification(OrderSummary orderSummary,
                                                                  AuthorizationAmountNotification notification) {
                        EdsGoogleCheckoutOrder existingOrder = googleCheckoutOrderManager.getByOrderNumber(orderSummary.getGoogleOrderNumber());
                        if(existingOrder==null) {
                            existingOrder.setOrderNumber(orderSummary.getGoogleOrderNumber());
                            existingOrder.setOrderSummary(orderSummary.toString());
                            googleCheckoutOrderManager.update(existingOrder);
                        } else {
                            EdsGoogleCheckoutOrder newOrder = new EdsGoogleCheckoutOrder();
                            newOrder.setOrderNumber(orderSummary.getGoogleOrderNumber());
                            newOrder.setOrderSummary(orderSummary.toString());

                            if(orderSummary!=null && orderSummary.getShoppingCart().getItems()!=null
                                    && orderSummary.getShoppingCart().getItems().getItem()!=null
                                    && orderSummary.getShoppingCart().getItems().getItem().size()>0 ) {
                                AnyMultiple itemData = orderSummary.getShoppingCart().getItems().getItem().get(0).getMerchantPrivateItemData();
                                if(itemData!=null) {
                                    for(Object o : itemData.getContent()) {
                                        Element e = (Element) o;
                                        log.info("MerchantPrivateItemData"+ e.getNodeName() + "="+e.getNodeValue());
                                    }
                                }
                            }
                            googleCheckoutOrderManager.create(newOrder);
                        }

                        log.debug(
                                "WFTGoogleCheckoutNotificationHandler Order " + notification.getGoogleOrderNumber()
                                        + " authorized and ready to ship to:"
                                        + orderSummary.getBuyerShippingAddress().getContactName());
                    }

                    @Override
                    protected void rememberSerialNumber(String serialNumber,
                                                        OrderSummary orderSummary, Notification notification) {
                        // NOTE: We'll have to remember serial numbers in our database,
                        // before using this for real
                    }

                    @Override
                    public boolean hasAlreadyHandled(String serialNumber,
                                                     OrderSummary orderSummary, Notification notification) {
                        // NOTE: We'll have to look up serial numbers in our database
                        // before using this for real
                        return false;
                    }
                });
        messageManager.sendGoogleCheckoutNotification(mes.toString(), "New Funds from Google Checkout Subscriptions WFT Notification");
        log.debug("-------- WFT Google Checkout Notification handler ended : --------");
    }

    public void setMessageManager(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    public void setGoogleCheckoutOrderManager(GoogleCheckoutOrderManager googleCheckoutOrderManager) {
        this.googleCheckoutOrderManager = googleCheckoutOrderManager;
    }
}
