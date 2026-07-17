package com.edatasite.workforce.gwt.reportingsystem.client.enumerable;

/**
 * User: ${Dilsh0d}
 * Date: 19-Mar-2010
 * Time: 19:03:40
 */
public enum SecuritryType {
    ConnectionSecuritry("UserSecuritryRpc"),
    ReportXmlString("ReportXmlString");


    SecuritryType(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }
}
