package com.edatasite.workforce.gwt.core.client.ui.components;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.CheckBox;

public class KpiCheckBox extends CheckBox {

    public KpiCheckBox() {
        super();
    }

    public KpiCheckBox(SafeHtml label) {
        super(label);
    }

    public KpiCheckBox(String label) {
        super(label, true);
    }

    public KpiCheckBox(String label, boolean asHTML) {
        super(label, asHTML);
    }

    @Override
    public void setText(String text) {
        super.setHTML("<span>"+text+"</span>");
    }

    @Override
    public void setHTML(String html) {
        super.setHTML("<span>" + html + "</span>");
    }
}
