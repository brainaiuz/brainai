package com.edatasite.workforce.core.config.datasource;

import org.springframework.util.Assert;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 3/28/11
 * Time: 2:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class TenantContextHolder {

    public static final String FREE_DB = "FREE";
    public static final String PAID_DB = "PAID";
    public static final String PAID1_DB = "PAID1";
    public static final String DEFAULT_DB = FREE_DB;
    public static final String DEFAULT_SCHEMA = "public";
    public static final Long DEFAULT_TENANT_ID = 0L;

    private static final ThreadLocal<String> contextHolder = new InheritableThreadLocal<>();

    public static String getCustomerType() {
        return contextHolder.get();
    }

    public static void setCustomerType(String customerType) {
        Assert.notNull(customerType, "customerType cannot be null");
        contextHolder.set(customerType);
    }

    public static void clearCustomerType() {
        contextHolder.remove();
    }
}
