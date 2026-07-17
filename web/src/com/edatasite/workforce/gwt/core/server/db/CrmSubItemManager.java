package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.crm.EdsCrmSubItem;

import java.util.List;

public interface CrmSubItemManager extends Manager<EdsCrmSubItem> {

    List<EdsCrmSubItem> getItemsByTypeAndId(String entityType, Integer entityID);

    void deleteItems(Integer entityId, String entityType);
}
