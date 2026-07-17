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
import com.edatasite.workforce.gwt.accounting.client.rpc.BudgetRelatedIds;
import com.edatasite.workforce.gwt.accounting.client.rpc.ExpensesAndRevenue;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.budgetsheet.BudgetsheetColumn;
import com.edatasite.workforce.gwt.accounting.client.ui.budgetsheet.BudgetsheetItem;
import com.edatasite.workforce.gwt.accounting.client.ui.budgetsheet.BudgetsheetKeyboardListener;
import com.edatasite.workforce.gwt.accounting.client.ui.budgetsheet.BudgetsheetObject;
import com.edatasite.workforce.gwt.accounting.client.ui.budgetsheet.BudgetsheetProvider;
import com.edatasite.workforce.gwt.accounting.client.ui.newbudgetsheet.BudgetSheetTable;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.SectionBoxPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 02.09.2014.
 */
public class NewBudgetSheet extends Composite implements AccountingConstants, Constants {
    interface NewBudgetSheetUiBinder extends UiBinder<HTMLPanel, NewBudgetSheet> {
    }

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    private final AccountingServiceAsync accountingService = AccountingService.App.get();

    private static final AccountingUtils utils = AccountingUtils.get();

    private static final boolean isDepartmentRelationEnabled = AccountingUtils.get().isEnableAccountingDepartmentRelation();

    private final DateTimeFormat dateFormat = DateTimeFormat.getFormat("MMM-yy");

    private final int INCOME = 0;
    private final int OUTCOME = 1;
    private final boolean CALCULATE_GROSS_PROFIT = true;

    private Date currentDate = new Date();
    private Date startDate;

    private BudgetSheetTable budgetsheet;
    private DataListBox departmentListBox;
    private final DataListBox startDateListBox;
    private final DataListBox periodListBox;

    private int columnCount;
    private boolean button_clicked = false;

    private ProfitCalc[] grossProfits;
    private ProfitCalc[] netProfits;
    private HTML allGrossTotal;
    private HTML allNetTotal;

    private Integer[] budgetProfitID;
    private final Map<Integer, Object> startDateMap;
    private boolean isAsc = true;
    private String styleIcon;
    private int sortDirection = ASC;

    private final WfmButton2 excelButton;
    private final WfmButton2 updateButton;

    private static final NewBudgetSheetUiBinder ourUiBinder = GWT.create(NewBudgetSheetUiBinder.class);

    @UiField
    SectionBoxPanel headerPanel;
    @UiField
    HTMLPanel exportPanel;
    @UiField
    HTMLPanel budgetSheetTable;

    public NewBudgetSheet() {

        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);

        GBox groupBox = headerPanel.drawNewGroupBox();
        groupBox.setStyleWidthFree(true);
        groupBox.setStyleUnited(true);


        startDateMap = new HashMap<>();

        startDateListBox = new DataListBox(false);
        startDateListBox.ensureDebugId("budgetManager-startListBox");
        DateTimeFormat format = DateTimeFormat.getFormat("MMMM-yyyy");
        Date startDateNew = new Date();
        startDateNew.setYear(startDateNew.getYear() - 1);
        startDateNew.setMonth(0);
        for (int i = 0; i < 36; i++) {
            SelectItem item = new SelectItem(Integer.valueOf(clarifyID(startDateNew)), format.format(startDateNew));
            startDateListBox.addListItem(item);
            startDateMap.put(item.getId(), startDateNew);
            startDateNew = DateUtil.addMonths(startDateNew, 1, 1);
        }
        startDateListBox.setSelected(Integer.valueOf(clarifyID(currentDate)));
        headerPanel.addGroupBoxItem(wfmStrings.start(), startDateListBox);

        periodListBox = new DataListBox(false);
        periodListBox.ensureDebugId("budgetManager-periodListBox");
        for (int i = 6; i < 25; i += 6) {
            periodListBox.addListItem(new SelectItem(i, i + " " + wfmStrings.months()));
        }
        periodListBox.setSelected(6);
        GBoxItem periodBoxItem = headerPanel.addGroupBoxItem(wfmStrings.period(), periodListBox);

