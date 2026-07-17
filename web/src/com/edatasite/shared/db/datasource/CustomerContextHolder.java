package com.edatasite.shared.db.datasource;

import org.springframework.util.Assert;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 3/28/11
 * Time: 2:26 PM
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
public class CustomerContextHolder {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();


    public static void setCustomerType(String customerType) {
        Assert.notNull(customerType, "customerType cannot be null");
        contextHolder.set(customerType);
    }


    public static String getCustomerType() {
        return contextHolder.get();
    }


    public static void clearCustomerType() {
        contextHolder.remove();
    }

}
