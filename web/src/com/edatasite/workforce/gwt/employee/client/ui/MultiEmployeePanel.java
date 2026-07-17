package com.edatasite.workforce.gwt.employee.client.ui;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.CoreMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.ReportService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.department.DepartmentItem;
import com.edatasite.workforce.gwt.core.client.ui.CallbackSynchronizer;
//import com.edatasite.workforce.gwt.core.client.ui.CompanyConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.Errors;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.AddListener;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.ColumnStatements;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.newemployee.client.rpc.NewEmployee;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MultiEmployeePanel extends Div implements Constants, Errors {

    public static final String FIRST_NAME = "FIRST_NAME";
    public static final String LAST_NAME = "LAST_NAME";
    public static final String EMAIL = "EMAIL";
    public static final String ROLE_EMPLOYEE = "ROLE_EMPLOYEE";
    public static final String DEPARTMENT = "DEPARTMENT";
    public static final String LOCATION = "LOCATION";
    public static final String EMPLOYEE_TYPE = "EMPLOYEE_TYPE";
    public static final String DRIVER_ID = "DRIVER_ID";

    private static final CoreMessages coreMessages = CoreMessages.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private final CallbackSynchronizer callbacksynchronizer = new CallbackSynchronizer();

    private int maxEmp = 0;
    private int maxNoAccessEmp = 0;
    private int maxEssEmp = 0;

    private SelectItem[] departments;
    private SelectItem[] locations = null;
    private List addeddEmployees;

    private CheckLimit checkLimit;
    private String createdFrom = "";

    private DataListBox employeeType;
    private DynamicTable employeesTable;
    private RefreshParent refreshParent;
