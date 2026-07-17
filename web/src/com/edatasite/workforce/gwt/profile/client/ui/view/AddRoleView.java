package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RoleListItem;
import com.edatasite.workforce.gwt.core.client.rpc.RolePermissionService;
import com.edatasite.workforce.gwt.core.client.rpc.RolePermissionServiceAsync;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Created by IntelliJ IDEA.
 * User: Azazello
 * Date: 15.03.18
 * Time: 00:02
 * To change this template use File | Settings | File Templates.
 */
public class AddRoleView extends KpiModal {
    private static final RolePermissionServiceAsync roleService = RolePermissionService.App.get();
    private TextBox name;
    private TextArea description;
    private KpiSwitcher active;
    private WfmButton2 saveAndClose;

    private final Integer id;
    private RoleListItem item;

    public AddRoleView(Integer id) {
        this.id = id;
        setTitle(wfmStrings.addRole());
        setWidth(400);
        init();
        if (id != null) {
            getRole();
        }
        open();
    }

    private void init() {
        name = new TextBox();
        description = new TextArea();
        description.setWidth("100%");
        active = new KpiSwitcher();

        addWidget(name, wfmStrings.name());
        addWidget(description, wfmStrings.description());
        addWidget(active, wfmStrings.active());

        addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> close()));
        saveAndClose = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        addButton(saveAndClose);
    }

    private void getRole() {
        if (id != null) {
            roleService.getRole(id, new AsyncCallback<RoleListItem>() {
                @Override
                public void onFailure(Throwable throwable) {
                }

                @Override
                public void onSuccess(RoleListItem roleItem) {
                    item = roleItem;
                    name.setValue(roleItem.getName());
                    description.setValue((roleItem.getDescription() == null) ? "" : roleItem.getDescription());
                    active.setValue(roleItem.isActive());
                }
            });
        }
    }

    private void save() {
        if(!Validation.validateTextBoxRequired(name)){
            Info.warn(wfmStrings.sureEnteredAllData());
            return;
        }
        item = item == null ? new RoleListItem() : item;
        item.setObjectID(id);
        item.setName(name.getText());
        item.setDescription(description.getValue());
        item.setActive(active.getValue());
        LoadingPanel.loading(true);
        saveAndClose.setEnabled(false);
        roleService.saveRole(item, new AbstractAsyncCallback<Integer>() {

            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                saveAndClose.setEnabled(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Integer result) {
                LoadingPanel.loading(false);
                if (result < 0) {
                    Info.show(wfmStrings.roleAlreadyExist(), Info.Type.WARNING);
                    saveAndClose.setEnabled(true);
                } else {
                    close();
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.role()), Info.Type.INFO);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ROLE_ADD, result, AddRoleView.this);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ROLE_ADD_PERMISSION_CHANGE, result, AddRoleView.this);
                }
            }
        });
    }
}
