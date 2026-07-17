package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsApprovableLogHistory;
import com.edatasite.workforce.gwt.core.server.db.ApprovableLogHistoryManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("approvableLogHistoryManagerImpl")
public class ApprovableLogHistoryManagerImpl extends BaseManager<EdsApprovableLogHistory> implements ApprovableLogHistoryManager {
    public ApprovableLogHistoryManagerImpl() {
        super(EdsApprovableLogHistory.class);
    }
    @Override
    public List<EdsApprovableLogHistory> listType(String entityType) {
        String query = "SELECT ah.* FROM " + getCompanyId() + ".approverLogHistory ah WHERE ah.entityType = '"+entityType+"' order by ah.date desc";
        return (List<EdsApprovableLogHistory>) findNative(query, EdsApprovableLogHistory.class);
    }

}
