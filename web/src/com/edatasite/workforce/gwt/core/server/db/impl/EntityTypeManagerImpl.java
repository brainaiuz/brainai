package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsEntityType;
import com.edatasite.workforce.gwt.core.server.db.EntityTypeManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Omonullo on 5/15/2017.
 */
@Repository("entityTypeManager")
public class EntityTypeManagerImpl extends BaseManager<EdsEntityType> implements EntityTypeManager {

    public EntityTypeManagerImpl() {
        super(EdsEntityType.class);
    }

    public List<EdsEntityType> list() {
        return find("select et from EdsEntityType et");
    }

}