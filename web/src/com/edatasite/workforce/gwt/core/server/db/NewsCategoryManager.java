package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsNewsCategory;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 23, 2009
 * Time: 5:29:55 PM
 * To change this template use File | Settings | File Templates.
 */
public interface NewsCategoryManager extends Manager<EdsNewsCategory> {
    List<EdsNewsCategory> getCategories();

    List<EdsNewsCategory> list(ListingFilterParameter filterParametrs);

    Integer getListCount(ListingFilterParameter fp);
    
    EdsNewsCategory getByName(String name);
}
