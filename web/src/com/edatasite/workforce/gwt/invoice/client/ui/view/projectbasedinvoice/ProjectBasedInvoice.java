package com.edatasite.workforce.gwt.invoice.client.ui.view.projectbasedinvoice;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.cell.CheckBoxCell;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.TaxLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProjectBaseData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProjectBaseInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.ProjectBaseInvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.ProjectBaseInvoiceServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Paragraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ProjectBasedInvoice extends Composite implements Constants, FittedContent {
    private final WfmStrings wfmStrings = WfmStrings.App.get();

    interface ProjectBasedInvoiceWidgetUiBinder extends UiBinder<HTMLPanel, ProjectBasedInvoice> {
    }

    private static final ProjectBasedInvoiceWidgetUiBinder ourUiBinder = GWT.create(ProjectBasedInvoiceWidgetUiBinder.class);

    private final ProjectBaseInvoiceServiceAsync pbInvoiceService = ProjectBaseInvoiceService.App.get();
    private static final AllInOneServiceAsync allInOneService = AllInOneService.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    //fields for filter data
    @UiField
    FormGroup pnlCustomer;
    @UiField
    FormGroup pnlFromDate;
    @UiField
    FormGroup pnlToDate;
    @UiField
    FormGroup amount;
    @UiField
    FormGroup pnlTaxRate;

    //result table
    @UiField
    Div pnlDataGrid;

    //timesheet based invoice type fields
    @UiField
    Div invoiceTypeLabel;
    @UiField
    Paragraph pnlDetailedInvoice;
    @UiField
    Paragraph pnlDetailedInvoiceDesc;
    @UiField
    Paragraph pnlgroupByAssignees;
    @UiField
    Paragraph pnlGroupByAssigneesDesc;
    @UiField
    Paragraph pnlGroupedByTask;
    @UiField
    Paragraph pnlGroupedByTaskDesc;
    @UiField
    Paragraph pnlGroupedByProject;
    @UiField
    Paragraph pnlGroupedByProjectDesc;

    //fields for descripe item name
    @UiField
    Div fieldsToNameLabel;
    @UiField
    Div pnlFieldsToNameContainer;

    //fields for descripe item self
    @UiField
    Div fieldsToDescLabel;
    @UiField
    Div pnlFieldsToDescContainer;


    @UiField
    Div wrapperDetailedInvoice;
    @UiField
    Div wrapperGrouppedByAssignee;
    @UiField
    Div wrapperGrouppedByTask;
    @UiField
    Div wrapperGrouppedByProject;


    private CrmAccountLookUp lookUp;
    protected DatePicker fromDate;
    protected DatePicker toDate;
    private TaxLookUp taxLookUp;
    private DataListBox amountList;

    private KpiRadioButton detailedInvoice;
    private KpiRadioButton groupByAssignees;
    private KpiRadioButton groupByTask;
    private KpiRadioButton groupByProject;

    private KpiCheckBox pn;
    private KpiCheckBox tn;
    private KpiCheckBox ta;
    private KpiCheckBox ep;

    private KpiDataGrid<ProjectBaseInvoiceItem> dataGrid;
    private ListDataProvider<ProjectBaseInvoiceItem> dataProvider;
    private static final ProvidesKey<ProjectBaseInvoiceItem> KEY_PROVIDER = item -> item == null ? null : item.getId();

    private ArrayList<Integer> projectIDs;
    private HashMap<Integer, SelectItem> projectItemMap = new HashMap<>();

    private HashMap<String, KpiCheckBox> toNameMap;
    protected HashMap<String, KpiCheckBox> toDescMap;

    private final int MAX_HEIGHT = 400;
    private final String RADIO_NAME = "RADIO_NAME";
    private final String NOT_SELECTED_TYPE_STYLE = "card-box--dark";
    private String invoiceType;


    public ProjectBasedInvoice() {
        initWidget(ourUiBinder.createAndBindUi(this));
        initialize();
    }

    private void initialize() {
        projectItemMap = new HashMap<>();

        lookUp = new CrmAccountLookUp(CrmAccountLookUp.CUSTOMER, true);
        pnlCustomer.setLabel(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()));
        pnlCustomer.addToContent(lookUp);

        lookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> pbInvoiceService.getClientRelatedProjectsForPBI(lookUp.getSelectedItem().getId(), new AbstractAsyncCallback<ProjectBaseInvoiceItem[]>() {
            public void success(ProjectBaseInvoiceItem[] projects) {
                projectIDs = new ArrayList<>();
                projectItemMap.clear();

                if (projects != null && projects.length > 0) {
                    int height = projects.length * 40 + 50;

                    if (height > MAX_HEIGHT) {
                        height = MAX_HEIGHT;
                    }
                    dataGrid.setHeight(height + "px");

                    if (height == MAX_HEIGHT) {
                        dataGrid.getElement().getStyle().setOverflow(Style.Overflow.AUTO);
                    }

                }

                dataProvider.getList().clear();
                dataProvider.getList().addAll(Arrays.asList(projects));
                dataProvider.refresh();
            }
        }));

        //date period fields
        fromDate = new DatePicker(false);
        pnlFromDate.setLabel(wfmStrings.from());
        pnlFromDate.addToContent(fromDate);

        toDate = new DatePicker(false);
        pnlToDate.setLabel(wfmStrings.to());
        pnlToDate.addToContent(toDate);

        //tax fields
        amountList = new DataListBox();
        amountList.setWithoutNullLabel(true);
        amountList.setItems(new SelectItem[]{
                new SelectItem(0, wfmStrings.noTax()),//NO TAX
                new SelectItem(1, wfmStrings.taxInclusive()),//TAX INCLUSIVE
                new SelectItem(2, wfmStrings.taxExclusive())});//TAX EXCLUSIVE
        amountList.setSelected(2);

        amount.setLabel(accountingStrings.amounts());
        amount.addToContent(amountList);


        taxLookUp = new TaxLookUp(RECEIVABLE);
        pnlTaxRate.setLabel(wfmStrings.taxRate());
        pnlTaxRate.addToContent(taxLookUp);

        //grid initialize
        initProjectTable();

        //initialize invoice type
        initInvoiceTypes();

        //fields to be include
        fieldsToNameLabel.getElement().setInnerHTML(accountingStrings.fieldsToBeIncluded());
        fieldsToDescLabel.getElement().setInnerHTML(accountingStrings.informationToBeIncluded());

        initToNameFields();
        initToDescFields();
        //default type of the invoice
        onClickInvoiceType(DETAILED_INVOICE);

    }

    private void initProjectTable() {
        dataProvider = new ListDataProvider<>();
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        dataProvider.addDataDisplay(dataGrid);
        dataGrid.setHeight("50px");

        initColumns();
        pnlDataGrid.clear();
        pnlDataGrid.add(dataGrid);

    }

    public static final String DETAILED_INVOICE = "DETAILED_INVOICE";
    public static final String GROUPED_BY_ASSIGNEE = "GROUPED_BY_ASSIGNEE";
    public static final String GROUPED_BY_TASK = "GROUPED_BY_TASK";
    public static final String GROUPED_BY_PROJECT = "GROUPED_BY_PROJECT";

    private void initInvoiceTypes() {
        //detailed invoice
        detailedInvoice = new KpiRadioButton(RADIO_NAME, accountingStrings.detailedInvoice());
        detailedInvoice.setValue(Boolean.TRUE);
        pnlDetailedInvoice.add(detailedInvoice);
        pnlDetailedInvoiceDesc.setText(accountingStrings.detailedInvoiceDescription());

        //grouped by assignee
        groupByAssignees = new KpiRadioButton(RADIO_NAME, accountingStrings.groupByAssignees());
        pnlgroupByAssignees.add(groupByAssignees);
        pnlGroupByAssigneesDesc.setText(accountingStrings.groupByAssigneesDescription());

        groupByTask = new KpiRadioButton(RADIO_NAME, accountingStrings.groupByTask());
        pnlGroupedByTask.add(groupByTask);
        pnlGroupedByTaskDesc.setText(accountingStrings.groupByTaskDescription());

        groupByProject = new KpiRadioButton(RADIO_NAME, accountingStrings.groupByProject());
        pnlGroupedByProject.add(groupByProject);
        pnlGroupedByProjectDesc.setText(accountingStrings.groupByProjectDescription());

        detailedInvoice.addClickHandler(ch -> onClickInvoiceType(DETAILED_INVOICE));
        groupByAssignees.addClickHandler(ch -> onClickInvoiceType(GROUPED_BY_ASSIGNEE));
        groupByTask.addClickHandler(ch -> onClickInvoiceType(GROUPED_BY_TASK));
        groupByProject.addClickHandler(ch -> onClickInvoiceType(GROUPED_BY_PROJECT));
    }

    private void initColumns() {
        int index = 0;

        Header<Boolean> header = new Header(new CheckBoxCell()) {
            @Override
            public Boolean getValue() {
                return Boolean.FALSE;
            }
        };
        header.setUpdater(value -> {
            List<ProjectBaseInvoiceItem> list = dataProvider.getList();

            for (ProjectBaseInvoiceItem item : list) {
                item.setSelected(value);

                if (value) {
                    projectIDs.add(item.getId());
                    projectItemMap.put(item.getId(), item);
                } else {
                    projectIDs.remove(item.getId());
                    projectItemMap.remove(item.getId());
                }
            }
            dataProvider.refresh();
        });
        dataGrid.addColumn(new Column<ProjectBaseInvoiceItem, Boolean>(new CheckBoxCell()) {
            @Override
            public Boolean getValue(ProjectBaseInvoiceItem item) {
                return item.isSelected();
            }
        }, header);
        dataGrid.setColumnWidth(dataGrid.getColumn(index++), 50, Style.Unit.PX);
        dataGrid.getHeader(0).setHeaderStyleNames("pbinv-cell-header");
        dataGrid.removeColumnStyleName(0, "com-google-gwt-user-cellview-client-DataGrid-Style-dataGridHeader");

        dataGrid.addColumn(new TextColumn<ProjectBaseInvoiceItem>() {
            @Override
            public String getValue(ProjectBaseInvoiceItem object) {
                return object.getName();
            }
        }, Property.get(Constants.PROJECT, wfmStrings.project()));
        dataGrid.setColumnWidth(dataGrid.getColumn(index++), 300, Style.Unit.PX);
        dataGrid.getHeader(1).setHeaderStyleNames("pbinv-cell-header");
        dataGrid.removeColumnStyleName(1, "com-google-gwt-user-cellview-client-DataGrid-Style-dataGridHeader");

        dataGrid.addColumn(new TextColumn<ProjectBaseInvoiceItem>() {
            @Override
            public String getValue(ProjectBaseInvoiceItem object) {
                return object.getLastInvoicedDate() != null ? DateUtils.format(object.getLastInvoicedDate()) : "N/A";
            }
        }, accountingStrings.lastInvoiceDate());
        dataGrid.setColumnWidth(dataGrid.getColumn(index++), 200, Style.Unit.PX);
        dataGrid.getHeader(2).setHeaderStyleNames("pbinv-cell-header");
        dataGrid.removeColumnStyleName(2, "com-google-gwt-user-cellview-client-DataGrid-Style-dataGridHeader");

        Column<ProjectBaseInvoiceItem, Boolean> checkbox = (Column<ProjectBaseInvoiceItem, Boolean>) dataGrid.getColumn(0);
        checkbox.setFieldUpdater((index1, item, value) -> {
            if (value) {
                projectIDs.add(item.getId());
                projectItemMap.put(item.getId(), item);
            } else {
                projectIDs.remove(item.getId());
                projectItemMap.remove(item.getId());
            }
        });
    }

    private void initToNameFields() {
        toNameMap = new HashMap<>();

        //project name
        pn = new KpiCheckBox(wfmStrings.projectName());
        pn.setLayoutData(generateIncFieldKey(accountingStrings.nameOfProject()));
        pn.setValue(Boolean.TRUE);
        toNameMap.put(PROJECT_NAME, pn);

        //task name
        tn = new KpiCheckBox(wfmStrings.taskName());
        tn.setLayoutData(generateIncFieldKey(accountingStrings.nameOfTask()));
        tn.setValue(Boolean.TRUE);
        toNameMap.put(TASK_NAME, tn);

        //task name
        ta = new KpiCheckBox(accountingStrings.taskAssignee());
        ta.setLayoutData(generateIncFieldKey(accountingStrings.taskAssigneesFIO()));
        ta.setValue(Boolean.TRUE);
        toNameMap.put(TASK_ASSIGNEE, ta);

        //employee position
        ep = new KpiCheckBox(WfmStrings.App.get().employeePosition());
        ep.setLayoutData(generateIncFieldKey(WfmStrings.App.get().employeePosition()));
        ep.setValue(Boolean.TRUE);
        toNameMap.put(EMPLOYEE_POSITION, ep);
    }

    public boolean isProjectNameIncluded() {
        return pn.getValue();
    }

    public boolean isTaskNameIncluded() {
        return tn.getValue();
    }

    public boolean isTaskAssigneeIncluded() {
        return ta.getValue();
    }

    public boolean isEmployeePositionIncluded() {
        return ep.getValue();
    }

    private void onClickInvoiceType(String type) {
        invoiceType = type;

        for (String key : toNameMap.keySet()) {
            switch (key) {
                case PROJECT_NAME:
                    toNameMap.get(key).setVisible(Boolean.TRUE);
                    break;
                case TASK_NAME:
                    toNameMap.get(key).setVisible(DETAILED_INVOICE.equals(type) || GROUPED_BY_TASK.equals(type));
                    break;
                case TASK_ASSIGNEE:
                    toNameMap.get(key).setVisible(DETAILED_INVOICE.equals(type) || GROUPED_BY_ASSIGNEE.equals(type));
                    break;
                case EMPLOYEE_POSITION:
                    toNameMap.get(key).setVisible(DETAILED_INVOICE.equals(type));
                    break;
            }
        }

        for (String key : toDescMap.keySet()) {
            switch (key) {
                case TASK_ASSIGNEE:
                    toDescMap.get(key).setVisible(DETAILED_INVOICE.equals(type) || GROUPED_BY_TASK.equals(type));
                    break;
                case TASK_DESCRIPTION:
                    toDescMap.get(key).setVisible(DETAILED_INVOICE.equals(type) || GROUPED_BY_TASK.equals(type) || GROUPED_BY_PROJECT.equals(type));
                    break;
                case TIMESHEET_ENTRY:
                    toDescMap.get(key).setVisible(DETAILED_INVOICE.equals(type));
                    break;
                case TIMESHEET_PERIOD:
                    toDescMap.get(key).setVisible(DETAILED_INVOICE.equals(type));
                    break;
            }
        }

        //show field to name
        pnlFieldsToNameContainer.clear();
        toNameMap.forEach((k, v) -> {
            if (v.isVisible()) {

                MaterialPanel item = new MaterialPanel("group-box__item");
                item.add(v);
                pnlFieldsToNameContainer.add(item);
//                pnlFieldsToNameContainer.add(v);
            }
        });

        //show field to desc
        pnlFieldsToDescContainer.clear();
        toDescMap.forEach((k, v) -> {
            if (v.isVisible()) {
                MaterialPanel item = new MaterialPanel("group-box__item");
                item.add(v);
                pnlFieldsToDescContainer.add(item);
            }
        });

        if (!wrapperDetailedInvoice.getStyleName().contains(NOT_SELECTED_TYPE_STYLE)) {
            wrapperDetailedInvoice.addStyleName(NOT_SELECTED_TYPE_STYLE);
        }
        if (!wrapperGrouppedByAssignee.getStyleName().contains(NOT_SELECTED_TYPE_STYLE)) {
            wrapperGrouppedByAssignee.addStyleName(NOT_SELECTED_TYPE_STYLE);
        }
        if (!wrapperGrouppedByTask.getStyleName().contains(NOT_SELECTED_TYPE_STYLE)) {
            wrapperGrouppedByTask.addStyleName(NOT_SELECTED_TYPE_STYLE);
        }
        if (!wrapperGrouppedByProject.getStyleName().contains(NOT_SELECTED_TYPE_STYLE)) {
            wrapperGrouppedByProject.addStyleName(NOT_SELECTED_TYPE_STYLE);
        }

        switch (type) {
            case DETAILED_INVOICE:
                wrapperDetailedInvoice.removeStyleName(NOT_SELECTED_TYPE_STYLE);
                break;
            case GROUPED_BY_ASSIGNEE:
                wrapperGrouppedByAssignee.removeStyleName(NOT_SELECTED_TYPE_STYLE);
                break;
            case GROUPED_BY_TASK:
                wrapperGrouppedByTask.removeStyleName(NOT_SELECTED_TYPE_STYLE);
                break;
            case GROUPED_BY_PROJECT:
                wrapperGrouppedByProject.removeStyleName(NOT_SELECTED_TYPE_STYLE);
                break;
        }
    }

    private void initToDescFields() {
        toDescMap = new HashMap<>();

        KpiCheckBox ta = new KpiCheckBox(accountingStrings.taskAssignee());
        ta.setLayoutData(generateIncFieldKey(accountingStrings.taskAssignee()));
        ta.setValue(Boolean.TRUE);
        toDescMap.put(TASK_ASSIGNEE, ta);

        //task description
        KpiCheckBox td = new KpiCheckBox(wfmStrings.taskDescription());
        td.setLayoutData(generateIncFieldKey(accountingStrings.descriptionOfTask()));
        td.setValue(Boolean.TRUE);
        toDescMap.put(TASK_DESCRIPTION, td);

        //timesheet entry
        KpiCheckBox tsh = new KpiCheckBox(accountingStrings.timesheetEntryDate());
        tsh.setLayoutData(generateIncFieldKey(accountingStrings.dateOfTimesheetEntry()));
        tsh.setValue(Boolean.TRUE);
        toDescMap.put(TIMESHEET_ENTRY, tsh);

        //timesheet period
        KpiCheckBox tp = new KpiCheckBox(accountingStrings.timesheetPeriod());
        tp.setLayoutData(generateIncFieldKey(accountingStrings.timesheetPeriod()));
        tp.setValue(Boolean.TRUE);
        toDescMap.put(TIMESHEET_PERIOD, tp);

        KpiCheckBox tn = new KpiCheckBox(wfmStrings.taskName());
        tn.setLayoutData(generateIncFieldKey(accountingStrings.nameOfTask()));
        tn.setValue(Boolean.TRUE);
        toDescMap.put(TASK_NAME, tn);
    }

    private String generateIncFieldKey(String key) {
        return "[" + key + "]";
    }

    public DataListBox getAmountList() {
        return amountList;
    }

    public Integer[] getProjectIds() {
        return projectIDs != null ? projectIDs.toArray(new Integer[]{}) : null;
    }

    public HashMap<Integer, SelectItem> getProjectItemMap() {
        return projectItemMap;
    }

    public Date getFromDate() {
        return fromDate.getDate();
    }

    public Date getToDate() {
        return toDate.getDate();
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public Integer getCustomerId() {
        return lookUp.getSelectedItemID();
    }

    public TaxItem getTaxItem() {
        return taxLookUp.getSelectedData();
    }

    public String generateName(ProjectBaseData data) {
        StringBuilder name = new StringBuilder();

        for (String key : toNameMap.keySet()) {
            switch (key) {
                case PROJECT_NAME:
                    if (toNameMap.get(key).isVisible() && toNameMap.get(key).getValue() && !Utils.isNullOrEmpty(data.getProjectName())) {
                        name.append(data.getProjectName()).append("\n");
                    }
                    break;
                case TASK_NAME:
                    if (toNameMap.get(key).isVisible() && toNameMap.get(key).getValue() && !Utils.isNullOrEmpty(data.getTaskName())) {
                        name.append(data.getTaskName()).append("\n");
                    }
                    break;
                case TASK_ASSIGNEE:
                    if (toNameMap.get(key).isVisible() && toNameMap.get(key).getValue() && !Utils.isNullOrEmpty(data.getEmployeeName())) {
                        name.append(data.getEmployeeName()).append("\n");
                    }
                    break;
                case EMPLOYEE_POSITION:
                    if (toNameMap.get(key).isVisible() && toNameMap.get(key).getValue() && !Utils.isNullOrEmpty(data.getEmployeePosition())) {
                        name.append(data.getEmployeePosition()).append("\n");
                    }
                    break;
            }
        }
        return name.toString();
    }

    public String generateDescription(ProjectBaseData data) {
        StringBuilder desc = new StringBuilder();

        for (String key : toDescMap.keySet()) {
            switch (key) {
                case TASK_ASSIGNEE:
                    if (toDescMap.get(key).isVisible() && toDescMap.get(key).getValue() && !Utils.isNullOrEmpty(data.getEmployeeName())) {
                        desc.append(data.getEmployeeName()).append("\n");
                    }
                    break;
                case TASK_DESCRIPTION:
                    if (toDescMap.get(key).isVisible() && toDescMap.get(key).getValue() && !Utils.isNullOrEmpty(data.getTaskDescription())) {
                        desc.append(data.getTaskDescription()).append("\n");
                    }
                    break;
                case TIMESHEET_ENTRY:
                    if (toDescMap.get(key).isVisible() && toDescMap.get(key).getValue()
                            && data.getTsEntryDate() != null && data.getTsEntryDate().getNonConvertedDate() != null) {
                        desc.append(DateUtils.format(data.getTsEntryDate().getNonConvertedDate())).append("\n");
                    }
                    break;
                case TIMESHEET_PERIOD:
                    if (toDescMap.get(key).isVisible() && toDescMap.get(key).getValue()) {
                        desc.append(DateUtils.format(fromDate.getDate()) + "-" + DateUtils.format(toDate.getDate())).append("\n");
                    }
                    break;
                case TASK_NAME:
                    if (toDescMap.get(key).isVisible() && toDescMap.get(key).getValue()) {
                        desc.append(data.getTaskName()).append("\n");
                    }
            }
        }
        return desc.toString();
    }

    public boolean validate() {
        int errors = 0;

        errors += !Validation.validateLookUpRequired(lookUp) ? 1 : 0;
        errors += !Validation.validateDate(fromDate) ? 1 : 0;
        errors += !Validation.validateDate(toDate) ? 1 : 0;
        errors += projectIDs != null && !(projectIDs.size() > 0) ? 1 : 0;

        if (errors > 0) {
            Info.show(wfmStrings.unableToSave(), Info.Type.WARNING);
            return false;
        }

        boolean selected = false;
        for (KpiCheckBox checkBox : toNameMap.values()) {
            if (checkBox.isVisible() && checkBox.getValue()) {
                selected = true;
                break;
            }
        }

        for (KpiCheckBox checkBox : toDescMap.values()) {
            if (checkBox.isVisible() && checkBox.getValue()) {
                selected = true;
                break;
            }
        }

        if (!selected) {
            Info.show(accountingStrings.shouldSelectOneField(), Info.Type.WARNING);
            errors++;
        }

        return errors == 0;
    }

    protected static final String PROJECT_NAME = "PROJECT_NAME";
    protected static final String TASK_NAME = "TASK_NAME";
    protected static final String TASK_ASSIGNEE = "TASK_ASSIGNEE";
    protected static final String TASK_DESCRIPTION = "TASK_DESCRIPTION";
    protected static final String EMPLOYEE_POSITION = "EMPLOYEE_POSITION";
    protected static final String TIMESHEET_ENTRY = "TIMESHEET_ENTRY";
    protected static final String TIMESHEET_PERIOD = "TIMESHEET_PERIOD";

}