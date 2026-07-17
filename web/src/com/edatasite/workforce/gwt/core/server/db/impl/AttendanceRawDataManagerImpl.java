package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsAttendanceRawData;
import com.edatasite.workforce.core.domain.EdsHoliday;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.availability.client.rpc.AttendanceStatusDto;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.DailyOvertimeData;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.AttendanceRawDataManager;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.SickRequestManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.rpc.AttendanceItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: Farhod
 * Date: 03/03/12
 * Time: 11:05
 */
@Repository("attendanceRawDataManager")
public class AttendanceRawDataManagerImpl extends BaseManager<EdsAttendanceRawData> implements AttendanceRawDataManager {

    @Autowired
    private JdbcSpringManager jdbcSpringManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private SickRequestManager sickRequestManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;

    public static final String ALL_DATES = "allDates";
    public static final String WORKING_DATES = "workingDates";
    public static final String HOLIDAY_DATES = "holidayDates";
    public static final String DAYOFF_DATES = "dayOffDates";
    private final DateFormat isoDateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    public AttendanceRawDataManagerImpl() {
        super(EdsAttendanceRawData.class);
    }

    public Date getLastEnteredDateForEmployee(Integer employeeID) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();

        sql.append("SELECT max(a.date) FROM " + companyID + ".attendancerawdata a ");
        sql.append("WHERE a.employeeid = " + employeeID);