        if (isDepartmentRelationEnabled) {
            departmentListBox = new DataListBox(false);
            ListingFilterParameter filter = new ListingFilterParameter();
            AccountingService.App.get().getDepartmentsForAccounting(filter, new AsyncCallback<SelectItem[]>() {
                @Override
                public void onFailure(Throwable throwable) {
                }

                @Override
                public void onSuccess(SelectItem[] selectItems) {
                    departmentListBox.setItems(selectItems);
                }
            });
            GBoxItem departBoxItem = headerPanel.addGroupBoxItem(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), departmentListBox);
            departBoxItem.setStyleSplitRight(true);
        } else {
            periodBoxItem.setStyleSplitRight(true);
        }

        updateButton = new WfmButton2(wfmStrings.update(), WfmButton2.BTN_PRIMARY);
        updateButton.addClickHandler(event -> update());
        GBoxItem updateItem = headerPanel.addGroupBoxItem(null, updateButton);
        updateItem.setStyleSplitRight(true);

        excelButton = new WfmButton2(null, WfmButton2.BTN_WHITE, "ficon--download-cloud");
        excelButton.removeHasiconLeftStyle();
        excelButton.addStyleName("btn--icon");
        excelButton.addClickHandler(ch -> {
            Date startDate = DateUtil.addMonths(currentDate, 0, 1);
            Date endDate = DateUtil.addMonths(currentDate, periodListBox.getSelectedId(true), 1);

            String url = CommandConstants.COMMON_URL + "/budgetSheetExcelReport";
            ListingFilterParameter filter = new ListingFilterParameter();
            filter.setStartDateNC(Utils.getStartDateNCForFilter(startDate));
            filter.setEndDateNC(Utils.getEndDateNCForFilter(endDate));
            filter.setCaseID(columnCount);
            filter.setAscending(isAsc);
            if (isDepartmentRelationEnabled && departmentListBox != null) {
                filter.setDepartmentId(departmentListBox.getSelectedId());
            }
            HashMap<String, String> parametrs = filter.getRequestParams();
            Utils.sendPDFOrExcelRequest(exportPanel, url, parametrs, "_blank");
        });
        Div div = new Div();
        new KpiToolTip(excelButton, wfmStrings.export());
        div.add(excelButton);
        GBoxItem excelItem = headerPanel.addGroupBoxItem(null, div);
        excelItem.setStyleSplitRight(true);

        update();
    }

    private void update() {
        currentDate = (Date) startDateMap.get(startDateListBox.getSelectedId());
        startDate = DateUtil.addMonths(currentDate, 0, 1);
        Date endDate = DateUtil.addMonths(currentDate, periodListBox.getSelectedId(), 1);
        //We are adding 1, because besides dates we have the name of the item too.
        columnCount = periodListBox.getSelectedId() + 2;// 1 for name, 1 for total column

        grossProfits = new ProfitCalc[columnCount];
        netProfits = new ProfitCalc[columnCount];
        budgetProfitID = new Integer[columnCount];
        allGrossTotal = new HTML(AccountingUtils.getZero());
        allNetTotal = new HTML(AccountingUtils.getZero());
        Integer departmentID = null;
        if (isDepartmentRelationEnabled && departmentListBox != null) {
            departmentID = departmentListBox.getSelectedId(false);
        }
        button_clicked = true;
        LoadingPanel.loading(true);
        accountingService.getBudgetedAccounts(new DateNonConvertable(startDate), new DateNonConvertable(DateUtil.getDayLastTime(endDate)), departmentID, isAsc, new AsyncCallback<ExpensesAndRevenue>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ExpensesAndRevenue result) {
                drawBudgetsheetTable(result);
                button_clicked = false;
                LoadingPanel.loading(false);
                frameAffix();
            }
        });
    }

    public void refresh(int ascOrDesc, String s) {
        styleIcon = s;
        isAsc = ASC == ascOrDesc;
        sortDirection = ascOrDesc;
        update();
    }

    public static native void frameAffix() /*-{
        $wnd.table__frame_affix_init();
    }-*/;

    private void drawBudgetsheetTable(ExpensesAndRevenue data) {
        if (button_clicked && budgetsheet != null) {
            budgetSheetTable.remove(budgetsheet);
        }
        BudgetsheetColumn[] columns = new BudgetsheetColumn[columnCount];
        int nameWidth = 200;
        columns[0] = new BudgetsheetColumn("name", wfmStrings.name(), nameWidth, TEXT_LEFT);
        int width = 70;

        for (int i = 1; i < columnCount - 1; i++) {
            columns[i] = new BudgetsheetColumn(dateFormat.format(startDate), width, TEXT_RIGHT);
            columns[i].setData(startDate);
            startDate = DateUtil.addMonths(startDate, 1, 1);
            grossProfits[i] = new ProfitCalc();
            netProfits[i] = new ProfitCalc();
        }
        columns[columnCount - 1] = new BudgetsheetColumn("total", accountingMessages.total("(" + data.getCurrency().getName() + ")"), width, TEXT_RIGHT);

        budgetsheet = new BudgetSheetTable(this, columns, styleIcon, sortDirection);
        if (data != null) {
            //Drawing revenue items and filling budgetsheet with its data.      ---Groupable view---
            collectItemsByOreder(data.getRevenue(), INCOME, CALCULATE_GROSS_PROFIT);

            //Drawing saleinvoice items and filling budgetsheet with its data.  ---Groupable view---
            collectItemsByOreder(data.getSale(), INCOME, CALCULATE_GROSS_PROFIT);

            //Drawing other income items and filling budgetsheet with its data. ---Groupable view---
            collectItemsByOreder(data.getOtherIncome(), INCOME, CALCULATE_GROSS_PROFIT);

            //Drawing direct costs items and filling budgetsheet with its data. ---Groupable view---
            collectItemsByOreder(data.getDirectCosts(), OUTCOME, CALCULATE_GROSS_PROFIT);

            //Drawing GROSS PROFIT
            drawGrossProfit();

            //Drawing expense items and filling budgetsheet with its data.      ---Groupable view---
            collectItemsByOreder(data.getExpense(), OUTCOME, !CALCULATE_GROSS_PROFIT);

            //Drawing depreciation items and filling budgetsheet with its data. ---Groupable view---
            collectItemsByOreder(data.getDepreciation(), OUTCOME, !CALCULATE_GROSS_PROFIT);

            //Drawing overhead items and filling budgetsheet with its data.     ---Groupable view---
            collectItemsByOreder(data.getOverhead(), OUTCOME, !CALCULATE_GROSS_PROFIT);

            //Drawing NET PROFIT
            drawNetProfit();
        }
        budgetSheetTable.add(budgetsheet);
    }

    private void collectItemsByOreder(AccountItemsByAccountType items, int type, boolean hasGrossProfit) {
        if (items != null) {
            AccountItemWithBudgetDate[] accItems = items.getAccountItems();
            if (accItems != null && accItems.length > 0) {
                Map<String, AccountItemWithBudgetDate> map1 = new HashMap<>(accItems.length);
                ArrayList<AccountItemWithBudgetDate> map2 = new ArrayList<>();

                Arrays.stream(accItems).forEach(acc -> map1.put(acc.getCode(), acc));
                Arrays.stream(accItems).forEach(acc -> {
                    if (acc.getParentCode() != null && !acc.getParentCode().isEmpty()) {
                        if (map1.get(acc.getParentCode()) == null) {
                            AccountItem accountCodeUnique = new AccountItem(acc.getParentId(), acc.getParentCode(), acc.getParentName());
                            AccountItemWithBudgetDate budgetDate = new AccountItemWithBudgetDate(accountCodeUnique.getId(), accountCodeUnique.getCode(), accountCodeUnique.getName());
                            budgetDate.getChilds().add(acc);
                            map1.put(acc.getParentCode(), budgetDate);
                            map2.add(budgetDate);
                        } else {
                            map1.get(acc.getParentCode()).getChilds().add(acc);
                        }
                    } else {
                        map2.add(acc);
                    }
                    List<AccountItemWithBudgetDate> accountWithDate = new ArrayList<>();
                    map2.forEach(key -> {
                        collectList(accountWithDate, key, 0);
                    });
                    items.setAccountItems(accountWithDate);
                });
            }
            drawGroup(items, type, hasGrossProfit);
        }
    }

    private void collectList(List<AccountItemWithBudgetDate> accountWithDate, AccountItemWithBudgetDate item, Integer level) {
        item.setLevel(level);
        accountWithDate.add(item);
        if (!item.getChilds().isEmpty()) {
            item.getChilds().forEach(child -> {
                collectList(accountWithDate, child, level + 2);
            });
        }
    }

    private void drawGroup(AccountItemsByAccountType account, int type, boolean hasGrossProfit) {
        HTML[] totalValue = new HTML[columnCount - 1];
        String groupName = account.getGroupName();

        int rows = account.getAccountItems().length;
        BudgetsheetItem[] budgetsheetItems = new BudgetsheetItem[rows];
        HTML[] verticalTotal = new HTML[rows];
        for (int row = 0; row < rows; row++) {
            AccountItemWithBudgetDate item = account.getAccountItems()[row];

            BudgetsheetObject[] values = new BudgetsheetObject[columnCount];
            String code = item.getCode() != null ? "<small>(".concat(item.getCode()).concat(")</small>") : "";
            values[0] = new BudgetsheetObject(item.getName() + code, "");
            verticalTotal[row] = new HTML(AccountingUtils.getZero());
            verticalTotal[row].addStyleName(TEXT_RIGHT);

            for (int column = 1; column < columnCount - 1; column++) {
                if (totalValue[column] == null) {
                    if ("Direct Costs".equals(groupName) || "Overhead".equals(groupName)) {
                        totalValue[column] = new HTML(AccountingUtils.getZero());
                    } else {
                        totalValue[column] = new HTML(AccountingUtils.getZero());
                    }
                    totalValue[column].addStyleName(TEXT_RIGHT);
                }

                values[column] = drawEditColumn(column, item, totalValue[column], verticalTotal[row], type, hasGrossProfit, groupName);

                drawProfits(row, column, rows, type, totalValue[column], hasGrossProfit);
            }

            values[columnCount - 1] = new BudgetsheetObject(verticalTotal[row], "");
            budgetsheetItems[row] = new BudgetsheetItem(values);
            budgetsheetItems[row].setHasChild(!item.getChilds().isEmpty());
            budgetsheetItems[row].setLevel(item.getLevel());
        }
        boolean hasTotal = true;
        if (budgetsheetItems.length > 0) {
            switch (groupName) {
                case "Revenue":
                    groupName = wfmStrings.revenue();
                    break;
                case "Sales":
                    groupName = wfmStrings.sales();
                    break;
                case "Expenses":
                    groupName = wfmStrings.expenses();
                    break;
                case "Other Income":
                    groupName = accountingStrings.otherIncomes();
                    break;
                case "Direct Costs":
                    groupName = accountingStrings.directCosts();
                    hasTotal = false;
                    break;
                case "Overhead":
                    groupName = accountingStrings.overhead();
                    hasTotal = false;
                    break;
            }
            budgetsheet.add(groupName, budgetsheetItems);
            drawTotal(totalValue, groupName, hasTotal);
        }
    }

    private void drawProfits(int row, int column, int rows, int type, HTML totalValue, boolean hasGrossProfit) {
        if (row == rows - 1) {
            BigDecimal total = parseToBigDecimal(totalValue.getText());
            if (hasGrossProfit) {
                BigDecimal grossProfit = parseToBigDecimal(grossProfits[column].getText());
                if (INCOME == type) {
                    grossProfits[column].setHTML(utils.formatPriceForNegative(grossProfit.add(total)));
                } else {
                    grossProfits[column].setHTML(utils.formatPriceForNegative(grossProfit.subtract(total)));
                }
                grossProfits[column].addStyleName(TEXT_RIGHT);
            }

            BigDecimal netProfit = parseToBigDecimal(netProfits[column].getText());
            if (INCOME == type) {
                netProfits[column].setHTML(utils.formatPriceForNegative(netProfit.add(total)));
            } else {
                netProfits[column].setHTML(utils.formatPriceForNegative(netProfit.subtract(total)));
            }
            netProfits[column].addStyleName(TEXT_RIGHT);
        }
    }

    private BudgetsheetObject drawEditColumn(int c, AccountItemWithBudgetDate item, HTML total, HTML verticalTotal, int type, boolean gross, String groupName) {
        BudgetsheetObject value = drawColumn(c, item.getRowCells(), total, verticalTotal, true, groupName);
        value.setBudgetsheetProvider(save(c, value, item.getId(), total, verticalTotal, type, gross));
        value.addKeyboardListener(onEditCell(total, verticalTotal, c, type, gross));
        return value;
    }

    private BudgetsheetObject drawColumn(int c, ArrayList<BudgetInDate> budgets, HTML total, HTML verticalTotal, boolean editable, String groupName) {
        BudgetsheetObject value = null;
        boolean hasData = false;
        if (budgets != null) {
            for (BudgetInDate budget1 : budgets) {
                Date uiDate = (Date) budgetsheet.getColumnData(c);
                Date serviceDate = budget1.getDate();
                if (DateUtil.equalByMonths(uiDate, serviceDate)) {
                    BigDecimal budget = budget1.getValue();
                    BigDecimal vTotal = parseToBigDecimal(verticalTotal.getText()).add(budget);
                    BigDecimal totalValue = parseToBigDecimal(total.getText()).add(budget);
                    if ("Direct Costs".equals(groupName) || "Overhead".equals(groupName)) {
                        total.setHTML(utils.formatPriceForNegative(totalValue));
                    } else {
                        total.setHTML(utils.formatPriceForNegative(totalValue));
                    }
                    verticalTotal.setHTML(utils.formatPriceForNegative(vTotal));
                    value = new BudgetsheetObject(utils.formatPriceForNegative(budget), editable);
                    value.setNewValue(budget);
                    value.setAccountBudgetID(budget1.getAccountBudgetID());
                    budgetProfitID[c] = budget1.getProfitID();
                    hasData = true;
                    break;
                }
            }
        }
        if (!hasData) {
            value = new BudgetsheetObject(AccountingUtils.getZero(), editable);
        }
        return value;
    }

    private void drawTotal(HTML[] total, String groupName, boolean hasTotal) {
        BudgetsheetObject[] values = new BudgetsheetObject[columnCount];
        values[0] = new BudgetsheetObject(wfmStrings.total().concat(" ").concat(groupName), "total_row");

        HTML allTotal = null;
        if (hasTotal) {
            allTotal = new HTML(AccountingUtils.getZero());
        } else {
            allTotal = new HTML(AccountingUtils.getZero());
        }
        allTotal.addStyleName(TEXT_RIGHT);
        for (int i = 1; i < columnCount - 1; i++) {
            values[i] = new BudgetsheetObject(total[i]);
            if (hasTotal) {
                allTotal.setHTML(utils.formatPriceForNegative(parseToBigDecimal(allTotal.getText()).add(parseToBigDecimal(total[i].getText()))));
            } else {
                allTotal.setHTML(utils.formatPriceForNegative(parseToBigDecimal(allTotal.getText()).add(parseToBigDecimal(total[i].getText()))));
            }
        }
        values[columnCount - 1] = new BudgetsheetObject(allTotal);
        budgetsheet.add(new BudgetsheetItem(values), "total_row");
    }

    private BudgetsheetProvider save(int c, BudgetsheetObject value, Integer accountID, HTML total, HTML verticalTotal, int type, boolean gross) {
        final int column = c;
        final BudgetsheetObject object = value;
        final Integer accountId = accountID;
        final HTML totalHTML = total;
        final HTML vTotal = verticalTotal;
        final int accountType = type;
        final boolean isGrossProfit = gross;
        return (budget, data) -> {
            object.setOldValue(object.getNewValue());
            object.setNewValue(budget);

            BigDecimal difference = object.getNewValue().subtract(object.getOldValue() != null ? object.getOldValue() : ZERO);
            calculateTotals(difference, column, totalHTML, vTotal, accountType, isGrossProfit);

            AccountBudget accountBudget = new AccountBudget();
            accountBudget.setId(object.getAccountBudgetID());
            accountBudget.setAccountID(accountId);
            if (isDepartmentRelationEnabled && departmentListBox != null) {
                accountBudget.setDepartmentID(departmentListBox.getSelectedId());
            }
            accountBudget.setBudget(budget);

            Date date = (Date) data;
            date = DateUtil.addDays(date, 1);
            accountBudget.setDate(date);
            accountBudget.setProfitId(budgetProfitID[column]);
            accountBudget.setGrossProfit(parseToBigDecimal(grossProfits[column].getText()));
            accountBudget.setNetProfit(parseToBigDecimal(netProfits[column].getText()));
            accountingService.createAccountsBudget(accountBudget, new AbstractAsyncCallback<BudgetRelatedIds>() {
                public void success(BudgetRelatedIds ids) {
                    object.setAccountBudgetID(ids.getAccountBudgetId());
                    budgetProfitID[column] = ids.getProfitId();
                    update();
                }
            });
        };
    }

    private BudgetsheetKeyboardListener onEditCell(HTML total, HTML verticalTotal, int column, int accountType, boolean hasGrossProfit) {
        final HTML totalHTML = total;
        final HTML vTotal = verticalTotal;
        final int index = column;
        final int type = accountType;
        final boolean isGrossProfit = hasGrossProfit;
        return (cellOldValue, cellNewValue) -> {
            BigDecimal difference = cellNewValue.subtract(cellOldValue);
            calculateTotals(difference, index, totalHTML, vTotal, type, isGrossProfit);
        };
    }

    private void calculateTotals(BigDecimal difference, int column, HTML total, HTML verticalTotal, int type, boolean hasGrossProfit) {
        BigDecimal totalValue = parseToBigDecimal(total.getText()).add(difference);
        verticalTotal.setHTML(utils.formatPriceForNegative(parseToBigDecimal(verticalTotal.getText()).add(difference)));
        verticalTotal.addStyleName(TEXT_RIGHT);
        total.setHTML(utils.formatPriceForNegative(totalValue));
        total.addStyleName(TEXT_RIGHT);
        if (hasGrossProfit) {
            BigDecimal grossProfit = parseToBigDecimal(grossProfits[column].getText());
            grossProfits[column].calculateGrossProfit(type, grossProfit, difference);
        }
        netProfits[column].calculateNetProfit(type, parseToBigDecimal(netProfits[column].getText()), difference);
    }

    private void drawGrossProfit() {
        BudgetsheetObject[] values = new BudgetsheetObject[columnCount];
        values[0] = new BudgetsheetObject(accountingStrings.grossProfit(), "");

        BigDecimal grossProfit;
        BigDecimal allGross;
        for (int i = 1; i < columnCount - 1; i++) {
            grossProfit = parseToBigDecimal(grossProfits[i].getText());
            allGross = parseToBigDecimal(allGrossTotal.getText());
            allGrossTotal.setHTML(utils.formatPriceForNegative(allGross.add(grossProfit)));
            allGrossTotal.addStyleName(TEXT_RIGHT);
            values[i] = new BudgetsheetObject(grossProfits[i]);
        }
        values[columnCount - 1] = new BudgetsheetObject(allGrossTotal, TEXT_RIGHT);
        budgetsheet.add(new BudgetsheetItem(values), "total_row double");
    }

    private void drawNetProfit() {
        BudgetsheetObject[] values = new BudgetsheetObject[columnCount];
        values[0] = new BudgetsheetObject(accountingStrings.netProfit(), "");
        for (int i = 1; i < columnCount - 1; i++) {
            allNetTotal.setHTML(utils.formatPriceForNegative(parseToBigDecimal(allNetTotal.getText()).add(parseToBigDecimal(netProfits[i].getText()))));
            allNetTotal.addStyleName(TEXT_RIGHT);
            values[i] = new BudgetsheetObject(netProfits[i]);
        }
        values[columnCount - 1] = new BudgetsheetObject(allNetTotal, TEXT_RIGHT);
        budgetsheet.add(new BudgetsheetItem(values), "total_row double");
    }

    private BigDecimal parseToBigDecimal(String text) {
        return budgetsheet.parseToBigDecimal(text);
    }

    private String clarifyID(Date date) {
        return Integer.toString(date.getYear()) + date.getMonth();
    }

    private class ProfitCalc extends HTML {

        ProfitCalc() {

        }

        void calculateGrossProfit(int type, BigDecimal profit, BigDecimal difference) {
            if (type == 0) {
                setHTML(utils.formatPriceForNegative(profit.add(difference)));
                allGrossTotal.setHTML(utils.formatPriceForNegative(parseToBigDecimal(allGrossTotal.getText()).add(difference)));
            } else if (type == 1) {
                setHTML(utils.formatPriceForNegative(profit.subtract(difference)));
                allGrossTotal.setHTML(utils.formatPriceForNegative(parseToBigDecimal(allGrossTotal.getText()).subtract(difference)));
            }
        }

        void calculateNetProfit(int type, BigDecimal profit, BigDecimal difference) {
            if (type == 0) {
                setHTML(utils.formatPriceForNegative(profit.add(difference)));
                allNetTotal.setHTML(utils.formatPriceForNegative(parseToBigDecimal(allNetTotal.getText()).add(difference)));
            } else {
                setHTML(utils.formatPriceForNegative(profit.subtract(difference)));
                allNetTotal.setHTML(utils.formatPriceForNegative(parseToBigDecimal(allNetTotal.getText()).subtract(difference)));
            }
        }
    }
}
