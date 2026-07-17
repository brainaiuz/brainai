package com.edatasite.workforce.gwt.project.client.ui.view.projectcost;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.AddListener;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.ColumnStatements;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.listener.DropdownListener;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectCostAllDataItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectCostItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectCostSelectItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectCostService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectCostServiceAsync;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 29.04.2010
 * Time: 12:20:06
 * To change this template use File | Settings | File Templates.
 */
public class AddProjectEstimateCostView extends View implements Constants, Colapse {

    private final ProjectCostServiceAsync projectCostService = ProjectCostService.App.get();
    private final ProjectServiceAsync projectService = ProjectService.App.get();
    private static final NumberFormat format = NumberFormat.getFormat("#,##0.00");
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final int COLUMNS_COUNT = 6;

    public AddProjectEstimateCostView() {
        super("addprojectcostestimate", wfmStrings.projectCostEstimate());
    }

    private Integer periodId = null;
    private Integer projectId = null;
    private Integer resourceTypeId = null;

    private Integer dayWorking;

    private WfmForm tableLeft;
    private WfmForm tableRight;
    private WfmForm otherTitle;

    private WfmForm.Field projectField;
    private WfmForm.Field taskField;
    private WfmForm.Field resourceTypeField;

    private DataListBox projects;
    private DataListBox tasks;
    private DataListBox resourceTypes;
    private DataListBox periodDateList;
    private DatePicker startDate;
    private DatePicker endDate;

    private HTML dayOffs;
    private HTML workingDays;
    private HTML employeeAmount;
    private HTML otherAmount;
    private HTML total;

    private boolean saveAndClose = false;
    private ProjectCostSelectItem[] resourcePoolItems;
    private ProjectCostSelectItem[] otherCostItems;
    private SelectItem[] chargeTypeItems;

    private WfmButton2 saveButton;
    private WfmButton2 saveAndCloseButton;

    private SimpleLink addNewProject;
    private DynamicTable employeeDynamicTable;
    private DynamicTable otherOverHeadDynamicTable;
    private VerticalPanel contentPanel;
    private ProjectCostAllDataItem costAllDataItem;
    private Map<Integer, ProjectCostSelectItem> map;

    private boolean from = true;
    private boolean to = true;

    public String getIconStyle() {
        return null;
    }

    protected Widget onInitialize() {
        initInitialize();
        return null;
    }

