package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.EdsWorldPayHistory;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.core.tools.UsagePlan;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.myaccount.server.app.MyAccountServiceLocal;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali Pirnafasov
 * Email: sherali.pirnafasov@gmail.com
 * Date: 27.11.2008
 * Time: 16:06:59
 */
public class WorldPayServiceHandler implements HttpRequestHandler, Constants {

    private static Logger log = LoggerFactory.getLogger(WorldPayServiceHandler.class);
    public static final DateFormat df = new SimpleDateFormat("HH:mm:ss MMM dd, yyyy z");

    private static final String TRANSACTION_SUCCESSFUL = "Y";
    private static final String TRANS_ID = "transId";
    private static final String TRANS_STATUS = "transStatus";
    private static final String TRANS_TIME = "transTime";
    private static final String AUTH_COST = "authCost";
    private static final String AUTH_CURRENCY = "authCurrency";
    private static final String AUTH_AMOUNT = "authAmount";
    private static final String AUTH_AMOUNT_STRING = "authAmountString";
    private static final String RAW_AUTH_MESSAGE = "rawAuthMessage";
    private static final String RAW_AUTH_CODE = "rawAuthCode";
    private static final String CALLBACK_PW = "callbackPW";
    private static final String CARD_TYPE = "cardType";
    private static final String COUNTRY_MATCH = "countryMatch";
    private static final String IPADDRESS = "ipAddress";
    private static final String AVS = "AVS";
    private static final String WAFMERCH_MESSAGE = "wafMerchMessage";
    private static final String AUTHENTICATION = "authentication";
    private static final String AUTHMODE = "authMode";
    private static final String IP_ADDRESS = "ipAddress";
    private static final String NAME = "name";
    private static final String FAX = "fax";
    private static final String CUSTOMER_EMAIL = "email";
    private static final String TEL = "tel";
    private static final String ADDRESS = "address";
    private static final String ADDRESS1 = "address1";
    private static final String ADDRESS2 = "address2";
    private static final String ADDRESS3 = "address3";
    private static final String POSTCODE = "postcode";
    private static final String COUNTRY = "country";
    private static final String REGION = "region";
    private static final String TOWN = "town";
    private static final String COUNTRY_STRING = "countryString";
    private static final String DISPLAY_ADDRESS = "displayAddress";
    private static final String CART_ID = "cartId";
    private static final String AMOUNT_STRING = "amountString";
    private static final String CURRENCY = "currency";
    private static final String AMOUNT = "amount";
    private static final String COST = "cost";
    private static final String COMP_NAME = "compName";
    private static final String FUTURE_PAY_ID = "futurePayId";
    private static final String CUSTOM = "MC_custom";
    private static final String MC_USAGEPLACEID = "MC_usageplaceId";
    private static final String ITEM_NUMBER = "MC_item_number";
    private static final String FUTURE_PAY_STATUS_CHANGE = "futurePayStatusChange";
    private static final String TXN_TYPE = "MC_txn_type";

    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private MyAccountServiceLocal myAccountServiceLocal;

    public static String getReceiverEmail() {
        if (EdsContextParams.isLiveEnvironment()) {
            return WORLDPAY_ACCOUNT_Live;
        } else {
            return WORLDPAY_ACCOUNT_TEST;
        }
    }

    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Enumeration<String> en = request.getParameterNames();
        log.info("-------- WorldPay Notification: --------");
        StringBuilder str = new StringBuilder("cmd=_notify-validate");
        StringBuilder mes = new StringBuilder("<html><body>");
        log.info("Request URL: " + request.getRequestURI());
        String charset = request.getParameter("charset");
        if ("".equals(charset) || charset == null) {
            mes.append("CharserNULL");
            charset = "UTF-8";
        }
        while (en.hasMoreElements()) {
            String paramName = URLDecoder.decode(en.nextElement(), charset);
            String paramValue = URLDecoder.decode(request.getParameter(paramName), charset);
            str.append("&").append(URLEncoder.encode(paramName, charset)).append("=").append(URLEncoder.encode(paramValue, charset));
            mes.append("<p>").append(paramName).append("=").append(paramValue).append("</p>");
            log.info(paramName + " = " + paramValue);
        }
        String lebazarPayment = request.getParameter("MC_from");
        if (lebazarPayment != null && !lebazarPayment.isEmpty() && "lebazar".equals(lebazarPayment.trim())) {
            redirectToLebazar(str.toString());
            return;//stop here. Payment made for lebazar!!!
        }

        log.info("Encode URL: " + str);

