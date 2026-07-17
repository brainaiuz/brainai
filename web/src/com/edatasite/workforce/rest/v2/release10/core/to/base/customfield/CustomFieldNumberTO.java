package com.edatasite.workforce.rest.v2.release10.core.to.base.customfield;

import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;

/**
 * Created by Abdurakhmonov Farrukh on 12/26/2017.
 */
public class CustomFieldNumberTO extends CategoryTO {
    private String value;

    public CustomFieldNumberTO() {
    }

    public CustomFieldNumberTO(String value) {
        this.value = value;
    }

    public CustomFieldNumberTO(Integer id, String title, String value) {
        super(id, title);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
