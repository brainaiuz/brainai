package com.edatasite.workforce.gwt.dashboardwidget.client.view.settings.quickadd;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Span;

public class EmployeesQuickAddForm extends KpiSideNavBox implements Constants {

    private EmployeesQuickAdd quickAdd;
    private MaterialPanel invitedUsersPanel;
    private WfmButton2 markAsDone, cancelBtn;

    public EmployeesQuickAddForm() {
        super(KpiSideNavBox.DEFAULT_WIDTH);

        initInternal();
        show();
    }

    private void initInternal() {
        //header
        Heading headerText = new Heading(HeadingSize.H1);
        headerText.setText(wfmStrings.inviteUsers());
        headerText.getElement().getStyle().setProperty("textTransform", "none");
        addHeader(headerText);

        //body
        MaterialPanel panel = new MaterialPanel("invite-users");

        MaterialPanel infoPanel = new MaterialPanel("margin-bottom-lg panel");
        Div info = new Div("panel__body");
        info.getElement().setInnerHTML(wfmStrings.inviteUsersInformation());
        infoPanel.add(info);

        quickAdd = new EmployeesQuickAdd(false);
        quickAdd.setCommand((s) -> addToInvited((String) s));

        WfmButton2 addUserButton = new WfmButton2(wfmStrings.addUser(), WfmButton2.BTN_WHITE, e -> quickAdd.toggle());
        addUserButton.setStyleName("btn btn-large btn--darkgrey fadeIn--trigger");
        addUserButton.getElement().getStyle().setProperty("textTransform", "uppercase");

        invitedUsersPanel = new MaterialPanel("bar-stack invited-users");

        panel.add(infoPanel);
        panel.add(addUserButton);
        panel.add(quickAdd);
        panel.add(invitedUsersPanel);
        addBody(panel);

        markAsDone = new WfmButton2(wfmStrings.markAsDone(), WfmButton2.BTN_SUCCESS);
        cancelBtn = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_RESET);

        markAsDone.addClickHandler(event -> command.execute());
        cancelBtn.addClickHandler(event -> remove());

        addFooter(markAsDone);
        addFooter(cancelBtn);
    }

    private void addToInvited(String email) {
        MaterialPanel bar = new MaterialPanel("bar");

        MaterialLink link = new MaterialLink(email);
        link.setHref("mailto:" + email);

        Span icon = new Span();
        icon.setStyleName("bar__icon");
        Element i = DOM.createElement("i");
        i.setClassName("ficon--check");
        icon.getElement().appendChild(i);

        bar.add(link);
        bar.add(icon);

        invitedUsersPanel.add(bar);

        initBodyScrollContent();
    }
}