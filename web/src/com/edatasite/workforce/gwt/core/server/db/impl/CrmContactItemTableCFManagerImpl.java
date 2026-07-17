package com.edatasite.workforce.gwt.core.server.db.impl;


import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContactItemTableCF;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CrmContactItemTableCFManagerImpl extends BaseManager<EdsCrmContactItemTableCF> implements CrmContactItemTableCFManager {

    public CrmContactItemTableCFManagerImpl() {
        super(EdsCrmContactItemTableCF.class);
    }

    @Override
    public List<EdsCrmContactItemTableCF> findByUuid(Integer id, String uuid) {
        return null;
    }

    @Override
    public void deleteByUUID(String uuid) {

    }
}
