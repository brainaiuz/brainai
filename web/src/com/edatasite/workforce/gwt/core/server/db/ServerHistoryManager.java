package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsServerHistory;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 24.03.11
 * Time: 20:28
 * To change this template use File | Settings | File Templates.
 */

public interface ServerHistoryManager extends Manager<EdsServerHistory> {

    EdsServerHistory getLastServerHistory();

    List<EdsServerHistory> list(ListingFilterParameter filterParameter);
}
