package com.edatasite.workforce.gwt.core.server.db.impl.goal;

import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsValidityPeriod;
import com.edatasite.workforce.core.domain.goal.EdsGoal;
import com.edatasite.workforce.gwt.core.client.rpc.GoalItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.goal.GoalManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: Sherali
 * Date: Oct 26, 2009
 * Time: 3:06:31 PM
 */
@Repository("goalManager")
public class GoalManagerImpl extends BaseManager<EdsGoal> implements GoalManager {

    public GoalManagerImpl() {
        super(EdsGoal.class);
    }

    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private CompanyCustomFieldsManager companyCFSettingsManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private EmployeeManager employeeManager;

    @Override
    public List<EdsGoal> list(ListingFilterParameter fp) {
        EdsReference personalGoal = referenceManager.findReference(EdsGoal._GOAL_CATEGORY, EdsGoal.PERSONAL_GOAL);
        Integer typeOrId;
        String companyID = getCompanyId();
        List<String> cfList = new ArrayList<>();
        boolean hasCustomFieldColumnName = false;
        if (fp.getEntityName() != null && !ViewName.PersonalGoal.name().equals(fp.getEntityName())) { //personal goal bo'lsa shart bajarilmaydi
            cfList = companyCFSettingsManager.getCompanyCustomFieldsColumnCodesList(fp.getEntityName());
            hasCustomFieldColumnName = fp.isCustomFieldsShown() && cfList != null && cfList.contains(fp.getSortField());
        }
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT g.*").append(hasCustomFieldColumnName ? (", gcf." + fp.getSortField() + " ") : "");
        sql.append(" FROM ").append(companyID).append(".goal g");
        if (fp.isCustomFieldsShown()) {
            sql.append(" left outer join ").append(companyID).append(".goalcustomfields gcf on gcf.id = g.goalcustomfieldsid");
        }
        sql.append(" left join ").append(companyID).append(".team t on t.id = g.department_id \n");
        sql.append(" left join ").append(companyID).append(".businessgoal bg on bg.id=g.businessgoal_id \n");
        sql.append(" left join ").append(companyID).append(".myuser man on man.id= g.resolver_id \n");
        sql.append(" left join ").append(companyID).append(".project p on p.id= g.project_id \n");
        sql.append(" where g.deleted is not true and g.goalcategory_id=");
        if (fp.getCrmEntityId() != null) {
            typeOrId = fp.getCrmEntityId();
        } else {
            typeOrId = fp.getType();
        }

        EdsUser currentUser = getUser();
        Integer currentUserID = currentUser.getObjectID();
        if (typeOrId.equals(personalGoal.getObjectID())) {
            if (fp.getEmployeeId() == null) {
                fp.setEmployeeId(currentUserID);
            }
            return getOwnGoalList(fp, personalGoal);
        } else {
            sql.append(typeOrId);
            //for department goal ->
            String assignedUsersIds = "";
            boolean withDepartmentEmployees = false;
            EdsReference departmentGoalReference = referenceManager.findReference(EdsGoal._GOAL_CATEGORY, EdsGoal.DEPARTMENT_GOAL);
            //current user ==> TL
            if (typeOrId.equals(departmentGoalReference.getObjectID()) && currentUser.hasRole(EdsRole.TL_CODE)) {
                EdsDepartment departmentByLeader = departmentManager.getDepartmentByLeader(currentUser);
                if (departmentByLeader != null) {
                    List<EdsEmployee> employees = employeeManager.getTeamEmployees(departmentByLeader.getObjectID());
                    assignedUsersIds = ServerUtils.getAsCommoDelimited(employees, "(0)");
                    withDepartmentEmployees = true;
                } else {
                    assignedUsersIds = currentUserID.toString();
                }
            } else {
                assignedUsersIds = currentUserID.toString();
            }
            if (fp.getProjectId() != null) {
                sql.append(" and p.id=").append(fp.getProjectId());
            }
            sql.append(" and ((g.id in (select ga.goal_id from ").append(companyID).append(".goalassignees ga where ga.assignee in ");
            sql.append(withDepartmentEmployees ? "" : "(").append(/*currentUserID*/assignedUsersIds).append(withDepartmentEmployees ? " " : ") ");
            sql.append(" and ga.deleted is not true))");
            sql.append(" or ( g.creator_id=");
            sql.append(currentUserID);
            sql.append("))");
            if (fp.getSqlSearchKey() != null) {
                sql.append(" and (lower(g.title) like '").append(fp.getSqlSearchKey()).append("' ");
                sql.append("or lower(g.description) like '").append(fp.getSqlSearchKey()).append("' ");
                sql.append("or g.numberdata ilike '").append(fp.getSqlSearchKey()).append("' ");
                //searching by goal custom fields
                if (fp.isCustomFieldsShown() && cfList != null && cfList.size() > 0) {
                    cfList.forEach(ccfS -> sql.append(" or lower(gcf.").append(ccfS).append(") like '").append(fp.getSqlSearchKey()).append("' "));
                }
                sql.append(")");
            }

            sql.append(sortList(fp, cfList));
            return findNative(sql.toString(), EdsGoal.class);
        }
    }

