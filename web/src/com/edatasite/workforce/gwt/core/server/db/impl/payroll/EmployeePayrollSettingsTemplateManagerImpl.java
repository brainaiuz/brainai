package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsEmployeePayrollSettingsTemplate;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.EmployeePayrollSettingsTemplateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 11/25/15
 * Time: 4:28 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("employeePayrollSettingsTemplateManager")
public class EmployeePayrollSettingsTemplateManagerImpl extends BaseManager<EdsEmployeePayrollSettingsTemplate> implements EmployeePayrollSettingsTemplateManager {

    @Autowired
    private JdbcSpringManager jdbcSpringManager;

    public EmployeePayrollSettingsTemplateManagerImpl() {
        super(EdsEmployeePayrollSettingsTemplate.class);
    }

    @Override
    public List<EdsEmployeePayrollSettingsTemplate> getEmployeeTemplateList(ListingFilterParameter lfp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select et from EdsEmployeePayrollSettingsTemplate et ").append("\n");
        sql.append("left join et.status s ").append("\n");
        sql.append("where ").append(ServerUtils.checkForDeleted("et.deleted")).append("\n");
        sql.append("and et.status.code<>'APPROVED'").append("\n");
        if (lfp.getSqlSearchKey() != null) {
            sql.append(" and (lower(firstName) like '" + lfp.getSqlSearchKey() + "'");
            sql.append(" or lower(employeeCode) like '" + lfp.getSqlSearchKey() + "'");
            //sql.append(" or lower(ca.sender.firstname) like '" + lfp.getSqlSearchKey() + "'");
            sql.append(" or lower(s.name) like '" + lfp.getSqlSearchKey() + "') ");
        }

        if (lfp.getSortField() != null) {
            String code = lfp.getSortField();
            if ("firstName".equals(code)) {
                sql.append(" order by firstName ");
            } else if ("lastname".equals(code)) {
                sql.append(" order by lastName ");
            } else if ("sender".equals(code)) {
                sql.append(" order by et.sender ");
            } else if ("status".equals(code)) {
                sql.append(" order by et.status ");
            }
            sql.append(!lfp.isAscending() ? " desc " : " ");
        }

        return findInterval(sql.toString(), lfp.getStart(), lfp.getLimit());
    }

    @Override
    public Integer getEmployeeTemplateCount() {
        StringBuilder sql = new StringBuilder();
        sql.append("select et from EdsEmployeePayrollSettingsTemplate et ").append("\n");
        sql.append("left join et.status s ").append("\n");
        sql.append("where ").append(ServerUtils.checkForDeleted("et.deleted")).append("\n");
        sql.append("and et.status.code<>'APPROVED'").append("\n");

        return find(sql.toString()).size();
    }

    @Override
    public EdsEmployeePayrollSettingsTemplate getEmployeeAssignedTemplate(Integer employeeId) {
        return (EdsEmployeePayrollSettingsTemplate) findSingle("select et from EdsEmployeePayrollSettingsTemplate et where et.employeeID=" + employeeId + " and " + ServerUtils.checkForDeleted("et.deleted"));
    }

    @Override
    public HashMap<Integer, String> getEmployeeAssignedTemplateMap(String employeeIds) {
        HashMap<Integer, String> result = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n")
                .append("  ept.employee_id         AS id,\n")
                .append("  ept.id || '_' || s.name AS data\n")
                .append("FROM ").append(getCompanyId()).append(".ep_template ept\n")
                .append("  LEFT JOIN ").append(getCompanyId()).append(".employee e ON ept.employee_id = e.id\n")
                .append("  LEFT JOIN ").append(getCompanyId()).append(".reference s ON s.id = ept.status_id\n")
                .append("WHERE ").append(ServerUtils.checkForDeleted("ept.deleted")).append(" and ept.employee_id IN(").append(employeeIds).append(")\n");
        List<Map<String, Object>> queryResult = jdbcSpringManager.getSimpleJdbcTemplate().queryForList(sql.toString(), new HashMap<String, String>());
        for (Map<String, Object> map : queryResult) {
            for (String key : map.keySet()) {
                result.put((Integer) map.get("id"), String.valueOf(map.get("data")));
            }
        }
        return result;
    }
}
