package com.edatasite.workforce.gwt.core.server.db.impl.projectcost;

import com.edatasite.workforce.core.domain.projectcost.EdsResourcePool;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.projectcost.ResourcePoolManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 28.04.2010
 * Time: 19:07:21
 * To change this template use File | Settings | File Templates.
 */
@Repository("resourcePoolManager")
public class ResourcePoolManagerImpl extends BaseManager<EdsResourcePool> implements ResourcePoolManager {

    public ResourcePoolManagerImpl() {
        super(EdsResourcePool.class);
    }

    @Override
    public List<EdsResourcePool> list(Integer resourceTypeId) {
        return find("select rp from EdsResourcePool rp where rp.resourceType.objectID=?", resourceTypeId);
    }
}