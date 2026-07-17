package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsExpenseHistory;
import com.edatasite.workforce.core.domain.EdsReference;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 27.11.2008
 * Time: 15:53:15
 * To change this template use File | Settings | File Templates.
 */
public interface ExpenseHistoryManager extends Manager<EdsExpenseHistory> {

    List<EdsExpenseHistory> getReportsHistory(Integer reportId);

    EdsExpenseHistory getLastEventRecord(EdsReference event, Integer reportId);
}
