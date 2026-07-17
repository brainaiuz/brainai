package com.edatasite.workforce.gwt.accounting.client.ui.budgetsheet;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 17.03.2009
 * Time: 16:12:43
 * To change this template use File | Settings | File Templates.
 */
public class BudgetsheetObject {

    private Object value;
    private boolean editable;
    private String itemStyle;

    private BudgetsheetProvider budgetsheetProvider;
    private BudgetsheetKeyboardListener keyboardListener;

    private Integer accountBudgetID;

    private BigDecimal oldValue;
    private BigDecimal newValue;

    public BudgetsheetObject(Object value) {
        this(value, "fixed-cell-background");
    }

    public BudgetsheetObject(Object value, String itemStyle) {
        this(value, false, itemStyle);
    }

    public BudgetsheetObject(Object value, boolean editable) {
        this(value, editable, editable ? null : "fixed-cell-background");
    }

    public BudgetsheetObject(Object value, boolean editable, String itemStyle) {
        this.value = value;
        this.editable = editable;
        this.itemStyle = itemStyle;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getItemStyle() {
        return itemStyle;
    }

    public void setItemStyle(String itemStyle) {
        this.itemStyle = itemStyle;
    }

    public BudgetsheetProvider getBudgetsheetProvider() {
        return budgetsheetProvider;
    }

    public void setBudgetsheetProvider(BudgetsheetProvider budgetsheetProvider) {
        this.budgetsheetProvider = budgetsheetProvider;
    }

    public BudgetsheetKeyboardListener getKeyboardListener() {
        return keyboardListener;
    }

    public void addKeyboardListener(BudgetsheetKeyboardListener keyboardListener) {
        this.keyboardListener = keyboardListener;
    }

    public Integer getAccountBudgetID() {
        return accountBudgetID;
    }

    public void setAccountBudgetID(Integer accountBudgetID) {
        this.accountBudgetID = accountBudgetID;
    }

    public BigDecimal getOldValue() {
        return oldValue;
    }

    public void setOldValue(BigDecimal oldValue) {
        this.oldValue = oldValue;
    }

    public BigDecimal getNewValue() {
        return newValue;
    }

    public void setNewValue(BigDecimal newValue) {
        this.newValue = newValue;
    }
}
