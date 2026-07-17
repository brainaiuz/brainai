package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.settings.EdsEmailFilter;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Mar 19, 2010
 * Time: 4:53:29 PM
 * To change this template use File | Settings | File Templates.
 */
public interface EmailFilterManager extends Manager<EdsEmailFilter> {

    List<EdsEmailFilter> getSubFilters(String filterType);

    List<EdsEmailFilter> getParentsOnly();

    List<EdsEmailFilter> list(ListingFilterParameter filterParameter);

    String[] getDefaultSelections(String type, String... defaults);
}