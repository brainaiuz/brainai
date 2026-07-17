package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.crm.EdsLink;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 10.06.2013
 * Time: 18:44:53
 * To change this template use File | Settings | File Templates.
 */

public interface LinkManager extends Manager<EdsLink> {

    void deleteAll(Integer messageID);

    EdsLink getByKpiLink(String kpiUrl, Integer messageID);
}
