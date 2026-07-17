package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.customfields.EdsOpportunityItemTableCF;
import com.edatasite.workforce.gwt.core.server.db.OpportunityItemTableCFManager;
import org.springframework.stereotype.Repository;


@Repository
public class OpportunityItemTableCFManagerImpl extends BaseManager<EdsOpportunityItemTableCF> implements OpportunityItemTableCFManager {

    public OpportunityItemTableCFManagerImpl() {
        super(EdsOpportunityItemTableCF.class);
    }

}