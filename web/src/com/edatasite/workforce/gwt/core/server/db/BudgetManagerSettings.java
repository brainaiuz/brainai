package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.accounting.EdsBudgetManagerSettings;

public interface BudgetManagerSettings extends Manager<EdsBudgetManagerSettings> {

    EdsBudgetManagerSettings getSettingsByBudget(Integer budgetManagerId);
}