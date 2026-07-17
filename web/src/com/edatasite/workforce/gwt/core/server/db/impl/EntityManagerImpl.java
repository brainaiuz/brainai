package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsEntity;
import com.edatasite.workforce.gwt.core.server.db.EntityManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 08-Jul-2009
 * Time: 18:37:22
 * To change this template use File | Settings | File Templates.
 */
@Repository("entityManager")
public class EntityManagerImpl extends BaseManager<EdsEntity> implements EntityManager {

    public EntityManagerImpl() {
        super(EdsEntity.class);
    }

}