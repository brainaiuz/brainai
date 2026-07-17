package com.edatasite.workforce.gwt.invoice.client.ui.view.projectbasedinvoice;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.GroupByAssigneeEntry;
import com.edatasite.workforce.gwt.accounting.client.rpc.GroupByProjectEntry;
import com.edatasite.workforce.gwt.accounting.client.rpc.GroupByTaskEntry;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProjectEmployeeStruct;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProjectEmployeeTaskStruct;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProjectEmployeeValue;
import com.edatasite.workforce.gwt.accounting.client.rpc.TimeSpentRateValue;
import com.edatasite.workforce.gwt.accounting.client.rpc.TotalCostHours;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.TaxLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.ExtendedHTML;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProjectBaseData;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.ProjectBaseInvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.ProjectBaseInvoiceServiceAsync;
import com.edatasite.workforce.gwt.invoice.client.ui.view.CustomCellTextArea;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.Discount;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.ItemQtyPanel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.ProductDescriptionTextArea;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.ProductsTable;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.UnitPrice;
import com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice.SalesInvoiceView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 13.05.2009
 * Time: 12:47:37
 */
public class ProjectBasedInvoiceAddView extends FooteredView implements Constants, AccountingConstants, Colapse, FittedContent {

    private final ProjectBaseInvoiceServiceAsync pbInvoiceService = ProjectBaseInvoiceService.App.get();
    private static final AllInOneServiceAsync allInOneService = AllInOneService.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private SalesInvoiceView salesInvoiceView;
    private ProductsTable productsTable;
    private Integer crmAccountID;
    private WfmButton2 continueButton;

    private ProjectBasedInvoice projectBasedInvoice;

    public ProjectBasedInvoiceAddView() {
        super("projectBaseInvoiceadd", accountingStrings.addProjectBasedInvoice());
    }

    public ProjectBasedInvoiceAddView(String relationId) {
        super("projectBaseInvoiceadd", accountingStrings.addProjectBasedInvoice());

        if (relationId != null && !"null".equals(relationId)) {
            crmAccountID = Integer.parseInt(relationId);
        }
    }

