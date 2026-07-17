package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContactCustomItemTable;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

public interface CrmContactItemTableManager extends Manager<EdsCrmContactCustomItemTable> {

    List<EdsCrmContactCustomItemTable> findByUuid(Integer id, String uuid);

    void deleteByUUID(String uuid);
}
