package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.EdsSickRequestDuration;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.SickRequestDurationManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * User: Ilhombek
 * Date: 4/19/13
 * Time: 9:56 AM
 */
@Repository("sickRequestDurationManager")
public class SickRequestDurationManagerImpl extends BaseManager<EdsSickRequestDuration> implements SickRequestDurationManager, Constants {

    public SickRequestDurationManagerImpl() {
        super(EdsSickRequestDuration.class);
    }

    private final String hours = " coalesce(SUM(CASE WHEN sickR.includeDayOffs then cast(s.durationTime as real)/60 WHEN s.holiday = false THEN cast(s.durationTime as real)/60 ELSE (CASE WHEN s.holidayfromannualleave = true THEN cast(s.timeslot as real)/60 ELSE 0 END) END), 0) as hours ";
    private final String days = Constants.durationDays;
    private final String paid_days = " coalesce(SUM(CASE WHEN sickR.includeDayOffs and isPaid is true then s.day WHEN s.holiday = false and isPaid is true THEN s.day ELSE (CASE WHEN s.holidayfromannualleave = true and isPaid is true THEN 1 ELSE 0 END) END), 0) as paiddays ";
    private final String non_paid_days = " coalesce(SUM(CASE WHEN sickR.includeDayOffs and isPaid is not true then s.day WHEN s.holiday = false and isPaid is not true THEN s.day ELSE (CASE WHEN s.holidayfromannualleave = true and isPaid is not true THEN 1 ELSE 0 END) END), 0) as nonpaiddays ";

    public Double[] getLeaveRequestMinutes(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        sql.append("SELECT  ");
        sql.append(hours).append(", ");
        sql.append(days).append(", ");
        sql.append(paid_days).append(", ");
        sql.append(non_paid_days).append(" ");

        sql.append("FROM ").append(companyID).append(".sickrequestduration s  \n");
        sql.append("inner JOIN ").append(companyID).append(".sickrequest sr on sr.id = s.sickrequestid  \n");
        sql.append("inner JOIN ").append(companyID).append(".leave_reason sickR on sr.reason_code=sickR.code  \n");
        sql.append("inner JOIN ").append(companyID).append(".attendancerawdata a ON (a.date=s.date and a.employeeid = sr.employeeid )  \n");
        sql.append("WHERE (s.daytype is null or s.daytype<>'" + Constants.USED_ANOHTER_LEAVE_OR_RECALL + "')  \n");

        if (fp.getYear() != null) {//leave request year
            sql.append("AND date_part('year', a.date) = ").append(fp.getYear()).append("  \n");
        }
        if (fp.getStartDate() != null && fp.getEndDate() != null) {//leave request start/end date
            sql.append("AND a.date between '").append(formatter.format(fp.getStartDate())).append("' and '").append(formatter.format(fp.getEndDate())).append("'  \n");
        }
        if (fp.getEmployeeId() != null) {//leave request employee id
            sql.append("AND a.employeeid = ").append(fp.getEmployeeId()).append("  \n");
        }
        if (fp.getObjectId() != null) {//sick request id
            sql.append(" AND s.sickRequestID = ").append(fp.getObjectId()).append("  \n");
        }
        if (fp.getReasonID() != null) {
            sql.append(" AND sr.reason = ").append(fp.getReasonID()).append("  \n");
        }
        if (fp.getStatusID() != null) {
            sql.append(" AND sr.overallstatus = ").append(fp.getStatusID()).append("  \n");
        }
        sql.append("AND a.timeslot <> 0 \n");
        sql.append("AND (a.leave <> 0 or a.leavedenied <> 0 or leavepending <> 0)");

        Object[] listResult = (Object[]) findNativeSingle(sql.toString());

        Double[] leaveRequestMinutes = new Double[4];
        leaveRequestMinutes[0] = (Double) listResult[0];
        leaveRequestMinutes[1] = ((BigDecimal) listResult[1]).doubleValue();
        leaveRequestMinutes[2] = ((BigDecimal) listResult[2]).doubleValue();
        leaveRequestMinutes[3] = ((BigDecimal) listResult[3]).doubleValue();

        return leaveRequestMinutes;
    }

