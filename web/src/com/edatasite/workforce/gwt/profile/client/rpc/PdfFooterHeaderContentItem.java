package com.edatasite.workforce.gwt.profile.client.rpc;


import com.google.gwt.user.client.rpc.IsSerializable;

public class PdfFooterHeaderContentItem implements IsSerializable {
    private String position;
    private String content;
    private Boolean isEnable;

    public PdfFooterHeaderContentItem(String position, String content, Boolean isEnable) {
        this.position = position;
        this.content = content;
        this.isEnable = isEnable;
    }

    public PdfFooterHeaderContentItem() {
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getEnable() {
        return isEnable;
    }

    public void setEnable(Boolean enable) {
        isEnable = enable;
    }

}
