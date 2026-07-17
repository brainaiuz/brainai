package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsWorkStream;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.WbsItem;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.WorkStreamManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 09.11.2008
 * Time: 17:47:20
 * To change this template use File | Settings | File Templates.
 */
@Repository("workStreamManager")
public class WorkStreamManagerImpl extends AttachmentSupportManager<EdsWorkStream> implements WorkStreamManager {

    @Autowired
    private ProjectManager projectManager;

    public WorkStreamManagerImpl() {
        super(EdsWorkStream.class);
    }

    public List<EdsWorkStream> findOrphanWorkstreams(Integer projectId) {
        return find("select w from EdsWorkStream w where w.project.objectID = ? " +
                " and w.parentWS is null and w.deleted is not true order by w.startDate desc", projectId);
    }

    public List<EdsWorkStream> findOrphanWorkstreamsByAlphabitic(Integer projectId) {
        return find("select w from EdsWorkStream w where w.project.objectID = ? " +
                " and w.parentWS is null and w.deleted is not true order by w.name asc", projectId);
    }

    public List<EdsWorkStream> findOrphanWorkstreams(ListingFilterParameter filterParameter) {
        StringBuilder sql = new StringBuilder();
        sql.append("select w.*,0 as clazz_ from ").append(getCompanyId()).append(".workstream w ");
        sql.append(" left join ").append(getCompanyId()).append(".project p on w.projectid = p.id ");
        sql.append(" where w.deleted is not true ");
        if (filterParameter.getProjectId() != null) {
            sql.append(" and p.id = ").append(filterParameter.getProjectId());
        }
        if (StringUtils.isNotBlank(filterParameter.getSearchKey())) {
            sql.append(" and lower(w.name) like '").append(filterParameter.getSqlSearchKey()).append("'");
        } else {
            sql.append(" and w.parentWSid is null ");
        }
        sql.append(" order by ");
        if (WbsItem.NAME.equals(filterParameter.getSortField())) {
            sql.append(" w.name ");
        } else if (WbsItem.START_DATE.equals(filterParameter.getSortField())) {
            sql.append(" w.startDate ");
        } else if (WbsItem.END_DATE.equals(filterParameter.getSortField())) {
            sql.append(" w.endDate ");
        } else {
            sql.append(" w.startDate ");
        }
        sql.append(filterParameter.getSortDir() == 1 ? "ASC " : "DESC");

        return findNative(sql.toString(), EdsWorkStream.class);
    }

    public List<Integer> getWorkStreamsSomeParent(Integer parentID) {
        String sql = "select t.keyid " +
                " from connectby('" + getCompanyId() + ".workstream', 'id', 'parentwsid', 'id', '" + parentID + "', 0, '>') " +
                " AS t(keyid int, parent_keyid int, level int, branch text, pos int)";

        return findNative(sql);
    }

	public List<EdsWorkStream> findOrphanWorkstreams(Integer projectId, Date from, Date to, String sortBy) {
		return find("select w from EdsWorkStream w where w.project.objectID = ? " +
				" and (w.endDate>=? and w.startDate<=?) and w.deleted is not true and w.parentWS is null ORDER BY w.taskGanttOrder asc, w." + sortBy, projectId, from, to);
	}

    public List<EdsWorkStream> findOrphanWorkstreams(Integer projectId, Date from, Date to, String sortBy, Integer start, Integer limit) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT w.* FROM ").append(getCompanyId()).append(".workstream w ");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".project p ON p.id = w.projectid ");
        sql.append("WHERE w.endDate >= '").append(dateFormat.format(from)).append("' AND w.startDate <= '").append(dateFormat.format(to)).append("' ");
        sql.append("AND p.id = ").append(projectId).append(" ");
        sql.append("AND w.deleted is not true ");
        sql.append("AND w.parentWSid is null ");
        sql.append("ORDER BY w.taskGanttOrder, w." + sortBy);

        if (start != null && limit != null) {
            sql.append(" OFFSET " + start + " LIMIT " + limit);
        }

