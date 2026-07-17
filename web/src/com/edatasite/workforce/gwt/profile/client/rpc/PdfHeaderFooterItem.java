package com.edatasite.workforce.gwt.profile.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PdfHeaderFooterItem implements IsSerializable {
    private String position;
    private String selectedAttribute;


    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSelectedAttribute() {
        return selectedAttribute;
    }

    public void setSelectedAttribute(String selectedAttribute) {
        this.selectedAttribute = selectedAttribute;
    }
}
