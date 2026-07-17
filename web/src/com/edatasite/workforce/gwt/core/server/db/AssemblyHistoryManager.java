package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsAssemblyItemBuildHistory;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 6/5/13
 * Time: 2:45 PM
 * To change this template use File | Settings | File Templates.
 */
public interface AssemblyHistoryManager extends Manager<EdsAssemblyItemBuildHistory> {

    List<EdsAssemblyItemBuildHistory> getAssemblyBuildHistory(Integer assemblyID);

    boolean isAssemblyBuilded(Integer objectID);

    List<EdsAssemblyItemBuildHistory> getList(ListingFilterParameter fp);


}
