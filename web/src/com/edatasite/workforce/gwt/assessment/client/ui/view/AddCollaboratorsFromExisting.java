package com.edatasite.workforce.gwt.assessment.client.ui.view;

import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentService;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.CallbackSynchronizer;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.newemployee.client.rpc.NewEmployee;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Aug 17, 2009
 * Time: 7:29:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddCollaboratorsFromExisting extends FlowPanel implements Constants {

    private final CallbackSynchronizer callbacksynchronizer = new CallbackSynchronizer();
    private WfmForm.Field titleField;
    private WfmForm employeeAddForm;
    private WfmButton2 saveEmployees;
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private SelectItem[] departmentsItems;

    private Integer appraisedEmployeeID;

    private List addeddEmployees;

    private List validCollaboratorsFields = new ArrayList();
    private static final String LISTBOX_WIDTH = "160px";
    private FlexTable buttonTable;
    public SimpleLink addMoreEmployee;

    public AddCollaboratorsFromExisting() {
        init();
    }

    private void init() {
        addeddEmployees = new ArrayList();
        addMoreEmployee = new SimpleLink(wfmStrings.addMore(), SimpleLink.ADD_ICON);
        addMoreEmployee.setWidth("60px");
        saveEmployees = new WfmButton2(wfmStrings.save());

        employeeAddForm = new WfmForm(new String[]{"1%", "24%", "24%", "24%", "24%"});
        buttonTable = new FlexTable();
        buttonTable.setCellPadding(15);
        buttonTable.setCellSpacing(15);
        buttonTable.getCellFormatter().setWidth(0, 0, "30%");
        buttonTable.getCellFormatter().setWidth(0, 1, "23%");
        buttonTable.getCellFormatter().setWidth(0, 2, "10%");
        buttonTable.getCellFormatter().setWidth(0, 3, "15%");
        buttonTable.getCellFormatter().setWidth(0, 4, "12%");
        buttonTable.setWidget(0, 0, addMoreEmployee);
        buttonTable.setWidget(1, 1, saveEmployees);
        employeeAddForm.addOutButton(buttonTable);

        addMoreEmployee.addClickHandler(sender -> addRow());
        saveEmployees.addClickHandler(widget -> saveCollaborator());


        for (int i = 0; i < 4; i++) {
            addRow();
        }

        add(employeeAddForm);
//        layout(true);
    }


    private void addRow() {
        if (titleField == null) {
            titleField = employeeAddForm.addField(null, new Widget[]{
                    new HTML("<b class=customTitle>" + wfmStrings.role() + "<font color='red'>*</font>:</b>"),
                    new HTML("<b class=customTitle>" + Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()) + "<font color='red'>*</font>:</b>"),
                    new HTML("<b class=customTitle>" + hrmsStrings.collaborator() + "<font color='red'>*</font>:</b>")}, false);
            saveEmployees.setVisible(true);
        }

        SimpleLink removeLink = new SimpleLink(wfmStrings.delete(), SimpleLink.REMOVE_ICON);
        removeLink.setWidth("50px");

        final DataListBox roles = new DataListBox();
        roles.setItems(new SelectItem[]{new SelectItem(PM, "Manager"), new SelectItem(MEM, "Peer"), new SelectItem(CLIENT, "Client")});
        roles.setWidth(LISTBOX_WIDTH);

        final DataListBox departmentsBox = new DataListBox();
        departmentsBox.setWidth(LISTBOX_WIDTH);

        final DataListBox employeesBox = new DataListBox();
        employeesBox.setWidth(LISTBOX_WIDTH);
        setNoCollaborator(employeesBox);


        fillDeparmtnentList(departmentsBox);

        final WfmForm.Field field;

        roles.addValueChangeHandler(widget -> {
            if (roles.getSelectedId() != null) {
                if (CLIENT.equals(roles.getSelectedId())) {
                    departmentsBox.setEnabled(false);
                    fillClientList(employeesBox);
                } else {
                    departmentsBox.setEnabled(true);
                    employeeListRefresher(roles, departmentsBox, employeesBox);
                }

            }
        });

        departmentsBox.addValueChangeHandler(widget -> employeeListRefresher(roles, departmentsBox, employeesBox));


        field = employeeAddForm.addField(null, new Widget[]{roles, departmentsBox, employeesBox, removeLink}, false);

        removeLink.addClickHandler(widget -> {
            employeeAddForm.removeField(field);
            if (employeeAddForm.getFields().size() < 2) {
                employeeAddForm.removeField(titleField);
                titleField = null;
                saveEmployees.setVisible(false);
            }
        });

//        setScrollEnabled(true);
//        layout(true);
    }

    private void setNoCollaborator(DataListBox collaBox) {
        collaBox.clear();
        collaBox.setWithoutNullLabel(true);
        collaBox.setItems(new SelectItem[]{new SelectItem(0, hrmsStrings.noCollaborators())});
    }

    private void fillClientList(final DataListBox employee) {
        setNoCollaborator(employee);
        LoadingPanel.loading(true);
        AssessmentService.App.get().getCompanyClientContacts(callbacksynchronizer.registerCallback(new AbstractAsyncCallback<SelectItem[]>() {
            public void success(SelectItem[] clientItems) {
                if (clientItems != null && clientItems.length > 0) {
                    employee.setWithoutNullLabel(false);
                    employee.setItems(clientItems);
                }
            }
        }));
    }

    private void employeeListRefresher(DataListBox role, DataListBox dep, DataListBox empl) {
        setNoCollaborator(empl);
        if (role.getSelectedId() != null && dep.getSelectedId() != null) {
            fillEmployeeList(empl, role.getSelectedItem().getId(), dep.getSelectedItem().getId());
        }
    }

    private void fillEmployeeList(final DataListBox employeeBox, Integer roleId, Integer departmentId) {
        LoadingPanel.loading(true);
        AssessmentService.App.get().getEmployeeByDepartment(departmentId, roleId, appraisedEmployeeID, callbacksynchronizer.registerCallback(new AbstractAsyncCallback<SelectItem[]>() {
            public void success(SelectItem[] items) {
                if (items != null && items.length > 0) {
                    employeeBox.setWithoutNullLabel(false);
                    employeeBox.setItems(items);
                } else {
                    setNoCollaborator(employeeBox);
                }
            }
        }));

    }


    private void fillDeparmtnentList(final DataListBox departmentBox) {
        if (departmentsItems == null) {
            LoadingPanel.loading(true);
            AssessmentService.App.get().getCompanyDepartments(callbacksynchronizer.registerCallback(new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(SelectItem[] items) {
                    LoadingPanel.loading(false);
                    departmentsItems = items;
                    departmentBox.setItems(items);
                }
            }));
        } else {
            departmentBox.setItems(departmentsItems);
        }
    }

    private boolean validateCollaborator() {
        boolean valid = true;
        validCollaboratorsFields = new ArrayList();
        if (employeeAddForm != null) {
            employeeAddForm.cleanupErrors();
            Iterator iterator = employeeAddForm.getFields().iterator();
            int errors = 0;
            int empty = 0;
            StringBuffer errorMessage;
            while (iterator.hasNext()) {
                WfmForm.Field field = (WfmForm.Field) iterator.next();
                if (field != titleField) {
                    Widget[] widgets = field.getWidgets();
                    if (widgets != null) {
                        errors = 0;
                        errorMessage = new StringBuffer();

                        if (!Validation.validateListBoxRequired((ListBox) widgets[0], hrmsStrings.chooseRole(), errorMessage)) {
                            errorMessage.append("<br/>");
                            errors++;
                        }

                        if (!Validation.validateListBoxRequired((ListBox) widgets[1], Property.get(Constants.DEPARTMENT_LIST, hrmsStrings.chooseDepartment(), wfmStrings.department()), errorMessage)) {
                            errorMessage.append("<br/>");
                            errors++;
                        }
                        if (!Validation.validateListBoxRequired((ListBox) widgets[2], wfmStrings.chooseEmployee(), errorMessage)) {
                            errors++;
                        }

                        if (errors > 0 && errors != 3 && errors != 0) {
                            field.setErrorMessage(errorMessage.toString(), null);
                            valid = false;
                        } else if (errors != 3) {
                            validCollaboratorsFields.add(field);
                        }
                    }
                }
            }

        }

        return valid;
    }

    private void saveCollaborator() {
        NewEmployee[] employees = null;
        if (validateCollaborator()) {
            if (validCollaboratorsFields.size() > 0) {
                Iterator iterator = validCollaboratorsFields.iterator();
                employees = new NewEmployee[validCollaboratorsFields.size()];
                int i = 0;
                while (iterator.hasNext()) {
                    WfmForm.Field field = (WfmForm.Field) iterator.next();
                    Widget[] widgets = field.getWidgets();
                    DataListBox roleBox = (DataListBox) widgets[0];
                    DataListBox employeeBox = (DataListBox) widgets[2];

                    employees[i] = new NewEmployee();
                    employees[i].setObjectID(employeeBox.getSelectedItem().getId());
                    employees[i].setFname(employeeBox.getSelectedItem().getName());
                    employees[i].setRole(roleBox.getSelectedItem().getId());
                    i++;
                }
            }

            refreshOnDemand(employees);
        }
    }

    private void refreshOnDemand(NewEmployee[] employees) {
        if (parentList != null) {
            parentList.refresh((shell, command) -> {
                shell.close();
                command.execute();
            }, employees);
        }
    }


    public RefreshParentList parentList;

    public void setParentList(RefreshParentList parentList, Integer apprasedEmployeeId) {
        this.parentList = parentList;
        this.appraisedEmployeeID = apprasedEmployeeId;
    }

    public interface RefreshParentList {
        void refresh(ShellExecutor executor, NewEmployee[] employees);
    }

    public interface ShellExecutor {
        void executeStrategy(KpiModal shell, Command command);
    }

    public void setAppraisedEmployeeID(Integer appraisedEmployeeID) {
        this.appraisedEmployeeID = appraisedEmployeeID;
    }
}