    @Override
    public List<EdsGoal> getOwnGoalList(ListingFilterParameter fp, EdsReference ref) {
        String companyID = getCompanyId();
        List<String> cfList = new ArrayList<>();
        boolean hasCustomFieldColumnName = false;
        if (fp.getEntityName() != null) { //no custom field for employee goal list
            cfList = companyCFSettingsManager.getCompanyCustomFieldsColumnCodesList(fp.getEntityName());
            hasCustomFieldColumnName = fp.isCustomFieldsShown() && cfList != null && cfList.contains(fp.getSortField());
        }
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT g.* ").append(hasCustomFieldColumnName ? (", gcf." + fp.getSortField() + " ") : "");
        sql.append(" FROM ").append(companyID).append(".goal g \n");
        if (fp.isCustomFieldsShown()) {
            sql.append(" left outer join ").append(companyID).append(".goalcustomfields gcf on gcf.id = g.goalcustomfieldsid \n");
        }
        sql.append(" left join ").append(companyID).append(".reference ref on ref.id = g.status_id \n");
        sql.append(" left join ").append(companyID).append(".myuser myu on myu.id = g.resolver_id\n");
        sql.append(" left join ").append(companyID).append(".reference refcat on refcat.id = g.goalcategory_id\n");
        sql.append(" left join ").append(companyID).append(".goal pg on g.project_goal_id= pg.id \n");
        sql.append(" left join ").append(companyID).append(".project p on pg.project_id= p.id \n");
        sql.append(" where g.deleted is not true ");
        if (fp.isAllGoals() && !fp.isAllByProjectGoal() && fp.getProjectId() == null) {
            sql.append(" and g.goalcategory_id=").append(ref.getObjectID());
            sql.append(" and g.creator_id = ").append(fp.getEmployeeId()).append(" ");
        } else if (fp.getProjectId() != null) {
            sql.append(" and g.goalcategory_id=").append(ref.getObjectID());
            sql.append(" and p.id=").append(fp.getProjectId());
        } else if (fp.isAllByProjectGoal()) {
            sql.append(" and g.project_goal_id=").append(fp.getRelationID()).append(" ");
        } else if (fp.getEmployeeIDs() != null && !"".equals(fp.getEmployeeIDs())) {
            sql.append(" and g.creator_id in (").append(fp.getEmployeeIDs()).append(") ");
        }
        //search
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" and (lower(g.title) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("or lower(g.description) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("or g.numberdata ilike '").append(fp.getSqlSearchKey()).append("' ");
            //searching by goal custom fields
            if (fp.isCustomFieldsShown() && cfList != null && cfList.size() > 0) {
                cfList.forEach(ccfS -> sql.append(" or lower(gcf.").append(ccfS).append(") like '").append(fp.getSqlSearchKey()).append("' "));
            }
            sql.append(") ");
        }

