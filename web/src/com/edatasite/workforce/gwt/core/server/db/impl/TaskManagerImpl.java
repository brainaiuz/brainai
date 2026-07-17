package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.core.solr.component.TaskSolrComponent;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.WbsItem;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateTypeManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA. User: iskan Date: Jan 2, 2008 Time: 11:37:19 PM To
 * change this template use File | Settings | File Templates.
 */
@Repository("taskManager")
public class TaskManagerImpl extends AttachmentSupportManager<EdsTask> implements TaskManager, Constants {

    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private TaskSolrComponent taskSolrComponent;

    public void setMyUpdateManager(MyUpdateManager myUpdateManager) {
        this.myUpdateManager = myUpdateManager;
    }

    public void setReferenceManager(ReferenceManager referenceManager) {
        this.referenceManager = referenceManager;
    }

    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    public TaskManagerImpl() {
        super(EdsTask.class);
    }

    public List<EdsTask> list() {
        return list(new ListingFilterParameter());
    }

    public List<EdsTask> getProjectTasks(Integer projectID, Integer start, Integer limit) {
        String companyID = ServerSecurityContext.getInstance().getCompanyId();
        String query = "select t.* ,0 as clazz_ from \"" + companyID + "\".task t left outer join \"" + companyID + "\".project p on t.projectid = p.id " +
                "where t.id>" + start + " and t.deleted is not true and p.id =" + projectID + " order by t.id asc limit " + limit;
        return findNative(query, EdsTask.class);
    }

    @Override
    public List<EdsTask> getProjectTasksByIntervalWithoutWS(Integer projectID, Integer start, Integer limit) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT t FROM EdsTask t ");
        sql.append("LEFT JOIN t.project p ");
        sql.append("WHERE t.deleted is not true ");
        sql.append("AND t.parent is null ");
        sql.append("AND p.objectID = '" + projectID + "' ");
        sql.append("ORDER BY t.objectID ");

