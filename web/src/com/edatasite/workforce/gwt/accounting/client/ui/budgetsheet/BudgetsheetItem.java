package com.edatasite.workforce.gwt.accounting.client.ui.budgetsheet;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 16.03.2009
 * Time: 18:54:56
 * To change this template use File | Settings | File Templates.
 */
public class BudgetsheetItem {

    private BudgetsheetObject[] values;
    private boolean hasChild;
    private Integer level;

    public BudgetsheetItem(BudgetsheetObject[] values) {
        this.values = values;
    }

    public BudgetsheetObject[] getValues() {
        return values;
    }

    public BudgetsheetObject getValue(int index) {
        return values[index];
    }

    public void setValue(int index, BudgetsheetObject value) {
        values[index] = value;
    }

    public boolean isHasChild() {
        return hasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
