package com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.widgets;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.TotalTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTextBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LinkableCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.CategoryLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.PayslipItemAmountWidget;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunItem;
import com.edatasite.workforce.gwt.payroll.client.ui.PayrollContants;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import org.gwt.advanced.client.ui.widget.EditableGrid;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Murad Satimov
 * Date: 3/27/18 1:35 AM
 */
public class CustomPaymentCell extends BaseAdditionlCell {
    private boolean fromPayment = false;
    private BigDecimal salary;
    private ArrayList<PaymentDeductionObject> categories = new ArrayList<>();

    public CustomPaymentCell(SinglePayrunItem item, EditableGrid grid, boolean editable, boolean fromPayment) {
        super(item);
        this.grid = grid;
        this.editable = editable;
        this.fromPayment = fromPayment;
        this.initData(item);
    }

    private void initData(SinglePayrunItem item) {
        if (item == null) {
            return;
        }
        this.salary = item.getBasicSalary();
        this.totalValue = this.fromPayment ? item.getAllowance() : item.getDeduction();
        this.categories = this.fromPayment ? item.getPaymentCategories() : item.getDeductionCategories();
    }

    @Override
    protected void drawPopup() {
        categoriesDialogBox = new KpiModal();
        categoriesDialogBox.setWidth(600);
        categoriesTable = new EditableTable(getColumns(), this.editable);
        paymentTotal = new HTML();

        categoriesTable.setRemoveRowListener(() -> {
            categoriesTable.getGrid().getModel().removeRow(categoriesTable.getGrid().getCurrentRow());
            applyTotal(categoriesTable, paymentTotal);
        });
        categoriesTable.setRemoveAllRows(true);
        if (fromPayment) {
            categoriesDialogBox.setTitle(payrollStrings.allowanceDetails());
        } else {
            categoriesDialogBox.setTitle(wfmStrings.deductionDetails());
        }
        for (PaymentDeductionObject object : categories) {
            if (!object.isSalaryObject()) {
                addItem(object, paymentTotal);
            }
        }
        recalculate();
        categoriesDialogBox.add(categoriesTable);

        applyTotal(categoriesTable, paymentTotal);

        TotalTable totalTable = new TotalTable();
        totalTable.addItem(new Label(wfmStrings.paymentTotal()), paymentTotal);
        categoriesDialogBox.add(totalTable);

        if (!editable) {
            WfmButton2 close = new WfmButton2(wfmStrings.close());

            close.addClickHandler(clickEvent -> categoriesDialogBox.close());
            categoriesDialogBox.addButton(close);
        } else {
            WfmButton2 apply = new WfmButton2(wfmStrings.apply());
            apply.addClickHandler(clickEvent -> {
                if (isValid()) {
                    recalculate();
                    saveAndApplyData();
                    categoriesDialogBox.close();
                }
            });
            WfmButton2 addNew = new WfmButton2(wfmStrings.addNew());
            addNew.addClickHandler(clickEvent -> {
                for (int rowId = 0; rowId < categoriesTable.getRowCount(); ) {
                    final CategoryLookUp categoryLookUp = (CategoryLookUp) categoriesTable.getColumnById(rowId, "category");

                    if (categoryLookUp == null || categoryLookUp.getSelectedData() == null) {
                        categoriesTable.getGrid().getModel().removeRow(rowId);
                        rowId = 0;
                    } else {
                        rowId++;
                    }
                }
                addItem(null, this.paymentTotal);
            });
            categoriesDialogBox.addButton(addNew);
            categoriesDialogBox.addButton(apply);
        }
    }

