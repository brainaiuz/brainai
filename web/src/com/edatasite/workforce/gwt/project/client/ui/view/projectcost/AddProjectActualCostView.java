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

/**
 * User: Dilsh0d
 * Date: 19-May-2010
 * Time: 15:00:53
 */
public class AddProjectActualCostView extends View implements Colapse, Constants {

    private final ProjectCostServiceAsync projectCostService = ProjectCostService.App.get();
    private final ProjectServiceAsync projectService = ProjectService.App.get();
    private static final NumberFormat format = NumberFormat.getFormat("#,##0.00");
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final int COLUMNS_COUNT = 6;

    private Integer projectId = null;
    private Integer resourceTypeId = null;

    private WfmForm tableLeft;
    private WfmForm tableRight;
    private WfmForm otherTitle;

    private WfmForm.Field projectField;
    private WfmForm.Field taskField;
    private WfmForm.Field resourceTypeField;

    private DataListBox projects;
    private DataListBox tasks;
    private DataListBox resourceTypes;
    private DatePicker date;

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

    private boolean from = true;
    private boolean to = true;


    public AddProjectActualCostView() {
        super("addprojectcostactual", projectStrings.projectCostActual());
    }

    public Widget onInitialize() {
        initInitialize();
        return null;
    }

    private void initInitialize() {
        contentPanel = new VerticalPanel();
        contentPanel.setSpacing(5);
        add(contentPanel);

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
        optianal.setWidth("95%");
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

            }
        });

        otherAmount = new HTML("<b>0.0</b>");
        otherOverHeadDynamicTable.setHTML(1, 4, "<b>Subtotal:</b>");
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
        costItemName.setWidth("150px");
        costItemName.addItems(otherCostItems);

        final DataListBox chargeType = new DataListBox();
        chargeType.setWidth("150px");
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
                percent.setEnabled(false);
                plannedAmount.setText("");
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
                if (result.getOtherPercent() != null && result.getOtherPercent() != 0) {
                    percent.setText(String.valueOf(result.getOtherPercent()));
                }
                amount.setEnabled(false);
                calculationOtherPercentSubtotal();
            } else {
                chargeType.setSelected(2);
                if (result.getOtherAmount() != null && result.getOtherAmount() != 0) {
                    amount.setText(String.valueOf(result.getOtherAmount()));
                }
                percent.setEnabled(false);
                amount.setEnabled(true);
                calculationOtherAmountSubtotal();
            }
            plannedAmount.setHTML("<b>" + format.format(result.getOtherPlannedAmount()) + "</b>");
            if (result.isPlanned()) {
                costItemName.setEnabled(false);
            }
            ProjectCostSelectItem item = (ProjectCostSelectItem) costItemName.getData(costItemName.getSelectedId());
            if (item.isLogicPercent()) {
                chargeType.setSelected(1);
                chargeType.setEnabled(false);
                percent.setEnabled(true);
                if (result.getOtherPercent() == null && result.getOtherPercent() == 0) {
                    percent.setText(String.valueOf(item.getPercent()));
                }
                amount.setEnabled(false);
                calculationOtherPercentSubtotal();
            } else {
                chargeType.setSelected(2);
                chargeType.setEnabled(false);
                percent.setEnabled(false);
                amount.setEnabled(true);
                amount.setText(String.valueOf(item.getAmount()));
                if (amount.getText() != null && !"".equals(amount.getText().trim())) {
                    plannedAmount.setHTML("<b>" + format.format(NumberFormat.getScientificFormat().parse(amount.getText())) + "</b>");
                } else {
                    plannedAmount.setHTML("<b>" + format.format(0.0) + "</b>");
                }
                calculationOtherAmountSubtotal();
            }
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

        employeeDynamicTable.setHTML(1, 4, "<b>Subtotal:</b>");
        employeeDynamicTable.getFlexCellFormatter().setStyleName(1, 4, "customTitle");
        employeeDynamicTable.getFlexCellFormatter().setHeight(1, 4, "25px");
        employeeDynamicTable.getFlexCellFormatter().setAlignment(1, 4, HorizontalPanel.ALIGN_CENTER, VerticalPanel.ALIGN_MIDDLE);


        employeeDynamicTable.setWidget(1, 5, employeeAmount);
        employeeDynamicTable.getFlexCellFormatter().setAlignment(1, 5, HorizontalPanel.ALIGN_CENTER, VerticalPanel.ALIGN_MIDDLE);

        contentPanel.add(employeeDynamicTable);
    }

    /**
     * Employee Resource Dynamic Table draw Widhets array
     *
     * @param result Old Save parametrs
     * @return One row DynamicTable rows
     */
    private Widget[] getEmployeeResourceWidgetArray(ProjectCostItem result) {
        Widget[] widgets = new Widget[COLUMNS_COUNT - 1];

        final WfmDropdown resourcePools = new WfmDropdown(true, projectStrings.newResourcePool());
        resourcePools.setWidth("150px");
        resourcePools.addItems(resourcePoolItems);

        final WfmDropdown resources = new WfmDropdown(true, projectStrings.resource());
        resources.setWidth("150px");

        final TextBox plannedQuantity = new TextBox();
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
            final Float rate = result.getActualRate();
            resourcePools.setSelected(result.getResourcePoolId());
            final Integer resourceId = result.getResourceId();
            projectCostService.getResources(resourceTypes.getSelectedItem().getId(), resourcePools.getSelectedItem().getId(), new AbstractAsyncCallback<ProjectCostSelectItem[]>() {
                public void success(ProjectCostSelectItem[] items) {
                    LoadingPanel.loading(false);
                    resources.addItems(items);
                    if (resourceId != null) {
                        resources.setSelected(resourceId);
                        ProjectCostSelectItem resourceItem = (ProjectCostSelectItem) resources.getData(resources.getSelectedId());
                        if (resourceItem.getRate() != null && resourceItem.getRate() != 0 && (rate == null || rate == 0)) {
                            plannedRate.setText(format.format(resourceItem.getRate()));
                        }

                    }
                }
            });

            plannedQuantity.setText(result.getActualQuantity() + "");
            if (result.getActualRate() != null && result.getActualRate() != 0) {
                plannedRate.setText(result.getActualRate() + "");
            } else {
                ProjectCostSelectItem item = (ProjectCostSelectItem) resourcePools.getData(resourcePools.getSelectedId());
                plannedRate.setText(format.format(item.getRate()));
            }
            plannedAmount.setHTML("<b>" + format.format(result.getActualCost()) + "</b>");
            if (result.isPlanned()) {
                resourcePools.setEnabled(false);
                resources.setEnabled(false);
            }
        }

        widgets[0] = resourcePools;
        widgets[1] = resources;
        widgets[2] = plannedQuantity;
        widgets[3] = plannedRate;
        widgets[4] = plannedAmount;

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

        tableLeft = new WfmForm(new String[]{"20%", "40%", "39%"});
        tableLeft.addField(null, new Label(""));
        tableLeft.setStyleName("padding10");

        tableRight = new WfmForm(new String[]{"20%", "40%", "39%"});
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

        date = new DatePicker();
        date.addStyleName(DEFAULT_WIDTH);

        projects.addValueChangeHandler(widget -> {
            reinit();
            projectChange();
            isAllFieldSelect();
        });

        tasks.addValueChangeHandler(event -> {
            reinit();
            isAllFieldSelect();
        });

        resourceTypes.addValueChangeHandler(event -> {
            reinit();
            resourceTypeChange();
            isAllFieldSelect();
        });
        date.addChangeHandler(event -> isAllFieldSelect());

        projectCostService.getResourceTypes(new AbstractAsyncCallback<SelectItem[]>() {
            public void success(SelectItem[] result) {
                resourceTypes.setItems(result);
            }
        });

        tableLeft.setCellPadding(7);

        if(Utils.hasPermission(PermissionConstants.PM_PROJECT_ADD)){
            projectField = tableLeft.addField(Property.get(Constants.PROJECT, wfmStrings.projectField()), new Widget[]{projects, addNewProject}, true, 5);
        }else{
            projectField = tableLeft.addField(Property.get(Constants.PROJECT, wfmStrings.projectField()), projects, true);
        }

        taskField = tableLeft.addField(wfmStrings.task(), tasks, true, wfmStrings.pleaseSelectTask());

        resourceTypeField = tableRight.addField("Resource Type", resourceTypes, true, projectStrings.pleaseSelectResourceType());
        tableRight.addField("Date", date, true);

        tableLeft.addTitleField("<br/>Employee Resources");

        innerSettingsPanel.add(tableLeft);
        innerSettingsPanel.add(tableRight);
        contentPanel.add(innerSettingsPanel);

        chargeTypeItems = new SelectItem[2];
        chargeTypeItems[0] = new SelectItem(1, "percentage");
        chargeTypeItems[1] = new SelectItem(2, "fixed amount");
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
     * Project Cost Item Fileds
     */
    private void isAllFieldSelect() {
        int selectedCount = 0;
        if (tasks.getSelectedItem() == null) {
            selectedCount++;
        }
        if (date.getDate() == null) {
            selectedCount++;
        }
        if (projects.getSelectedItem() == null) {
            selectedCount++;
        }
        if (resourceTypes.getSelectedItem() == null) {
            selectedCount++;
        }
        if (selectedCount == 0 && from && to) {
            to = false;
            from = false;
            ProjectCostAllDataItem item = new ProjectCostAllDataItem();
            item.setTaskId(tasks.getSelectedItem().getId());
            item.setProjectId(projectId);
            item.setResourceTypeId(resourceTypeId);
            item.setFrom(date.getDate());
            item.setEstemitedCost(false);
            LoadingPanel.loading(true);
            projectCostService.getProjectActualCostItems(item, new AbstractAsyncCallback<ProjectCostAllDataItem>() {
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                    from = true;
                    to = true;
                }

                public void success(ProjectCostAllDataItem allDataItem) {
                    from = true;
                    to = true;
                    // draw resource dynamic table
                    employeeDynamicTable.clear();
                    employeeAmount.setHTML("");
                    float planned = 0.0f;
                    ProjectCostItem[] resources = allDataItem.getResources();
                    for (int i = 0; i < resources.length; i++) {
                        planned += resources[i].getActualCost();
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
        }
    }

    private void calculatonPlannedTotal() {
        float sum = 0;
        for (int rowId = 0; rowId < employeeDynamicTable.getRowNumber(); rowId++) {
            DynamicTableItem tableItem = employeeDynamicTable.getItem(rowId);
            HTML plnAmount = (HTML) tableItem.getColumnById("plnAmount");
            if (plnAmount.getText() != null && !"".equals(plnAmount.getText().trim())) {
                sum += NumberFormat.getScientificFormat().parse(plnAmount.getText());
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
            if (resourcePoolItems != null && resourceTypeId != null && projectId != null && tasks.getSelectedIndex() != 0 && date.getDate() != null) {
                Widget[] widgets = getEmployeeResourceWidgetArray(null);
                employeeDynamicTable.addRow(widgets);
            } else {
                if (resourceTypeId != null && projectId != null && tasks.getSelectedIndex() != 0 && date.getDate() != null) {
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
        DynamicTableColumn[] columns = new DynamicTableColumn[COLUMNS_COUNT - 1];
        columns[0] = new DynamicTableColumn(projectStrings.resourcePool(), "resourcePool", new ColumnStatements("", projectStrings.pleaseSelectResourcePool()), 180);
        columns[1] = new DynamicTableColumn(projectStrings.resource(), "resource", new ColumnStatements("", "Please select resource"), 180);
        columns[2] = new DynamicTableColumn("Acl qty/hrs", "plnQuantity", new ColumnStatements("", ""), 100, true);
        columns[3] = new DynamicTableColumn("Acl rate", "plnRate", new ColumnStatements("", ""), 100, true);
        columns[4] = new DynamicTableColumn(projectStrings.actualAmount(), "plnAmount", new ColumnStatements("", ""), 130);
        return columns;
    }

    /**
     * Draw other over head Column title
     */
    private DynamicTableColumn[] getOtherOverHeadColumnArray() {
        DynamicTableColumn[] columns = new DynamicTableColumn[COLUMNS_COUNT - 1];
        columns[0] = new DynamicTableColumn(projectStrings.costItemName(), "costItemName", new ColumnStatements("", projectStrings.pleaseSelectCostItemName()), 180);
        columns[1] = new DynamicTableColumn(projectStrings.chargeType(), "chargeType", new ColumnStatements("", projectStrings.pleaseSelectChargeType()), 180);
        columns[2] = new DynamicTableColumn("%", "percent", new ColumnStatements("", ""), 100, true);
        columns[3] = new DynamicTableColumn(wfmStrings.amount(), "amounty", new ColumnStatements("", ""), 100, true);
        columns[4] = new DynamicTableColumn(projectStrings.actualAmount(), "plnAmount", new ColumnStatements("", ""), 130);
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

        if (date.getDate() == null || wfmStrings.pleaseSelect().equalsIgnoreCase(date.getText())) {
            errors++;
            Info.show(wfmStrings.pleaseSelectDate(), Info.Type.WARNING);
            return false;
        }

        employeeDynamicTable.resetValidation();
        costAllDataItem = new ProjectCostAllDataItem(projectId, tasks.getSelectedId(), resourceTypeId, date.getDate());
        ProjectCostItem[] employeeResourceItem = new ProjectCostItem[employeeDynamicTable.getRowNumber()];
        for (int rowId = 0; rowId < employeeDynamicTable.getRowNumber(); rowId++) {

            DynamicTableItem tableItem = employeeDynamicTable.getItem(rowId);
            WfmDropdown resourcePool = (WfmDropdown) tableItem.getColumnById("resourcePool");
            WfmDropdown resource = (WfmDropdown) tableItem.getColumnById("resource");
            TextBox plannedQuantity = (TextBox) tableItem.getColumnById("plnQuantity");
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
                employeeResourceItem[rowId].setActualQuantity((float) NumberFormat.getScientificFormat().parse(plannedQuantity.getText()));
            }
            if (plannedRate.getText() != null && !"".equals(plannedRate.getText().trim())) {
                employeeResourceItem[rowId].setActualRate((float) NumberFormat.getScientificFormat().parse(plannedRate.getText()));
            }
            if (plnAmount.getText() != null && !"".equals(plnAmount.getText().trim())) {
                employeeResourceItem[rowId].setActualCost((float) NumberFormat.getScientificFormat().parse(plnAmount.getText()));
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
        }
    }

    private void saveCostSheet() {
        if (validate()) {
            LoadingPanel.loading(true);
            costAllDataItem.setEstemitedCost(false);
            projectCostService.saveActualCostSheet(costAllDataItem, new AbstractAsyncCallback<Void>() {
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                public void success(Void result) {
                    LoadingPanel.loading(false);
                    Info.show("Save successfull", Info.Type.INFO);
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

    public String getIconStyle() {
        return null;
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
