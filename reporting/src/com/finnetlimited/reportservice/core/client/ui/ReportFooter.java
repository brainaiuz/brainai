package com.finnetlimited.reportservice.core.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: ${Dilsh0d}
 * Date: 10-Mar-2010
 * Time: 16:49:40
 */
public class ReportFooter extends Composite implements Constants {

    private static final String id = "exportPanel";

    interface FooterBinder extends UiBinder<Widget, ReportFooter> {
    }

    public static final FooterBinder header = GWT.create(FooterBinder.class);

    @UiField
    Anchor police;
    @UiField
    Anchor termsUs;
    @UiField
    Anchor contactUs;

    @UiHandler("police")
    public void policeClicked(ClickEvent event) {
        Window.open("http://www.workforcetrack.com/content/privacy", "_blank", commonParamForUrl);
    }

    @UiHandler("termsUs")
    public void termsUsClicked(ClickEvent event) {
        Window.open("http://www.workforcetrack.com/content/terms-of-use", "_blank", commonParamForUrl);
    }

    @UiHandler("contactUs")
    public void contactUsClicked(ClickEvent event) {
        Window.open("http://www.workforcetrack.com/content/contact-us", "_blank", commonParamForUrl);
    }

    public ReportFooter() {
        initWidget(header.createAndBindUi(this));
    }

    public void addTopButtonPanel() {

    }
}