        return findInterval(sql.toString(), start, limit);
    }

    @Override
    public List<EdsTask> getWorkStreamTasksByInterval(Integer workStreamID, Integer start, Integer limit) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT t FROM EdsTask t ");
        sql.append("LEFT JOIN t.parent p ");
        sql.append("WHERE t.deleted is not true ");
        sql.append("AND p.objectID = '" + workStreamID + "' ");
        sql.append("ORDER BY t.objectID ");
        return findInterval(sql.toString(), start, limit);
    }


    /**
     * <b> This is method get all delete tasks by lastUpdateTime... </b>
     * <p/>
     * <i> Write by Dilshod.T </i>
     *
     * @param solrReindex
     * @return
     */
    public List<Integer> getCompanyDeleteTasksForSolr(SolrReindexRpc solrReindex) {
        StringBuilder taskSqlQuery = new StringBuilder("select t.id from " + getCompanyId() + ".task t ");
        taskSqlQuery.append("where t.deleted=true and t.isissue is not true and t.lastUpdateTime>=").append("'").append(solrReindex.getLastUpdateTime()).append("'");
        if (solrReindex.getLastUpdateEndTime() != null) {
            taskSqlQuery.append(" and t.lastUpdateTime<='").append(solrReindex.getLastUpdateEndTime()).append("'");
        }
        return (List<Integer>) findNative(taskSqlQuery.toString());
    }

    /**
     * <b> This is method uses in task solr reindex.
     * Please not changed this is logic,
     * becaus task solr reindex working with error ... </b>
     * <p/>
     * <i> Write by Dilshod.T </i>
     *
     * @param solrReindex
     * @param start
     * @param limit
     * @return
     */
    public List<EdsTask> getCompanyTasksForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder query = new StringBuilder("select t from EdsTask t ");
        query.append("where (t.deleted is null or t.deleted <> true) and (t.isIssue is null or t.isIssue <> true) ");
        if (!solrReindex.isAllReindex() && solrReindex.getLastUpdateTime() != null) {
            params.put("updatedAt", solrReindex.getLastUpdateTime());
            query.append("and t.lastUpdateTime >= :updatedAt");
            if (solrReindex.getLastUpdateEndTime() != null) {
                query.append(" and t.lastUpdateTime<='").append(solrReindex.getLastUpdateEndTime()).append("'");
            }
        }
        query.append(" order by t.id ");
        return findIntervalByNamedParams(query.toString(), start, limit, params);
    }

    public List<EdsTask> listByParentId(Integer parentId) {
        return find("select t from EdsTask t " +
                "where t.parent.objectID=? and t.deleted = false " +
                "order by t.objectID asc", parentId);
    }


    public static class TaskSearchResult {

        private List<EdsTask> tasks;
        private int totalCount;

        public TaskSearchResult(List<EdsTask> tasks, int totalCount) {
            super();
            this.tasks = tasks;
            this.totalCount = totalCount;
        }

        public List<EdsTask> getTasks() {
            return tasks;
        }

        public int getTotalCount() {
            return totalCount;
        }
    }

    public TaskSearchResult findByKeyword(Integer projectId, String[] fields, String keyword, ListingFilterParameter fp,
                                          ListLoadConfig config) throws EdsDbException {
        EdsUser user = getUser();
        String filtername = null;
        if (projectId != null) {
            if (fp == null) {
                fp = new ListingFilterParameter();
            }
            fp.setProjectId(projectId);
            fp.setTaskStatusId(null);
            fp.setSearchKey(keyword);
            fp.setViewAsId(EdsRole.DR);
        } else {
            ListingFilterParameter fpPr = new ListingFilterParameter();
            fpPr.setEmployeeId(user.getEmployee().getObjectID());
            fpPr.setViewAsId(EdsRole.DR);
            List<EdsProject> projectList = projectManager.list(fpPr, user);
            Integer[] projects = new Integer[projectList.size()];
            int i = 0;
            for (EdsProject project : projectList) {
                projects[i] = project.getObjectID();
                i++;
            }
        }
        List<EdsTask> result = new ArrayList<>(list(fp));

        int start = config.getStart();
        int end = config.getStart() + config.getLimit();
        if (end > result.size()) {
            end = result.size();
        }

        return new TaskSearchResult(result.subList(start, end), result.size());
    }

    public List<EdsProjectEmployee> getTaskAssignees(Integer taskId) {

        return find("select distinct et.projectEmployee from EdsEmployeeTask et" +
                " where et.task.objectID=? and et.status.code<>? and et.deleted=false", taskId, EdsTask.CANCELLED);
    }

    @Override
    public EdsTask getTaskByIssueId(Integer issueId) {
        return (EdsTask) findSingle("SELECT task FROM EdsTask task WHERE task.edsIssue.objectID = ? AND task.deleted = false", issueId);
    }

    @Override
    public EdsEmployeeTask getEmployeeTask(Integer employeeId, Integer taskId) {
        String schema = SecurityContext.getInstance().getCompanyId();

        String sql = "SELECT et.* FROM \"" + schema + "\".employeetask et " +
                "LEFT JOIN \"" + schema + "\".projectemployee pe ON et.projectemployeeid = pe.id " +
                "LEFT JOIN \"" + schema + "\".teamemployee te ON pe.employeedepartmentid = te.id " +
                "WHERE " +
                "    et.taskid = '" + taskId + "' " +
                "    AND te.employeeid = '" + employeeId + "' ";

        List employeeTasks = findNative(sql, EdsEmployeeTask.class);

        if (employeeTasks != null && employeeTasks.size() > 0) {
            return (EdsEmployeeTask) employeeTasks.get(0);
        }

        return null;
    }

    public List<EdsTask> findOrphanTasks(Integer projectId) {
        return find("select t from EdsTask t where t.project.objectID = ? " +
                " and t.deleted=false and (t.isIssue is null or t.isIssue is not true) and t.parent is null order by t.startDate desc ", projectId);
    }

    public List<EdsTask> findOrphanTasks(ListingFilterParameter filterParameter) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.*, 0 as clazz_ from ").append(getCompanyId()).append(".task t ");
        sql.append(" left join ").append(getCompanyId()).append(".project p on t.projectid = p.id ");
        sql.append(" where t.deleted is not true and t.isIssue is not true ");
        if (filterParameter.getProjectId() != null) {
            sql.append(" and p.id = ").append(filterParameter.getProjectId());
        }
        if (StringUtils.isNotBlank(filterParameter.getSearchKey())) {
            sql.append(" and lower(t.name) like '").append(filterParameter.getSqlSearchKey()).append("'");
        } else {
            sql.append(" and t.parentwsid is null");
        }

        sql.append(" order by ");
        if (WbsItem.NAME.equals(filterParameter.getSortField())) {
            sql.append(" t.name ");
        } else if (WbsItem.START_DATE.equals(filterParameter.getSortField())) {
            sql.append(" t.startDate ");
        } else if (WbsItem.END_DATE.equals(filterParameter.getSortField())) {
            sql.append(" t.dueDate ");
        } else {
            sql.append(" t.startDate ");
        }
        sql.append(filterParameter.getSortDir() == 1 ? "ASC " : "DESC");

        return findNative(sql.toString(), EdsTask.class);

    }

    public List<EdsTask> findOrphanTasksForGanttChart(Integer projectId, Integer employeeId, Date from, Date to, String sortBy) {
        return find("select distinct t from EdsEmployeeTask et right join et.task t " +
                "where t.project.objectID = ? and t.deleted <> true and (t.isIssue is null or t.isIssue = false) " +
                (employeeId != null && employeeId != 0
                        ? "and et.projectEmployee.employeeDepartment.employee.objectID=" + employeeId + " "
                        : " ") +
                "and t.parent is null and (t.dueDate>=? and t.startDate<=?) order by t.taskGanttOrder, t." + sortBy + ", t.name asc", projectId, from, to);
    }

    public List<EdsTask> findOrphanTasksForGanttChart(Integer projectId, Integer employeeId, Date from, Date to, String sortBy, Integer start, Integer limit) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT t.id, t.*, 0 as clazz_ FROM " + getCompanyId() + ".employeetask et ");
        sql.append("RIGHT JOIN ").append(getCompanyId()).append(".task t ON t.id = et.taskid ");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".project p ON p.id = t.projectid ");

        if (employeeId != null && employeeId != 0) {
            sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".projectemployee pe ON pe.id = et.projectemployeeid ");
            sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".teamEmployee te ON te.id = pe.employeeDepartmentId ");
        }
        sql.append("WHERE t.deleted is not true AND t.isIssue is not true ");
        sql.append("AND p.id = ").append(projectId).append(" ");

        if (employeeId != null && employeeId != 0) {
            sql.append("AND te.employeeid = ").append(employeeId).append(" AND et.deleted=false ");
        }

        sql.append("AND t.parentwsid is null ");
        sql.append("AND t.dueDate >= '").append(dateFormat.format(from)).append("' AND t.startDate <= '").append(dateFormat.format(to)).append("' ");
        sql.append("ORDER BY t.taskGanttOrder, t." + sortBy + ", t.name ");

        if (start != null && limit != null) {
            sql.append(" OFFSET " + start + " LIMIT " + limit);
        }

        return findNative(sql.toString(), EdsTask.class);
    }

    public LinkedHashMap<Integer, List<EdsTask>> findTasksForGanttChart(Integer projectId, Integer employeeId, Date from, Date to) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT t.id, t.*, 0 as clazz_ FROM " + getCompanyId() + ".employeetask et ");
        sql.append("RIGHT JOIN ").append(getCompanyId()).append(".task t ON t.id = et.taskid ");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".project p ON p.id = t.projectid ");

        if (employeeId != null && employeeId != 0) {
            sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".projectemployee pe ON pe.id = et.projectemployeeid ");
            sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".teamEmployee te ON te.id = pe.employeeDepartmentId ");
        }
        sql.append("WHERE t.deleted is not true AND t.isIssue is not true ");
        sql.append("AND p.id = ").append(projectId).append(" ");

        if (employeeId != null && employeeId != 0) {
            sql.append("AND te.employeeid = ").append(employeeId).append(" AND et.deleted=false ");
        }

        sql.append("AND t.dueDate >= '").append(dateFormat.format(from)).append("' AND t.startDate <= '").append(dateFormat.format(to)).append("' ");
        sql.append("ORDER BY t.startDate, t.dueDate ");

        List<EdsTask> taskList = findNative(sql.toString(), EdsTask.class);

        LinkedHashMap<Integer, List<EdsTask>> result = new LinkedHashMap<>();
        for (EdsTask task : taskList) {
            Integer key = task.getParent() != null ? task.getParent().getObjectID() : 0;

            List<EdsTask> list = result.getOrDefault(key, new ArrayList<>());
            list.add(task);
            result.put(key, list);
        }

        return result;
    }

    public List<EdsTask> getTodoListTasks(ListingFilterParameter filterParametrs) {
        EdsReference closed = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.CLOSED);
        EdsReference completed = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.COMPLETED);

        String schemaName = getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct ta.id, ta.*, 0 as clazz_ ");
        sql.append(" from ").append(schemaName).append(".task ta \n");
        sql.append(" left outer join ").append(schemaName).append(".employeetask et on (et.taskid=ta.id ) \n");
        sql.append(" left outer join ").append(schemaName).append(".projectemployee pe on(et.projectemployeeid=pe.id ) \n");
        sql.append(" left outer join ").append(schemaName).append(".project p on (pe.projectid=p.id ) \n");
        sql.append(" left outer join ").append(schemaName).append(".teamemployee te on (pe.employeedepartmentid=te.id) \n");
        sql.append(" left outer join ").append(schemaName).append(".employee e on (e.id=te.employeeid) \n");
        sql.append(" where p.isdeleted<>true and ta.deleted<>true and et.deleted <> true \n");
        sql.append(" and (ta.isissue is null or ta.isissue<>true) and ta.todoListOrder is not null \n");
        if (filterParametrs.getProjectId() != null) {
            sql.append(" and p.id=").append(filterParametrs.getProjectId());
        }
        if (filterParametrs.getEmployeeId() != null) {
            sql.append(" and e.id=").append(filterParametrs.getEmployeeId());
        }
        if (closed != null) {
            sql.append(" and et.statusid<>").append(closed.getObjectID());
        }
        if (completed != null) {
            sql.append(" and et.statusid<>").append(completed.getObjectID()).append("\n");
        }
        sql.append(" order by ta.todoListOrder asc ");
        if (filterParametrs.getLimit() != null && filterParametrs.getLimit() > 0) {
            sql.append(" offset 0 limit ").append(filterParametrs.getLimit());
        } else {
            sql.append(" limit 100");
        }
        return findNative(sql.toString(), EdsTask.class);
    }

    private Date getDayLastTime(Date date) {
        return date == null
                ? null
                : new Date(date.getYear(), date.getMonth(), date.getDate() + 1, date.getHours(), date.getMinutes(), date.getSeconds());
    }

    @Override
    public List<EdsTask> workflowTaskList(ListingFilterParameter filterParameter) {
        StringBuilder sql = new StringBuilder();
        Integer workflowID = filterParameter.getWorkflowID();
        if (workflowID == null) {
            if (filterParameter.getRelationID() != null && filterParameter.getRelationType() != null && filterParameter.getRelationType().equals(RelationItem.TYPE_WORKFLOW)) {
                workflowID = filterParameter.getRelationID();
            }
        }
        sql.append("select distinct t.id, t.* , 0 as clazz_ from ").append(getCompanyId()).append(".task t").append(" where t.workflowID = " + workflowID);
        return findNative(sql.toString(), EdsTask.class);
    }

    /**
     * Returns the list of clients filtered by the given params and the viewer Role.
     * You can filter by Project, Department and Employee, if you don't want to filter them, just supply null values.
     * viewAsFilter - EdsRole.DR, ADMIN, TL, PM, MEM, CLIENT values can be supplied. if supplied value, it will isolate
     * the results for that role only. Supplying null will show only the related results for the current user.
     * Mostly null should be used for the viewer, but in reports.
     */
    public List<EdsTask> list(ListingFilterParameter fp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        EdsReference inProgress = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.IN_PROGRESS);
        EdsReference notStarted = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.NOT_STARTED);
        EdsReference waiting = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.WAITING_FOR_SOMEONE_ELSE);
        Integer viewAsFilter = fp.getViewAsId() != null ? fp.getViewAsId() : EdsRole.DEFAULT;

        Integer statusId = fp.getTaskStatusId() != null ? fp.getTaskStatusId() : 0;
        EdsUser user = getUser();
        String schemaName = getCompanyId();
        StringBuffer sql = null;
        sql = new StringBuffer();
        sql.append(" select distinct ta.id, ta.*, 0 as clazz_ "); //this is hack due to inheritance requre clazz_ fileld in the ResultSet set to 0
        sql.append(" from " + schemaName + ".task ta");                              // used only in native query, to ommit it eather reject inheritance EdsIssue-->EdsTask or use JPA query which is written and commented at the end of current class
        sql.append(" left outer join " + schemaName + ".employeetask et on (et.taskid=ta.id ) ");
        sql.append(" left outer join " + schemaName + ".projectemployee pe on(et.projectemployeeid=pe.id ) ");
        sql.append(" left outer join " + schemaName + ".project p on (pe.projectid=p.id ) ");
        sql.append(" left outer join " + schemaName + ".teamemployee te on (pe.employeedepartmentid=te.id) ");
        sql.append(" left outer join " + schemaName + ".myuser mu on (te.employeeid=mu.id) ");
        sql.append(" left outer join " + schemaName + ".employee e on (e.id=te.employeeid) ");
        sql.append(" left outer join " + schemaName + ".team t on (t.id=te.teamid) ");
        sql.append(" left outer join " + schemaName + ".reference re ON (re.id=et.statusid) ");
        sql.append(" where p.isdeleted<>true ");
        sql.append(" and ta.deleted<>true ");
        sql.append(" and (ta.isissue is null or ta.isissue<>true) ");

        if (fp.getClientId() != null && fp.getClientId() > 0) {
            sql.append(" and p.clientid=" + fp.getClientId() + " ");
        }
        if (fp.getProjectId() != null && fp.getProjectId() > 0) {
            sql.append(" and p.id=" + fp.getProjectId() + " ");
        }
        if (fp.getDepartmentId() != null && fp.getDepartmentId() > 0) {
            sql.append(" and t.id=" + fp.getDepartmentId() + " ");
        }
        if (fp.getEmployeeId() != null && fp.getEmployeeId() > 0) {
            sql.append(" and e.id=" + fp.getEmployeeId() + " ");
        }
        if (fp.isDoNotExportToQB()) {
            sql.append(" and ta.quickbook_task_id is null ");
        }
        if (fp.getObjectIDs() != null && !fp.getObjectIDs().isEmpty()) {
            sql.append(" and ta.id not in (" + ServerUtils.getAsCommoDelimited(fp.getObjectIDs(), "0", ",") + ") ");
        }
        sql.append(" and ( 1=1 ");

        if (EdsRole.DR.equals(viewAsFilter) || EdsRole.ADMIN.equals(viewAsFilter)) {
            // if he is director or admin should see
            // all the projects of the company
        } else if (EdsRole.ADMIN_LOCATION.equals(viewAsFilter)) {
            EdsLocation location = user.getLocation();
            sql.append(" and (mu.locationId is not null and mu.locationId=" + (location != null
                    ? location.getObjectID()
                    : null) + " )");

        } else if (EdsRole.PM.equals(viewAsFilter)) {
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
        } else if (EdsRole.TL.equals(viewAsFilter)) {
            sql.append(" and (t.leaderid=" + user.getObjectID() + ") ");
        } else if (EdsRole.MEM.equals(viewAsFilter)) {
            sql.append(" and (e.id=" + user.getObjectID() + ")");
        } else if (user.isClientContact()) {
            sql.append(" and p.clientid=" + user.getClientContact().getClientID() + " ");
        } else if (EdsRole.DEFAULT.equals(viewAsFilter) || viewAsFilter == null) {
            sql.append(" and (t.leaderid=" + user.getObjectID() + " or p.managerid=" + user.getObjectID() + " or p.backup_managerid=" + user.getObjectID());
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
        } else {
            sql.append(" and (t.leaderid=" + user.getObjectID() + " or p.managerid=" + user.getObjectID() + " or p.backup_managerid=" + user.getObjectID());
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

        if (statusId == 0) {
            //Show all statuses
        } else if (ALL_DUE_TASKS.equals(statusId)) {
            sql.append("and et.deleted <> true and ( et.statusid=" + inProgress.getObjectID() + " or et.statusid=" + notStarted.getObjectID() + " or et.statusid=" + waiting.getObjectID() + " or (re.isSystemReference is not true and re.deleted is not true) )");
        } else {
            sql.append("and et.deleted <> true and (et.statusid=" + statusId + ")");
        }
        if (fp.getStatusValues() != null) {
            sql.append(" and et.deleted <> true and et.statusid in (" + fp.getStatusValues() + ")");
        }
        if (fp.getStartDate() != null && fp.getEndDate() != null && (fp.isPlannedDue() || fp.isPlannedStart() || fp.isActualDue() || fp.isActualStart())) {

            String startDate = format.format(fp.getStartDate());
            String endDate = format.format(fp.getEndDate());
            sql.append(" and (");
            if (fp.isPlannedStart()) {
                sql.append(" ta.startDate between to_date('" + startDate + "','yyyy-MM-dd') and to_date('" + endDate + "','yyyy-MM-dd')");
            }
            if (fp.isPlannedDue()) {
                if (fp.isPlannedStart()) {
                    sql.append(" or ");
                }
                sql.append(" ta.dueDate between to_date('" + startDate + "','yyyy-MM-dd') and to_date('" + endDate + "','yyyy-MM-dd')");
            }
            if (fp.isActualStart()) {
                if (fp.isPlannedDue() || fp.isPlannedStart()) {
                    sql.append(" or ");
                }
                sql.append("  ta.actualStartDate between to_date('" + startDate + "','yyyy-MM-dd') and to_date('" + endDate + "','yyyy-MM-dd')");

            }
            if (fp.isActualDue()) {
                if (fp.isActualStart() || fp.isPlannedDue() || fp.isPlannedStart()) {
                    sql.append(" or ");
                }
                sql.append(" ta.actualEndDate between to_date('" + startDate + "','yyyy-MM-dd') and to_date('" + endDate + "','yyyy-MM-dd') ");
            }

            sql.append(") ");
        }
        if (fp.getTaskPriorityId() != null && fp.getTaskPriorityId() > 0) {
            sql.append(" and ta.priorityid=" + fp.getTaskPriorityId());
        }

        if (fp.getSqlSearchKey() != null) {
            sql.append(" and (");
            sql.append("   lower(ta.name) like '" + fp.getSqlSearchKey() + "' ");
            sql.append("or lower(ta.description) like '" + fp.getSqlSearchKey() + "' ");
            sql.append("or lower(mu.firstName) like '" + fp.getSqlSearchKey() + "' ");
            sql.append("or lower(mu.lastName) like '" + fp.getSqlSearchKey() + "' ");
            sql.append("or ta.number LIKE '" + "%" + fp.getSearchKey() + "%" + "' ");
            sql.append(") ");
        }

        //get Employee tasks this  holidy range startDate and endDate entered
        if (fp.getSickRequestStartDate() != null && fp.getSickRequestEndDate() != null) {
            String startDate = format.format(fp.getSickRequestStartDate().getTime());
            String endDate = format.format(fp.getSickRequestEndDate().getTime());
            sql.append(" and (");
            sql.append("(to_date('" + startDate + "','yyyy-MM-dd') between ta.startDate and ta.dueDate) ");
            sql.append(" or ");
            sql.append("(to_date('" + endDate + "','yyyy-MM-dd') between ta.startDate and ta.dueDate) ");
            sql.append(") ");
        }

        sql.append(" )");//CLOSED

        if (!(fp.getSortField() == null && "".equals(fp.getSortField()))) {
            sql.append("ORDER BY ta.lastUpdateTime DESC ");
        } else {
            // Sort by Task Name
            sql.append("ORDER BY ta.lastUpdateTime DESC ");
        }
        if (fp.isDoNotExportToQB() && fp.getLimit() != null) {
            sql.append(" OFFSET 0 LIMIT " + fp.getLimit());
        } else { //by default
            sql.append(" limit 5000");
        }
        return findNative(sql.toString(), EdsTask.class);
    }

    @Deprecated
    public List<Integer> getCompaniesByTaskRegDate(Date sTime, Date eTime) {
        return find("select t.project.company.objectID FROM EdsTask t where (t.creationTime " +
                "between '" + sTime + "' and '" + eTime + "') and t.project.company.objectID <> 1 group by t.project.company.objectID");
    }

    @Deprecated
    public List<EdsTask> getTasksByRegDate(Date sTime, Date eTime, EdsCompany company, boolean includeUpdateTime) {
        List list;
        if (company == null) {
            list = find("FROM EdsTask t where (t.creationTime between '" + sTime + "' and '" + eTime + "')");
        } else if (includeUpdateTime) {
            list = find("FROM EdsTask t where ((t.creationTime between '" + sTime + "' and '" + eTime + "') or" +
                    " (t.lastUpdateTime between '" + sTime + "' and '" + eTime + "'))", company);
        } else {
            list = find("FROM EdsTask t where (t.creationTime between '" + sTime + "' and '" + eTime + "')", company);
        }
        return list;
    }

    @Override
    public List<EdsTask> getTaskByIds(String Ids) {
        return (List<EdsTask>) find("SELECT t FROM EdsTask t WHERE t.objectID IN (" + Ids + ") AND t.deleted = false AND ((t.visibility is null or t.visibility = 'PUBLIC') or (t.visibility = 'PRIVATE' AND t.toDoListAssignee = " + getUser().getObjectID() + "))");
    }

    /**
     * Returns all the predecessors (recursively, i.e. scans the all subtree) of the task
     *
     * @param rootTaskId
     * @return
     */
    public List getRecursivelyPredecessors(Integer rootTaskId) {
        String sqlRecursive = "select t.keyid " +
                "from connectby('" + getCompanyId() + ".taskpredecessor',  'predecessorid', 'taskid',  'taskid' , '" + rootTaskId + "', 0, '>') " +
                "AS t(keyid text, parent_keyid text, level int, branch text, pos int) ";
        return findNative(sqlRecursive);
    }

    /**
     * Returns all the succseesors (recursively, i.e. scans the all subtree) of the task
     *
     * @param rootTaskId
     * @return
     */
    public List getRecursivelySuccessors(Integer rootTaskId) {
        String sqlRecursive = "select t.keyid " +
                "from connectby('" + getCompanyId() + ".taskpredecessor',  'taskid', 'predecessorid',  'predecessorid' , '" + rootTaskId + "', 0, '>') " +
                "AS t(keyid text, parent_keyid text, level int, branch text, pos int) ";
        return findNative(sqlRecursive);
    }

    public void deleteTask(EdsEmployeeTask employeeTask) {
        update("update EdsTask task set deleted='true' " +
                "where task.objectID=?  and task.deleted<>true", employeeTask.getTask().getObjectID());
    }

    public void deleteProjectTasks(EdsProject project) {
        update("update EdsTask task set task.deleted=true " +
                "where task.project=? and task.deleted<>true", project);
    }

    public void deleteTask(EdsTask taskItem) {
        update("update EdsTask task set task.deleted=true, task.recurrenceID = null where task.objectID=" + taskItem.getObjectID());
    }

    public void deleteEmployeesTask(EdsTask task) {
        update("update EdsEmployeeTask et set et.deleted=true " +
                "where et.task=? and et.deleted<>true", task);
    }

    public List<EdsTask> listByProjectAndEmployee(Integer projectId) {
        return find("select t from EdsTask t where t.project.objectID = ? " +
                " and t.deleted=false and (t.isIssue is null or t.isIssue=false) and t.parent is null", projectId);
    }

    public Date getFirstProjectTask(Integer projectID) {
        return (Date) findSingle("select min(t.startDate) from EdsTask t where t.project.objectID=? and t.startDate is not null ", projectID);
    }

    public Date getLastProjectTask(Integer projectID) {
        return (Date) findSingle("select max(t.dueDate) from EdsTask t where t.project.objectID=? and t.dueDate is not null ", projectID);
    }

    @Override
    public Date getLastExistingProjectTask(Integer projectID) {
        return (Date) findSingle("select max(t.dueDate) from EdsTask t where t.project.objectID=? and t.dueDate is not null and t.deleted=false", projectID);
    }

    public List<EdsTask> getProjectTasks(EdsProject project) {
        return find("select t from EdsTask t where t.project = ? " +
                " and t.deleted=false order by t.objectID", project);

    }

    public EdsTask getProjectTaskByName(Integer projectID, String name) {
        return (EdsTask) findSingle("select t from EdsTask t where t.project.objectID=? and t.name = ?", projectID, name);

    }

    public List<EdsTask> getProjectTasksOrderByDate(EdsProject project) {
        return find("select t from EdsTask t where t.project = ? " +
                " and t.deleted=false and t.parent=null order by t.startDate, t.name", project);

    }

    public List<EdsTask> getProjectTasksOrderBySDate(EdsProject project) {
        return find("select t from EdsTask t where t.project = ? " +
                " and t.deleted=false and (t.isIssue = false or t.isIssue is null) order by t.startDate", project);
    }

    /**
     * Will return WFT calendar tasks from database
     *
     * @param employeeIDs
     * @param startDate
     * @param endDate
     * @param fromAgenda
     * @return
     */
    public List<EdsEmployeeTask> getCalendarTasks(List<Integer> employeeIDs, Date startDate, Date endDate, boolean fromAgenda) {
        Map params = new HashMap();
        params.put("employeeIDs", employeeIDs);
        params.put("start", startDate);
        params.put("end", endDate);
        return findByNamedParams("select et from EdsEmployeeTask et where " +
                "et.projectEmployee.employeeDepartment.employee.objectID in (:employeeIDs) " +
                "and et.task.startDate<=:end and et.task.dueDate>=:start " +
                "and et.deleted<>true and et.task.deleted<>true order by et.task.name", params);

    }

    public List<EdsEmployeeTask> getUserTasks(List<Integer> employeeIDs, Date startDate, Date endDate, boolean fromAgenda) {
        Map params = new HashMap();

        params.put("start", startDate);
        params.put("employeeIDs", employeeIDs);
        if (!fromAgenda) {//For CalendarView page
            params.put("end", endDate);
            return findByNamedParams("select distinct et from EdsEmployeeTask et where " +
                    "et.projectEmployee.employeeDepartment.employee.objectID in (:employeeIDs) " +
                    "and et.task.startDate<=:end and et.task.dueDate>=:start " +
                    "and et.deleted<>true and et.task.deleted<>true and (et.task.isIssue is null or et.task.isIssue=false)", params);
        } else {//For Workspace home page
            return findByNamedParams("select et from EdsEmployeeTask et where " +
                    "et.projectEmployee.employeeDepartment.employee.objectID in (:employeeIDs) and et.deleted<>true and et.task.deleted<>true " +
                    "and et.task.dueDate>=:start and (et.task.isIssue is null or et.task.isIssue=false) " +
                    "order by et.task.startDate ", params);
        }

    }

    @Override
    public List<Integer> getOverdueTasksByIDs(List<Integer> taskIDs) {
        Integer inProgressID = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.IN_PROGRESS).getObjectID();
        Integer notStartedID = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.NOT_STARTED).getObjectID();
        Integer waitingID = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.WAITING_FOR_SOMEONE_ELSE).getObjectID();

        StringBuilder sql = new StringBuilder();
        sql.append("select t.id from ").append(getCompanyId()).append(".task t");
        sql.append(" where (t.deleted is not true or t.deleted is null) and (t.isissue is not true or t.isissue is null) ");
        sql.append(" and t.id in (").append(ServerUtils.getAsCommoDelimited(taskIDs, "0", ",")).append(")");
        sql.append(" and t.duedate<'").append(new Date()).append("'");
        sql.append(" and t.statusid in (").append(inProgressID).append(",").append(notStartedID).append(",").append(waitingID).append(")");

        return (List<Integer>) findNative(sql.toString());
    }

    @Override
    public List<Integer> getTasksByIDs(List<Integer> taskIDs) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id from ").append(getCompanyId()).append(".task t");
        sql.append(" where (t.deleted is not true or t.deleted is null) and (t.isissue is not true or t.isissue is null) ");
        sql.append(" and t.id in (").append(ServerUtils.getAsCommoDelimited(taskIDs, "0", ",")).append(")");
        return (List<Integer>) findNative(sql.toString());
    }

    public List<EdsEmployeeTask> getEmployeeOverdueTasks(Integer employeeId, Date currentDate) {
        EdsReference inProgress = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.IN_PROGRESS);
        EdsReference notStarted = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.NOT_STARTED);
        EdsReference waiting = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.WAITING_FOR_SOMEONE_ELSE);
        return findLimited("select et from EdsEmployeeTask et where " +
                "et.projectEmployee.employeeDepartment.employee.objectID=? " +
                "and et.task.dueDate<? " +
                "and et.status.objectID in (?,?,?) " +
                "and et.deleted<>true and et.task.deleted<>true and (et.task.isIssue is null or et.task.isIssue=false) order by et.task.startDate desc " +
                " ", 20, employeeId, currentDate, inProgress.getObjectID(), notStarted.getObjectID(), waiting.getObjectID());
    }

    private String getEncryptEncodedLink(String plainText) {
        return EncryptionHelper.encodeURL(EncryptionHelper.encryptURL(plainText));
    }

    public Date getLastModifiedTaskDateByEmployee(Integer employeeID) {
        String schemaName = getCompanyId();
        Map<String, Object> map = new HashMap<>();
        map.put("employeeID", employeeID);

        String sql = "select ta.lastupdatetime from " + schemaName + ".task ta " +
                "        left outer join " + schemaName + ".employeetask et on et.taskid=ta.id " +
                "        left outer join " + schemaName + ".projectemployee pe on(et.projectemployeeid=pe.id )         " +
                "        left outer join " + schemaName + ".project p on (pe.projectid=p.id )                          " +
                "        left outer join " + schemaName + ".teamemployee te on (pe.employeedepartmentid=te.id)         " +
                "        left outer join " + schemaName + ".employee e on (e.id=te.employeeid) where e.id=" + employeeID +
                "        and p.isdeleted<>true" +
                "        and ta.deleted<>true " +
                "        and (ta.isissue is null or ta.isissue<>true) and ta.lastupdatetime is not null order by ta.lastupdatetime desc limit 1";
        List result = findNative(sql);
        if (result != null && !result.isEmpty()) {
            return (Date) result.get(0);
        }
        return null;
    }

    public EdsTask getFirstOrLastTaskInRecurringSeries(Integer recurrenceID, boolean isFirst) {
        return (EdsTask) findSingle("select task from EdsTask task where task.recurrenceID = ? order by task.fireTime " + (
                isFirst
                        ? "asc"
                        : "desc"), recurrenceID);
    }

    public EdsTask getTaskInstance(Integer recurrenceID, Date fireTime) {
        return (EdsTask) findSingle("select task from EdsTask task where task.recurrenceID = ? and task.fireTime = ?", recurrenceID, fireTime);
    }

    public List<EdsTask> getAllTaskInstances(Integer recurrenceID) {
        return find("select task from EdsTask task where task.deleted = false and task.recurrenceID = ?", recurrenceID);
    }

    public List<EdsTask> getAllTaskInstancesAfter(Integer recurrenceID, Date afterFireTime) {
        return find("select task from EdsTask task where task.deleted = false and task.recurrenceID = ? and task.fireTime >= ? order by id asc", recurrenceID, afterFireTime);
    }

    public void updateTask(EdsTask task) {
        super.update(task);
        flush();
    }

    public EdsMyUpdate registerTaskAllUpdates(EdsTask task, EdsUser creator, Date time, String updateType) {
        Integer taskRecurrence = task.getRecurrenceID();
        EdsMyUpdate myUpdate = null;
        if (taskRecurrence != null) {
            EdsTask edsTask = getFirstOrLastTaskInRecurringSeries(taskRecurrence, true);
            if (edsTask != null) {
                List<EdsMyUpdate> myUpdateList;
                if (EdsMyUpdate.ADD.equals(updateType)) {
                    myUpdateList = myUpdateManager.getUpdates(edsTask.getObjectID(), MyUpdateTypeManager.TASK, EdsMyUpdate.ADD);
                    if (myUpdateList == null || myUpdateList.size() == 0) {
                        myUpdate = myUpdateManager.registerTaskAddUpdate(task, creator, time);
                    }
                } else if (EdsMyUpdate.EDIT.equals(updateType)) {
                    myUpdateList = myUpdateManager.getUpdates(edsTask.getObjectID(), MyUpdateTypeManager.TASK, EdsMyUpdate.EDIT);
                    if (myUpdateList == null || myUpdateList.size() == 0) {
                        myUpdate = myUpdateManager.registerTaskEditUpdate(task, creator, time);
                    }
                } else if (EdsMyUpdate.DELETE.equals(updateType)) {
                    myUpdateList = myUpdateManager.getUpdates(edsTask.getObjectID(), MyUpdateTypeManager.TASK, EdsMyUpdate.DELETE);
                    if (myUpdateList == null || myUpdateList.size() == 0) {
                        myUpdate = myUpdateManager.registerTaskDeleteUpdate(task, creator, time);
                    }
                }
            }
        } else {
            List<EdsMyUpdate> myUpdateList;
            if (EdsMyUpdate.ADD.equals(updateType)) {
                myUpdateList = myUpdateManager.getUpdates(task.getObjectID(), MyUpdateTypeManager.TASK, EdsMyUpdate.ADD);
                if (myUpdateList == null || myUpdateList.size() == 0) {
                    myUpdate = myUpdateManager.registerTaskAddUpdate(task, creator, time);
                }
            } else if (EdsMyUpdate.EDIT.equals(updateType)) {
                myUpdateList = myUpdateManager.getUpdates(task.getObjectID(), MyUpdateTypeManager.TASK, EdsMyUpdate.EDIT);
                if (myUpdateList == null || myUpdateList.size() == 0) {
                    myUpdate = myUpdateManager.registerTaskEditUpdate(task, creator, time);
                }
            } else if (EdsMyUpdate.DELETE.equals(updateType)) {
                myUpdateList = myUpdateManager.getUpdates(task.getObjectID(), MyUpdateTypeManager.TASK, EdsMyUpdate.DELETE);
                if (myUpdateList == null || myUpdateList.size() == 0) {
                    myUpdate = myUpdateManager.registerTaskDeleteUpdate(task, creator, time);
                }
            }
        }
        return myUpdate;
    }

    public List<EdsTask> getUndeletedTasksIn(String ids) {
        return (List<EdsTask>) find("SELECT ta FROM EdsTask ta WHERE ta.objectID IN (" + ids + ") AND (ta.deleted is null or ta.deleted<>true)");
    }

    @Override
    public Integer getProjectTasksLastIntNumber(Integer projectID, boolean isUnique) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(" select t.intNumber from EdsTask t where (t.deleted is null or t.deleted is false) ");
        if (!isUnique) {
            buffer.append(" and t.project.objectID = " + projectID + " ");
        }
        buffer.append(" and t.intNumber is not null order by t.intNumber desc");
        return (Integer) findSingle(buffer.toString());
    }

    @Override
    public boolean isTaskNumberExists(String number, Integer projectId, Integer objectID) {
        if (objectID != null) {
            return find("select t from EdsTask t where (t.deleted is false or t.deleted is null) and t.project.objectID = ? and t.number = ? and t.objectID != ?",
                    projectId, number.trim(), objectID).size() > 0;
        } else {
            return find("select t from EdsTask t where (t.deleted is false or t.deleted is null) and t.project.objectID = ? and t.number = ?",
                    projectId, number.trim()).size() > 0;
        }
    }

    public void removeRecurrenceFromTask(Integer recurrenceID, Integer companyID) {
        updateNative("update \"" + companyID + "\".task set recurrenceid = null where recurrenceid=" + recurrenceID);
    }

    @Override
    public List<Integer> getTaskIDsByIDs(Integer companyID, String ids) {
        String query = "select t.id from \"" + companyID + "\".task t where (t.deleted <> true or t.deleted is null) and (t.isissue <> true or t.isissue is null) and t.id in(" + ids + ")";
        return (List<Integer>) findNative(query);
    }

    @Override
    public List<Integer> getTaskIdsWithLimit(Integer companyID, int startat, int limit) {
        String query = "select t.id from \"" + companyID + "\".task t where (t.deleted <> true or t.deleted is null) and (t.isissue <> true or t.isissue is null) and t.id>" + startat + " order by t.id asc limit " + limit;
        return (List<Integer>) findNative(query);
    }

    @Override
    public List<EdsTask> getOrderByTask(ListingFilterParameter filterParameter) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.*,0 as clazz_ from ").append(getCompanyId()).append(".task t ");
        sql.append(" where t.deleted is not true and t.isIssue is not true ");
        if (filterParameter.getWorkstreamID() != null) {
            sql.append(" and t.parentwsid = ").append(filterParameter.getWorkstreamID());
        }
        if (StringUtils.isNotBlank(filterParameter.getSearchKey())) {
            sql.append(" and lower(t.name) like '").append(filterParameter.getSqlSearchKey()).append("'");
        }

        sql.append(" order by ");
        if (WbsItem.NAME.equals(filterParameter.getSortField())) {
            sql.append(" t.name ");
        } else if (WbsItem.START_DATE.equals(filterParameter.getSortField())) {
            sql.append(" t.startDate ");
        } else if (WbsItem.END_DATE.equals(filterParameter.getSortField())) {
            sql.append(" t.dueDate ");
        } else {
            sql.append(" t.startDate ");
        }
        sql.append(filterParameter.getSortDir() == 1 ? "ASC " : "DESC");

        return findNative(sql.toString(), EdsTask.class);
    }

    public List<EdsTask> getWorkStreamTasksOrderBy(Integer workStreamID, String sortBy) {
        if (sortBy == null || sortBy.equals("")) {
            sortBy = "startDate desc";
        }
        return (List<EdsTask>) find("select t FROM EdsTask t where t.parent.objectID=? and (t.isIssue is null or t.isIssue is not true) and (t.deleted is null or t.deleted=false) order by t. " + sortBy, workStreamID);
    }

    public List<EdsTask> getWorkStreamTasksByEmployee(Integer workStreamID, Integer employeeID, String sortBy) {
        if (sortBy == null || sortBy.equals("")) {
            sortBy = "startDate desc";
        }
        return (List<EdsTask>) find("select distinct t FROM EdsEmployeeTask et join et.task t where t.parent.objectID=? and et.projectEmployee.employeeDepartment.employee.objectID=" + employeeID + " and (t.isIssue is null or t.isIssue is not true) and (t.deleted is null or t.deleted is not true) order by t." + sortBy, workStreamID);
    }

    public List getTasksStatisticByWS(Integer parentID) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT t.status.objectID, COUNT(t.objectID) ");
        sql.append("FROM EdsTask t ");
        sql.append("WHERE t.parent.objectID = '" + parentID + "' ");
        sql.append("AND t.status is not null ");
        sql.append("AND t.deleted = false ");
        sql.append("GROUP BY t.status.objectID ");
        return find(sql.toString());
    }

    //this function gets the latest task end date before the supplied currentEventFireTime
    public Date getRecurringTaskFirstOrLastDate(Integer recurrenceID, Date currentTaskFireTime, boolean isFirst) {
        return (Date) findSingle("select " + (isFirst
                ? "t.startDate"
                : "t.dueDate") + " from EdsTask t where  " + (isFirst
                ? ""
                : "t.fireTime < '" + currentTaskFireTime + "' and ") + " t.deleted <> true and t.recurrenceID = " + recurrenceID + " order by t.fireTime " + (
                isFirst
                        ? "asc"
                        : "desc"));
    }

    public Long getAllTaskInstancesSize(Integer recurrenceID) {
        return (Long) findSingle("select count(t) from EdsTask t where (t.deleted is null or t.deleted=false) and t.recurrenceID = ?", recurrenceID);
    }


    public void updateTasksStatus(EdsReference status, Integer projectID) {
        update("UPDATE EdsTask t SET t.status.objectID =? WHERE t.project.objectID =?", status.getObjectID(), projectID);
    }

    @Override
    public Map<Integer, List<String>> getTaskAssigneeUserList(List<Integer> taskIds) {
        List<Object[]> items = find("select distinct et.task.objectID, CONCAT(ed.employee.lastName, ' ' , ed.employee.firstName) " +
                " from EdsEmployeeTask et " +
                "inner join et.projectEmployee pe inner join pe.employeeDepartment ed " +
                "where et.task.objectID in (?) and et.deleted <> true", taskIds);
        LinkedHashMap<Integer, List<String>> taskEMployees = new LinkedHashMap();
        for (Object[] item : items) {
            Integer taskId = (Integer) item[0];
            String employeeName = (String) item[1];
            List<String> employess = taskEMployees.get(taskId);
            if (employess == null) {
                employess = new ArrayList<>();
                employess.add(employeeName);
                taskEMployees.put(taskId, employess);
                continue;
            }
            employess.add(employeeName);
        }
        return taskEMployees;
    }

    @Override
    public List<String> getTaskAssigneeUsers(Integer taskId) {
        return find("select distinct CONCAT(ed.employee.lastName, ' ' , ed.employee.firstName) from EdsEmployeeTask et " +
                "inner join et.projectEmployee pe inner join pe.employeeDepartment ed " +
                "where et.task.objectID=? and et.deleted <> true", taskId);
    }

    @Override
    public Map<Integer, List<Integer>> getTaskProjectManagerAndBManagerMap(String taskIds) {
        String sql = "SELECT t.id,p.managerid,p.backup_ManagerId,p.backup_ManagerId2,p.backup_ManagerId3,p.backup_ManagerId4,p.backup_ManagerId5,p.backup_ManagerId6,p.backup_ManagerId7,p.backup_ManagerId8,p.backup_ManagerId9,p.backup_ManagerId10 FROM " + getCompanyId() + ".task t " +
                "INNER JOIN " + getCompanyId() + ".project p on p.id=t.projectid " +
                "WHERE t.id IN (" + taskIds + ")";
        List<Object[]> listObjects = (List<Object[]>) findNative(sql);
        Map<Integer, List<Integer>> taskManagerBManagerMap = new HashMap<>();
        for (Object[] objects : listObjects) {
            Integer taskId = (Integer) objects[0];
            Integer managerId = (Integer) objects[1];
            Integer bManagerId = (Integer) objects[2];
            Integer bManagerId2 = (Integer) objects[3];
            Integer bManagerId3 = (Integer) objects[4];
            Integer bManagerId4 = (Integer) objects[5];
            Integer bManagerId5 = (Integer) objects[6];
            Integer bManagerId6 = (Integer) objects[7];
            Integer bManagerId7 = (Integer) objects[8];
            Integer bManagerId8 = (Integer) objects[9];
            Integer bManagerId9 = (Integer) objects[10];
            Integer bManagerId10 = (Integer) objects[11];

            List<Integer> projectManagersList = new ArrayList<>();
            projectManagersList.add(managerId);
            projectManagersList.add(bManagerId);
            projectManagersList.add(bManagerId2);
            projectManagersList.add(bManagerId3);
            projectManagersList.add(bManagerId4);
            projectManagersList.add(bManagerId5);
            projectManagersList.add(bManagerId6);
            projectManagersList.add(bManagerId7);
            projectManagersList.add(bManagerId8);
            projectManagersList.add(bManagerId9);
            projectManagersList.add(bManagerId10);
            taskManagerBManagerMap.put(taskId, projectManagersList);
        }
        return taskManagerBManagerMap;
    }

    public void removeTaskPredecessors(Integer taskID) {
        updateNative("delete from " + getCompanyId() + ".taskpredecessor where taskid = " + taskID.toString() + " or predecessorid = " + taskID.toString());
    }

    @Override
    public String getSavedNumberformat(Integer objectID) {
        return (String) findSingle("select t.savedNumberFormula from EdsTask t where t.objectID =" + objectID);
    }

    @Override
    public Date getTaskPredecessorsMaxLastDueDate(Integer taskID) {
        return (Date) findNativeSingle("SELECT t.dueDate FROM " + getCompanyId() + ".task t" +
                " INNER JOIN " + getCompanyId() + ".taskpredecessor tp ON tp.predecessorId=t.id" +
                " WHERE tp.taskId=? AND (t.deleted IS NULL OR t.deleted=FALSE)" +
                " ORDER BY t.dueDate DESC", taskID);
    }

    @Override
    public Float getTaskActualPercentCompleted(Integer objectID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT max(t.estimatedtime) estimatedtime, sum(coalesce(tsh.timespent,0)) actualtime FROM ").append(getCompanyId()).append(".task t \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".timesheet tsh on tsh.taskid = t.id \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".reference ts on ts.id = tsh.statusid \n");
        sql.append("WHERE ts.code = '_APPROVE' \n");
        sql.append("AND t.id = ").append(objectID).append(" \n");
        sql.append("GROUP BY t.id ");

        Object[] object = (Object[]) findNativeSingle(sql.toString());

        if (object == null) {
            return 0f;
        }

        Integer estimatedTime = (Integer) object[0];
        BigInteger actualTime = (BigInteger) object[1];

        if (estimatedTime == null || estimatedTime == 0 || actualTime == null || actualTime.intValue() == 0) {
            return 0f;
        }

        Float percent = (actualTime.floatValue() / estimatedTime.floatValue()) * 100;

        return (percent > 100f && !genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_PERCENT_OVER_HUNDRED)) ? 100f : percent;
    }

    @Override
    public Double getTaskFieldValue(Integer objectID, String customFieldCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("select tc." + customFieldCode + " from ").append(getCompanyId()).append(".task t \n");
        sql.append("inner join ").append(getCompanyId()).append(".taskcustomfields tc on tc.id = t.taskcustomfieldsid \n");
        sql.append("where t.id = " + objectID);
        return (Double) findNativeSingle(sql.toString());
    }

    @Override
    public EdsTask getSiblingTaskByPrevItem(Integer prevTaskId, Integer statusId) {
        try {
            return slaveEntityManager.createQuery("select t from EdsTask t where (t.deleted is null or t.deleted<>true) and t.status.objectID=:statusID " +
                    "and t.kanbanOrder > (select t2.kanbanOrder from EdsTask t2 where t2.objectID=:prevTaskId)", EdsTask.class)
                    .setParameter("statusID", statusId).setParameter("prevTaskId", prevTaskId).setMaxResults(1).getSingleResult();
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public Long getTaskCountByStatus(Integer statusID) {
        TypedQuery<Long> query = slaveEntityManager.createQuery("select count(objectID) from EdsTask where (deleted is null or deleted<>true) and status.id=:status", Long.class).setParameter("status", statusID);
        return query.getSingleResult();
    }

    @Override
    public Long getTaskCountByStatus(Long prev, EdsReference status) {
        TypedQuery<Long> query = slaveEntityManager.createQuery("select count(objectID) from EdsTask where (deleted is null or deleted<>true) and status=:status" +
                (prev != null ? " and kanbanOrder>:prev" : ""), Long.class).setParameter("status", status);
        if (prev != null) {
            query.setParameter("prev", prev);
        }
        return query.getSingleResult();
    }

    @Override
    public List<EdsTask> getTasksByStatus(Long prev, EdsReference status, int start, int limit) {
        TypedQuery<EdsTask> query = slaveEntityManager.createQuery("select t from EdsTask t where (deleted is null or deleted<>true) and status=:status" +
                        (prev != null ? " and kanbanOrder>:prev" : "") + " order by kanbanOrder", EdsTask.class).setParameter("status", status)
                .setFirstResult(start).setMaxResults(limit);
        if (prev != null) {
            query.setParameter("prev", prev);
        }
        return query.getResultList();
    }

    @Override
    public void update(EdsTask task, boolean addToSolr) {
        update(task);
        if (addToSolr) {
            try {
                taskSolrComponent.index(task);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<EdsTask> getTasksByStatus(Integer statusID, int start, int limit) {
        TypedQuery<EdsTask> query = slaveEntityManager.createQuery("select t from EdsTask t where (deleted is null or deleted<>true) and status.id=:status" +
                " order by kanbanOrder", EdsTask.class).setParameter("status", statusID)
                .setFirstResult(start).setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public List<EdsTask> getTasksByDate(ListingFilterParameter fp) {
        String sql = "select distinct t from EdsEmployeeTask et " +
                "left join et.task t " +
                "left join et.projectEmployee pe " +
                "left join pe.employeeDepartment ed " +
                "where et.deleted <> true and t.dueDate between :startDate and :endDate and (t.deleted<>true or t.deleted is null) " +
                "and t.status.code not in ('COMPLETED', 'CLOSED', 'CANCELLED') ";
        if (fp.getEmployeeId() != null) {
            sql += "and ed.employee.id=:empID ";
        }
        sql += "order by t.dueDate desc ";

        Query dd = slaveEntityManager.createQuery(sql, EdsTask.class);
        dd.setParameter("startDate", fp.getStartDate());
        dd.setParameter("endDate", fp.getEndDate());
        if (fp.getEmployeeId() != null) {
            dd.setParameter("empID", fp.getEmployeeId());
        }
        dd.setFirstResult(fp.getStart());
        dd.setMaxResults(fp.getLimit());

        return dd.getResultList();
    }

    @Override
    public List<EdsEmployee> getTasksAssigneeByDate(ListingFilterParameter fp) {
        String sql = "select distinct ed.employee from EdsEmployeeTask et " +
                "inner join et.projectEmployee pe " +
                "inner join pe.employeeDepartment ed " +
                "where et.deleted <> true and et.task.dueDate between :startDate and :endDate and (et.task.deleted<>true or et.task.deleted is null) " +
                "and et.task.status.code not in ('COMPLETED', 'CLOSED', 'CANCELLED')";
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql += " and (";
            sql += " lower(ed.employee.firstName) like '%" + fp.getSearchKey().toLowerCase() + "%' " +
                    " or lower(ed.employee.lastName) like '%" + fp.getSearchKey().toLowerCase() + "%')";
        }
        return slaveEntityManager.createQuery(sql, EdsEmployee.class)
                .setParameter("startDate", fp.getStartDate())
                .setParameter("endDate", fp.getEndDate())
                .setFirstResult(fp.getStart())
                .setMaxResults(fp.getLimit())
                .getResultList();
    }

    @Override
    public Long getMinKanbanOrder(Integer statusId) {
        if (statusId == null || statusId == 0) {
            return slaveEntityManager.createQuery("SELECT min(t.kanbanOrder) FROM EdsTask t  where (t.deleted is null or t.deleted <> true) AND t.status.objectID IS NULL", Long.class).getSingleResult();
        } else {
            return slaveEntityManager.createQuery("SELECT min(t.kanbanOrder) FROM EdsTask t  where (t.deleted is null or t.deleted <> true) AND t.status.objectID=:statusId", Long.class)
                    .setParameter("statusId", statusId)
                    .getSingleResult();
        }
    }

    @Override
    public List<Object[]> getMyCalendarDayFirstEvents(Date day, Integer userId) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "(select 'TASK' entitytype, t.id from " + getCompanyId() + ".task t " +
                "  left join " + getCompanyId() + ".employeetask et on et.taskId = t.id " +
                "  left join " + getCompanyId() + ".projectEmployee pe on pe.id = et.projectEmployeeId " +
                "  left join " + getCompanyId() + ".teamEmployee te on te.id = pe.employeeDepartmentId " +
                "  left join " + getCompanyId() + ".reference r on r.id = et.statusId " +
                "  where t.deleted is not true and et.deleted is not true and t.isissue is not true " +
                "  and te.employeeId = " + userId + " and r.code not in ('COMPLETED', 'CLOSED') " +
                "  and to_date(to_char(t.startDate, 'yyyy-MM-dd'), 'yyyy-MM-dd') =  to_date('" + format.format(day) + "','yyyy-MM-dd') " +
                "  and to_date(to_char(t.dueDate, 'yyyy-MM-dd'), 'yyyy-MM-dd') = to_date('" + format.format(day) + "','yyyy-MM-dd') limit 1) " +
                "UNION (select 'MEETING' entitytype, m.id from " + getCompanyId() + ".event m " +
                "  left join " + getCompanyId() + ".employeeevent ee on ee.event_id = m.id " +
                "  where m.deleted is not true and ee.deleted is not true and m.activityType = 1 " +
                "  and (m.owner = " + userId + " or ee.employee_id = " + userId + ") " +
                "  and to_date(to_char(m.startDate, 'yyyy-MM-dd'), 'yyyy-MM-dd') =  to_date('" + format.format(day) + "','yyyy-MM-dd') " +
                "  and to_date(to_char(m.endDate, 'yyyy-MM-dd'), 'yyyy-MM-dd') = to_date('" + format.format(day) + "','yyyy-MM-dd') limit 1) " +
                "UNION (select 'CALL' entitytype, e.id from " + getCompanyId() + ".event e " +
                "  left join " + getCompanyId() + ".employeeevent ee on ee.event_id = e.id " +
                "  where e.deleted is not true and ee.deleted is not true and e.activityType = 2 " +
                "  and (e.owner = " + userId + " or ee.employee_id = " + userId + ") " +
                "  and to_date(to_char(e.startDate, 'yyyy-MM-dd'), 'yyyy-MM-dd') =  to_date('" + format.format(day) + "','yyyy-MM-dd') " +
                "  and to_date(to_char(e.endDate, 'yyyy-MM-dd'), 'yyyy-MM-dd') = to_date('" + format.format(day) + "','yyyy-MM-dd') limit 1)";

        return slaveEntityManager.createNativeQuery(sql).getResultList();
    }

    @Override
    public List<Object[]> getMyCalendarDayEvents(Date day, Integer userId) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "(select t.name subject, t.description description, t.startDate startDate, t.dueDate endDate, 'TASK' entitytype, t.id from " + getCompanyId() + ".task t " +
                "  left join " + getCompanyId() + ".employeetask et on et.taskId = t.id " +
                "  left join " + getCompanyId() + ".projectEmployee pe on pe.id = et.projectEmployeeId " +
                "  left join " + getCompanyId() + ".teamEmployee te on te.id = pe.employeeDepartmentId " +
                "  left join " + getCompanyId() + ".reference r on r.id = et.statusId " +
                "  where t.deleted is not true and et.deleted is not true and t.isissue is not true " +
                "  and te.employeeId = " + userId + " and r.code not in ('COMPLETED', 'CLOSED') " +
                "  and (to_date('" + format.format(day) + "','yyyy-MM-dd') between to_date(to_char(t.startDate, 'yyyy-MM-dd'), 'yyyy-MM-dd') and to_date(to_char(t.dueDate, 'yyyy-MM-dd'), 'yyyy-MM-dd'))" +
                " order by startDate asc limit 10) " +
                "UNION (select m.subject subject, m.description description, m.startDate startDate, m.endDate endDate, 'MEETING' entitytype, m.id from " + getCompanyId() + ".event m " +
                "  left join " + getCompanyId() + ".employeeevent ee on ee.event_id = m.id " +
                "  where m.deleted is not true and ee.deleted is not true and m.activityType = 1 " +
                "  and (m.owner = " + userId + " or ee.employee_id = " + userId + ") " +
                "  and to_date(to_char(m.startDate, 'yyyy-MM-dd'), 'yyyy-MM-dd') =  to_date('" + format.format(day) + "','yyyy-MM-dd') " +
                "  and to_date(to_char(m.endDate, 'yyyy-MM-dd'), 'yyyy-MM-dd') = to_date('" + format.format(day) + "','yyyy-MM-dd') order by startDate asc limit 10) " +
                "UNION (select e.subject subject, e.description description, e.startDate startDate, e.endDate endDate, 'CALL' entitytype, e.id from " + getCompanyId() + ".event e " +
                "  left join " + getCompanyId() + ".employeeevent ee on ee.event_id = e.id " +
                "  where e.deleted is not true and ee.deleted is not true and e.activityType = 2 " +
                "  and (e.owner = " + userId + " or ee.employee_id = " + userId + ") " +
                "  and to_date(to_char(e.startDate, 'yyyy-MM-dd'), 'yyyy-MM-dd') =  to_date('" + format.format(day) + "','yyyy-MM-dd') " +
                "  and to_date(to_char(e.endDate, 'yyyy-MM-dd'), 'yyyy-MM-dd') = to_date('" + format.format(day) + "','yyyy-MM-dd') order by startDate asc limit 10)";

        return slaveEntityManager.createNativeQuery(sql).getResultList();
    }
}
