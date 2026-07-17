package com.edatasite.workforce.gwt.project.client.ui.view.projectbudget;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.ExportPdfButton;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmWindow;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.project.client.localization.ProjectMessages;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.rpc.NewProjectBudgetData;
import com.edatasite.workforce.gwt.project.client.rpc.NewProjectBudgetRowItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/17/12
 * Time: 2:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewProjectBudgetView extends FooteredView implements Constants {

    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final ProjectMessages projectMessages = ProjectMessages.App.get();

    private static final DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy_MM");
    private static final NumberFormat numberFormat = Utils.getCalculationNumberFormat();
    private static final Integer calculationScale = Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2;

    private final Integer projectID;
    private Div mainPanel;
    private VerticalPanel contentPanel;
    private HTMLPanel buttonPanel;
    private VerticalPanel titlePanel;
    private Div scrollPanel;

    private ProjectBudgetTable revenuesTable, expensesTable, purchasesTable;
    private FlexTable projectProfitTable;

    private ProjectBudgetSheetProvider budgetSheetProvider;

    private ArrayList<DateNonConvertable[]> monthIntervalsList = new ArrayList<>();
    private final LinkedList<String> monthColumnKeys = new LinkedList<>();
    private final LinkedHashMap<Integer, ColumnConfig> columnsMap = new LinkedHashMap<>();

    private DateNonConvertable startDate, endDate;

    private Boolean isContentChanged = false;

    private WfmButton2 saveButton, refreshButton, pdfVersionButton, excelVersionButton;

    private boolean isDetailedPurchasesEnabled = false;
    private boolean hasAccessToChange = true;

    public NewProjectBudgetView(Integer projectID, boolean hasAccessToChange) {
        super("projectBudget", projectStrings.projectBudget());
        this.projectID = projectID;
        this.hasAccessToChange = hasAccessToChange;
    }

    @Override
    protected Widget onInitialize() {

        budgetSheetProvider = new ProjectBudgetSheetProvider() {

            @Override
            public Integer getProjectID() {
                return projectID;
            }

            @Override
            public List<String> getMonthColumnKeys() {
                return monthColumnKeys;
            }

            @Override
            public ArrayList<DateNonConvertable[]> getMonthIntervalsList() {
                return monthIntervalsList;
            }

            @Override
            public void calculateProjectProfit() {
                for (int i = 1; i < columnsMap.size() - 2; i++) {
                    calculateColumnProfit(i);
                }
                calculateProfitVariance();
                isContentChanged = true;
            }

            @Override
            public void calculateProjectColumnProfit(Integer currentColumn) {
                //Calculate Current Column Profit
                calculateColumnProfit(currentColumn);
                //Calculate Total Column Profit
                calculateColumnProfit(columnsMap.size() - 4);//Project Profit Budget Total
                calculateColumnProfit(columnsMap.size() - 3);//Project Profit Actual Total
                calculateProfitVariance();
                isContentChanged = true;
            }

            private void calculateColumnProfit(Integer column) {
                FlexTable revTotalTable = revenuesTable.getTotalTable(), expTotalTable = expensesTable.getTotalTable(), purTotalTable = purchasesTable.getTotalTable();
                BigDecimal currentColProfit = BigDecimal.valueOf(numberFormat.parse(((HTML) revTotalTable.getWidget(0, column)).getText())).setScale(calculationScale, RoundingMode.HALF_UP);
                currentColProfit = currentColProfit.subtract(BigDecimal.valueOf(numberFormat.parse(((HTML) expTotalTable.getWidget(0, column)).getText())).setScale(calculationScale, RoundingMode.HALF_UP));
                currentColProfit = currentColProfit.subtract(BigDecimal.valueOf(numberFormat.parse(((HTML) purTotalTable.getWidget(0, column)).getText())).setScale(calculationScale, RoundingMode.HALF_UP));
                ((HTML) projectProfitTable.getWidget(0, column)).setText(numberFormat.format(currentColProfit));
            }

            private void calculateProfitVariance() {
                Integer columnsSize = columnsMap.size();
                HTML tableTotalVarianceAmountHTML = (HTML) projectProfitTable.getWidget(0, columnsSize - 2);
                HTML tableTotalVariancePercentHTML = (HTML) projectProfitTable.getWidget(0, columnsSize - 1);

                BigDecimal totalBudgetAmount = BigDecimal.valueOf(numberFormat.parse(((HTML) projectProfitTable.getWidget(0, columnsSize - 4)).getText())).setScale(calculationScale, RoundingMode.HALF_UP);
                BigDecimal totalActualAmount = BigDecimal.valueOf(numberFormat.parse(((HTML) projectProfitTable.getWidget(0, columnsSize - 3)).getText())).setScale(calculationScale, RoundingMode.HALF_UP);
                tableTotalVarianceAmountHTML.setText(numberFormat.format(totalActualAmount.subtract(totalBudgetAmount)));
                if (totalBudgetAmount.compareTo(BigDecimal.ZERO) > 0) {
                    tableTotalVariancePercentHTML.setText(numberFormat.format(totalActualAmount.subtract(totalBudgetAmount).multiply(new BigDecimal(100)).divide(totalBudgetAmount, calculationScale, RoundingMode.HALF_UP)));
                } else {
                    tableTotalVariancePercentHTML.setText(numberFormat.format(BigDecimal.ZERO));
                }
            }
        };


        contentPanel = new VerticalPanel();

        loadContent();
        return null;
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return NewProjectBudgetView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return NewProjectBudgetView.this.getFooterRightSideWidgets();
            }
        });
    }

    public List<Widget> getFooterLeftSideWidgets() {
        return null;
    }

    public List<Widget> getFooterRightSideWidgets() {
        List<Widget> rightSideWidgets = new ArrayList<>();

        Div saveWrapper = new Div();
        saveWrapper.add(buttonPanel);
        rightSideWidgets.add(saveWrapper);

        return rightSideWidgets;
    }
    private void loadContent() {
        clear();
        LoadingPanel.loading(true);
        ProjectService.App.get().getProjectPeriod(projectID, new AsyncCallback<Date[]>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Date[] projectPeriod) {
                startDate = new DateNonConvertable(DateUtil.resetTime(projectPeriod[0]));
                endDate = new DateNonConvertable(DateUtil.getDayLastTime(projectPeriod[1] != null ? projectPeriod[1] : new Date()));

                ProjectService.App.get().getNewProjectBudgetData(projectID, startDate, endDate, new AsyncCallback<NewProjectBudgetData>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void onSuccess(NewProjectBudgetData result) {

                        isDetailedPurchasesEnabled = result.isDetailedPurchasesEnabled();
                        monthIntervalsList = result.getMonthIntervalList();
                        mainPanel = new Div("section-box box-bg--1");
                        initContent(result);
                        isContentChanged = false;
                        initButtonPanel();

                        buttonPanel.setVisible(true);
                        add(mainPanel);
                        add(createFooter());
                        pdfVersionButton.setVisible(true);
                        excelVersionButton.setVisible(true);
                        LoadingPanel.loading(false);
                    }
                });
            }
        });
    }

    private void initContent(NewProjectBudgetData result) {
        ColumnConfig[] columnsArray = initColumnsMap();

        columnsArray[0].setTitle(wfmStrings.revenue());
        revenuesTable = new ProjectBudgetTable(REVENUE, columnsArray, budgetSheetProvider);

        columnsArray[0].setTitle(projectStrings.expensesAndCurrenAssets());
        expensesTable = new ProjectBudgetTable(EXPENSES_AND_CURRENT_ASSET, columnsArray, budgetSheetProvider);

        columnsArray[0].setTitle(wfmStrings.purchases());
        purchasesTable = new ProjectBudgetTable(PURCHASES_STR, columnsArray, budgetSheetProvider);
        purchasesTable.setAddNewLineEnabled(isDetailedPurchasesEnabled);

        initProjectProfitTable();

        revenuesTable.setTableData(result.getRevenues());
        result.getEmployeeCost().setAccount(new SelectItem(-1, projectStrings.employeecost()));
        expensesTable.setTableData(new NewProjectBudgetRowItem[]{result.getEmployeeCost()});
        expensesTable.setTableData(result.getExpenses());

        if (isDetailedPurchasesEnabled) {
            purchasesTable.setTableData(result.getDetailedPurchases());
        } else {
            result.getPurchases().setAccount(new SelectItem(-2, wfmStrings.purchases()));
            purchasesTable.setTableData(new NewProjectBudgetRowItem[]{result.getPurchases()});
        }

        budgetSheetProvider.calculateProjectProfit();

        FlexTable headerTable = new HeaderTable(monthIntervalsList);
        VerticalPanel topTablesPanel = new VerticalPanel();
        topTablesPanel.setSpacing(0);
        topTablesPanel.setBorderWidth(0);
        topTablesPanel.add(headerTable);

        VerticalPanel bottomTablesPanel = new VerticalPanel();
        bottomTablesPanel.setSpacing(0);
        bottomTablesPanel.setBorderWidth(0);
        bottomTablesPanel.add(projectProfitTable);


//        if (titlePanel != null) {
//            titlePanel.removeFromParent();
//        }
//        titlePanel = new VerticalPanel();
//        HTML titleHTML = new HTML(projectMessages.projectBudgetSheet(result.getProjectName()));
//        titleHTML.getElement().setAttribute("style", "font-size:17px;");
//        titleHTML.setStyleName("customTitle text-center");
//        titlePanel.add(titleHTML);
//        if (result.getCustomerName() != null) {
//            HTML assignedToHTML = new HTML(projectMessages.assignedTo(result.getCustomerName()));
//            assignedToHTML.setStyleName("text-center");
//            assignedToHTML.getElement().setAttribute("style", "font-size:15px;");
//            titlePanel.add(assignedToHTML);
//        }
//        if (monthIntervalsList.size() > 0) {
//            HTML forPeriodHTML = new HTML(projectMessages.forPeriod(DateUtils.format(monthIntervalsList.get(0)[0]) + " - " + DateUtils.format(monthIntervalsList.get(monthIntervalsList.size() - 1)[1].getDate())));
//            forPeriodHTML.setStyleName("text-center");
//            forPeriodHTML.getElement().setAttribute("style", "font-size:12px;color:gray;");
//            titlePanel.add(forPeriodHTML);
//        }

        topTablesPanel.getElement().setAttribute("style", "margin-top:10px;");
        revenuesTable.getElement().setAttribute("style", "margin-top:10px;");
        expensesTable.getElement().setAttribute("style", "margin-top:10px;");
        purchasesTable.getElement().setAttribute("style", "margin-top:10px;");
        bottomTablesPanel.getElement().setAttribute("style", "margin-top:10px;");

//        mainPanel.add(titlePanel);
        contentPanel.add(topTablesPanel);
        contentPanel.add(revenuesTable);
        contentPanel.add(expensesTable);
        contentPanel.add(purchasesTable);
        contentPanel.add(bottomTablesPanel);

        if (scrollPanel != null) {
            scrollPanel.removeFromParent();
        }
        scrollPanel = new Div("scroll-box--x");
        scrollPanel.add(contentPanel);

        mainPanel.add(scrollPanel);
    }

    private void initButtonPanel() {
        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.setEnabled(hasAccessToChange);
        refreshButton = new WfmButton2(wfmStrings.refresh());
        pdfVersionButton = new WfmButton2(wfmStrings.pdf());
        pdfVersionButton.setVisible(false);
        excelVersionButton = new WfmButton2(wfmStrings.excel());
        excelVersionButton.setVisible(false);
        saveButton.addClickHandler(event -> saveProjectBudget(false));
        refreshButton.addClickHandler(event -> {
            if (isContentChanged) {
                final WfmMessageBox confirmBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo);
                confirmBox.setMessage(projectStrings.budgetChangedMessage());
                confirmBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onCancel() {
                        reloadContent();
                    }

                    @Override
                    public void onSubmit() {
                        saveProjectBudget(true);
                    }
                });
                confirmBox.open();
            } else {
                reloadContent();
            }
        });
        ExportPdfButton pdf = new ExportPdfButton(new ExportPdfButton.PdfRequestInterface() {
            @Override
            public String getUrl() {
                return "/projectBudgetPDFHandler";
            }

            @Override
            public boolean isLandscapeOptionEnabled() {
                return true;
            }

            @Override
            public HashMap<String, String> getParameters() {
                RequestObject requestObject = new RequestObject(projectID);
                ListingFilterParameter filterParameter = new ListingFilterParameter();
                filterParameter.setObjectId(projectID);
                filterParameter.setStartDateNC(Utils.getStartDateNCForFilter(startDate));
                filterParameter.setEndDateNC(Utils.getEndDateNCForFilter(endDate));
                HashMap<String, String> parametrs = filterParameter.getRequestParams();
                return parametrs;
            }
        });
        excelVersionButton.addClickHandler(clickEvent -> generatePDFOrExcel(buttonPanel,false));

        buttonPanel = new HTMLPanel("");
        buttonPanel.setStyleName("btns-group right");
        buttonPanel.add(refreshButton);
        buttonPanel.add(pdf);
        buttonPanel.add(excelVersionButton);
        buttonPanel.add(saveButton);
        buttonPanel.setVisible(false);
    }

    private void generatePDFOrExcel(HTMLPanel hp, boolean isPDF) {
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setObjectId(projectID);
        filterParameter.setStartDateNC(Utils.getStartDateNCForFilter(startDate));
        filterParameter.setEndDateNC(Utils.getEndDateNCForFilter(endDate));
        String url = isPDF ? (CommandConstants.PDF_URL + "/projectBudgetPDFHandler") : (CommandConstants.COMMON_URL + "/projectBudgetExcelHandler");
        HashMap<String, String> parametrs = filterParameter.getRequestParams();
        Utils.sendPDFOrExcelRequest(hp, url, parametrs, "_blank");
    }

    private void saveProjectBudget(final boolean reloadContent) {
        if (!validate()) {
            return;
        }

        NewProjectBudgetData budgetData = new NewProjectBudgetData();
        budgetData.setProjectID(projectID);

        budgetData.setRevenues(revenuesTable.getRowItems());
        budgetData.setExpenses(expensesTable.getRowItems());
        if (isDetailedPurchasesEnabled) {
            budgetData.setDetailedPurchases(purchasesTable.getRowItems());
        } else {
            budgetData.setPurchases(purchasesTable.getRowItems()[0]);
        }

        ProjectService.App.get().saveProjectBudgetData(budgetData, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                WfmWindow.error(wfmStrings.errorOccurredSavingChanges());
            }

            @Override
            public void onSuccess(Void result) {
                isContentChanged = false;
                if (reloadContent) {
                    reloadContent();
                } else {
                    WfmWindow.info(Utils.textFormat(wfmStrings.messSuccessfullySaved(), projectStrings.projectBudget()));
                }
            }
        });
    }

    private boolean validate() {
        List<String> accountsDuplicated = new LinkedList<>();
        Map<Integer, String> accountIDsMap = new HashMap<>();
        for (int i = 0; i < revenuesTable.getCellTable().getGrid().getRowCount(); i++) {
            ProjectBudgetAccountLookUp accountLookUp = (ProjectBudgetAccountLookUp) revenuesTable.getCellTable().getColumnById(i, Constants.PROJECT_BUDGET_ACCOUNT);
            SelectItem selectedItem = accountLookUp.getSelectedItem();
            if (selectedItem != null && selectedItem.getId() != null) {
                if (accountIDsMap.containsKey(selectedItem.getId())) {
                    accountsDuplicated.add(selectedItem.getName());
                } else {
                    accountIDsMap.put(selectedItem.getId(), selectedItem.getName());
                }
            }
        }

        for (int i = 0; i < expensesTable.getCellTable().getGrid().getRowCount(); i++) {
            ProjectBudgetAccountLookUp accountLookUp = (ProjectBudgetAccountLookUp) expensesTable.getCellTable().getColumnById(i, Constants.PROJECT_BUDGET_ACCOUNT);
            SelectItem selectedItem = accountLookUp.getSelectedItem();
            if (selectedItem != null && selectedItem.getId() != null) {
                if (accountIDsMap.containsKey(selectedItem.getId())) {
                    accountsDuplicated.add(selectedItem.getName());
                } else {
                    accountIDsMap.put(selectedItem.getId(), selectedItem.getName());
                }
            }
        }

        if (accountsDuplicated.size() > 0) {
            StringBuilder accountsAsString = new StringBuilder();
            int i = 0;
            for (String acc : accountsDuplicated) {
                if (i != 0) {
                    accountsAsString.append(", ");
                }
                accountsAsString.append("\"" + acc + "\"");
                i++;
            }

            WfmWindow.alert(accountsDuplicated.size() == 1 ? projectMessages.accountIsDuplicated(accountsAsString.toString()) : projectMessages.accountsAreDuplicated(accountsAsString.toString()));

            return false;
        }

        return true;
    }

    private void reloadContent() {
        contentPanel.clear();

        monthIntervalsList.clear();
        monthColumnKeys.clear();
        columnsMap.clear();

        loadContent();
    }

    private ColumnConfig[] initColumnsMap() {
        columnsMap.put(0, new ColumnConfig(LookUpCell.class, Constants.PROJECT_BUDGET_ACCOUNT, "", 300, true, true));

        int index = 1;
        for (DateNonConvertable[] monthInterval : monthIntervalsList) {
            String monthKey = dateFormat.format(monthInterval[0].getNonConvertedDate());
            monthColumnKeys.add(monthKey);
            columnsMap.put(index++, new ColumnConfig(CustomCell.class, monthKey + "_BUDGET", "", 100, true, true));
            columnsMap.put(index++, new ColumnConfig(CustomCell.class, monthKey + "_ACTUAL", "", 100, false, true));
        }

        columnsMap.put(index++, new ColumnConfig(CustomCell.class, Constants.TOTAL_BUDGET, "", 100, true, true));
        columnsMap.put(index++, new ColumnConfig(CustomCell.class, Constants.TOTAL_ACTUAL, "", 100, true, true));
        columnsMap.put(index++, new ColumnConfig(CustomCell.class, Constants.VARIANCE_AMOUNT, "", 100, false, true));
        columnsMap.put(index, new ColumnConfig(CustomCell.class, Constants.VARIANCE_PERCENT, "", 100, false, true));
        return columnsMap.values().toArray(new ColumnConfig[]{});
    }

    private FlexTable initProjectProfitTable() {
        projectProfitTable = new FlexTable();

        projectProfitTable.setWidget(0, 0, new HTML(projectStrings.projectProfit()));
        projectProfitTable.getFlexCellFormatter().setWidth(0, 0, "300px");
        for (int i = 1; i < columnsMap.size(); i++) {
            HTML totalValueHTML = new HTML(numberFormat.format(BigDecimal.ZERO));
            projectProfitTable.setWidget(0, i, totalValueHTML);
            projectProfitTable.getFlexCellFormatter().setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_RIGHT);
            projectProfitTable.getFlexCellFormatter().setWidth(0, i, "100px");
            projectProfitTable.getFlexCellFormatter().getElement(0, i).setAttribute("style", "padding:7px 0;");
        }
        projectProfitTable.setWidget(0, columnsMap.size(), new HTML());
        projectProfitTable.getFlexCellFormatter().setWidth(0, columnsMap.size(), "50px");
        projectProfitTable.getElement().setAttribute("class", "advanced-Grid  grid-columns zreachFullWidthFields file--NewProjectBudgetView");
        projectProfitTable.getElement().setAttribute("style", "font-weight:bold; background:#D0EDCC; color:#234B14; text-transform:uppercase;");
        return projectProfitTable;
    }

    @Override
    public String getIconStyle() {
        return "bgMark project-budget-sheet";
    }

    @Override
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