        return findNative(sql.toString(), EdsWorkStream.class);
    }

    public List<EdsWorkStream> listByProjectId(Integer projectId) {
        if (projectId != null) {
            return find(
                    "select w from EdsWorkStream w " +
                            "where w.project.objectID=? and w.deleted <> true " +
                            "order by w.objectID asc", projectId);
        } else {
            return find(
                    "select w from EdsWorkStream w where w.deleted <> true order by w.objectID asc");
        }
    }

    @Override
    public List<EdsWorkStream> listByProjectIds(String projectIds) {
        return find("select w from EdsWorkStream w " +
                "where w.project.objectID IN (" + projectIds + ") and w.deleted <> true");
    }

    @Override
    public List<EdsWorkStream> getOrderByWorkStream(ListingFilterParameter filterParameter) {
        StringBuilder sql = new StringBuilder();
        sql.append("select w.*,0 as clazz_ from ").append(getCompanyId()).append(".workstream w ");
        sql.append(" where w.deleted is not true ");
        if (filterParameter.getWorkstreamID() != null) {
            sql.append(" and w.parentWSid = ").append(filterParameter.getWorkstreamID());
        }
        if (StringUtils.isNotBlank(filterParameter.getSearchKey())) {
            sql.append(" and lower(w.name) like '").append(filterParameter.getSqlSearchKey()).append("'");
        }

        sql.append(" order by ");
        if (WbsItem.NAME.equals(filterParameter.getSortField())) {
            sql.append(" w.name ");
        } else if (WbsItem.START_DATE.equals(filterParameter.getSortField())) {
            sql.append(" w.startDate ");
        } else if (WbsItem.END_DATE.equals(filterParameter.getSortField())) {
            sql.append(" w.endDate ");
        } else {
            sql.append(" w.startDate ");
        }
        sql.append(filterParameter.getSortDir() == 1 ? "ASC " : "DESC");

        return findNative(sql.toString(), EdsWorkStream.class);
    }

    @Override
    public Object getWSPercent(Integer objectID) {
        return findSingle("SELECT COUNT(t.objectID), SUM(t.percent) FROM EdsTask t WHERE t.deleted is not true AND t.parent.objectID = ?", objectID);
    }

    @Override
    public Date getWSStartDateByTask(Integer objectID) {
        return (Date) findSingle("SELECT t.startDate FROM EdsTask t WHERE t.deleted != true AND t.parent.objectID = ? ORDER BY t.startDate ", objectID);
    }

    @Override
    public Date getWSEndDAteByTask(Integer objectID) {
        return (Date) findSingle("SELECT t.dueDate FROM EdsTask t WHERE t.deleted != true AND t.parent.objectID = ? ORDER BY t.dueDate DESC ", objectID);
    }

    @Override
    public EdsProject selectCompilitedStatus(Integer projectID) {
        return (EdsProject) findNativeSingle("select p.* from " + getCompanyId() + ".project p where  p.id = " + projectID, EdsProject.class);
    }

    @Override
    public Integer getWorkSreamLastIntNumber(Integer projectID, boolean isUnique) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(" select w.intNumber from EdsWorkStream w where ");
        if (!isUnique) {
            buffer.append(" w.project.objectID = " + projectID + " and ");
        }
        buffer.append(" w.intNumber is not null order by w.intNumber desc");
        return (Integer) findSingle(buffer.toString());
    }

    @Override
    public String getSavedNumberformat(Integer objectID) {
        return (String) findSingle("select w.savedNumberFormula from EdsWorkStream w where w.objectID =" + objectID);
    }

	public Integer getEmployeeAssignedTasksCount(Integer workStreamID, Integer employeeID) {
		Long count = (Long)findSingle("select count(et.objectID) from EdsEmployeeTask et right join et.task t where et.deleted = false and " +
				"t.deleted = false and t.parent.objectID=? and et.projectEmployee.employeeDepartment.employee.objectID=?", workStreamID, employeeID);
		return count != null ? count.intValue() : 0;
	}


    public ProjectManager getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }
}