    private void saveAndApplyData() {
        if (saveHandler == null || !editable) {
            return;
        }
        if (fromPayment) {
            item.setAllowance(totalValue);
            item.setPaymentCategories(getCategories());
        } else {
            item.setDeduction(totalValue);
            item.setDeductionCategories(getCategories());
        }
        CustomPaymentCell allowanceCell = (CustomPaymentCell) table.getColumnById(grid.getCurrentRow(), PayrollContants.ALLOWANCE);
        CustomPaymentCell deductioncell = (CustomPaymentCell) table.getColumnById(grid.getCurrentRow(), PayrollContants.DEDUCTION);
        CustomExpenseCell expenseCell = (CustomExpenseCell) table.getColumnById(grid.getCurrentRow(), PayrollContants.EXPENSE);
        PayslipItemAmountWidget pensionCell = (PayslipItemAmountWidget) table.getColumnById(grid.getCurrentRow(), PayrollContants.PENSION);
        BigDecimal total = item.getBasicSalary();

        total = total.add(allowanceCell.getTotalValue());
        total = total.add(expenseCell.getPayTotal());
        total = total.subtract(expenseCell.getDedTotal());
        total = total.subtract(deductioncell.getTotalValue());
        if (pensionCell != null && pensionCell.getAmount() != null) {
            total = total.subtract(pensionCell.getAmount());
        }
        item.setTotal(total);
        saveHandler.accept(grid.getCurrentRow(), item);
    }