    protected Widget onInitialize() {
        if (Utils.isEnableBonnardCustomization()) {
            projectBasedInvoice = new BonnardProjectBasedInvoice();
        } else {
            projectBasedInvoice = new ProjectBasedInvoice();
        }

        salesInvoiceView = new SalesInvoiceView(true);

        continueButton = new WfmButton2(accountingStrings.getPropertyContinue(), BTN_DEFAULT_OUTLINE);
        continueButton.addClickHandler(ch -> {
            if (!projectBasedInvoice.validate()) {
                return;
            }


            clear();

            LoadingPanel.loading(true);

            initializeData();
        });

        add(projectBasedInvoice);
        add(createFooter());

        return null;
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return ProjectBasedInvoiceAddView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return ProjectBasedInvoiceAddView.this.getFooterRightSideWidgets();
            }
        });
    }

    private List<Widget> getFooterLeftSideWidgets() {
        return null;
    }

    private List<Widget> getFooterRightSideWidgets() {
        List<Widget> result = new ArrayList<>();

        Div continueWrapper = new Div();

        continueWrapper.add(continueButton);

        result.add(continueWrapper);
        return result;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    private void getRelationName(final Integer relationID, final String relType) {
        allInOneService.getRelationName(relationID, relType, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(String result) {
//                if (result != null) {
//                    relationName = result;
//                }
//                if (crmAccountID != null) {
//                    lookUp.addItem(new SelectItem(crmAccountID, relationName));
//                    itemSelected();
//                }
            }
        });
    }

    private void initializeData() {
        salesInvoiceView.asyncOnInitialize(new AbstractAsyncCallback<Widget>() {
            public void failure(Throwable reason) {

            }

            public void success(Widget result) {
                salesInvoiceView.setOnProjectBaseInvoiceInit(() -> {
                    salesInvoiceView.setProjectIDs(projectBasedInvoice.getProjectIds());

                    Date dateTo = projectBasedInvoice.getToDate();
                    dateTo.setHours(23);
                    dateTo.setMinutes(59);
                    dateTo.setSeconds(59);
                    DateNonConvertable from = new DateNonConvertable(projectBasedInvoice.getFromDate());
                    DateNonConvertable to = new DateNonConvertable(dateTo);

                    switch (projectBasedInvoice.getInvoiceType()) {
                        case ProjectBasedInvoice.DETAILED_INVOICE:
                            initDetailedInvoice(from, to);
                            break;
                        case ProjectBasedInvoice.GROUPED_BY_ASSIGNEE:
                            initGrouppedByAssignee(from, to);
                            break;
                        case ProjectBasedInvoice.GROUPED_BY_TASK:
                            initGrouppedByTask(from, to);
                            break;
                        case ProjectBasedInvoice.GROUPED_BY_PROJECT:
                            initGrouppedByProject(from, to);
                            break;
                    }
                });

            }
        });
    }

    private void initDetailedInvoice(DateNonConvertable from, DateNonConvertable to) {
        pbInvoiceService.getDetailedInvoice(projectBasedInvoice.getProjectIds(), from, to, Boolean.parseBoolean(Utils.userSettings.get(MONTHLY_TIMESHEET)), projectBasedInvoice.getCustomerId(), new AbstractAsyncCallback<ProjectBaseData[]>() {
            public void failure(Throwable caught) {

            }

            public void success(ProjectBaseData[] result) {
                drawInvoiceView(result);
            }
        });
    }

    private void initGrouppedByAssignee(DateNonConvertable from, DateNonConvertable to) {
        pbInvoiceService.getGroupedByAssignee(projectBasedInvoice.getProjectIds(), from, to, Boolean.parseBoolean(Utils.userSettings.get(MONTHLY_TIMESHEET)), new AbstractAsyncCallback<ArrayList<GroupByAssigneeEntry>>() {
            public void failure(Throwable caught) {

            }

            public void success(ArrayList<GroupByAssigneeEntry> result) {
                ProjectBaseData[] data = new ProjectBaseData[result.size()];
                int i = 0;
                for (Object aMap : result) {
                    GroupByAssigneeEntry entry = (GroupByAssigneeEntry) aMap;
                    ProjectEmployeeStruct keyStruct = entry.getKey();
                    ProjectEmployeeValue value = entry.getValue();
                    SelectItem task = value.getTasks().get(0);
                    data[i] = new ProjectBaseData();
                    data[i].setEmployeeName(keyStruct.getEmployee().getName());
                    data[i].setProjectName(keyStruct.getProject().getName());
                    data[i].setTimespent(value.getHourSpent());
                    data[i].setTimesheetEntryIdList(value.getEntryIds());
                    data[i].setTimesheetDescription(value.getTimesheetDescription());
                    data[i].setClientChargeRate(value.getClientChargeRate());
                    data[i++].setTaskName(task.getName());
                }

                drawInvoiceView(data);
            }
        });
    }

    private void initGrouppedByTask(DateNonConvertable from, DateNonConvertable to) {
        pbInvoiceService.getGroupedByTask(projectBasedInvoice.getProjectIds(), from, to, Boolean.parseBoolean(Utils.userSettings.get(MONTHLY_TIMESHEET)), new AbstractAsyncCallback<ArrayList<GroupByTaskEntry>>() {
            public void failure(Throwable caught) {

            }

            public void success(ArrayList<GroupByTaskEntry> map) {
                ProjectBaseData[] data = new ProjectBaseData[map.size()];
                int i = 0;
                for (GroupByTaskEntry entry : map) {
                    ProjectEmployeeTaskStruct keyStruct = entry.getKey();
                    TimeSpentRateValue value = entry.getValue();
                    data[i] = new ProjectBaseData();
                    data[i].setEmployeeName(keyStruct.getEmployee().getName());
                    data[i].setProjectName(keyStruct.getProject().getName());
                    data[i].setTaskName(keyStruct.getTask().getName());
                    data[i].setTimespent(value.getTimeSpent());
                    data[i].setTimesheetEntryIdList(value.getEntryIds());
                    data[i].setTimesheetDescription(value.getTimesheetDescription());
                    data[i++].setClientChargeRate(value.getClientChargeRate());
                }

                drawInvoiceView(data);
            }
        });
    }

    private void initGrouppedByProject(DateNonConvertable from, DateNonConvertable to) {
        pbInvoiceService.getGroupedByProject(projectBasedInvoice.getProjectIds(), from, to, Boolean.parseBoolean(Utils.userSettings.get(MONTHLY_TIMESHEET)), new AbstractAsyncCallback<ArrayList<GroupByProjectEntry>>() {
            public void failure(Throwable caught) {

            }

            public void success(ArrayList<GroupByProjectEntry> map) {
                ProjectBaseData[] data = new ProjectBaseData[map.size()];
                int i = 0;

                for (GroupByProjectEntry entry : map) {
                    SelectItem key = entry.getKey();
                    TotalCostHours value = entry.getValue();
                    data[i] = new ProjectBaseData();
                    data[i].setProjectName(key.getName());
                    data[i].setProjectDescription(key.getDescription());
                    Double timespent = value.getTotalHours() != null ? value.getTotalHours() * 60 : 0d;
                    data[i].setTimespent(timespent.intValue());
                    data[i].setTimesheetEntryIdList(value.getEntryIds());
                    Double rate = (value.getTotalCost() != null && value.getTotalHours() != null && value.getTotalHours().compareTo(0d) != 0) ? value.getTotalCost().doubleValue() / value.getTotalHours().doubleValue() : 0;
                    data[i++].setClientChargeRate(rate);
                }

                drawInvoiceView(data);
            }
        });
    }

    private void drawInvoiceView(ProjectBaseData[] data) {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TIMESHEET_INVOICE_ADDED, ProjectBasedInvoiceAddView.this, (sender, args) -> this.closeTab());

        InvoiceService.App.get().getClientOrSupplier(projectBasedInvoice.getCustomerId(), Constants.RECEIVABLE, new AbstractAsyncCallback<TypeItem>() {
            public void failure(Throwable throwable) {

            }

            public void success(TypeItem typeItem) {
                SelectItem relatedProject = null;
                if (projectBasedInvoice.getProjectIds() != null && projectBasedInvoice.getProjectIds().length == 1) {
                    relatedProject = projectBasedInvoice.getProjectItemMap().get(projectBasedInvoice.getProjectIds()[0]);
                }

                salesInvoiceView.getFormPresenter().applyTypeItemData(typeItem, true, true, relatedProject, true);
                salesInvoiceView.getFormPresenter().setProjectBasedInvoiceStartDate(projectBasedInvoice.getFromDate());
                salesInvoiceView.getFormPresenter().setProjectBasedInvoiceEndDate(projectBasedInvoice.getToDate());
                salesInvoiceView.getFormPresenter().onChangeClientHandler(false);
            }
        });

        productsTable = salesInvoiceView.getProductsTable();
        salesInvoiceView.setPeriod(projectBasedInvoice.getFromDate(), projectBasedInvoice.getToDate());
        fillItemsTable(data);
        add(salesInvoiceView);

        LoadingPanel.loading(false);
    }

    private void fillItemsTable(ProjectBaseData[] data) {
        productsTable.getItemsTable().removeAllRows();
        for (ProjectBaseData aData : data) {
            productsTable.getItemsTable().addRow(initWidgets(aData));
        }

        int length = data.length;

        while (length < ProductsTable.DEFAULT_ROWS) {
            productsTable.getItemsTable().addRow(initWidgets(new ProjectBaseData()));
            length++;
        }
    }

    private Widget[] initWidgets(ProjectBaseData data) {
        LinkedHashMap<String, Widget> widgetsMap = new LinkedHashMap<>();

        for (final String key : productsTable.getColumnsMap().keySet()) {
            switch (key) {
                case ProductsTable.PRODUCT:
                    final CustomCellTextArea product = new CustomCellTextArea();
                    product.setText(projectBasedInvoice.generateName(data));
                    if (data.getTimesheetEntryIdList() != null) {
                        product.setEntryIds(data.getTimesheetEntryIdList());
                    } else if (data.getTimesheetEntryId() != null) {
                        Integer[] entryIds = new Integer[1];
                        entryIds[0] = data.getTimesheetEntryId();
                        product.setEntryIds(entryIds);
                    }
                    widgetsMap.put(ProductsTable.PRODUCT, product);
                    break;
                case ProductsTable.DESCRIPTION:
                    ProductDescriptionTextArea description = productsTable.getDescriptionTextAreaInstance();
                    String generatedDescription = projectBasedInvoice.generateDescription(data);
                    description.setText(generatedDescription);
                    description.hideCharacterLimitPanel();
                    widgetsMap.put(ProductsTable.DESCRIPTION, description);
                    break;
                case ProductsTable.QTY:
                    ItemQtyPanel timeSpent = productsTable.getQtyPanelInstance(widgetsMap);
                    timeSpent.setText(data.getTimeSpentInHours());
                    timeSpent.setFromTimesheet(data.getTimespent() != null && data.getTimespent() > 0);
                    widgetsMap.put(ProductsTable.QTY, timeSpent);
                    break;
                case ProductsTable.UNITPRICE:
                    UnitPrice clientChargeRate = productsTable.getUnitPriceInstance();
                    BigDecimal value = ZERO;

                    if (data.getClientChargeRate() != null) {
                        if (data.isFixed() && data.getTimespent() != null && data.getTimespent() > 0) {
                            value = new BigDecimal(data.getClientChargeRate()).divide(new BigDecimal(data.getTimespentInHours()), 5).setScale(4, BigDecimal.ROUND_HALF_UP);
                        } else {
                            value = new BigDecimal(data.getClientChargeRate()).setScale(2, BigDecimal.ROUND_HALF_UP);
                        }
                    }
                    clientChargeRate.setValuableText(AccountingUtils.get().formatUnitPrice(value), value);
                    clientChargeRate.setValueInBaseCurrency(value);
                    clientChargeRate.setIgnoreMultiPrice(data.isIgnoreExRate());
                    Validation.checkToFocusTextBox(clientChargeRate, AccountingUtils.getUnitPriceZero());
                    widgetsMap.put(ProductsTable.UNITPRICE, clientChargeRate);
                    break;
                case ProductsTable.DISCOUNT_AMT:
                    Discount discountPanel = productsTable.getDiscountInstance();
                    discountPanel.setOnDiscountChange(() -> {
                    });
                    if (data.getDiscount() != null && data.getDiscount() > 0) {
                        discountPanel.setValueText(String.valueOf(data.getDiscount()), new BigDecimal(data.getDiscount()));
                    }
                    widgetsMap.put(ProductsTable.DISCOUNT_AMT, discountPanel);
                    break;
                case ProductsTable.ACCOUNT:
                    AccountsLookUp accountsList = new AccountsLookUp(RECEIVABLE);
                    if (productsTable.getDefaultAccount() != null) {
                        accountsList.setSelected(productsTable.getDefaultAccount());
                    }
                    widgetsMap.put(ProductsTable.ACCOUNT, accountsList);
                    break;
                case ProductsTable.NET_AMT:
                    ExtendedHTML netAmount = productsTable.getZeroAsHTML();
                    widgetsMap.put(ProductsTable.NET_AMT, netAmount);
                    break;
                case ProductsTable.TAX_LIST:
                    TaxLookUp taxList = new TaxLookUp(RECEIVABLE);
                    taxList.getSuggestBox().addSelectionHandler(e -> productsTable.reDrawTaxesDropdown());
                    taxList.getSuggestBox().addKeyUpHandler(e -> productsTable.reDrawTaxesDropdown());

                    if (projectBasedInvoice.getTaxItem() != null) {
                        taxList.addItem(projectBasedInvoice.getTaxItem());
                        taxList.map.put(projectBasedInvoice.getTaxItem().getId(), projectBasedInvoice.getTaxItem());
                    }
                    widgetsMap.put(ProductsTable.TAX_LIST, taxList);
                    break;
            }
        }

        if (Utils.isProjectInLineItemEnable()) {
            ProjectLookUp projectLookUp = new ProjectLookUp(RECEIVABLE, null);
            projectLookUp.getSuggestBox().setWidth("110px");
            widgetsMap.put(PROJECT, projectLookUp);

            if (data.getProjectId() != null) {
                projectLookUp.addItem(new SelectItem(data.getProjectId(), data.getProjectName()));
                projectLookUp.setSelected(new SelectItem(data.getProjectId(), data.getProjectName()));
            }
        }
        return widgetsMap.values().toArray(new Widget[]{});
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
