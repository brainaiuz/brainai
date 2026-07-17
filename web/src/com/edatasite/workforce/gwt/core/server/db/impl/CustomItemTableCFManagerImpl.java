package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.customfields.EdsCustomItemTableCF;
import com.edatasite.workforce.gwt.core.server.db.CustomItemTableCFManager;
import org.springframework.stereotype.Repository;

@Repository
public class CustomItemTableCFManagerImpl extends BaseManager<EdsCustomItemTableCF> implements CustomItemTableCFManager {

    public CustomItemTableCFManagerImpl() {
        super(EdsCustomItemTableCF.class);
    }
}
