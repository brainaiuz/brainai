package com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

import java.util.List;

public class CompanySwitcher extends Composite {
    private static final CompanySwitcherUiBinder ourUiBinder = GWT.create(CompanySwitcherUiBinder.class);
    @UiField()
    MaterialPanel container;
    private final List<UserCompanyDTO> companyList;

    public CompanySwitcher(List<UserCompanyDTO> companyList) {
        this.companyList = companyList;
        initWidget(ourUiBinder.createAndBindUi(this));
        drawPanel();
    }

    public void drawPanel() {
        MaterialDropDown ul = null;
        MaterialLink li;
        for (int j = 0; j < companyList.size(); j++) {
            li = new MaterialLink();
            li.setStyle("margin: 0; padding: 0; min-height: 2.5rem");
            li.setHoverable(true);
            Span text = new Span(companyList.get(j).getCompanyName());
            text.addStyleName("prof-comp-list__text");
            li.add(text);
            Span mark = new Span();
            mark.addStyleName("prof-comp-list__mark");
            if ("expired".equals(companyList.get(j).getStatus())) {
                li.addStyleName("status--expired");
            } else if ("active".equals(companyList.get(j).getStatus())) {
                li.addStyleName("status--active");
            } else if ("free".equals(companyList.get(j).getStatus())) {
                li.addStyleName("status--free");
            }
            Icon i = new Icon();
            mark.add(i);
            li.add(mark);
            if (companyList.get(j).isCurrent()) {
                li.setStyle("background-color: #536677;\n" +
                        "    color: #fff !important;\n" +
                        "    cursor: default !important;");
            } else {
                li.setHref(companyList.get(j).getClusterURL());
            }
            if (ul == null) {
                ul = new MaterialDropDown(li);
                ul.setClass("company-switch");
                ul.setStyle("margin: 0; display: block;position: absolute;\n" +
                        "  top: 0 !important;\n" +
                        "  max-height: 300px !important;\n" +
                        "  width: 287px !important;\n" +
                        "  opacity: 1 !important;\n" +
                        "  background-color: $color-base-0;\n" +
                        "  box-shadow: none !important;");
                if (Utils.isArabicCompany()) {
                    ul.addStyleName("prof-comp-list__arabic");
                }
            }
            ul.add(li);
        }
        container.add(ul);
    }

    interface CompanySwitcherUiBinder extends UiBinder<HTMLPanel, CompanySwitcher> {
    }
}