    @Deprecated
    public HashMap<Integer, Double[]> getAllowanceSpent(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        String lastYearMinutesValue = "s.lastyearminutes";
        if (fp.isZeroAvoided()) {
            lastYearMinutesValue = "0";
        }
        sql.append("SELECT ")
                .append("  employeeid, ")
                .append("  sum(t.minutesFromAllowance) AS minutesFromAllowance, ")
                .append("  sum(t.hoursFromAllowance)   AS hoursFromAllowance, ")
                .append("  sum(t.daysFromAllowance)    AS daysFromAllowance, ")
                .append("  sum (t.lastyearminutes) as lastyearminutes ")
                .append("FROM (SELECT ")
                .append("        a.employeeid, ")
                .append("        CASE WHEN a.holiday = FALSE AND (s.durationTime > 0 or ").append(lastYearMinutesValue).append(" > 0) ")
                .append("          THEN cast(s.durationTime + ").append(lastYearMinutesValue).append(" AS REAL) / 1 ")
                .append("        ELSE (CASE WHEN a.holidayfromannualleave = TRUE ")
                .append("          THEN cast(a.timeslot AS REAL) / 1 ")
                .append("              ELSE 0 END) END AS minutesFromAllowance, ")
                .append("        CASE WHEN a.holiday = FALSE AND (s.durationTime > 0 or ").append(lastYearMinutesValue).append(" > 0) ")
                .append("          THEN cast(").append(lastYearMinutesValue).append(" + s.durationTime AS REAL) / 60 ")
                .append("        ELSE (CASE WHEN a.holidayfromannualleave = TRUE ")
                .append("          THEN cast(a.timeslot AS REAL) / 60 ")
                .append("              ELSE 0 END) END AS hoursFromAllowance, ")
                .append("        CASE WHEN a.holiday = FALSE AND (").append(lastYearMinutesValue).append(" > 0 or s.durationTime > 0) ")
                .append("          THEN cast(").append(lastYearMinutesValue).append(" + s.durationTime AS REAL) / cast(a.timeslot AS REAL) ")
                .append("        ELSE (CASE WHEN a.holidayfromannualleave = TRUE ")
                .append("          THEN 1 ")
                .append("              ELSE 0 END) END AS daysFromAllowance, ")
                .append("        s.lastyearminutes as lastyearminutes ")
                .append("      FROM ").append(companyID).append(".sickrequestduration s ")
                .append("      INNER JOIN ").append(companyID).append(".sickrequest sr ON s.sickrequestid = sr.id ")
                .append("        INNER JOIN ").append(companyID).append(".attendancerawdata a ON (sr.employeeid = a.employeeid AND s.date = a.date) ")
                .append("      WHERE sr.toTakeFromAllowance is true and s.daytype<>'" + Constants.USED_ANOHTER_LEAVE_OR_RECALL + "' ");
        if (fp.getYear() != null) {
            sql.append(" AND date_part('year', a.date) = " + fp.getYear());
        }
        sql.append(" AND a.timeslot <> 0 ");
        sql.append(" AND (a.leave <> 0 or a.leavedenied <> 0 or leavepending <> 0)");
        if (fp.getReasonCode() != null) {
            sql.append(" AND sr.reason_code = '" + fp.getReasonCode() + "'");
        }
        if (fp.getStatusIDs() != null && fp.getStatusIDs().length > 0) {
            sql.append(" AND sr.overallstatus in (").append(ServerUtils.getAsCommoDelimited(Arrays.asList(fp.getStatusIDs()), "0")).append(")  ");
        } else if (fp.getStatusID() != null) {
            sql.append(" AND sr.overallstatus = ").append(fp.getStatusID()).append("  ");
        }

        if (fp.getEmployeeId() != null) {
            sql.append(" AND sr.employeeid = ").append(fp.getEmployeeId()).append("  ");
        }
        if (fp.getObjectId() != null) {
            sql.append(" AND sr.id = ").append(fp.getObjectId()).append("  ");
        }
        if (fp.getObjectId() == null && fp.getAvoidId() != null) {
            sql.append(" AND sr.id != ").append(fp.getObjectId()).append("  ");
        }
        sql.append(") t ")
                .append("GROUP BY t.employeeid");
        if (fp.getObjectId() == null && fp.getReasonID() != null) {
            sql.append("      UNION ALL ")
                    .append("      SELECT ")
                    .append("        pti.employee_id, ")
                    .append("        0, ")
                    .append("        0, ")
                    .append("        0, ")
                    .append("        pd.leaveminutes ")
                    .append(" ")
                    .append("      FROM ").append(companyID).append(".paymentdeduction pd ")
                    .append("        JOIN ").append(companyID).append(".payslip_payments pp ON pp.payment_deduction_id = pd.id ")
                    .append("        LEFT JOIN ").append(companyID).append(".paysliptableitem pti ON pti.id = pp.payslip_item_id ")
                    .append("        JOIN ").append(companyID).append(".reference ref ON ref.id = pti.status_id ")
                    .append("      WHERE 1=1");
            if (fp.getYear() != null) {
                sql.append("        and pd.leavePaymentYear = ").append(fp.getYear());
            }
            sql.append(" AND leaveminutes IS NOT NULL AND (pti.deleted IS NULL OR pti.deleted <> TRUE) ")
                    .append("        AND ref.code = 'PY_APPROVED' AND pd.leavereasonid = ").append(fp.getReasonID());
            if (fp.getEmployeeId() != null) {
                sql.append(" AND pti.employee_id = ").append(fp.getEmployeeId());
            }
        }

        List<Object[]> employeeStats = findNative(sql.toString());
        HashMap<Integer, Double[]> result = new HashMap<>();
        for (Object[] employeeStat : employeeStats) {
            Integer employeeID = (Integer) employeeStat[0];
            Double spentAnnualMinutes = (Double) employeeStat[1];
            Double spentAnnualHours = (Double) employeeStat[2];
            Double spentAnnualDays;

            if (employeeStat[3] != null && employeeStat[3] instanceof Float) {
                spentAnnualDays = ((Float) employeeStat[3]).doubleValue();
            } else {
                spentAnnualDays = (Double) employeeStat[3];
            }

            Double[] spentStats = new Double[4];
            spentStats[0] = spentAnnualMinutes;
            spentStats[1] = spentAnnualHours;
            spentStats[2] = spentAnnualDays;
            spentStats[3] = ((BigInteger) employeeStat[4]).doubleValue();
            result.put(employeeID, spentStats);
        }

        return result;
    }

