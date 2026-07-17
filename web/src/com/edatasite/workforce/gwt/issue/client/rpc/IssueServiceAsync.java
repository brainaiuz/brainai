package com.edatasite.workforce.gwt.issue.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NewsComment;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.ProjectItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.HashSet;


/**
 * User: Acer
 * Date: 06-Jan-2008
 * Time: 22:11:05
 */
public interface IssueServiceAsync {

    void getIssuesList(ListingFilterParameter fp, AsyncCallback<ListResult<IssueListItem>> async);

    void getIssueStatuses(boolean isResolver, AsyncCallback<SelectItem[]> async);

    void getResolversRelatedTo(String relatedTo, Integer relatedId, AsyncCallback<SelectItem[]> async);

    void getPriorities(AsyncCallback<SelectItem[]> async);

    void createIssueItem(IssueItem issueItem, AsyncCallback<Integer> async);

    void getAssigneesWithPositions(Integer projectId, AsyncCallback<PositionsSelectItem[]> async);

    void getProjectsNotStartedOngoing(Integer issueID, boolean withProjectNumber, AsyncCallback<ProjectItem[]> async);

    void editIssueItem(Integer objectId, Integer relationId, AsyncCallback<IssueItem> async);

    void editProjectItem(Integer relationId, AsyncCallback<ProjectItem> async);

    void getProjectTasks(Integer projectId, AsyncCallback<SelectItem[]> async);

    void deleteIssue(Integer issueID, AsyncCallback<Boolean> callback);

    void deleteIssueMass(ArrayList<Integer> objectIDs, AsyncCallback<Boolean> callback);

    void generateIssueNumber(AsyncCallback<NumberData> callback);

    void getIssueNotes(Integer issueID, AsyncCallback<HistoryListItem[]> callback);

    void getIssueNoteComments(Integer noteID, AsyncCallback<NewsComment[]> callback);

    void saveIssueNoteComments(NewsComment commentData, AsyncCallback<NewsComment> callback);

    void checkAccess(Integer issueID, String permission, AsyncCallback<Boolean> callback);

    void getPermissions(Integer taskID, String context, AsyncCallback<HashSet<String>> async);

    void getAssigneesWithPositionsForIssue(Integer projectId, Integer issueID, AsyncCallback<PositionsSelectItem[]> callback);

    void saveIssueEditCellValue(IssueListItem rowValue, String columnCodeName, AsyncCallback<Boolean> asyncCallback);
}