        //Year
        if (fp.getYear() != null) {
            sql.append(" AND ( date_part('year', g.fromDate)=").append(fp.getYear());
            sql.append(" OR date_part('year', toDate)=").append(fp.getYear()).append(" )");
        }
        sql.append(sortList(fp, cfList));
        return findNative(sql.toString(), EdsGoal.class);
    }

    @Override
    public List<EdsGoal> getGoalsPeerAssign(ListingFilterParameter parameter) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("fromDate", parameter.getStartDate());
        paramMap.put("toDate", parameter.getDueDate());
        paramMap.put("employeeId", parameter.getEmployeeId());

        return findByNamedParams("SELECT goal FROM EdsGoal goal WHERE goal.deleted<>true AND ((goal.validityPeriod.fromDate between :fromDate and :toDate) AND (goal.validityPeriod.toDate between :fromDate and :toDate)) AND goal.objectID in " +
                "(SELECT goalAssign.goal.objectID FROM EdsGoalAssignees goalAssign WHERE  goalAssign.assignee.objectID = :employeeId AND goalAssign.deleted<>true) ", paramMap);
    }

    @Override
    public List<EdsGoal> getGoalsPeerAssignOutValidity(ListingFilterParameter parameter) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("employeeId", parameter.getEmployeeId());

        return findByNamedParams("SELECT goal FROM EdsGoal goal WHERE goal.deleted<>true AND goal.objectID in " +
                "(SELECT goalAssign.goal.objectID FROM EdsGoalAssignees goalAssign WHERE  goalAssign.assignee.objectID = :employeeId AND goalAssign.deleted<>true) ", paramMap);
    }

    private String sortList(ListingFilterParameter fp, List<String> customFields) {
        StringBuilder sql = new StringBuilder();
        if (fp.getSortField() != null && !"".equals(fp.getSortField()) && !fp.getSortField().equalsIgnoreCase("Action")) {
            sql.append(" \n");
            sql.append(" ORDER BY ");
            //order by custom fields
            if (customFields != null && customFields.contains(fp.getSortField())) {
                sql.append(" gcf.").append(fp.getSortField()).append(" ");
            }
            if (fp.getSortField().equals(GoalItem.EMPLOYEE_GOAL_LIST_STATUS)) {
                sql.append(" ref.name ");
            }
            if (fp.getSortField().equals(GoalItem.EMPLOYEE_GOAL_LIST_GOAL_CATEGORY)) {
                sql.append(" refcat.name ");
            }
            if (fp.getSortField().equals(GoalItem.EMPLOYEE_GOAL_LIST_ACTIONSTEPS)) {
                sql.append(" g.actionSteps ");
            }
            if (fp.getSortField().equals(GoalItem.EMPLOYEE_GOAL_LIST_DESCRIPTION)) {
                sql.append(" g.description ");
            }
            if (fp.getSortField().equals(GoalItem.EMPLOYEE_GOAL_LIST_TITLE)) {
                sql.append(" g.title ");
            }
            if (fp.getSortField().equals(GoalItem.EMPLOYEE_GOAL_LIST_WEIGHT)) {
                sql.append(" g.weight ");
            }
            if (fp.getSortField().equals(GoalItem.EMPLOYEE_GOAL_LIST_RESOLVER)) {
                sql.append(" myu.firstName ");
            }

            if (fp.getSortField().equals(GoalItem.GOAL_LIST_STRATEGIC)) {
                sql.append(" bg.title ");
            }
            if (fp.getSortField().equals(GoalItem.GOAL_LIST_DEPARTMENT)) {
                sql.append("t .name ");
            }
            if (fp.getSortField().equals(GoalItem.GOAL_LIST_WEIGHT)) {
                sql.append(" g.weight ");
            }
            if (fp.getSortField().equals(GoalItem.GOAL_LIST_RESOVER)) {
                if (!fp.isAscending()) {
                    sql.append(" man.firstname DESC, man.lastname ");
                } else {
                    sql.append(" man.firstname, man.lastname ");
                }
            }
            if (fp.getSortField().equals(GoalItem.GOAL_LIST_PROJECT)) {
                sql.append(" p.name ");
            }
            if (fp.getSortField().equals(GoalItem.GOAL_LIST_FROM_DATE)) {
                sql.append(" g.fromDate ");
            }
            if (fp.getSortField().equals(GoalItem.GOAL_LIST_TO_DATE)) {
                sql.append(" g.toDate ");
            }
            if (fp.getSortField().equals(GoalItem.GOAL_LIST_DESCRIPTION)) {
                sql.append(" g.description ");
            }

            if (fp.getSortField().equals(GoalItem.GOAL_LIST_TITLE)) {
                sql.append(" g.title ");
            }
            if (fp.getSortField().equals(GoalItem.GOAL_NUMBER)) {
                sql.append(" g.numberData ");
            }
            if (!fp.isAscending()) {
                sql.append(" DESC ");
            }
        } else {
            sql.append(" ORDER BY g.title asc ");
        }
        return sql.toString();
    }

    public List<EdsGoal> getGoalListByYear(ListingFilterParameter fp, Date startYearDate, Date endYearDate) {
        StringBuilder sql = new StringBuilder();
        if (fp.getSqlSearchKey() != null) {
            sql.append(" and (lower(g.title) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("or lower(g.description) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(") ");
        }
        return find(
                "from EdsGoal g where g.objectID in (select ga.goal.objectID from EdsGoalAssignees ga where ga.assignee.objectID=? and ga.deleted<>true)" +
                        " and (g.fromDate >=? ) and (g.toDate <=? ) " + sql, fp.getEmployeeId(), startYearDate, endYearDate);

    }

    @Override
    public List<EdsGoal> getDepartmentGoalsByDepartments(Set<Integer> ids) {

        EdsReference type = this.referenceManager.findReference(EdsGoal._GOAL_CATEGORY, EdsGoal.DEPARTMENT_GOAL);
        Query query = slaveEntityManager.createQuery("SELECT g FROM EdsGoal g WHERE g.deleted is not true AND g.goalCategory.objectID = :category AND g.department.objectID IN :ids ORDER BY g.weight DESC ")
                .setParameter("ids", ids)
                .setParameter("category", type.getObjectID());

        return query.getResultList();
    }

    @Override
    public List<EdsGoal>  getDepartmentGoalsByDepartment(Integer ids) {
        EdsReference type = this.referenceManager.findReference(EdsGoal._GOAL_CATEGORY, EdsGoal.DEPARTMENT_GOAL);
        Query query = slaveEntityManager.createQuery("SELECT g FROM EdsGoal g WHERE g.deleted is not true AND g.goalCategory.objectID = :category AND g.department.objectID = :id  ORDER BY g.weight DESC ")
                .setParameter("id", ids)
                .setParameter("category", type.getObjectID());

        return query.getResultList();
    }

    public void deleteGoal(EdsGoal goal) {
        update("update EdsGoal g set g.deleted=true where g=? and g.deleted<>true", goal);
    }

    @Override
    public Boolean isUsedValidityPeriod(EdsValidityPeriod validityPeriod) {
        Long count = (Long) findSingle("select count(g.objectID) from EdsGoal g where g.deleted<>true and validityPeriod=?", validityPeriod);
        return count > 0;
    }

    @Override
    public SelectItem[] getListAsSelectItems(ListingFilterParameter fp) {
        EdsReference goalType = switch (fp.getViewType()) {
            case Constants.PERSONAL_GOAL ->
                    referenceManager.findReference(EdsGoal._GOAL_CATEGORY, EdsGoal.PERSONAL_GOAL);
            case Constants.DEPARTMENT_GOAL ->
                    referenceManager.findReference(EdsGoal._GOAL_CATEGORY, EdsGoal.DEPARTMENT_GOAL);
            case Constants.PROJECT_GOAL -> referenceManager.findReference(EdsGoal._GOAL_CATEGORY, EdsGoal.PROJECT_GOAL);
            case Constants.BUSINESS_GOAL ->
                    referenceManager.findReference(EdsGoal._GOAL_CATEGORY, EdsGoal.BUSINESS_GOAL);
            default -> null;
        };
        if (goalType == null) {
            return new SelectItem[0];
        }
        StringBuilder sql = new StringBuilder();
        sql.append("select g.id, g.title from ").append(getCompanyId()).append(".goal g ");
        sql.append("left join ").append(getCompanyId()).append(".reference cat on cat.id = g.goalcategory_id ");
        sql.append("where (g.deleted is null or g.deleted is not true) ");
        sql.append("and cat.id = ").append(goalType.getObjectID());

        if (StringUtils.isNotBlank(fp.getSqlSearchKey())) {
            sql.append(" and (");
            sql.append(" lower(g.title) like lower('").append(fp.getSqlSearchKey()).append("')");
            sql.append(" or lower(g.title) like lower('").append(fp.getSqlSearchKey()).append("%')");
            sql.append(" or lower(g.title) like lower('%").append(fp.getSqlSearchKey()).append("%')");
            sql.append(")");
        }
        if (fp.getLimit() > 0) {
            sql.append(" offset ").append(fp.getStart()).append(" limit ").append(fp.getLimit());
        }

        List<Object[]> objects = findNative(sql.toString());
        List<SelectItem> result = new ArrayList<>();
        if (objects != null && !objects.isEmpty()) {
            objects.forEach(object -> {
                SelectItem item = new SelectItem();
                item.setId((Integer) object[0]);
                item.setName((String) object[1]);
                result.add(item);
            });
        }
        return result.toArray(new SelectItem[0]);
    }

    @Override
    public Integer getGoalLastIntNumber(String categoryType) {
        return (Integer) findSingle("select g.intNumber from EdsGoal g where g.intNumber is not null and g.goalCategory.code=? and g.deleted <> true order by g.intNumber desc", categoryType);
    }

    @Override
    public Boolean getGoalByNumberData(String numberString) {
        return findSingle("select g from EdsGoal g where g.deleted <> true and g.numberData='" + numberString + "'") != null;
    }

    @Override
    public Integer getDepartmentGoalAvailableWeight(Integer departmentId) {
        StringBuilder query = new StringBuilder();

        query.append("SELECT COALESCE(100 - SUM(weight), 100) ")
                .append("FROM ").append(getCompanyId()).append(".goal ")
                .append("WHERE deleted IS NOT TRUE ")
                .append("AND goalcategory_id = 446 ")
                .append("AND department_id = ").append(departmentId);

        Object resultObj = findNativeSingle(query.toString());
        if (resultObj == null) return 100;

        if (resultObj instanceof BigInteger bigIntResult) {
            return bigIntResult.intValue();
        }
        return (Integer) resultObj;
    }

}