//    private final boolean mayAddNoAccessUser = maxNoAccessEmp > 0;
//    private final boolean customizationFor47229 = !mayAddNoAccessUser && CompanyConstants.C47229.equals(Utils.getEncryptedCompanyID());

    private WfmButton2 saveEmployeesButton;

    public MultiEmployeePanel() {
        getEmployeesMaxCount();
        initEmployeesTable();
    }

    private void getEmployeesMaxCount() {
        ReportService.App.get().getEmployeesMaxCount(null, new AbstractAsyncCallback<Integer[]>() {
            public void success(Integer[] result) {
                maxEmp = result[ACTIVE];
                maxNoAccessEmp = result[NO_ACCESS];
                maxEssEmp = result[ESS];
                if (maxEmp > 0 || maxNoAccessEmp > 0 || maxEssEmp > 0) {
                    show1();
                } else {
                    show2();
                }
            }
        });
    }

    private void show2() {
        if (checkLimit != null) {
            checkLimit.limitExceeded();
        }
    }

    public void show1() {
        addeddEmployees = new ArrayList();
        saveEmployeesButton = new WfmButton2(wfmStrings.saveAndClose(), WfmButton2.BTN_PRIMARY);

        for (int i = 1; i <= 3; i++) {
            Widget[] widgets = getWidgetArray();
            employeesTable.addRow(widgets);
        }

        saveEmployeesButton.addClickHandler(widget -> saveEmployees(true));
        add(createFooter());

        int limit = 4;
        if (maxEssEmp <= 0) {
            limit = (maxEmp < 4 ? maxEmp : 4);
            if (limit <= 0) {
                limit = (maxNoAccessEmp < 4 ? maxNoAccessEmp : 4);
            }
        }
    }

    public void saveEmployees(final boolean showLoading) {
        if (validateEmployees()) {
            final NewEmployee[] newEmployees = getNewEmployees();
            if (showLoading) {
                LoadingPanel.loading(true);
            }
            EmployeeService.App.get().createEmployees(newEmployees, new AbstractAsyncCallback<Integer[]>() {
                public void failure(Throwable caught) {
                    if (showLoading) {
                        LoadingPanel.loading(false);
                    }
                }

                public void success(Integer[] employees) {
                    if (showLoading) {
                        LoadingPanel.loading(false);
                    }
                    boolean noError = true;

                    if (employees != null) { //TODO highlight invalid row widgets

                        for (int i = 0; i < employees.length; i++) {

//                            if (validEmployeeFields.contains(field)) {

                            //INVALIDATE EMAIL FIELD
                            if (employees[i] == EMPLOYEE_WITH_THIS_EMAIL_ALREADY_EXISTS) {
                                Info.show(wfmStrings.employeeWithEmailAlreadyExist(), Info.Type.WARNING);
                                noError = false;
                            } else if (employees[i] == EMPLOYEE_WITH_THIS_EMAIL_HOST_DOES_NOT_EXIST) {
                                Info.show(wfmStrings.invalidEmail(), Info.Type.WARNING);
                                noError = false;
                            } else if (employees[i] == CAN_NOT_CREATE_EMPLOYEE) {
                                Info.show(wfmStrings.canNotCreateAnEmployee(), Info.Type.WARNING);
                                noError = false;
                            } else {
//                                    field.setErrorMessage(null, "", 2);
                                newEmployees[i].setObjectID(employees[i]);
                                addeddEmployees.add(newEmployees[i]);
                            }
                            i++;
//                            }
                        }

                        switch (employees[0]) {
                            case ACTIVE_LIMIT_EXCEEDED:
                                Info.show(wfmStrings.usersLimitExceeded(), Info.Type.WARNING);
                                noError = false;
                                break;
                            case NO_ACCESS_LIMIT_EXCEEDED:
                                Info.show(wfmStrings.userLimitNoAccessExceeded(), Info.Type.WARNING);
                                noError = false;
                                break;
                            case ESS_LIMIT_EXCEEDED:
                                noError = false;
                                Info.show(wfmStrings.userLimitEssExceeded(), Info.Type.WARNING);
                                break;
                        }
                    }
                    if (noError) {
                        refreshOnDemand();
                    }

                }
            });
        }
    }

    private NewEmployee[] getNewEmployees() {
        List<NewEmployee> newEmployees = new LinkedList<>();

        for (int i = 0; i < employeesTable.getRowNumber(); i++) {
            if (validateRow(i).isRowValid()) {
                DynamicTableItem row = employeesTable.getItem(i);
                NewEmployee newEmployee = new NewEmployee();
                newEmployee.setCreatedFrom(createdFrom);

                TextBox firstNameBox = (TextBox) row.getColumnById(FIRST_NAME);
                newEmployee.setFname(firstNameBox.getValue());

                TextBox lastNameBox = (TextBox) row.getColumnById(LAST_NAME);
                newEmployee.setLname(lastNameBox.getValue() != null ? lastNameBox.getValue() : "");

                TextBox emailBox = (TextBox) row.getColumnById(EMAIL);
                newEmployee.setEmail(emailBox.getValue().trim());
                newEmployee.setIsFromMultiEmployee(true);

                DataListBox roles = (DataListBox) row.getColumnById(ROLE_EMPLOYEE);
                if (roles != null && roles.getSelectedId() != null) {
                    newEmployee.setRole(roles.getSelectedId());
                }

                DataListBox departments = (DataListBox) row.getColumnById(DEPARTMENT);
                if (departments != null && departments.getSelectedItem() != null) {
                    newEmployee.setDepartment(departments.getSelectedId());
                }
                DataListBox locationDropDown = (DataListBox) row.getColumnById(LOCATION);
                if (locationDropDown.isValid()) {
                    newEmployee.setLocationId(locationDropDown.getSelectedId());
                }

                DataListBox employeeType = (DataListBox) row.getColumnById(EMPLOYEE_TYPE);

                newEmployee.setHasAccess(maxNoAccessEmp <= 0 || employeeType.getSelectedId() != NO_ACCESS);

                newEmployee.setEssUser(maxEssEmp > 0 && ESS == employeeType.getSelectedId());

//                if (customizationFor47229) {
//                    TextBox tb = ((TextBox) row.getColumnById(DRIVER_ID));
//                    if (!"".equals(tb.getValue())) {
//                        newEmployee.setDriverNumber(Long.valueOf(tb.getValue()));
//                    }
//                }
                newEmployees.add(newEmployee);
            }
        }
        return newEmployees.toArray(new NewEmployee[]{});
    }

    public boolean validateEmployees() {
        List emailExistList = new ArrayList();
        employeesTable.resetValidation();
        int defaultUserCount = 0;
        int noAccessUserCount = 0;
        int essUserCount = 0;
        int errors = 0;
        boolean emailRepeated = false;

        emailExistList.clear();
        for (int rowId = 0; rowId < employeesTable.getRowNumber(); rowId++) {
            ValidityResponse validityResponse = validateRow(rowId);

            if (validityResponse.isRowValid()) {
                DataListBox empType_ = (DataListBox) employeesTable.getItem(rowId).getColumnById(EMPLOYEE_TYPE);

                if (empType_.equals(wfmStrings.defaultUser())) {
                    defaultUserCount++;
                }
                if (empType_.equals(wfmStrings.noAccess())) {
                    noAccessUserCount++;
                }
                if (empType_.equals(wfmStrings.essUser())) {
                    essUserCount++;
                }
            }

            if (!validityResponse.isRowValid() && validityResponse.isDirty()) {
                errors++;
            }

            if (validityResponse.getEmailExist().trim().length() > 0 && emailExistList.contains(validityResponse.getEmailExist())) {
                emailRepeated = true;
            } else {
                emailExistList.add(validityResponse.getEmailExist());
            }

        }

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }

        if (emailRepeated) {
            Info.show(wfmStrings.canNotAddedSameEmail(), Info.Type.WARNING);
            return false;
        }

        if (defaultUserCount > 0 && maxEmp < defaultUserCount) {
            Info.show(coreMessages.exceededUserLimit(String.valueOf(defaultUserCount - maxEmp)), Info.Type.WARNING);
            return false;
        }
        if (noAccessUserCount > 0 && maxNoAccessEmp < noAccessUserCount) {
            Info.show(coreMessages.exceededNoAccessUserLimit(String.valueOf(noAccessUserCount - maxNoAccessEmp)), Info.Type.WARNING);
            return false;
        }
        if (essUserCount > 0 && maxEssEmp < essUserCount) {
            Info.show(coreMessages.exceededEssUserLimit(String.valueOf(essUserCount - maxEssEmp)), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    protected ValidityResponse validateRow(Integer rowId) {
        boolean rowValid = true;
        boolean dirty = true;
        DynamicTableItem tableItem = employeesTable.getItem(rowId);
        TextBox firstName = (TextBox) tableItem.getColumnById(FIRST_NAME);
        TextBox email = (TextBox) tableItem.getColumnById(EMAIL);
        DataListBox employeeType = (DataListBox) tableItem.getColumnById(EMPLOYEE_TYPE);

        if (Utils.isNullOrEmpty(firstName.getText())) {
            employeesTable.notValid(rowId, FIRST_NAME);
            rowValid = false;
        }

        if (!Validation.validateDataListBoxRequired(employeeType)) {
            employeesTable.notValid(rowId, EMPLOYEE_TYPE);
            rowValid = false;
        }
        if (Utils.isNullOrEmpty(email.getText()) && !Validation.validEmailFormat(email.getText(), false) && employeeType.getValue().getId() != 1) {
            employeesTable.notValid(rowId, EMAIL);
            rowValid = false;
        }

        if (firstName.getText().trim().length() == 0 && email.getText().trim().length() == 0 && employeeType.getSelectedItem() == null) {
            employeesTable.getItem(rowId).getColumnById(FIRST_NAME).removeStyleName(ERROR_FORM_STYLE);
            employeesTable.getItem(rowId).getColumnById(EMAIL).removeStyleName(ERROR_FORM_STYLE);
            employeesTable.getItem(rowId).getColumnById(EMPLOYEE_TYPE).removeStyleName(ERROR_FORM_STYLE);
            dirty = false;
        }
        return new ValidityResponse(rowValid, dirty, email.getText());
    }


    private void refreshOnDemand() {
        NewEmployee[] newEmployees = null;
        if (addeddEmployees != null) {
            newEmployees = (NewEmployee[]) addeddEmployees.toArray(new NewEmployee[]{});
        }
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYEE_ADD, newEmployees, MultiEmployeePanel.this);
        if (refreshParent != null) {
            refreshParent.refresh(newEmployees);
        }
    }

    private SelectItem[] getEmployeeTypesList() {
        int count = 1;
        boolean mayAddNoAccessUser = maxNoAccessEmp > 0;
        boolean mayAddESSUser = maxEssEmp > 0;
        if (mayAddESSUser) {
            count++;
        }
        if (mayAddNoAccessUser) {
            count++;
        }
        SelectItem[] result = new SelectItem[count];
        result[0] = new SelectItem(ACTIVE, wfmStrings.defaultUser());
        if (mayAddNoAccessUser) {
            result[1] = new SelectItem(NO_ACCESS, wfmStrings.noAccess());
        }
        if (mayAddESSUser) {
            result[2] = new SelectItem(ESS, wfmStrings.essUser());
        }
        return result;
    }

    private void refreshLocationDropDown(final DataListBox location, final boolean takenFromDatabase) {
        ReportService.App.get().getLocationList(new AbstractAsyncCallback<SelectItem[]>() {
            public void failure(Throwable throwable) {

            }

            public void success(SelectItem[] selectItems) {
                location.setItems(selectItems);
                if (takenFromDatabase || (locations == null || locations.length == 0)) {
                    locations = selectItems;
                }
            }
        });
    }

    private void fillDepartmentList(final DataListBox departmentsList) {
        if (departments == null) {
            LoadingPanel.loading(true);
            EmployeeService.App.get().getDepartmentsSelectItem(callbacksynchronizer.registerCallback(new AbstractAsyncCallback<DepartmentItem[]>() {
                public void success(DepartmentItem[] deps) {
                    SelectItem[] items = new SelectItem[deps.length];
                    for (int i = 0; i < items.length; i++) {
                        items[i] = new SelectItem(deps[i].getDepatmentID(), deps[i].getDepartmentName());
                    }
                    departments = items;
                    departmentsList.setItems(departments);
                    if (departments.length == 1) {
                        departmentsList.setSelected(departments[departments.length - 1].getId());
                    }
                    LoadingPanel.loading(false);
                }
            }));
        } else {
            departmentsList.setItems(departments);
            if (departments.length == 1) {
                departmentsList.setSelected(departments[departments.length - 1].getId());
            }
        }
    }

    private void getEmployeeRolesWithCustomRoles(final DataListBox roles) {
        AllInOneService.App.get().getRolesCheckAdmin(new AbstractAsyncCallback<ArrayList<SelectItem>>() {
            public void onFailure(Throwable throwable) {
                roles.setItems(getRoleWithCustom());
            }

            public void onSuccess(ArrayList<SelectItem> customRoleList) {
//                customRoles = customRoleList;
                if (customRoleList != null && customRoleList.size() > 0) {
                    roles.setItems(customRoleList.toArray(new SelectItem[]{}));
                }
            }
        });
    }

    private SelectItem[] getRoleWithCustom() {
        ArrayList<SelectItem> result = new ArrayList<>();
        result.add(new SelectItem(MEM, wfmStrings.employee()));
        if (!Utils.hasRole(ADMIN_LOCATION) || ((Utils.hasRole(ADMIN) || Utils.hasRole(DR)) && Utils.hasRole(ADMIN_LOCATION))) {
            result.add(new SelectItem(ADMIN, wfmStrings.administrator()));
            result.add(new SelectItem(DR, wfmStrings.director()));
        }
        result.add(new SelectItem(ACCOUNTANT, wfmStrings.accountant()));
        result.add(new SelectItem(HR, wfmStrings.hrManager()));
        result.add(new SelectItem(SALESMAN, wfmStrings.salesManager()));
        result.add(new SelectItem(CUSTOMER_SERVICE_REPRESENTATIVE, wfmStrings.customerServiceRepresentative()));
        result.add(new SelectItem(SALESPERSON, wfmStrings.salesPerson()));
        return result.toArray(new SelectItem[]{});
    }

    private void initEmployeesTable() {
        employeesTable = new DynamicTable(getColumnArray());
        employeesTable.addListener(new AddListener() {
            @Override
            public void plusClicked(int rowId) {
                Widget[] widgets = getWidgetArray();
                employeesTable.insertRow(rowId + 1, widgets);
            }

            @Override
            public void minusClicked(int rowId, Integer objectId) {

            }
        });

        add(employeesTable);

    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return MultiEmployeePanel.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return MultiEmployeePanel.this.getFooterRightSideWidgets();
            }
        });
    }

    private List<Widget> getFooterRightSideWidgets() {
        List<Widget> items = new ArrayList<>();
        Div saveWrapper = new Div();
        saveWrapper.add(saveEmployeesButton);
        items.add(saveWrapper);

        return items;
    }

    private List<Widget> getFooterLeftSideWidgets() {
        return null;
    }

    private DynamicTableColumn[] getColumnArray() {

        int index = 0;
        int columnCount = 7;

//        if (customizationFor47229) {
//            columnCount++;
//        }

        DynamicTableColumn[] columns = new DynamicTableColumn[columnCount];

        columns[index++] = new DynamicTableColumn("<b class=customTitle>" + wfmStrings.firstName() + "<font color='red'>*</font>:</b>", FIRST_NAME, new ColumnStatements(".", ""), 100);
        columns[index++] = new DynamicTableColumn("<b class=customTitle>" + wfmStrings.lastName() + ":", LAST_NAME, new ColumnStatements(".", ""), 100);
        columns[index++] = new DynamicTableColumn("<b class=customTitle>" + wfmStrings.email() + "<font color='red'>*</font>:</b>", EMAIL, new ColumnStatements(".", ""), 100);
        columns[index++] = new DynamicTableColumn("<b class=customTitle>" + wfmStrings.role() + ":</b>", ROLE_EMPLOYEE, new ColumnStatements(".", ""), 100);
        columns[index++] = new DynamicTableColumn("<b class=customTitle>" + Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()) + ":</b>", DEPARTMENT, new ColumnStatements(".", ""), 140);
        columns[index++] = new DynamicTableColumn("<b class=customTitle>" + Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()) + ":</b>", LOCATION, new ColumnStatements(".", ""), 140);
        columns[index++] = new DynamicTableColumn("<b class=customTitle>" + wfmStrings.employeeType() + ":</b>", EMPLOYEE_TYPE, new ColumnStatements(".", ""), 140);

