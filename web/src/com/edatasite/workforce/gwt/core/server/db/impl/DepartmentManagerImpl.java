package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsDepartmentTree;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeDepartment;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsReferenceLocale;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentTreeManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.LocationManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.team.client.rpc.TeamListItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.EMPLOYEE_STATUS;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.EMPLOYEE_STATUS_RESIGNED;

/**
 * Created by IntelliJ IDEA. User: mansur Date: Jan 8, 2008 Time: 5:57:01 PM To
 * change this template use File | Settings | File Templates.
 */
@Repository("departmentManager")
public class DepartmentManagerImpl extends BaseManager<EdsDepartment> implements
        DepartmentManager {
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private DepartmentTreeManager departmentTreeManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private LocationManager locationManager;
    @Autowired
    public GenericSettingsManager genericSettingsManager;

    public DepartmentManagerImpl() {
        super(EdsDepartment.class);
    }

    public List<EdsDepartment> list() {
        return list(new ListingFilterParameter());
    }

    /**
     * Returns the list of clients filtered by the given params and the viewer Role.
     * You can filter by Project, Department and Employee, if you don't want to filter them, just supply null values.
     * viewAsFilter - EdsRole.DR, ADMIN, TL, PM, MEM, CLIENT values can be supplied. if supplied value, it will isolate
     * the results for that role only. Supplying null will show only the related results for the current user.
     * Mostly null should be used for the viewer, but in reports.
     */
    public List<EdsDepartment> list(ListingFilterParameter fp) {
        return this.list(fp, false);
    }

    public List<EdsDepartment> list(List<Integer> depIds) {
        return (List<EdsDepartment>) findByNamedParams(
                "FROM EdsDepartment d WHERE d.deleted <> TRUE AND d.objectID IN (:ids)",
                Collections.singletonMap("ids", depIds)
        );
    }

    public List<EdsDepartment> list(ListingFilterParameter fp, boolean fromRU) {
        EdsUser user = getUser();
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        String companyId = getCompanyId();

        StringBuffer sql = null;
        sql = new StringBuffer();
        sql.append(" select distinct tem.id,tem.*");
        if (fp.getSortField() != null) {
            if (fp.getSortField().equals(TeamListItem.HEADCOUNT)) {
                sql.append(" , (select count(te1.id) from " + companyId + ".teamemployee te1 where te1.teamid = tem.id  and isdeleted <> true) headcount");
            }
            if (fp.getSortField().equals(TeamListItem.LEADER_NAME)) {
                sql.append(" , (select mu1.firstname from " + companyId + ".myuser mu1 where mu1.id = tem.leaderid) leader");
            }
            if (fp.getSortField().equals(TeamListItem.LEADER2_NAME)) {
                sql.append(" , (select mu1.firstname from " + companyId + ".myuser mu1 where mu1.id = tem.leaderId2) leader2");
            }
            if (fp.getSortField().equals(TeamListItem.LEADER3_NAME)) {
                sql.append(" , (select mu1.firstname from " + companyId + ".myuser mu1 where mu1.id = tem.leaderId3) leader3");
            }
            if (fp.getSortField().equals(TeamListItem.LEADER4_NAME)) {
                sql.append(" , (select mu1.firstname from " + companyId + ".myuser mu1 where mu1.id = tem.leaderId4) leader4");
            }
            if (fp.getSortField().equals(TeamListItem.LEADER5_NAME)) {
                sql.append(" , (select mu1.firstname from " + companyId + ".myuser mu1 where mu1.id = tem.leaderId5) leader5");
            }
        }
        sql.append(" from " + companyId + ".team tem ");
        sql.append(" left join " + companyId + ".teamemployee te on (tem.id=te.teamid and te.isdeleted<>true) ");
        sql.append(" left join " + companyId + ".myuser mu on (te.employeeid=mu.id) ");
        sql.append(" left join " + companyId + ".employee e on (e.id=te.employeeid ) ");
        sql.append(" left join " + companyId + ".projectemployee pe on(te.id=pe.employeedepartmentid ) ");
        sql.append(" left join " + companyId + ".project p on (p.id=pe.projectid) ");
        sql.append(" left join " + companyId + ".reference_locale rl ON rl.id = tem.localeid ");
        //Filter out the deleted items
        sql.append("where tem.isdeleted<>true ");


        // Filter for Client, Project, Employee
        if (fp.getClientId() != null && fp.getClientId() > 0) {
            sql.append(" and p.clientid=" + fp.getClientId() + " ");
        }
        if (fp.getProjectId() != null && fp.getProjectId() > 0) {
            sql.append(" and p.id=" + fp.getProjectId() + " ");
        }
        if (fp.getEmployeeId() != null && fp.getEmployeeId() > 0) {
            sql.append(" and e.id=" + fp.getEmployeeId() + " ");
        }
        if (fp.getLocationId() != null && fp.getLocationId() > 0) {
            sql.append(" and tem.locationId=" + fp.getLocationId() + " ");
        }
        if (fp.getStatusCode() != null) {
            sql.append(" and tem.active=").append(fp.getStatusCode().equals("ACTIVE")).append(" ");
        }
        if (fp.getObjectsIds() != null) {
            sql.append(" and tem.id in (").append(fp.getObjectsIds()).append(") ");
        }

        if (fromRU) {
            sql.append(employeeManager.getRolePermission());
        } else {
            //Filter for USER ROLE
            if (fp.getViewAsId() == null
                    || EdsRole.DEFAULT.equals(fp.getViewAsId())) {
                if (user.isClientContact()) {
                    sql.append(" or p.clientid=" + user.getClientContact().getClientID());
                } else {
                    sql.append(" and (tem.leaderid=" + user.getObjectID() + " or p.managerid=" + user.getObjectID() + " or p.backup_managerid=" + user.getObjectID());
                    sql.append(" or p.backup_managerid2=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid3=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid4=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid5=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid6=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid7=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid8=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid9=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid10=").append(user.getObjectID());
                    sql.append(" or e.id=" + user.getObjectID() + ")");
                }
            } else if (EdsRole.DR.equals(fp.getViewAsId()) || EdsRole.ADMIN.equals(fp.getViewAsId())
                    || ServerUtils.hasPermission(PermissionConstants.HRMS_SEE_ALL_DEPARTMENT_LIST)) {
                // if he is director or admin should see
                // all the projects of the company
            } else if (EdsRole.ADMIN_LOCATION.equals(fp.getViewAsId())) {
                EdsLocation location = user.getLocation();
                sql.append(" and (mu.locationId is not null)  and mu.locationId=" + (location != null ? location.getObjectID() : null));
            } else if (EdsRole.TL.equals(fp.getViewAsId())) {
                sql.append(" and (tem.leaderid=" + user.getObjectID() + ") ");
            } else if (EdsRole.PM.equals(fp.getViewAsId())) {
                sql.append(" and (p.managerid=" + user.getObjectID() + " or p.backup_managerid=" + user.getObjectID());
                sql.append(" or p.backup_managerid2=").append(user.getObjectID());
                sql.append(" or p.backup_managerid3=").append(user.getObjectID());
                sql.append(" or p.backup_managerid4=").append(user.getObjectID());
                sql.append(" or p.backup_managerid5=").append(user.getObjectID());
                sql.append(" or p.backup_managerid6=").append(user.getObjectID());
                sql.append(" or p.backup_managerid7=").append(user.getObjectID());
                sql.append(" or p.backup_managerid8=").append(user.getObjectID());
                sql.append(" or p.backup_managerid9=").append(user.getObjectID());
                sql.append(" or p.backup_managerid10=").append(user.getObjectID()).append(") ");
            } else if (EdsRole.MEM.equals(fp.getViewAsId())) {
                sql.append(" and e.id=" + user.getObjectID());
            } else if (EdsRole.CLIENT.equals(fp.getViewAsId()) || user.isClientContact()) {
                sql.append(" and (p.clientid=" + user.getClientContact().getClientID() + ") ");
            }
        }

        //Filter for SearchKey
        String nameLocale = "";
        String lang = ServerUtils.getUserLocale().getLanguage();
        switch (lang) {
            case "en" -> nameLocale += "COALESCE (rl.english, tem.name) ";
            case "ru" -> nameLocale += "COALESCE (rl.russian, tem.name) ";
            case "uz" -> nameLocale += "COALESCE (rl.uzbek, tem.name) ";
            case "ar" -> nameLocale += "COALESCE (rl.arabic, tem.name) ";
            default -> nameLocale += "tem.name";
        }
        if (fp.getSqlSearchKey() != null && !"".equals(fp.getSqlSearchKey())) {
            sql.append(" and (");
            sql.append("   lower(" + nameLocale + ") ilike '%" + fp.getSqlSearchKey().toLowerCase() + "%' ");
            sql.append("or lower(tem.numberdata) ilike '%" + fp.getSqlSearchKey().toLowerCase() + "%' ");
            sql.append(") ");
        }
        sql.append(" ORDER BY ");

        if (fp.getSortField() == null || "".equals(fp.getSortField())) {
            sql.append("tem.lastUpdateTime");
        } else if (fp.getSortField().equals(TeamListItem.NAME)) {
            sql.append("tem.name");
        } else if (fp.getSortField().equals(TeamListItem.START_DATE)) {
            sql.append("tem.startdate");
        } else if (fp.getSortField().equals(TeamListItem.LEADER_NAME)) {
            sql.append("leader");
        } else if (fp.getSortField().equals(TeamListItem.LEADER2_NAME)) {
            sql.append("leader2");
        } else if (fp.getSortField().equals(TeamListItem.LEADER3_NAME)) {
            sql.append("leader3");
        } else if (fp.getSortField().equals(TeamListItem.LEADER4_NAME)) {
            sql.append("leader4");
        } else if (fp.getSortField().equals(TeamListItem.LEADER5_NAME)) {
            sql.append("leader5");
        } else if (fp.getSortField().equals(TeamListItem.HEADCOUNT)) {
            sql.append("headcount");
        } else if (fp.getSortField().equals(TeamListItem.CODE)) {
            sql.append("numberData");
        } else {
            sql.append("tem.lastUpdateTime");
        }

        if (!fp.isAscending()) {
            sql.append(" DESC ");
        }

        return findNative(sql.toString(), EdsDepartment.class);
    }

    public List<EdsDepartment> getLastDepartments() {
        return findLimited("from EdsDepartment d where " +
                "(d.deleted=false or d.deleted is null) " +
                "order by d.objectID DESC", 10);
    }

    @Deprecated
    public List<EdsDepartment> getDepartmentsByRegDate(Date sTime, Date eTime, EdsCompany company, boolean includeUpdateTime) {
        return find("FROM EdsDepartment d where ((d.creationTime between '" + sTime + "' and '" + eTime + "') or (d.lastUpdateTime between '" + sTime + "' and '" + eTime + "'))");
    }

    public void updateTeamRole(Integer objectID) {
        EdsUser us = (EdsUser) findSingle("select u from EdsUser u where u.objectID=?", objectID);
        Set<EdsRole> roleList = us.getRoles();
        for (EdsRole role : roleList) {
            if (role.getObjectID().equals(EdsRole.TL)) {
                us.getRoles().remove(role);
                break;
            }
        }
        EdsRole newRole = (EdsRole) findSingle("select r from EdsRole r where r.objectID=?", EdsRole.MEM);
        us.getRoles().add(newRole);
    }

    public void deleteTeam(EdsDepartment department) {
        update("update EdsDepartment department set department.deleted=true " +
                "where department=?", department);
    }

    public List<EdsDepartment> getDepartmentByName(String name) {
        name = name.replace("'", "''");
        return (List<EdsDepartment>) findNative("select depN.* from  " + getCompanyId() + ".team depN where lower(depN.name)='" + name.toLowerCase() + "'  and depN.isDeleted=false", EdsDepartment.class);
    }

    @Override
    public SelectItem[] getDepartmentListWithDistinctName() {
        String nameLocale = "";
        StringBuilder sql = new StringBuilder();
        String lang = ServerUtils.getUserLocale().getLanguage();
        switch (lang) {
            case "en":
                nameLocale += "COALESCE (rl.english, t.name) ";
                break;
            case "ru":
                nameLocale += "COALESCE (rl.russian, t.name) ";
                break;
            case "uz":
                nameLocale += "COALESCE (rl.uzbek, t.name) ";
                break;
            case "ar":
                nameLocale += "COALESCE (rl.arabic, t.name) ";
                break;
            default:
                nameLocale += "t.name";
                break;
        }
        sql.append("SELECT DISTINCT ON (" + nameLocale + ") t.id, " + nameLocale + " FROM " + getCompanyId() + ".team t left join " + getCompanyId() + ".reference_locale rl on rl.id = t.localeid WHERE isdeleted IS NOT TRUE ");
        List<Object[]> objects = findNative(sql.toString());
        SelectItem[] resultItems = new SelectItem[objects.size()];
        int i = 0;
        for (Object[] object : objects) {
            resultItems[i++] = new SelectItem((Integer) object[0], (String) object[1]);
        }

        return resultItems;
    }

    @Override
    public List<EdsDepartment> getDepartmentListByName(String name) {
        String nameLocale = "";
        String lang = ServerUtils.getUserLocale().getLanguage();
        switch (lang) {
            case "en":
                nameLocale += "rl.english";
                break;
            case "ru":
                nameLocale += "rl.russian";
                break;
            case "uz":
                nameLocale += "rl.uzbek";
                break;
            case "ar":
                nameLocale += "rl.arabic";
                break;
            default:
                nameLocale += "t.name";
                break;
        }
        String sql = "SELECT * FROM " +
                getCompanyId() +
                ".team t " +
                "LEFT JOIN " +
                getCompanyId() +
                ".reference_locale rl ON t.localeid = rl.id " +
                "WHERE isdeleted IS NOT TRUE AND ((" +
                "t.localeid IS NULL AND t.name = '" + name + "')" +
                "OR" +
                "(t.localeid IS NOT NULL AND " + nameLocale + "= '" + name + "'));";

        return (List<EdsDepartment>) findNative(sql, EdsDepartment.class);
    }

    public List<EdsDepartment> getDepartmentByCode(String code) {
        code = code.replace("'", "''");
        return (List<EdsDepartment>) findNative("select depN.* from  " + getCompanyId() + ".team depN where lower(depN.numberData)='" + code.toLowerCase() + "'  and depN.isDeleted=false", EdsDepartment.class);
    }

    public List<EdsDepartment> getDepartmentByNameAndId(String name, Integer id) {
        return find("select depN from EdsDepartment depN where depN.name = ? and depN.deleted=false and depN.objectID<> ?", name, id);
    }

    public List<EdsDepartment> getDepartmentByCodeAndId(String code, Integer id) {
        return find("select depN from EdsDepartment depN where depN.numberData = ? and depN.deleted=false and depN.objectID<> ?", code, id);
    }

    public List<EdsDepartment> getTeamsByEmployeeId(Integer employeeId) {
        return find("select team from EdsDepartment team \n" +
                "where (team.leader.objectID=? " +
                "or team.leader2.objectID=? " +
                "or team.leader3.objectID=? " +
                "or team.leader4.objectID=? " +
                "or team.leader5.objectID=?) and team.deleted<>true order by team.startDate desc", employeeId, employeeId, employeeId, employeeId, employeeId);
    }

    public void removeTeamLeaderAndMoveNewEmployee(String ids, EdsEmployee employee) {
        update("update EdsDepartment team set team.leader=? " +
                "where team.objectID in (" + ids + ")", employee);
    }

    @Override
    public EdsDepartment getDepartmentByLeader(EdsUser user) {
        return (EdsDepartment) findSingle("from EdsDepartment WHERE deleted<>true AND leader = ? ", user);
    }

    @Override
    public Integer getDepartmentLastIntNumber() {
        return (Integer) findSingle("select d.intNumber from EdsDepartment d where (d.deleted = false or d.deleted is null) and d.intNumber is not null order by d.intNumber desc");
    }

    @Override
    public List<EdsDepartment> getUserDepartments(EdsUser user) {
        if (user != null) {
            return (List<EdsDepartment>) find("from EdsDepartment WHERE deleted<>true AND leader = ? ", user);
        }
        return null;
    }

    public List<EdsDepartment> getCompanyDepartments(EdsCompany company) {
        String companyId = "\"" + company.getObjectID() + "\"";
        return (List<EdsDepartment>) findNative("select d.* from " + companyId + ".team d where d.isdeleted<>true order by d.name asc", EdsDepartment.class);
    }

    @Override
    public List<Object[]> getListByCode() {
        return findNative("select lower(t.numberdata), t.id, t.locationid from " + getCompanyId() + ".team t where t.isdeleted is not true");
    }

    @Override
    public EdsReferenceLocale getDeparmentLocalization(Integer deparmentId) {
        if (deparmentId == null) {
            return null;
        }
        String query = "select rl.* from " + getCompanyId() + ".team d LEFT JOIN " +
                getCompanyId()+".reference r on r.id = d.departmentNameId LEFT JOIN "+
                getCompanyId() + ".reference_locale rl ON rl.id = r.localeId" +
                " where(d.isdeleted is null or d.isdeleted<> true) " +
                " and d.id = " + deparmentId;

        return (EdsReferenceLocale) findNativeSingle(query, EdsReferenceLocale.class);
    }

    @Override
    public EdsReferenceLocale getDepartmentLocalizationByReferenceId(Integer deparmentId) {
        if (deparmentId == null) {
            return null;
        }
        String query = "select rl.* from " + getCompanyId() + ".team d LEFT JOIN " +
                getCompanyId() + ".reference r ON r.id = d.departmentnameid" + " LEFT JOIN " +
                getCompanyId() + ".reference_locale rl ON rl.id = r.localeid" +
                " where(d.isdeleted is null or d.isdeleted<> true) " +
                " and d.id = " + deparmentId;

        return (EdsReferenceLocale) findNativeSingle(query, EdsReferenceLocale.class);
    }

    @Override
    public SelectItem[] getDepartmentsForAccounting(ListingFilterParameter filterParametrs) {
        Integer companyID = SecurityContext.getCompanyID();
        StringBuilder query = new StringBuilder();
        String lang = ServerUtils.getUserLocale().getLanguage();
        String sql = "";
        switch (lang) {
            case "en" -> sql += "COALESCE(COALESCE (rl.english, d.name) , COALESCE (rl.english, d.name))";
            case "ru" -> sql += "COALESCE(COALESCE (rl.russian, d.name) , COALESCE (rl.english, d.name))";
            case "uz" -> sql += "COALESCE(COALESCE (rl.uzbek, d.name) , COALESCE (rl.uzbek, d.name))";
            case "ar" -> sql += "COALESCE(COALESCE (rl.arabic, d.name) , COALESCE (rl.arabic, d.name))";
            default -> sql += "d.name";
        }
        query.append("select distinct d.id, ").append(sql).append(",d.numberdata ").append(" from \"").append(companyID).append("\".team d LEFT JOIN ").append(getCompanyId()).append(".reference_locale rl ON rl.id = d.localeid").
        append(" LEFT JOIN ").append(getCompanyId()).append(".reference rn on rn.id = d.departmentNameId ")
                .append(" LEFT JOIN ").append(getCompanyId()).append(".reference_locale rnl on rn.localeId = rnl.id ")
                .append(" where(d.isdeleted is null or d.isdeleted<> true) ");

        String searchKey = filterParametrs.getSqlSearchKey();
        if (searchKey != null) {
            searchKey = searchKey.toLowerCase();
            query.append(" and (lower(").append(sql).append(") like '").append(searchKey).append("' ");
            query.append("or lower(").append("d.numberdata").append(") like '").append(searchKey).append("' ");
            query.append(" or lower(").append("rn.name").append(") like '").append(searchKey).append("' ) ");
        }
        if (filterParametrs.isShowDepartment()) {
            if (filterParametrs.getDepartmentId() != null) {
                List<Integer> childList = departmentTreeManager.getChildList(filterParametrs.getDepartmentId());
                childList.add(filterParametrs.getDepartmentId());
                String ids = ServerUtils.integerListToString(childList);
                query.append(" and d.id not in (").append(ids).append(")");
            }
        }
        if (searchKey == null && filterParametrs.isFromMultiDepartment()) {
            if (filterParametrs.getDepartmentId() != null) {
                List<Integer> childList = departmentTreeManager.getAncestorsAndChildren(filterParametrs.getDepartmentId());
                String ids = ServerUtils.integerListToString(childList);
                query.append(" and d.id in (").append(ids).append(")");
            }
        }
        if (filterParametrs.getLocationId() != null) {
            ArrayList<Integer> departmentsIdByLocationId = getDepartmentsIdByLocationId(filterParametrs.getLocationId());
            String ids = ServerUtils.getAsCommoDelimited(departmentsIdByLocationId, "0", ",");
            query.append(" and d.id in (").append(ids).append(")");
        }

        query.append(" order by (").append(sql).append(") asc");
        List<Object[]> dataList = findNativeLimited(query.toString(), filterParametrs.getLimit() != null ? filterParametrs.getLimit() : 20);

        int i = 0;
        SelectItem[] resultItems = new SelectItem[dataList.size()];
        for (Object[] data : dataList) {
            String code = data[2] != null && !"".equals(data[2]) ? (String) data[2] : "";
            String number = !"".equals(code.replace("null", "").trim()) ? code + " -> " : "";
            resultItems[i++] = new SelectItem((Integer) data[0], number + data[1]);
        }
        return resultItems;
    }

    @Override
    public ArrayList<Integer> getDepartmentsIdByLocationId(Integer locationId) {

        StringBuilder sql = new StringBuilder(
                "SELECT depN.id FROM " + getCompanyId() + ".team depN " +
                        "WHERE depN.isDeleted = FALSE AND depN.locationId IN ("
        );

        sql.append(locationId);

        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_PARENT_LOCATION_DEPENDENT_DEPARTMENTS)) {
            EdsLocation location = locationManager.get(locationId);
            if (location != null && location.getParent() != null && location.getParent().getObjectID() != null) {
                sql.append(", ").append(location.getParent().getObjectID());
            }
        }

        sql.append(")");

        return (ArrayList<Integer>) findNative(sql.toString());
    }

    @Override
    public Map<String, Integer> getDepartmentAsMap() {
        List<Object[]> list = findNative("select d.name, d.id from " + getCompanyId() + ".team d where (d.isdeleted is null or d.isdeleted<>true)");

        Map<String, Integer> map = new HashMap<>();
        if (list != null && !list.isEmpty()) {
            for (Object[] objects : list) {
                if (objects[0] != null && objects[1] != null) {
                    map.put((String) objects[0], (Integer) objects[1]);
                }
            }
            return map;
        }
        return null;
    }

    @Override
    public ArrayList<Integer> getEmployeeIDsByTeamLeader(Integer employeeId) {
        String sql = "select distinct te.employeeid from " + getCompanyId() + ".teamemployee te " +
                "where te.isdeleted is not true " +
                "and te.teamid in (" + "select t.id from " + getCompanyId() + ".team t where t.isdeleted is not true and t.leaderid=" + employeeId + ")";
        return (ArrayList<Integer>) findNative(sql);
    }

    @Override
    public void create(EdsDepartment obj) {
        super.create(obj);
        /* Create first tree depth */
        EdsDepartmentTree edsDepartmentTree = new EdsDepartmentTree();
        edsDepartmentTree.setParentId(obj.getObjectID());
        edsDepartmentTree.setChildId(obj.getObjectID());
        edsDepartmentTree.setDepth(0);
        super.createObject(edsDepartmentTree);
    }

    @Override
    public HashMap<Integer, String> getDepartmentNamesMapByIds(String departmentIds) {
        StringBuilder sql = new StringBuilder();
        String lang = ServerUtils.getUserLocale().getLanguage();
        String localeSql = "";
        switch (lang) {
            case "en" -> localeSql += "COALESCE (rl.english, d.name)";
            case "ru" -> localeSql += "COALESCE (rl.russian, d.name)";
            case "uz" -> localeSql += "COALESCE (rl.uzbek, d.name)";
            case "ar" -> localeSql += "COALESCE (rl.arabic, d.name)";
            default -> localeSql += "d.name";
        }
        sql.append("select distinct d.id, ").append(localeSql).append(" from ").append(getCompanyId())
                .append(".team d LEFT JOIN ").append(getCompanyId())
                .append(".reference_locale rl ON rl.id = d.localeid")
                .append(" where (d.isdeleted is null or d.isdeleted<>true) and d.id in (" + departmentIds + ")");
        List<Object[]> list = findNative(sql.toString());
        HashMap<Integer, String> map = new HashMap<>();
        if (list != null && !list.isEmpty()) {
            for (Object[] objects : list) {
                if (objects[0] != null && objects[1] != null) {
                    map.put((Integer) objects[0], (String) objects[1]);
                }
            }
            return map;
        }
        return new HashMap<>();
    }

    @Override
    public EdsDepartment getDepartmentByUniqueId(String uniqueId) {
        return (EdsDepartment) findNativeSingle("select * from " + getCompanyId() + ".team where externalGUID = '" + uniqueId + "'", EdsDepartment.class);
    }

    @Override
    public List<EdsDepartment> getDepartmentByLocationID(Integer id) {
        if (id == null) {
            return (List<EdsDepartment>) findNative("select depN.* from  " + getCompanyId() + ".team depN where depN.locationId is null and depN.isDeleted=false", EdsDepartment.class);
        } else {
            return (List<EdsDepartment>) findNative("select depN.* from  " + getCompanyId() + ".team depN where depN.locationId=" + id + "  and depN.isDeleted=false", EdsDepartment.class);
        }
    }

    @Override
    public void updateTeamLocation(HashSet<Integer> teamsId, EdsLocation location) {
        ArrayList<Integer> teamId = new ArrayList<>(teamsId);
        update("update EdsDepartment d set d.location = ? where d.objectID in (" + ServerUtils.getAsCommoDelimited(teamId, "0") + ")", location);
    }

    @Override
    public SelectItem[] getDepartmentsByLocationAsSelectItem(Integer locationId) {
        List<EdsDepartment> departmentList = (List<EdsDepartment>) findNative("select depN.* from  " + getCompanyId() + ".team depN where depN.locationId=" + locationId + "  and depN.isDeleted=false", EdsDepartment.class);
        String lang = ServerUtils.getUserLocale().getLanguage();
        List<SelectItem> list = new ArrayList<>();
        for (EdsDepartment department : departmentList) {
            SelectItem asSelectItem = department.getAsSelectItem(lang);
            list.add(asSelectItem);
        }
        return list.toArray(new SelectItem[]{});
    }

    @Override
    public boolean isDepartmentNumberExist(String numberString, Integer objectID) {
        List numberList;
        if (objectID != null) {
            numberList = find("select d.intNumber from EdsDepartment d where (d.deleted = false or d.deleted is null)  " + " and d.numberData = ? and d.objectID <> ? ", numberString, objectID);
        } else {
            numberList = find("select d.intNumber from EdsDepartment d where (d.deleted = false or d.deleted is null)  " + " and d.numberData = ?", numberString);
        }
        return numberList != null && !numberList.isEmpty();
    }

    @Override
    public List<EdsDepartment> getDepartmentsForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select p from EdsDepartment p ");
        sql.append(" where  ").append(ServerUtils.checkForDeleted("p.deleted"));
        if (!solrReindex.isAllReindex() && solrReindex.getLastUpdateTime() != null) {
            params.put("modifiedDate", solrReindex.getLastUpdateTime());
            sql.append(" and p.lastUpdateTime >= :modifiedDate");
            if (solrReindex.getLastUpdateEndTime() != null) {
                sql.append(" and p.lastUpdateTime<='").append(solrReindex.getLastUpdateEndTime()).append("'");
            }
        }
        sql.append(" order by p.objectID ");

        return findIntervalByNamedParams(sql.toString(), start, limit, params);
    }

    @Override
    public List<Integer> getDepartmentIdsByIds(String ids) {
        return find("SELECT c.objectID FROM EdsDepartment c WHERE c.objectID IN(" + ids + ") and " + ServerUtils.checkForDeleted("c.deleted"));
    }

    @Override
    public List<Integer> getDepartmentIdsWithLimit(Integer start, Integer limit) {
        return findInterval("select c.objectID from EdsDepartment c where " + ServerUtils.checkForDeleted("c.deleted"), start, limit);
    }

    @Override
    public Map<Integer, Integer> getLocationAndTeamSize() {
        Map<Integer, Integer> locationSizeMap = new HashMap<>();
        List<Object[]> objects = findNative("SELECT locationid, COUNT(*) AS team_size FROM " + getCompanyId() + ".team WHERE isdeleted IS NOT TRUE GROUP BY locationid ");
        for (Object[] object : objects) {
            Integer locationId = (Integer) object[0];
            BigInteger teamSize = (BigInteger) object[1];
            locationSizeMap.put(locationId, teamSize.intValue());
        }
        return locationSizeMap;
    }

    @Override
    public Boolean hasDepartmentsWithLocation() {
        return (Boolean) findNativeSingle("select count(*) > 0 from " + getCompanyId() + ".team where locationid IS NOT NULL");
    }

    @Override
    public ArrayList<SelectItem> getReferenceRelatedDepartments(Integer referenceId) {
        ArrayList<SelectItem> departmentsList = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("select id,numberdata from  ").append(getCompanyId());
        query.append(".team where localeId in ( ");
        query.append(" select rl.id from ").append(getCompanyId()).append(".reference r ");
        query.append(" left join ").append(getCompanyId()).append(".reference_locale rl ");
        query.append(" on r.localeId = rl.id");
        query.append(" where r.id = ").append(referenceId).append(")");

        List<Object[]> items = (List<Object[]>) findNative(query.toString());
        for (Object[] item : items) {
            departmentsList.add(new SelectItem((Integer) item[0],(String) item[1]));
        }
        return departmentsList;
    }

    @Override
    public ArrayList<EdsEmployeeDepartment> getCompanyDepartments(ListingFilterParameter fp) {
        EdsReference inactive = referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_RESIGNED);

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select d.* from ").append(getCompanyId()).append(".teamEmployee d ");
        queryBuilder.append(" left join ").append(getCompanyId()).append(".myuser my on d.employeeId = my.id ");
        queryBuilder.append(" where d.isdeleted <> true ");
        queryBuilder.append("       and my.deleted <> true ");
        queryBuilder.append("       and my.accountStatusid <> ").append(inactive.getObjectID());
        if (fp.getSearchKey() != null) {
            queryBuilder.append(" and (my.firstname ilike '%").append(fp.getSearchKey()).append("%' ");
            queryBuilder.append(" or my.lastname ilike '%").append(fp.getSearchKey()).append("%' ");
            queryBuilder.append(" or my.middlename ilike '%").append(fp.getSearchKey()).append("%') ");
        }
        return (ArrayList<EdsEmployeeDepartment>) findNative(queryBuilder.toString(),EdsEmployeeDepartment.class);
    }

    public List<Integer> getCompanyDeletedDepartmentsForSolr(SolrReindexRpc solrReindex) {
        StringBuilder newsSqlQuery = new StringBuilder("SELECT ns.objectID FROM EdsDepartment ns WHERE ns.deleted=true");
        newsSqlQuery.append(" AND ns.lastUpdateTime>=").append("'").append(solrReindex.getLastUpdateTime()).append("'");
        if (solrReindex.getLastUpdateEndTime() != null) {
            newsSqlQuery.append(" and ns.lastUpdateTime<='").append(solrReindex.getLastUpdateEndTime()).append("'");
        }
        return (List<Integer>) find(newsSqlQuery.toString());
    }

    public List<EdsEmployee> getVacants(Integer departmentId) {
        return (List<EdsEmployee>) find("SELECT d.vacants FROM EdsDepartment d WHERE (d.deleted = false or d.deleted is null) and d.objectID = ?", departmentId);
    }
}
