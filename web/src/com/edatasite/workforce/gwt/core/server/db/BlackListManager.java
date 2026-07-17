package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsBlackList;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * User: admin
 * Date: Jan 5, 2010
 * Time: 4:01:50 PM
 */
public interface BlackListManager extends Manager<EdsBlackList> {

    boolean isEmailValid(String email);

    List<String> getBlackList();

    List<EdsBlackList> blackLists(ListingFilterParameter fp);

    Integer blackListsCount(ListingFilterParameter fp);
}