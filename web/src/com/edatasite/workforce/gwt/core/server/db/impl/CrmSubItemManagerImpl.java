package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.crm.EdsCrmSubItem;
import com.edatasite.workforce.gwt.core.server.db.CrmSubItemManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("crmSubItemManager")
public class CrmSubItemManagerImpl extends BaseManager<EdsCrmSubItem> implements CrmSubItemManager {
    public CrmSubItemManagerImpl() {
        super(EdsCrmSubItem.class);
    }

    @Override
    public List<EdsCrmSubItem> getItemsByTypeAndId(String entityType, Integer entityID) {

        return find("SELECT csi FROM EdsCrmSubItem csi WHERE csi.entityId = ? AND csi.entityType = ?", entityID, entityType);
    }

    @Override
    public void deleteItems(Integer entityId, String entityType) {

        update("DELETE FROM EdsCrmSubItem csi WHERE csi.entityId = ? AND csi.entityType = ?", entityId, entityType);
    }
}
