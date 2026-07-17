package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: Khasan
 * Date: 29.04.13
 * Time: 17:07
 * To change this template use File | Settings | File Templates.
 */
public class CsvTemplateItem implements IsSerializable {

    private Integer objectID;
    private String value;//csvcolumnname(when isSystemValue=false) or systemfieldvalue(when isSystemValue=true)
    private String systemField;
    private boolean isSystemValue;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSystemField() {
        return systemField;
    }

    public void setSystemField(String systemField) {
        this.systemField = systemField;
    }

    public boolean isSystemValue() {
        return isSystemValue;
    }

    public void setSystemValue(boolean systemValue) {
        isSystemValue = systemValue;
    }
}
