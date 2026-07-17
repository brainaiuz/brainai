package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.customform.EdsOpportunityCustomItemTable;
import com.edatasite.workforce.gwt.core.server.db.OpportunityItemTableManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OpportunityItemTableManagerImpl extends BaseManager<EdsOpportunityCustomItemTable> implements OpportunityItemTableManager {

    public OpportunityItemTableManagerImpl() {
        super(EdsOpportunityCustomItemTable.class);
    }


    @Override
    public List<EdsOpportunityCustomItemTable> findByUuid(Integer id, String uuid) {
        return (List<EdsOpportunityCustomItemTable>) find("select t from EdsOpportunityCustomItemTable t where t.opportunity.objectID=? and t.uuid=?", id, uuid);
    }

    @Override
    public void deleteByUUID(String uuid) {
        updateNative("delete from " + getCompanyId() + ".opportunity_item_table cf where cf.uuid = '" + uuid + "'");
    }
}