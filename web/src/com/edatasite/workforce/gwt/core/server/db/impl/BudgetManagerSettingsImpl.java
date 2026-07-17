package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsBudgetManagerSettings;
import com.edatasite.workforce.gwt.core.server.db.BudgetManagerSettings;
import org.springframework.stereotype.Repository;


@Repository("budgetManagerSettings")
public class BudgetManagerSettingsImpl extends BaseManager<EdsBudgetManagerSettings> implements BudgetManagerSettings {

    public BudgetManagerSettingsImpl() {
        super(EdsBudgetManagerSettings.class);
    }

    @Override
    public EdsBudgetManagerSettings getSettingsByBudget(Integer budgetManagerId) {
        return (EdsBudgetManagerSettings) findSingle("select bms from EdsBudgetManagerSettings bms left join bms.budgetManager bm where bm.deleted is not true and bm.objectID = " + budgetManagerId);
    }
}