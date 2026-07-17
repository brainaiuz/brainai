package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.FromToDate;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 16.03.2009
 * Time: 19:06:58
 * To change this template use File | Settings | File Templates.
 */
public class AccountItemWithBudgetDate extends AccountItem implements IsSerializable {

    private ArrayList<BudgetInDate> rowCells;
    private LinkedHashMap<String, LinkedList<BudgetInDate>> budgetData;

    //Used in BudgetSheet's actual part
    private BudgetInDate[] actualValues;

    private ActBudVar variance;
    private ActBudVar YTDvariance;
    private ArrayList<AccountItemWithBudgetDate> childs;
    private boolean calculated = false;

    public AccountItemWithBudgetDate() {
    }

    public AccountItemWithBudgetDate(Integer id, String code, String name, Integer accountTypeID, String accountTypeCode, String accountTypeCategory, Integer key, Integer currencyID, String currencyCode) {
        super(id, code, name, accountTypeID, accountTypeCode, accountTypeCategory, key, currencyID, currencyCode);
    }

    public AccountItemWithBudgetDate(Integer objectID, String accountCode, String name) {
        super(objectID, accountCode, name);
    }

    public ArrayList<BudgetInDate> getRowCells() {
        return rowCells;
    }

    public void setRowCells(ArrayList<BudgetInDate> rowCells) {
        this.rowCells = rowCells;
    }

    public ActBudVar getVariance() {
        return variance;
    }

    public void setVariance(ActBudVar variance) {
        this.variance = variance;
    }

    public ActBudVar getYTDvariance() {
        return YTDvariance;
    }

    public void setYTDvariance(ActBudVar YTDvariance) {
        this.YTDvariance = YTDvariance;
    }

    public BudgetInDate[] getActualValues() {
        return actualValues;
    }

    public void setActualValues(BudgetInDate[] actualValues) {
        this.actualValues = actualValues;
    }

    public ArrayList<AccountItemWithBudgetDate> getChilds() {
        if (childs == null) {
            childs = new ArrayList<>();
        }
        return childs;
    }

    public void setChilds(ArrayList<AccountItemWithBudgetDate> childs) {
        this.childs = childs;
    }

    public boolean isCalculated() {
        return calculated;
    }

    public void setCalculated(boolean calculated) {
        this.calculated = calculated;
    }

    public LinkedHashMap<String, LinkedList<BudgetInDate>> getBudgetData() {
        return this.budgetData;
    }

    public void setBudgetData(final LinkedHashMap<String, LinkedList<BudgetInDate>> budgetData) {
        this.budgetData = budgetData;
    }

    public static ArrayList<BudgetInDate> createAndSetRowCellsData(FromToDate main, FromToDate[] compareTo) {
        ArrayList<BudgetInDate> rowCells = new ArrayList<>();
        BudgetInDate budgetInDate = new BudgetInDate();
        budgetInDate.setDate(main.getTo().getNonConvertedDate());
        rowCells.add(budgetInDate);
        if (compareTo != null && compareTo.length > 0) {
            for (FromToDate compare : compareTo) {
                budgetInDate = new BudgetInDate();
                budgetInDate.setDate(compare.getTo().getNonConvertedDate());
                rowCells.add(budgetInDate);
            }
        }
        return rowCells;
    }
}
