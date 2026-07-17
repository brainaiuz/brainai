package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsShift;
import com.edatasite.workforce.core.domain.EdsShiftItem;
import com.edatasite.workforce.core.domain.EdsShiftSettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.ShiftManager;
import com.edatasite.workforce.gwt.hrms.client.rpc.ShiftItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Repository("shiftManager")
public class ShiftManagerImpl extends BaseManager<EdsShift> implements ShiftManager {

    @Autowired
    protected EmployeeManager employeeManager;
    @Autowired
    private ReferenceManager referenceManager;

    public ShiftManagerImpl() {
        super(EdsShift.class);
    }

    @Override
    public List<EdsShift> getList(ListingFilterParameter fp, Integer creatorId, boolean isAdmin) {
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct (b)  from  EdsShift b left join b.owners owners where (b.deleted is not true or b.deleted is null) ");
        sql.append(fp.getDepartmentId() != null && !isAdmin ? " and b.department.objectID = " + fp.getDepartmentId() : (!isAdmin ? " and (b.creator = " + creatorId + " or owners.objectID = " + creatorId + ") " : ""));
        if (fp.getStatusCode() != null) {
            sql.append(" and b.overallStatus = ").append(referenceManager.getByCode(fp.getStatusCode()).getObjectID().toString());
        }
        if (fp.getType() != null) {
            sql.append(" and b.lookupType = ").append(fp.getType());
        }
        if (isAdmin && !ServerUtils.isNullOrEmpty(fp.getDepartmentIds())) {
            sql.append(" and b.department.objectID in (").append(fp.getDepartmentIds()).append(")");
        }
        if (fp.getEmployeeId() != null) {
            sql.append(" and b.creator = ").append(fp.getEmployeeId());
        }
        if (fp.getApproverID() != null) {
            sql.append(" b.approver = ").append(employeeManager.get(fp.getApproverID()));
        }
        if (fp.getShiftPeriod() != null) {
            sql.append(" and to_char(b.period, 'MM/yyyy') = '"
                    + ServerUtils.getBankTransferDateNumber(fp.getShiftPeriod()) + "' ");
        }
        if (StringUtils.isNotBlank(fp.getSearchKey())) {
            sql.append(" and (lower(b.shiftCode) like '").append(fp.getSqlSearchKey()).append("') ");
        }
        if (fp.getSortField() != null) {
            sql.append(" ORDER BY  b.");
            if (fp.getSortField().equals(ShiftItem.APPROVAL_DATE)) {
                sql.append("approvalDate ");
            } else if (fp.getSortField().equals(ShiftItem.SUBMITTED_DATE)) {
                sql.append("submittedDate ");
            } else if (fp.getSortField().equals(ShiftItem.PERIOD)) {
                sql.append("period ");
            } else if (ShiftItem.CREATOR.equals(fp.getSortField())) {
                sql.append("creator ");
            } else if (ShiftItem.UPDATOR.equals(fp.getSortField())) {
                sql.append("updater ");
            } else if (ShiftItem.CREATED_DATE.equals(fp.getSortField())) {
                sql.append("createdDate ");
            } else if (ShiftItem.UPDATED_DATE.equals(fp.getSortField())) {
                sql.append("updatedDate ");
            } else if (ShiftItem.UPDATOR.equals(fp.getSortField())) {
                sql.append("updator ");
            } else if (ShiftItem.NUMBER.equals(fp.getSortField())) {
                sql.append("shiftCode ");
            } else if (ShiftItem.MONTH.equals(fp.getSortField())) {
                sql.append("period ");
            } else if (ShiftItem.STATUS.equals(fp.getSortField())) {
                sql.append("overallStatus ");
            } else if (ShiftItem.TYPE.equals(fp.getSortField())) {
                sql.append("lookupType ");
            }
            sql.append(fp.isAscending() ? "" : " desc");
        } else {
            sql.append(" order by  b.objectID  desc ");
        }

        return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    @Override
    public List<EdsShift> getShiftsTypeTeamByEmployeeId(Integer empId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT DISTINCT sh.*\n" +
                "FROM ").append(getCompanyId()).append(".shift sh\n" +
                "LEFT JOIN ").append(getCompanyId()).append(".shift_items shi ON sh.id = shi.shift_id\n" +
                "LEFT JOIN ").append(getCompanyId()).append(".brigadas b ON shi.groupId = b.id\n" +
                "LEFT JOIN ").append(getCompanyId()).append(".brigada_employees be ON b.id = be.projectid\n" +
                "LEFT JOIN ").append(getCompanyId()).append(".teamEmployee te ON be.employeeDepartmentId = te.id\n" +
                "WHERE sh.deleted IS NOT TRUE\n" +
                "AND te.employeeId = ").append(empId);
        return findNative(query.toString(), EdsShift.class);
    }

