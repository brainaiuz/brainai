package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContactCustomItemTable;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class CrmContactItemTableManagerImpl extends BaseManager<EdsCrmContactCustomItemTable> implements CrmContactItemTableManager {

    public CrmContactItemTableManagerImpl() {
        super(EdsCrmContactCustomItemTable.class);
    }

    @Override
    public List<EdsCrmContactCustomItemTable> findByUuid(Integer id, String uuid) {
        return (List<EdsCrmContactCustomItemTable>) find("select t from EdsCrmContactCustomItemTable t where t.crmContact.objectID=? and t.uuid=?", id, uuid);
    }

    @Override
    public void deleteByUUID(String uuid) {
        updateNative("delete from " + getCompanyId() + ".crmContactCustomItemTable cf where cf.uuid = '" + uuid + "'");
    }
}
