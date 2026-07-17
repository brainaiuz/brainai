package com.edatasite.workforce.gwt.team.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.department.DepartmentItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.*;


/**
 * Created by IntelliJ IDEA.
 * User: Root
 * Date: Jan 7, 2008
 * Time: 3:28:33 PM
 * To change this template use File | Settings | File Templates.
 */

public interface DepartmentService extends RemoteService {

    void addMembers(Integer departmentId, Integer[] members);

    Integer createTeam(NewTeam team);


    void createTeamItems(List<NewTeam> teamList, List<TeamListItem> teamListItems);

    TeamListItem getTeamForEdit(Integer objectId, String actionType);

    SelectItem[] getTeamsList();

    void updateTeam(TeamListItem team);

    DepartmentItem[] getDepartmentsSelectItem();

    SelectItem[] getDepartmentsAsSelectItem(ListingFilterParameter fp);

    ListResult<TeamListItem> getTeams(ListingFilterParameter fp);

    ProjectMember[] getTeamEmployees(Integer objectId);

    ProjectMember[] getCompanyEmployees();

    TeamListItem getTeam(Integer departmentId);

    Boolean getNotRemoveDefaultDepartment(Integer departmentId);

    String getTeamName(Integer departmentId);

    Boolean deleteDepartment(Integer departmentId, Integer defaultDepartmentId);

    Boolean checkAccess(Integer departmentId, String permission);

    void runMindsharePatch(Integer companyID, int oldEmployeeDepartmentID, int newEmployeeDepartmentID);

    void runMindshareCleanup(Integer companyID, Date start, ArrayList<Integer> employeeIDs, Integer oldDepartmentID);

    HashSet<String> getDepartmentSpecificPermissions(Integer departmentID, String sectionContext);

    Integer getDepartmentByNameAndId(String name, Integer objectId);

    Integer getDepartmentByCodeAndId(String code, Integer objectId);

    Integer maxChildLevels();

    void saveCustomizationOrgChart(LinkedList<SelectItem> items, LinkedList<String> colorItems);

    SelectItem[] getDepartmentsForCustomization(Integer parentId);

    String getTeamGraphChart(boolean isShowView, Integer levelOptionList, boolean isFromUI, Integer parentId, Integer nodeId, Integer showAllId, boolean showExternalEmployee, Integer locationId, boolean fromClickEvents);

    void saveTeamParent(Integer teamId, Integer parentId);

    String getChildDepartmentNames(Integer id);

    String getChildDepartmentIds(Integer id, boolean needComma);

    SelectItem getLocationByDepartmentId(Integer departmentId);

    void saveEmployeeDepartment(HashSet<Integer> teamMembers, Integer teamID, boolean isChecked, boolean indexToSolr, boolean setNullToEmployeePosition);

    void saveEmployeeDepartment(HashSet<Integer> teamMembers, Integer teamID, boolean isChecked, boolean indexToSolr, Date startDate, boolean setNullToEmployeePosition);

    String getDepartmentFacetQuery(final ListingFilterParameter fp, final Integer companyId);

    void activateOrDisctivateTeam(Integer teamId, Boolean activate);


    class App {
        public static DepartmentServiceAsync get() {
            ServiceDefTarget target = GWT.create(DepartmentService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/department");
            return (DepartmentServiceAsync) target;
        }
    }
}
