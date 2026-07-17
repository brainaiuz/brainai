package com.edatasite.workforce.gwt.core.client.ui.components;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.jquery.client.api.Event;
import gwt.material.design.jquery.client.api.Functions;

import static gwt.material.design.jquery.client.api.JQuery.$;

public class InvoiceCollapseLink extends Composite {
    private static final String MORE_OPTIONS = "inv_moreoptions";
    private static final String FORM_HEADER = "inv_form_header";
    private HTMLPanel htmlPanel;
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private MaterialPanel pnlCollapseLink;
    private MaterialLink collapseLink;
    private String moreOptionsId;
    private String invHeadId;
    private String hideText;
    public InvoiceCollapseLink() {
        this(null, null, false);
    }

    public InvoiceCollapseLink(String headId, String moreId) {
        this(headId, moreId, false);
    }

    public InvoiceCollapseLink(boolean collapsed) {
        this(null, null, collapsed);
    }

    public InvoiceCollapseLink(String headId, String moreId, boolean collapsed) {
        invHeadId = headId == null || moreId == null ? FORM_HEADER : headId;
        moreOptionsId = headId == null || moreId == null ? MORE_OPTIONS : moreId;
        pnlCollapseLink = new MaterialPanel(collapsed ? "btn-aux btn-aux--binary" : "btn-aux btn-aux--binary btn-aux--hide");
        initWidget(pnlCollapseLink);

        collapseLink = new MaterialLink();
        collapseLink.getSpan().setStyleName("btn-aux__title");
        pnlCollapseLink.add(collapseLink);

        Icon icon = new Icon();
        icon.setClass("btn-aux__icon ficon--list-open");
        collapseLink.add(icon);

        String showText = wfmStrings.showOptions();
        hideText = wfmStrings.hideOptions();

        collapseLink.setText(collapsed ? hideText : showText);

        collapseLink.addClickHandler(ch -> {
            /*$(htmlPanel.getElementById(moreOptionsId)).slideToggle(500, new Functions.Func() {
                @Override
                public void call() {

                    if ($(htmlPanel.getElementById(invHeadId)).hasClass("section-box--collapse")) {
                        $(htmlPanel.getElementById(invHeadId)).removeClass("section-box--collapse");
                        pnlCollapseLink.removeStyleName("btn-aux--hide");
                        collapseLink.setText(hideText);
                    } else {
                        $(htmlPanel.getElementById(invHeadId)).addClass("section-box--collapse");
                        pnlCollapseLink.addStyleName("btn-aux--hide");
                        collapseLink.setText(showText);
                    }
                }
            });*/
            collaplse();
        });

    }

    public void setHtmlPanel(HTMLPanel panel) {
        this.htmlPanel = panel;
        $(htmlPanel).on(Utils.SCROLL_EVENT, new Functions.EventFunc() {
            @Override
            public Object call(Event e) {
                $(htmlPanel.getElementById(moreOptionsId)).slideDown(500);
                $(htmlPanel.getElementById(invHeadId)).removeClass("section-box--collapse");
                pnlCollapseLink.removeStyleName("btn-aux--hide");
                collapseLink.setText(hideText);
                return null;
            }
        });
    }

    public void collaplse() {
        $(htmlPanel.getElementById(moreOptionsId)).slideToggle(500, new Functions.Func() {
            @Override
            public void call() {

                if ($(htmlPanel.getElementById(invHeadId)).hasClass("section-box--collapse")) {
                    $(htmlPanel.getElementById(invHeadId)).removeClass("section-box--collapse");
                    pnlCollapseLink.removeStyleName("btn-aux--hide");
                    collapseLink.setText(hideText);
                } else {
                    $(htmlPanel.getElementById(invHeadId)).addClass("section-box--collapse");
                    pnlCollapseLink.addStyleName("btn-aux--hide");
                    collapseLink.setText(wfmStrings.showOptions());
                }
            }
        });
    }
}