    @Transactional
    public EdsSickRequestDuration getSickRequestDurationT(Date selectedDate, Integer sickRequestID, Integer periodId, String dayType) {
        EdsSickRequestDuration sickRequestDuration = (EdsSickRequestDuration) findSingle("SELECT srd FROM EdsSickRequestDuration srd WHERE srd.date = ? AND srd.sickRequestID=?", selectedDate, sickRequestID);
        if (sickRequestDuration == null || sickRequestDuration.isNew()) {
            sickRequestDuration = new EdsSickRequestDuration();
            sickRequestDuration.setDate(selectedDate);
            sickRequestDuration.setSickRequestID(sickRequestID);
            sickRequestDuration.setPeriodID(periodId);
            sickRequestDuration.setDayType(dayType);
            create(sickRequestDuration);
        }
        return sickRequestDuration;
    }

    @Override
    public Integer getLeaveMinutes(Integer requestID, Date date) {
        Integer leaveMinutes = (Integer) findSingle("select coalesce(durationTime,0) from EdsSickRequestDuration where sickRequestID = ? and date = ?", requestID, date);
        return leaveMinutes != null ? leaveMinutes : 0;
    }

    @Override
    public Map<Integer, Double[]> getAllowanceSpentByEmployees(ListingFilterParameter filter) {
        if (filter == null) {
            return Collections.emptyMap();
        }
        final String lvm = filter.isZeroAvoided() ? "0" : "s.lastyearminutes";
        final StringBuilder sql = new StringBuilder();

        sql.append("SELECT ")
                .append("    employeeid, ")
                .append("    sum(t.minutesFromAllowance) AS minutesFromAllowance, ")
                .append("    sum(t.hoursFromAllowance)   AS hoursFromAllowance, ")
                .append("    sum(t.daysFromAllowance)    AS daysFromAllowance, ")
                .append("    sum(t.lastyearminutes) as lastyearminutes ")
                .append("    from (")
                .append("        select ")
                .append("            a.employeeid, ")
                .append("            CASE ")
                .append("                WHEN a.holiday = FALSE AND (s.durationTime > 0 or ").append(lvm).append(" > 0) ")
                .append("                    THEN cast(s.durationTime + ").append(lvm).append(" AS REAL) / 1 ")
                .append("                ELSE (CASE WHEN a.holidayfromannualleave = TRUE ")
                .append("                    THEN cast(a.timeslot AS REAL) / 1 ")
                .append("                ELSE 0 END) END AS minutesFromAllowance, ")
                .append("            CASE ")
                .append("                WHEN a.holiday = FALSE AND (s.durationTime > 0 or ").append(lvm).append(" > 0) ")
                .append("                    THEN cast(").append(lvm).append(" + s.durationTime AS REAL) / 60 ")
                .append("                ELSE (CASE WHEN a.holidayfromannualleave = TRUE ")
                .append("                    THEN cast(a.timeslot AS REAL) / 60 ")
                .append("                ELSE 0 END) END AS hoursFromAllowance, ")
                .append("            CASE ")
                .append("                WHEN a.holiday = FALSE ")
                .append("                    THEN ").append(lvm).append("+ s.day ")
                .append("                ELSE (CASE WHEN a.holidayfromannualleave = TRUE ")
                .append("                    THEN 1 ")
                .append("              ELSE 0 END) END AS daysFromAllowance, ")
                .append("            s.lastyearminutes as lastyearminutes ")
                .append("    FROM ").append(getCompanyId()).append(".sickrequestduration s ")
                .append("        INNER JOIN ").append(getCompanyId()).append(".sickrequest sr ON s.sickrequestid = sr.id ")
                .append("        INNER JOIN ").append(getCompanyId()).append(".attendancerawdata a ON (sr.employeeid = a.employeeid AND s.date = a.date) ")
                .append("    WHERE sr.toTakeFromAllowance is true and s.daytype<>'" + Constants.USED_ANOHTER_LEAVE_OR_RECALL + "' ")
                .append("        AND a.timeslot <> 0 ")
                .append("        AND (a.leave <> 0 or a.leavedenied <> 0 or leavepending <> 0)");
        if (filter.getYear() != null) {
            sql.append("        AND date_part('year', a.date) = :yearParam");
        }
        if (filter.getReasonCode() != null) {
            sql.append(" AND sr.reason_code = '" + filter.getReasonCode() + "'");
        }
        if (filter.getStatusIDs() != null && filter.getStatusIDs().length > 0) {
            sql.append(" AND sr.overallstatus in (:statusIdsParam) ");
        } else if (filter.getStatusID() != null) {
            sql.append(" AND sr.overallstatus = :statusIdParam ");
        }
        if (filter.getEmployeeId() != null) {
            sql.append(" AND sr.employeeid = :employeeIdParam ");
        }
        if (filter.getObjectIDs() != null && !filter.getObjectIDs().isEmpty()) {
            sql.append(" AND sr.employeeid in (:employeeIds) ");
        }
        if (filter.getObjectId() != null) {
            sql.append(" AND sr.id = :objectIdParam ");
        }
        if (filter.getObjectId() == null && filter.getAvoidId() != null) {
            sql.append(" AND sr.id != :avoidIdParam ");
        }
        if (filter.getObjectId() == null && filter.getReasonID() != null) {
            sql.append("      UNION ALL ")
                    .append("      SELECT ")
                    .append("        pti.employee_id, ")
                    .append("        0, ")
                    .append("        0, ")
                    .append("        0, ")
                    .append("        pd.leaveminutes ")
                    .append("      FROM ").append(getCompanyId()).append(".paymentdeduction pd ")
                    .append("        JOIN ").append(getCompanyId()).append(".payslip_payments pp ON pp.payment_deduction_id = pd.id ")
                    .append("        LEFT JOIN ").append(getCompanyId()).append(".paysliptableitem pti ON pti.id = pp.payslip_item_id ")
                    .append("        JOIN ").append(getCompanyId()).append(".reference ref ON ref.id = pti.status_id ")
                    .append("      WHERE 1=1");
            if (filter.getYear() != null) {
                sql.append("        and pd.leavePaymentYear = :yearParam");
            }
            sql.append("        AND leaveminutes IS NOT NULL ")
                    .append("        AND (pti.deleted IS NULL OR pti.deleted <> TRUE)")
                    .append("        AND ref.code = 'PY_APPROVED' ")
                    .append("        AND pd.leavereasonid = :reasonIdParam");
            if (filter.getEmployeeId() != null) {
                sql.append(" AND pti.employee_id = :employeeIdParam");
            }
            if (filter.getObjectIDs() != null && !filter.getObjectIDs().isEmpty()) {
                sql.append(" AND pti.employee_id in (:employeeIds)");
            }
        }
        sql.append(") t ")
                .append("GROUP BY t.employeeid");
        Query query = this.slaveEntityManager.createNativeQuery(sql.toString());

        if (filter.getYear() != null) {
            query = query.setParameter("yearParam", filter.getYear());
        }
        if (filter.getReasonID() != null) {
            query = query.setParameter("reasonIdParam", filter.getReasonID());
        }
        if (filter.getStatusIDs() != null && filter.getStatusIDs().length > 0) {
            query = query.setParameter("statusIdsParam", filter.getStatusIDs());
        } else if (filter.getStatusID() != null) {
            query = query.setParameter("statusIdParam", filter.getStatusID());
        }
        if (filter.getEmployeeId() != null) {
            query.setParameter("employeeIdParam", filter.getEmployeeId());
        }
        if (filter.getObjectId() != null) {
            query = query.setParameter("objectIdParam", filter.getObjectId());
        }
        if (filter.getObjectId() == null && filter.getAvoidId() != null) {
            query = query.setParameter("avoidIdParam", filter.getAvoidId());
        }
        if (filter.getObjectIDs() != null && !filter.getObjectIDs().isEmpty()) {
            query = query.setParameter("employeeIds", filter.getObjectIDs());
        }
        final List<Object[]> list = query.getResultList();

        final Map<Integer, Double[]> result = new HashMap<>();
        for (Object[] employeeStat : list) {
            final Integer employeeId = (Integer) (employeeStat.length > 0 && employeeStat[0] != null
                    ? employeeStat[0]
                    : null);
            final Double spentAnnualMinutes = (Double) (employeeStat.length > 1 && employeeStat[1] != null
                    ? employeeStat[1]
                    : null);
            final Double spentAnnualHours = (Double) (employeeStat.length > 2 && employeeStat[2] != null
                    ? employeeStat[2]
                    : null);
            final Double spentAnnualDays;

            if (employeeStat[3] != null && employeeStat[3] instanceof Float) {
                spentAnnualDays = ((Float) employeeStat[3]).doubleValue();
            } else {
                spentAnnualDays = (Double) employeeStat[3];
            }
            final double lastYearMins = employeeStat.length > 3 && employeeStat[4] != null
                    ? ((BigInteger) employeeStat[4]).doubleValue()
                    : null;
            result.put(employeeId, new Double[]{spentAnnualMinutes, spentAnnualHours, spentAnnualDays, lastYearMins});
        }
        return result;
    }

