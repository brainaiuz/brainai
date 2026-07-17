package com.edatasite.workforce.gwt.gettingstarted.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.AddListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.gettingstarted.client.rpc.GettingStartedService;
import com.edatasite.workforce.gwt.gettingstarted.client.rpc.GettingStartedServiceAsync;
import com.edatasite.workforce.gwt.newemployee.client.rpc.NewEmployee;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.ProvidesKey;

/**
 * Getting Started Add Employee View
 */
public class CreateEmployeeGuideView extends GettingStartedMainView implements Errors {

    private static final int COLUMNS_COUNT = 4;
    private static final int COLUMNS_COUNT_WITH_NO_ACCESS = 5;
    private final GettingStartedServiceAsync gettingStartedService = GettingStartedService.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private TextBox firstName;
    private TextBox lastName;
    private TextBox email;
    private DataListBox roles;
    private KpiCheckBox noAccess;
    private DynamicTable addEmployee;
    private DisclosurePanel panel;
    private SimpleLink addMore;
    private KpiDataGrid<SelectItem> employeesList;
    private int maxEmp = 0;
    private int maxNoAccessEmp = 0;
    private int emplCount = 1;

    public CreateEmployeeGuideView() {
        super(false);
    }

    public void showView() {
        container.clear();
        gettingStartedService.getEmpMaxCount(new AbstractAsyncCallback<Integer[]>() {
            public void success(final Integer[] object) {
                maxEmp = object[0];
                maxNoAccessEmp = object[1];
                if (maxEmp > 0) {
                    initComponents();
                } else {
                    initEmptyForm();
                }
                init();
            }
        });

    }

    private void initEmptyForm() {
        FlexTable internalTable = new FlexTable();
        internalTable.setStyleName("stage-background");
        internalTable.setSize("90%", "90%");
        internalTable.setCellSpacing(30);

        HTMLTable.CellFormatter cellFormatter = internalTable.getCellFormatter();
        cellFormatter.setWidth(0, 0, "65%");
        cellFormatter.setWidth(0, 1, "25%");
        internalTable.setWidget(0, 0, description2());
        cellFormatter.setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
        internalTable.setWidget(0, 1, getLastEmployees());
        cellFormatter.setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
        container.add(internalTable);
        //container.layout(true);

    }

    private int count = 1;

    /**
     * Regex taken from: http://www.regular-expressions.info/email.html
     * by Jan Goyvaerts
     */
    private final String emailRegex = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+[.][A-Za-z]{2,4}";

    // draw dynamic table column title

    private void drawDynamicalTable() {

        Widget[] widgets = getWidgetArray();
        emplCount++;

        addEmployee = new DynamicTable(getColumnTitle());
        addEmployee.setHeight("50px");
        addEmployee.addRow(widgets);

        if (emplCount <= maxEmp) {
            addEmployee.addRow(getWidgetArray());
            emplCount++;
        }
        if (emplCount <= maxEmp) {
            addEmployee.addRow(getWidgetArray());
            emplCount++;
        }

        addDynamicTableListener();
    }

    private void initComponents() {
        HTML thema = new HTML("<span style='text-transform:capitalize;font-size:13pt;color:#1F4F8F;font-weight: bold;'>" + wfmStrings.addingCompanyEmployees() + "</span>");

        drawDynamicalTable();

        getLastEmployees();

        addMore = new SimpleLink(wfmStrings.addMore(), SimpleLink.ADD_ICON);
        addMore.addClickHandler(sender -> {
            if (emplCount < maxEmp) {
                Widget[] widgets = getWidgetArray();
                addEmployee.addRow(widgets);
                emplCount++;
            } else {
                exceedEmployeeMessage();
            }
        });

        FlexTable internalTable = new FlexTable();
        internalTable.setStyleName("stage-background");
        internalTable.setSize("1000px", "90%");
        internalTable.setCellSpacing(20);

        HTMLTable.CellFormatter cellFormatter = internalTable.getCellFormatter();

        cellFormatter.setWidth(0, 0, "75%");
        cellFormatter.setWidth(1, 0, "75%");
        cellFormatter.setWidth(2, 0, "75%");

        internalTable.setWidget(0, 0, thema);
        cellFormatter.setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);

