package com.edatasite.workforce.gwt.project.client.ui.view.projectbudget;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.rpc.NewProjectBudgetCellItem;
import com.edatasite.workforce.gwt.project.client.rpc.NewProjectBudgetRowItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/21/12
 * Time: 5:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProjectBudgetTable extends VerticalPanel {

    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private static NumberFormat numberFormat = Utils.getCalculationNumberFormat();
    private static Integer calculationScale = Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2;

    private ColumnConfig[] columns;
    private ProjectBudgetSheetProvider budgetSheetProvider;
    private String type;

    private EditableTable cellTable;
    private FlexTable totalTable;

    private Boolean addNewLineEnabled = true;

    public ProjectBudgetTable(String type, ColumnConfig[] columns, ProjectBudgetSheetProvider budgetSheetProvider) {
        super();
        this.type = type;
        this.budgetSheetProvider = budgetSheetProvider;
        this.columns = columns;
        initialize();
    }

    private void initialize() {
        setSpacing(0);
        cellTable = new EditableTable(columns);
        cellTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                if (addNewLineEnabled)
                    addTableRow();
            }

            @Override
            public void removeRow() {
                calculateTable();
            }
        });
        add(cellTable);
        add(initTotalTable());
    }

    private FlexTable initTotalTable() {
        totalTable = new FlexTable();

        HTML totalLabelHTML = null;
        if (Constants.REVENUE.equals(type)) {
            totalLabelHTML = new HTML(projectStrings.totalRevenue());
        } else if (Constants.EXPENSES_AND_CURRENT_ASSET.equals(type)) {
            totalLabelHTML = new HTML(projectStrings.totalExpensesAndCurrentAssets());
        } else if (Constants.PURCHASES_STR.equals(type)) {
            totalLabelHTML = new HTML(wfmStrings.totalPurchases());
        }
        totalTable.setWidget(0, 0, totalLabelHTML != null ? totalLabelHTML : new HTML());
        totalTable.getFlexCellFormatter().setWidth(0, 0, "300px");
        for (int i = 1; i < columns.length; i++) {
            HTML totalValueHTML = new HTML(numberFormat.format(BigDecimal.ZERO));
            totalTable.setWidget(0, i, totalValueHTML);
            totalTable.getFlexCellFormatter().setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_RIGHT);
            totalTable.getFlexCellFormatter().setWidth(0, i, "100px");
        }
        totalTable.setWidget(0, columns.length, new HTML());
        totalTable.getFlexCellFormatter().setWidth(0, columns.length, "45px");
        totalTable.getElement().setAttribute("class", "advanced-Grid  grid-columns reachFullWidthFields");
        totalTable.getElement().setAttribute("style", "table-layout: fixed;font-weight: bold;");
        return totalTable;
    }

    public void addDefaultRows() {
        addTableRow();
        addTableRow();
        addTableRow();
    }

    public void addTableRow() {
        cellTable.addRow(getWidgetArray(null));
    }

    public void addTableRow(NewProjectBudgetRowItem rowItem) {
        cellTable.addRow(getWidgetArray(rowItem));
    }

    public void setTableData(NewProjectBudgetRowItem[] rowItems) {
        if (rowItems != null && rowItems.length > 0) {
            for (NewProjectBudgetRowItem rowItem : rowItems) {
                addTableRow(rowItem);
            }

            calculateTable();
        } else {
            addDefaultRows();
        }
    }

    private Widget[] getWidgetArray(NewProjectBudgetRowItem rowItem) {
        final LinkedHashMap<String, Widget> widgetsMap = new LinkedHashMap<>();

        boolean isEmployeeCost = false, isPurchases = false;
        if (rowItem != null && rowItem.getAccount() != null) {
            isEmployeeCost = rowItem.getAccount().getId() == -1;
            isPurchases = rowItem.getAccount().getId() == -2;
        }

        final ProjectBudgetAccountLookUp accountLookUp = new ProjectBudgetAccountLookUp(type);
        widgetsMap.put(Constants.PROJECT_BUDGET_ACCOUNT, accountLookUp);

        for (String colKey : budgetSheetProvider.getMonthColumnKeys()) {
            ProjectBudgetCellWidget budgetWidget = new ProjectBudgetCellWidget(this);
            ActualLabelCellWidget actualWidget = new ActualLabelCellWidget();

            if (isEmployeeCost) {
                budgetWidget.setEnabled(false);
            }

            if (rowItem != null && rowItem.getCellDataMap().get(colKey) != null) {
                if (rowItem.getCellDataMap().get(colKey).getBudget() != null) {
                    budgetWidget.setText(numberFormat.format(rowItem.getCellDataMap().get(colKey).getBudget()));
                }
                if (rowItem.getCellDataMap().get(colKey).getActual() != null) {
                    actualWidget.setText(numberFormat.format(rowItem.getCellDataMap().get(colKey).getActual()));
                }
            }
            budgetWidget.addValueChangeHandler(a -> calculateTable());
            widgetsMap.put(colKey + "_BUDGET", budgetWidget);
            widgetsMap.put(colKey + "_ACTUAL", actualWidget);
        }

        ProjectBudgetCellWidget totalBudget = new ProjectBudgetCellWidget(this);
        ActualLabelCellWidget totalActual = new ActualLabelCellWidget();
        ActualLabelCellWidget varianceAmount = new ActualLabelCellWidget();
        ActualLabelCellWidget variancePercent = new ActualLabelCellWidget();
        widgetsMap.put(Constants.TOTAL_BUDGET, totalBudget);
        widgetsMap.put(Constants.TOTAL_ACTUAL, totalActual);
        widgetsMap.put(Constants.VARIANCE_AMOUNT, varianceAmount);
        widgetsMap.put(Constants.VARIANCE_PERCENT, variancePercent);

        if (isEmployeeCost || isPurchases) {
            accountLookUp.setEnabled(false);
        }
        if (isEmployeeCost) {
            totalBudget.setEnabled(false);
        }

        applyTableRowData(rowItem, widgetsMap);

        accountLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            LoadingPanel.loading(true);
            ProjectService.App.get().getProjectBudgetRowDataByAccount(budgetSheetProvider.getProjectID(), accountLookUp.getSelectedItemID(),
                    budgetSheetProvider.getMonthIntervalsList(), type, new AsyncCallback<NewProjectBudgetRowItem>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void onSuccess(NewProjectBudgetRowItem result) {
                            for (int i = 1; i < columns.length; i++) {
                                CustomCell customCell = (CustomCell) cellTable.getColumnCellWidgetById(cellTable.getGrid().getCurrentRow(), columns[i].getName());
                                Widget widget = customCell.getCustomWidget();
                                if (widget instanceof ProjectBudgetCellWidget) {
                                    ((ProjectBudgetCellWidget) widget).setText(numberFormat.format(BigDecimal.ZERO));
                                } else if (widget instanceof ActualLabelCellWidget) {
                                    ((ActualLabelCellWidget) widget).setText(numberFormat.format(BigDecimal.ZERO));
                                }
                                customCell.InActive();
                            }
                            applyTableRowData(result, widgetsMap);
                            for (String colKey : budgetSheetProvider.getMonthColumnKeys()) {
                                ((CustomCell) cellTable.getColumnCellWidgetById(cellTable.getGrid().getCurrentRow(), colKey + "_ACTUAL")).InActive();
                            }
                            calculateTable();
                            budgetSheetProvider.calculateProjectProfit();
                            LoadingPanel.loading(false);
                        }
                    });
        });

        return widgetsMap.values().toArray(new Widget[]{});
    }

    private void applyTableRowData(NewProjectBudgetRowItem rowItem, LinkedHashMap<String, Widget> widgetsMap) {
        if (rowItem != null) {
            if (rowItem.getAccount() != null) {
                ((ProjectBudgetAccountLookUp)widgetsMap.get(Constants.PROJECT_BUDGET_ACCOUNT)).addItem(rowItem.getAccount());
            }

            for (String colKey : budgetSheetProvider.getMonthColumnKeys()) {
                if (rowItem.getCellDataMap().get(colKey) != null) {
                    if (rowItem.getCellDataMap().get(colKey).getBudget() != null) {
                        ((ProjectBudgetCellWidget) widgetsMap.get(colKey + "_BUDGET")).setText(numberFormat.format(rowItem.getCellDataMap().get(colKey).getBudget()));
                    }
                    if (rowItem.getCellDataMap().get(colKey).getActual() != null) {
                        ((ActualLabelCellWidget) widgetsMap.get(colKey + "_ACTUAL")).setText(numberFormat.format(rowItem.getCellDataMap().get(colKey).getActual()));
                    }
                }
            }

            if (rowItem.getCellDataMap().get(Constants.TOTAL_BUDGET) != null) {
                if (rowItem.getCellDataMap().get(Constants.TOTAL_BUDGET).getBudget() != null) {
                    ((ProjectBudgetCellWidget) widgetsMap.get(Constants.TOTAL_BUDGET)).setText(numberFormat.format(rowItem.getCellDataMap().get(Constants.TOTAL_BUDGET).getBudget()));
                }
                if (rowItem.getCellDataMap().get(Constants.TOTAL_BUDGET).getActual() != null) {
                    ((ActualLabelCellWidget)widgetsMap.get(Constants.TOTAL_ACTUAL)).setText(numberFormat.format(rowItem.getCellDataMap().get(Constants.TOTAL_BUDGET).getActual()));
                }
            }
        }
    }

    public void calculateRelatedFields() {
        Integer currentRow = cellTable.getGrid().getCurrentRow();
        String columnKey = columns[cellTable.getGrid().getCurrentColumn()].getName();
        BigDecimal totalRowBudgetAmount = BigDecimal.ZERO, totalColBudgetAmount = BigDecimal.ZERO, tableTotalBudgetAmount = BigDecimal.ZERO;

        //Calculate current column vertical
        for (int i = 0; i < cellTable.getGrid().getRowCount(); i++) {
            ProjectBudgetCellWidget cellWidget = (ProjectBudgetCellWidget) cellTable.getColumnById(i, columnKey);
            if (!"".equals(cellWidget.getText().trim())) {
                totalColBudgetAmount = totalColBudgetAmount.add(new BigDecimal(numberFormat.parse(cellWidget.getText())).setScale(calculationScale, BigDecimal.ROUND_HALF_UP));
            }
        }
        HTML columnTotalBudgetHTML = (HTML) totalTable.getWidget(0, cellTable.getGrid().getCurrentColumn());
        columnTotalBudgetHTML.setText(numberFormat.format(totalColBudgetAmount));


        if (!Constants.TOTAL_BUDGET.equals(columnKey)) {
            //Calculate current row horizontal
            for (String colKey : budgetSheetProvider.getMonthColumnKeys()) {
                ProjectBudgetCellWidget cellWidget = (ProjectBudgetCellWidget) cellTable.getColumnById(currentRow, colKey + "_BUDGET");
                if (!"".equals(cellWidget.getText().trim())) {
                    totalRowBudgetAmount = totalRowBudgetAmount.add(new BigDecimal(numberFormat.parse(cellWidget.getText())).setScale(calculationScale, BigDecimal.ROUND_HALF_UP));
                }
            }

            CustomCell totalCell = (CustomCell) cellTable.getColumnCellWidgetById(currentRow, Constants.TOTAL_BUDGET);
            ProjectBudgetCellWidget customCellWidget = (ProjectBudgetCellWidget) totalCell.getCustomWidget();
            customCellWidget.setText(numberFormat.format(totalRowBudgetAmount));
            totalCell.InActive();

            //Calculate total column vertical
            for (int i = 0; i < cellTable.getGrid().getRowCount(); i++) {
                ProjectBudgetCellWidget totalCellWidget = (ProjectBudgetCellWidget) cellTable.getColumnById(i, Constants.TOTAL_BUDGET);
                if (!"".equals(totalCellWidget.getText().trim())) {
                    tableTotalBudgetAmount = tableTotalBudgetAmount.add(new BigDecimal(numberFormat.parse(totalCellWidget.getText())).setScale(calculationScale, BigDecimal.ROUND_HALF_UP));
                }
            }
            HTML tableTotalBudgetHTML = (HTML) totalTable.getWidget(0, columns.length - 4);
            tableTotalBudgetHTML.setText(numberFormat.format(tableTotalBudgetAmount));
        }

        calculateVariance(currentRow);
        calculateTotalVariance();
        budgetSheetProvider.calculateProjectColumnProfit(cellTable.getGrid().getCurrentColumn());
    }

    private void calculateVariance(Integer currentRow) {
        ActualLabelCellWidget rowTotalActual = (ActualLabelCellWidget) cellTable.getColumnById(currentRow, Constants.TOTAL_ACTUAL);
        ProjectBudgetCellWidget rowTotalBudget = (ProjectBudgetCellWidget) cellTable.getColumnById(currentRow, Constants.TOTAL_BUDGET);

        BigDecimal rowTotalActualAmount = BigDecimal.ZERO, rowTotalBudgetAmount = BigDecimal.ZERO;
        if (!"".equals(rowTotalActual.getText().trim())) {
            rowTotalActualAmount = new BigDecimal(numberFormat.parse(rowTotalActual.getText().trim())).setScale(calculationScale, BigDecimal.ROUND_HALF_UP);
        }
        if (!"".equals(rowTotalBudget.getText().trim())) {
            rowTotalBudgetAmount = new BigDecimal(numberFormat.parse(rowTotalBudget.getText().trim())).setScale(calculationScale, BigDecimal.ROUND_HALF_UP);
        }
        CustomCell varianceAmountCell = (CustomCell) cellTable.getColumnCellWidgetById(currentRow, Constants.VARIANCE_AMOUNT);
        CustomCell variancePercentCell = (CustomCell) cellTable.getColumnCellWidgetById(currentRow, Constants.VARIANCE_PERCENT);
        ActualLabelCellWidget varianceAmountCellWidget = (ActualLabelCellWidget) varianceAmountCell.getCustomWidget();
        ActualLabelCellWidget variancePercentCellWidget = (ActualLabelCellWidget) variancePercentCell.getCustomWidget();

        varianceAmountCellWidget.setText(numberFormat.format(rowTotalActualAmount.subtract(rowTotalBudgetAmount)));
        if (rowTotalBudgetAmount.compareTo(BigDecimal.ZERO) > 0) {
            variancePercentCellWidget.setText(numberFormat.format(rowTotalActualAmount.subtract(rowTotalBudgetAmount).multiply(new BigDecimal(100)).divide(rowTotalBudgetAmount, calculationScale, BigDecimal.ROUND_HALF_UP)));
        } else {
            variancePercentCellWidget.setText(numberFormat.format(BigDecimal.ZERO));
        }
        varianceAmountCell.InActive();
        variancePercentCell.InActive();
    }

    private void calculateTotalVariance() {
        HTML tableTotalVarianceAmountHTML = (HTML) totalTable.getWidget(0, columns.length - 2);
        HTML tableTotalVariancePercentHTML = (HTML) totalTable.getWidget(0, columns.length - 1);

        BigDecimal totalBudgetAmount = new BigDecimal(numberFormat.parse(((HTML) totalTable.getWidget(0, columns.length - 4)).getText())).setScale(calculationScale, BigDecimal.ROUND_HALF_UP);
        BigDecimal totalActualAmount = new BigDecimal(numberFormat.parse(((HTML) totalTable.getWidget(0, columns.length - 3)).getText())).setScale(calculationScale, BigDecimal.ROUND_HALF_UP);
        tableTotalVarianceAmountHTML.setText(numberFormat.format(totalActualAmount.subtract(totalBudgetAmount)));
        if (totalBudgetAmount.compareTo(BigDecimal.ZERO) > 0) {
            tableTotalVariancePercentHTML.setText(numberFormat.format(totalActualAmount.subtract(totalBudgetAmount).multiply(new BigDecimal(100)).divide(totalBudgetAmount, calculationScale, BigDecimal.ROUND_HALF_UP)));
        } else {
            tableTotalVariancePercentHTML.setText(numberFormat.format(BigDecimal.ZERO));
        }
    }

    private void calculateTable() {

        //Calculate total actual by row
        for (int i = 0; i < cellTable.getGrid().getRowCount(); i++) {
            BigDecimal rowTotalActualAmount = BigDecimal.ZERO, rowTotalBudgetAmount = BigDecimal.ZERO;
            for (String colKey : budgetSheetProvider.getMonthColumnKeys()) {
                String actualTxt = ((ActualLabelCellWidget) cellTable.getColumnById(i, colKey + "_ACTUAL")).getText().trim();
                if(!"".equals(actualTxt)){
                    rowTotalActualAmount = rowTotalActualAmount.add(new BigDecimal(numberFormat.parse(actualTxt)).setScale(calculationScale, BigDecimal.ROUND_HALF_UP));
                }
                String budgetTxt = ((ProjectBudgetCellWidget) cellTable.getColumnById(i, colKey + "_BUDGET")).getText().trim();
                if(!"".equals(budgetTxt)){
                    rowTotalBudgetAmount = rowTotalBudgetAmount.add(new BigDecimal(numberFormat.parse(budgetTxt)).setScale(calculationScale, BigDecimal.ROUND_HALF_UP));
                }
            }
            CustomCell rowTotalActualCell = (CustomCell) cellTable.getColumnCellWidgetById(i, Constants.TOTAL_ACTUAL);
            ActualLabelCellWidget rowTotalActualHTML = (ActualLabelCellWidget) rowTotalActualCell.getCustomWidget();
            rowTotalActualHTML.setText(numberFormat.format(rowTotalActualAmount));
            rowTotalActualCell.InActive();
            CustomCell rowTotalBudgetCell = (CustomCell) cellTable.getColumnCellWidgetById(i, Constants.TOTAL_BUDGET);
            ProjectBudgetCellWidget rowTotalBudgetHTML = (ProjectBudgetCellWidget) rowTotalBudgetCell.getCustomWidget();
            rowTotalBudgetHTML.setText(numberFormat.format(rowTotalBudgetAmount));
            rowTotalBudgetCell.InActive();
        }

        //Calculate totals by column
        for (int col = 1; col < columns.length - 2; col++) {
            BigDecimal totalColAmount = BigDecimal.ZERO;
            for (int row = 0; row < cellTable.getGrid().getRowCount(); row++) {
                Widget widget = cellTable.getColumnById(row, columns[col].getName());
                String amountTxt = "";
                if (widget instanceof ProjectBudgetCellWidget) {
                    amountTxt = ((ProjectBudgetCellWidget) widget).getText().trim();
                } else if (widget instanceof ActualLabelCellWidget) {
                    amountTxt = ((ActualLabelCellWidget) widget).getText().trim();
                }
                if (!"".equals(amountTxt)) {
                    totalColAmount = totalColAmount.add(new BigDecimal(numberFormat.parse(amountTxt)).setScale(calculationScale, BigDecimal.ROUND_HALF_UP));
                }
            }
            HTML columnTotalHTML = (HTML) totalTable.getWidget(0, col);
            columnTotalHTML.setText(numberFormat.format(totalColAmount));
        }

        //Calculate variance by row
        for (int i = 0; i < cellTable.getGrid().getRowCount(); i++) {
            calculateVariance(i);
        }
        calculateTotalVariance();
    }

    public NewProjectBudgetRowItem[] getRowItems() {
        LinkedList<NewProjectBudgetRowItem> rowItemList = new LinkedList<>();
        for (int i = 0; i < cellTable.getGrid().getRowCount(); i++) {
            ProjectBudgetAccountLookUp accountLookUp = (ProjectBudgetAccountLookUp) cellTable.getColumnById(i, Constants.PROJECT_BUDGET_ACCOUNT);
            if (accountLookUp.getSelectedItem() != null && accountLookUp.getSelectedItem().getId() != -1) {

                NewProjectBudgetRowItem rowItem = new NewProjectBudgetRowItem();
                rowItem.setAccount(accountLookUp.getSelectedItem());
                for (String colKey : budgetSheetProvider.getMonthColumnKeys()) {
                    ProjectBudgetCellWidget customCellWidget = (ProjectBudgetCellWidget) cellTable.getColumnById(i, colKey + "_BUDGET");
                    NewProjectBudgetCellItem cellItem = new NewProjectBudgetCellItem();
                    String[] yearAndMonth = colKey.split("_");
                    cellItem.setYear(Integer.parseInt(yearAndMonth[0]));
                    cellItem.setMonth(Integer.parseInt(yearAndMonth[1]));
                    cellItem.setBudget((!"".equals(customCellWidget.getText().trim()) ? new BigDecimal(numberFormat.parse(customCellWidget.getText())).setScale(calculationScale, BigDecimal.ROUND_HALF_UP) : BigDecimal.ONE));
                    rowItem.getCellDataMap().put(colKey, cellItem);
                    //  Window.alert(customCellWidget.getText()+"   "+i+"  "+1+colKey);
                }

                ProjectBudgetCellWidget totalBudgetCellWidget = (ProjectBudgetCellWidget) cellTable.getColumnById(i, Constants.TOTAL_BUDGET);
                NewProjectBudgetCellItem cellItem = new NewProjectBudgetCellItem();
                cellItem.setTotal(true);
                cellItem.setBudget((!"".equals(totalBudgetCellWidget.getText().trim()) ? new BigDecimal(numberFormat.parse(totalBudgetCellWidget.getText())).setScale(calculationScale, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO));
                //  Window.alert(cellItem.getBudget().toString());
                rowItem.getCellDataMap().put(Constants.TOTAL_BUDGET, cellItem);
                // Window.alert(totalBudgetCellWidget.getText()+"   "+i+"    "+Constants.TOTAL_BUDGET);
                rowItemList.add(rowItem);
            }
        }

        return rowItemList.toArray(new NewProjectBudgetRowItem[]{});
    }

    public class ProjectBudgetCellWidget extends TextBox implements CustomCellInterface {

        public ProjectBudgetCellWidget(final ProjectBudgetTable parentTable) {
            super();
            setText(numberFormat.format(BigDecimal.ZERO));
            setAlignment(ValueBoxBase.TextAlignment.RIGHT);
            Validation.addNumericKeyboardListener(this, calculationScale);

            addKeyUpHandler(event -> parentTable.calculateRelatedFields());
        }

        @Override
        public String getDisplayValue() {
            return getText();
        }

        @Override
        public void setItemValue(Object value) {
            setText(String.valueOf(value));
        }

        @Override
        public void setItemFocus(boolean focused) {
            setFocus(focused);
        }
    }

    public class ActualLabelCellWidget extends HTML implements CustomCellInterface {

        public ActualLabelCellWidget() {
            super();
            setText(numberFormat.format(BigDecimal.ZERO));
        }

        @Override
        public String getDisplayValue() {
            return numberFormat.format(!"".equals(getText().trim()) ? numberFormat.parse(getText()) : BigDecimal.ZERO);
        }

        @Override
        public void setItemValue(Object value) {
           setText((String) value);
        }

        @Override
        public void setItemFocus(boolean focused) {

        }
    }

    public EditableTable getCellTable() {
        return cellTable;
    }

    public FlexTable getTotalTable() {
        return totalTable;
    }

    public void setAddNewLineEnabled(Boolean addNewLineEnabled) {
        this.addNewLineEnabled = addNewLineEnabled;
    }
}