    @Override
    public Map<String, Double[]> getUserTakenDays(ListingFilterParameter fp, List<String> reasons) {
        StringBuilder sql = new StringBuilder();
        sql.append("select sr.reason_code, ")
                .append(paid_days).append(", ")
                .append(non_paid_days).append(" ");
        sql.append("from ").append(getCompanyId()).append(".sickrequestduration s ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".sickrequest sr ON s.sickrequestid = sr.id ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".leave_reason sickR on sr.reason_code=sickR.code ");
        sql.append("where (s.daytype is null or s.daytype<>'" + Constants.USED_ANOHTER_LEAVE_OR_RECALL + "') ");
        if (fp.getYear() != null) {
            sql.append(" AND date_part('year', s.date) = " + fp.getYear());
        }
        sql.append(" AND s.timeslot <> 0 ");
        if (reasons != null && reasons.size() > 0) {
            sql.append(" AND sr.reason_code in ('" + ServerUtils.getAsCommoDelimited(reasons, "", "', '") + "')");
        }
        if (fp.getStatusID() != null) {
            sql.append(" AND sr.overallstatus = ").append(fp.getStatusID()).append("  ");
        }
        sql.append(" AND sr.employeeid = ").append(fp.getEmployeeId()).append("  ");
        sql.append("group by sr.employeeid, sr.reason_code");
        Map<String, Double[]> result = new HashMap<>();
        List<Object[]> listResult = findNative(sql.toString());

        listResult.forEach(obj -> {
            Double[] dd = new Double[2];
            dd[0] = ((BigDecimal) obj[1]).doubleValue();
            dd[1] = ((BigDecimal) obj[2]).doubleValue();
            if (obj[0] != null) {
                result.put(obj[0].toString(), dd);
            }
        });
        return result;
    }

