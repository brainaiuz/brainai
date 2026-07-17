package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmploymentHistory;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Oct 25, 2009
 * Time: 11:08:28 AM
 * To change this template use File | Settings | File Templates.
 */
public interface EmploymentHistoryManager extends Manager<EdsEmploymentHistory> {
    List<EdsEmploymentHistory> getEmploymentHistoryList(EdsEmployee employee);
}
