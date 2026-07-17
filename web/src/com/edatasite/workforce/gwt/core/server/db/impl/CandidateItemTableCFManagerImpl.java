package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.recruitment.EdsCandidateItemTableCF;
import com.edatasite.workforce.gwt.core.server.db.CandidateItemTableCFManager;
import org.springframework.stereotype.Repository;

@Repository
public class CandidateItemTableCFManagerImpl extends BaseManager<EdsCandidateItemTableCF> implements CandidateItemTableCFManager {
    public CandidateItemTableCFManagerImpl() {
        super(EdsCandidateItemTableCF.class);
    }

}
