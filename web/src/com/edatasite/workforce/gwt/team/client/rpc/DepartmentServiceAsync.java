package com.edatasite.workforce.gwt.team.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.department.DepartmentItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.*;


/**
 * Created by IntelliJ IDEA.
 * User: Root
 * Date: Jan 7, 2008
 * Time: 3:28:33 PM
 * To change this template use File | Settings | File Templates.
 */

public interface DepartmentServiceAsync {

    void addMembers(Integer departmentId, Integer[] members, AsyncCallback<Void> async);

    void createTeam(NewTeam team, AsyncCallback<Integer> async);

    void createTeamItems(List<NewTeam> newTeamList, List<TeamListItem> teamListItems, AsyncCallback<Void> async);

    void getTeamForEdit(Integer objectId, String actionType, AsyncCallback<TeamListItem> async);

    void getTeamsList(AsyncCallback<SelectItem[]> async);

    void updateTeam(TeamListItem team, AsyncCallback<Void> async);

    void getDepartmentsSelectItem(AsyncCallback<DepartmentItem[]> async);

    void getDepartmentsAsSelectItem(ListingFilterParameter fp, AsyncCallback<SelectItem[]> async);

    Request getTeams(ListingFilterParameter fp, AsyncCallback<ListResult<TeamListItem>> async);

    void getChildDepartmentNames(Integer id, AsyncCallback<String> async);

    void getChildDepartmentIds(Integer id, boolean needComma, AsyncCallback<String> async);

    void getTeamEmployees(Integer objectId, AsyncCallback<ProjectMember[]> async);

    void getCompanyEmployees(AsyncCallback<ProjectMember[]> async);

    void getTeam(Integer departmentId, AsyncCallback<TeamListItem> async);

    void getNotRemoveDefaultDepartment(Integer departmentId, AsyncCallback<Boolean> async);

    void getTeamName(Integer departmentId, AsyncCallback<String> async);

    void deleteDepartment(Integer departmentId, Integer defaultDepartmentId, AsyncCallback<Boolean> async);

    void checkAccess(Integer issueID, String permission, AsyncCallback<Boolean> callback);

    void runMindsharePatch(Integer companyID, int oldEmployeeDepartmentID, int newEmployeeDepartmentID, AsyncCallback<Void> callback);

    void runMindshareCleanup(Integer companyID, Date start, ArrayList<Integer> employeeIDs, Integer oldDepartmentID, AsyncCallback<Void> callback);

    void getDepartmentSpecificPermissions(Integer objectID, String finalContext, AsyncCallback<HashSet<String>> asyncCallback);

    void getDepartmentByNameAndId(String name, Integer objectId, AsyncCallback<Integer> async);

    void getDepartmentByCodeAndId(String code, Integer objectId, AsyncCallback<Integer> async);

    void maxChildLevels(AsyncCallback<Integer> async);

    void getTeamGraphChart(boolean isShowView, Integer levelOptionList, boolean isFromUI, Integer parentId, Integer nodeId, Integer showAllId, boolean showExternalEmployee, Integer locationId, boolean fromClickEvents, AsyncCallback<String> async);

    void saveTeamParent(Integer teamId, Integer parentId, AsyncCallback<Void> callback);

    void saveEmployeeDepartment(HashSet<Integer> teamMembers, Integer teamID, boolean isChecked, boolean indexToSolr, boolean setNullToEmployeePosition, AsyncCallback<Void> async);

    void getDepartmentsForCustomization(Integer parentID, AsyncCallback<SelectItem[]> async);

    void saveCustomizationOrgChart(LinkedList<SelectItem> items, LinkedList<String> colorItems, AsyncCallback<Void> async);

    void getLocationByDepartmentId(Integer departmentId, AsyncCallback<SelectItem> async);


    void saveEmployeeDepartment(HashSet<Integer> teamMembers, Integer teamID, boolean isChecked, boolean indexToSolr, Date startDate, boolean setNullToEmployeePosition, AsyncCallback<Void> async);

    void getDepartmentFacetQuery(final ListingFilterParameter fp, Integer companyId, AsyncCallback<String> async);

    void activateOrDisctivateTeam (Integer teamId, Boolean activate, AsyncCallback<Void> async);
}
