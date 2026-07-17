package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.recruitment.EdsGroupPlacementCustomFields;
import com.edatasite.workforce.gwt.core.server.db.GroupPlacementCustomFieldManager;
import org.springframework.stereotype.Repository;

@Repository("groupPlacementCustomFieldsManager")
public class GroupPlacementCustomFieldsManagerImpl extends BaseManager<EdsGroupPlacementCustomFields> implements GroupPlacementCustomFieldManager {
    public GroupPlacementCustomFieldsManagerImpl() {
        super(EdsGroupPlacementCustomFields.class);
    }
}
