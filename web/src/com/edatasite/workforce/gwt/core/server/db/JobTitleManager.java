package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.crm.EdsJobTitle;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 23.02.2010
 * Time: 14:38:49
 * To change this template use File | Settings | File Templates.
 */
public interface JobTitleManager extends Manager<EdsJobTitle> {

    List<EdsJobTitle> getList(ListingFilterParameter fp);

}