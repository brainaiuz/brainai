package com.finnetlimited.reportservice.core.client.ui.table;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.DurationType;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.ReportType;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.SqlColumnType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.FolderRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.SelectListRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit.DRSDateBox;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.finnetlimited.reportservice.core.client.gwtrpc.ViewRpc;
import com.finnetlimited.reportservice.core.client.ui.button.DRSButton;
import com.finnetlimited.reportservice.core.client.ui.element.CustomFilterComponents;
import com.finnetlimited.reportservice.core.client.ui.events.KPIChangeEvent;
import com.finnetlimited.reportservice.core.client.ui.listbox.DRSListBox;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * User: ${Dilsh0d}
 * Date: 17-Mar-2010
 * Time: 16:34:30
 */
public class FilterTable extends FlexTable {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private DRSButton addRow;
    private DRSDateBox endDate;
    private DRSDateBox startDate;
    //    private DRSListBox tipsList;
    private DRSListBox columns;
    private ListBox duration;
    private ListBox limit;
    private FlexCellFormatter formatter;
    private FilterTableAddRow advancedFilter;
    private DrillDownReportAddRow drillDownReport;
    private LinkedList<ColumnRpc> selectColumnList;
    private ArrayList<SelectListRpc> dateColumnList;

    private KpiCheckBox addNewAction;
    private KpiCheckBox viewAction;
    private KpiCheckBox editAction;
    private KpiCheckBox deleteAction;
    private KpiCheckBox viewEditAction;
    private KpiCheckBox drillReportAction;
    private KpiCheckBox viewEditActionIcon;

    /* Custom filter components.*/
    private ArrayList<CustomFilterComponents> customFieldsMap;              //CustomFilterComponents contains field, name, variable and field type.
    private FlowPanel customFieldsPanel;                                    //Div panel contains all custom filter components.
    private boolean customFieldsEnabled = false;                            //Is Custom Filter Enabled through xml configuration
    private HashMap<String, String> customQueries;                          // Used for lookup custom filter components. Contains variable value (#!custom_coutry_id!#), query.
    private final static String OPERATION_BOX = "OPERATION_BOX";
    private ReportRpc reportRPC;

    private Command command;

    public FilterTable() {
        setStyleName("order-table");
//        setWidth("646px");
        init();
    }

    private void init() {
        formatter = getFlexCellFormatter();
        startDate = new DRSDateBox();
        endDate = new DRSDateBox();
        duration = new DRSListBox();
        duration.setWidth("150px");
        columns = new DRSListBox();
        columns.setWidth("130px");
//        tipsList = new DRSListBox();
        limit = new DRSListBox();
        addRow = new DRSButton("<span class='markPlus'>" + wfmStrings.addRow() + "</span>", DRSButton.PREV_STYLE);
        addRow.getElement().setAttribute("style", "margin-top:20px;");
    }

    public void drawSaveFilterReports(final ReportRpc report) {
        drawFilterTable(new LinkedList<>(report.getColumnMap().values()), report.getTableType(), report);
        if (report.getSntFilterName() != null) {
            columns.setSelectedName(report.getSntFilterTitle());
            duration.setEnabled(true);
            DurationType type = DurationType.valueOf(report.getDurationType());
            for (int i = 0; i < duration.getItemCount(); i++) {
                if (type.getName().equals(duration.getItemText(i))) {
                    duration.setSelectedIndex(i);
                    startDate.setSelectedDate(type.getStartDate());
                    endDate.setSelectedDate(type.getEndDate());
                    if (DurationType.Between.name().equals(type.name())) {
                        startDate.setText(report.getStartDate());
                        endDate.setText(report.getEndDate());
                        startDate.setEnabled(true);
                        endDate.setEnabled(true);
                        duration.setEnabled(true);
                    } else {
                        startDate.setEnabled(false);
                        endDate.setEnabled(false);
                    }
                    break;
                }
            }
        }
        advancedFilter.drawSaveReportCriteriya(report);
        if (report.getLimit() == -1) {
            limit.setSelectedIndex(0);
        } else {
            for (int i = 1; i < limit.getItemCount(); i++) {
                if (limit.getValue(i).equals(report.getLimit() + "")) {
                    limit.setSelectedIndex(i);
                }
            }
        }
        CoreService.App.get().getReportListByUser(new AsyncCallback<ArrayList<FolderRpc>>() {

            public void onFailure(Throwable throwable) {

            }

            public void onSuccess(ArrayList<FolderRpc> folderRpcs) {
                ArrayList<SelectListRpc> reportList = new ArrayList<>();
                for (FolderRpc folder : folderRpcs) {
                    ArrayList<? extends SelectListRpc> tempReports = folder.getReports();
                    if (tempReports != null && tempReports.size() > 0) {
                        reportList.addAll(tempReports);
                    }
                }
                drillDownReport.setReports(reportList);
                drillReportAction.setValue(report.getShowDrillReports());
                drillDownReport.setVisible(drillReportAction.getValue());
                drillDownReport.drawBySavedOptions(report);
            }
        });

    }

