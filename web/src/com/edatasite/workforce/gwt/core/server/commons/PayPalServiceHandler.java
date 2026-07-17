package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.EdsSubscriptionPayment;
import com.edatasite.workforce.core.domain.EdsUsagePlan;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.core.tools.UsagePlan;
import com.edatasite.workforce.gwt.core.client.enums.PaymentTypeEnum;
import com.edatasite.workforce.gwt.core.server.db.UsagePlanManager;
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
import java.io.IOException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali Pirnafasov
 * Email: sherali.pirnafasov@gmail.com
 * Date: 27.11.2008
 * Time: 16:06:59
 */
public class PayPalServiceHandler extends PayPalUtils implements HttpRequestHandler {

    private static Logger log = LoggerFactory.getLogger(PayPalServiceHandler.class);
    private static final String SUBSCR_CANCEL = "subscr_cancel";
    private static final String SUBSCR_SIGNUP = "subscr_signup";
    private static final String SUBSCR_MODIFY = "subscr_modify";
    private static final String SUBSCR_PAYMENT = "subscr_payment";
    private static final String COMPLETED = "COMPLETED";
    public static final DateFormat df = new SimpleDateFormat("HH:mm:ss MMM dd, yyyy z");

    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private MyAccountServiceLocal myAccountServiceLocal;
    @Autowired
    private UsagePlanManager usagePlanManager;

    public static String getReceiverEmail() {
        if (EdsContextParams.isLiveEnvironment()) {
            return paypal_ACCOUNT_Live;
        } else {
            return paypal_ACCOUNT_Test;
        }
    }

    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Enumeration en = request.getParameterNames();
        log.info("-------- PayPal Notification: --------");
        StringBuilder mes = new StringBuilder("<html><body>");
        log.info("Request URL: " + request.getRequestURI());
        String charset = request.getParameter("charset");
        if ("".equals(charset) || charset == null) {
            mes.append("CharserNULL");
            charset = "UTF-8";
        }
        while (en.hasMoreElements()) {
            String paramName = URLDecoder.decode((String) en.nextElement(), charset);
            String paramValue = URLDecoder.decode(request.getParameter(paramName), charset);
            mes.append("<p>").append(paramName).append("=").append(paramValue).append("</p>");
            log.info(paramName + " = " + paramValue);
        }

        String res = getPayPalIpnVerification(request);

