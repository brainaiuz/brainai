package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.ReferenceParentEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.KpiSelect2;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiSelectEmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.ui.FlowPanel;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.html.Heading;

import java.util.ArrayList;

public class ReferencePermissionSideNavBox extends KpiSideNavBox implements Constants, Colapse {

    private ReferenceItem item;
    private FlowPanel panel;
    private KpiSelect2 editRole;
    private KpiSelect2 viewRole;
    private KpiSelect2 opportunityEditButtonRole;
    private WfmButton2 saveAndCloseButton;
    private WfmButton2 closeButton;
    private ArrayList<SelectItem> rolesList;
    private boolean isKanban;
    private final String parentCode;
    private final String title;
    private DynamicTable employeeAssignTable;
    private MultiSelectEmployeeLookUp employeeCanEdit;
    private MultiSelectEmployeeLookUp employeeCanView;
    private MultiSelectEmployeeLookUp employeeCanEditButton;


    public ReferencePermissionSideNavBox(Integer objectID, String parentCode, String title) {
        this.parentCode = parentCode;
        this.title = title;
        AllInOneService.App.get().getReference(objectID, new AbstractAsyncCallback<ReferenceItem>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(ReferenceItem result) {
                item = result;
                isKanban = ReferenceParentEnum._OPPORTUNITY_STAGE.name().equals(parentCode) || ReferenceParentEnum._LEAD_STATUS.name().equals(parentCode) || ReferenceParentEnum._CASE_STATUS.name().equals(parentCode) || ReferenceParentEnum._TASK_STATUS.name().equals(parentCode) || ReferenceParentEnum._CANDIDATE_STATUS.name().equals(parentCode);
                rolesList = item.getAllRoles();
                init();
            }
        });
    }

    private void init() {
        panel = new FlowPanel();
        Heading header = new Heading(HeadingSize.H1);
        header.setText(title != null ? title : wfmStrings.roles());
        addHeader(header);

        editRole = new KpiSelect2(true);
        editRole.setItems(item.getAllowedRoles());
        editRole.onValueChangeHandler(cl -> {
            onChangeEditRole(true);
        });

        employeeCanEdit = new MultiSelectEmployeeLookUp();
        employeeCanEdit.setItems(null, item.getEmployeeCanEdit().toArray(new SelectItem[]{}));
        employeeCanEdit.getFilterParametrs().setHRMS(true);
        employeeCanEdit.addStyleName(Constants.DEFAULT_WIDTH);

        employeeCanView = new MultiSelectEmployeeLookUp();
        employeeCanView.setItems(null, item.getEmployeeCanView().toArray(new SelectItem[]{}));
        employeeCanView.getFilterParametrs().setHRMS(true);
        employeeCanView.addStyleName(Constants.DEFAULT_WIDTH);

        employeeCanEditButton = new MultiSelectEmployeeLookUp();
        employeeCanEditButton.setItems(null, item.getEmployeeCanEditButton().toArray(new SelectItem[]{}));
        employeeCanEditButton.getFilterParametrs().setHRMS(true);
        employeeCanEditButton.addStyleName(Constants.DEFAULT_WIDTH);

        viewRole = new KpiSelect2(true);
        onChangeEditRole(false);

        opportunityEditButtonRole = new KpiSelect2(true);
        opportunityEditButtonRole.setItems(item.getOppEditBtnRole());

        panel.add(new FormGroup(wfmStrings.changeStatus(), editRole));
        panel.add(new FormGroup("", employeeCanEdit));
        if (isKanban) {
            panel.add(new FormGroup(wfmStrings.viewOnly(), viewRole));
            panel.add(new FormGroup("", employeeCanView));
        }
        if (ReferenceParentEnum._OPPORTUNITY_STAGE.name().equals(parentCode) || ReferenceParentEnum._CANDIDATE_STATUS.name().equals(parentCode)) {
            panel.add(new FormGroup(wfmStrings.editForm(), opportunityEditButtonRole));
            panel.add(new FormGroup("", employeeCanEditButton));
        }

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
        viewRoleList.clear();
        if (editRole.getSelectedItems() != null && editRole.getSelectedItems().size() > 0) {
            for (SelectItem allRole : rolesList) {
                if (!editRole.getSelectedItems().contains(allRole)) {
                    viewRoleList.add(allRole);
                }
            }
            setSelectedRoles(onChange, viewRoleList);
        } else {
            viewRoleList = rolesList;
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
            if (item.getAllowedRolesView() != null && item.getAllowedRolesView().size() > 0) {
                for (SelectItem roles : viewRoleList) {
                    for (SelectItem editRole : item.getAllowedRolesView()) {
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
            item.setAllowedRoles(editRole.getSelectedItems());
            item.setEmployeeCanEdit(employeeCanEdit.getSelectedItems());
            if (isKanban) {
                item.setAllowedRolesView(viewRole.getSelectedItems());
                item.setEmployeeCanView(employeeCanView.getSelectedItems());
            }
            if (ReferenceParentEnum._OPPORTUNITY_STAGE.name().equals(parentCode) || ReferenceParentEnum._CANDIDATE_STATUS.name().equals(parentCode)) {
                item.setOppEditBtnRole(opportunityEditButtonRole.getSelectedItems());
                item.setEmployeeCanEditButton(employeeCanEditButton.getSelectedItems());
            }
            AllInOneService.App.get().saveReferencePermission(item, new AbstractAsyncCallback<Void>() {
                @Override
                public void failure(Throwable throwable) {
                }

                @Override
                public void success(Void result) {
                    LoadingPanel.loading(false);
                    remove();
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.changes()), Info.Type.INFO);
                }
            });
        }
    }
}
