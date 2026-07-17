package com.edatasite.workforce.gwt.issue.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NewsComment;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.ProjectItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA. User: Acer Date: 06-Jan-2008 Time: 22:11:05 To
 * change this template use File | Settings | File Templates.
 */

public interface IssueService extends RemoteService {

    ListResult<IssueListItem> getIssuesList(ListingFilterParameter fp);

    SelectItem[] getIssueStatuses(boolean isResolver);

    SelectItem[] getResolversRelatedTo(String relatedTo, Integer relatedId);

    SelectItem[] getPriorities();

    Integer createIssueItem(IssueItem issueItem);

    PositionsSelectItem[] getAssigneesWithPositions(Integer projectId);

    ProjectItem[] getProjectsNotStartedOngoing(Integer issueID, boolean withProjectNumber);

    IssueItem editIssueItem(Integer objectId, Integer relationId);

    ProjectItem editProjectItem(Integer relationId);

    Boolean deleteIssue(Integer issueID);

    Boolean deleteIssueMass(ArrayList<Integer> objectIDs);

    SelectItem[] getProjectTasks(Integer projectId);

    NumberData generateIssueNumber();

    HistoryListItem[] getIssueNotes(Integer issueID);

    NewsComment[] getIssueNoteComments(Integer noteID);

    NewsComment saveIssueNoteComments(NewsComment commentData);

    Boolean checkAccess(Integer issueID, String permission);

    HashSet<String> getPermissions(Integer taskID, String context);

    PositionsSelectItem[] getAssigneesWithPositionsForIssue(Integer projectId, Integer issueID);

    boolean saveIssueEditCellValue(IssueListItem rowValue, String columnCodeName);

    class App {
        public static IssueServiceAsync get() {
            ServiceDefTarget target = GWT
                    .create(IssueService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/issue");
            return (IssueServiceAsync) target;
        }
    }
}