    @Override
    public Double getUserSpentPaidAllowance(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ").append(paid_days).append(" from ");
        sql.append(getCompanyId()).append(".sickrequestduration s ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".sickrequest sr ON s.sickrequestid = sr.id ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".leave_reason sickR on sr.reason_code=sickR.code ");
        sql.append("where s.daytype<>'" + Constants.USED_ANOHTER_LEAVE_OR_RECALL + "' ");
        if (fp.getYear() != null) {
            sql.append(" AND date_part('year', s.date) = " + fp.getYear());
        }
        sql.append(" AND s.timeslot <> 0 ");
        if (fp.getReasonCode() != null) {
            sql.append(" AND sr.reason_code = '" + fp.getReasonCode() + "'");
        }
        if (fp.getStatusID() != null) {
            sql.append(" AND sr.overallstatus = ").append(fp.getStatusID()).append("  ");
        }
        sql.append(" AND sr.employeeid = ").append(fp.getEmployeeId()).append("  ");
        BigDecimal day = (BigDecimal) findNativeSingle(sql.toString());
        return day != null ? day.doubleValue() : 0d;
    }

    @Override
    public Map<Integer, Double[]> getEmployeesLeaveRequestsDuration(List<EdsSickRequest> requestList) {
        if (requestList == null || requestList.size() == 0) {
            return new HashMap<>();
        }
        String ids = requestList.stream()
                .map(x -> String.valueOf(x.getObjectID()))
                .collect(Collectors.joining(","));

        return getLeaveRequestsDurationByIds(ids);
    }

