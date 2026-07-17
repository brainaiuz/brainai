package com.edatasite.workforce.gwt.core.server.controllers;

import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 14.07.2009
 * Time: 12:41:20
 * To change this template use File | Settings | File Templates.
 */
public class PhoneNumberValidator {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9 .,/\\-()+]*$");
    private String phone;

    public PhoneNumberValidator(String phone) {
        this.phone = phone;
    }

    public boolean hasContent() {
        return phone != null && !"".equals(phone);
    }

    public boolean checkPhone() {
        if (hasContent()) {
            return PHONE_PATTERN.matcher(phone).matches();
        }
        return false;
    }
}