        StringBuilder messageSubject = new StringBuilder();
        String first_name = request.getParameter("first_name");
        messageSubject.append(first_name).append(",");
        String last_name = request.getParameter("last_name");
        messageSubject.append(last_name).append(",");
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
            messageSubject.append("txn_type:").append(txn_type).append(",");
        }
        if (mc_gross != null) {
            messageSubject.append("mc_gross:").append(mc_gross).append(",");
        }
        if (payment_fee != null) {
            messageSubject.append("payment_fee:").append(payment_fee).append(",");
        }
        if (mc_fee != null) {
            messageSubject.append("mc_fee:").append(mc_fee).append(",");
        }
        if (payer_email != null) {
            messageSubject.append("payer_email:").append(payer_email).append(",");
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

        try {
            if ("VERIFIED".equals(res) && (SUBSCR_MODIFY.equals(txn_type) || SUBSCR_CANCEL.equals(txn_type) || SUBSCR_PAYMENT.equals(txn_type) || SUBSCR_SIGNUP.equals(txn_type))) {
                newSubscriptionPayment.setVerified(true);
                mes.append("<p>NOTIFICATION_VALIDATION = VERIFIED<p>");
                if (StringUtils.isNotBlank(custom) && getReceiverEmail().equals(receiver_email)) {
                    Integer subsId = null;
                    Integer storefrontId = null;
                    String customType = null;
                    Integer companyId = null;
                    String[] params = null;
                    if (StringUtils.contains(custom, SUBSCRIPTION_ADD)) {
                        customType = SUBSCRIPTION_ADD;
                        params = StringUtils.splitByWholeSeparator(custom, SUBSCRIPTION_ADD);
                        if (params.length > 1) {
                            try {
                                companyId = Integer.valueOf(params[0]);
                            } catch (Exception e) {
                                log.info("COMPANY_ID = " + EncryptionHelper.decrypt(params[0]));
                                companyId = Integer.valueOf(EncryptionHelper.decrypt(params[0]));
                            }
                            if(params.length>2 && StringUtils.isNotBlank(params[2])) {
                                EdsUsagePlan usagePlan = usagePlanManager.getUsagePlanByUID(params[2]);
                                if(usagePlan!=null) {
                                    subsId = usagePlan.getObjectID();
                                }
                            } else {

                                if(StringUtils.isNotBlank(params[1]) && params[1].length()>30) {
                                    EdsUsagePlan usagePlan = usagePlanManager.getUsagePlanByUID(params[1]);
                                    if (usagePlan != null) {
                                        subsId = usagePlan.getObjectID();
                                    }
                                } else {
                                    subsId = Integer.valueOf(params[1]);
                                }
                            }
                        } else {
                            subsId = Integer.valueOf(params[0]);
                        }
                    } else if (StringUtils.contains(custom, SUBSCRIPTION_UPG)) {
                        customType = SUBSCRIPTION_UPG;
                        params = StringUtils.splitByWholeSeparator(custom, SUBSCRIPTION_UPG);
                        if (params.length > 1) {
                            try {
                                companyId = Integer.valueOf(params[0]);
                            } catch (Exception e) {
                                log.info("COMPANY_ID = " + EncryptionHelper.decrypt(params[0]));
                                companyId = Integer.valueOf(EncryptionHelper.decrypt(params[0]));
                            }

                            if(params.length>2 && StringUtils.isNotBlank(params[2])) {
                                EdsUsagePlan usagePlan = usagePlanManager.getUsagePlanByUID(params[2]);
                                if(usagePlan!=null && usagePlan.getSubscriptionHistory()!=null) {
                                    subsId = usagePlan.getSubscriptionHistory().getObjectID();
                                }
                            } else {
                                subsId = Integer.valueOf(params[1]);
                            }
                        } else {
                            subsId = Integer.valueOf(params[0]);
                        }
                    } else if (StringUtils.contains(custom, SUBSCRIPTION_SF)) {
                        customType = SUBSCRIPTION_SF;
                        params = StringUtils.splitByWholeSeparator(custom, SUBSCRIPTION_SF);
                        try {
                            companyId = Integer.valueOf(params[0]);
                        } catch (Exception e) {
                            log.info("COMPANY_ID = " + EncryptionHelper.decrypt(params[0]));
                            companyId = Integer.valueOf(EncryptionHelper.decrypt(params[0]));
                        }
                        storefrontId = Integer.valueOf(params[1]);

                        //storefront sub scription payment subject
                        messageSubject = new StringBuilder().append("Storefront Subscription: ").append(messageSubject.toString());
                    }
                    if (params != null && params.length >= 1) {//For Debug only
                        System.out.println("CompanyHash - " + params[0]);
                        System.out.println("CompanyID = " + companyId);
                    }

                    if (companyId == null) {
                        UsagePlan tmpUsagePlan = globalAuthJdbcSpringManager.getUsagePlane(subsId, customType);
                        if (tmpUsagePlan != null) {
                            companyId = tmpUsagePlan.getCompanyId();
                        }
                    }
                    newSubscriptionPayment.setSubsId(subsId);
                    newSubscriptionPayment.setStorefrontId(storefrontId);
                    newSubscriptionPayment.setCustomType(customType);

                    ServerSecurityContext.getInstance().setCompanyId(companyId);
                    String databaseName = globalAuthJdbcSpringManager.getCompanyDatabaseName(companyId);
                    if (databaseName == null) {
                        log.info("|||||||||||||||||||||||||||||>> UPS! Company = " + companyId + " not found.");
                        return;
                    }
                    ServerSecurityContext.getInstance().setDatabase(databaseName);
                    //
                    mes = new StringBuilder(myAccountServiceLocal.payFromPaypal(newSubscriptionPayment, mes.toString()));
                }
            } else if (res.equals("INVALID")) {
                mes.append("<p>NOTIFICATION_VALIDATION = INVALID</p>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mes.append("<p>HOST =").append(EdsContextParams.getHostname()).append("</p>");
        mes.append("</body></html>");
        log.debug(mes.toString());

        myAccountServiceLocal.sendPayPalNotification(mes.toString(), messageSubject.toString());
    }
}
