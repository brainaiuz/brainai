package com.edatasite.workforce.gwt.core.server.db.projectcost;

import com.edatasite.workforce.core.domain.projectcost.EdsResourcePool;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 28.04.2010
 * Time: 19:04:24
 * To change this template use File | Settings | File Templates.
 */
public interface ResourcePoolManager extends Manager<EdsResourcePool> {
    List<EdsResourcePool> list(Integer resourceTypeId);
}
