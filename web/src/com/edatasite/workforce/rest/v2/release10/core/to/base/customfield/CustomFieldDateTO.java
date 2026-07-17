package com.edatasite.workforce.rest.v2.release10.core.to.base.customfield;

import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;

/**
 * Created by Abdurakhmonov Farrukh on 12/26/2017.
 */
public class CustomFieldDateTO extends CategoryTO {
    private String date;

    public CustomFieldDateTO() {
    }

    public CustomFieldDateTO(String date) {
        this.date = date;
    }

    public CustomFieldDateTO(Integer id, String title, String date) {
        super(id, title);
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
