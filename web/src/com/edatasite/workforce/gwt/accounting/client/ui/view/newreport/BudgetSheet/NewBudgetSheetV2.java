package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.BudgetSheet;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountBudget;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountItemWithBudgetDate;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountItemsByAccountType;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.rpc.BudgetInDate;
import com.edatasite.workforce.gwt.accounting.client.rpc.BudgetManagerItems;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.SectionBoxPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listTable.ImportFilePopUp;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import gwt.material.design.addins.client.menubar.MaterialMenuBar;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NewBudgetSheetV2 extends Composite implements AccountingConstants, Constants {
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final NewBudgetSheetUiBinder ourUiBinder = GWT.create(NewBudgetSheetUiBinder.class);
    private final AccountingServiceAsync accountingService = AccountingService.App.get();
    private final DateTimeFormat dateFormat = DateTimeFormat.getFormat("LLLL yyyy");

    @UiField
    SectionBoxPanel headerPanel;
    @UiField
    HTMLPanel exportPanel;
    @UiField
    DivElement budgetSheetTable;
    private Date currentDate = new Date();
    private Date startDate;
    private HashMap<Integer, Date> columnDates;
    private LinkedList<BudgetColumn> columns;
    private final DatePicker startDatePicker;
    private final DataListBox periodListBox;
    private final DataListBox budgetManager;
    private int columnCountPeriod;
    private boolean isAsc = true;
    private final WfmButton2 updateButton;
    private LinkedHashMap<Date, HashMap<String, BudgetInDate>> totalVerticalPlan;
    private Map<String, BudgetInDate> totalHorizontalPlan;
    private Integer scale;
    private LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> filterData;
    private MaterialMenuBar showMenuBar;
    private final ActionButton filterBtn;

    public NewBudgetSheetV2() {

        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);

        GBox groupBox = headerPanel.drawNewGroupBox();
        groupBox.setStyleWidthFree(true);
        groupBox.setStyleUnited(true);

        startDatePicker = new DatePicker();
        startDatePicker.setDate(new Date());
        startDatePicker.addChangeHandler(changeEvent -> {
            update();
        });
//        startDateListBox = new DataListBox(false);
//        startDateListBox.ensureDebugId("budgetManager-startListBox");
//        startDateListBox.addValueChangeHandler(change -> {
//            if (startDateListBox.getSelectedItem() != null && startDateListBox.getSelectedItem().getSelectedId() != null) {
//                update();
//            }
//        });
//        DateTimeFormat format = DateTimeFormat.getFormat("LLLL-yyyy");
//        Date startDateNew = new Date();
//        startDateNew.setYear(startDateNew.getYear() - 1);
//        startDateNew.setMonth(0);
//        for (int i = 0; i < 24; i++) {
//            SelectItem item = new SelectItem(Integer.valueOf(clarifyID(startDateNew)), format.format(startDateNew));
//            startDateListBox.addListItem(item);
//            startDateMap.put(item.getId(), startDateNew);
//            startDateNew = DateUtil.addMonths(startDateNew, 1, 1);
//        }


        ActionButton addButton = new ActionButton("", "btn btn--new btn--circle");
        addButton.add(new SvgIcon(SvgEnum.plus));
        addButton.addClickHandler(click -> {
            new AddNewBudgetSheet();
        });

        GBoxItem addButtonButtonItem = headerPanel.addGroupBoxItem(null, addButton);
        addButtonButtonItem.addStyleToComponent("paging-group__wrapper");
        addButtonButtonItem.setStyleSplitRight(true);


        headerPanel.addGroupBoxItem(wfmStrings.start(), startDatePicker);

        DataListBox periodTypeListBox = new DataListBox(false);
        periodTypeListBox.setWithoutNullLabel(true);
        periodTypeListBox.ensureDebugId("budgetManager-periodListBox");

        periodTypeListBox.addListItem(new SelectItem(1, wfmStrings.month()));
        periodTypeListBox.setSelected(1);
        headerPanel.addGroupBoxItem(wfmStrings.periodType(), periodTypeListBox);

        periodListBox = new DataListBox(false);
        periodListBox.setWithoutNullLabel(true);
        periodListBox.ensureDebugId("budgetManager-periodListBox");

        periodListBox.addListItem(new SelectItem(1, 1 + " " + wfmStrings.month()));
        periodListBox.addListItem(new SelectItem(3, 3 + " " + wfmStrings.months()));
        periodListBox.addListItem(new SelectItem(4, 4 + " " + wfmStrings.months()));
        periodListBox.addListItem(new SelectItem(6, 6 + " " + wfmStrings.months()));
        periodListBox.addListItem(new SelectItem(12, 12 + " " + wfmStrings.months()));
        periodListBox.setSelected(1);
        headerPanel.addGroupBoxItem(wfmStrings.period(), periodListBox);
        periodListBox.addValueChangeHandler(change -> {
            update();
        });

        budgetManager = new DataListBox(false);
        budgetManager.ensureDebugId("budgetManager-periodListBox");
        budgetManager.setWithoutNullLabel(true);
        budgetManager.addValueChangeHandler(change -> {
            if (budgetManager.getSelectedItem() != null) {
                scale = budgetManager.getSelectedItem().getOrderId() != null ? budgetManager.getSelectedItem().getOrderId() : 0;
            }
            update();
        });
        GBoxItem typeBoxItem = headerPanel.addGroupBoxItem(wfmStrings.name(), budgetManager);
        typeBoxItem.setStyleSplitRight(true);


        filterBtn = new ActionButton("", "btn btn--icon btn--white");
        filterBtn.ensureDebugId("filter_button");
        Icon filterIcon = new Icon();
        filterIcon.addStyleName("ficon--filter");
        filterBtn.add(filterIcon);
        filterBtn.setVisible(false);

        filterBtn.addClickHandler(click -> {
            if (budgetManager.getSelectedItem() != null && (filterData == null || filterData != null && filterData.isEmpty())) {
                LoadingPanel.loading(true);
                ListingFilterParameter filterParameter = new ListingFilterParameter();
                filterParameter.setEntityID(budgetManager.getSelectedItem().getId());
                AllInOneService.App.get().getAssignesByType(filterParameter, budgetManager.getSelectedItem().getDescription(), new AbstractAsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>>() {
                    @Override
                    public void failure(Throwable throwable) {
                        super.failure(throwable);
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void success(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> result) {
                        super.success(result);
                        filterData = result;
                        LoadingPanel.loading(false);
                        new AssignItemPopUp(budgetManager.getSelectedItem(), filterData);
                    }
                });
            } else {
                new AssignItemPopUp(budgetManager.getSelectedItem(), filterData);
            }
        });
        GBoxItem filterButtonItem = headerPanel.addGroupBoxItem(null, filterBtn);
        filterButtonItem.addStyleToComponent("paging-group__wrapper");
        filterButtonItem.setStyleSplitRight(true);

        updateButton = new WfmButton2(wfmStrings.update(), WfmButton2.BTN_PRIMARY);
        updateButton.addClickHandler(event -> update());
        GBoxItem updateItem = headerPanel.addGroupBoxItem(null, updateButton);
        updateItem.setStyleSplitRight(true);

        headerPanel.addGroupBoxItem(exportSection());


        ActionButton customizeButton = new ActionButton("", "btn btn--icon");
        customizeButton.add(new SvgIcon(SvgEnum.sliders));
        customizeButton.addClickHandler(click -> {
            new AddNewBudgetSheet(budgetManager.getSelectedItem());
        });

        GBoxItem customizeButtonItem = headerPanel.addGroupBoxItem(null, customizeButton);
        customizeButtonItem.addStyleToComponent("paging-group__wrapper");
        customizeButtonItem.getComponent().getElement().setAttribute("style", "position: absolute;");
        customizeButtonItem.setStyleSplitRight(true);
        customizeButtonItem.addStyleName("ml-auto");

        getBudgetManagers();

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BUDGET_SHEET_UPDATE, NewBudgetSheetV2.this, (sender, args) -> update());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CHANGE_BUDGET_MANAGERS, NewBudgetSheetV2.this, (sender, args) -> getBudgetManagers());
    }

    private GBoxItem exportSection() {
        showMenuBar = new MaterialMenuBar();
        showMenuBar.setVisible(false);
        showMenuBar.setClass("dropdown-kit--arrow--below");

        MaterialLink showLink = new MaterialLink();
        showLink.addStyleName("btn btn--white btn--icon");

        Icon ieIcon = new Icon();
        ieIcon.setClass("ficon--download-cloud");
        showLink.add(ieIcon);

        MaterialDropDown showMenuContainer = new MaterialDropDown(showLink);
        showMenuContainer.setClass("dropdown-content--2 dropdown-content--export");
        showMenuContainer.setBelowOrigin(true);
        showLink.add(showMenuContainer);


        MaterialLink importExport = new MaterialLink();
        Icon exlIcon = new Icon();
        exlIcon.setClass("ficon--import");
        importExport.add(exlIcon);
        importExport.setText(wfmStrings.importString());

        MaterialDropDown mdp = new MaterialDropDown(importExport);
        mdp.setHover(true);
        mdp.setHoverable(true);

        importExport.addClickHandler(ch -> {

            ImportFilePopUp imp = new ImportFilePopUp(ImportTypeEnum.BUDGET_MANAGER, null);
            imp.open();
            imp.setSubmitCompleted(() -> {
                Integer budgetId = budgetManager.getSelectedItem() != null ? budgetManager.getSelectedItem().getId() : null;
                SinksContainerFactory.entryPoint.onHistoryChanged("importbudgetmanager|add/add/" + imp.getObjectId() + "/" + budgetId);
            });
        });
        showMenuContainer.add(importExport);
        showMenuBar.add(showLink);

        Div div = new Div();
        new KpiToolTip(importExport, wfmStrings.importString(), Position.TOP);
        div.add(showMenuBar);
        GBoxItem excelItem = headerPanel.addGroupBoxItem(null, div);
        return excelItem;
    }

    private void getBudgetManagers() {
        columns = null;
        accountingService.getBudgetManagerData(null, new AbstractAsyncCallback<BudgetManagerItem>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(BudgetManagerItem result) {
                super.success(result);
                if (result != null) {
                    budgetManager.setItems(result.getBudgetManagers());
                    if (result.getColumns() != null) {
                        columns = result.getColumns();
                    }
                    if (result.getDefaultBudgetManager() != null) {
                        budgetManager.setSelected(result.getDefaultBudgetManager());
                        scale = result.getDefaultBudgetManager().getOrderId() != null ? result.getDefaultBudgetManager().getOrderId() : 0;
                        showMenuBar.setVisible(true);
                        filterBtn.setVisible(true);
                    }
                    update();
                }
            }
        });
    }

    public static native void frameAffix() /*-{
        $wnd.table__frame_affix_init();
    }-*/;


    private void update() {
        columnDates = new HashMap<>();

        currentDate = DateUtil.getMonthFirstDay(startDatePicker.getDate());
        startDate = DateUtil.addMonths(currentDate, 0, 1);
        Date endDate = DateUtil.addMonths(currentDate, periodListBox.getSelectedId(), 1);
        //We are adding 1, because besides dates we have the name of the item too.
        columnCountPeriod = periodListBox.getSelectedId() == 1 ? 2 : periodListBox.getSelectedId() + 2;// 1 for name, 1 for total column

        LoadingPanel.loading(true);
        accountingService.getBudgetedDataItem(budgetManager.getSelectedId(), new DateNonConvertable(startDate), new DateNonConvertable(DateUtil.getDayLastTime(endDate)), isAsc, new AsyncCallback<BudgetManagerItems>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(BudgetManagerItems result) {
                if (result != null && result.getBudgetManagerDetail() != null) {
                    columns = result.getBudgetManagerDetail().getColumns();
                }
                drawBudgetsheetTable(result);
                Utils.table__frame_affix_init();
                LoadingPanel.loading(false);
                frameAffix();

            }
        });
    }

    public void refresh(int ascOrDesc, String s) {
        isAsc = ASC == ascOrDesc;
        update();
    }

    private void drawBudgetsheetTable(BudgetManagerItems data) {

        clearElementChild(budgetSheetTable);
        Element table = DOM.createTable();
        table.addClassName("table tm");
        table.setAttribute("cellspacing", "0");
        table.setAttribute("cellpadding", "0");
        Element header = createTH();
        table.appendChild(header);
        totalVerticalPlan = new LinkedHashMap<>();
        if (data != null && data.getItems() != null && data.getItems().size() > 0) {
            for (AccountItemsByAccountType accountItemsByAccountType : data.getItems()) {
                collectTreeData(table, accountItemsByAccountType, true);
            }
        }
        Element tbody = DOM.createTBody();
        tbody.addClassName("category_set");
        table.appendChild(tbody);
        createTotalRow(tbody);
        budgetSheetTable.appendChild(table);
    }

    private void createTotalRow(Element element) {
        Element tr = DOM.createTR();
        tr.addClassName("total_row");
        HashMap<String, BudgetInDate> totalHorVer = new HashMap<>();
        for (int i = 0; i < columnCountPeriod; i++) {
            if (i == 0) {
                Element td = DOM.createTD();
                td.setInnerHTML("<b>" + wfmStrings.total() + "</b>");
                td.addClassName("tm__cat-cell");
                tr.appendChild(td);
            } else {
                Element td = DOM.createTD();
                td.addClassName("tm__td-month");
                tr.appendChild(td);
                Element table = DOM.createTable();
                table.setAttribute("cellspacing", "0");
                table.setAttribute("cellpadding", "0");
                td.appendChild(table);
                Element tbody = DOM.createTBody();
                table.appendChild(tbody);
                Element row = DOM.createTR();
                tbody.appendChild(row);

                if (columns != null) {
                    for (BudgetColumn budgetColumn : columns) {
                        Element tdChild = DOM.createTD();
                        tdChild.addClassName("tm-child");


                        if (i == columnCountPeriod - 1 && columnCountPeriod != 2) {
                            tdChild.setInnerHTML("<b>" + getValueAsString(totalHorVer.get(budgetColumn.getCode()) != null ? totalHorVer.get(budgetColumn.getCode()).getValue() : BigDecimal.ZERO) + "</b>");
                            row.appendChild(tdChild);
                        } else {

                            HashMap<String, BudgetInDate> totalBugdets = totalVerticalPlan.get(columnDates.get(i));
                            if (totalBugdets != null && totalBugdets.get(budgetColumn.getCode()) != null) {
                                BudgetInDate verticalTotal = totalBugdets.get(budgetColumn.getCode());
                                if (totalHorVer.get(budgetColumn.getCode()) != null) {
                                    BudgetInDate totalHorVerBudget = totalHorVer.get(budgetColumn.getCode());
                                    totalHorVerBudget.setValue(totalHorVerBudget.getValue().add(verticalTotal.getValue()));
                                    totalHorVer.replace(budgetColumn.getCode(), totalHorVerBudget);
                                } else {
                                    BudgetInDate totalHorVerBudget = new BudgetInDate();
                                    totalHorVerBudget.setValue(verticalTotal.getValue());
                                    totalHorVer.put(budgetColumn.getCode(), totalHorVerBudget);
                                }

                                tdChild.setInnerHTML("<b>" + getValueAsString(verticalTotal.getValue()) + "</b>");
                            } else {
                                tdChild.setInnerHTML("<b>" + getValueAsString(BigDecimal.ZERO) + "</b>");
                            }
                            row.appendChild(tdChild);
                        }
                    }
                }
            }
        }
        element.appendChild(tr);
    }

    private void clearElementChild(Element element) {
        Element child;
        while ((child = element.getFirstChildElement()) != null) {
            element.removeChild(child);
        }
    }

    private Element createTH() {
        Element header = DOM.createTHead();
        Element tr = DOM.createTR();


        for (int i = 0; i < columnCountPeriod; i++) {
            Element th = DOM.createTH();
            th.addClassName("stickerCell");
            Element div1 = DOM.createDiv();
            div1.addClassName("frame_affix_top");
            Element div = DOM.createDiv();
            div.addClassName("tm__th-day");
            div1.appendChild(div);

            div.setInnerHTML(i == 0 ? wfmStrings.name() : i == columnCountPeriod - 1 && columnCountPeriod != 2 ? wfmStrings.total() : dateFormat.format(startDate));
            th.appendChild(div1);
            if (i != 0) {
                startDate = DateUtil.addMonths(startDate, 1, 1);
                columnDates.put(i + 1, new DateNonConvertable(startDate).getNonConvertedDate());
                th.addClassName("tm__th-month");

                Element table = DOM.createDiv();
                table.addClassName("table");
                Element childtr = DOM.createDiv();

                if (columns != null) {
                    int colInt = columns.size() > 0 ? 12 / columns.size() : 12;
                    childtr.addClassName("table-row");
                    for (BudgetColumn budgetColumn : columns) {
                        Element childth = DOM.createDiv();
                        childth.addClassName("tm-child tm-child--" + colInt);
                        childth.setInnerHTML(budgetColumn.getName());
                        childtr.appendChild(childth);
                    }
                }
                table.appendChild(childtr);
                div1.appendChild(table);
            } else {
                th.addClassName("tm__cat-cell");
                columnDates.put(1, new DateNonConvertable(startDate).getNonConvertedDate());
            }
            tr.appendChild(th);
        }

        header.appendChild(tr);
        return header;
    }

    private void collectTreeData(Element table, AccountItemsByAccountType accountItemsByAccountType, boolean calculateTotal) {
        Element element = DOM.createTBody();
        table.appendChild(element);
        element.addClassName("category_set");

        createGroupHeader(element, accountItemsByAccountType.getGroupName());
        totalHorizontalPlan = new HashMap<>();
        if (accountItemsByAccountType.getChild() != null && !accountItemsByAccountType.getChild().isEmpty()) {
            for (AccountItemsByAccountType child : accountItemsByAccountType.getChild()) {
                Element tr = DOM.createTR();
                element.appendChild(tr);
                element.addClassName("parentElement");
                Element td = DOM.createTD();
                Element divInner = DOM.createDiv();
                divInner.appendChild(td);
                divInner.addClassName("table-wrapper");
                td.addClassName("second_level");
                td.setAttribute("colspan", columnCountPeriod + "");
                tr.appendChild(td);
                td.appendChild(divInner);

                Element table2 = DOM.createTable();
                table2.addClassName("table tm");
                table2.setAttribute("cellspacing", "0");
                table2.setAttribute("cellpadding", "0");

                divInner.appendChild(table2);

                Element thead = DOM.createTHead();
                Element theadTr = DOM.createTR();
                thead.appendChild(theadTr);
                table2.appendChild(thead);
                for (int i = 0; i < columnCountPeriod; i++) {
                    Element th = DOM.createTH();
                    th.addClassName(i == 0 ? "stickerCell tm__cat-cell" : "stickerCell tm__th-month");
                    theadTr.appendChild(th);
                }
                collectTreeData(table2, child, false);
            }
        }
        for (AccountItemWithBudgetDate accountItemWithBudgetDates : accountItemsByAccountType.getAccountItems()) {
            create(element, accountItemWithBudgetDates, accountItemsByAccountType);
        }
        createGroupTotalRow(element, wfmStrings.total() + " " + accountItemsByAccountType.getGroupName(), accountItemsByAccountType.getBudgetItemTotal(), calculateTotal);
    }

    private void createGroupHeader(Element element, String groupName) {
        EventListener headerEvent = null;
        element.addClassName("collapsed");
        Element tr = DOM.createTR();
        tr.addClassName("heading_row");
        Element td = DOM.createTD();

        Element icon = DOM.createElement("i");
        icon.addClassName("btn--circle plusMinus");
        DOM.sinkEvents(icon.cast(), Event.ONCLICK);
        headerEvent = event -> {
            if (element.getClassName().contains("collapsed")) {
                element.removeClassName("collapsed");
                element.addClassName("expanded");
            } else {
                element.removeClassName("expanded");
                element.addClassName("collapsed");
            }
        };

        if (headerEvent != null) {
            DOM.setEventListener(icon.cast(), headerEvent);
        }

        Element nameElement = DOM.createElement("span");
        nameElement.setInnerHTML(groupName);

        td.appendChild(icon);
        td.appendChild(nameElement);

        tr.appendChild(td);
        element.appendChild(tr);

        for (int j = 0; j < columnCountPeriod; j++) {
            if (j == 0) {
                td.addClassName("level-toggle");
                td.setAttribute("colspan", columnCountPeriod + "");
            }
        }
    }

    private Element create(Element element, AccountItemWithBudgetDate accountItemWithBudgetDate, AccountItemsByAccountType accountItemsByAccountType) {

        Element tr = DOM.createTR();
        element.appendChild(tr);
        Map<String, BudgetInDate> horizontalTotalByItemPlan = new HashMap<>();
        String type = budgetManager.getSelectedItem().getDescription();
        boolean hasPermission = Constants.PRODUCTS.equals(type) ? Utils.hasPermission(PermissionConstants.ACCOUNTING_PRODUCT_SUMMARY) : Constants.CHART_OF_ACCOUNT.equals(type) ? Utils.hasPermission(PermissionConstants.ACCOUNTING_ACCOUNT_SUMMARY) : !Constants.CUSTOMER.equals(type) || Utils.hasPermission(PermissionConstants.ACCOUNTING_CUSTOMER_SUMMARY);

        for (int j = 0; j < columnCountPeriod; j++) {
            if (j == 0) {
                Element td = DOM.createTD();
                td.addClassName("tm__cat-cell");
                if (!hasPermission) {
                    Element span = DOM.createAnchor();
                    span.setInnerHTML(accountItemWithBudgetDate.getName());
                    td.appendChild(span);
                } else {
                    Element entityLink = DOM.createAnchor();
                    DOM.sinkEvents(entityLink.cast(), Event.ONCLICK);
                    EventListener entityEventListener = event -> {
                        if (Constants.PRODUCTS.equals(type)) {
                            if (Utils.getPathName().contains("Accounting.html") || Utils.getPathName().contains("Crm.html")) {
                                SinksContainerFactory.entryPoint.onHistoryChanged("product|summary/" + accountItemWithBudgetDate.getId(), accountItemWithBudgetDate.getName());
                            } else {
                                Utils.openURL("Accounting.html#product|summary/" + accountItemWithBudgetDate.getId());
                            }
                        } else if (Constants.OPPORTUNITY.equals(type)) {
                            if (Utils.getPathName().contains("Accounting.html") || Utils.getPathName().contains("Crm.html")) {
                                SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|summary/" + accountItemWithBudgetDate.getId(), accountItemWithBudgetDate.getName());
                            } else {
                                Utils.openURL("Crm.html#opportunity|summary/" + accountItemWithBudgetDate.getId());
                            }
                        } else if (Constants.CHART_OF_ACCOUNT.equals(type)) {
                            if (Utils.getPathName().contains("Accounting.html")) {
                                SinksContainerFactory.entryPoint.onHistoryChanged("chartOfAccount|chartOfAccountSummary/" + accountItemWithBudgetDate.getId(), accountItemWithBudgetDate.getName());
                            } else {
                                Utils.openURL("Accounting.html#chartOfAccount|chartOfAccountSummary/" + accountItemWithBudgetDate.getId());
                            }
                        } else if (Constants.CUSTOMER.equals(type)) {
                            if (Utils.getPathName().contains("Accounting.html")) {
                                SinksContainerFactory.entryPoint.onHistoryChanged("client|summary/" + accountItemWithBudgetDate.getId(), accountItemWithBudgetDate.getName());
                            } else if (Utils.getPathName().contains("Crm.html")) {
                                SinksContainerFactory.entryPoint.onHistoryChanged("account|summary/" + accountItemWithBudgetDate.getId() + "/false/Customer", accountItemWithBudgetDate.getName());
                            } else {
                                Utils.openURL("Crm.html#account|summary/" + accountItemWithBudgetDate.getId() + "/false/Customer");
                            }
                        } else if (Constants.EMPLOYEES.equals(type)) {
                            Utils.openURL("Hrms.html#employeeProfile|employeeProfileView/" + accountItemWithBudgetDate.getId());
                        } else if (type.contains("_FORM")) {
                            SinksContainerFactory.entryPoint.onHistoryChanged(Constants.ITEM_LIST + "|summary/" + accountItemWithBudgetDate.getId() + "/" + budgetManager.getSelectedItem().getEntityId() + "/" + type + "/" + accountItemWithBudgetDate.getName());
                        }
                    };
                    DOM.setEventListener(entityLink.cast(), entityEventListener);

                    if (accountItemWithBudgetDate.getName() != null) {
                        entityLink.setInnerHTML(String.valueOf(accountItemWithBudgetDate.getName()));
                        entityLink.setTitle(String.valueOf(accountItemWithBudgetDate.getName()));
                    }
                    td.appendChild(entityLink);
                }
                tr.appendChild(td);
//                Event.sinkEvents(span, Event.ONCLICK);
//                Event.setEventListener(span, new EventListener() {
//
//                    @Override
//                    public void onBrowserEvent(Event event) {
//                        if(Event.ONCLICK == event.getTypeInt()) {
//                        }
//                    }
//                });

            } else {
                Element td = DOM.createTD();
                td.addClassName("tm__td-month");
                tr.appendChild(td);
                Element table = DOM.createTable();
                table.setAttribute("cellspacing", "0");
                table.setAttribute("cellpadding", "0");
                td.appendChild(table);
                Element tbody = DOM.createTBody();
                table.appendChild(tbody);
                Element row = DOM.createTR();
                tbody.appendChild(row);

                if (columns != null) {
                    for (BudgetColumn budgetColumn : columns) {

                        Element tdChild = DOM.createTD();
                        tdChild.addClassName("tm-child ");

                        Element widget = null;
                        Date uiDate = columnDates.get(j);
                        LinkedHashMap<String, LinkedList<BudgetInDate>> budgets = accountItemWithBudgetDate.getBudgetData();
                        if (j == columnCountPeriod - 1 && columnCountPeriod != 2) {
                            if (horizontalTotalByItemPlan.get(budgetColumn.getCode()) != null) {
                                tdChild.setInnerHTML(getValueAsString(horizontalTotalByItemPlan.get(budgetColumn.getCode()).getValue()));
                                if (totalHorizontalPlan.get(budgetColumn.getCode()) != null) {
                                    BudgetInDate totalBudgetInDate = totalHorizontalPlan.get(budgetColumn.getCode());
                                    totalBudgetInDate.setValue(totalBudgetInDate.getValue().add(horizontalTotalByItemPlan.get(budgetColumn.getCode()).getValue()));
                                    totalHorizontalPlan.replace(budgetColumn.getCode(), totalBudgetInDate);
                                } else {
                                    BudgetInDate totalBudgetInDate = new BudgetInDate();
                                    totalBudgetInDate.setValue(horizontalTotalByItemPlan.get(budgetColumn.getCode()).getValue());
                                    totalHorizontalPlan.replace(budgetColumn.getCode(), totalBudgetInDate);
                                }
                            } else {
                                tdChild.setInnerHTML(getValueAsString(BigDecimal.ZERO));
                            }

//                            if (i == 0) {
//                            } else {
//                                totalHorizontalPlan.setActualValue(totalHorizontalPlan.getActualValue().add(horizontalTotalByItemPlan.getActualValue()));
//                                tdChild.setInnerHTML(getValueAsString(horizontalTotalByItemPlan.getActualValue()));
//                            }
                            row.appendChild(tdChild);
                        } else {
                            if (Constants.EDITABLE.equals(budgetColumn.getType())) {
                                if (budgets != null && budgets.get(budgetColumn.getCode()) != null) {
                                    LinkedList<BudgetInDate> budgetInDates = budgets.get(budgetColumn.getCode());
                                    for (BudgetInDate budget1 : budgetInDates) {

                                        Date serviceDate = new DateNonConvertable(budget1.getDate()).getNonConvertedDate();
                                        Date uiDateNonConverted = new DateNonConvertable(uiDate).getNonConvertedDate();
                                        if (uiDateNonConverted.getMonth() == serviceDate.getMonth()) {
                                            BigDecimal budget = budget1.getValue() != null ? budget1.getValue() : BigDecimal.ZERO;
                                            if (horizontalTotalByItemPlan.get(budgetColumn.getCode()) != null) {
                                                BudgetInDate totalBudget = horizontalTotalByItemPlan.get(budgetColumn.getCode());
                                                totalBudget.setDate(serviceDate);
                                                totalBudget.setValue(totalBudget.getValue().add(budget));
                                                horizontalTotalByItemPlan.replace(budgetColumn.getCode(), totalBudget);
                                            } else {
                                                horizontalTotalByItemPlan.put(budgetColumn.getCode(), budget1);
                                            }
                                            widget = getEditableCell(budget, uiDate, budget1.getAccountBudgetID(), accountItemWithBudgetDate.getId(), budgetColumn, accountItemsByAccountType.getGroupId());
                                            tdChild.appendChild(widget);
                                            row.appendChild(tdChild);
                                            break;
                                        }
                                    }
                                    if (widget == null) {

                                        widget = getEditableCell(BigDecimal.ZERO, uiDate, null, accountItemWithBudgetDate.getId(), budgetColumn, accountItemsByAccountType.getGroupId());
                                        tdChild.appendChild(widget);
                                        row.appendChild(tdChild);
                                    }
                                } else if (widget == null) {

                                    widget = getEditableCell(BigDecimal.ZERO, uiDate, null, accountItemWithBudgetDate.getId(), budgetColumn, accountItemsByAccountType.getGroupId());
                                    tdChild.appendChild(widget);
                                    row.appendChild(tdChild);
                                }
                            } else {
                                Element span = DOM.createSpan();
                                span.addClassName(RIGHT_ALIGN_CELL);
                                span.setInnerHTML(getValueAsString(BigDecimal.ZERO));
                                tdChild.appendChild(span);
                                row.appendChild(tdChild);
                            }
                        }
                    }
                }
            }
        }

        return element;
    }

    private void createGroupTotalRow(Element element, String groupName, Map<String, ArrayList<BudgetInDate>> budgetTotal, boolean calculateTotal) {
        totalHorizontalPlan = new HashMap<>();
        Element tr = DOM.createTR();
        tr.addClassName("total_row");
        for (int i = 0; i < columnCountPeriod; i++) {
            if (i == 0) {
                Element td = DOM.createTD();
                td.setInnerHTML(groupName);
                td.addClassName("tm__cat-cell");
                tr.appendChild(td);
            } else {
                Element td = DOM.createTD();
                td.addClassName("tm__td-month");
                tr.appendChild(td);
                Element table = DOM.createTable();
                table.setAttribute("cellspacing", "0");
                table.setAttribute("cellpadding", "0");
                td.appendChild(table);
                Element tbody = DOM.createTBody();
                table.appendChild(tbody);
                Element row = DOM.createTR();
                tbody.appendChild(row);

                if (columns != null) {
                    for (BudgetColumn budgetColumn : columns) {
                        Element tdChild = DOM.createTD();
                        tdChild.addClassName("tm-child");

                        if (i == columnCountPeriod - 1 && columnCountPeriod != 2) {
                            tdChild.setInnerHTML("<span class='tm-totalCol'>" + getValueAsString(totalHorizontalPlan.get(budgetColumn.getCode()) != null ? totalHorizontalPlan.get(budgetColumn.getCode()).getValue() : BigDecimal.ZERO) + "</span>");
                            row.appendChild(tdChild);
                        } else {
                            BudgetInDate verticalTotal = new BudgetInDate();
                            if (budgetTotal != null && budgetTotal.get(budgetColumn.getCode()) != null) {
                                List<BudgetInDate> budgetInDateList = budgetTotal.get(budgetColumn.getCode());
                                for (BudgetInDate budget1 : budgetInDateList) {
                                    Date uiDate = columnDates.get(i);
                                    Date serviceDate = new DateNonConvertable(budget1.getDate()).getNonConvertedDate();
                                    if (uiDate != null && DateUtil.equalByMonths(uiDate, serviceDate)) {

                                        verticalTotal.setValue(budget1.getValue());
                                        if (totalHorizontalPlan.get(budgetColumn.getCode()) != null) {
                                            BudgetInDate horizonBudget = totalHorizontalPlan.get(budgetColumn.getCode());
                                            horizonBudget.setValue(horizonBudget.getValue().add(budget1.getValue()));
                                            totalHorizontalPlan.replace(budgetColumn.getCode(), horizonBudget);
                                        } else {
                                            totalHorizontalPlan.put(budgetColumn.getCode(), budget1);
                                        }

                                        if (calculateTotal) {
                                            if (totalVerticalPlan.get(uiDate) != null) {
                                                HashMap<String, BudgetInDate> totalBudgetInDates = totalVerticalPlan.get(uiDate);
                                                if (totalBudgetInDates.get(budgetColumn.getCode()) != null) {
                                                    BudgetInDate total = totalBudgetInDates.get(budgetColumn.getCode());
                                                    total.setValue(total.getValue().add(budget1.getValue()));
                                                } else {
                                                    totalBudgetInDates.put(budgetColumn.getCode(), budget1);
                                                }
                                                totalVerticalPlan.replace(uiDate, totalBudgetInDates);
                                            } else {
                                                HashMap<String, BudgetInDate> totalBudgetInDates = new HashMap<>();
                                                totalBudgetInDates.put(budgetColumn.getCode(), verticalTotal);
                                                totalVerticalPlan.put(uiDate, totalBudgetInDates);
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                            tdChild.setInnerHTML(getValueAsString(verticalTotal.getValue()));
                            row.appendChild(tdChild);
                        }
                    }
                }
            }
        }
        element.appendChild(tr);
    }

    private String getValueAsString(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) >= 0) {
            return " " + Utils.getCalculationNumberFormatWithCustomScale(scale).format(value);
        } else {
            return "(" + Utils.getCalculationNumberFormatWithCustomScale(scale).format(value.abs()) + ")";
        }
    }

    private Element getEditableCell(BigDecimal value, Date uiDate, Integer accountBudgetID, Integer entityID, BudgetColumn budgetColumn, Integer groupId) {
        EventListener eventListener = null;
        Element inputElement = DOM.createInputText();
        InputElement inputPlan = inputElement.cast();
//        inputPlan.addClassName("form-control-sm " + RIGHT_ALIGN_CELL);
        inputPlan.addClassName("form-control-sm");
        inputPlan.setPropertyString("value", Utils.getCalculationNumberFormatWithCustomScale(scale).format(value != null ? value : BigDecimal.ZERO));
        inputPlan.setMaxLength(9);
        String oldValue = inputPlan.getValue();

        DOM.sinkEvents(inputPlan, Event.ONBLUR | Event.ONKEYPRESS);

        eventListener = event -> {
            switch (DOM.eventGetType(event)) {
                case Event.ONKEYPRESS:
                    char key = (char) event.getCharCode();

                    if (key == (char) 0) {
                        return;
                    }

                    if ((!Character.isDigit(key)) && (key != (char) KeyCodes.KEY_TAB)
                            && (key != (char) KeyCodes.KEY_BACKSPACE)
                            && (key != (char) KeyCodes.KEY_DELETE) && (key != (char) KeyCodes.KEY_ENTER)
                            && (key != (char) KeyCodes.KEY_HOME) && (key != (char) KeyCodes.KEY_END)
                            && (key != (char) KeyCodes.KEY_LEFT) && (key != (char) KeyCodes.KEY_UP)
                            && (key != (char) KeyCodes.KEY_RIGHT) && (key != (char) KeyCodes.KEY_DOWN)) {
                        DOM.eventPreventDefault(event);
                    }
                    break;
                case Event.ONBLUR:
                    if (inputPlan.getValue() != null && !inputPlan.getValue().equals(oldValue)) {
                        BigDecimal budget = AccountingUtils.parsePriceToBigDecimal(inputPlan.getValue());
                        if (budget != null && budget.compareTo(BigDecimal.ZERO) > 0) {
                            AccountBudget accountBudget = new AccountBudget();
                            accountBudget.setId(accountBudgetID);
                            accountBudget.setEntityID(entityID);
                            accountBudget.setBudgetManagerId(budgetManager.getSelectedItem() != null ? budgetManager.getSelectedItem().getId() : null);
                            accountBudget.setType(budgetManager.getSelectedItem() != null ? budgetManager.getSelectedItem().getDescription() : "");
                            accountBudget.setColumnCode(budgetColumn.getCode());
                            accountBudget.setGroupId(groupId);
                            accountBudget.setBudget(budget);

                            Date date = uiDate;
                            date = DateUtil.addDays(date, 1);
                            accountBudget.setDate(date);
                            accountingService.createBudgetManagerItem(accountBudget, new AbstractAsyncCallback<Void>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    super.failure(throwable);
                                }

                                @Override
                                public void success(Void result) {
                                    super.success(result);
//                                    update();
                                }
                            });
                        }
                    }
                    break;
            }
        };
        if (eventListener != null) {
            DOM.setEventListener(inputPlan, eventListener);
        }

        return inputPlan;
    }

    private String clarifyID(Date date) {
        return Integer.toString(date.getYear()) + date.getMonth();
    }

    interface NewBudgetSheetUiBinder extends UiBinder<HTMLPanel, NewBudgetSheetV2> {
    }
}

