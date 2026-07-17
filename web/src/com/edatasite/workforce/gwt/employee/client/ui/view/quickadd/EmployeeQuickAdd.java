package com.edatasite.workforce.gwt.employee.client.ui.view.quickadd;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.ReportService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.department.DepartmentItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.newemployee.client.rpc.NewEmployee;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Label;

import java.util.ArrayList;

/**
 * User: Anvar Akramov
 * Date: 18.12.2017 10:42
 */
public class EmployeeQuickAdd extends KpiSideNavBox implements Constants {

    interface EmployeeQuickAddFormUiBinder extends UiBinder<Widget, EmployeeQuickAdd> {
    }

    private static final EmployeeQuickAddFormUiBinder ourUiBinder = GWT.create(EmployeeQuickAddFormUiBinder.class);

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    @UiField
    HTMLPanel panel;
    @UiField
    Label firstnameLabel;
    @UiField
    TextBox firstname;
    @UiField
    Label lastnameLabel;
    @UiField
    TextBox lastname;
    @UiField
    Label emailLabel;
    @UiField
    TextBox email;
    @UiField
    Label roleLabel;
    @UiField
    DataListBox employeeRole;
    @UiField
    Label departmentLabel;
    @UiField
    DataListBox department;
    @UiField
    Label positionLabel;
    @UiField
    DataListBox position;

    protected NewEmployee item;
    private WfmButton2 saveBtn;

    private final String debugId = "employee_quick_add_";

    public EmployeeQuickAdd() {
        super(KpiSideNavBox.DEFAULT_WIDTH);
        ourUiBinder.createAndBindUi(this);

        addOpeningHandler(o -> loadData());
        show();
        initInternal();
    }

    private void initInternal() {
        //header
        addHeader(new HTML(wfmStrings.addEmployee()));

        firstnameLabel.setText(wfmStrings.firstName());
        lastnameLabel.setText(wfmStrings.lastName());
        emailLabel.setText(wfmStrings.email());
        roleLabel.setText(wfmStrings.role());
        departmentLabel.setText(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()));
        positionLabel.setText(wfmStrings.position());

        firstname.ensureDebugId(this.debugId + "firstname");
        lastname.ensureDebugId(this.debugId + "lastname");
        email.ensureDebugId(this.debugId + "email");
        employeeRole.ensureDebugId(this.debugId + "role");
        department.ensureDebugId(this.debugId + "department");
        position.ensureDebugId(this.debugId + "position");

        //body
        addBody(panel);

        saveBtn = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveBtn.ensureDebugId(debugId + "save_button");
        saveBtn.addClickHandler(event -> {
            enableButtons(false);
            if (validateForm()) {
                save();
            } else {
                enableButtons(true);
            }
        });