        return (Date) findNativeSingle(sql.toString());
    }

    public List<EdsAttendanceRawData> getAttendanceRawDataByDates(Date from, Date to, Integer employeeID) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        sql.append("SELECT a.* FROM " + companyID + ".attendancerawdata a ");
        sql.append("WHERE a.date between '" + formatter.format(from) + "' and '" + formatter.format(to) + "' ");

        if (employeeID != null) {
            sql.append("and a.employeeid = " + employeeID);
        }
        sql.append(" ORDER BY a.date");

        return findNative(sql.toString(), EdsAttendanceRawData.class);
    }

    public void updateAttendanceRawDataByEmployeeAndTimeslotId(Integer employeeID, Date from, Date to) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        sql.append("UPDATE ").append(companyID).append(".attendancerawdata at ")
                .append("SET dayoff = (coalesce(tsi.starttime, tsi2.starttime) = 0 or coalesce(tsi.endtime, tsi2.endtime) = 0), ")
                .append("timeslot = case when date(rawdata.date) = date(tsi.exceptionaldate) ")
                .append("then ((tsi.endtime - tsi.starttime) - (tsi.lunchend - tsi.lunchstart) - (tsi.coffeeend - tsi.coffeestart)) ")
                .append("else ((tsi2.endtime - tsi2.starttime) - (tsi2.lunchend - tsi2.lunchstart) - (tsi2.coffeeend - tsi2.coffeestart)) end ")
                .append("from ").append(companyID).append(".attendancerawdata rawdata ")
                .append("join ").append(companyID).append(".employee em on em.id = rawdata.employeeid ")
                .append("join ").append(companyID).append(".timeslot ts on ts.id = em.timeslotid and ts.deleted is not true ")
                .append("left join ").append(companyID).append(".timeslotitem tsi on tsi.timeslotid = ts.id and date(rawdata.date)=date(tsi.exceptionaldate) ")
                .append("left join ").append(companyID).append(".timeslotitem tsi2 on tsi2.timeslotid = ts.id and extract('dow' from rawdata.date) = tsi2.day and tsi2.exceptionaldate is null ")
                .append("where rawdata.employeeid = ").append(employeeID).append(" and rawdata.id = at.id ")
                .append("and rawdata.date between '").append(formatter.format(from)).append("' and '").append(formatter.format(to)).append("' ");
        updateNative(sql.toString());
    }

    public List<Date> getLeaveDates(Date from, Date to, Integer employeeID) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        sql.append("SELECT a.date FROM " + companyID + ".attendancerawdata a ");
        sql.append("WHERE a.date between '" + formatter.format(from) + "' and '" + formatter.format(to) + "' ");
        sql.append("and a.leave > 0");

        if (employeeID != null) {
            sql.append(" and a.employeeid = " + employeeID);
        }
        sql.append(" ORDER BY a.date");

        return findNative(sql.toString());
    }

    @Override
    public Map<Integer, Integer> getPlannedDaysForEmployeeBatch(Date startDate, Date endDate, Set<Integer> employeeIds) {
        Map<Integer, Integer> result = new HashMap<>();
        if (employeeIds == null || employeeIds.isEmpty()) return result;
        String companyID = getCompanyId();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String ids = employeeIds.stream().map(Object::toString).collect(java.util.stream.Collectors.joining(","));
        String sql = "SELECT a.employeeid, COUNT(a.id) FROM " + companyID + ".attendancerawdata a" +
                " WHERE a.date BETWEEN '" + formatter.format(startDate) + "' AND '" + formatter.format(endDate) + "'" +
                " AND a.employeeid IN (" + ids + ")" +
                " AND a.dayoff IS NOT TRUE AND a.holiday IS NOT TRUE AND a.timeslot > 0" +
                " GROUP BY a.employeeid";
        List<Object> rows = (List<Object>) findNative(sql);
        if (rows != null) {
            for (Object row : rows) {
                Object[] r = (Object[]) row;
                Integer uid = r[0] != null ? ((Number) r[0]).intValue() : null;
                int count = r[1] != null ? ((Number) r[1]).intValue() : 0;
                if (uid != null) result.put(uid, count);
            }
        }
        return result;
    }

    @Override
    public Map<Integer, Integer> getLeaveDatesCountBatch(Date from, Date to, Set<Integer> employeeIds) {
        Map<Integer, Integer> result = new HashMap<>();
        if (employeeIds == null || employeeIds.isEmpty()) return result;
        String companyID = getCompanyId();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String ids = employeeIds.stream().map(Object::toString).collect(java.util.stream.Collectors.joining(","));
        String sql = "SELECT a.employeeid, COUNT(*) FROM " + companyID + ".attendancerawdata a" +
                " WHERE a.date BETWEEN '" + formatter.format(from) + "' AND '" + formatter.format(to) + "'" +
                " AND a.employeeid IN (" + ids + ")" +
                " AND a.leave > 0" +
                " GROUP BY a.employeeid";
        List<Object> rows = (List<Object>) findNative(sql);
        if (rows != null) {
            for (Object row : rows) {
                Object[] r = (Object[]) row;
                Integer uid = r[0] != null ? ((Number) r[0]).intValue() : null;
                int count = r[1] != null ? ((Number) r[1]).intValue() : 0;
                if (uid != null) result.put(uid, count);
            }
        }
        return result;
    }

    public EdsAttendanceRawData getAttendanceRawDataByDate(Date from, Integer employeeID) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        sql.append("SELECT a.* FROM " + companyID + ".attendancerawdata a ");
        sql.append("WHERE a.date = '" + formatter.format(from) + "' ");
        if (employeeID != null) {
            sql.append("and a.employeeid = " + employeeID);
        }

        return (EdsAttendanceRawData) findNativeSingle(sql.toString(), EdsAttendanceRawData.class);
    }

    public Double[] getLeaveRequestMinutes(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        sql.append("SELECT SUM(CASE when ref.code!='SS_APPROVED' then 0 ");
        sql.append("WHEN a.holiday = false THEN cast(s.lastyearminutes + s.durationtime as real)/60  ELSE (CASE WHEN a.holidayfromannualleave = true THEN cast(a.timeslot as real)/60 ELSE 0 END) END) as hoursApproved, ");
        sql.append("SUM(CASE when ref.code!='SS_APPROVED' then 0 ");
        sql.append("WHEN a.holiday = false THEN (s.lastyearminutes + s.durationtime)/cast(a.timeslot as real) ELSE (CASE WHEN a.holidayfromannualleave = true THEN 1 ELSE 0 END) END) as daysApproved, ");

        sql.append("SUM(CASE when ref.code!='DENIED' then 0 WHEN a.holiday = false THEN cast(s.lastyearminutes + s.durationtime as real)/60  ELSE 0 END) as hoursDenied, ");
        sql.append("SUM(CASE when ref.code!='DENIED' then 0 WHEN a.holiday = false THEN (s.lastyearminutes + s.durationtime)/cast(a.timeslot as real) ELSE 0 END) as daysDenied, ");

        sql.append("SUM(CASE when ref.code!='NOT_DEFINED' then 0 WHEN a.holiday = false THEN cast(s.durationtime as real)/60  ELSE 0 END) as hoursPending, ");
        sql.append("SUM(CASE when ref.code!='NOT_DEFINED' then 0 WHEN a.holiday = false THEN (s.lastyearminutes + s.durationtime)/cast(a.timeslot as real) ELSE 0 END) as daysPending, ");

        sql.append("SUM(CASE WHEN a.holiday = false and a.fromAnnualLeaveTime > 0 THEN cast(a.fromAnnualLeaveTime as real)/60  ELSE (CASE WHEN a.holidayfromannualleave = true THEN cast(a.timeslot as real)/60 ELSE 0 END) END) as hoursFromAnnual, ");
        sql.append("SUM(CASE WHEN a.holiday = false and a.fromAnnualLeaveTime > 0 THEN a.fromAnnualLeaveTime/cast(a.timeslot as real) ELSE (CASE WHEN a.holidayfromannualleave = true THEN 1 ELSE 0 END) END) as daysFromAnnual ");
        sql.append(" FROM " + companyID + ".sickrequest sr ");
        sql.append(" left JOIN " + companyID + ".reference ref on ref.id = sr.overallstatus ");
        sql.append(" inner JOIN " + companyID + ".sickrequestduration s on sr.id = s.sickrequestid and s.daytype<>'" + Constants.USED_ANOHTER_LEAVE_OR_RECALL + "' ");
        sql.append(" inner JOIN " + companyID + ".attendancerawdata a on (a.date=s.date and a.employeeid = sr.employeeid) ");

        sql.append(" WHERE 1=1 ");

        if (fp.getYear() != null) {
            sql.append(" AND date_part('year', a.date) = " + fp.getYear());
        }
        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append(" AND a.date between '" + formatter.format(fp.getStartDate()) + "' and '" + formatter.format(fp.getEndDate()) + "' ");
        }
        if (fp.getEmployeeId() != null) {
            sql.append(" AND a.employeeid = " + fp.getEmployeeId());
        }
        if (fp.getReasonID() != null) {
            sql.append(" AND sr.reason = " + fp.getReasonID());
        }
        if (fp.getEntityID() != null) {
            sql.append("   and sr.id != ").append(fp.getEntityID());
        }
        sql.append(" and (a.holidayfromannualleave is true or a.dayoff is not true) ");
        sql.append(" AND a.timeslot <> 0");
        sql.append(" AND sr.toTakeFromAllowance is true ");

        Object[] listResult = (Object[]) findNativeSingle(sql.toString());
        return Arrays.copyOf(listResult, listResult.length, Double[].class);
    }

    public Integer getMonthlyPlanned(Integer year, Integer month, Integer employeeID) {
        Calendar monthStart = new GregorianCalendar(year, month - 1, 1);
        Calendar monthEnd = new GregorianCalendar();
        monthEnd.setTime(monthStart.getTime());
        monthEnd.add(Calendar.MONTH, 1);
        monthEnd.add(Calendar.DAY_OF_MONTH, -1);

        List<AttendanceItem> list = sickRequestManager.getEmployeeDurationItems(employeeID, monthStart.getTime(), monthEnd.getTime());

        return list.stream()
                .filter(x -> !x.isHoliday() && !x.isDayOff())
                .mapToInt(x -> x.getLeave() > 0 ? (x.getTimeslot() - x.getLeave()) : x.getTimeslot())
                .sum();
    }

    public List<Date> getHolidayDays(ListingFilterParameter fp) {
        return getDates(fp, HOLIDAY_DATES);
    }

    public Date getLastDateOfLR(Integer employeeID) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        sql.append("SELECT MAX(a.date) FROM " + companyID + ".attendancerawdata a ");
