package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCustomLayout;
import com.edatasite.workforce.gwt.core.server.db.CustomLayoutManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 1/25/12
 * Time: 5:39 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("customLayoutManager")
public class CustomLayoutManagerImpl extends BaseManager<EdsCustomLayout> implements CustomLayoutManager {

    public CustomLayoutManagerImpl() {
        super(EdsCustomLayout.class);
    }

    @Override
    public EdsCustomLayout getWebFormLayout(String formID) {
        return (EdsCustomLayout) findSingle("select l from EdsCustomLayout l where l.active is true and l.webForm is true order by id desc");
    }
}
