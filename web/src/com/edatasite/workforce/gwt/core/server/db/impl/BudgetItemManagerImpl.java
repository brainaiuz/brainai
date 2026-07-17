package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsBudgetManagerItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.BudgetItemManager;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository("budgetItemManager")
public class BudgetItemManagerImpl extends BaseManager<EdsBudgetManagerItem> implements BudgetItemManager {

    public BudgetItemManagerImpl() {
        super(EdsBudgetManagerItem.class);
    }

    @Override
    public EdsBudgetManagerItem findBudgetItem(Integer accountID, Integer groupID, String columnCode, Date from, Date to) {
        if (groupID != null) {
            return (EdsBudgetManagerItem) findSingle("select bm from EdsBudgetManagerItem bm where bm.entityId = ? and bm.groupId = ? and bm.columnCode = ? and bm.date between ? and ? order by bm.objectID desc", accountID, groupID, columnCode, from, to);
        } else {
            return (EdsBudgetManagerItem) findSingle("select bm from EdsBudgetManagerItem bm where bm.entityId = ? and bm.columnCode = ? and bm.date between ? and ? order by bm.objectID desc", accountID, columnCode, from, to);
        }
    }

    @Override
    public List<EdsBudgetManagerItem> findBudgetedItemsInTheRange(Integer budgetmanagerId, Date from, Date to) {
        return find("select bm from EdsBudgetManagerItem bm where bm.budgetManager.objectID = ? and bm.department is null and bm.date between ? and ? order by bm.entityId, bm.date", budgetmanagerId, from, to);
    }

    @Override
    public EdsBudgetManagerItem findBudgetedItem(Integer budgetmanagerId, Date date, String columncode, Integer entityId, Integer groupId) {

        Date start = ServerUtils.getMonthStartDate(date);
        Date end = ServerUtils.getMonthEndDate(date);


        if (groupId != null) {
            return (EdsBudgetManagerItem) findSingle("select bm from EdsBudgetManagerItem bm where bm.budgetManager.objectID = ? and bm.columnCode =? and bm.entityId =? and bm.groupId =? and bm.department is null and bm.date between ? and ? ", budgetmanagerId, columncode, entityId, groupId, start, end);
        } else {
            return (EdsBudgetManagerItem) findSingle("select bm from EdsBudgetManagerItem bm where bm.budgetManager.objectID = ? and bm.columnCode =? and bm.entityId =? and bm.department is null and bm.date between ? and ? ", budgetmanagerId, columncode, entityId, start, end);
        }
    }
}

