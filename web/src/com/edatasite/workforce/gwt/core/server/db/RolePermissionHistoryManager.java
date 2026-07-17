package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsRolePermissionHistory;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

public interface RolePermissionHistoryManager extends Manager<EdsRolePermissionHistory> {

    List<EdsRolePermissionHistory> getHistoryList(ListingFilterParameter fp, boolean isTotalQuery);
}
