package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.customfields.EdsOpportunityItemTableCF;
import com.edatasite.workforce.core.domain.customfields.EdsProjectItemTableCF;
import com.edatasite.workforce.gwt.core.server.db.ProjectItemTableCFManager;
import org.springframework.stereotype.Repository;


@Repository
public class ProjectItemTableCFManagerImpl extends BaseManager<EdsProjectItemTableCF> implements ProjectItemTableCFManager {

    public ProjectItemTableCFManagerImpl() {
        super(EdsOpportunityItemTableCF.class);
    }

}