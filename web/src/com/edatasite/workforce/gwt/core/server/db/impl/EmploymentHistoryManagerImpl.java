package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmploymentHistory;
import com.edatasite.workforce.gwt.core.server.db.EmploymentHistoryManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Oct 25, 2009
 * Time: 11:11:27 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository("employmentHistoryManager")
public class EmploymentHistoryManagerImpl extends BaseManager<EdsEmploymentHistory> implements EmploymentHistoryManager {

    public EmploymentHistoryManagerImpl() {
        super(EdsEmploymentHistory.class);
    }

    public List<EdsEmploymentHistory> getEmploymentHistoryList(EdsEmployee employee) {
        return find("from EdsEmploymentHistory eh where " +
                " eh.employee=? and (eh.deleted=false or eh.deleted is null)", employee);

    }
}
