package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.crm.EdsCaseHistory;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: May 1, 2010
 * Time: 7:37:34 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CaseHistoryManager extends Manager<EdsCaseHistory> {
    List<EdsCaseHistory> historyList(Integer crmCaseID);
}
