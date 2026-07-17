package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.crm.EdsSolution;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 21-Jul-2009
 * Time: 17:22:16
 * To change this template use File | Settings | File Templates.
 */
public interface SolutionManager extends Manager<EdsSolution> {
    List<EdsSolution> getList(ListingFilterParameter fp);

    Integer getListCount(ListingFilterParameter fp);
}