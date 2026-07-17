package com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass;

import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.notification.NotificationItem;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.tabs.KpiTabContainer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;

public class NavToolBar extends Composite {
    interface NavToolBarUiBinder extends UiBinder<Widget, NavToolBar> {
    }

    private static NavToolBarUiBinder ourUiBinder = GWT.create(NavToolBarUiBinder.class);

    @UiField
    MaterialPanel companyContainer;
    @UiField
    MaterialPanel addNewMenuContainer;
    @UiField
    MaterialPanel emailAccountMenuContainer;
    @UiField
    MaterialLink emailAccountMenu;
    @UiField
    MaterialDropDown emailAccountMenuList;
    @UiField
    UserMenu userMenu;
    @UiField
    KpiTabContainer tabContainer;

    public NavToolBar() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void initRemoveSampleLink() {
        userMenu.initRemoveSampleLink();
    }

    public Widget getRemoveSampleLink() {
        return userMenu.getRemoveSampleLink();
    }

    public void enableMCBar() {
        emailAccountMenuContainer.getElement().getStyle().setDisplay(Style.Display.BLOCK);
    }

    public MaterialLink getEmailAccountMenu() {
        return emailAccountMenu;
    }

    public MaterialDropDown getEmailAccountMenuList() {
        return emailAccountMenuList;
    }

    public void drawNotifications(ListResult<NotificationItem> result) {
        userMenu.drawNotifications(result);
    }

    public void drawEmails(ListResult<Email> result) {
        userMenu.drawEmails(result);
    }

    public void initUserMenu() {
        userMenu.init();
    }

    public void setTimerData(Integer objectID, int pmTask, Integer projectId) {
        userMenu.setTimerData(objectID, pmTask, projectId);
    }

    public KpiTabContainer getTabContainer() {
        return tabContainer;
    }
}
