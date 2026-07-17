package com.edatasite.workforce.gwt.core.server.db.impl.projectcost;

import com.edatasite.workforce.core.domain.projectcost.EdsResource;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.projectcost.ResourceManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 28.04.2010
 * Time: 19:07:21
 * To change this template use File | Settings | File Templates.
 */
@Repository("resourceManager")
public class ResourceManagerImpl extends BaseManager<EdsResource> implements ResourceManager {

    public ResourceManagerImpl() {
        super(EdsResource.class);
    }

    @Override
    public List<EdsResource> list(Integer resourceTypeId, Integer resourcePoolId) {
        if (resourcePoolId == null) {
            return find("from EdsResource r where resourceType.objectID=?", resourceTypeId);
        } else {
            return find("from EdsResource r where resourceType.objectID=? and r.resourcePool.objectID=?", resourceTypeId, resourcePoolId);
        }

    }
}
