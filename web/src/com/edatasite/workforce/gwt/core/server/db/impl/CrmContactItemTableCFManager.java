package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContactItemTableCF;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

public interface CrmContactItemTableCFManager extends Manager<EdsCrmContactItemTableCF> {

    List<EdsCrmContactItemTableCF> findByUuid(Integer id, String uuid);

    void deleteByUUID(String uuid);
}
