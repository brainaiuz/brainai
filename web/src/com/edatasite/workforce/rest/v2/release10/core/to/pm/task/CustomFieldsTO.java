package com.edatasite.workforce.rest.v2.release10.core.to.pm.task;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.customfield.CustomFieldTextTO;

/**
 * Created by Abdurakhmonov Farrukh on 12/26/2017.
 */
public class CustomFieldsTO extends ResponseData {
    private String type;
    private Object object;

    public CustomFieldsTO() {
    }

    public CustomFieldsTO(String type, CustomFieldTextTO object) {
        this.type = type;
        this.object = object;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
