package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.accounting.EdsBudgetManager;

import java.util.List;

public interface BudgetManager extends Manager<EdsBudgetManager> {
    List<EdsBudgetManager> getBudgetManagerList();

    Integer isBudgetManagerAlreadyExists(String code, String name, Integer objectId);

    void updateBudgetsNotDefault();

    EdsBudgetManager findByNameOrCode(String name, String budget_name);
}