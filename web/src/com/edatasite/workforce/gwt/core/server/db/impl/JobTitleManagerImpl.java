package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.crm.EdsJobTitle;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.JobTitleManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 25.01.2010
 * Time: 14:43:14
 * To change this template use File | Settings | File Templates.
 */
@Repository("jobTitleManager")
public class JobTitleManagerImpl extends BaseManager<EdsJobTitle> implements JobTitleManager {

    public JobTitleManagerImpl() {
        super(EdsJobTitle.class);
    }

    public List<EdsJobTitle> getList(ListingFilterParameter fp) {
        StringBuffer sql = null;
        sql = new StringBuffer();
        sql.append("select distinct jt.* from " + getCompanyId() + ".crmJobTitle as jt ");
        if (fp.getGroupById() != null) {
            sql.append(" where jt.industryid = " + fp.getGroupById());
        }
        return findNative(sql.toString(), EdsJobTitle.class);

    }
}