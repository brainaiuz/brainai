package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.BudgetSheet;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class BudgetManagerItem implements IsSerializable, Serializable {

    private Integer objectId;
    private String name;
    private String type;
    private String groupBy;
    private Integer scale;
    private boolean defaultBudget;
    private SelectItem[] budgetManagers;
    private SelectItem defaultBudgetManager;
    private LinkedList<BudgetColumn> columns;
    private ArrayList<SelectItem> groupsByType;

    public Integer getObjectId() {
        return this.objectId;
    }

    public void setObjectId(final Integer objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public Integer getScale() {
        return this.scale;
    }

    public void setScale(final Integer scale) {
        this.scale = scale;
    }

    public boolean isDefaultBudget() {
        return this.defaultBudget;
    }

    public void setDefaultBudget(final boolean defaultBudget) {
        this.defaultBudget = defaultBudget;
    }

    public SelectItem[] getBudgetManagers() {
        return this.budgetManagers;
    }

    public void setBudgetManagers(final SelectItem[] budgetManagers) {
        this.budgetManagers = budgetManagers;
    }

    public SelectItem getDefaultBudgetManager() {
        return this.defaultBudgetManager;
    }

    public void setDefaultBudgetManager(final SelectItem defaultBudgetManager) {
        this.defaultBudgetManager = defaultBudgetManager;
    }

    public LinkedList<BudgetColumn> getColumns() {
        return this.columns;
    }

    public void setColumns(final LinkedList<BudgetColumn> columns) {
        this.columns = columns;
    }

    public String getGroupBy() {
        return this.groupBy;
    }

    public void setGroupBy(final String groupBy) {
        this.groupBy = groupBy;
    }

    public ArrayList<SelectItem> getGroupsByType() {
        return this.groupsByType;
    }

    public void setGroupsByType(final ArrayList<SelectItem> groupsByType) {
        this.groupsByType = groupsByType;
    }
}
