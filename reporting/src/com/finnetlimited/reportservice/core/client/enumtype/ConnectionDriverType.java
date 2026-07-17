package com.finnetlimited.reportservice.core.client.enumtype;

/**
 * User: ${Dilsh0d}
 * Date: 10-Mar-2010
 * Time: 13:50:28
 * <p/>
 * <br/> This Enum uses for Show or Hide data
 */
public enum ConnectionDriverType {
    SQL(""),
    MYSQL(""),
    ORACLE(""),
    POSTGRESS("org.postgresql.Driver");

    ConnectionDriverType(String driverValue) {
        this.driverValue = driverValue;
    }

    private String driverValue;

    public String getDriverClassName() {
        return driverValue;
    }
}
