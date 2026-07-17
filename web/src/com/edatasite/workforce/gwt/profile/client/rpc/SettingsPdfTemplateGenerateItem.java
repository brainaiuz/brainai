package com.edatasite.workforce.gwt.profile.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Abror Abdukadirov
 * Date: 16.01.2019 16:57
 */
public class SettingsPdfTemplateGenerateItem implements IsSerializable {
    private String headerHtml;
    private String bodyHtml;
    private String footerHtml;

    public SettingsPdfTemplateGenerateItem() {
    }

    public SettingsPdfTemplateGenerateItem(String headerHtml, String bodyHtml, String footerHtml) {
        this.headerHtml = headerHtml;
        this.bodyHtml = bodyHtml;
        this.footerHtml = footerHtml;
    }

    public String getHeaderHtml() {
        return headerHtml;
    }

    public void setHeaderHtml(String headerHtml) {
        this.headerHtml = headerHtml;
    }

    public String getBodyHtml() {
        return bodyHtml;
    }

    public void setBodyHtml(String bodyHtml) {
        this.bodyHtml = bodyHtml;
    }

    public String getFooterHtml() {
        return footerHtml;
    }

    public void setFooterHtml(String footerHtml) {
        this.footerHtml = footerHtml;
    }
}