    public Map<Integer, Double[]> getLeaveRequestsDurationByIds(String ids) { // get durations by leave requests ids

        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        sql.append("SELECT srorg.id, ").append(hours).append(", ");
        sql.append(days).append(", ");
        sql.append(paid_days).append(", ");
        sql.append(non_paid_days);

        sql.append("FROM ").append(companyID).append(".sickrequestduration s  \n");
        sql.append("JOIN ").append(companyID).append(".sickrequest sr on sr.id = s.sickrequestid  \n");
        sql.append("LEFT JOIN ").append(companyID).append(".sickrequest pr on sr.parentId = pr.id  \n");
        sql.append("LEFT JOIN ").append(companyID).append(".sickrequest srorg on coalesce(sr.parentid, sr.id) = srorg.id  \n");
        sql.append("JOIN ").append(companyID).append(".leave_reason sickR on sr.reason_code=sickR.code \n");
        sql.append("WHERE (s.daytype is null OR s.daytype <> '" + Constants.USED_ANOHTER_LEAVE_OR_RECALL + "') \n");

        if (ids.length() > 0) {
            sql.append(" and (srorg.id in (").append(ids).append(")) ");
        }
        sql.append("AND s.timeslot <> 0 \n");
        sql.append("group by srorg.id ");

        Map<Integer, Double[]> result = new HashMap<>();
        List<Object[]> listResult = findNative(sql.toString());

        listResult.forEach(obj -> {
            if (result.get(Integer.parseInt(obj[0].toString())) == null) {
                Double[] dd = new Double[4];
                dd[0] = (Double) obj[1];
                dd[total] = ((BigDecimal) obj[2]).doubleValue();
                dd[paid] = ((BigDecimal) obj[3]).doubleValue();
                dd[non_paid] = ((BigDecimal) obj[4]).doubleValue();
                result.put(Integer.parseInt(obj[0].toString()), dd);
            } else {
                Double[] dd = result.get(Integer.parseInt(obj[0].toString()));
                dd[total] += ((BigDecimal) obj[2]).doubleValue();
                dd[paid] += ((BigDecimal) obj[3]).doubleValue();
                dd[non_paid] += ((BigDecimal) obj[4]).doubleValue();
                result.put(Integer.parseInt(obj[0].toString()), dd);
            }
        });
        return result;
    }

