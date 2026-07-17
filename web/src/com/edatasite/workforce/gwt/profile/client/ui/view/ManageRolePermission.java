package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.ContentHeader;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.PermissionItem;
import com.edatasite.workforce.gwt.core.client.rpc.RoleListItem;
import com.edatasite.workforce.gwt.core.client.ui.customtabbar.CustomTabBar;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.profile.client.rpc.PermissionColumnsItem;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: Fatkhulla
 * Date: 23.05.12
 * Time: 15:41
 * To change this template use File | Settings | File Templates.
 */
public class ManageRolePermission extends View implements FittedContent, ContentHeader {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public ManageRolePermission() {
        super("rolePermission", wfmStrings.permissions());
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    protected Widget onInitialize() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ROLE_ADD, ManageRolePermission.this, (sender, args) -> {
            clear();
            getData();
        });

        getData();
        return null;
    }

    private void getData() {
        ProfileService.App.get().getRolesPermissions(Utils.isSuperUser(), new AsyncCallback<PermissionColumnsItem>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(PermissionColumnsItem listTab) {
                draw(listTab.getSectionList(), listTab.getRoleList());
            }
        });
    }

    private void draw(final List<PermissionItem> listTab, List<RoleListItem> allRole) {

        CustomTabBar managePermissionTab = new CustomTabBar(listTab.size());
        managePermissionTab.setMargin("5px 5px 5px 0px");
        managePermissionTab.setWidth("100%");
        managePermissionTab.setStyleName("settings_permission-management");
        for (PermissionItem item : listTab) {
            CustomSectionTabs customSectionTabs = new CustomSectionTabs(item.getContext(), item.getLocalizationName(), item.getRoleList());
            customSectionTabs.setRoleList(allRole);
            customSectionTabs.setPanelSize();
            managePermissionTab.addWidget(customSectionTabs);
        }
        add(managePermissionTab);
        managePermissionTab.selectTab(0);
    }


    @Override
    protected void onAttach() {
        super.onAttach();
        RootPanel.get().addStyleName("has-customScrollArea");
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        RootPanel.get().removeStyleName("has-customScrollArea");
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
