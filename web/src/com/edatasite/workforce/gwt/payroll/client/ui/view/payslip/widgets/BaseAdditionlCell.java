package com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.widgets;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.LinkableCellInterface;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.PayslipItemAmountWidget;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunItem;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import org.gwt.advanced.client.ui.widget.EditableGrid;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * User: Murad Satimov
 * Date: 3/27/18 12:09 AM
 */
public abstract class BaseAdditionlCell extends Label implements LinkableCellInterface {
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected static final PayrollStrings payrollStrings = PayrollStrings.App.get();
    protected static final Integer calculationScale = Optional.ofNullable(Utils.getAccountingCalculationScale()).orElse(2);

    protected SinglePayrunItem item;
    protected BigDecimal totalValue;
    protected HTML paymentTotal;

    protected Command clickHandler;
    protected BiConsumer<Integer, SinglePayrunItem> saveHandler;
    protected KpiModal categoriesDialogBox;
    protected EditableTable categoriesTable;
//    protected boolean summaryView = false;
    protected boolean editable = false;

    protected EditableGrid grid;
    protected EditableTable table;
    protected Integer currencyId;


    public BaseAdditionlCell(SinglePayrunItem item) {
        this.item = item;
        this.registerEventHandlers();
    }

    protected void registerEventHandlers() {
        this.clickHandler = () -> {
            if (this.categoriesDialogBox == null) {
                this.drawPopup();
            }
            this.categoriesDialogBox.open();
        };
    }

    protected void applyTotal(EditableTable catTable, HTML payTotal) {
        BigDecimal result = BigDecimal.ZERO;

        if (catTable != null && catTable.getRowCount() > 0) {
            for (int i = 0; i < catTable.getRowCount(); i++) {
                PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) catTable.getColumnById(i, "amount");
                if (amountWidget != null && amountWidget.getAmount() != null) {
                    result = result.add(amountWidget.getAmount());
                }
            }
        }
        payTotal.setText(AccountingUtils.get().formatPrice(result));
    }

    protected abstract void drawPopup();

    protected abstract ColumnConfig[] getColumns();

    public void setSaveHandler(final BiConsumer<Integer, SinglePayrunItem> saveHandler) {
        this.saveHandler = saveHandler;
    }

    @Override
    public String getDisplayValue() {
        return PayrollClientUtils.format(Optional.ofNullable(this.totalValue).orElse(BigDecimal.ZERO));
    }

    @Override
    public void setItemValue(Object value) {
        this.totalValue = PayrollClientUtils.parseToBigDecimal(String.valueOf(value));
    }

    @Override
    public void setItemFocus(boolean focused) {
    }

    @Override
    public Command getClickHandler() {
        return this.clickHandler;
    }

    public void setGrid(EditableGrid grid) {
        this.grid = grid;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTable(EditableTable table) {
        this.table = table;
    }

    public SinglePayrunItem getItem() {
        return item;
    }
}
