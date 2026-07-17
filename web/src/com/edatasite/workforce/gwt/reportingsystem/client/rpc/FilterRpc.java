package com.edatasite.workforce.gwt.reportingsystem.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: Virus
 * Date: 5/1/13
 * Time: 9:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class FilterRpc implements IsSerializable {
    String column;
    String value;
    String andOr;
    String operation;

    public FilterRpc() {
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAndOr() {
        return andOr;
    }

    public void setAndOr(String andOr) {
        if (andOr == null || !"null".equals(andOr))
            this.andOr = andOr;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}