        EdsWorldPayHistory domain = getParams(request);

        StringBuilder messageSubject = new StringBuilder();
        if (!isEmpty(domain.getAmountString())) {
            messageSubject.append(domain.getAmountString()).append(",");
        }
        if (!isEmpty(domain.getEmail())) {
            messageSubject.append(domain.getEmail()).append(",");
        }
        if (!isEmpty(domain.getName())) {
            messageSubject.append(domain.getName());
        }
        try {
            System.out.println("-----------------------------------------------I am here in service handler");
            if (domain.getTransStatus() != null && TRANSACTION_SUCCESSFUL.equals(domain.getTransStatus())) {
                System.out.println("-----------------------------------------------Getting to start update SP");
                domain.setVerified(true);
                mes.append("<p>NOTIFICATION_VALIDATION = VERIFIED<p>");
                Integer subsId = null;
                Integer storefrontId = null;
                String customType = null;
                Integer companyId = null;
                String[] params = null;

                if (domain.getCustom() != null && !domain.getCustom().isEmpty()) {
                    System.out.println("-----------------------------------------------This is custom field" + domain.getCustom());
                    if (StringUtils.contains(domain.getCustom(), SUBSCRIPTION_ADD)) {
                        System.out.println("-----------------------------------------------" + domain.getCustom());
                        customType = SUBSCRIPTION_ADD;
                        System.out.println(customType);
                        params = StringUtils.splitByWholeSeparator(domain.getCustom(), SUBSCRIPTION_ADD);
                        if (params.length > 1) {
                            try {
                                companyId = Integer.valueOf(params[0]);
                            } catch (Exception e) {
                                companyId = Integer.valueOf(EncryptionHelper.decrypt(params[0]));
                            }
                            subsId = Integer.valueOf(params[1]);
                        } else {
                            subsId = Integer.valueOf(params[0]);
                        }
                    } else if (StringUtils.contains(domain.getCustom(), SUBSCRIPTION_UPG)) {
                        customType = SUBSCRIPTION_UPG;
                        System.out.println("Upgrading SB in servlet");
                        System.out.println(customType);
                        params = StringUtils.splitByWholeSeparator(domain.getCustom(), SUBSCRIPTION_UPG);
                        if (params.length > 1) {
                            try {
                                companyId = Integer.valueOf(params[0]);
                            } catch (Exception e) {
                                companyId = Integer.valueOf(EncryptionHelper.decrypt(params[0]));
                            }
                            subsId = Integer.valueOf(params[1]);
                        } else {
                            subsId = Integer.valueOf(params[0]);
                        }
                    } else if (StringUtils.contains(domain.getCustom(), SUBSCRIPTION_SF)) {
                        customType = SUBSCRIPTION_SF;
                        params = StringUtils.splitByWholeSeparator(domain.getCustom(), SUBSCRIPTION_SF);
                        try {
                            companyId = Integer.valueOf(params[0]);
                        } catch (Exception e) {
                            companyId = Integer.valueOf(EncryptionHelper.decrypt(params[0]));
                        }
                        storefrontId = Integer.valueOf(params[1]);
                        messageSubject = new StringBuilder().append("Storefront Subscription: ").append(messageSubject.toString());
                    }
                    if (companyId == null) {
                        UsagePlan tmpUsagePlan = globalAuthJdbcSpringManager.getUsagePlane(subsId, customType);
                        if (tmpUsagePlan != null) {
                            companyId = tmpUsagePlan.getCompanyId();
                        }
                    }

                    domain.setSubsId(subsId);
                    domain.setStorefrontId(storefrontId);
                    domain.setCustomType(customType);

                    ServerSecurityContext.getInstance().setCompanyId(companyId);
                    String databaseName = globalAuthJdbcSpringManager.getCompanyDatabaseName(companyId);
                    if (databaseName == null) {
                        log.info("|||||||||||||||||||||||||||||>> UPS! Company = " + companyId + " not found.");
                        return;
                    }
                    ServerSecurityContext.getInstance().setDatabase(databaseName);
                    mes = new StringBuilder(myAccountServiceLocal.payFromWorld(domain, mes.toString()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mes.append("<p>HOST =").append(EdsContextParams.getHostname()).append("</p>");
        mes.append("</body></html>");
        log.debug(mes.toString());
        myAccountServiceLocal.sendWorldPayNotification(str.toString(), "WorldPay Payment");

        myAccountServiceLocal.sendPayPalNotification(mes.toString(), messageSubject.toString());
    }

    private void redirectToLebazar(String str) throws IOException {
        if (str == null || str.isEmpty()) {
            return;
        }
        String url = "http://sellmigusta.dst.uz/common/migustaWorldPayNotification";
        url = url + "?" + str;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        //print result
        System.out.println(response.toString());
        if (responseCode != 200) {
            myAccountServiceLocal.sendWorldPayNotification(response.toString(), "Error lebazar paypal");
        }
    }

    private EdsWorldPayHistory getParams(HttpServletRequest request) {
        EdsWorldPayHistory domain = new EdsWorldPayHistory();
        domain.setCartId(getRequestParameter(request, CART_ID));
        domain.setAmountString(getRequestParameter(request, AMOUNT_STRING));
        domain.setCurrency(getRequestParameter(request, CURRENCY));
        domain.setAmount(getRequestParameter(request, AMOUNT));
        domain.setCost(getRequestParameter(request, COST));
        domain.setCompName(getRequestParameter(request, COMP_NAME));
        domain.setFuturePayId(getRequestParameter(request, FUTURE_PAY_ID));
        domain.setAuthentication(getRequestParameter(request, AUTHENTICATION));
        domain.setAuthMode(getRequestParameter(request, AUTHMODE));
        domain.setIpAddress(getRequestParameter(request, IP_ADDRESS));
        domain.setName(getRequestParameter(request, NAME));
        domain.setFax(getRequestParameter(request, FAX));
        domain.setEmail(getRequestParameter(request, CUSTOMER_EMAIL));
        domain.setTel(getRequestParameter(request, TEL));
        domain.setAddress(getRequestParameter(request, ADDRESS));
        domain.setAddress1(getRequestParameter(request, ADDRESS1));//rename
        domain.setAddress2(getRequestParameter(request, ADDRESS2));//rename
        domain.setAddress3(getRequestParameter(request, ADDRESS3));
        domain.setPostcode(getRequestParameter(request, POSTCODE));
        domain.setCountry(getRequestParameter(request, COUNTRY));
        domain.setRegion(getRequestParameter(request, REGION));
        domain.setTown(getRequestParameter(request, TOWN));
        domain.setCountryString(getRequestParameter(request, COUNTRY_STRING));
        domain.setDisplayAddress(getRequestParameter(request, DISPLAY_ADDRESS));
        domain.setTransId(getRequestParameter(request, TRANS_ID));
        domain.setTransStatus(getRequestParameter(request, TRANS_STATUS));
        domain.setAuthAmount(getRequestParameter(request, TRANS_TIME));
        domain.setAuthCost(getRequestParameter(request, AUTH_COST));
        domain.setAuthCurrency(getRequestParameter(request, AUTH_CURRENCY));
        domain.setAuthAmountString(getRequestParameter(request, AUTH_AMOUNT_STRING));
        domain.setRawAuthMessage(getRequestParameter(request, RAW_AUTH_MESSAGE));
        domain.setRawAuthCode(getRequestParameter(request, RAW_AUTH_CODE));
        domain.setCallbackPW(getRequestParameter(request, CALLBACK_PW));
        domain.setCardType(getRequestParameter(request, CARD_TYPE));
        domain.setCountryMatch(getRequestParameter(request, COUNTRY_MATCH));
        domain.setWafMerchMessage(getRequestParameter(request, WAFMERCH_MESSAGE));
        domain.setIpAddress(getRequestParameter(request, IPADDRESS));
        domain.setCustom(getRequestParameter(request, CUSTOM));
        domain.setItemNumber(getRequestParameter(request, ITEM_NUMBER));
        domain.setFuturePayStatusChange(getRequestParameter(request, FUTURE_PAY_STATUS_CHANGE));
        domain.setTxn_type(getRequestParameter(request, TXN_TYPE));

        String transTime = getRequestParameter(request, TRANS_TIME);
        if (!isEmpty(transTime)) {
            domain.setTransTime(new Date(Long.parseLong(transTime)));
        }
        String avs = getRequestParameter(request, AVS);
        if (!isEmpty(avs)) {
            avs = getRequestParameter(request, AVS.toLowerCase());
        }
        domain.setAVS(avs);
        return domain;
    }

    private String getRequestParameter(HttpServletRequest req, String name) {
        if (req != null && !isEmpty(name) && !isEmpty(req.getParameter(name))) {
            return req.getParameter(name).trim();
        }
        return null;
    }

    private boolean isEmpty(String value) {
        return value == null || value.isEmpty() || value.trim().isEmpty();
    }

}
