package com.edatasite.workforce.rest.v2.release10.core.to.base.customfield;

import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;

/**
 * Created by Abdurakhmonov Farrukh on 12/26/2017.
 */
public class CustomFieldTextTO extends CategoryTO {
    private String text;

    public CustomFieldTextTO() {
    }

    public CustomFieldTextTO(String text) {
        this.text = text;
    }

    public CustomFieldTextTO(Integer id, String title, String text) {
        super(id, title);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