    @Override
    public List<EdsShift> getShiftsTypeEmployeeByEmployeeId(Integer empId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT DISTINCT sh.*\n" +
                "FROM ").append(getCompanyId()).append(".shift sh \n" +
                "LEFT JOIN ").append(getCompanyId()).append(".shift_items shi ON sh.id = shi.shift_id \n" +
                "WHERE sh.deleted IS NOT TRUE \n" +
                "AND (sh.lookuptype = 37 or sh.lookuptype = 47) \n" +
                "AND shi.groupid = ").append(empId);

        return findNative(query.toString(), EdsShift.class);
    }

    @Override
    public Integer getTotalCount(ListingFilterParameter fp, Integer userid, boolean isAdmin) {
        StringBuffer sql = new StringBuffer();
        sql.append("select count (distinct sh.id ) from ").append(getCompanyId());
        sql.append(".shift sh left join " + getCompanyId() + ".shift_owners sho on sh.id = sho.shift_id  where sh.deleted is not true " +
                (fp.getDepartmentId() != null && !isAdmin ? " and sh.departmentid = " + fp.getDepartmentId() : (!isAdmin ? " and (creatorId =  " + userid + " or sho.owner_id = " + userid + " ) " : "")));
        if (fp.getShiftPeriod() != null) {
            sql.append(" and to_char(sh.period, 'MM/yyyy') = '"
                    + ServerUtils.getBankTransferDateNumber(fp.getShiftPeriod()) + "' ");
        }
        if (fp.getType() != null) {
            sql.append(" and sh.lookupType = ").append(fp.getType());
        }
        return Integer.parseInt(findNativeSingle(sql.toString()).toString());
    }

    @Override
    public Integer getShiftLastIntNumber() {
        return (Integer) findSingle("select bce.intNumber from EdsShift bce where (bce.deleted = false or bce.deleted is null) and bce.intNumber is not null order by bce.intNumber desc");

    }

    @Override
    public boolean isShiftNumberExist(String numberString, Integer objectID) {
        List numberList;
        if (objectID != null) {
            numberList = find("select sh.intNumber from EdsShift sh where (sh.deleted = false or sh.deleted is null)  " + " and sh.shiftCode = ? and sh.objectID <> ? ", numberString, objectID);
        } else {
            numberList = find("select sh.intNumber from EdsShift sh where (sh.deleted = false or sh.deleted is null)  " + " and sh.shiftCode = ?", numberString);
        }
        return numberList != null && numberList.size() > 0;
    }

    @Override
    public EdsShiftItem getEmployeeDuty(Date period, Integer employeeId, boolean checkForTimeslot) {
        return (EdsShiftItem) findNativeSingle("select it.* from " + getCompanyId() + ".shift sh left join " + getCompanyId() + ".shift_items it on it.shift_id = sh.id " +
                "where (sh.deleted = false or sh.deleted is null) and lookupType = 37 and" +
                " to_char(sh.period, 'MM/yyyy') = '"
                + ServerUtils.getBankTransferDateNumber(period)
                + "' and it.groupId = " + employeeId + " and it.key = '" + period.getDate() + "'" + (checkForTimeslot ? "and it.shift_settings_id is not null" : ""), EdsShiftItem.class);
    }

    @Override
    public void updateEmployeeDuty(EdsShiftItem item, EdsShiftSettings timeslot) {
        updateNative("update " + getCompanyId() + ".shift_items set shift_settings_id = " + (timeslot != null ? timeslot.getObjectID() : null) + " where id = " + item.getObjectID());
    }

    public BigDecimal getShiftHours(Date period, Integer employeeId) {
        String sql = "select s.working_hours \n" +
                "from " + getCompanyId() + ".shift_teams_data std \n" +
                "         left join " + getCompanyId() + ".shift s on std.shift_id = s.id \n" +
                "         left join " + getCompanyId() + ".reference r on s.overallstatus = r.id \n" +
                "where " + ServerUtils.checkForDeleted("s.deleted") + " \n" +
                "  and s.lookuptype = " + LookUpConstants.BRIGADA_ID + " \n" +
                "  and std.empid = " + employeeId + " \n" +
                "  and to_char(s.period, 'yyyy-MM') = '" + new SimpleDateFormat("yyyy-MM").format(period) + "' \n" +
                "  and r.code = '" + Constants.SHIFT_APPROVED + "' \n";
        return (BigDecimal) findNativeSingle(sql);
    }

    public List<Object[]> getCustomEmployeesShift(Integer shiftId) {
        String schema = getCompanyId();

        String sql = "with shift_employees as (\n" +
                "    select distinct shi.groupid as emp_id, cast(shi.key as date) as work_date\n" +
                "    from  " + schema + ".shift_items shi\n" +
                "             join  " + schema + ".shift sh on sh.id = shi.shift_id\n" +
                "    where sh.id = " + shiftId +
                "      and shi.shift_settings_id is not null and shi.key is not null\n" +
                "),\n" +
                "     manager as (\n" +
                "         select b.managerid as manager_id\n" +
                "         from  " + schema + ".shift sh\n" +
                "                  join  " + schema + ".brigadas b on sh.brigadaid = b.id\n" +
                "         where sh.id = " + shiftId +
                "     ),\n" +
                "     managers_fingerprint as (\n" +
                "         select fd.userid as manager_id, manager.firstname||' '||manager.lastname as manager_name, fp.startdate as m_date, fa.latitude as m_latitude, fa.longitude as m_longitude, fd.device_id as m_deviceId\n" +
                "         from  " + schema + ".fingerprint fp\n" +
                "                  join  " + schema + ".userfingerprintdevice fd on fd.fingerprint_id = fp.fingerprintid and fd.device_id = fp.deviceuuid\n" +
                "                  join  " + schema + ".fingerprint_adjustment fa on fa.fingerprint_id = fp.id\n" +
                "                  join manager m on fd.userid = m.manager_id\n" +
                "                  join  " + schema + ".myuser manager on manager.id = m.manager_id\n" +
                "     ),\n" +
                "     employees_fingerprint as (\n" +
                "         select fd.userid as emp_id, emp.firstname||' '||emp.lastname as emp_name, p.name as emp_pos_name, fd.device_id as emp_deviceId, fp.startdate as emp_date, fa.latitude as emp_latitude, fa.longitude as emp_longitude, se.work_date\n" +
                "         from  " + schema + ".fingerprint fp\n" +
                "                  join " + schema + ".userfingerprintdevice fd on fd.fingerprint_id = fp.fingerprintid and fd.device_id = fp.deviceuuid\n" +
                "                  join " + schema + ".fingerprint_adjustment fa on fa.fingerprint_id = fp.id\n" +
                "                  join shift_employees se on se.emp_id = fd.userid and date(fp.startdate) = se.work_date\n" +
                "                  join " + schema + ".myuser emp on emp.id = se.emp_id\n" +
                "                  join " + schema + ".employee e on e.id = emp.id\n" +
                "                  join " + schema + ".position p on e.positionid = p.id" +
                "     ),\n" +
                "     ranked as (\n" +
                "         select ef.*, mf.manager_id, mf.manager_name, mf.m_deviceId as manager_deviceId, mf.m_latitude as manager_lat, mf.m_longitude as manager_lon, m_date,\n" +
                "                case when mf.manager_id is not null\n" +
                "                         then sqrt(power(ef.emp_latitude - mf.m_latitude, 2) + power(ef.emp_latitude - mf.m_latitude, 2)) end as distance_to_manager,\n" +
                "                row_number()\n" +
                "                over (\n" +
                "                    partition by ef.emp_id, ef.work_date order by case when mf.manager_id is null then 1 else 0 end,\n" +
                "                    sqrt(power(ef.emp_latitude - coalesce(mf.m_latitude, ef.emp_latitude), 2) + power(ef.emp_latitude - coalesce(mf.m_latitude, ef.emp_latitude), 2)) ) as rn\n" +
                "         from employees_fingerprint ef\n" +
                "                  left join managers_fingerprint mf on date(mf.m_date) = ef.work_date\n" +
                "     )\n" +
                "select emp_id, emp_name, emp_pos_name, emp_deviceId, work_date, emp_date as fingerprint_time, emp_latitude, emp_longitude, manager_id, manager_name, manager_deviceId, m_date, manager_lat, manager_lon, distance_to_manager\n" +
                "from ranked where rn = 1\n" +
                "order by emp_id, work_date;";

        List<Object[]> aNative = findNative(sql);
        return aNative;
    }
}
