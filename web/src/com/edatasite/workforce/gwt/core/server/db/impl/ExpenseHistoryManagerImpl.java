package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsExpenseHistory;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.ExpenseHistoryManager;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 27.11.2008
 * Time: 15:55:36
 * To change this template use File | Settings | File Templates.
 */
@Repository("expenseHistoryManager")
public class ExpenseHistoryManagerImpl extends BaseManager<EdsExpenseHistory> implements
        ExpenseHistoryManager, Constants {


    public ExpenseHistoryManagerImpl() {
        super(EdsExpenseHistory.class);
    }

    public List<EdsExpenseHistory> getReportsHistory(Integer reportId) {

        if (reportId == null) {
            return null;
        }

        return find("select eh from EdsExpenseHistory eh where eh.expenseReport.objectID =? order by eh.eventDate DESC", reportId);
    }

    public EdsExpenseHistory getLastEventRecord(EdsReference event, Integer reportId) {

        Map<String, Object> map = new HashMap<>();
        map.put("event", event);
        map.put("reportId", reportId);

        return (EdsExpenseHistory) findSingleByNamedParams("from EdsExpenseHistory eh2 " +
                "where eh2.objectID = (select max(eh.objectID) from EdsExpenseHistory eh " +
                "where eh.expenseReport.objectID = :reportId and eh.event = :event)", map);
    }
}