        internalTable.getFlexCellFormatter().setRowSpan(0, 1, 3);
//        internalTable.getFlexCellFormatter().setStyleName(0,1,"right-rect-back");
        internalTable.setWidget(0, 1, employeesList);
        cellFormatter.setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
        cellFormatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);

        internalTable.setWidget(1, 0, addEmployee);
        cellFormatter.setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);

        internalTable.setWidget(2, 0, addMore);
        cellFormatter.setVerticalAlignment(2, 0, HasVerticalAlignment.ALIGN_TOP);


        container.add(internalTable);
        //container.layout(true);
    }

    private void addDynamicTableListener() {
        addEmployee.addListener(new AddListener() {
            public void plusClicked(int rowId) {
                if (emplCount < maxEmp) {
                    Widget[] widgets = getWidgetArray();
                    addEmployee.insertRow(rowId + 1, widgets);
                    emplCount++;
                } else {
                    exceedEmployeeMessage();
                }

            }

            public void minusClicked(int rowId, Integer objectId) {
                if (rowId >= 0 && emplCount > 1) {
                    --emplCount;
                }
            }
        });
    }

    // add Widgets to dynamic table

    private Widget[] getWidgetArray() {
        Widget[] widget;
        if (maxNoAccessEmp <= 0) {
            widget = new Widget[COLUMNS_COUNT];
        } else {
            widget = new Widget[COLUMNS_COUNT_WITH_NO_ACCESS];
        }

        firstName = new TextBox();
        firstName.addStyleName(DEFAULT_WIDTH);

        lastName = new TextBox();
        lastName.addStyleName(DEFAULT_WIDTH);

        email = new TextBox();
        email.addStyleName(DEFAULT_WIDTH);

        roles = new DataListBox();
        roles.setWidth("100px");
        roles.setWithoutNullLabel(true);

        roles.setItems(new SelectItem[]{new SelectItem(MEM, wfmStrings.employee()),
                new SelectItem(ADMIN, wfmStrings.administrator()),
                new SelectItem(DR, wfmStrings.director()),
                new SelectItem(HR, wfmStrings.hrManager()),
                new SelectItem(ACCOUNTANT, wfmStrings.accountant())});

        roles.setSelected(MEM);

        noAccess = new KpiCheckBox();
        noAccess.setValue(false);
        noAccess.setWidth("80px");

        widget[0] = firstName;
        widget[1] = lastName;
        widget[2] = email;
        widget[3] = roles;
        if (maxNoAccessEmp > 0) {
            widget[4] = noAccess;
        }

        return widget;
    }

    private DynamicTableColumn[] getColumnTitle() {
        DynamicTableColumn[] columns;
        if (maxNoAccessEmp <= 0) {
            columns = new DynamicTableColumn[COLUMNS_COUNT];
        } else {
            columns = new DynamicTableColumn[COLUMNS_COUNT_WITH_NO_ACCESS];
        }
        columns[0] = new DynamicTableColumn(wfmStrings.firstName(), "firstName", 170);
        columns[1] = new DynamicTableColumn(wfmStrings.lastName(), "lastName", 170);
        columns[2] = new DynamicTableColumn(wfmStrings.email(), "email", 170);
        columns[3] = new DynamicTableColumn(wfmStrings.role(), "role", 120);
        if (maxNoAccessEmp > 0) {
            columns[4] = new DynamicTableColumn(wfmStrings.noAccess(), "noAccess", 80);
        }

        return columns;
    }

    public void exceedEmployeeMessage() {
        Info.show(wfmStrings.errorUsersLimitExceeded(), Info.Type.WARNING);
    }

    private KpiDataGrid<SelectItem> getLastEmployees() {
        employeesList = new KpiDataGrid<>(KEY_PROVIDER);
        employeesList.addStyleName(DEFAULT_WIDTH);
        employeesList.setHeight("250px");

        Column<SelectItem, String> department = new Column<SelectItem, String>(new TextCell()) {
            @Override
            public String getValue(final SelectItem object) {
                return (count++) + ". " + object.getName();
            }
        };

        employeesList.addColumn(department, wfmStrings.latestAddedEmployees());
        employeesList.setColumnWidth(department, 60, com.google.gwt.dom.client.Style.Unit.PCT);

        return employeesList;
    }

    private void save() {
        LoadingPanel.loading(true);

        NewEmployee[] employees = new NewEmployee[addEmployee.getRowNumber()];
        int k = 0;
        for (int rowId = 0; rowId < addEmployee.getRowNumber(); rowId++) {

            DynamicTableItem tableItem = addEmployee.getItem(rowId);
            TextBox firstNameText = (TextBox) tableItem.getColumnById("firstName");
            TextBox lastNameText = (TextBox) tableItem.getColumnById("lastName");
            TextBox emailText = (TextBox) tableItem.getColumnById("email");
            DataListBox listBoxText = (DataListBox) tableItem.getColumnById("role");
            KpiCheckBox noAccess = new KpiCheckBox();
            noAccess.setValue(false);
            if (maxNoAccessEmp > 0) {
                noAccess = (KpiCheckBox) tableItem.getColumnById("noAccess");
            }

            if ((firstNameText.getText() == null || firstNameText.getText().equals(""))
                    && (lastNameText.getText() == null || lastNameText.getText().equals(""))
                    && (emailText.getText() == null || emailText.getText().equals(""))) {
                continue;
            }

            final NewEmployee employee = new NewEmployee();
            employee.setCreatedFrom(EMPLOYEE_CREATED_FROM_PM_GETTING_STARTED);
            employee.setFname(firstNameText.getText());
            employee.setLname(lastNameText.getText());
            employee.setEmail(emailText.getText());
            employee.setRole(listBoxText.getSelectedItem().getId());
            employee.setHasAccess(!noAccess.getValue());

            employees[k++] = employee;
        }
        final NewEmployee[] newEmpl = new NewEmployee[k];
        System.arraycopy(employees, 0, newEmpl, 0, k);

        EmployeeService.App.get().createEmployees(newEmpl, new AbstractAsyncCallback<Integer[]>() {

            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.errorOccurredSavingChanges(), Info.Type.INFO);
            }

            public void success(Integer[] employeeId) {
                LoadingPanel.loading(false);
                boolean p = true;
                for (Integer anEmployeeId : employeeId) {

                    if (anEmployeeId.intValue() == EMPLOYEE_WITH_THIS_EMAIL_ALREADY_EXISTS) {
                        whichEmailExists(employeeId);
                        Info.show(wfmStrings.sorryEmailWithThisNameAlreadyExists(), Info.Type.WARNING);
                        p = false;
                        break;

                    } else if (anEmployeeId.intValue() == EMPLOYEE_WITH_THIS_EMAIL_HOST_DOES_NOT_EXIST) {
                        validate(anEmployeeId.intValue()); //host exist valid
                        p = false;
                        break;
                    } else if (anEmployeeId == CAN_NOT_CREATE_EMPLOYEE) {

                        Info.show(wfmStrings.canNotCreateAnEmployee(), Info.Type.INFO);
                        p = false;
                        break;

                    }
                }

                if (p) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYEE_ADD, employeeId, CreateEmployeeGuideView.this);
                    emplCount = 1;
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.employee()), Info.Type.INFO);
                    shows();
                }

            }
        });

    }

    private void shows() {
        showView();
        listener.onNextButtonClick();
    }

    public static final ProvidesKey<SelectItem> KEY_PROVIDER = item -> item == null ? null : item.getId();

    private void whichEmailExists(Integer[] status) {
        int step = 0, k = 0;
        DynamicTable newTable = new DynamicTable(getColumnTitle());
        newTable.setHeight("50px");
        addEmployee.resetValidation();
        for (int rowId = 0; rowId < addEmployee.getRowNumber(); rowId++) {
            DynamicTableItem tableItem = addEmployee.getItem(rowId);

            TextBox firstNameText = (TextBox) tableItem.getColumnById("firstName");
            TextBox lastNameText = (TextBox) tableItem.getColumnById("lastName");
            TextBox emailText = (TextBox) tableItem.getColumnById("email");
            DataListBox listBoxText = (DataListBox) tableItem.getColumnById("role");
            KpiCheckBox noAccess = new KpiCheckBox();
            noAccess.setValue(false);
            if (maxNoAccessEmp > 0) {
                noAccess = (KpiCheckBox) tableItem.getColumnById("noAccess");
            }

            if ((firstNameText.getText() == null || firstNameText.getText().equals(""))
                    && (lastNameText.getText() == null || lastNameText.getText().equals(""))
                    && (emailText.getText() == null || emailText.getText().equals(""))) {
                emplCount--;
                continue;
            }

            if (status[step] == EMPLOYEE_WITH_THIS_EMAIL_ALREADY_EXISTS) {
                newTable.addRow(getWidgetArray());
                DynamicTableItem putWidgets = newTable.getItem(k);
                TextBox firstNameText1 = (TextBox) putWidgets.getColumnById("firstName");
                TextBox lastNameText1 = (TextBox) putWidgets.getColumnById("lastName");
                TextBox emailText1 = (TextBox) putWidgets.getColumnById("email");
                DataListBox listBoxText1 = (DataListBox) putWidgets.getColumnById("role");
                KpiCheckBox noAccess1 = new KpiCheckBox();
                if (maxNoAccessEmp > 0) {
                    noAccess1 = (KpiCheckBox) putWidgets.getColumnById("noAccess");
                }

                firstNameText1.setText(firstNameText.getText());
                lastNameText1.setText(lastNameText.getText());
                emailText1.setText(emailText.getText());
                listBoxText1.setSelected(listBoxText.getSelectedItem().getId());
                noAccess1.setValue(noAccess.getValue());

                newTable.notValid(k, "email");
                k++;
            }
            step++;
        }
        addEmployee = newTable;
        addDynamicTableListener();
        FlexTable flexTable = (FlexTable) container.getWidget(0);
        flexTable.setWidget(1, 0, addEmployee);

        init();
    }

    private void init() {
        count = 1;
        LoadingPanel.loading(true);
        gettingStartedService.getLastEmployees(new AbstractAsyncCallback<SelectItem[]>() {
            public void success(SelectItem[] employees) {
                LoadingPanel.loading(false);
                employeesList.supplyProvider(employees);
                employeesList.refresh();
            }
        });
    }

    private HTML description2() {
        return new HTML("<span style='font-size:12px;color:red;padding-left:10px;text-align:justify;'>" + wfmStrings.errorUsersLimitExceeded() + wfmStrings.ifYouWantAdd() + "</span>");
    }

    private boolean validateMail(String emailText) {
        emailText = emailText.trim();
        for (int i = 0; i < emailText.length(); i++) {
            if (emailText.charAt(i) == ' ') {
                return false;
            }
        }

        return emailText.matches(emailRegex/*".+@.+\\.[a-z]+"*/);
    }

    private boolean validate(Integer hostExist) {
        String emailErrorMessage = null;
        boolean isEmailValid = true;
        int error = 0;
        addEmployee.resetValidation();
        for (int rowId = 0; rowId < addEmployee.getRowNumber(); rowId++) {
            DynamicTableItem tableItem = addEmployee.getItem(rowId);
            TextBox firstNameText = (TextBox) tableItem.getColumnById("firstName");
            TextBox lastNameText = (TextBox) tableItem.getColumnById("lastName");
            TextBox emailText = (TextBox) tableItem.getColumnById("email");
            DataListBox listBoxText = (DataListBox) tableItem.getColumnById("role");

            if ((firstNameText.getText() == null || firstNameText.getText().equals(""))
                    && (lastNameText.getText() == null || lastNameText.getText().equals(""))
                    && (emailText.getText() == null || emailText.getText().equals(""))) {
                continue;
            }

            if (firstNameText.getText() == null || firstNameText.getText().equals("")) {
                error++;
                addEmployee.notValid(rowId, "firstName", wfmStrings.pleaseEnterFirstName());
                isEmailValid = false;
            }

            if (lastNameText.getText() == null || lastNameText.getText().equals("")) {
                error++;
                addEmployee.notValid(rowId, "lastName", wfmStrings.pleaseEnterLastName());
                isEmailValid = false;
            }

            if (emailText.getText() == null || emailText.getText().equals("")) {
                error++;
                addEmployee.notValid(rowId, "email", wfmStrings.pleaseEnterEmail());
                emailErrorMessage = wfmStrings.pleaseEnterValidEmailAddress();
            } else if (!validateMail(emailText.getText())) {
                error++;
                addEmployee.notValid(rowId, "email", wfmStrings.pleaseEnterCorrectEmailAddress());
                emailErrorMessage = wfmStrings.pleaseEnterValidEmailAddress();
            } else if (hostExist != null) {//host exist valid
                error++;
                addEmployee.notValid(rowId, "email", wfmStrings.thisEmailDoesNotExist());
                emailErrorMessage = wfmStrings.pleaseEnterValidEmailAddress();
            }
        }

        if (error > 0) {
            if (emailErrorMessage != null && isEmailValid) {
                Info.show(emailErrorMessage, Info.Type.WARNING);
            } else {
                Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            }
            return false;
        } else {

            return true;
        }
    }

    private boolean filledFields() {
        addEmployee.resetValidation();
        for (int rowId = 0; rowId < addEmployee.getRowNumber(); rowId++) {
            DynamicTableItem tableItem = addEmployee.getItem(rowId);
            TextBox firstNameText = (TextBox) tableItem.getColumnById("firstName");
            TextBox lastNameText = (TextBox) tableItem.getColumnById("lastName");
            TextBox emailText = (TextBox) tableItem.getColumnById("email");
            DataListBox listBoxText = (DataListBox) tableItem.getColumnById("role");

            if (firstNameText.getText() != null && !firstNameText.getText().equals("")) {
                return true;
            }
            if (lastNameText.getText() != null && !lastNameText.getText().equals("")) {
                return true;
            }
            if (emailText.getText() != null && !emailText.getText().equals("")) {
                return true;
            }
        }
        return false;
    }

    public void refresh() {
        init();
    }

    protected void saveAddAnother() {
    }

    protected void skipThisStep() {
        listener.onNextButtonClick();
    }

    protected boolean saveAndNext() {
        if (maxEmp > 0) {
            if (filledFields()) {
                if (validate(null)) {
                    save();
                    return false;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return true;
        }

    }
}
