package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.crm.EdsCaseHistory;
import com.edatasite.workforce.gwt.core.server.db.CaseHistoryManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: May 1, 2010
 * Time: 7:36:53 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("caseHistoryManager")
public class CaseHistoryManagerImpl extends BaseManager<EdsCaseHistory> implements CaseHistoryManager {
    public CaseHistoryManagerImpl() {
        super(EdsCaseHistory.class);
    }

    public List<EdsCaseHistory> historyList(Integer crmCaseID) {
        return find("select caseHistr from EdsCaseHistory caseHistr where caseHistr.crmCase.objectID = ? order by caseHistr.creationTime desc", crmCaseID);
    }
}
