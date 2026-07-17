package com.edatasite.workforce.gwt.core.server.db.impl.goal;

import com.edatasite.workforce.core.domain.goal.EdsDepartmentGoalEmployeeMetricHistory;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.goal.DepartmentGoalEmployeeMetricHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository("departmentGoalEmployeeMetricHistoryManager")
public class DepartmentGoalEmployeeMetricHistoryManagerImpl extends BaseManager<EdsDepartmentGoalEmployeeMetricHistory> implements DepartmentGoalEmployeeMetricHistoryManager {
    public DepartmentGoalEmployeeMetricHistoryManagerImpl() {
        super(EdsDepartmentGoalEmployeeMetricHistory.class);
    }


    @Override
    public EdsDepartmentGoalEmployeeMetricHistory getEmployeeMetricHistoryById(Integer id) {
        return (EdsDepartmentGoalEmployeeMetricHistory) findSingle("SELECT s FROM EdsDepartmentGoalEmployeeMetricHistory s WHERE (s.deleted is null or s.deleted <> true) AND s.id = ?", id);
    }

    @Override
    public void deleteEmployeeMetricHistoryById(Integer id) {
        update("update EdsDepartmentGoalEmployeeMetricHistory s set s.deleted = true where s.id = ?", id);
    }

    @Override
    public void deleteEmployeeMetricHistoriesByDepartmentGaolId(Integer departmentGoalId) {
        update("update EdsDepartmentGoalEmployeeMetricHistory s set s.deleted = true where s.goalAssignees.id in (select g.id from EdsGoalAssignees g where g.goal.id = ? )", departmentGoalId);
    }


    @Override
    public Integer getTotalCount(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(d) from EdsDepartmentGoalEmployeeMetricHistory d ");
        sql.append(" where ").append(ServerUtils.checkForDeleted("d.deleted"));
        sql.append(" and d.goalAssignees.goal.id = ").append(fp.getObjectId());

        Long total = (Long) findSingle(sql.toString());
        return total != null ? total.intValue() : 0;
    }


    @Override
    public List<EdsDepartmentGoalEmployeeMetricHistory> getList(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select d from EdsDepartmentGoalEmployeeMetricHistory d ");
        sql.append(" where ").append(ServerUtils.checkForDeleted("d.deleted"));
        sql.append(" and d.goalAssignees.goal.id = ").append(fp.getObjectId());

        String searchKey = fp.getSearchKey();

        if (searchKey != null && !(searchKey = searchKey.trim()).isEmpty()) {
            String key = searchKey.toLowerCase();
            sql.append(" and (")
                    .append(" lower(d.assignee.firstName) like '%").append(key).append("%' ")
                    .append(" or lower(d.assignee.lastName) like '%").append(key).append("%' ")
                    .append(")");
        }

        if (fp.getSortField() != null) {
            sql.append(" order by ");

            switch (fp.getSortField()) {
                case "EMPLOYEE":
                    sql.append("d.assignee.firstName");
                    break;
                case "ACTUAL":
                    sql.append("d.actual");
                    break;
                case "DATE":
                    sql.append("d.date");
                    break;
                case "CREATION_DATE":
                    sql.append("d.creationDate");
                    break;
                default:
                    sql.append("d.creationDate");
            }

            boolean asc = "true".equals(fp.getValueMap().get("ASC"));
            sql.append(asc ? " asc" : " desc");

        } else {
            sql.append(" order by d.objectID desc");
        }

        return find(sql.toString());
    }


    @Override
    public Double getActualTotalByGoalAssigneeIdAndEmployeeId(Integer goalAssigneeId, Integer employeeId) {
        Double total = (Double) findSingle(
                "SELECT SUM(s.actual) FROM EdsDepartmentGoalEmployeeMetricHistory s where (s.deleted is null or s.deleted <> true) and s.goalAssignees.id=? and s.assignee.id=?",
                goalAssigneeId, employeeId
        );
        return total != null ? total : 0D;
    }

    @Override
    public Date getMinEntryDateByGoalId(Integer goalId) {
        return (Date) findSingle(
                "SELECT MIN(s.date) FROM EdsDepartmentGoalEmployeeMetricHistory s where (s.deleted is null or s.deleted <> true) and s.goalAssignees.goal.id=?",
                goalId
        );
    }

    @Override
    public Date getMaxEntryDateByGoalId(Integer goalId) {
        return (Date) findSingle(
                "SELECT MAX(s.date) FROM EdsDepartmentGoalEmployeeMetricHistory s where (s.deleted is null or s.deleted <> true) and s.goalAssignees.goal.id=?",
                goalId
        );
    }

    @Override
    public List<Object[]> getChartDataForGoal(Integer goalId) {
        // Projection: only the two columns the chart needs. No entity hydration,
        // no assignee lazy-load (N+1), and no second count query.
        return find(
                "select d.date, d.actual from EdsDepartmentGoalEmployeeMetricHistory d " +
                        "where " + ServerUtils.checkForDeleted("d.deleted") +
                        " and d.goalAssignees.goal.id = ?",
                goalId
        );
    }
}