//        sql.append("LEFT JOIN datejoin d ON a.dateid = d.id ");
        sql.append(" where a.employeeid = " + employeeID);
        sql.append(" and (a.leave > 0 or a.leavePending > 0 or a.leaveDenied > 0)");

        return (Date) findNativeSingle(sql.toString());
    }

    public List<Object> getDailyInTimes(Integer int_employeeID, Date start_date, Date end_date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT dj.from_date, \n");
        sql.append("(CASE WHEN SUM((EXTRACT(EPOCH FROM tt.endDate)-EXTRACT(EPOCH FROM tt.startDate))/60)<0 THEN 0 ELSE SUM((EXTRACT(EPOCH FROM tt.endDate)-EXTRACT(EPOCH FROM tt.startDate))/60) END) as time, \n");
        sql.append("tt.employeeid \n");
        sql.append("FROM ").append(getPublic()).append(".datejoin dj \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".timetrack tt ON (to_char(dj.from_date,'yyyy-MM-dd')=to_char(tt.startdate,'yyyy-MM-dd')) \n");
        sql.append("WHERE (dj.from_date between '").append(format.format(start_date)).append("' AND '").append(format.format(end_date)).append("' ) \n");
        sql.append("AND tt.employeeid=").append(int_employeeID).append(" \n");
        sql.append("AND tt.statusid=20 \n");
        sql.append("AND tt.startDate is not null \n");
        sql.append("AND tt.endDate is not null \n");
        sql.append("GROUP BY dj.from_date,tt.employeeid \n");
        sql.append("ORDER BY dj.from_date");

        return findNative(sql.toString());
    }

    @Override
    public List<Date> getAllDaysWithIntervel(ListingFilterParameter fp) {
        return getDates(fp, ALL_DATES);
    }

    private List<Date> getDates(ListingFilterParameter fp, String datesType) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        sql.append("SELECT DISTINCT a.date FROM ").append(companyID).append(".attendancerawdata a ");
        sql.append(" WHERE 1=1 ");
        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append(" AND a.date between '").append(formatter.format(fp.getStartDate())).append("' and '").append(formatter.format(fp.getEndDate())).append("' ");
        } else if (fp.getStartDate() != null) {
            sql.append(" AND a.date >= '").append(formatter.format(fp.getStartDate())).append("' ");
        }
        if (fp.getEmployeeId() != null) {
            sql.append(" and a.employeeid = ").append(fp.getEmployeeId());
        }
        if (datesType.equals(WORKING_DATES)) {
            sql.append(" and a.holiday is not true and a.dayOff is not true ");
        } else if (datesType.equals(HOLIDAY_DATES)) {
            sql.append(" and a.holiday = true and a.holidayfromannualleave is false ");
        } else if (datesType.equals(DAYOFF_DATES)) {
            sql.append(" and a.dayOff = true ");
        }
        sql.append(" order by a.date ");
        if (fp.getLimit() != null && fp.getLimit() > 0) {
            sql.append(" limit ").append(fp.getLimit());
        }
        return findNative(sql.toString());
    }

    public Object[] getWorkingDate(Integer employeeID, Date date) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return (Object[]) findSingle("SELECT DISTINCT a.holiday, a.dayOff, extract(dow from a.date),holidayFromAnnualLeave FROM EdsAttendanceRawData a WHERE a.employee.objectID = ? and a.date = '" + formatter.format(date) + "' ", employeeID);
    }

    @Override
    public List<Date> getWorkingDays(ListingFilterParameter fp) {
        return getDates(fp, WORKING_DATES);
    }

    @Override
    public List<Date> getWorkingDays(ListingFilterParameter fp, String datesType) {
        return getDates(fp, datesType);
    }

    @Override
    public List<DailyOvertimeData> getDailyOvertimeData(ListingFilterParameter lfp) {
        StringBuilder sql;
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PAYSLIP_FROM_TIMESHEET)) {
            sql = getDailyOvertimeDataFromTimesheet(lfp);
        } else if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.FINGERPRINT_DEVICE_ENABLED)) {
            sql = getDailyOvertimeDataFromFingerprint(lfp);
        } else {
            sql = getDailyOvertimeDataFromTimetrack(lfp);
        }
        return jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(DailyOvertimeData.class));
    }

    private StringBuilder getDailyOvertimeDataFromTimetrack(ListingFilterParameter lfp) {
        StringBuilder sql = new StringBuilder();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sql.append("select t.from_date as date, t.dayoff, t.holiday,").append("\n");
        sql.append("  case when t.holiday is true then t.workingtime/60 else  (t.workingtime - t.timeslot)/60 end as overtimeHour from (").append("\n");
        sql.append("  select dj.from_date, sum(ard.timeslot) as timeslot, ard.dayoff, ard.holiday,").append("\n");
        sql.append("    case when sum((extract(epoch from tt.endDate)-extract(epoch from tt.startDate))/60)<0 then 0  else ").append("\n");
        sql.append("        sum((extract(epoch from tt.endDate)-extract(epoch from tt.startDate))/60) end as workingTime").append("\n");
        sql.append("from ").append(getPublic()).append(".datejoin dj ").append("\n");
        sql.append("left join ").append(getCompanyId()).append(".timetrack tt ON (to_char(dj.from_date,'yyyy-MM-dd')=to_char(tt.startdate,'yyyy-MM-dd')) ");
        sql.append("left join ").append(getCompanyId()).append(".reference ref on tt.statusid = ref.id").append("\n");
        sql.append("left join ").append(getCompanyId()).append(".attendancerawdata ard on ard.date = dj.from_date").append("\n");
        sql.append("where dj.from_date between '").append(format.format(lfp.getStartDate())).append("' and '").append(format.format(lfp.getEndDate())).append("'\n");
        sql.append("and tt.employeeid=").append(lfp.getEmployeeId()).append("\n");
        sql.append("and ard.employeeid=").append(lfp.getEmployeeId()).append("\n");
        sql.append("and ref.code='AVAILABLE'").append("\n");
        sql.append("and tt.startDate is not null").append("\n");
        sql.append("and tt.endDate is not null").append("\n");
        sql.append("group by dj.from_date,tt.employeeid, ard.dayoff, ard.holiday ").append("\n");
        sql.append("order by dj.from_date) t ").append("\n");
        sql.append("where case when t.holiday is not true then t.workingtime > t.timeslot else 1=1 end");
        return sql;
    }

    private StringBuilder getDailyOvertimeDataFromFingerprint(ListingFilterParameter lfp) {
        StringBuilder sql = new StringBuilder();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sql.append("SELECT to_char(fp.startdate, 'yyyy-MM-dd') as date, cast(COALESCE(fp.actualinhours, 0) as integer) actualinhours, tsi.endtime - tsi.starttime timeslot, ");
        sql.append("case when (actualinhours - (tsi.endtime - tsi.starttime)) > 0 then (actualinhours - (tsi.endtime - tsi.starttime))/60 else 0 end overtimeHour, case when (to_char(fp.startdate, 'yyyy-MM-dd') = to_char(h.date, 'yyyy-MM-dd')) then true else false end holiday, ");
        sql.append("case when bool_or(((tsi.endTime-tsi.startTime)-(tsi.lunchEnd-tsi.lunchStart)-(tsi.coffeeEnd-tsi.coffeeStart))<=0) then true else false end dayOff ");
        sql.append("FROM " + getCompanyId() + ".employee e ");
        sql.append("JOIN " + getCompanyId() + ".myuser mu ON mu.id = e.id ");
        sql.append("LEFT JOIN " + getCompanyId() + ".location lc ON mu.locationId = lc.id ");
        sql.append("LEFT JOIN " + getCompanyId() + ".holiday_location hl ON hl.locations_id = lc.id ");
        sql.append("LEFT JOIN " + getCompanyId() + ".holiday h ON hl.holiday_id = h.id ");
        sql.append("LEFT JOIN (SELECT fp.eid employeeid, indate startdate, outdate enddate, COALESCE(fp.fdate,fp2.fdate) fpdate, ");
        sql.append("(date_part('hour', outdate) * 60 + date_part('min', outdate)) - (date_part('hour', indate) * 60 + date_part('min', indate)) actualinhours ");
        sql.append("FROM (SELECT fd.userid eid, date(startdate) fdate, min(startdate) indate FROM " + getCompanyId() + ".fingerprint fp ");
        sql.append("left join " + getCompanyId() + ".userfingerprintdevice fd on fd.fingerprint_id=fp.fingerprintid and fp.deviceuuid=fd.device_id ");
        sql.append("WHERE statusstring = '0' GROUP BY fd.userid, date(startdate)) fp ");
        sql.append("FULL OUTER JOIN ");
        sql.append("(SELECT fd.userid eid, date(startdate) fdate, max(startdate) outdate FROM " + getCompanyId() + ".fingerprint fp ");
        sql.append("left join " + getCompanyId() + ".userfingerprintdevice fd on fd.fingerprint_id=fp.fingerprintid and fp.deviceuuid=fd.device_id ");
        sql.append("WHERE statusstring = '1' GROUP BY fd.userid, date(startdate)) fp2 ON fp.eid = fp2.eid AND fp.fdate = fp2.fdate ");
        sql.append("WHERE outdate > indate ");
        sql.append("UNION ");
        sql.append("SELECT fp.eid employeeid,indate startdate, outdate enddate, COALESCE(fp.fdate,fp2.fdate) fpdate, ");
        sql.append("(60*24 + date_part('hour', outdate) * 60 + date_part('min', outdate)) -(date_part('hour', indate) * 60 + date_part('min', indate)) actualinhours ");
        sql.append("FROM (SELECT fd.userid eid, date(startdate) fdate, min(startdate) indate FROM " + getCompanyId() + ".fingerprint fp ");
        sql.append("LEFT JOIN " + getCompanyId() + ".userfingerprintdevice fd on fd.fingerprint_id=fp.fingerprintid and fp.deviceuuid=fd.device_id ");
        sql.append("WHERE statusstring = '0' GROUP BY fd.userid, date(startdate)) fp ");
        sql.append("FULL OUTER JOIN ");
        sql.append("(SELECT fd.userid eid, date(startdate) fdate, max(startdate) outdate ");
        sql.append("FROM " + getCompanyId() + ".fingerprint fp ");
        sql.append("left join " + getCompanyId() + ".userfingerprintdevice fd on fd.fingerprint_id=fp.fingerprintid and fp.deviceuuid=fd.device_id ");
        sql.append("WHERE statusstring = '1' GROUP BY fd.userid, date(startdate)) fp2 ON fp.eid = fp2.eid AND date(fp.fdate + INTERVAL '1' DAY) = fp2.fdate ");
        sql.append("WHERE (date_part('hour', indate) * 60 + date_part('min', indate)) >(date_part('hour', outdate) * 60 + date_part('min', outdate))) fp ON fp.employeeid = e.id ");
        sql.append("LEFT JOIN (SELECT tsi.endtime - tsi.starttime budgethours, tsi.* FROM " + getCompanyId() + ".timeslotitem tsi) tsi ON tsi.timeslotid = e.timeslotid and EXTRACT(dow FROM fp.fpdate)=tsi.day ");
        sql.append("WHERE e.id=").append(lfp.getEmployeeId().toString()).append(" and (fp.startdate BETWEEN '" + format.format(lfp.getStartDate()) + "' and '" + format.format(lfp.getEndDate()) + "') ");
        sql.append("group by fp.startdate, fp.actualinhours, tsi.endtime, tsi.starttime, fp.enddate, h.date ");
        sql.append("ORDER BY date asc");
        return sql;
    }

    public StringBuilder getDailyOvertimeDataFromTimesheet(ListingFilterParameter lfp) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder sql = new StringBuilder();
        sql.append("select dj.from_date date, ard.dayoff,ard.holiday, \n");
        sql.append("(case when ard.dayoff then 0 else COALESCE(ard.timeslot,0)/60 end) timeslotHour, \n");
        sql.append("(case when COALESCE(tsh.timespent,0)>COALESCE(ard.timeslot,0) then COALESCE(ard.timeslot,0)/60 else COALESCE(tsh.timespent,0)/60 end) as timesheet").append(", \n");
        sql.append("(case when COALESCE(tsh.timespent,0)>COALESCE(ard.timeslot,0) then (COALESCE(tsh.timespent,0)-COALESCE(ard.timeslot,0))/60 else 0 end) as overtimeHour").append(", \n");
        sql.append("(case when COALESCE(ard.timeslot,0)>COALESCE(tsh.timespent,0) then (COALESCE(ard.timeslot,0)-COALESCE(tsh.timespent,0))/60 else 0 end) as absenceHour").append(" \n");
        sql.append("from ").append(getPublic()).append(".datejoin dj ").append("\n");
        sql.append("left join (select ts.employeeid,date(ts.date) date, sum(ts.timespent) timespent \n");
        sql.append("from ").append(getCompanyId()).append(".timesheet ts \n");
        sql.append("join ").append(getCompanyId()).append(".reference rf on ts.statusId=rf.id \n");
        sql.append("where ts.timespent>0 and rf.code='_APPROVE' and ts.employeeid=").append(lfp.getEmployeeId()).append(" \n");
        sql.append("group by ts.employeeid, date(ts.date) ) tsh on date(dj.from_date)=tsh.date \n");
        sql.append("left join ").append(getCompanyId()).append(".attendancerawdata ard on ard.employeeid=").append(lfp.getEmployeeId()).append(" and date(dj.from_date)=date(ard.date)").append("\n");
        sql.append("where dj.from_date between '").append(format.format(lfp.getStartDate())).append("' and '").append(format.format(lfp.getEndDate())).append("'\n");
        sql.append("order by dj.from_date ").append("\n");
        return sql;
    }


    @Override
    public void updateHolidays(Date from, Date to, List<Integer> locationId, boolean isHoliday, boolean holidayfromannualleave) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        if (locationId == null) {
            masterEntityManager.createNativeQuery("update " + getCompanyId() + ".attendancerawdata set holiday=:isHoliday, holidayfromannualleave=:holidayfromannualleave " +
                            "where date between '" + formatter.format(from) + "' and '" + formatter.format(to) + "' ")
                    .setParameter("isHoliday", isHoliday)
                    .setParameter("holidayfromannualleave", holidayfromannualleave)
                    .executeUpdate();

        } else {
            masterEntityManager.createNativeQuery("update " + getCompanyId() + ".attendancerawdata as a set holiday=:isHoliday, holidayfromannualleave=:holidayfromannualleave " +
                            "where date between '" + formatter.format(from) + "' and '" + formatter.format(to) + "' " +
                            "and employeeid in (select mu.id from " + getCompanyId() + ".myuser as mu where mu.locationId in (:locationId))")
                    .setParameter("isHoliday", isHoliday)
                    .setParameter("locationId", locationId)
                    .setParameter("holidayfromannualleave", holidayfromannualleave)
                    .executeUpdate();
        }

        EdsUser user = userManager.getUser();
        if (from.compareTo(user.getUserDate(new Date())) >= 0) {
            StringBuilder sql = new StringBuilder();
            sql.append("update ").append(getCompanyId()).append(".sickrequestduration as s set holiday=:isHoliday, holidayfromannualleave=:holidayfromannualleave ");
            sql.append("where date between '").append(formatter.format(from)).append("' and '").append(formatter.format(to)).append("'  ");
            sql.append("and sickrequestid in (select sr.id from ").append(getCompanyId()).append(".sickrequest as sr ")
                    .append("left join ").append(getCompanyId()).append(".myuser as mu on mu.id=sr.employeeid ")
                    .append("where mu.deleted is false ");
            if (locationId != null) {
                sql.append("and mu.locationId in (:locationId)");
            }
            sql.append(")");

            if (locationId != null) {
                masterEntityManager.createNativeQuery(sql.toString())
                        .setParameter("isHoliday", isHoliday)
                        .setParameter("locationId", locationId)
                        .setParameter("holidayfromannualleave", holidayfromannualleave)
                        .executeUpdate();
            } else {
                masterEntityManager.createNativeQuery(sql.toString())
                        .setParameter("isHoliday", isHoliday)
                        .setParameter("holidayfromannualleave", holidayfromannualleave)
                        .executeUpdate();
            }
        }
    }

    @Override
    public void updateHolidays(EdsHoliday holiday, Integer employeeId, boolean isHoliday, boolean holidayfromannualleave) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder sql = new StringBuilder();
        sql.append("update ").append(getCompanyId()).append(".attendancerawdata as a set holiday=:isHoliday, holidayfromannualleave=:holidayfromannualleave ");
        sql.append("where employeeid=:employeeId and ");
        sql.append("date between '").append(formatter.format(holiday.getStartDate())).append("' and '").append(formatter.format(holiday.getEndDate())).append("'  ");
        masterEntityManager.createNativeQuery(sql.toString())
                .setParameter("isHoliday", isHoliday)
                .setParameter("employeeId", employeeId)
                .setParameter("holidayfromannualleave", holidayfromannualleave)
                .executeUpdate();

        String d = "update " + getCompanyId() + ".sickrequestduration as s set holiday=:isHoliday, holidayfromannualleave=:holidayfromannualleave " +
                "where sickrequestid in (select sr.id from " + getCompanyId() + ".sickrequest as sr where sr.employeeid=:employeeid) " +
                "and date between '" + formatter.format(holiday.getStartDate()) + "' and '" + formatter.format(holiday.getEndDate()) + "'  ";
        masterEntityManager.createNativeQuery(d)
                .setParameter("isHoliday", isHoliday)
                .setParameter("employeeid", employeeId)
                .setParameter("holidayfromannualleave", holidayfromannualleave)
                .executeUpdate();
    }

    @Override
    public Date getEndDateForLeaveRequest(ListingFilterParameter fp) {
        return getDates(fp, WORKING_DATES).get(fp.getLimit() - 1);
    }

    @Override
    public void restoreAttendanceRawData(Integer employeeID, Date start, Date end) {
        updateNative("update " + getCompanyId() + ".attendancerawdata set leave = 0 where employeeid=" + employeeID + " and date between '" + start + "' and '" + end + "'");
    }

    @Override
    public long getPlannedHoursForEmployee(Date startDate, Date endDate, Integer employeeId) {
        return (long) findSingle("select coalesce(sum(a.timeSlot), 0) from EdsAttendanceRawData a where a.employee.objectID = ? and date between ? and ? and dayoff <> true and holiday <> true", employeeId, startDate, endDate);
    }

    @Override
    public int getPlannedDaysForEmployee(Date startDate, Date endDate, Integer employeeId) {
        long result = (long) findSingle("select count(id) from EdsAttendanceRawData a where a.employee.objectID = ? and date between ? and ? and dayoff <> true and holiday <> true and timeslot > 0", employeeId, startDate, endDate);
        return (int) result;
    }

    @Override
    public Map<Date, BigDecimal> getWorkingHours(ListingFilterParameter fp, String datesType) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        sql.append("SELECT DISTINCT a.date, cast(coalesce(a.timeslot,0) as double precision)/60 FROM ").append(companyID).append(".attendancerawdata a ");
        sql.append(" WHERE 1=1 ");
        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append(" AND a.date between '").append(formatter.format(fp.getStartDate())).append("' and '").append(formatter.format(fp.getEndDate())).append("' ");
        } else if (fp.getStartDate() != null) {
            sql.append(" AND a.date >= '").append(formatter.format(fp.getStartDate())).append("' ");
        }
        if (fp.getEmployeeId() != null) {
            sql.append(" and a.employeeid = ").append(fp.getEmployeeId());
        }
        if (fp.isShowLeaveRequest()) {
            sql.append(" and a.leave > 0");
        }
        if (datesType.equals(WORKING_DATES)) {
            sql.append(" and a.holiday is not true and a.dayOff is not true ");
        } else if (datesType.equals(HOLIDAY_DATES)) {
            sql.append(" and a.holiday = true and a.holidayfromannualleave is false ");
        } else if (datesType.equals(DAYOFF_DATES)) {
            sql.append(" and a.dayOff = true ");
        }
        sql.append(" order by a.date ");
        if (fp.getLimit() != null && fp.getLimit() > 0) {
            sql.append(" limit ").append(fp.getLimit());
        }
        List<Object[]> result = findNative(sql.toString());
        Map<Date, BigDecimal> resultMap = new HashMap<>();
        if (result == null || result.isEmpty()) {
            return resultMap;
        }
        for (Object[] item : result) {
            Date date = (Date) item[0];
            BigDecimal hours = BigDecimal.valueOf((Double) item[1]);
            resultMap.put(date, hours);
        }
        return resultMap;
    }

    @Override
    public Set<Date> getLeaveDays(ListingFilterParameter fp, String datesType) {
        //Checking for By Shift Employees
        if (Constants.BY_ATTENDANCE_REPORT.equals(fp.getExcludedType())) {
            return geLeaveDataDaysForHourlyEmployees(fp);
        }
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        sql.append("SELECT DISTINCT a.date ");
        sql.append(" FROM ").append(companyID).append(".sickrequest sr ");
        sql.append(" join ").append(companyID).append(".leave_reason sickR on sr.reason_code=sickR.code ");
        sql.append(" join ").append(companyID).append(".sickrequestduration sd on sr.id=sd.sickrequestid and (sd.daytype='DAY' or sd.daytype is null) ");
        sql.append(" join ").append(companyID).append(".attendancerawdata a on sr.employeeid=a.employeeid and sd.date=a.date ");
        sql.append(" join ").append(companyID).append(".reference st on sr.overallstatus=st.id ");
        sql.append(" WHERE sd.durationTime > 0 ");
        if (EdsSickRequest.APPROVED.equals(fp.getStatusCode())) {
            sql.append(" AND st.code='" + EdsSickRequest.APPROVED + "' ");
        }
        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append(" AND a.date between '").append(formatter.format(fp.getStartDate())).append("' and '").append(formatter.format(fp.getEndDate())).append("' ");
        } else if (fp.getStartDate() != null) {
            sql.append(" AND a.date >= '").append(formatter.format(fp.getStartDate())).append("' ");
        }
        if (fp.getEmployeeId() != null) {
            sql.append(" and a.employeeid = ").append(fp.getEmployeeId());
        }
        if (datesType.equals(WORKING_DATES)) {
            sql.append(" and a.holiday is not true and a.dayOff is not true ");
        } else if (datesType.equals(HOLIDAY_DATES)) {
            sql.append(" and a.holiday = true and a.holidayfromannualleave is false ");
        } else if (datesType.equals(DAYOFF_DATES)) {
            sql.append(" and a.dayOff = true ");
        }
        sql.append(" order by a.date ");
        if (fp.getLimit() != null && fp.getLimit() > 0) {
            sql.append(" limit ").append(fp.getLimit());
        }
        return new HashSet<>(findNative(sql.toString()));
    }

    private Set<Date> geLeaveDataDaysForHourlyEmployees(ListingFilterParameter fp) {
        String companyID = getCompanyId();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT date(dj.from_date) ");
        sql.append(" FROM ").append(companyID).append(".sickrequest sr ");
        sql.append(" join ").append(companyID).append(".leave_reason sickR on sr.reason_code=sickR.code ");
        sql.append(" join ").append(companyID).append(".reference st on sr.overallstatus=st.id ");
        sql.append(" join datejoin dj on date(dj.from_date)>=date(sr.startdate) and date(sr.enddate)>=date(dj.from_date) ");
        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append(" WHERE date(dj.from_date) between '").append(formatter.format(fp.getStartDate())).append("' and '").append(formatter.format(fp.getEndDate())).append("' ");
        } else if (fp.getStartDate() != null) {
            sql.append(" WHERE date(dj.from_date) >= '").append(formatter.format(fp.getStartDate())).append("' ");
        }
        if (fp.getEmployeeId() != null) {
            sql.append(" and sr.employeeid = ").append(fp.getEmployeeId());
        }
        if (EdsSickRequest.APPROVED.equals(fp.getStatusCode())) {
            sql.append(" AND st.code='" + EdsSickRequest.APPROVED + "' ");
        }
        sql.append(" order by date(dj.from_date) ");

        return new HashSet<>(findNative(sql.toString()));
    }

    public List<AttendanceStatusDto> getEmployeeDailyLeaveStatus(Integer userId, Date fromDate, Date toDate) {
        if (Duration.between(fromDate.toInstant(), toDate.toInstant()).toDays() > 370) {
            throw new IllegalArgumentException("Date range cannot exceed 1 year");
        }
        String companyId = getCompanyId();
        EdsUser currentUser = getUser();
        String query = "with currentUser as (select userid, internationalization lang from " + companyId + ".userEmailSettings)\n" +
                "select distinct on (dj.from_date) dj.from_date as day,\n" +
                "                                  case\n" +
                "                                      when sr.id is not null then rs.shortname\n" +
                "                                      when ss.id is not null then ss.short_name\n" +
                "                                      when h.id is not null then 'H'\n" +
                "                                      when ti.starttime = 0 and ti.endtime = 0 then 'DO'\n" +
                "                                      end as code,\n" +
                "                                  case\n" +
                "                                      when sr.id is not null then (\n" +
                "                                          case\n" +
                "                                              when cu.lang = 'uz' then coalesce(rl.uzbek, rs.name)\n" +
                "                                              when cu.lang = 'en' then coalesce(rl.english, rs.name)\n" +
                "                                              when cu.lang = 'ru' then coalesce(rl.russian, rs.name)\n" +
                "                                              when cu.lang = 'ar' then coalesce(rl.arabic, rs.name)\n" +
                "                                              end\n" +
                "                                          )\n" +
                "                                      when h.id is not null then h.name\n" +
                "                                      when ss.id is not null then ss.name\n" +
                "                                      when ti.starttime = 0 and ti.endtime = 0 then (\n" +
                "                                          case\n" +
                "                                              when cu.lang = 'uz' then 'Dam kuni'\n" +
                "                                              when cu.lang = 'en' then 'Day Off'\n" +
                "                                              when cu.lang = 'ru' then 'Выходной'\n" +
                "                                              when cu.lang = 'ar' then 'يوم عطلة'\n" +
                "                                              end\n" +
                "                                          )\n" +
                "                                      end as title,\n" +
                "                                  cast(extract(dow from dj.from_date) as int) dow,\n" +
                "                                  ss.id is not null as has_shift\n" +
                "from datejoin dj\n" +
                "left join " + companyId + ".employee e on e.id = " + userId + "\n" +
                "left join " + companyId + ".timeslotitem ti on ti.timeslotid = e.timeslotid and ti.day = cast(extract(dow from dj.from_date) as int)\n" +
                "left join " + companyId + ".sickrequest sr on cast(dj.from_date as date) between cast(sr.startdate as date) and sr.enddate and sr.employeeid = e.id\n" +
                "left join " + companyId + ".leave_reason rs on sr.reason_code = rs.code\n" +
                "left join " + companyId + ".reference_locale rl on rs.localeId = rl.id\n" +
                "left join " + companyId + ".holiday h on dj.from_date between h.date and h.enddate and h.deleted != true and h.dayoff = true\n" +
                "left join " + companyId + ".attendance_hour ah on  to_char(ah.start_date,'yyyy-MM-dd') = to_char(dj.from_date,'yyyy-MM-dd') and ah.employee_id = " + userId + "\n" +
                "left join " + companyId + ".shift_settings ss on ss.id = ah.timeslot_id\n" +
                "left join currentUser cu on cu.userid = " + currentUser.getObjectID() + " where dj.from_date between '" + isoDateFormatter.format(fromDate) + "' and '" + isoDateFormatter.format(toDate) + "'\n" +
                "order by dj.from_date, sr.startdate;";

        List<Object[]> rows = findNative(query);
        return rows.stream()
                .map(row -> {
                    AttendanceStatusDto dto = new AttendanceStatusDto();
                    dto.setDay((Date) row[0]);
                    dto.setCode((String) row[1]);
                    dto.setTitle((String) row[2]);
                    dto.setDow((Integer) row[3]);
                    dto.setHasShift((Boolean) row[4]);
                    return dto;
                })
                .toList();
    }
}
