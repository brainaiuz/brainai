package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 16.03.2009
 * Time: 16:54:29
 * To change this template use File | Settings | File Templates.
 */
public class AccountItemsByAccountType implements IsSerializable {
    private String groupName;
    private Integer groupId;

    private ArrayList<AccountItemWithBudgetDate> accountItems;

    private LinkedList<AccountItemsByAccountType> child;

    private ArrayList<BudgetInDate> budgetedTotal;//ussed as budgeted in BudgetSheet and as actual in PNL

    private HashMap<String, ArrayList<BudgetInDate>> budgetItemTotal;

    private ArrayList<BudgetInDate> actualTotal;

    private ActBudVar totalWithVariance;

    public AccountItemsByAccountType() {
    }

    public AccountItemsByAccountType(String groupName) {
        this.groupName = groupName;
    }

    public AccountItemsByAccountType(String groupName, Integer groupId) {
        this.groupName = groupName;
        this.groupId = groupId;
    }

    public Integer getGroupId() {
        return this.groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


    public AccountItemWithBudgetDate[] getAccountItems() {
        return accountItems.toArray(new AccountItemWithBudgetDate[]{});
    }

    public ArrayList<AccountItemWithBudgetDate> getAccountItemsList() {
        return accountItems;
    }

    public AccountItemsByAccountType setAccountItems(List<AccountItemWithBudgetDate> accountItems) {
        this.accountItems = (ArrayList<AccountItemWithBudgetDate>) accountItems;
        return this;
    }

    public AccountItemsByAccountType setBudgetedTotal(List<BudgetInDate> budgetedTotal) {
        this.budgetedTotal = (ArrayList<BudgetInDate>) budgetedTotal;
        return this;
    }

    public BudgetInDate[] getBudgetedTotal() {
        return budgetedTotal != null ? budgetedTotal.toArray(new BudgetInDate[]{}) : null;
    }


    public AccountItemsByAccountType setActualTotal(List<BudgetInDate> actualTotal) {
        this.actualTotal = (ArrayList<BudgetInDate>) actualTotal;
        return this;
    }

    public ArrayList<BudgetInDate> getActualTotal() {
        return actualTotal;
    }

    public ActBudVar getTotalWithVariance() {
        return totalWithVariance;
    }

    public void setTotalWithVariance(ActBudVar totalWithVariance) {
        this.totalWithVariance = totalWithVariance;
    }

    public LinkedList<AccountItemsByAccountType> getChild() {
        return this.child;
    }

    public void setChild(final LinkedList<AccountItemsByAccountType> child) {
        this.child = child;
    }

    public HashMap<String, ArrayList<BudgetInDate>> getBudgetItemTotal() {
        return this.budgetItemTotal;
    }

    public void setBudgetItemTotal(final HashMap<String, ArrayList<BudgetInDate>> budgetItemTotal) {
        this.budgetItemTotal = budgetItemTotal;
    }
}
