package com.edatasite.workforce.core.domain.accounting;


import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.List;

@Entity
@TypeDefs(@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class))
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "budget_manager")
public class EdsBudgetManager extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    private String type;

    @Column(name = "deleted", columnDefinition = "boolean default false")
    private boolean deleted;

    @Column(name = "default_budget", columnDefinition = "boolean default false")
    private boolean defaultBudget;

    private Integer scale;

    @Type(type = "jsonb")
    @Column(name = "selected_items", columnDefinition = "jsonb")
    private HashMap<Integer, List<Integer>> selectedItems;


    @Column(name = "budget_columns")
    @Type(type = "text")
    private String budgetColumns;

    private String groupBy;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public SelectItem toSelectItem() {
        SelectItem selectItem = new SelectItem(getObjectID(), getName(), getType());
        selectItem.setOrderId(getScale() != null ? getScale() : 0);
        selectItem.setDefaultSelected(isDefaultBudget());
        return selectItem;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isDefaultBudget() {
        return defaultBudget;
    }

    public void setDefaultBudget(boolean defaultBudget) {
        this.defaultBudget = defaultBudget;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }

    public HashMap<Integer, List<Integer>> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(HashMap<Integer, List<Integer>> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public String getBudgetColumns() {
        return budgetColumns;
    }

    public void setBudgetColumns(String budgetColumns) {
        this.budgetColumns = budgetColumns;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }
}
