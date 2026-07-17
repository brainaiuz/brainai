package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.rpc.HasObjectPermission;
import com.edatasite.workforce.gwt.core.client.rpc.RolePermissionService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.CheckboxSelector;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;

import java.util.ArrayList;


/**
 * Created by IntelliJ IDEA.
 * User: Hayot Rahimov
 * Date: Feb 03, 2018
 * Time: 12:12:44 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class HasObjectPermissionCustomForm extends CustomForm implements Constants {

    //Form fields

    private KpiCheckBox denyCheckBox;
    private CheckboxSelector rolesWidget;


    public HasObjectPermissionCustomForm(String name, String description) {
        super(name, description);
    }


    protected void initInternal() {
        rolesWidget = new CheckboxSelector();
        addTitleField(OBJECT_PERMISSION_TITLE, wfmStrings.permission());
        denyCheckBox = new KpiCheckBox();
        denyCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                rolesWidget.setVisible(event.getValue());
            }
        });
        FlexTable ft = new FlexTable();
        ft.setWidget(0, 0, new Label(wfmStrings.showToSelectedRolesOnly()));
        ft.setWidget(0, 1, denyCheckBox);
        ft.setWidget(1, 1, rolesWidget);
        addField(OBJECT_PERMISSION_ROLES, ft);
    }

    protected void setData() {
        initRoleList();
    }


    private void initRoleList() {
        RolePermissionService.App.get().getRoles(new AsyncCallback<ArrayList<SelectItem>>() {

            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ArrayList<SelectItem> result) {
                HasObjectPermission item = getObjectPermissionForEdit();
                denyCheckBox.setValue(item.isDenied());
                rolesWidget.addItems(result.toArray(new SelectItem[]{}), item.getRoles());
                DeferredCommand.addCommand(new Command() {
                    @Override
                    public void execute() {
                        rolesWidget.setVisible(item.isDenied());
                    }
                });
            }
        });

    }

    protected abstract <T extends HasObjectPermission> T getObjectPermissionForEdit();

    protected <T extends HasObjectPermission> T fillRPCWithValues(T p) {
        p.setDenied(denyCheckBox.getValue().booleanValue());
        p.setRoles(denyCheckBox.getValue().booleanValue() ? rolesWidget.getSelectItems() : new ArrayList<>());
        return p;
    }

}


