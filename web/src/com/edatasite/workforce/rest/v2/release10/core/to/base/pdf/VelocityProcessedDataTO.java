package com.edatasite.workforce.rest.v2.release10.core.to.base.pdf;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Anvar Akramov on 02/21/2018.
 */
public class VelocityProcessedDataTO extends ResponseData {

    private String html;

    public VelocityProcessedDataTO() {
    }

    public VelocityProcessedDataTO(String html) {
        this.html = html;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
