package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.crm.EdsLink;
import com.edatasite.workforce.gwt.core.server.db.LinkManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 10.06.2013
 * Time: 14:43:14
 */
@Repository
public class LinkManagerImpl extends BaseManager<EdsLink> implements LinkManager {

    public LinkManagerImpl() {
        super(EdsLink.class);
    }

    @Override
    public void deleteAll(Integer messageID) {
        updateNative("delete from " + getCompanyId() + ".link l where l.messageid = " + messageID);
    }

    @Override
    public EdsLink getByKpiLink(String kpiUrl, Integer messageID) {
        return (EdsLink) findNativeSingle("select l.id, l.* from " + getCompanyId() + ".link l where l.messageid = " + messageID + " and l.original_link = '" + kpiUrl + "' order by id desc", EdsLink.class);
    }
}