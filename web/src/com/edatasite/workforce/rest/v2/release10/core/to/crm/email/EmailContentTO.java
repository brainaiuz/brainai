package com.edatasite.workforce.rest.v2.release10.core.to.crm.email;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Farrukh Abdurakhmonov on 4/14/2018.
 */
public class EmailContentTO extends ResponseData {
    private String plain_text;
    private String html_data;

    public String getPlain_text() {
        return plain_text;
    }

    public void setPlain_text(String plain_text) {
        this.plain_text = plain_text;
    }

    public String getHtml_data() {
        return html_data;
    }

    public void setHtml_data(String html_data) {
        this.html_data = html_data;
    }
}
