package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.profile.client.localization.ProfileMessages;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/9/11
 * Time: 4:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class PaymentTermsConditionsUtil {

    private static final ProfileMessages profileMessages = ProfileMessages.App.get();

    public static final String DUE_DAY = "${dueday}";
    public static final String DUE_DATE = "${duedate}";
    public static final String PAYMENT_METHOD = "${paymentmethod}";
    public static final String START_DATE = "${startdate}";
    public static final String FROM_DATE = "${fromdate}";
    public static final String TO_DATE = "${todate}";
    public static final String NUMBER = "${number}";
    public static final String TERMS = "${terms}";

    public static String generateSelectedTemplate(String template, String dueDay, String dueDate, String paymentMethod, String startDate, String fromDate, String toDate,String number, String terms) {
        if (template != null) {
            if (dueDay != null)
                template = template.replace(DUE_DAY, dueDay);
            if (dueDate != null)
                template = template.replace(DUE_DATE, dueDate);
            if (paymentMethod != null)
                template = template.replace(PAYMENT_METHOD, paymentMethod);
            if (startDate != null) {
                template = template.replace(START_DATE, startDate);
            }
            if (fromDate != null) {
                template = template.replace(FROM_DATE, fromDate);
            }
            if (toDate != null) {
                template = template.replace(TO_DATE, toDate);
            }
            if (number != null) {
                template = template.replace(NUMBER, number);
            }
            if (terms != null) {
                template = template.replace(TERMS, terms);
            }
            return template;
        }
        return "";
    }

    public static String getSettingsTemplate(String type) {
        if (Constants.SALE_INVOICE.equals(type)) {
            return profileMessages.instructionTemplate(DUE_DAY, NUMBER, DUE_DATE);
        }
        return "";
    }
}