//        if (customizationFor47229) {
//            columns[index++] = new DynamicTableColumn("<b class=customTitle>Driver ID<font color='red'>*</font>:</b>", DRIVER_ID, new ColumnStatements(".", ""), 80);
//        }
        return columns;
    }

    private Widget[] getWidgetArray() {

        TextBox fName = new TextBox();
        TextBox lName = new TextBox();
        TextBox eMail = new TextBox();
        TextBox driverNo = new TextBox();
        employeeType = new DataListBox();
        employeeType.setItems(getEmployeeTypesList());


        int index = 0;
        int columnCount = 7;
        boolean mayAddNoAccessUser = maxNoAccessEmp > 0;
//        boolean customizationFor47229 = !mayAddNoAccessUser && CompanyConstants.C47229.equals(Utils.getEncryptedCompanyID());
//        if (customizationFor47229) {
//            columnCount++;
//        }

        Widget[] widgets = new Widget[columnCount];
        widgets[index++] = fName;
        widgets[index++] = lName;
        widgets[index++] = eMail;

        DataListBox roles = new DataListBox();
        getEmployeeRolesWithCustomRoles(roles);
        roles.setSelected(SALESPERSON);

        final DataListBox departmentsList = new DataListBox();
        fillDepartmentList(departmentsList);

        final DataListBox location = new DataListBox();

        refreshLocationDropDown(location, true);

        widgets[index++] = roles;
        widgets[index++] = departmentsList;
        widgets[index++] = location;
        widgets[index++] = employeeType;

//        if (customizationFor47229) {
//            widgets[index++] = driverNo;
//        }
        return widgets;

    }

    public interface RefreshParent {
        void refresh(final NewEmployee[] addedEmployees);
    }

    public interface CheckLimit {
        void limitExceeded();
    }

    public void setCreatedFrom(String createdFrom) {
        this.createdFrom = createdFrom;
    }

    public void setCheckLimit(CheckLimit checkLimit) {
        this.checkLimit = checkLimit;
    }


    public void setRefreshParent(RefreshParent refreshParent) {
        this.refreshParent = refreshParent;
    }

    private class ValidityResponse {
        private final boolean rowValid;
        private final boolean dirty;
        private final String emailExist;

        public ValidityResponse(boolean rowValid, boolean dirty, String emailExist) {
            this.rowValid = rowValid;
            this.dirty = dirty;
            this.emailExist = emailExist;
        }

        public boolean isRowValid() {
            return rowValid;
        }

        public boolean isDirty() {
            return dirty;
        }

        public String getEmailExist() {
            return emailExist;
        }
    }
}
