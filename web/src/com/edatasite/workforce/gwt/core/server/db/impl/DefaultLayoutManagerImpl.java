package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsDefaultLayout;
import com.edatasite.workforce.gwt.core.server.db.DefaultLayoutManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 1/25/12
 * Time: 5:39 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("defaultLayoutManager")
public class DefaultLayoutManagerImpl extends BaseManager<EdsDefaultLayout> implements DefaultLayoutManager {
    public DefaultLayoutManagerImpl() {
        super(EdsDefaultLayout.class);
    }

    @Override
    public EdsDefaultLayout getWebFormLayout(String formID) {
        return (EdsDefaultLayout) findSingle("select l from EdsDefaultLayout l where l.active is true and l.webForm is true order by id desc");
    }
}
