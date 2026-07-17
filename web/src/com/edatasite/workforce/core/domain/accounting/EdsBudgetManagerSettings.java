package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "budget_manager_settings")
public class EdsBudgetManagerSettings extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budgetManagerid")
    private EdsBudgetManager budgetManager;

    @Column(name = "settingsJSONData")
    @Type(type = "text")
    private String settingsJSONData;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }


    public String getSettingsJSONData() {
        return settingsJSONData;
    }

    public void setSettingsJSONData(String settingsJSONData) {
        this.settingsJSONData = settingsJSONData;
    }

    public EdsBudgetManager getBudgetManager() {
        return this.budgetManager;
    }

    public void setBudgetManager(final EdsBudgetManager budgetManager) {
        this.budgetManager = budgetManager;
    }
}
