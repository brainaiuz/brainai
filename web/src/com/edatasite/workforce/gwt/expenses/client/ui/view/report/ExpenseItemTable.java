package com.edatasite.workforce.gwt.expenses.client.ui.view.report;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiTextArea;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.TaxLookUp;
import com.edatasite.workforce.gwt.expenses.client.localization.ExpenseMessages;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseListItem;
import com.edatasite.workforce.gwt.expenses.client.ui.ExpenseConstants;
import com.edatasite.workforce.gwt.expenses.client.ui.ItemUploadForm;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartAccountLookUpForExpense;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldLookUpField;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class ExpenseItemTable extends EditableTable implements ExpenseConstants {
    private static final DateTimeFormat dateFormat = DateTimeFormat.getFormat("MM/yyyy");
    private static final ExpenseMessages expenseMessages = ExpenseMessages.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    public boolean isDepartmentRelationEnabled = AccountingUtils.get().isEnableAccountingDepartmentRelation();
    private boolean isDoubleTaxEnabled;
    private final boolean canApprove = Utils.hasPermission(PermissionConstants.ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM) || Utils.hasPermission(PermissionConstants.HRMS_CAN_APPROVE_EXPENSE_CLAIM);
    private final boolean canRelateToProject = Utils.hasPermission(PermissionConstants.EXPENSE_ADD_VIEW_FULL_ACCESS) || Utils.hasPermission(PermissionConstants.HRMS_EXPENSE_ADD_VIEW_FULL_ACCESS);
    private final boolean canAddCategory = Utils.hasPermission(PermissionConstants.ACCOUNTING_EXPENSE_CLAIM_ADD_CATEGORY);

    private boolean isCompanyExpense;
    private final ProvideExpenseItemTableDependencies provider;
    private Command removeRow;
    private KpiCheckBox reverseChargeBox;

    public ExpenseItemTable(ProvideExpenseItemTableDependencies provider) {
        super(provider.getColumns(), true, true, false);
        this.provider = provider;
        setDraggable(true);
        initItemsTable();
    }

    private void initItemsTable() {
        setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                Widget[] widgets = getWidgetArray();
                ExpenseItemTable.super.addRow(widgets);
            }

            @Override
            public void removeRow() {
                if (removeRow != null) {
                    removeRow.execute();
                }
            }
        });
    }

    public void setRemoveRowCommand(Command removeRow) {
        this.removeRow = removeRow;
    }

    public void setReverseChargeBox(KpiCheckBox chargeBox) {
        this.reverseChargeBox = chargeBox;
    }

    private Widget[] getWidgetArray() {
        HashMap<String, Widget> widgetsMap = provider.getWidgetsMap();
        Widget[] widgets = widgetsMap.values().toArray(new Widget[]{});
        return widgets;
    }


    public void clearProjectFromLookUp() {
        if (Utils.isProjectInLineItemEnable()) {
            for (int i = 0; i < getRowCount(); i++) {
                ProjectLookUp project = (ProjectLookUp) getColumnById(i, ExpenseConstants.PROJECT_LIST);
                project.clear();
            }
        }
    }

    public void onExchangeRateChange(boolean calculateTotal) {
        for (int rowId = 0; rowId < getRowCount(); rowId++) {
            ExpenseAddEditView.UnitPriceTextBox units = (ExpenseAddEditView.UnitPriceTextBox) getColumnById(rowId, UNITS);
            ExpenseAddEditView.ExtendedTextBox cost = (ExpenseAddEditView.ExtendedTextBox) getColumnById(rowId, COST);

            BigDecimal unitCostInBase = cost.getCostAmountInBase();
            BigDecimal unitCost = BigDecimal.ZERO;

            if (units.getSubtotalCalculator() != null) {
                units.getSubtotalCalculator().setExchangeRateValue(provider.getCurrencyWidget().getExchangeRate());

                if (provider.getCurrencyWidget().getExchangeRate().compareTo(BigDecimal.ZERO) != 0) {
                    unitCost = unitCostInBase.multiply(provider.getCurrencyWidget().getExchangeRate()).setScale(AccountingUtils.customUnitPriceScale, RoundingMode.HALF_UP);
                }
                units.getSubtotalCalculator().setUnitCost(unitCost);
                units.getSubtotalCalculator().calculate(calculateTotal);
            }
        }
    }

    public boolean validate() {
        int errors = 0;
        for (int i = 0; i < getRowCount(); i++) {
            if (!validateRow(i)) {
                errors++;
            }
        }
        if (getValidRows() == 0) {
            notValid(0, UNITS);
            notValid(0, COST);
            return false;
        }
        return errors == 0;
    }

    public ExpenseListItem[] getExpenseList(String status) {
        LinkedList<ExpenseListItem> result = new LinkedList<>();
        boolean isNotDraft = !Constants.EXPENSE_DRAFT.equals(status);
        for (int i = 0; i < getRowCount(); i++) {
            if (isNotDraft && !validateRequireds(i)) {
                continue;
            }
            ExpenseListItem temp = getExpense(i);
            result.add(temp);
            if (provider.getProjectLookUp() != null && !Utils.isProjectInLineItemEnable() && Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
                temp.setProject(provider.getProjectLookUp().getSelectedItem());
            }
        }
        return result.toArray(new ExpenseListItem[]{});
    }

    public void resetValidation() {
        for (int i = 0; i < getRowCount(); i++) {
            super.resetValidation(i);
        }
    }

    private boolean validateRow(int rowId) {
        if (!validateRequireds(rowId)) {
            return true;
        }

        int errors = 0;

        validateFields(rowId);
        if (canAddCategory || canApprove || canRelateToProject) {
            if (getColumnById(rowId, ACCOUNT_LIST) instanceof SmartAccountLookUpForExpense) {
                SmartAccountLookUpForExpense category = (SmartAccountLookUpForExpense) getColumnById(rowId, ACCOUNT_LIST);

                if (category.getSelectedItem() == null) {
                    notValid(rowId, ACCOUNT_LIST);
                    errors++;
                }
            }
        }
        if (provider != null && provider.getColumns() != null && provider.getColumns().length > 0) {
            for (ColumnConfig columnConfig : provider.getColumns()) {
                if (columnConfig != null) {
                    switch (columnConfig.getName()) {
                        case ExpenseConstants.DESCRIPTION:
                            if (columnConfig.isRequired() && (!isCompanyExpense && !canApprove && !canRelateToProject)) {
                                KpiTextArea description = (KpiTextArea) getColumnById(rowId, DESCRIPTION);

                                if (description.getText() == null || description.getText().isEmpty()) {
                                    notValid(rowId, DESCRIPTION);
                                    errors++;
                                }
                            }
                            break;
                        case RECEIPTS_PANEL:
                            if (columnConfig.isRequired()) {
                                ItemUploadForm uploadPanel = (ItemUploadForm) getColumnById(rowId, RECEIPTS_PANEL);
                                if (uploadPanel.getAttachedFiles() == null || uploadPanel.getAttachedFiles().length == 0) {
                                    notValid(rowId, RECEIPTS_PANEL);
                                    errors++;
                                }
                            }
                            break;
                        case DEPARTMENT_LIST:
                            if (columnConfig.isRequired() && (isCompanyExpense || canApprove || canRelateToProject)) {
                                DepartmentLookUp departmentLookUp = (DepartmentLookUp) getColumnById(rowId, ExpenseConstants.DEPARTMENT_LIST);
                                if (!Validation.validateLookUpRequired(departmentLookUp)) {
                                    notValid(rowId, DEPARTMENT_LIST);
                                    errors++;
                                }
                            }
                            break;
                        case PROJECT_LIST:
                            if (columnConfig.isRequired() && (isCompanyExpense || canApprove || canRelateToProject)) {
                                ProjectLookUp projectLookUp = (ProjectLookUp) getColumnById(rowId, ExpenseConstants.PROJECT_LIST);
                                if (!Validation.validateLookUpRequired(projectLookUp)) {
                                    notValid(rowId, PROJECT_LIST);
                                    errors++;
                                }
                            }
                            break;
                        case PO_LIST:
                            if (columnConfig.isRequired() && (isCompanyExpense || canApprove || canRelateToProject)) {
                                PurchaseOrderLookUp poLookUp = (PurchaseOrderLookUp) getColumnById(rowId, ExpenseConstants.PO_LIST);
                                if (!Validation.validateLookUpRequired(poLookUp)) {
                                    notValid(rowId, PO_LIST);
                                    errors++;
                                }
                            }
                            break;
                        case DOUBLE_TAX:
                            if (columnConfig.isRequired() && isDoubleTaxEnabled && (isCompanyExpense || canApprove || canRelateToProject)) {
                                ExpenseAddEditView.ExtendedTaxLookUp doubleTaxLookUp = (ExpenseAddEditView.ExtendedTaxLookUp) getColumnById(rowId, DOUBLE_TAX);
                                if (!Validation.validateLookUpRequired(doubleTaxLookUp)) {
                                    notValid(rowId, DOUBLE_TAX);
                                    errors++;
                                }
                            }
                            break;
                        case CUSTOMER_LIST:
                            if (columnConfig.isRequired() && (isCompanyExpense || canApprove || canRelateToProject)) {
                                CrmAccountLookUp client = (CrmAccountLookUp) getColumnById(rowId, CUSTOMER_LIST);
                                if (!Validation.validateLookUpRequired(client)) {
                                    notValid(rowId, CUSTOMER_LIST);
                                    errors++;
                                }
                            }
                            break;
                        case TAX_LIST:
                            if (columnConfig.isRequired() && (isCompanyExpense || canApprove || canRelateToProject)) {
                                ExpenseAddEditView.ExtendedTaxLookUp taxLookUp = (ExpenseAddEditView.ExtendedTaxLookUp) getColumnById(rowId, TAX_LIST);
                                if (!Validation.validateLookUpRequired(taxLookUp)) {
                                    notValid(rowId, TAX_LIST);
                                    errors++;
                                }
                            }
                            break;
                        case MARKUP_AMOUNT:
                            if (columnConfig.isRequired() && (isCompanyExpense || canApprove || canRelateToProject)) {
                                TextBox markup = (TextBox) getColumnById(rowId, MARKUP_AMOUNT);

                                if (!Validation.validateTextBoxRequired(markup)) {
                                    notValid(rowId, MARKUP_AMOUNT);
                                    errors++;
                                }
                            }
                            break;
                    }
                }
            }
        } else {
            if (!isCompanyExpense && !canApprove && !canRelateToProject) {
                KpiTextArea description = (KpiTextArea) getColumnById(rowId, DESCRIPTION);

                if (description.getText() == null || description.getText().isEmpty()) {
                    notValid(rowId, DESCRIPTION);
                    errors++;
                }
            }

            if (isDepartmentRelationEnabled && !Utils.hasPermission(PermissionConstants.SKIP_DEPARTMENT_ITEM_VALIDATION)) {
                DepartmentLookUp departmentLookUp = (DepartmentLookUp) getColumnById(rowId, DEPARTMENT_LIST);

                if (departmentLookUp != null && departmentLookUp.getSelectedItemID() == null) {
                    notValid(rowId, DEPARTMENT_LIST);
                    errors++;
                }
            }
        }

        boolean valid = errors == 0;
        setItemValid(rowId, valid);
        incValidRow();
        return valid;
    }

    private boolean validateRequireds(int rowId) {

        TextBox units = (TextBox) getColumnById(rowId, UNITS);
        TextBox cost = (TextBox) getColumnById(rowId, COST);

        return validateCalculatable(units) && validateCalculatable(cost);
    }

    public void applyTaxCalculationTypeChange(Integer taxCalculationType, Boolean calculate) {
        if (AccountingConstants.NO_TAX_CALCULATION.equals(taxCalculationType)) {
            for (int rowId = 0; rowId < getRowCount(); rowId++) {
                ExpenseAddEditView.UnitPriceTextBox units = (ExpenseAddEditView.UnitPriceTextBox) getColumnById(rowId, UNITS);
                TaxLookUp taxLookUp = (TaxLookUp) getColumnById(rowId, TAX_LIST);

                if (taxLookUp != null) {
                    taxLookUp.clear();
                    taxLookUp.setEnabled(false);
                }
                if (isDoubleTaxEnabled) {
                    TaxLookUp doubleTaxLookUp = (TaxLookUp) getColumnById(rowId, DOUBLE_TAX);
                    if (doubleTaxLookUp != null) {
                        doubleTaxLookUp.clear();
                        doubleTaxLookUp.setEnabled(false);
                    }
                }

                BigDecimal exRateToCalculate = provider.getCurrencyWidget().getExchangeRate();
                if (units.getSubtotalCalculator() != null) {
                    units.getSubtotalCalculator().setExchangeRateValue(exRateToCalculate);
                    units.getSubtotalCalculator().setTaxCalculationType(taxCalculationType, calculate);
                }
            }
        } else {
            for (int rowId = 0; rowId < getRowCount(); rowId++) {
                ExpenseAddEditView.UnitPriceTextBox units = (ExpenseAddEditView.UnitPriceTextBox) getColumnById(rowId, UNITS);
                TaxLookUp taxLookUp = (TaxLookUp) getColumnById(rowId, TAX_LIST);

                if (taxLookUp != null) {
                    taxLookUp.setEnabled(true);
                }
                if (isDoubleTaxEnabled) {
                    TaxLookUp doubleTaxLookUp = (TaxLookUp) getColumnById(rowId, DOUBLE_TAX);
                    if (doubleTaxLookUp != null) {
                        doubleTaxLookUp.setEnabled(true);
                    }
                }

                BigDecimal exRateToCalculate = provider.getCurrencyWidget().getExchangeRate();
                if (units.getSubtotalCalculator() != null) {
                    units.getSubtotalCalculator().setExchangeRateValue(exRateToCalculate);
                    units.getSubtotalCalculator().setTaxCalculationType(taxCalculationType, calculate);
                }
            }
        }
    }

    public void clearSelectedTaxFromItems(boolean disableTaxField) {
        for (int rowId = 0; rowId < getRowCount(); rowId++) {
            LookUpCell lookUpCell = (LookUpCell) getColumnCellWidgetById(rowId, TAX_LIST);
            ExpenseAddEditView.UnitPriceTextBox units = (ExpenseAddEditView.UnitPriceTextBox) getColumnById(rowId, UNITS);

            if (lookUpCell != null) {
                TaxLookUp tax = (TaxLookUp) lookUpCell.getLookUp();
                tax.clear();
                tax.setEnabled(!disableTaxField);

                tax.setExcludeExempt(reverseChargeBox != null && reverseChargeBox.getValue());
                lookUpCell.InActive();
            }

            if (isDoubleTaxEnabled) {
                LookUpCell doubleLookUpCell = (LookUpCell) getColumnCellWidgetById(rowId, DOUBLE_TAX);

                if (doubleLookUpCell != null) {
                    TaxLookUp doubleTax = (TaxLookUp) doubleLookUpCell.getLookUp();
                    doubleTax.clear();
                    doubleTax.setEnabled(!disableTaxField);
                    doubleLookUpCell.InActive();
                }
            }
            units.getSubtotalCalculator().calculate(true);
        }
    }

    public void recalculate() {
        for (int rowId = 0; rowId < getRowCount(); rowId++) {
            ExpenseAddEditView.UnitPriceTextBox units = (ExpenseAddEditView.UnitPriceTextBox) getColumnById(rowId, UNITS);
            units.getSubtotalCalculator().calculate(true);
        }
    }

    public void setDoubleTaxEnabled(boolean isDoubleTaxEnabled) {
        this.isDoubleTaxEnabled = isDoubleTaxEnabled;
    }

    public void addRow() {
        addRow(getWidgetArray());
    }

    public void fillDynamicTable(ExpenseListItem[] expenses) {
        removeAllRows();
        if (expenses == null || expenses.length == 0) {
            addMissingRows(3);
            return;
        }
        int countRow = 0;
        for (ExpenseListItem expenseItem : expenses) {
            HashMap<String, Widget> itemWidgetsMap = provider.getWidgetsMap();

            KpiTextArea description = (KpiTextArea) itemWidgetsMap.get(DESCRIPTION);
            if (description != null) {
                description.setLayoutData(expenseItem.getId());
            }
            ExpenseAddEditView.UnitPriceTextBox units = (ExpenseAddEditView.UnitPriceTextBox) itemWidgetsMap.get(UNITS);
            ExpenseAddEditView.ExtendedTextBox cost = (ExpenseAddEditView.ExtendedTextBox) itemWidgetsMap.get(COST);
            ItemUploadForm uploadPanel = (ItemUploadForm) itemWidgetsMap.get(RECEIPTS_PANEL);
            Label total = (Label) itemWidgetsMap.get(TOTAL);

//            if (provider.getExpenseReportData().getPurchaseOrder() != null) {
//                units.setEnabled(false);
//                cost.setEnabled(false);
//            }

            if (description != null) {
                description.setText(expenseItem.getDescription());
                description.setEntryIds(expenseItem.getProjectBasedEntryIds());
            }

            if (expenseItem.getUnits() != null && units != null) {
                if (expenseItem.isProjectBase()) {
                    units.setText(Utils.formatMinutes(expenseItem.getUnits().multiply(new BigDecimal(60)).setScale(0, RoundingMode.HALF_UP).intValue()));
                    units.setEnabled(false);
                } else {
                    units.setText(AccountingUtils.get().format(expenseItem.getUnits()));
                }
            }
            if (expenseItem.getCostPerUnit() != null && cost != null) {
                cost.setText(AccountingUtils.get().formatUnitPrice(expenseItem.getCostPerUnit()));
                cost.setCostAmountInBase(expenseItem.getCostPerUnit().divide(provider.getExpenseReportData().getExchangeRate(), 10, RoundingMode.HALF_UP));
            }

            if (isCompanyExpense || canApprove || canRelateToProject) {
                ExpenseAddEditView.ExtendedTaxLookUp taxLookUp = (ExpenseAddEditView.ExtendedTaxLookUp) itemWidgetsMap.get(TAX_LIST);
                ExpenseAddEditView.ExtendedTaxLookUp doubleTaxLookUp = (ExpenseAddEditView.ExtendedTaxLookUp) itemWidgetsMap.get(DOUBLE_TAX);

                if (provider.getExpenseReportData().getPurchaseOrder() != null) {
                    if (taxLookUp != null) {
                        taxLookUp.setEnabled(false);
                    }
                    if (doubleTaxLookUp != null) {
                        doubleTaxLookUp.setEnabled(false);
                    }
                }
                TextBox markup = (TextBox) itemWidgetsMap.get(MARKUP_AMOUNT);
                CrmAccountLookUp client = (CrmAccountLookUp) itemWidgetsMap.get(CUSTOMER_LIST);
                DepartmentLookUp departmentLookUp = (DepartmentLookUp) itemWidgetsMap.get(ExpenseConstants.DEPARTMENT_LIST);
                ProjectLookUp projectLookUp = (ProjectLookUp) itemWidgetsMap.get(ExpenseConstants.PROJECT_LIST);
                PurchaseOrderLookUp poLookUp = (PurchaseOrderLookUp) itemWidgetsMap.get(ExpenseConstants.PO_LIST);
                SmartAccountLookUpForExpense category = (SmartAccountLookUpForExpense) itemWidgetsMap.get(ACCOUNT_LIST);

                if (expenseItem.getAccountId() != null) {
                    category.setSelected(new SelectItem(expenseItem.getAccountId(), expenseItem.getAccountName()));
                }
                if (taxLookUp != null && expenseItem.getTax() != null) {
                    taxLookUp.addTaxItem(expenseItem.getTax());
                    taxLookUp.setTaxAmountInBase(expenseItem.getTaxAmountInBase());
                    taxLookUp.setTaxAmountInTc(expenseItem.getTaxAmountInTc());
                }
                if (doubleTaxLookUp != null && expenseItem.getDoubleTax() != null) {
                    doubleTaxLookUp.addTaxItem(expenseItem.getDoubleTax());
                    doubleTaxLookUp.setTaxAmountInBase(expenseItem.getDoubleTaxAmountInBase());
                }

                if (client != null && expenseItem.getClientId() != null) {
                    client.setSelected(new SelectItem(expenseItem.getClientId(), expenseItem.getClientName()));
                }
                if (departmentLookUp != null && expenseItem.getDepartment() != null) {
                    departmentLookUp.addItem(expenseItem.getDepartment());
                }
                if (projectLookUp != null && expenseItem.getProject() != null) {
                    projectLookUp.addItem(expenseItem.getProject());
                }
                if (poLookUp != null && expenseItem.getPurchaseOrder() != null) {
                    poLookUp.addItem(expenseItem.getPurchaseOrder());
                }
                if (markup != null && expenseItem.getMarkupAmount() != null) {
                    markup.setText(AccountingUtils.get().formatPrice(expenseItem.getMarkupAmount()));
                }
            }


            units.getSubtotalCalculator().setExchangeRateValue(provider.getExpenseReportData().getExchangeRate());

            if (expenseItem.getCustomFieldItems() != null && !expenseItem.getCustomFieldItems().isEmpty()) {
                for (String column : provider.getCustomFieldsMap().keySet()) {
                    if (itemWidgetsMap.get(column) != null) {
                        if (itemWidgetsMap.get(column) instanceof ExpenseAddEditView.CustomFieldInterface) {
                            ((ExpenseAddEditView.CustomFieldInterface) itemWidgetsMap.get(column)).setFieldItem(expenseItem.getCustomFieldByCode(column));
                        } else {
                            ((CustomFieldInterface) itemWidgetsMap.get(column)).setFieldItem(expenseItem.getCustomFieldByCode(column));
                        }
                    }
                }
            }

            if (uploadPanel != null) {
                uploadPanel.setFiles(expenseItem.getAttachments());
            }
            addRow(itemWidgetsMap.values().toArray(new Widget[]{}));
            countRow++;
        }
        if (countRow < 3) {
            addMissingRows(3 - countRow);
        }

    }

    private void addMissingRows(int missingRows) {
        for (int i = 0; i < missingRows; i++) {
            addRow();
        }
    }

    public void setCompanyExpense(boolean value) {
        this.isCompanyExpense = value;
    }

    private ExpenseListItem getExpense(int rowId) throws IllegalArgumentException {
        ExpenseListItem expenseItem = new ExpenseListItem();
        KpiTextArea description = (KpiTextArea) getColumnById(rowId, DESCRIPTION);
        if (description != null && provider.getFormParams().getExternalObjectID() == null) {
            expenseItem.setId((Integer) description.getLayoutData());
        }

        TextBox units = (TextBox) getColumnById(rowId, UNITS);
        TextBox cost = (TextBox) getColumnById(rowId, COST);

        Label total = (Label) getColumnById(rowId, TOTAL);
        Label baseSubtotal = (Label) getColumnById(rowId, BASE_SUBTOTAL);
        ItemUploadForm uploadPanel = (ItemUploadForm) getColumnById(rowId, RECEIPTS_PANEL);

        if (canAddCategory || canApprove || canRelateToProject) {
            SmartAccountLookUpForExpense category = (SmartAccountLookUpForExpense) getColumnById(rowId, ACCOUNT_LIST);
            expenseItem.setCategoryId(category.getSelectedItemID());
            expenseItem.setAccountId(category.getSelectedItemID());
            expenseItem.setCategoryName(category.getText());
        }
        if (description != null) {
            expenseItem.setDescription(description.getText());
            expenseItem.setProjectBasedEntryIds(description.getEntryIds());
        }
        expenseItem.setUnits(getUnits(units.getText()));
        expenseItem.setCostPerUnit(AccountingUtils.get().parseToBigDecimal(cost.getText()));
        expenseItem.setProjectBase(units.getText().contains(":"));

        expenseItem.setSubtotal(AccountingUtils.get().parseToBigDecimal(total.getText()));
        expenseItem.setBaseSubtotal(AccountingUtils.get().parseToBigDecimal(baseSubtotal != null ? baseSubtotal.getText() : ""));

        if (uploadPanel != null) {
            expenseItem.setAttachments(uploadPanel.getAttachedFiles());
        }

        if (isCompanyExpense || canApprove || canRelateToProject) {
            DepartmentLookUp departmentLookUp = (DepartmentLookUp) getColumnById(rowId, ExpenseConstants.DEPARTMENT_LIST);
            ProjectLookUp projectLookUp = (ProjectLookUp) getColumnById(rowId, ExpenseConstants.PROJECT_LIST);
            PurchaseOrderLookUp poLookUp = (PurchaseOrderLookUp) getColumnById(rowId, ExpenseConstants.PO_LIST);

            if (departmentLookUp != null) {
                expenseItem.setDepartment(departmentLookUp.getSelectedItem());
            }
            if (projectLookUp != null) {
                expenseItem.setProject(projectLookUp.getSelectedItem());
            }
            if (poLookUp != null) {
                expenseItem.setPurchaseOrder(poLookUp.getSelectedItem());
            }
            if (isDoubleTaxEnabled) {
                ExpenseAddEditView.ExtendedTaxLookUp doubleTaxLookUp = (ExpenseAddEditView.ExtendedTaxLookUp) getColumnById(rowId, DOUBLE_TAX);

                if (doubleTaxLookUp != null) {
                    expenseItem.setDoubleTax(doubleTaxLookUp.getSelectedData());
                    expenseItem.setDoubleTaxAmountInBase(doubleTaxLookUp.getTaxAmountInBase());
                }
            }

            CrmAccountLookUp client = (CrmAccountLookUp) getColumnById(rowId, CUSTOMER_LIST);

            if (client != null) {
                expenseItem.setClientId(client.getSelectedItemID());
                expenseItem.setClientName(client.getText());
            }

            ExpenseAddEditView.ExtendedTaxLookUp taxLookUp = (ExpenseAddEditView.ExtendedTaxLookUp) getColumnById(rowId, TAX_LIST);

            if (taxLookUp != null) {
                expenseItem.setTax(taxLookUp.getSelectedData());
                expenseItem.setTaxAmountInBase(taxLookUp.getTaxAmountInBase());
                expenseItem.setTaxAmountInTc(taxLookUp.getTaxAmountInTc());
            }

            TextBox markup = (TextBox) getColumnById(rowId, MARKUP_AMOUNT);

            if (markup != null) {
                expenseItem.setMarkupAmount(AccountingUtils.get().parseToBigDecimal(markup.getText()));
            }
        }

        //initialize custom fields data
        if (provider.getCustomFieldsMap() != null && !provider.getCustomFieldsMap().isEmpty()) {
            ArrayList<CompanyCustomFieldItem> fieldItems = new ArrayList<>();

            for (String key : provider.getCustomFieldsMap().keySet()) {
                if (getColumnById(rowId, key) instanceof CustomFieldLookUpField) {
                    CustomFieldLookUpField customFieldLookUpField = (CustomFieldLookUpField) getColumnById(rowId, key);
                    fieldItems.add(customFieldLookUpField.getFieldItem());
                } else {
                    ExpenseAddEditView.CustomFieldInterface customField = (ExpenseAddEditView.CustomFieldInterface) getColumnById(rowId, key);
                    if (customField != null) {
                        fieldItems.add(customField.getFieldItem());
                    }
                }
            }

            if (!fieldItems.isEmpty()) {
                expenseItem.setCustomFieldItems(fieldItems);
            }
        }
        return expenseItem;
    }

    private BigDecimal getUnits(String text) {
        if (wfmStrings.notAvailable().equals(text)) {
            return BigDecimal.ONE;
        }
        return (text.indexOf(':') == -1 ? AccountingUtils.get().parseToBigDecimal(text) : AccountingUtils.get().parseToBigDecimal(getHourValue(text)));
    }

    private String getHourValue(String text) {
        String[] time = text.split(":");
        return (Double.parseDouble(time[0]) + Double.parseDouble(time[1]) / 60) + "";
    }

    private boolean validateCalculatable(TextBox textBox) {
        if (textBox.getText() == null || "".equals(textBox.getText())) {
            return false;
        } else return AccountingUtils.get().parseToBigDecimal(textBox.getText()).compareTo(BigDecimal.ZERO) != 0;

    }

}