    public void drawFilterTable(final LinkedList<ColumnRpc> selectColumns, String reportType, final ReportRpc report) {
        init();
        this.selectColumnList = selectColumns;
        int row = 0;

        // Standard Filters end

        //Advanced Filters begin
//        if (CompanyConstants.C30871.equals(Utils.getEncryptedCompanyID())) {
        addSectionTitle(row++, wfmStrings.advancedFilters());
//        }

        //draw advanced table
        advancedFilter = new FilterTableAddRow(selectColumnList, report);
//        if (CompanyConstants.C30871.equals(Utils.getEncryptedCompanyID())) {
        setWidget(row, 0, advancedFilter);
//        }
        formatter.setColSpan(row++, 0, 4);
        //add row
//        if (CompanyConstants.C30871.equals(Utils.getEncryptedCompanyID())) {
        setWidget(row, 0, addRow);
//        }
        formatter.setColSpan(row++, 0, 4);

        //Advanced Filters end

        //Limit Rows Count begin

        limit = new ListBox();
        limit.addItem("All", "all");
        limit.addItem("10", "10");
        limit.addItem("20", "20");
        limit.addItem("30", "30");
        limit.addItem("40", "40");
        limit.addItem("50", "50");
        limit.addItem("100", "100");
        limit.addItem("200", "200");
        limit.addItem("500", "500");
        limit.setSelectedIndex(2);

        if (ReportType.SUMMARY.name().equals(reportType)) {
            limit.setSelectedIndex(0);
            limit.setEnabled(false);
        }

        HorizontalPanel hPanel = new HorizontalPanel();
        HTML rowsDislay = new HTML("<b>" + wfmStrings.rowToDisplay() + ":</b>");
        hPanel.add(rowsDislay);
        hPanel.setCellVerticalAlignment(rowsDislay, VerticalPanel.ALIGN_MIDDLE);
        hPanel.add(limit);
        hPanel.setCellVerticalAlignment(limit, VerticalPanel.ALIGN_MIDDLE);

        //    setWidget(row, 0, hPanel);
        formatter.setColSpan(row, 0, 4);
        //Limit Rows Count end

        addRow.addClickHandler(clickEvent -> {
            if (command != null) {
                command.execute();
            }
            advancedFilter.addRow();
        });

        addNewAction = new KpiCheckBox();
        addNewAction.setValue(report.enableAddNewAction());
        viewAction = new KpiCheckBox();
        viewAction.setValue(report.enableViewAction());
        editAction = new KpiCheckBox();
        editAction.setValue(report.enableEditAction());
        deleteAction = new KpiCheckBox();
        deleteAction.setValue(report.enableDeleteAction());

        viewEditAction = new KpiCheckBox();
        viewEditAction.setValue(report.getShowActions());
        viewEditActionIcon = new KpiCheckBox();
        viewEditAction.setValue(report.getShowActionsIcon());
        drillReportAction = new KpiCheckBox();

        drillReportAction.addClickHandler(clickEvent -> drillDownReport.setVisible(drillReportAction.getValue()));
        drillReportAction.setVisible(false); //remmove to work

        row++;

        row++;
        drillDownReport = new DrillDownReportAddRow(selectColumns, report);
        CoreService.App.get().getReportListByUser(new AsyncCallback<ArrayList<FolderRpc>>() {

            public void onFailure(Throwable throwable) {

            }

            public void onSuccess(ArrayList<FolderRpc> folderRpcs) {
                ArrayList<SelectListRpc> reportList = new ArrayList<>();
                for (FolderRpc folder : folderRpcs) {
                    ArrayList<SelectListRpc> tempReports = folder.getReports();
                    if (tempReports != null && tempReports.size() > 0) {
                        reportList.addAll(tempReports);
                    }
                }
                drillDownReport.setReports(reportList);
                drillDownReport.setVisible(false);
                drillDownReport.init();
            }
        });
        formatter.setColSpan(row, 0, 2);
        //  setWidget(row++, 0, drillDownReport);
        /*addRow = new DRSButton("<span class='addNew'>Add Row</span>", DRSButton.PREV_STYLE);

        addRow.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                drillDownReport.addRow();
            }
        });*/
        //  setWidget(row++, 0, addRow);
    }

