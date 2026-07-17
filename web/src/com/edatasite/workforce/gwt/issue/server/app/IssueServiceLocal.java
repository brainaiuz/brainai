package com.edatasite.workforce.gwt.issue.server.app;

import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.issue.client.rpc.IssueListItem;

/**
 * User: Xushnud
 * Date: 22.03.2010
 * Time: 17:56:43
 */
public interface IssueServiceLocal {

    ListResult<IssueListItem> getIssuesList(ListingFilterParameter fp);

    SelectItem[] getPriorities();

    PositionsSelectItem[] getAssigneesWithPositions(Integer projectId);

    SelectItem[] getProjectTasks(Integer projectId);
}