    @Override
    public HashMap<Integer, Double> getEmployeeLeaveDurations(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        sql.append("SELECT sr.employeeid, ").append(days);
        sql.append(" FROM ").append(companyID).append(".sickrequestduration s ");
        sql.append(" inner join ").append(companyID).append(".sickrequest sr on s.sickrequestid=sr.id ");
        sql.append(" inner join ").append(companyID).append(".leave_reason sickR on sr.reason_code=sickR.code  ");
        sql.append(" LEFT JOIN ").append(companyID).append(".reference st on sr.overallstatus=st.id ");
        sql.append(" WHERE s.timeslot <> 0 and daytype<>'" + Constants.USED_ANOHTER_LEAVE_OR_RECALL + "' ");
        if (fp.getYear() != null) {
            sql.append(" AND date_part('year', s.date) = ").append(fp.getYear());
        }
        if (fp.getEmployeeId() != null) {
            sql.append(" AND sr.employeeid = ").append(fp.getEmployeeId());
        }
        if (fp.getEmployeeIDs() != null && StringUtils.isNotBlank(fp.getEmployeeIDs())) {
            sql.append(" AND sr.employeeid in (").append(fp.getEmployeeIDs()).append(")");
        }
        if (StringUtils.isNotBlank(fp.getReasonCode())) {
            sql.append(" AND sr.reason_code = '").append(fp.getReasonCode()).append("'");
        }
        if (fp.isPaid() != null) {
            sql.append(" AND s.isPaid is true ");
        }
        if (StringUtils.isNotBlank(fp.getStatusCode())) {
            sql.append(" AND st.code='").append(fp.getStatusCode()).append("'");
        } else {
            sql.append(" AND st.code!='").append(Constants.LR_STATUS_SS_DENIED).append("'");
        }
        sql.append(" GROUP BY sr.employeeid ORDER BY sr.employeeid");
        List<Object[]> employeeStats = findNative(sql.toString());
        HashMap<Integer, Double> result = new HashMap<>();
        for (Object[] employeeStat : employeeStats) {
            Integer employeeID = (Integer) employeeStat[0];
            Double spentAnnualDays = ((BigDecimal) employeeStat[1]).doubleValue();
            result.put(employeeID, spentAnnualDays);
        }

        return result;
    }

    public void restoreDuration(String sickRequestIds, Date startDate, Date endDate) {
        updateNative("delete from " + getCompanyId() + ".SickRequestDuration where daytype<>'" + Constants.MONEY + "' and sickrequestid in (" + sickRequestIds + ") and date >= '" + startDate + "' and date <= '" + endDate + "'");
    }