    /**
     * Create custom filter table in case xml template contains tag <customfilter/>.
     *
     * @param report Transfer object {@link ReportRpc}
     */
    public void drawCustomFilterTable(final ReportRpc report) {
        reportRPC = report;
        CoreService.App.get().getReportStructure(report.getViewCode(), new AsyncCallback<ViewRpc>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ViewRpc view) {
                //If custom filter enabled, then draw custom filter components.
                if (view.isCustomFilterEnabled()) {
                    customFieldsEnabled = view.isCustomFilterEnabled();

                    customFieldsPanel = new FlowPanel();

                    customFieldsPanel.setStyleName("customfilter");

                    customFieldsMap = new ArrayList<>();
                    customQueries = new HashMap<>();
                    addSectionTitle(4, "Custom Filters");

                    for (ColumnRpc column : view.getCustomFilterColumns()) {
                        //Initialize components for each custom filter.
                        createCustomField(column);
                    }

                    //If report saved and contains report_id, then fill out custom filter components with existing values.
                    if (report.getId() != null) {
                        setUpContent(report, customFieldsMap);
                    }

                }

                setWidget(5, 0, customFieldsPanel);
            }
        });
    }

    public void addSectionTitleWithColSpan(int row, String title, int colSpan) {
        setHTML(row, 0, "<b style='padding:0 0 0 10px'>" + title + "</b>");
        getFlexCellFormatter().setColSpan(row, 0, colSpan);
        getFlexCellFormatter().setStyleName(row, 0, "bheader");
        getFlexCellFormatter().getElement(row, 0).setAttribute("style", "height:auto;");
        getRowFormatter().setStyleName(row, "theader");
    }

    public void addSectionTitle(int row, String title) {
        addSectionTitleWithColSpan(row, title, 4);
    }

    public ReportRpc getReport(ReportRpc report) {
        String limitType = limit.getValue(limit.getSelectedIndex());

        if (!"all".equals(limitType)) {
            report.setLimit(Integer.parseInt(limitType));
        } else {
            report.setLimit(-1);
        }

        if (columns.getSelectedIndex() != 0) {
            if (startDate.getSelectedDate() != null && endDate.getSelectedDate() != null) {
                report.setStartDate(startDate.getText());
                report.setEndDate(endDate.getText());
            }
            report.setSntFilterName(selectColumnList.get(columns.getSelectId()).getName());
            report.setSntFilterTitle(columns.getSelecedName());
            report.setDurationType(duration.getValue(duration.getSelectedIndex()));
        } else {
            report.setStartDate(null);
            report.setEndDate(null);
            report.setSntFilterName(null);
            report.setSntFilterTitle(null);
            report.setDurationType(null);
        }

        //If Custom Fields Enabled, it return hash map <variable, value>.
        if (customFieldsEnabled) {
            HashMap<String, String> customFilters = new HashMap<>();

            for (CustomFilterComponents customFilterComponents : customFieldsMap) {
                int i = 0;

                //Get field type and put input value to HashMap with it variable.
                if ("textbox".equals(customFilterComponents.getType())) {
                    customFilters.put(customFilterComponents.getValue(), "".equals(((TextBox) customFilterComponents.getField()).getText()) ? "" : ((TextBox) customFilterComponents.getField()).getText().trim());
                } else if ("lookup".equals(customFilterComponents.getType())) {
                    String value = ((LookUp) customFilterComponents.getField()).getSelectedItemID() != null ? ((LookUp) customFilterComponents.getField()).getSelectedItemID().toString() : null;
                    if (value != null && value.contains("Type here")) {
                        value = null;
                    }
                    customFilters.put(customFilterComponents.getValue(), value);
                } else if ("date".equals(customFilterComponents.getType())) {
                    if (customFilterComponents.getField() instanceof DRSDateBox && customFilterComponents.getField() != null && customFilterComponents.getField().isVisible()) {
                        customFilters.put(customFilterComponents.getValue(), ((DRSDateBox) customFilterComponents.getField()).getSelectedDate().toGMTString());
                    } else if (customFilterComponents.getField() instanceof ListBox && !((ListBox) customFilterComponents.getField()).getValue(((ListBox) customFilterComponents.getField()).getSelectedIndex()).equals(DurationType.Between.getName()) && !((ListBox) customFilterComponents.getField()).getValue(((ListBox) customFilterComponents.getField()).getSelectedIndex()).equals(DurationType.Equals.getName())) {
                        customFilters.put(customFilterComponents.getValue(), ((ListBox) customFilterComponents.getField()).getValue(((ListBox) customFilterComponents.getField()).getSelectedIndex()));
                    }
                } else if ("grouping_field".equals(customFilterComponents.getType()) && report.getRangeType() != null && report.getRangeType().size() != 0) {
                    customFilters.put(customFilterComponents.getValue(), report.getRangeType().get(i));
                    i++;
                }
            }

            report.setCustomFilter(customFilters);

        }

        report = advancedFilter.getReportRpc(report);

        report.setEnableAddNewAction(addNewAction.getValue());
        report.setEnableViewAction(viewAction.getValue());
        report.setEnableEditAction(editAction.getValue());
        report.setEnableDeleteAction(deleteAction.getValue());
        report.setShowActions(viewEditAction.getValue());
        report.setShowActionsIcon(viewEditActionIcon.getValue());
        // report.setShowDrillReports(drillReportAction.getValue());

        report = drillDownReport.getReport(report);

        return report;
    }

    public void refreshFilterTable(LinkedList<ColumnRpc> selectColumns, String reportType) {

        if (selectColumnList != null) {
            String selectName = columns.getSelecedName();
            dateColumnList = new ArrayList<>();
            for (int i = 0; i < selectColumnList.size(); i++) {
                if (SqlColumnType.DATE.getName().equals(selectColumnList.get(i).getType())) {
                    SelectListRpc select = new SelectListRpc();
                    select.setId(i);
                    select.setName(selectColumnList.get(i).getTitle());
                    dateColumnList.add(select);
                }
            }

            columns.setItems(dateColumnList);
            if (selectName != null && !columns.setSelectedName(selectName)) {
                startDate.setText("");
                endDate.setText("");
                duration.setSelectedIndex(0);
            }

            if (ReportType.SUMMARY.name().equals(reportType)) {
                limit.setEnabled(false);
            } else {
                limit.setEnabled(true);
            }
        }
        advancedFilter.refreshTableRow(selectColumns);
    }

    public void setCommand(Command command) {
        this.command = command;
    }


    /**
     * Create custom filter component with the certain type. Currently available types: TextBox, LookUp and DRSDateBox.
     *
     * @param field Transfer Object {@link ColumnRpc}
     */
    public void createCustomField(final ColumnRpc field) {
        CustomFilterComponents customFilterComponents = new CustomFilterComponents();
        customFilterComponents.setType(field.getCustomField());

        if ("textbox".equals(field.getCustomField())) {
            TextBox customTextField = new TextBox();
            customFilterComponents.setField(customTextField);
            customFilterComponents.setFieldName(field.getName() != null ? field.getName() : "Custom textbox");
            customFilterComponents.setValue(field.getFirstValue());
            customFieldsPanel.add(new Label(field.getName() != null ? field.getName() : "Custom textbox"));
            customFieldsPanel.add(customTextField);

        } else if ("lookup".equals(field.getCustomField())) {
            customQueries.put(field.getFirstValue(), field.getCustomQuery());
            LookUp lookUp = new LookUp() {
                @Override
                protected void onItemDeleteInsertUpdate(int type) {
                }

                @Override
                protected void lookUpService(final ListingFilterParameter filterParametrs) {
                    //Return items for custom filter lookup. Contains id and string.
                    getCustomFilterLookUpItems(filterParametrs, field.getCustomQuery(), this);
                }
            };
            customFilterComponents.setField(lookUp);
            customFilterComponents.setFieldName(field.getName() != null ? field.getName() : wfmStrings.customField());
            customFilterComponents.setValue(field.getFirstValue());
            customFieldsPanel.add(new Label(field.getName() != null ? field.getName() : "Custom textbox"));
            customFieldsPanel.add(lookUp);

        } else if ("date".equals(field.getCustomField())) {
            customFieldsPanel.add(new Label(wfmStrings.customDate()));
            createCustomFilterDateBoxes(field);
        } else if ("grouping_field".equals(field.getCustomField())) {
            customFilterComponents.setValue(field.getFirstValue());
        }
        if (customFilterComponents.getField() != null || "grouping_field".equals(customFilterComponents.getType())) {
            customFieldsMap.add(customFilterComponents);
        }
    }

    /**
     * Set data for custom filter for saved reports.
     *
     * @param report                 Retrieve customfilter's hashmap (variable, value).
     * @param customFilterComponents Custom Filter components {@link CustomFilterComponents}
     */
    private void setUpContent(ReportRpc report, ArrayList<CustomFilterComponents> customFilterComponents) {

        //Initialized new list to eliminate java.util.ConcurrentModificationException that occur on change event.
        HashMap<String, String> customFilters = report.getCustomFilter();
        try {
            //Pass through each variable and value
            for (Map.Entry<String, String> entrySet : customFilters.entrySet()) {

                //Pass through custom filter components.
                for (CustomFilterComponents filterComponents : customFilterComponents) {

                    //If variable equals to component variable, then it set value of this vairiable to custom filter component.
                    if (entrySet.getKey().equals(filterComponents.getValue())) {

                        if ("textbox".equals(filterComponents.getType())) {

                            if (!"".equals(entrySet.getValue())) {
                                ((TextBox) filterComponents.getField()).setText(entrySet.getValue());
                            }
                        } else if ("lookup".equals(filterComponents.getType())) {

                            if (!"".equals(entrySet.getValue()) && entrySet.getValue() != null && !"null".equals(entrySet.getValue())) {
                                setCustomLookUpValue(entrySet.getValue(), filterComponents.getValue(), filterComponents.getField());
                            }
                        } else if ("date".equals(filterComponents.getType())) {
                            //Setting date with cutted timezone format. Example: UZT.
                            if (oneOfTheDurationType(entrySet.getValue()) && filterComponents.getField() instanceof ListBox) {
                                for (int i = 0; i < ((ListBox) filterComponents.getField()).getItemCount(); i++) {
                                    ListBox lb = ((ListBox) filterComponents.getField());
                                    if (lb.getItemText(i).equals(entrySet.getValue())) {
                                        lb.setSelectedIndex(i);
                                        lb.fireEvent(new KPIChangeEvent());
                                    }
                                }

                            } else if (entrySet.getValue().matches("... ... .. ..:..:.. ... ....") || entrySet.getValue().matches("... ... .. ..:..:.. ....")) {
                                ((DRSDateBox) filterComponents.getField()).setSelectedDate(DateTimeFormat.getFormat("EEE MMM dd HH:mm:ss yyyy").parse(entrySet.getValue().subSequence(0, entrySet.getValue().lastIndexOf(":00") + 3) + " " + entrySet.getValue().substring(entrySet.getValue().length() - 4)));
                            }
                        }
                    }

                }

            }
        } catch (ConcurrentModificationException ignored) {

        }
    }

    /**
     * Used for custom filter lookup components, when selected item id is known.
     *
     * @param ID                   saved selected item id
     * @param customFilterVariable variable name from xml file.
     * @param widget               LookUp component
     */
    private void setCustomLookUpValue(String ID, String customFilterVariable, final Widget widget) {
        final SelectItem[] selectItem = {new SelectItem()};
        final Integer id = Integer.valueOf(ID);
        String query = "";
        for (Map.Entry<String, String> entrySet : customQueries.entrySet()) {
            if (customFilterVariable.equals(entrySet.getKey())) {
                query = entrySet.getValue();
                break;
            }
        }
        if (!"".equals(query)) {
            CoreService.App.get().getCustomItems(query, new AsyncCallback<ArrayList<SelectItem>>() {
                @Override
                public void onFailure(Throwable throwable) {
                }

                @Override
                public void onSuccess(ArrayList<SelectItem> selectItems) {
                    for (SelectItem item : selectItems) {
                        if (item.getId().byteValue() == id.byteValue()) {
                            ((LookUp) widget).setSelected(item);
                        }
                    }
                }
            });
        }
    }

    /**
     * Retrieve Data for custom filter lookup component
     *
     * @param filterParameter
     * @param query           Custom query to retriev data for custom filter lookup component.
     * @param lookup          Custom component.
     */
    private void getCustomFilterLookUpItems(final ListingFilterParameter filterParameter, String query, final LookUp lookup) {
        CoreService.App.get().getCustomItems(query, new AsyncCallback<ArrayList<com.edatasite.workforce.gwt.core.client.rpc.SelectItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(ArrayList<SelectItem> selectItems) {
                lookup.setItems(filterParameter.getSearchKey(), selectItems.toArray(new SelectItem[]{}));
            }
        });

    }

    /**
     * Create custom filter date box. If xml file parameter contains second value,
     * it will create second date box for cases where between dates required.
     * If operation set to equals or between, one or two date box would be shown.
     * Before, after duration types removed from list box.
     * If durations listbox's value equals to "EQUALS" or "BETWEEN",
     * map with custom fields get values from date boxes, else it will get values from
     * durations listbox.
     *
     * @param field Transfer object {@link ColumnRpc}
     */
    private void createCustomFilterDateBoxes(final ColumnRpc field) {
        ArrayList<CustomFilterComponents> list = new ArrayList<>();
        final CustomFilterComponents customDate1 = new CustomFilterComponents();
        HorizontalPanel panel = new HorizontalPanel();
        final ListBox operationBox = new ListBox();
        final DRSDateBox dateBox1 = new DRSDateBox();
        final DRSDateBox dateBox2 = new DRSDateBox();
        final CustomFilterComponents customDate2 = new CustomFilterComponents();

        panel.setStyleName("customOperationBox");
        dateBox1.setStyleName("customDateBox1");
        dateBox2.setStyleName("customDateBox2");

        for (DurationType type : DurationType.values()) {
            if (!type.getName().toLowerCase().contains("next") & !type.getName().toLowerCase().contains("tomorrow") & !type.getName().toLowerCase().contains("quarter") & !type.getName().toLowerCase().contains("after") & !type.getName().toLowerCase().contains("before")) {
                operationBox.addItem(type.getName());
            }
        }

        //Operation box (contains duration variables) set into CustomFilterComponents}
        final CustomFilterComponents customOperationComponent = new CustomFilterComponents();
        customOperationComponent.setType(field.getCustomField());
        customOperationComponent.setField(operationBox);
        customOperationComponent.setFieldName(OPERATION_BOX);
        customOperationComponent.setValue(field.getFirstValue());

        customDate1.setFieldName("");
        customDate1.setField(dateBox1);
        customDate1.setType(field.getCustomField());
        customDate1.setValue(field.getFirstValue());

        list.add(customDate1);
        customFieldsMap.add(customDate1);
        panel.add(operationBox);
        panel.add(dateBox1);

        if (field.getSecondValue() != null && !"".equals(field.getSecondValue())) {
            dateBox2.setVisible(false);
            customDate2.setField(dateBox2);
            customDate2.setFieldName("");
            customDate2.setType(field.getCustomField());
            customDate2.setValue(field.getSecondValue());
            list.add(customDate2);
            customFieldsMap.add(customDate2);
            panel.add(dateBox2);
        }

        customFieldsPanel.add(panel);

        customFieldsMap.add(customOperationComponent);

        operationBox.addChangeHandler(changeEvent -> {

            if (field.getSecondValue() != null && !"".equals(field.getSecondValue()) && operationBox.getValue(operationBox.getSelectedIndex()).equals(DurationType.Between.getName())) {
                dateBox2.setVisible(true);
                dateBox1.setVisible(true);
                if (!customFieldsMap.contains(customDate1)) {
                    customFieldsMap.add(customDate1);
                }
                if (!customFieldsMap.contains(customDate2)) {
                    customFieldsMap.add(customDate2);
                }
            } else if (field.getSecondValue() != null && !"".equals(field.getSecondValue()) && !operationBox.getValue(operationBox.getSelectedIndex()).equals(DurationType.Between.getName()) && operationBox.getValue(operationBox.getSelectedIndex()).equals(DurationType.Equals.getName())) {
                dateBox2.setVisible(false);
                dateBox1.setVisible(true);
                customFieldsMap.remove(customDate2);
            } else if (field.getSecondValue() != null && !"".equals(field.getSecondValue()) && !DurationType.Between.getName().equals(operationBox.getValue(operationBox.getSelectedIndex())) && !DurationType.Equals.getName().equals(operationBox.getValue(operationBox.getSelectedIndex()))) {
                customOperationComponent.setValue(field.getFirstValue());
                dateBox1.setVisible(false);
                dateBox2.setVisible(false);
            }

        });
    }

    private boolean oneOfTheDurationType(String durationType) {
        boolean oneOfTheDurationType = false;
        for (DurationType type : DurationType.values()) {
            if (type.getName().equals(durationType)) {
                oneOfTheDurationType = true;
                break;
            }
        }
        return oneOfTheDurationType;
    }
}
