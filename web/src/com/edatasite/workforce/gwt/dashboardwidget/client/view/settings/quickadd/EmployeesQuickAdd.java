package com.edatasite.workforce.gwt.dashboardwidget.client.view.settings.quickadd;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CustomCommand;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.newemployee.client.rpc.NewEmployee;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Label;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.List;

public class EmployeesQuickAdd extends Composite implements Constants {

    interface EmployeesQuickAddUiBinder extends UiBinder<Widget, EmployeesQuickAdd> {
    }

    private static final EmployeesQuickAddUiBinder ourUiBinder = GWT.create(EmployeesQuickAddUiBinder.class);

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    @UiField
    HTMLPanel panel;
    @UiField
    Span header;
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
    Label essLabel;
    @UiField
    CheckBox ess;
    @UiField
    WfmButton2 inviteBtn;

    private CustomCommand<String> command;
    boolean open;
    final int WARN_DURATION = 6000;

    public EmployeesQuickAdd(boolean open) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.open = open;
        if (open) {
            addStyleName("fade--in");
        } else {
            addStyleName("fade--out");
        }
        AllInOneService.App.get().getRolesCheckAdmin(new AbstractAsyncCallback<ArrayList<SelectItem>>() {
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                initInternal(getRoleWithCustom(true));
            }

            public void onSuccess(ArrayList<SelectItem> roleList) {
                LoadingPanel.loading(false);
                if (roleList != null && roleList.size() > 0) {
                    initInternal(roleList);
                }
            }
        });
    }

    private void initInternal(List<SelectItem> roles) {
        firstnameLabel.setText(wfmStrings.firstName());
        lastnameLabel.setText(wfmStrings.lastName());
        emailLabel.setText(wfmStrings.email());
        roleLabel.setText(wfmStrings.role());
        essLabel.setText(wfmStrings.ess());
        inviteBtn.setText(wfmStrings.inviteUser());

        Icon info = new Icon();
        info.addStyleName("ficon--info");
        essLabel.add(info);
        new KpiToolTip(info, "Invite users to their self-service portal");

        email.addKeyUpHandler(event -> {
            if (!"".equals(email.getValue())) {
                header.getElement().setInnerHTML(email.getValue());
            } else {
                header.getElement().setInnerHTML("&nbsp;");
            }
        });

        if (roles != null && roles.size() > 0) {
            employeeRole.setItems(roles.toArray(new SelectItem[]{}));
        }

        ess.addValueChangeHandler(event -> {
            if (ess.getValue()) {
                employeeRole.clearSelected();
                employeeRole.setEnabled(false);
            } else {
                employeeRole.setEnabled(true);
            }
        });

        employeeRole.addValueChangeHandler(event -> {
            if (employeeRole.isSomethingSelected()) {
                ess.setValue(false);
                ess.setEnabled(false);
            } else {
                ess.setEnabled(true);
            }
        });

        inviteBtn.addClickHandler(event -> {
            inviteBtn.setEnabled(false);
            if (validate()) {
                save();
            } else {
                inviteBtn.setEnabled(true);
            }
        });
    }

    private List<SelectItem> getRoleWithCustom(boolean getWithLocation) {
        ArrayList<SelectItem> roles = new ArrayList<>();
        roles.add(new SelectItem(MEM, wfmStrings.employee()));
        if (!Utils.hasRole(ADMIN_LOCATION) || ((Utils.hasRole(ADMIN) || Utils.hasRole(DR)) && Utils.hasRole(ADMIN_LOCATION))) {
            roles.add(new SelectItem(ADMIN, wfmStrings.administrator()));
            roles.add(new SelectItem(DR, wfmStrings.director()));
        }
        roles.add(new SelectItem(ACCOUNTANT, wfmStrings.accountant()));
        roles.add(new SelectItem(HR, wfmStrings.hrManager()));
        roles.add(new SelectItem(SALESMAN, wfmStrings.salesManager()));
        if (getWithLocation) {
            roles.add(new SelectItem(CUSTOMER_SERVICE_REPRESENTATIVE, wfmStrings.customerServiceRepresentative()));
        }
        roles.add(new SelectItem(SALESPERSON, wfmStrings.salesPerson()));

        return roles;
    }

    public NewEmployee getData() {
        NewEmployee item = new NewEmployee();

        item.setFname(firstname.getText());
        item.setLname(lastname.getText());
        item.setEmail(email.getText());
        item.setRole(employeeRole.getSelectedId());
        item.setEssUser(ess.getValue());

        return item;
    }

    private void save() {
        LoadingPanel.loading(true, panel);
        EmployeeService.App.get().createEmployee(getData(), false, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.sorrySomethingWentWrong(), Info.Position.BOTTOM_RIGHT);
                inviteBtn.setEnabled(true);
            }

            @Override
            public void onSuccess(Integer result) {
                LoadingPanel.loading(false);
                inviteBtn.setEnabled(true);
                if (result == -1) {
                    email.addStyleName(ERROR_FORM_STYLE);
                    Info.warn(wfmStrings.sorryEmailWithThisNameAlreadyExists(), Info.Position.BOTTOM_RIGHT, WARN_DURATION);
                } else if (result == -3) {
                    email.addStyleName(ERROR_FORM_STYLE);
                    Info.warn(wfmStrings.enterCorrectEmailAddress(), Info.Position.BOTTOM_RIGHT, WARN_DURATION);
                } else if (result == -7) {
                    Info.warn(wfmStrings.youAlreadyHaveAIdWithThisNumber(), Info.Position.BOTTOM_RIGHT, WARN_DURATION);
                } else if (result == -11) {

                    Info.warn(wfmStrings.usersLimitExceeded(), Info.Position.BOTTOM_RIGHT, 7000);
                } else if (result == -12) {
                    Info.warn(wfmStrings.userLimitNoAccessExceeded(), Info.Position.BOTTOM_RIGHT, WARN_DURATION);
                } else if (result == -13) {
                    Info.warn(wfmStrings.userLimitEssExceeded(), Info.Position.BOTTOM_RIGHT, WARN_DURATION);
                } else if (result > 0) {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.newEmployee()), Info.Position.BOTTOM_RIGHT);

                    if (command != null) {
                        command.execute(email.getText());
                    }
                    toggle();
                }
            }
        });
    }

    public boolean validate() {
        clearError();
        if (!Validation.validateTextBoxRequired(firstname)) {
            Info.warn(wfmStrings.pleaseEnterFirstName(), Info.Position.BOTTOM_RIGHT);
            return false;
        }

        if (!Validation.validateEmailRequired(email)) {
            Info.warn(wfmStrings.pleaseEnterCorrectEmailAddress(), Info.Position.BOTTOM_RIGHT);
            return false;
        }

        return true;
    }

    private void clearError() {
        firstname.removeStyleName(ERROR_FORM_STYLE);
        email.removeStyleName(ERROR_FORM_STYLE);
    }

    private void clearForm() {
        header.setText("");
        firstname.setText("");
        lastname.setText("");
        email.setText("");
        employeeRole.clearSelected();
        ess.setValue(false);
        ess.setEnabled(true);
    }

    public void toggle() {
        clearForm();
        if (open) {
            removeStyleName("fade--in");
            addStyleName("fade--out");
            open = false;
        } else {
            removeStyleName("fade--out");
            addStyleName("fade--in");
            open = true;
        }
    }

    public void setCommand(CustomCommand command) {
        this.command = command;
    }
}