    private void initInitialize() {
        contentPanel = new VerticalPanel();
        contentPanel.setSpacing(5);
        add(contentPanel);

        map = new HashMap<>();
        drawDescriptionPanel();
        initEmployeeDynamicTable();
        drawEmployeeResourceDTAddRowPanel();
        initOtherOverHeadDynamicTable();
        drawOtherOverHeadDTAddRowPanel();
        drawSaveButtonlPanel();
        initProjects();

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PROJECT_ADD, this, (sender, args) -> {
            if (args != null) {
                projectId = (Integer) args;
            }
            initProjects();
        });
    }

    /**
     * Draw pther over head add row button
     */
    private void drawOtherOverHeadDTAddRowPanel() {
        WfmButton2 addOtherOverHead = new WfmButton2(wfmStrings.addNewLines());
        addOtherOverHead.addClickHandler(event -> {
            if (otherCostItems != null) {
                Widget[] widgets = getOtherOverHeadWidgets(null);
                otherOverHeadDynamicTable.addRow(widgets);
            } else {
                Info.show(wfmStrings.pleaseSelectRequiredFields(), Info.Type.INFO);
            }
        });


        HTML totalName = new HTML("<b style='font-size:14px'>" + wfmStrings.total() + ":</b>");
        totalName.setStyleName("customTitle");
        total = new HTML("0.0");
        total.getElement().getStyle().setFontSize(14, Style.Unit.PX);
        total.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);

        HorizontalPanel totalPanel = new HorizontalPanel();
        totalPanel.setSpacing(10);
        totalPanel.setWidth("200px");
        totalPanel.add(totalName);
        totalPanel.add(total);

        HorizontalPanel optianal = new HorizontalPanel();
        optianal.setWidth("100%");
        optianal.add(addOtherOverHead);
        optianal.setCellHorizontalAlignment(totalPanel, HorizontalPanel.ALIGN_LEFT);
        optianal.setCellVerticalAlignment(totalPanel, VerticalPanel.ALIGN_MIDDLE);

        optianal.add(totalPanel);
        optianal.setCellHorizontalAlignment(totalPanel, HorizontalPanel.ALIGN_RIGHT);
        optianal.setCellVerticalAlignment(totalPanel, VerticalPanel.ALIGN_MIDDLE);

        contentPanel.add(optianal);
    }

    /**
     * Draw Other Over Head Dynamic Table
     */
    private void initOtherOverHeadDynamicTable() {
        otherTitle = new WfmForm();
        otherTitle.addField(null, new Label(""));
        otherTitle.setStyleName("padding10");
        otherTitle.setWidth("300px");
        otherTitle.setCellPadding(7);
        otherTitle.addTitleField(projectStrings.otherOverheadCosts() + "<br/>");
        otherOverHeadDynamicTable = new DynamicTable(getOtherOverHeadColumnArray());
        otherOverHeadDynamicTable.setHeight("50px");
        otherOverHeadDynamicTable.addListener(new AddListener() {
            public void plusClicked(int rowId) {
                Widget[] widgets = getOtherOverHeadWidgets(null);
                otherOverHeadDynamicTable.insertRow(rowId + 1, widgets);
            }

            public void minusClicked(int rowId, Integer objectId) {
                calculationOtherPercentSubtotal();
            }
        });

        otherAmount = new HTML("<b>0.0</b>");
        otherOverHeadDynamicTable.setHTML(1, 4, "<b>" + wfmStrings.subtotal() + ":</b>");
        otherOverHeadDynamicTable.getFlexCellFormatter().setStyleName(1, 4, "customTitle");
        otherOverHeadDynamicTable.getFlexCellFormatter().setHeight(1, 4, "25px");
        otherOverHeadDynamicTable.getFlexCellFormatter().setAlignment(1, 4, HorizontalPanel.ALIGN_CENTER, VerticalPanel.ALIGN_MIDDLE);

        otherOverHeadDynamicTable.setWidget(1, 5, otherAmount);
        otherOverHeadDynamicTable.getFlexCellFormatter().setAlignment(1, 5, HorizontalPanel.ALIGN_CENTER, VerticalPanel.ALIGN_MIDDLE);

        contentPanel.add(new HTML("<br/>"));
        contentPanel.add(otherTitle);
        contentPanel.add(otherOverHeadDynamicTable);
    }

    private Widget[] getOtherOverHeadWidgets(ProjectCostItem result) {
        Widget[] widgets = new Widget[COLUMNS_COUNT - 1];

        final WfmDropdown costItemName = new WfmDropdown(true, projectStrings.otherCostItem());
        costItemName.setWidth("200px");
        costItemName.addItems(otherCostItems);

        final DataListBox chargeType = new DataListBox();
        chargeType.setWidth("200px");
        chargeType.setItems(chargeTypeItems);

        final TextBox percent = new TextBox();
        percent.setName(String.valueOf(otherOverHeadDynamicTable.getRowNumber()));
        final TextBox amount = new TextBox();
        final HTML plannedAmount = new HTML();

        final Command command = () -> getOtherCostItem();

        costItemName.addEventHandler(new DropdownListener() {
            public void itemSelected() {
                if (costItemName.getSelectedId() != null) {
                    ProjectCostSelectItem item = (ProjectCostSelectItem) costItemName.getData(costItemName.getSelectedId());
                    if (item.isLogicPercent()) {
                        chargeType.setSelected(1);
                        chargeType.setEnabled(false);
                        percent.setEnabled(true);
                        if (item.getPercent() != null) {
                            percent.setText(String.valueOf(item.getPercent()));
                        }
                        amount.setEnabled(false);
                        calculationOtherPercentSubtotal();
                    } else {
                        chargeType.setSelected(2);
                        chargeType.setEnabled(false);
                        percent.setEnabled(false);
                        amount.setEnabled(true);
                        if (item.getAmount() != null) {
                            amount.setText(String.valueOf(item.getAmount()));
                        }
                        if (amount.getText() != null && !"".equals(amount.getText().trim())) {
                            plannedAmount.setHTML("<b>" + format.format(NumberFormat.getScientificFormat().parse(amount.getText())) + "</b>");
                        } else {
                            plannedAmount.setHTML("<b>" + format.format(0.0) + "</b>");
                        }
                        calculationOtherAmountSubtotal();
                    }
                } else {
                    chargeType.setEnabled(true);
                    amount.setEnabled(true);
                    amount.setText("");
                    percent.setEnabled(false);
                    percent.setText("");
                    chargeType.setSelectedNullLabel();
                    calculationOtherPercentSubtotal();
                }
            }

            public void saveNewItem() {
                new AddOtherCostItem(command, resourceTypeId);
            }
        });

        chargeType.addValueChangeHandler(event -> {
            if (chargeType.getSelectedIndex() == 1) {
                percent.setEnabled(true);
                if (amount.getText() != null && !"".equals(amount.getText().trim())) {
                    float amountNum = (float) NumberFormat.getScientificFormat().parse(amount.getText());
                    float otherNum = (float) NumberFormat.getScientificFormat().parse(otherAmount.getText());
                    float totalNum = (float) NumberFormat.getScientificFormat().parse(total.getText());
                    otherAmount.setHTML("<b>" + format.format(otherNum - amountNum) + "</b>");
                    total.setHTML("<b>" + format.format(totalNum - amountNum) + "</b>");
                }
                amount.setText("");
                plannedAmount.setText("");
                amount.setEnabled(false);
            } else {
                percent.setText("");
                plannedAmount.setText("");
                percent.setEnabled(false);
                amount.setEnabled(true);
                calculationOtherPercentSubtotal();
            }

        });

        percent.addChangeHandler(event -> calculationOtherPercentSubtotal());

        amount.addChangeHandler(event -> {
            if (amount.getText() != null && !"".equals(amount.getText().trim())) {
                plannedAmount.setHTML("<b>" + format.format(NumberFormat.getScientificFormat().parse(amount.getText())) + "</b>");
            } else {
                plannedAmount.setHTML("<b>" + format.format(0.0) + "</b>");
            }
            calculationOtherAmountSubtotal();
        });

        if (result != null) {
            costItemName.setSelected(result.getOtherCostItemId());
            if (result.isPercent()) {
                chargeType.setSelected(1);
                percent.setText(String.valueOf(result.getOtherPercent()));
                amount.setEnabled(false);
            } else {
                chargeType.setSelected(2);
                amount.setText(String.valueOf(result.getOtherAmount()));
                percent.setEnabled(false);
                amount.setEnabled(true);
            }
            plannedAmount.setHTML("<b>" + format.format(result.getOtherPlannedAmount()) + "</b>");
        }

        widgets[0] = costItemName;
        widgets[1] = chargeType;
        widgets[2] = percent;
        widgets[3] = amount;
        widgets[4] = plannedAmount;

        return widgets;
    }

    private void calculationOtherAmountSubtotal() {
        float sum = 0.0f, empSum = 0.0f;
        for (int rowId = 0; rowId < otherOverHeadDynamicTable.getRowNumber(); rowId++) {
            DynamicTableItem items = otherOverHeadDynamicTable.getItem(rowId);
            HTML plnActual = (HTML) items.getColumnById("plnAmount");
            if (plnActual.getText() != null && !"".equals(plnActual.getText().trim())) {
                sum += (float) NumberFormat.getScientificFormat().parse(plnActual.getText());
            }
        }
        if (employeeAmount.getText() != null && !"".equals(employeeAmount.getText().trim())) {
            empSum = (float) NumberFormat.getScientificFormat().parse(employeeAmount.getText());
        }
        otherAmount.setHTML("<b>" + format.format(sum) + "</b>");
        total.setHTML("<b>" + format.format(empSum + sum) + "</b>");
    }

    private void calculationOtherPercentSubtotal() {
        float sum = 0.0f, allSum = 0.0f, empSum = 0.0f, percentNum = 0.0f;
        if (employeeAmount.getText() != null && !"".equals(employeeAmount.getText().trim())) {
            empSum = (float) NumberFormat.getScientificFormat().parse(employeeAmount.getText());
        }

        for (int rowId = 0; rowId < otherOverHeadDynamicTable.getRowNumber(); rowId++) {
            DynamicTableItem tableItem = otherOverHeadDynamicTable.getItem(rowId);
            DataListBox chargeType = (DataListBox) tableItem.getColumnById("chargeType");

            HTML plnAmount = (HTML) tableItem.getColumnById("plnAmount");
            TextBox percent = (TextBox) tableItem.getColumnById("percent");

            if (chargeType.getSelectedId() == 1) {
                if (percent.getText() != null && !"".equals(percent.getText().trim())) {
                    percentNum = (float) NumberFormat.getScientificFormat().parse(percent.getText());
                }
                if (percentNum != 0) {
                    plnAmount.setHTML("<b>" + format.format((empSum + sum) / 100 * percentNum) + "</b>");
                } else {
                    plnAmount.setHTML("<b>" + format.format(0) + "</b>");
                }
                if (plnAmount.getText() != null && !"".equals(plnAmount.getText().trim())) {
                    sum += (float) NumberFormat.getScientificFormat().parse(plnAmount.getText());
                }
            }

            if (plnAmount.getText() != null && !"".equals(plnAmount.getText().trim())) {
                allSum += (float) NumberFormat.getScientificFormat().parse(plnAmount.getText());
            }
            percentNum = 0.0f;
        }

        otherAmount.setHTML("<b>" + format.format(allSum) + "</b>");
        total.setText(format.format(allSum + empSum));
    }

    /**
     * Draw Employee Dynamic Table
     */
    private void initEmployeeDynamicTable() {
        employeeAmount = new HTML("<b>0.0</b>");

        employeeDynamicTable = new DynamicTable(getEmploeeResourceColumnArray());
        employeeDynamicTable.setHeight("50px");
        employeeDynamicTable.addListener(new AddListener() {

            public void plusClicked(int rowId) {
                Widget[] widgets = getEmployeeResourceWidgetArray(null);
                employeeDynamicTable.insertRow(rowId + 1, widgets);
            }

            public void minusClicked(int rowId, Integer objectId) {
                calculatonPlannedTotal();
                calculationOtherPercentSubtotal();
            }
        });

        employeeDynamicTable.addClickHandler(event -> {
            if (!projects.isSomethingSelected()) {
                Info.show(wfmStrings.pleaseSelectProjectFirst(), Info.Type.WARNING);
            }
        });

        employeeDynamicTable.setHTML(1, 5, "<b>" + wfmStrings.subtotal() + ":</b>");
        employeeDynamicTable.getFlexCellFormatter().setStyleName(1, 5, "customTitle");
        employeeDynamicTable.getFlexCellFormatter().setHeight(1, 5, "25px");
        employeeDynamicTable.getFlexCellFormatter().setAlignment(1, 5, HorizontalPanel.ALIGN_CENTER, VerticalPanel.ALIGN_MIDDLE);


        employeeDynamicTable.setWidget(1, 6, employeeAmount);
        employeeDynamicTable.getFlexCellFormatter().setAlignment(1, 6, HorizontalPanel.ALIGN_CENTER, VerticalPanel.ALIGN_MIDDLE);

        contentPanel.add(employeeDynamicTable);
    }

    /**
     * Employee Resource Dynamic Table draw Widhets array
     *
     * @param result Old Save parametrs
     * @return One row DynamicTable rows
     */
    private Widget[] getEmployeeResourceWidgetArray(ProjectCostItem result) {
        Widget[] widgets = new Widget[COLUMNS_COUNT];

        final WfmDropdown resourcePools = new WfmDropdown(true, projectStrings.newResourcePool());
        resourcePools.setWidth("150px");
        resourcePools.addItems(resourcePoolItems);

        final WfmDropdown resources = new WfmDropdown(true, projectStrings.resource());
        resources.setWidth("150px");

        final TextBox plannedQuantity = new TextBox();
        final TextBox plannedDaily = new TextBox();
        final TextBox plannedRate = new TextBox();
        final HTML plannedAmount = new HTML();

        final Command poolCammand = () -> refreshAllResourcePoolList();

        final Command resourceCommand = () -> refreshAllResourceList(resourcePools.getSelectedId());

        resourcePools.addEventHandler(new DropdownListener() {
            public void itemSelected() {
                if (resourceTypes.getSelectedItem() != null && resourcePools.getSelectedId() != null) {
                    LoadingPanel.loading(true);
                    projectCostService.getResources(resourceTypes.getSelectedItem().getId(), resourcePools.getSelectedItem().getId(), new AbstractAsyncCallback<ProjectCostSelectItem[]>() {
                        public void success(ProjectCostSelectItem[] result) {
                            LoadingPanel.loading(false);
                            resources.addItems(result);
                        }
                    });
                    ProjectCostSelectItem item = (ProjectCostSelectItem) resourcePools.getData(resourcePools.getSelectedId());
                    plannedRate.setText(String.valueOf(item.getRate()));
                } else {
                    resources.addItems(new SelectItem[0]);
                }
            }

            public void saveNewItem() {
                new AddResourcePool(poolCammand, resourceTypeId);
            }
        });

        resources.addEventHandler(new DropdownListener() {
            public void itemSelected() {
                if (resources.getSelectedId() != null) {
                    if (resources.getSelectedId() != null) {
                        ProjectCostSelectItem item = (ProjectCostSelectItem) resources.getData(resources.getSelectedId());
                        if (item.getRate() != 0) {
                            plannedRate.setText(String.valueOf(item.getRate()));
                        }
                    }
                    float multi = 0, quantity = 0, rate = 0;

                    if (plannedQuantity.getText() != null && !"".equals(plannedQuantity.getText().trim())) {
                        quantity = (float) NumberFormat.getScientificFormat().parse(plannedQuantity.getText());
                    }

                    if (plannedRate.getText() != null && !"".equals(plannedRate.getText().trim())) {
                        rate = (float) NumberFormat.getScientificFormat().parse(plannedRate.getText());
                    }

                    if (quantity != 0 && rate != 0) {
                        multi = quantity * rate;
                    }

                    plannedAmount.setHTML("<b>" + format.format(multi) + "</b>");
                    calculatonPlannedTotal();
                    calculationOtherPercentSubtotal();
                }
            }

            public void saveNewItem() {
                if (resourcePools.getSelectedId() != null) {
                    new AddResource(resourceCommand, resourceTypeId, resourceTypes.getSelectedItem().getName(), resourcePools.getSelectedId());
                }
            }
        });

        plannedQuantity.addChangeHandler(event -> {
            float multi = 0, quantity = 0, rate = 0;

            if (plannedDaily == null || "".equals(plannedDaily.getText().trim())) {
                if (plannedQuantity.getText() != null && !"".equals(plannedQuantity.getText().trim())) {
                    float plannedAllDay = Float.parseFloat(plannedQuantity.getText());
                    plannedDaily.setText(format.format(plannedAllDay / dayWorking));
                }
            } else {
                float plannedDay = Float.parseFloat(plannedDaily.getText());
                if (plannedQuantity.getText() != null && !"".equals(plannedQuantity.getText().trim())) {
                    float plannedAllDay = Float.parseFloat(plannedQuantity.getText());
                    if (plannedAllDay > plannedDay * dayWorking) {
                        Info.show(projectStrings.plannedValuesCanNoAllocatedSelected(), Info.Type.INFO);
                        plannedQuantity.setText("");
                    }
                }
            }

            if (plannedQuantity.getText() != null && !"".equals(plannedQuantity.getText().trim())) {
                quantity = (float) NumberFormat.getScientificFormat().parse(plannedQuantity.getText());
            }
            if (plannedRate.getText() != null && !"".equals(plannedRate.getText().trim())) {
                rate = (float) NumberFormat.getScientificFormat().parse(plannedRate.getText());
            }

            if (quantity != 0 && rate != 0) {
                multi = quantity * rate;
            }

            plannedAmount.setHTML("<b>" + format.format(multi) + "</b>");
            calculatonPlannedTotal();
            calculationOtherPercentSubtotal();
        });

        plannedDaily.addChangeHandler(event -> {
            if (plannedQuantity.getText() == null || "".equals(plannedQuantity.getText().trim())) {
                if (plannedDaily.getText() != null && !"".equals(plannedDaily.getText().trim())) {
                    plannedQuantity.setText(format.format(dayWorking * Float.parseFloat(plannedDaily.getText())));
                }
            } else {
                float plannedAllDay = Float.parseFloat(plannedQuantity.getText());
                if (plannedDaily.getText() != null && !"".equals(plannedDaily.getText().trim())) {
                    float plannedDay = Float.parseFloat(plannedDaily.getText());
                    if (plannedAllDay / plannedDay < dayWorking) {
                        Info.show(projectStrings.plannedValuesCanNoAllocatedSelected(), Info.Type.INFO);
                        plannedDaily.setText("");
                    }
                }
            }
            float multi = 0, quantity = 0, rate = 0;
            if (plannedQuantity.getText() != null && !"".equals(plannedQuantity.getText().trim())) {
                quantity = (float) NumberFormat.getScientificFormat().parse(plannedQuantity.getText());
            }
            if (plannedRate.getText() != null && !"".equals(plannedRate.getText().trim())) {
                rate = (float) NumberFormat.getScientificFormat().parse(plannedRate.getText());
            }

            if (quantity != 0 && rate != 0) {
                multi = quantity * rate;
            }

            plannedAmount.setHTML("<b>" + format.format(multi) + "</b>");
            calculatonPlannedTotal();
            calculationOtherPercentSubtotal();
        });

        plannedRate.addChangeHandler(event -> {
            float multi = 0, quantity = 0, rate = 0;

            if (plannedQuantity.getText() != null && !"".equals(plannedQuantity.getText().trim())) {
                quantity = (float) NumberFormat.getScientificFormat().parse(plannedQuantity.getText());
            }

            if (plannedRate.getText() != null && !"".equals(plannedRate.getText().trim())) {
                rate = (float) NumberFormat.getScientificFormat().parse(plannedRate.getText());
            }

            if (quantity != 0 && rate != 0) {
                multi = quantity * rate;
            }

            plannedAmount.setHTML("<b>" + format.format(multi) + "</b>");
            calculatonPlannedTotal();
            calculationOtherPercentSubtotal();
        });


        if (result != null) {
            resourcePools.setSelected(result.getResourcePoolId());
            final Integer resourceId = result.getResourceId();
            projectCostService.getResources(resourceTypes.getSelectedItem().getId(), resourcePools.getSelectedItem().getId(), new AbstractAsyncCallback<ProjectCostSelectItem[]>() {
                public void success(ProjectCostSelectItem[] items) {
                    resources.addItems(items);
                    if (resourceId != null) {
                        resources.setSelected(resourceId);
                    }
                }
            });

            plannedQuantity.setText(format.format(result.getPlannedQuantity()));
            plannedDaily.setText(format.format(result.getPlannedDaily()));
            plannedRate.setText(format.format(result.getPlannedRate()));
            plannedAmount.setHTML("<b>" + format.format(result.getPlannedCost()) + "</b>");
        }

        widgets[0] = resourcePools;
        widgets[1] = resources;
        widgets[2] = plannedQuantity;
        widgets[3] = plannedDaily;
        widgets[4] = plannedRate;
        widgets[5] = plannedAmount;

        return widgets;
    }

    private void refreshAllResourceList(final Integer resourcePoolId) {
        if (resourceTypes.getSelectedItem() != null && resourcePoolId != null) {
            LoadingPanel.loading(true);
            projectCostService.getResources(resourceTypes.getSelectedItem().getId(), resourcePoolId, new AbstractAsyncCallback<ProjectCostSelectItem[]>() {
                public void success(ProjectCostSelectItem[] result) {
                    LoadingPanel.loading(false);
                    for (int k = 0; k < employeeDynamicTable.getRowNumber(); k++) {
                        DynamicTableItem tableItem = employeeDynamicTable.getItem(k);
                        WfmDropdown resourcePool = (WfmDropdown) tableItem.getColumnById("resourcePool");
                        if (resourcePool.getSelectedId() != null && resourcePoolId.equals(resourcePool.getSelectedId())) {
                            WfmDropdown resource = (WfmDropdown) tableItem.getColumnById("resource");
                            Integer itemId = resource.getSelectedId();
                            resource.addItems(result);
                            if (itemId != null) {
                                resource.setSelected(itemId);
                            }
                        }
                    }
                }
            });
        }
    }

    private void refreshAllResourcePoolList() {
        if (resourceTypeId != null) {
            LoadingPanel.loading(true);
            projectCostService.getResourcePoolItems(resourceTypeId, new AbstractAsyncCallback<ProjectCostSelectItem[]>() {
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                public void success(ProjectCostSelectItem[] result) {
                    LoadingPanel.loading(false);
                    resourcePoolItems = result;
                    for (int k = 0; k < employeeDynamicTable.getRowNumber(); k++) {
                        DynamicTableItem tableItem = employeeDynamicTable.getItem(k);
                        WfmDropdown resourcePool = (WfmDropdown) tableItem.getColumnById("resourcePool");
                        Integer itemId = resourcePool.getSelectedId();
                        resourcePool.addItems(result);
                        if (itemId != null) {
                            resourcePool.setSelected(itemId);
                        }
                    }
                }
            });
        }
    }

    /**
     * Draw top ProjectCost Items
     */
    private void drawDescriptionPanel() {
        HorizontalPanel innerSettingsPanel = new HorizontalPanel();
        innerSettingsPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
        innerSettingsPanel.setWidth("800px");

        tableLeft = new WfmForm(new String[]{"20%", "40%", "40%"});
        tableLeft.addField(null, new Label(""));
        tableLeft.setStyleName("padding10");

        tableRight = new WfmForm(new String[]{"35%", "40%", "25%"});
        tableRight.addField(null, new Label(""));
        tableRight.setStyleName("padding10");

        projects = new DataListBox();
        projects.setAllowFirstItem(true);
        projects.addStyleName(DEFAULT_WIDTH);
        tasks = new DataListBox();
        tasks.addStyleName(DEFAULT_WIDTH);
        resourceTypes = new DataListBox();
        resourceTypes.addStyleName(DEFAULT_WIDTH);

        addNewProject = new SimpleLink(projectStrings.addProject(), SimpleLink.ADD_ICON, "project|add/add/pm");
        addNewProject.setWordWrap(true);

        periodDateList = new DataListBox();
        periodDateList.addStyleName(DEFAULT_WIDTH);
        startDate = new DatePicker();
        startDate.addStyleName(DEFAULT_WIDTH);
        endDate = new DatePicker();
        endDate.addStyleName(DEFAULT_WIDTH);

        HTML workingDaysName = new HTML(projectStrings.workingDays() + ":");
        workingDaysName.setStyleName("customTitle");
        workingDays = new HTML("");
        workingDays.setStyleName("customTitle");

        HTML dayOffsName = new HTML(wfmStrings.dayOff() + ":");
        dayOffsName.setStyleName("customTitle");
        dayOffs = new HTML("");
        dayOffs.setStyleName("customTitle");

        HorizontalPanel workingDayPanel = new HorizontalPanel();
        workingDayPanel.setSpacing(2);
        workingDayPanel.add(workingDaysName);
        workingDayPanel.add(workingDays);

        HorizontalPanel dayOffsPanel = new HorizontalPanel();
        dayOffsPanel.setSpacing(2);
        dayOffsPanel.add(dayOffsName);
        dayOffsPanel.add(dayOffs);

        projects.addValueChangeHandler(widget -> {
            reinit();
            projectChange();
            getSavedPeriod();
            isEmpityPeriod();
            isAllFieldSelect();
        });

        tasks.addValueChangeHandler(event -> {
            reinit();
            getSavedPeriod();
            isEmpityPeriod();
            isAllFieldSelect();
        });

        resourceTypes.addValueChangeHandler(event -> {
            reinit();
            resourceTypeChange();
            isAllFieldSelect();
        });

        periodDateList.addValueChangeHandler(event -> {
            if (periodDateList.getSelectedId() != null) {
                startDate.setText(wfmStrings.pleaseSelect());
                endDate.setText(wfmStrings.pleaseSelect());
                startDate.setEnabled(false);
                endDate.setEnabled(false);
                isAllFieldSelect();
            } else {
                startDate.setEnabled(true);
                endDate.setEnabled(true);
            }
        });
        startDate.addChangeHandler(event -> {
            if (endDate.getDate() != null && endDate.getDate().getTime() < startDate.getDate().getTime()) {
                startDate.setText(wfmStrings.pleaseSelect());
            }
            isEmpityPeriod();
            isAllFieldSelect();
        });

        endDate.addChangeHandler(event -> {
            if (startDate.getDate() != null && endDate.getDate().getTime() < startDate.getDate().getTime()) {
                endDate.setText(wfmStrings.pleaseSelect());
            }
            isEmpityPeriod();
            isAllFieldSelect();
        });

        projectCostService.getResourceTypes(new AbstractAsyncCallback<SelectItem[]>() {
            public void success(SelectItem[] result) {
                resourceTypes.setItems(result);
            }
        });

        tableLeft.setCellPadding(7);
        if (Utils.hasPermission(PermissionConstants.PM_PROJECT_ADD)) {
            projectField = tableLeft.addField(Property.get(Constants.PROJECT, wfmStrings.projectField()), new Widget[]{projects, addNewProject}, true, 5);
        } else {
            projectField = tableLeft.addField(Property.get(Constants.PROJECT, wfmStrings.projectField()), projects, true);
        }

        taskField = tableLeft.addField(wfmStrings.task(), tasks, true, wfmStrings.pleaseSelectTask());
        resourceTypeField = tableLeft.addField(projectStrings.resourceType(), resourceTypes, true, projectStrings.pleaseSelectResourceType());

        tableRight.addField(projectStrings.savedPeriods(), periodDateList);
        tableRight.addField(wfmStrings.fromDate(), new Widget[]{startDate, workingDayPanel}, true, 5);
        tableRight.addField(wfmStrings.toDate(), new Widget[]{endDate, dayOffsPanel}, true, 5);

        tableLeft.addTitleField("<br/>" + projectStrings.employeeResources());

        innerSettingsPanel.add(tableLeft);
        innerSettingsPanel.add(tableRight);
        contentPanel.add(innerSettingsPanel);

        chargeTypeItems = new SelectItem[2];
        chargeTypeItems[0] = new SelectItem(1, "percentage");
        chargeTypeItems[1] = new SelectItem(2, "fixed amount");
    }

    /**
     *
     */
    private void isEmpityPeriod() {
        int selectCount = 0;
        if (projects.getSelectedId() == null) {
            selectCount++;
        }
        if (tasks.getSelectedId() == null) {
            selectCount++;
        }
        if ((startDate.getDate() == null || wfmStrings.pleaseSelect().equalsIgnoreCase(startDate.getText())) && startDate.isEnabled()) {
            selectCount++;
        }
        if ((endDate.getDate() == null || wfmStrings.pleaseSelect().equalsIgnoreCase(endDate.getText())) && endDate.isEnabled()) {
            selectCount++;
        }
        if (selectCount == 0 && from && to && periodDateList.getSelectedIndex() == 0) {
            from = false;
            to = false;
            LoadingPanel.loading(true);
            projectCostService.isEmpityPeriod(projects.getSelectedId(), tasks.getSelectedId(), startDate.getDate(), endDate.getDate(), new AbstractAsyncCallback<Boolean>() {
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                    from = true;
                    to = true;
                }

                public void success(Boolean result) {
                    LoadingPanel.loading(false);
                    from = true;
                    to = true;
                    if (result) {
                        isAllFieldSelect();
                    } else {
                        Info.show(projectStrings.selectedPeriodCanNotOverlap(), Info.Type.INFO);
                        startDate.setText(wfmStrings.pleaseSelect());
                        endDate.setText(wfmStrings.pleaseSelect());
                    }
                }
            });
        }
    }

    /**
     * Get project list By Manager
     */
    private void initProjects() {
        projectService.getProjectsList(PM, new AbstractAsyncCallback<SelectItem[]>() {
            public void success(final SelectItem[] object) {
                DeferredCommand.addCommand(() -> {
                    projects.setItems(object);
                    if (projectId != null) {
                        projects.setSelected(projectId);
                    }
                    projectChange();
                });
            }
        });
    }

    /**
     * Project Cost Select startDate and endDate
     */
    private void getSavedPeriod() {
        int selectCount = 0;
        if (tasks.getSelectedItem() == null) {
            selectCount++;
        }
        if (projects.getSelectedItem() == null) {
            selectCount++;
        }
        if (selectCount == 0) {
            projectCostService.getProjectCostPeriodList(projects.getSelectedId(), tasks.getSelectedId(), new AbstractAsyncCallback<ProjectCostSelectItem[]>() {
                public void failure(Throwable caught) {

                }

                public void success(ProjectCostSelectItem[] result) {
                    periodDateList.setItems(result);
                    map.clear();
                    for (ProjectCostSelectItem aResult : result) {
                        map.put(aResult.getId(), aResult);
                    }
                }
            });
        }
    }

    /**
     * Project Cost Item Fileds
     */
    private void isAllFieldSelect() {
        int selectedCount = 0;
        if (tasks.getSelectedItem() == null) {
            selectedCount++;
        }
        if ((startDate.getDate() == null || wfmStrings.pleaseSelect().equalsIgnoreCase(startDate.getText())) && startDate.isEnabled()) {
            selectedCount++;
        }
        if ((endDate.getDate() == null || wfmStrings.pleaseSelect().equalsIgnoreCase(endDate.getText())) && endDate.isEnabled()) {
            selectedCount++;
        }
        if (projects.getSelectedItem() == null) {
            selectedCount++;
        }
        if (resourceTypes.getSelectedItem() == null) {
            selectedCount++;
        }
        if (selectedCount == 0 && from && to) {
            from = false;
            to = false;
            ProjectCostAllDataItem item = new ProjectCostAllDataItem();
            item.setTaskId(tasks.getSelectedItem().getId());
            item.setProjectId(projectId);
            item.setResourceTypeId(resourceTypeId);
            if (periodDateList.getSelectedIndex() == 0) {
                item.setFrom(startDate.getDate());
                item.setTo(endDate.getDate());
            } else {
                ProjectCostSelectItem select = map.get(periodDateList.getSelectedId());
                item.setFrom(select.getStartDate());
                item.setTo(select.getEndDate());
            }

            LoadingPanel.loading(true);
            projectCostService.getProjectCostItems(item, new AbstractAsyncCallback<ProjectCostAllDataItem>() {
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                    from = true;
                    to = true;
                }

                public void success(ProjectCostAllDataItem allDataItem) {
                    from = true;
                    to = true;
                    periodId = allDataItem.getCostPeriodId();
                    // draw resource dynamic table
                    employeeDynamicTable.clear();
                    employeeAmount.setHTML("");
                    float planned = 0.0f;
                    ProjectCostItem[] resources = allDataItem.getResources();
                    for (int i = 0; i < resources.length; i++) {
                        planned += resources[i].getPlannedCost();
                        Widget[] widgets = getEmployeeResourceWidgetArray(resources[i]);
                        employeeDynamicTable.addRow(widgets);
                        employeeDynamicTable.getItem(i).setObjectId(resources[i].getCostItemId());
                    }
                    employeeAmount.setHTML("<b>" + format.format(planned) + "</b>");

                    // draw otherOverHead dynamic table
                    otherOverHeadDynamicTable.clear();
                    otherAmount.setHTML("");
                    float amount = 0.0f;
                    ProjectCostItem[] others = allDataItem.getOtherOverHeads();
                    for (int i = 0; i < others.length; i++) {
                        amount += others[i].getOtherPlannedAmount();
                        Widget[] widgets = getOtherOverHeadWidgets(others[i]);
                        otherOverHeadDynamicTable.addRow(widgets);
                        otherOverHeadDynamicTable.getItem(i).setObjectId(others[i].getCostItemId());
                    }
                    otherAmount.setHTML("<b>" + format.format(amount) + "</b>");
                    // calculation total
                    total.setHTML("<b>" + format.format(planned + amount) + "</b>");
                    LoadingPanel.loading(false);
                }
            });
            // working days
            projectCostService.getByDateCompanyWorkingDate(item.getFrom(), item.getTo(), new AbstractAsyncCallback<Integer[]>() {
                public void failure(Throwable caught) {

                }

                public void success(Integer[] result) {
                    dayWorking = result[0];
                    workingDays.setHTML("<b>" + result[0] + "</b>");
                    dayOffs.setHTML("<b>" + result[1] + "</b>");
                }
            });
        }
    }

    private void calculatonPlannedTotal() {
        float sum = 0;
        for (int rowId = 0; rowId < employeeDynamicTable.getRowNumber(); rowId++) {
            DynamicTableItem tableItem = employeeDynamicTable.getItem(rowId);
            HTML plnAmount = (HTML) tableItem.getColumnById("plnAmount");
            if (plnAmount.getText() != null && !"".equals(plnAmount.getText().trim())) {
                sum += (float) NumberFormat.getScientificFormat().parse(plnAmount.getText());
            }
        }
        employeeAmount.setHTML("<b>" + format.format(sum) + "</b>");
    }

    private void projectChange() {
        if (projects.getSelectedItem() != null) {
            if (projects.getSelectedItem().getId() != 0) {
                projectId = projects.getSelectedItem().getId();
                getTasks();
            }
        }
    }

    private void resourceTypeChange() {
        if (resourceTypes.getSelectedItem() != null) {
            if (resourceTypes.getSelectedIndex() != 0) {
                resourceTypeId = resourceTypes.getSelectedItem().getId();
                getResourcePoolItems(resourceTypeId);
            }
        }
    }

    private void getTasks() {
        LoadingPanel.loading(true);
        projectCostService.getProjectTasks(projectId, new AbstractAsyncCallback<SelectItem[]>() {
            public void success(SelectItem[] result) {
                LoadingPanel.loading(false);
                tasks.setItems(result);
            }
        });
    }

    private void getResourcePoolItems(Integer resourceTypeId) {
        if (resourceTypeId != null) {
            projectCostService.getResourcePoolItems(resourceTypeId, new AbstractAsyncCallback<ProjectCostSelectItem[]>() {
                public void success(ProjectCostSelectItem[] result) {
                    resourcePoolItems = result;
                }
            });
            projectCostService.getOtherCostItemList(resourceTypeId, new AbstractAsyncCallback<ProjectCostSelectItem[]>() {
                public void failure(Throwable caught) {

                }

                public void success(ProjectCostSelectItem[] result) {
                    otherCostItems = result;
                }
            });
        }
    }

    private void getOtherCostItem() {
        if (resourceTypeId != null) {
            LoadingPanel.loading(true);
            projectCostService.getOtherCostItemList(resourceTypeId, new AbstractAsyncCallback<ProjectCostSelectItem[]>() {
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                public void success(ProjectCostSelectItem[] result) {
                    LoadingPanel.loading(false);
                    otherCostItems = result;
                    for (int i = 0; i < otherOverHeadDynamicTable.getRowNumber(); i++) {
                        DynamicTableItem item = otherOverHeadDynamicTable.getItem(i);
                        WfmDropdown otherCostItem = (WfmDropdown) item.getColumnById("costItemName");
                        Integer selectId = otherCostItem.getSelectedId();
                        otherCostItem.addItems(otherCostItems);
                        if (selectId != null) {
                            otherCostItem.setSelected(selectId);
                        }
                    }
                }
            });
        }
    }

    /**
     * Draw add employee resource dynamic table row
     */
    private void drawEmployeeResourceDTAddRowPanel() {
        WfmButton2 addNewLine = new WfmButton2(wfmStrings.addNewLines());
        addNewLine.addClickHandler(sender -> {
            if (resourcePoolItems != null && resourceTypeId != null && projectId != null && tasks.getSelectedIndex() != 0 && startDate.getDate() != null) {
                Widget[] widgets = getEmployeeResourceWidgetArray(null);
                employeeDynamicTable.addRow(widgets);
            } else {
                if (resourceTypeId != null && projectId != null && tasks.getSelectedIndex() != 0 && startDate.getDate() != null) {
                    projectCostService.getResourcePoolItems(resourceTypeId, new AbstractAsyncCallback<ProjectCostSelectItem[]>() {
                        public void success(ProjectCostSelectItem[] result) {
                            resourcePoolItems = result;
                            Widget[] widgets = getEmployeeResourceWidgetArray(null);
                            employeeDynamicTable.addRow(widgets);
                        }
                    });
                } else {
                    Info.show(wfmStrings.pleaseSelectRequiredFields(), Info.Type.INFO);
                }
            }
        });
        contentPanel.add(addNewLine);
        contentPanel.setCellHorizontalAlignment(addNewLine, HorizontalPanel.ALIGN_LEFT);
    }

    /**
     * Draw Employee Resource Column title
     *
     * @return DynamicTableColumn[] column array
     */
    private DynamicTableColumn[] getEmploeeResourceColumnArray() {
        DynamicTableColumn[] columns = new DynamicTableColumn[COLUMNS_COUNT];
        columns[0] = new DynamicTableColumn(projectStrings.resourcePool(), "resourcePool", new ColumnStatements("", projectStrings.pleaseSelectResourcePool()), 180);
        columns[1] = new DynamicTableColumn(projectStrings.resource(), "resource", new ColumnStatements("", projectStrings.pleaseSelectResource()), 180);
        columns[2] = new DynamicTableColumn("Pln qty/hrs", "plnQuantity", new ColumnStatements("", ""), 100, true);
        columns[3] = new DynamicTableColumn("Dly qty/hrs", "dlyQuantity", new ColumnStatements("", ""), 100, true);
        columns[4] = new DynamicTableColumn("Pln rate", "plnRate", new ColumnStatements("", ""), 100, true);
        columns[5] = new DynamicTableColumn(projectStrings.plannedAmount(), "plnAmount", new ColumnStatements("", ""), 130);
        return columns;
    }

    /**
     * Draw other over head Column title
     */
    private DynamicTableColumn[] getOtherOverHeadColumnArray() {
        DynamicTableColumn[] columns = new DynamicTableColumn[COLUMNS_COUNT - 1];
        columns[0] = new DynamicTableColumn(projectStrings.costItemName(), "costItemName", new ColumnStatements("", projectStrings.pleaseSelectCostItemName()), 240);
        columns[1] = new DynamicTableColumn(projectStrings.chargeType(), "chargeType", new ColumnStatements("", projectStrings.pleaseSelectChargeType()), 240);
        columns[2] = new DynamicTableColumn("%", "percent", new ColumnStatements("", ""), 100, true);
        columns[3] = new DynamicTableColumn(wfmStrings.amount(), "amounty", new ColumnStatements("", ""), 100, true);
        columns[4] = new DynamicTableColumn(projectStrings.plannedAmount(), "plnAmount", new ColumnStatements("", ""), 130);
        return columns;
    }

    /**
     * Save All Data to Data Base
     */
    private void drawSaveButtonlPanel() {

        HorizontalPanel tailActionPanel = new HorizontalPanel();
        tailActionPanel.setSpacing(10);
        tailActionPanel.setWidth("100%");

        HorizontalPanel actionPanel = new HorizontalPanel();

        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveAndCloseButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);

        saveButton.addClickHandler(sender -> saveCostSheet());

        saveAndCloseButton.addClickHandler(sender -> {
            saveAndClose = true;
            saveCostSheet();
        });
        actionPanel.add(saveButton);
        actionPanel.add(saveAndCloseButton);

        tailActionPanel.add(actionPanel);
        tailActionPanel.setCellHorizontalAlignment(actionPanel, VerticalPanel.ALIGN_RIGHT);

        contentPanel.add(tailActionPanel);
    }

    /**
     * Save Table Validation
     *
     * @return
     */
    private boolean validate() {
        int errors = 0;
        tableLeft.cleanupErrors();
        tableRight.cleanupErrors();

        if (!Validation.validateListBoxRequired(projects, projectField, wfmStrings.pleaseSelectProject())) {
            errors++;
            Info.show(wfmStrings.pleaseSelectProject(), Info.Type.WARNING);
            return false;
        }

        if (!Validation.validateListBoxRequired(tasks, taskField, wfmStrings.pleaseSelectTask())) {
            errors++;
            Info.show(wfmStrings.pleaseSelectTask(), Info.Type.WARNING);
            return false;
        }

        if (!Validation.validateListBoxRequired(resourceTypes, resourceTypeField, projectStrings.pleaseSelectResourceType())) {
            errors++;
            Info.show(projectStrings.pleaseSelectResourceType(), Info.Type.WARNING);
            return false;
        }

        if ((startDate.getDate() == null || wfmStrings.pleaseSelect().equalsIgnoreCase(startDate.getText())) && periodDateList.getSelectedIndex() == 0) {
            errors++;
            Info.show(projectStrings.pleaseSelectFromDate(), Info.Type.WARNING);
            return false;
        }

        if ((endDate.getDate() == null || wfmStrings.pleaseSelect().equalsIgnoreCase(endDate.getText())) && periodDateList.getSelectedIndex() == 0) {
            errors++;
            Info.show(projectStrings.pleaseSelectToDate(), Info.Type.WARNING);
            return false;
        }

        employeeDynamicTable.resetValidation();
        costAllDataItem = new ProjectCostAllDataItem(projectId, tasks.getSelectedId(), resourceTypeId, startDate.getDate(), endDate.getDate());
        if (periodDateList.getSelectedIndex() != 0) {
            ProjectCostSelectItem item = map.get(periodDateList.getSelectedId());
            costAllDataItem.setFrom(item.getStartDate());
            costAllDataItem.setTo(item.getEndDate());
            costAllDataItem.setCostPeriodId(item.getId());
        } else {
            costAllDataItem.setCostPeriodId(periodId);
        }
        ProjectCostItem[] employeeResourceItem = new ProjectCostItem[employeeDynamicTable.getRowNumber()];
        for (int rowId = 0; rowId < employeeDynamicTable.getRowNumber(); rowId++) {

            DynamicTableItem tableItem = employeeDynamicTable.getItem(rowId);
            WfmDropdown resourcePool = (WfmDropdown) tableItem.getColumnById("resourcePool");
            WfmDropdown resource = (WfmDropdown) tableItem.getColumnById("resource");
            TextBox plannedQuantity = (TextBox) tableItem.getColumnById("plnQuantity");
            TextBox plennedDaily = (TextBox) tableItem.getColumnById("dlyQuantity");
            TextBox plannedRate = (TextBox) tableItem.getColumnById("plnRate");
            HTML plnAmount = (HTML) tableItem.getColumnById("plnAmount");

            employeeResourceItem[rowId] = new ProjectCostItem();
            employeeResourceItem[rowId].setCostItemId(tableItem.getObjectId());

            if (resourcePool.getSelectedItem() != null) {
                employeeResourceItem[rowId].setResourcePoolId(resourcePool.getSelectedItem().getId());
            }
            if (resource.getSelectedItem() != null) {
                employeeResourceItem[rowId].setResourceId(resource.getSelectedItem().getId());
            }
            if (plannedQuantity.getText() != null && !"".equals(plannedQuantity.getText())) {
                employeeResourceItem[rowId].setPlannedQuantity((float) NumberFormat.getScientificFormat().parse(plannedQuantity.getText()));
            }
            if (plannedRate.getText() != null && !"".equals(plannedRate.getText().trim())) {
                employeeResourceItem[rowId].setPlannedRate((float) NumberFormat.getScientificFormat().parse(plannedRate.getText()));
            }
            if (plennedDaily.getText() != null && !"".equals(plennedDaily.getText().trim())) {
                employeeResourceItem[rowId].setPlannedDaily((float) NumberFormat.getScientificFormat().parse(plennedDaily.getText()));
            }
            if (plnAmount.getText() != null && !"".equals(plnAmount.getText().trim())) {
                employeeResourceItem[rowId].setPlannedCost((float) NumberFormat.getScientificFormat().parse(plnAmount.getText()));
            }

            if (resourcePool.getSelectedId() == null) {
                employeeDynamicTable.notValid(rowId, "resourcePool");
                errors++;
            }
        }

        otherOverHeadDynamicTable.resetValidation();
        ProjectCostItem[] otherOverHeadItem = new ProjectCostItem[otherOverHeadDynamicTable.getRowNumber()];
        for (int rowId = 0; rowId < otherOverHeadDynamicTable.getRowNumber(); rowId++) {
            DynamicTableItem tableRow = otherOverHeadDynamicTable.getItem(rowId);
            WfmDropdown otherCostItem = (WfmDropdown) tableRow.getColumnById("costItemName");
            DataListBox chargeType = (DataListBox) tableRow.getColumnById("chargeType");
            TextBox percent = (TextBox) tableRow.getColumnById("percent");
            TextBox amount = (TextBox) tableRow.getColumnById("amounty");
            HTML plannedAmount = (HTML) tableRow.getColumnById("plnAmount");

            otherOverHeadItem[rowId] = new ProjectCostItem();
            otherOverHeadItem[rowId].setCostItemId(tableRow.getObjectId());

            otherOverHeadItem[rowId].setOtherCostItemId(otherCostItem.getSelectedId());
            if (plannedAmount.getText() != null && !"".equals(plannedAmount.getText().trim())) {
                otherOverHeadItem[rowId].setOtherPlannedAmount((float) NumberFormat.getScientificFormat().parse(plannedAmount.getText()));
            }

            if (percent.getText() != null && !"".equals(percent.getText().trim())) {
                otherOverHeadItem[rowId].setOtherPercent((float) NumberFormat.getScientificFormat().parse(percent.getText()));
                otherOverHeadItem[rowId].setPercent(true);
            }

            if (amount.getText() != null && !"".equals(amount.getText().trim())) {
                otherOverHeadItem[rowId].setOtherAmount((float) NumberFormat.getScientificFormat().parse(amount.getText()));
                otherOverHeadItem[rowId].setPercent(false);
            }

            if (otherCostItem.getSelectedId() == null) {
                otherOverHeadDynamicTable.notValid(rowId, "costItemName");
                errors++;
            }

            if (chargeType.getSelectedIndex() == 0) {
                otherOverHeadDynamicTable.notValid(rowId, "chargeType");
                errors++;
            }
        }
        costAllDataItem.setResources(employeeResourceItem);
        costAllDataItem.setOtherOverHeads(otherOverHeadItem);

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        } else {
            return true;
        }
    }

    private void onShellOk() {
        if (saveAndClose) {
            closeTab();
        } else {
//            reinit();
        }
    }

    private void saveCostSheet() {
        if (validate()) {
            LoadingPanel.loading(true);
            projectCostService.saveEstimateCostSheet(costAllDataItem, new AbstractAsyncCallback<Void>() {
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                public void success(Void result) {
                    LoadingPanel.loading(false);
                    Info.show("Save successfull", Info.Type.INFO);
                    if (periodDateList.getSelectedIndex() == 0) {
                        getSavedPeriod();
                    }
                    onShellOk();
                }
            });
        }
    }

    public void reinit() {
        employeeDynamicTable.clear();
        employeeAmount.setHTML("");
        otherOverHeadDynamicTable.clear();
        otherAmount.setHTML("");
        total.setHTML("");
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}