    @Override
    public HashMap<Integer, Double> getDurationByDateAndEmployeeId(Date startDate, Date endDate, Integer employeeId) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        sql.append("select s.sickrequestid, ").append(days);
        sql.append(" from ").append(companyID).append(".sickrequestduration s ");
        sql.append(" inner join ").append(companyID).append(".sickrequest sr on s.sickrequestid=sr.id ");
        sql.append(" inner join ").append(companyID).append(".leave_reason sickR on sr.reason_code=sickR.code  ");
        sql.append(" where sr.reason_code in ('" + CustomFormConstants.LR_TYPE_ANNUAL_LEAVE + "') ");
        sql.append(" and sr.employeeid = " + employeeId + " and s.daytype<>'" + Constants.USED_ANOHTER_LEAVE_OR_RECALL + "' and s.date between '" + startDate + "' and '" + endDate + "'");
        sql.append(" GROUP BY s.sickrequestid ORDER BY s.sickrequestid");
        List<Object[]> durations = findNative(sql.toString());
        HashMap<Integer, Double> result = new HashMap<>();
        for (Object[] duration : durations) {
            Integer requestID = (Integer) duration[0];
            Double spentAnnualDays = ((BigDecimal) duration[1]).doubleValue();
            result.put(requestID, spentAnnualDays);
        }
        return result;
    }

    @Override
    public void deleteDurationByDateAndEmployeeId(Date startDate, Date endDate, Integer employeeId) {
        updateNative("delete from " + getCompanyId() + ".sickRequestDuration where id in (select srd.id from " + getCompanyId() + ".sickrequestduration srd " +
                "inner join " + getCompanyId() + ".sickrequest sr on sr.id = srd.sickrequestid " +
                "where sr.reason_code in ('" + CustomFormConstants.LR_TYPE_ANNUAL_LEAVE + "') " +
                "and sr.employeeid = " + employeeId + " and srd.date between '" + startDate + "' and '" + endDate + "')");
    }

    @Override
    public List<EdsSickRequestDuration> getDurationByDateAndEmployeeId(Date date, EdsSickRequest sickRequest) {
        return (List<EdsSickRequestDuration>) findNative("select srd.* from " + getCompanyId() + ".sickrequestduration srd " +
                " inner join " + getCompanyId() + ".sickrequest sr on sr.id = srd.sickrequestid " +
                " where sr.reason_code in ('" + CustomFormConstants.LR_TYPE_ANNUAL_LEAVE + "') and srd.daytype='" + Constants.DAY + "'" +
                " and sr.employeeid = " + sickRequest.getEmployee().getObjectID() + " and sr.id <> " + sickRequest.getObjectID() +
                " and srd.date = '" + date + "' ",EdsSickRequestDuration.class);
    }

    @Override
    public void deleteDurationBySickId(Integer sickId) {
        updateNative("delete from " + getCompanyId() + ".sickRequestDuration where sickrequestid = " + sickId);
    }

    @Override
    public Double getEmployeeLeaveDurationsByMonthAndYear(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        sql.append("SELECT ").append(days);
        sql.append(" FROM ").append(companyID).append(".sickrequestduration s ");
        sql.append(" inner join ").append(companyID).append(".sickrequest sr on s.sickrequestid=sr.id ");
        sql.append(" inner join ").append(companyID).append(".leave_reason sickR on sr.reason_code=sickR.code  ");
        sql.append(" LEFT JOIN ").append(companyID).append(".reference st on sr.overallstatus=st.id ");
        sql.append(" WHERE s.timeslot <> 0 and s.daytype<>'" + Constants.USED_ANOHTER_LEAVE_OR_RECALL + "' ");
        if (fp.getYear() != null) {
            sql.append(" AND date_part('year', s.date) = ").append(fp.getYear());
        }
        if (fp.getMonthId() != null) {
            sql.append(" AND date_part('month', s.date) = ").append(fp.getMonthId());
        }
        if (fp.getEmployeeId() != null) {
            sql.append(" AND sr.employeeid = ").append(fp.getEmployeeId());
        }
        if (StringUtils.isNotBlank(fp.getReasonCode())) {
            sql.append(" AND sr.reason_code = '").append(fp.getReasonCode()).append("'");
        }
        if (fp.isPaid() != null) {
            sql.append(" AND s.isPaid is true ");
        }
        if (StringUtils.isNotBlank(fp.getStatusCode())) {
            sql.append(" AND st.code='").append(fp.getStatusCode()).append("'");
        } else {
            sql.append(" AND st.code!='").append(Constants.LR_STATUS_SS_DENIED).append("'");
        }
        BigDecimal day = (BigDecimal) findNativeSingle(sql.toString());
        return day != null ? day.doubleValue() : 0d;

    }
}