        //footer
        addFooter(saveBtn);
    }

    private void loadData() {
        LoadingPanel.loading(true, panel);
        //Load Departments
        EmployeeService.App.get().getDepartmentsSelectItem(new AbstractAsyncCallback<DepartmentItem[]>() {
            public void success(DepartmentItem[] deps) {
                SelectItem[] items = new SelectItem[deps.length];
                for (int i = 0; i < items.length; i++) {
                    items[i] = new SelectItem(deps[i].getDepatmentID(), deps[i].getDepartmentName());
                }
                department.setItems(items);
                if (items.length == 1) {
                    department.setSelected(items[items.length - 1].getId());
                }
                LoadingPanel.loading(false, panel);
            }
        });

        //Load Roles
        AllInOneService.App.get().getRolesCheckAdmin(new AbstractAsyncCallback<ArrayList<SelectItem>>() {
            public void onFailure(Throwable throwable) {
                employeeRole.setItems(getRoleWithCustom(true));
            }

            public void onSuccess(ArrayList<SelectItem> customRoleList) {
                if (customRoleList != null && customRoleList.size() > 0) {
                    employeeRole.setItems(customRoleList.toArray(new SelectItem[]{}));
                }
            }
        });

        //Load Locations
        ReportService.App.get().getPositionsList(null,new AbstractAsyncCallback<SelectItem[]>() {
            public void failure(Throwable throwable) {

            }

            public void success(SelectItem[] selectItems) {
                position.setItems(selectItems);
            }
        });

    }

    private SelectItem[] getRoleWithCustom(boolean getWithLocation) {
        ArrayList<SelectItem> aa = new ArrayList<>();
        aa.add(new SelectItem(MEM, wfmStrings.employee()));
        if (!Utils.hasRole(ADMIN_LOCATION) || ((Utils.hasRole(ADMIN) || Utils.hasRole(DR)) && Utils.hasRole(ADMIN_LOCATION))) {
            aa.add(new SelectItem(ADMIN, wfmStrings.administrator()));
            aa.add(new SelectItem(DR, wfmStrings.director()));
        }
        aa.add(new SelectItem(ACCOUNTANT, wfmStrings.accountant()));
        aa.add(new SelectItem(HR, wfmStrings.hrManager()));
        aa.add(new SelectItem(SALESMAN, wfmStrings.salesManager()));
        if (getWithLocation) {
            aa.add(new SelectItem(CUSTOMER_SERVICE_REPRESENTATIVE, wfmStrings.customerServiceRepresentative()));
        }
        aa.add(new SelectItem(SALESPERSON, wfmStrings.salesPerson()));

        return aa.toArray(new SelectItem[]{});
    }

    public boolean validateForm() {
        if (firstname.getText() == null || "".equals(firstname.getText())) {
            firstname.addStyleName(ERROR_FORM_STYLE);
            Info.warn(wfmStrings.pleaseEnterFirstName(), Info.Position.BOTTOM_RIGHT);
            return false;
        }

        if (!Utils.validateEmail(email.getText(), false)) {
            email.addStyleName(ERROR_FORM_STYLE);
            Info.warn(wfmStrings.pleaseEnterCorrectEmailAddress(), Info.Position.BOTTOM_RIGHT);
            return false;
        }
        return true;
    }

    public void save() {
        LoadingPanel.loading(true, panel);
        setValuesToRPC();
        EmployeeService.App.get().createEmployee(item, false, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false, panel);
                Info.warn(wfmStrings.sorrySomethingWentWrong(), Info.Position.BOTTOM_RIGHT);
            }

            @Override
            public void success(Integer result) {
                LoadingPanel.loading(false, panel);
                enableButtons(true);
                if (result == -1) {
                    email.setFocus(true);
                    email.addStyleName(ERROR_FORM_STYLE);
                    Info.warn(wfmStrings.sorryEmailWithThisNameAlreadyExists(), Info.Position.BOTTOM_RIGHT);
                } else if (result == -3) {
                    Info.warn(wfmStrings.enterCorrectEmailAddress(), Info.Position.BOTTOM_RIGHT);
                } else if (result == -7) {
                    Info.warn(wfmStrings.youAlreadyHaveAIdWithThisNumber(), Info.Position.BOTTOM_RIGHT);
                } else if (result == -11 || result == -13) {
                    Info.warn(wfmStrings.usersLimitExceeded(), Info.Position.BOTTOM_RIGHT, 7000);
                } else {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.employee()), Info.Position.BOTTOM_RIGHT);
                }
                if (result > 0) {
                    if (command != null) {
                        command.execute();
                    }
                }
            }
        });
    }

    private void setValuesToRPC() {
        item = new NewEmployee();
        item.setFname(firstname.getText());
        item.setLname(lastname.getText());
        item.setEmail(email.getText());
        item.setRole(employeeRole.getSelectedId());
        item.setDepartment(department.getSelectedId());
        item.setPositionId(position.getSelectedId());
    }

    public void enableButtons(boolean enabled) {
        saveBtn.setEnabled(enabled);
    }
}
