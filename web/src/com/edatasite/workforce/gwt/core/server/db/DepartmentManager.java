package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeDepartment;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsReferenceLocale;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: mansur
 * Date: Jan 8, 2008
 * Time: 5:55:19 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DepartmentManager extends Manager<EdsDepartment> {
    List<EdsDepartment> list();

    List<EdsDepartment> list(ListingFilterParameter fp);

    List<EdsDepartment> list(List<Integer> depIds);

    List<EdsDepartment> list(ListingFilterParameter fp, boolean fromRU);

    List<EdsDepartment> getLastDepartments();

    List<EdsDepartment> getDepartmentsByRegDate(Date sTime, Date eTime, EdsCompany company, boolean includeUpdateTime);

    List<EdsDepartment> getCompanyDepartments(EdsCompany company);

    List<Object[]> getListByCode();

    void updateTeamRole(Integer objectID);

    void deleteTeam(EdsDepartment department);

    List<EdsDepartment> getDepartmentByName(String name);

    SelectItem[] getDepartmentListWithDistinctName();

    List<EdsDepartment> getDepartmentListByName(String name);

    List<EdsDepartment> getDepartmentByCode(String code);

    List<EdsDepartment> getDepartmentByNameAndId(String name, Integer id);

    List<EdsDepartment> getDepartmentByCodeAndId(String code, Integer id);

    List<EdsDepartment> getTeamsByEmployeeId(Integer employeeId);

    void removeTeamLeaderAndMoveNewEmployee(String ids, EdsEmployee employee);

    EdsDepartment getDepartmentByLeader(EdsUser user);

    List<EdsDepartment> getUserDepartments(EdsUser user);

    SelectItem[] getDepartmentsForAccounting(ListingFilterParameter filterParametrs);

    ArrayList<Integer> getDepartmentsIdByLocationId(Integer locationId);

    Map<String, Integer> getDepartmentAsMap();

    ArrayList<Integer> getEmployeeIDsByTeamLeader(Integer employeeId);

    Integer getDepartmentLastIntNumber();

    EdsReferenceLocale getDeparmentLocalization(Integer deparmentId);

    EdsReferenceLocale getDepartmentLocalizationByReferenceId (Integer departmentId);

    HashMap<Integer, String> getDepartmentNamesMapByIds(String departmentIds);

    EdsDepartment getDepartmentByUniqueId(String uniqueId);

    List<EdsDepartment> getDepartmentByLocationID(Integer id);

    void updateTeamLocation(HashSet<Integer> teamsId, EdsLocation location);

    SelectItem[] getDepartmentsByLocationAsSelectItem(Integer locationID);


    boolean isDepartmentNumberExist(String numberString, Integer objectID);


    List<EdsDepartment> getDepartmentsForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit);

    List<Integer> getDepartmentIdsByIds(String ids);

    List<Integer> getDepartmentIdsWithLimit(Integer start, Integer limit);


    Map<Integer, Integer> getLocationAndTeamSize();


    Boolean hasDepartmentsWithLocation();


    ArrayList<SelectItem> getReferenceRelatedDepartments(Integer referenceId);


    List<EdsEmployeeDepartment> getCompanyDepartments(ListingFilterParameter listingFilterParameter);

    List<Integer> getCompanyDeletedDepartmentsForSolr(SolrReindexRpc solrReindex);

    List<EdsEmployee> getVacants(Integer departmentId);
}
