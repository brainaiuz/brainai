package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.PredefinedValueItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.KpiSelect2;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiSelectEmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.html.Heading;

import java.util.ArrayList;

public class DropDownPermissionSideNavBox extends KpiSideNavBox implements Constants, Colapse {

    private FlowPanel panel;
    private KpiSelect2 editRole;
    private KpiSelect2 viewRole;
    private WfmButton2 saveAndCloseButton;
    private WfmButton2 closeButton;
    private final String value;
    private MultiSelectEmployeeLookUp employeeCanEdit;
    private MultiSelectEmployeeLookUp employeeCanView;
    private PredefinedValueItem item;


    public DropDownPermissionSideNavBox(Integer customFieldId, String value) {
        this.value = value;
        ProfileService.App.get().getPredefinedValueRoles(customFieldId, value, new AsyncCallback<PredefinedValueItem>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(PredefinedValueItem result) {
                item = result;
                init();
            }
        });
    }

    private void init() {
        panel = new FlowPanel();
        Heading header = new Heading(HeadingSize.H1);
        header.setText(value != null ? value : wfmStrings.roles());
        addHeader(header);

        editRole = new KpiSelect2(true);
        editRole.setItems(item.getAllRoles());
        if (item.getChangeRoles() != null) {
            editRole.setSelectedItems(item.getChangeRoles());
        }
        editRole.onValueChangeHandler(cl -> onChangeEditRole(true));

        employeeCanEdit = new MultiSelectEmployeeLookUp();
        if (item.getChangeEmployees() != null) {
            employeeCanEdit.setSelectedItems(item.getChangeEmployees());
        }
        employeeCanEdit.getFilterParametrs().setHRMS(true);
        employeeCanEdit.addStyleName(Constants.DEFAULT_WIDTH);

        employeeCanView = new MultiSelectEmployeeLookUp();
        if (item.getViewEmployees() != null) {
            employeeCanView.setSelectedItems(item.getViewEmployees());
        }
        employeeCanView.getFilterParametrs().setHRMS(true);
        employeeCanView.addStyleName(Constants.DEFAULT_WIDTH);

        viewRole = new KpiSelect2(true);
        onChangeEditRole(false);
        if (item.getViewRoles() != null) {
            viewRole.setSelectedItems(item.getViewRoles());
        }

        panel.add(new FormGroup(wfmStrings.changeStatus(), editRole));
        panel.add(new FormGroup("", employeeCanEdit));
        panel.add(new FormGroup(wfmStrings.viewOnly(), viewRole));
        panel.add(new FormGroup("", employeeCanView));

        //init buttons
        saveAndCloseButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        closeButton = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);

        saveAndCloseButton.addClickHandler(sender -> {
            saveAndCloseButton.setEnabled(false);
            save();
        });

        closeButton.addClickHandler(event -> remove());

        addBody(panel);
        addFooter(saveAndCloseButton);
        addFooter(closeButton);
        show();
    }

    private void onChangeEditRole(boolean onChange) {

        ArrayList<SelectItem> viewRoleList = new ArrayList<>();
        if (editRole.getSelectedItems() != null && editRole.getSelectedItems().size() > 0) {
            for (SelectItem allRole : item.getAllRoles()) {
                if (!editRole.getSelectedItems().contains(allRole)) {
                    viewRoleList.add(allRole);
                }
            }
            setSelectedRoles(onChange, viewRoleList);
        } else {
            viewRoleList = item.getAllRoles();
            setSelectedRoles(onChange, viewRoleList);
        }
        viewRole.clear();
        viewRole.setItems(viewRoleList);
    }

    private void setSelectedRoles(boolean onChange, ArrayList<SelectItem> viewRoleList) {
        if (onChange) {
            if (viewRole.getSelectedItems() != null && viewRole.getSelectedItems().size() > 0) {
                for (SelectItem roles : viewRoleList) {
                    for (SelectItem editRole : viewRole.getSelectedItems()) {
                        if (roles.getId().equals(editRole.getId())) {
                            roles.setSelected(true);
                        }
                    }
                }
            }
        } else {
            if (item.getViewRoles() != null && item.getViewRoles().size() > 0) {
                for (SelectItem roles : viewRoleList) {
                    for (SelectItem editRole : item.getViewRoles()) {
                        if (roles.getId().equals(editRole.getId()) && editRole.isSelected()) {
                            roles.setSelected(true);
                        }
                    }
                }
            }
        }
    }

    private void save() {
        if (item != null) {
            item.setChangeRoles(editRole.getSelectedItems());
            item.setChangeEmployees(employeeCanEdit.getSelectedItems());
            item.setViewRoles(viewRole.getSelectedItems());
            item.setViewEmployees(employeeCanView.getSelectedItems());
            ProfileService.App.get().savePredefinedValueRoles(item, new AbstractAsyncCallback<Void>() {
                @Override
                public void failure(Throwable throwable) {
                    super.failure(throwable);
                }

                @Override
                public void success(Void result) {
                    super.success(result);
                    LoadingPanel.loading(false);
                    remove();
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.changes()), Info.Type.INFO);
                }
            });
        }
    }
}
