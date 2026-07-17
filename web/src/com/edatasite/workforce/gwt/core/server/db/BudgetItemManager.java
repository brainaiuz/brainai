package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.accounting.EdsBudgetManagerItem;

import java.util.Date;
import java.util.List;

public interface BudgetItemManager extends Manager<EdsBudgetManagerItem> {

    EdsBudgetManagerItem findBudgetItem(Integer entityID, Integer groupId, String columnCode, Date time, Date time1);

    List<EdsBudgetManagerItem> findBudgetedItemsInTheRange(Integer budgetmanagerId, Date nonConvertedDate, Date nonConvertedDate1);

    EdsBudgetManagerItem findBudgetedItem(Integer budgetmanagerId, Date date, String columncode, Integer entityId, Integer groupId);
}