package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsBudgetManager;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.BudgetManager;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("budgetManager")
public class BudgetManagerImpl extends BaseManager<EdsBudgetManager> implements BudgetManager {

    public BudgetManagerImpl() {
        super(EdsBudgetManager.class);
    }

    @Override
    public List<EdsBudgetManager> getBudgetManagerList() {
        return find("SELECT item FROM EdsBudgetManager item WHERE item.deleted is not true ");
    }


    public Integer isBudgetManagerAlreadyExists(String code, String name, Integer objectID) {
        Map<String, Object> map = new HashMap<>();
        map.put("budgetCode", code != null ? code : "");
        map.put("budgetName", name != null ? name.toLowerCase() : "");
        EdsBudgetManager budgetManager = (EdsBudgetManager) findSingleByNamedParams("select a from EdsBudgetManager a where " + (objectID != null ? "a.objectID <> " + objectID + " and " : "") + " ( a.code = :budgetCode or  lower(a.name) = :budgetName ) and " + ServerUtils.checkForDeleted("a.deleted"), map);
        if (budgetManager != null) {
            if (budgetManager.getCode() != null && budgetManager.getCode().equals(code)) {
                return -1;
            } else if (budgetManager.getName() != null && budgetManager.getName().equals(name)) {
                return -2;
            }
        }
        return 0;
    }

    @Override
    public void updateBudgetsNotDefault() {
        updateNative("update " + getCompanyId() + ".budget_manager set default_budget = false ");
    }

    @Override
    public EdsBudgetManager findByNameOrCode(String name, String code) {
        return (EdsBudgetManager) findSingle("select bm from EdsBudgetManager bm where bm.deleted is not true and (lower(bm.name) = '" + name.toLowerCase() + "' or bm.code='" + code + "'");
    }
}