    private void recalculate() {
        totalValue = BigDecimal.ZERO;

        for (int i = 0; i < categoriesTable.getRowCount(); i++) {
            PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) categoriesTable.getColumnById(i, "amount");
            if (amountWidget != null) {
                totalValue = totalValue.add(amountWidget.getAmount());
            }

            LinkableCell cell = (LinkableCell) table.getColumnCellWidgetById(grid.getCurrentRow(), fromPayment ? PayrollContants.ALLOWANCE : PayrollContants.DEDUCTION);
            if (cell != null) {
                cell.displayActive(false);
            }
        }
    }

    private boolean isValid() {
        int errors = 0;
        if (this.categoriesTable != null) {
            for (int i = 0; i < categoriesTable.getRowCount(); i++) {
                CategoryLookUp categoryLookUp = (CategoryLookUp) categoriesTable.getColumnById(i, "category");
                PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) categoriesTable.getColumnById(i, "amount");
                if (categoryLookUp.getSelectedData() == null) {
                    errors++;
                }
                if (amountWidget.getAmountTextBox() == null || amountWidget.getAmountTextBox().getText().isEmpty() || BigDecimal.ZERO.compareTo(amountWidget.getAmount()) == 1) {
                    errors++;
                }
            }
        } else {
            for (PaymentDeductionObject obj : this.categories) {
                if (BigDecimal.ZERO.compareTo(obj.getPaymentAmount()) > 0) {
                    errors++;
                }
            }
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    @Override
    protected ColumnConfig[] getColumns() {
        String title = fromPayment ? payrollStrings.paymentCategories() : payrollStrings.deductionCategories();

        return new ColumnConfig[]{
                new ColumnConfig(LookUpCell.class, "category", title, 200, true, Constants.LEFT_ALIGN_CELL),
                new ColumnConfig(CustomCell.class, "type", wfmStrings.type(), 200, true, Constants.RIGHT_ALIGN_CELL),
                new ColumnConfig(CustomCell.class, "amount", wfmStrings.amount(), 100, true, Constants.RIGHT_ALIGN_CELL)
        };
    }

    private void addItem(PaymentDeductionObject paymentDeduction, HTML payTotal) {
        CategoryLookUp categoryLookUp = new CategoryLookUp(fromPayment ? PayrollConstants.CATEGORY_PAYMENT : PayrollConstants.CATEGORY_DEDUCTION, () -> true);

        if (paymentDeduction != null) {
            if (paymentDeduction.getCategoryItem() != null) {
                categoryLookUp.addCategoryItem(paymentDeduction.getCategoryItem());
            }
            categoryLookUp.setEnabled(false);
            categoryLookUp.setSickRequestIds(paymentDeduction.getSickRequestids());
            categoryLookUp.setLeavePaymentItem(paymentDeduction.getLeavePaymentItem());
        }
        if (!Utils.hasPermission(PermissionConstants.PAYROLL_PAYSLIP_EDITABLE)) {
            categoryLookUp.setEnabled(false);
        }
        EditableTextBox type = new EditableTextBox();
        type.setEnabled(false);
        if (paymentDeduction != null && paymentDeduction.getType() != null) {
            if (paymentDeduction.getType().equals(PayrollConstants.LINKED_TYPE_FIXED) || paymentDeduction.isLoan()) {
                type.setText("Fixed");
            } else if (paymentDeduction.getType().equals(PayrollConstants.LINKED_TYPE_PERCENTAGE_OF_BASIC)) {
                type.setText(PayrollClientUtils.numberFormat(paymentDeduction.getPercentage()) + " % of Basic Salary");
            } else {
                type.setText(PayrollClientUtils.numberFormat(paymentDeduction.getPercentage()) + " % of Basic + Allowances");
            }
        } else {
            type.setText("Fixed");
        }

        PayslipItemAmountWidget amountWidget = new PayslipItemAmountWidget();
        amountWidget.setEditable(editable);
        amountWidget.setWidth("100px");
        amountWidget.setAmount(BigDecimal.ZERO);
        if (paymentDeduction != null) {
            amountWidget.setLeaveDaysCount(paymentDeduction.getLeaveDaysCount());
            if (paymentDeduction.getType() == null || paymentDeduction.getType().equals(PayrollConstants.LINKED_TYPE_FIXED) || paymentDeduction.isLoan()) {
                amountWidget.setAmount(paymentDeduction.getPaymentAmount());
            } else if (paymentDeduction.getPercentage() != null) {
//                amountWidget.setPercentage(paymentDeduction.getPercentage());
                if (paymentDeduction.getPaymentAmount() != null) {
                    amountWidget.setAmount(paymentDeduction.getPaymentAmount());
                } else if (!fromPayment) {
                    if (paymentDeduction.isFromAllAllowances()) {
//                        amountWidget.setFromAllAllowances(true);
//                        amountWidget.setAmount(paymentDeduction.getPaymentAmount() != null ? paymentDeduction.getPaymentAmount() : BigDecimal.ZERO);
                        CustomPaymentCell customPaymentCell = (CustomPaymentCell) table.getColumnById(grid.getCurrentRow(), PayrollContants.ALLOWANCE);
                        BigDecimal allowanceTotal = customPaymentCell.getAllowanceTotal(null).add(salary);
                        amountWidget.setAmount(allowanceTotal.multiply(paymentDeduction.getPercentage()).divide(BigDecimal.valueOf(100), calculationScale, BigDecimal.ROUND_HALF_UP));
                    } else if (paymentDeduction.getLinkedCategories() != null && paymentDeduction.getLinkedCategories().size() > 0) {
//                        amountWidget.setLinkedCategories(paymentDeduction.getLinkedCategories());
//                        amountWidget.setAmount(paymentDeduction.getPaymentAmount() != null ? paymentDeduction.getPaymentAmount() : BigDecimal.ZERO);

                        CustomPaymentCell customPaymentCell = (CustomPaymentCell) table.getColumnById(grid.getCurrentRow(), PayrollContants.DEDUCTION);
                        BigDecimal allowanceTotal = customPaymentCell.getAllowanceTotal(paymentDeduction.getLinkedCategories()).add(salary);
                        amountWidget.setAmount(allowanceTotal.multiply(paymentDeduction.getPercentage()).divide(BigDecimal.valueOf(100), calculationScale, BigDecimal.ROUND_HALF_UP));
                    } else {
                        amountWidget.setAmount(salary.multiply(paymentDeduction.getPercentage()).divide(BigDecimal.valueOf(100), calculationScale, BigDecimal.ROUND_HALF_UP));
                    }
                } else {
                    amountWidget.setAmount(salary.multiply(paymentDeduction.getPercentage()).divide(BigDecimal.valueOf(100), calculationScale, BigDecimal.ROUND_HALF_UP));
                }
                amountWidget.getAmountTextBox().setEnabled(false);
            }

            if (!fromPayment && paymentDeduction.isLoan()) {
//                amountWidget.setLoan(true);
//                amountWidget.setRemainingAmount(paymentDeduction.getRemainingAmount());
                amountWidget.getAmountTextBox().addKeyUpHandler(keyUpEvent -> {
                    if (amountWidget.getAmount().compareTo(paymentDeduction.getRemainingAmount()) > 0) {
                        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setWidth("400px");
                        messageBox.setTitle(wfmStrings.confirmation());
                        messageBox.setMessage("Amount of the loan cannot be more than the remaining amount. Do you want to deduct the remaining amount? ");
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onCancel() {
                                amountWidget.setAmount(BigDecimal.ZERO);
                                messageBox.close();
                            }

                            @Override
                            public void onSubmit() {
                                amountWidget.setAmount(paymentDeduction.getRemainingAmount());
                            }
                        });
                        messageBox.open();
                    }
                });
            }
            amountWidget.setItemID(paymentDeduction.getId());
        }
        categoriesTable.addRow(new Widget[]{categoryLookUp, type, amountWidget});
        amountWidget.getAmountTextBox().addValueChangeHandler(valueChangeEvent -> applyTotal(categoriesTable, payTotal));
    }

    public BigDecimal getAllowanceTotal(List<PaymentDeductionObject> linkedCategories) {
        BigDecimal result = BigDecimal.ZERO;

        if (linkedCategories != null) {
            for (int i = 0; i < categoriesTable.getRowCount(); i++) {
                CategoryLookUp categoryLookUp = (CategoryLookUp) categoriesTable.getColumnById(i, "category");
                PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) categoriesTable.getColumnById(i, "amount");

                if (categoryLookUp.getSelectedData() != null) {
                    for (PaymentDeductionObject item : linkedCategories) {
                        if (item.getCategoryItem().getId().equals(categoryLookUp.getSelectedData().getId())) {
                            result = result.add(amountWidget.getAmount());
                            break;
                        }
                    }
                }
            }
        } else {
            result = totalValue;
        }
        return result;
    }

    public ArrayList<PaymentDeductionObject> getCategories() {
        if (categoriesTable == null) {
            return categories;
        }
        final ArrayList<PaymentDeductionObject> result = new ArrayList<>();

        for (int i = 0; i < categoriesTable.getRowCount(); i++) {
            CategoryLookUp categoryLookUp = (CategoryLookUp) categoriesTable.getColumnById(i, "category");
            if (categoryLookUp == null || categoryLookUp.getSelectedData() == null) {
                continue;
            }
            final PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) categoriesTable.getColumnById(i, "amount");

            if (amountWidget == null) {
                continue;
            }
            PaymentDeductionObject object = new PaymentDeductionObject();
            object.setCategoryItem(categoryLookUp.getSelectedData());
            object.setPaymentAmount(amountWidget.getAmount());
            object.setId(amountWidget.getItemID());
            object.setLeaveDaysCount(amountWidget.getLeaveDaysCount());
            object.setSickRequestids(categoryLookUp.getSickRequestIds());
            object.setLeavePaymentItem(categoryLookUp.getLeavePaymentItem());
            result.add(object);
        }
        return result;
    }

    public BigDecimal getPensionAmount(BigDecimal calculatedSalary, BigDecimal pensionRate, List<PaymentDeductionSelectItem> pensionAllowances, BigDecimal maxTaxableAmount) {
        BigDecimal allowanceTotal = BigDecimal.ZERO;

        if (categoriesTable != null) {
            for (int i = 0; i < categoriesTable.getRowCount(); i++) {
                CategoryLookUp categoryLookUp = (CategoryLookUp) categoriesTable.getColumnById(i, "category");
                PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) categoriesTable.getColumnById(i, "amount");

                if (categoryLookUp.getSelectedData() == null) {
                    continue;
                }

                for (PaymentDeductionSelectItem item : pensionAllowances) {
                    if (item.getId().equals(categoryLookUp.getSelectedData().getId())) {
                        allowanceTotal = allowanceTotal.add(amountWidget.getAmount());
                        break;
                    }
                }
            }
        } else {
            for (PaymentDeductionObject object : categories) {
                for (PaymentDeductionSelectItem item : pensionAllowances) {
                    if (item.getId().equals(object.getCategoryItem().getId())) {
                        allowanceTotal = allowanceTotal.add(object.getPaymentAmount());
                        break;
                    }
                }
            }
        }
        allowanceTotal = allowanceTotal.add(calculatedSalary);
        if (maxTaxableAmount.compareTo(BigDecimal.ZERO) > 0 && allowanceTotal.compareTo(maxTaxableAmount) >= 0) {
            allowanceTotal = maxTaxableAmount;
        }
        return allowanceTotal.multiply(pensionRate).divide(BigDecimal.valueOf(100), calculationScale, BigDecimal.ROUND_HALF_UP);
    }

}
