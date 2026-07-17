package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.customfields.EdsCrmSubItemCustomFields;
import com.edatasite.workforce.gwt.core.server.db.CrmSubItemCFManager;
import org.springframework.stereotype.Repository;

@Repository("crmSubItemCFManager")
public class CrmSubItemCFManagerImpl extends BaseManager<EdsCrmSubItemCustomFields> implements CrmSubItemCFManager {

    public CrmSubItemCFManagerImpl() {
        super(EdsCrmSubItemCustomFields.class);
    }
}
