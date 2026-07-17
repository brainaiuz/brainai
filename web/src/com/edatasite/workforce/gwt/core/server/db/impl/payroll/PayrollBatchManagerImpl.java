package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollBatch;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrollBatchManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 10/22/15
 * Time: 9:19 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository("payrollBatchManager")
public class PayrollBatchManagerImpl extends BaseManager<EdsPayrollBatch> implements PayrollBatchManager {

    @Autowired
    private RoleManager roleManager;

    @Autowired
    private JdbcSpringManager jdbcSpringManager;

    public PayrollBatchManagerImpl() {
        super(EdsPayrollBatch.class);
    }

    @Override
    public void removeEmployeesReferencebyBatch(Integer objectID) {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ").append(getCompanyId()).append(".emp_batch ");
        sql.append("where batch_id=").append(objectID);
        updateNative(sql.toString());
    }

    @Override
    public Integer getTotalCount() {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(distinct pb.objectID) from EdsPayrollBatch pb left join pb.managers m where " + ServerUtils.checkForDeleted("pb.deleted"));

        if (!(roleManager.hasRole(getUser(), EdsRole.DR) || roleManager.hasRole(getUser(), EdsRole.ADMIN)
                || roleManager.hasRole(getUser(), EdsRole.HR) || roleManager.hasRole(getUser(), EdsRole.ACCOUNTANT)
                || ServerUtils.hasPermission(PermissionConstants.PAYROLL_GROUP_FULL_ACCESS))) {
            sql.append(" and m.objectID = " + getUser().getObjectID());
        }

        Long count = (Long) findSingle(sql.toString());
        return count != null ? count.intValue() : 0;
    }


    @Override
    public List<EdsPayrollBatch> getPayrollBatchList(ListingFilterParameter lfp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct pb from EdsPayrollBatch pb left join pb.managers m where " + ServerUtils.checkForDeleted("pb.deleted"));

        if (!(roleManager.hasRole(getUser(), EdsRole.DR) || roleManager.hasRole(getUser(), EdsRole.ADMIN)
                || roleManager.hasRole(getUser(), EdsRole.HR) || roleManager.hasRole(getUser(), EdsRole.ACCOUNTANT)
                || ServerUtils.hasPermission(PermissionConstants.PAYROLL_GROUP_FULL_ACCESS))) {
            sql.append(" and m.objectID = " + getUser().getObjectID());
        }
        if (lfp.getSearchKey() != null && !lfp.getSearchKey().trim().isEmpty()) {
            String searchKey = lfp.getSearchKey().trim().toLowerCase();
            sql.append(" and (lower(pb.name) like '%" + searchKey + "%'")
                    .append("  or lower(pb.description) like '%" + searchKey + "%'")
                    .append(" )");

        }
        //ORDERING\\
        sql.append(" order by ");
        if ("name".equals(lfp.getSortField())) {
            sql.append(" pb.name ");
        } else if ("description".equals(lfp.getSortField())) {
            sql.append(" pb.description ");
        } else if ("type".equals(lfp.getSortField())) {
            sql.append(" pb.type ");
        } else {
            sql.append(" pb.objectID ");
        }
        if (!lfp.isAscending()) {
            sql.append(" desc ");
        }
        return findInterval(sql.toString(), lfp.getStart(), lfp.getLimit());
    }

    @Override
    public Map<Integer, Integer> getPayrollBatchEmployeeAmount() {
        String companyID = getCompanyId();
        Map<Integer, Integer> result = new HashMap<>();
        String query = "select eb.batch_id, count(distinct(eb.emp_id)) from " + companyID + ".emp_batch eb " +
                "left join " + companyID + ".myuser u on eb.emp_id=u.id " +
                "left join " + companyID + ".reference r on r.id=u.accountstatusid " +
                "where (u.deleted <> TRUE OR u.deleted IS NULL) and r.code <> 'RESIGNED_EMPLOYEE' group by eb.batch_id";
        List<Object[]> queryList = findNative(query);
        queryList.forEach(value -> result.put((Integer) value[0], ((BigInteger) value[1]).intValue()));
        return result;
    }

    @Override
    public ArrayList<SelectItem> getPayrollBatchesForLookUp(ListingFilterParameter lfp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct pb.id as id, pb.name as name from ").append(getCompanyId()).append(".payroll_batch pb ").append("\n");
        sql.append("left join ").append(getCompanyId()).append(".group_managers gm on gm.groupid = pb.id \n");
        sql.append("where ").append(ServerUtils.checkForDeleted("pb.deleted")).append("\n");

        if (!ServerUtils.hasPermission(PermissionConstants.PAYROLL_GROUP_FULL_ACCESS)) {
            sql.append(" and gm.managerid = " + getUser().getObjectID() + " ");
        }
        if (lfp.getSearchKey() != null) {
            sql.append(" and (").append("\n");
            sql.append(" lower(pb.name) like '").append(lfp.getSqlSearchKey()).append("' ");
            sql.append("or lower(pb.description) like '").append(lfp.getSqlSearchKey()).append("') ");
        }

        ArrayList<SelectItem> result = new ArrayList<>(jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(SelectItem.class)));

        return result;
    }

    @Override
    public List<EdsPayrollBatch> getManagerPayrollGroups(Integer managerID) {
        return find("select pb from EdsPayrollBatch pb left join pb.managers m where (pb.deleted is null or pb.deleted is false) and m.objectID = " + managerID);
    }

    @Override
    public void removeEmployeeFromGroups(Integer employeeID) {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ").append(getCompanyId()).append(".emp_batch ");
        sql.append("where emp_id =").append(employeeID);
        updateNative(sql.toString());
    }

    @Override
    public void removeEmployeesFromGroup(Integer objectID, Set<Integer> members) {
        updateNative("delete from " + getCompanyId() + ".emp_batch where batch_id = " + objectID + " and emp_id in (" + ServerUtils.getAsCommoDelimited(new ArrayList<>(members), "", ",") + ")");
    }

    @Override
    public void addEmployeePayrolBatch(Integer objectID, Set<Integer> members) {
        StringBuilder s = new StringBuilder();
        members.forEach(x -> s.append(
                "insert into " + getCompanyId() + ".emp_batch (batch_id, emp_id) " +
                        "select " + objectID + ", " + x + " where not exists(" +
                        "select 1 from " + getCompanyId() + ".emp_batch where batch_id=" + objectID + " and emp_id=" + x + ");"));
        updateNative(s.toString());
    }
}
