package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsTimeSlot;
import com.edatasite.workforce.core.domain.EdsTimeSlotItem;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.availability.client.rpc.FingerprintTimeDto;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TimeslotItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSlotItemManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSlotManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.EMPLOYEE_STATUS_RESIGNED;


@Repository("timeSlotManager")
public class TimeSlotManagerImpl extends BaseManager<EdsTimeSlot> implements TimeSlotManager {

    @Autowired
    private RoleManager roleManager;
    @Autowired
    TimeSlotItemManager timeSlotItemManager;

    @Autowired
    private GenericSettingsManager genericSettingsManager;

    public TimeSlotManagerImpl() {
        super(EdsTimeSlot.class);
    }

    public List<EdsTimeSlot> getTimeslots(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select timeslot from EdsTimeSlot timeslot where timeslot.deleted<>true ");
        if (fp != null && fp.getSqlSearchKey() != null) {
            sql.append(" and (lower(timeslot.name) like '");
            sql.append(fp.getSqlSearchKey());
            sql.append("' or ");
            sql.append(" lower(timeslot.description) like '");
            sql.append(fp.getSqlSearchKey());
            sql.append("') ");
        }
        if (fp != null && fp.isLookUp()) {
            sql.append(" and timeslot.shortName is not null ");
        }
        sql.append(" ORDER BY ");
        if (fp != null && TimeslotItem.NAME.equals(fp.getSortField())) {
            sql.append("name");
        } else if (fp != null && TimeslotItem.DESCRIPTION.equals(fp.getSortField())) {
            sql.append("description");
        } else {
            sql.append("name");
        }
        if (fp != null && !fp.isAscending()) {
            sql.append(" DESC ");
        }
        return findByNamedParams(sql.toString(), params);
    }

    public List<Object[]> getTimeslotsForListing(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select t.id, t.name, t.description, t.short_name, false from ").append(getCompanyId()).append(".timeslot t where t.deleted<>true ");
        if (fp != null && fp.getSqlSearchKey() != null) {
            sql.append(" and (lower(t.name) like '");
            sql.append(fp.getSqlSearchKey());
            sql.append("' or ");
            sql.append(" lower(t.description) like '");
            sql.append(fp.getSqlSearchKey());
            sql.append("') ");
        }
        sql.append(" UNION ALL ");
        sql.append("select sh.id, sh.name, sh.description, sh.short_name, true from").append(getCompanyId()).append(".shift_settings sh where sh.deleted<>true ");
        if (fp != null && fp.getSqlSearchKey() != null) {
            sql.append(" and (lower(sh.name) like '");
            sql.append(fp.getSqlSearchKey());
            sql.append("' or ");
            sql.append(" lower(sh.description) like '");
            sql.append(fp.getSqlSearchKey());
            sql.append("') ");
        }
        sql.append(" ORDER BY ");
        if (fp != null && TimeslotItem.NAME.equals(fp.getSortField())) {
            sql.append(" name");
        } else if (fp != null && TimeslotItem.DESCRIPTION.equals(fp.getSortField())) {
            sql.append(" description");
        } else {
            sql.append(" name");
        }
        if (fp != null && !fp.isAscending()) {
            sql.append(" DESC ");
        }
        return findNativeByNamedParams(sql.toString(), params);
    }

    public List<Object[]> getResult(Integer teamID, Integer employeeID, Integer viewAsFilter, Date from, Date to) {
        Boolean isCustomfingerPrint = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.FOR_CUSTOM_FINGER_PRINT);
        if (isCustomfingerPrint) {
            boolean withLunchTime = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ATTENDANCE_LUNCH_AND_COFFEE_TIME);
            return getInUotReportCustom(teamID, null, employeeID, viewAsFilter, from, to, withLunchTime, null, null);
        } else {
            return getInUotReportDefault(teamID, employeeID, viewAsFilter, from, to);
        }
    }

    public List<Object[]> getInUotReportCustom(Integer teamID, Integer locationID, Integer employeeID,
                                               Integer viewAsFilter, Date from, Date to,
                                               boolean withLunchTime, String employeeIds, String departmentIds) {

        EdsUser user = getUser();
        String companyId = getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append(" with fingerprint_raw as ( \n");
        sql.append("   select fp.employee_id as employeeid, \n");
        sql.append("          fp.timeslot_id as timeslotid, \n");
        sql.append("          ss.short_name, \n");
        sql.append("          fp.start_date, \n");
        sql.append("          fp.end_date, \n");
        sql.append("          fp.type, \n");
        sql.append("          case \n");
        sql.append("             when ss.start_time > ss.end_time \n");
        sql.append("                  and (extract(hour from fp.start_date) * 60 + extract(minute from fp.start_date)) < ss.end_time \n");
        sql.append("             then date(fp.start_date - interval '1 day') \n");
        sql.append("             else date(fp.start_date) \n");
        sql.append("          end as fpdate, \n");
        sql.append("          extract(epoch from (fp.end_date - fp.start_date)) / 60 as minutesworked, \n");
        sql.append("         ss.start_time as shift_start,");
        sql.append("         ss.end_time as shift_end, ");
        sql.append("         ss.color as shift_color ");
        sql.append("     from ").append(companyId).append(".attendance_hour fp \n");
        sql.append("     left join ").append(companyId).append(".shift_settings ss on ss.id = fp.timeslot_id \n");
        sql.append(" ), fingerprintdata as ( \n");
        sql.append("   select employeeid, \n");
        sql.append("          timeslotid, \n");
        sql.append("          short_name, \n");
        sql.append("          shift_start, \n");
        sql.append("          shift_end, \n");
        sql.append("          shift_color, \n");
        sql.append("          fpdate, \n");
        sql.append("          sum(case when type != 'OVERTIME' then minutesworked else 0 end) as actualinhours, \n");
        sql.append("          sum(case when type = 'OVERTIME' then minutesworked else 0 end) as overtime, \n");
        sql.append("          max(type) as type \n");
        sql.append("     from fingerprint_raw \n");
        sql.append("    group by employeeid, timeslotid, short_name, fpdate, shift_start, shift_end,shift_color  \n");
        sql.append(" ) \n");
        sql.append(" ,holiday as ( select h.date startdate, h.enddate, h.dayoff, l.id locid from ").append(companyId).append(".holiday h \n");
        sql.append(" left join ").append(companyId).append(".holiday_location hl on h.id = hl.holiday_id \n");
        sql.append(" left join ").append(companyId).append(".location l on hl.locations_id = l.id \n");
        sql.append(" where  h.deleted is not true and l.deleted is not true) \n");
        sql.append(" ,shift_item_data as ( \n");
        sql.append("   select distinct on (te_sid.employeeid, si.key) \n");
        sql.append("          te_sid.employeeid  as employeeid, \n");
        sql.append("          si.key             as sidate, \n");
        sql.append("          ss_sid.short_name, \n");
        sql.append("          ss_sid.id          as timeslotid, \n");
        sql.append("          ss_sid.start_time  as shift_start, \n");
        sql.append("          ss_sid.end_time    as shift_end, \n");
        sql.append("          ss_sid.color       as shift_color \n");
        sql.append("   from ").append(companyId).append(".shift_items si \n");
        sql.append("   join ").append(companyId).append(".shift_settings ss_sid on ss_sid.id = si.shift_settings_id \n");
        sql.append("   join ").append(companyId).append(".shift sh on sh.id = si.shift_id \n");
        sql.append("   join ").append(companyId).append(".brigada_employees be on be.projectid = si.groupid and be.isdeleted is not true \n");
        sql.append("   join ").append(companyId).append(".teamemployee te_sid on te_sid.id = be.employeeDepartmentId and te_sid.isdeleted is not true \n");
        sql.append("   where si.deleted is not true \n");
        sql.append("     and sh.deleted is not true \n");
        sql.append("     and sh.period_type = 'week' \n");
        sql.append(" ) \n");
        sql.append(" SELECT fp.fpdate as start_date, \n");
        sql.append(" fp.fpdate as end_date, \n");
        sql.append(" mu.id AS employeeid, \n");
        sql.append(" to_char(dj.from_date, 'yyyy-MM-dd') dates, \n");
        sql.append(" 20, \n");
        sql.append(" cast('AVAILABLE' as text) statusCode, \n");
        sql.append(" cast(coalesce(fp.actualinhours, 0) as integer) actualinhours, \n");
        sql.append(" tsi.starttime AS startTimeslot, \n");
        sql.append(" tsi.endtime AS endTimeslot, \n");
        sql.append(" coalesce(mu.firstname, '') || ' ' || coalesce(mu.lastname, '') employeename, \n");
        sql.append(" 0 timesheetHours, \n");
        sql.append(" t.id departmentID, \n");
        sql.append(" t.name department, \n");

        if (withLunchTime) {
            sql.append(" case when h.dayoff then 0 else cast(case when tsie.id is not null then tsie.endtime - tsie.starttime  - (tsie.lunchEnd - tsie.lunchStart) - (tsie.coffeeEnd - tsie.coffeeStart) else tsi.endtime - tsi.starttime  - (tsi.lunchEnd - tsi.lunchStart) - (tsi.coffeeEnd - tsi.coffeeStart) end as integer) end timeslot, \n");
        } else {
            sql.append(" case when h.dayoff then 0 else cast(case when tsie.id is not null then tsie.endtime - tsie.starttime else tsi.endtime - tsi.starttime end as integer) end timeslot, \n");
        }
        sql.append(" coalesce(fp.short_name,  sid.short_name)  as short_name, \n");
        sql.append(" coalesce(fp.timeslotid,  sid.timeslotid)  as timeslotid, \n");
        sql.append(" cast(coalesce(fp.overtime, 0) as numeric) overtime, \n");
        sql.append(" fp.type, \n");
        sql.append(" coalesce(fp.shift_start, sid.shift_start) as shift_start, \n");
        sql.append(" coalesce(fp.shift_end,   sid.shift_end)   as shift_end, \n");
        sql.append(" coalesce(fp.shift_color, sid.shift_color) as shift_color \n");
        sql.append(" FROM ").append(companyId).append(".employee e \n");
        sql.append(" JOIN ").append(companyId).append(".myuser mu ON mu.id = e.id \n");
        sql.append(" LEFT JOIN ").append(companyId).append(".teamemployee te ON e.id = te.employeeid AND te.isdeleted IS NOT TRUE \n");
        sql.append(" LEFT JOIN ").append(companyId).append(".team t ON te.teamId = t.id AND t.isdeleted IS NOT TRUE \n");
        if (StringUtils.isNotBlank(departmentIds)) {
            sql.append(" LEFT JOIN ").append(companyId).append(".teamemployee oldte ON e.id = oldte.employeeid AND oldte.enddate IS NOT NULL and oldte.enddate = te.startdate and oldte.enddate != oldte.startdate \n");
            sql.append(" LEFT JOIN ").append(companyId).append(".team oldt ON oldte.teamId = oldt.id AND oldt.isdeleted IS NOT TRUE \n");
        }
        sql.append(" LEFT JOIN ").append(companyId).append(".reference ref ON ref.id = mu.accountstatusid \n");
        sql.append(" LEFT JOIN ").append(companyId).append(".location lc ON mu.locationId = lc.id \n");
        sql.append(" LEFT JOIN ").append(companyId).append(".timeslot ts ON e.timeslotid = ts.id \n");
        sql.append(" join ").append(getPublic()).append(".datejoin dj on (case when e.enddate is not null and e.enddate between '").append(from).append("' and '").append(to).append("' then ");
        sql.append(" dj.from_date between '").append(from).append("' and e.enddate else dj.from_date between '").append(from).append("' and '").append(to).append("' end");
        sql.append(" and case when e.startDate is not null and e.startDate between '").append(from).append("' and '").append(to).append("' then ");
        sql.append(" dj.from_date between e.startDate and '").append(to).append("' else dj.from_date between '").append(from).append("' and '").append(to).append("' end)");
        if (StringUtils.isNotBlank(departmentIds)) {
            sql.append(" and (case when te.startdate between '").append(from).append("' and '").append(to).append("' and not (t.id in (").append(departmentIds).append(") and oldt.id in (").append(departmentIds).append(")) \n");
            sql.append(" then case when t.id in (").append(departmentIds).append(") then dj.from_date between te.startdate and '").append(to).append("' else \n");
            sql.append(" dj.from_date between '").append(from).append("' and (select (oldte.enddate - interval '1 day')) end else true end) \n");
        }
        sql.append(" LEFT JOIN fingerprintdata fp ON fp.employeeid = e.id and date(dj.from_date) = date(fp.fpdate) \n");
        sql.append(" LEFT JOIN shift_item_data sid ON sid.employeeid = e.id AND sid.sidate = to_char(dj.from_date, 'yyyy-MM-dd') \n");
        sql.append(" left join ").append(companyId).append(".timeslotitem tsie on tsie.timeslotid = ts.id and date(tsie.exceptionaldate) = date(dj.from_date) \n");
        sql.append(" LEFT JOIN ").append(companyId).append(".timeslotitem tsi \n");
        sql.append(" ON tsi.timeslotid = ts.id and EXTRACT(dow FROM date(dj.from_date)) = tsi.day and \n");
        sql.append(" tsi.exceptionaldate is null \n");
        sql.append(" left join holiday h on (date(dj.from_date) between date(h.startdate) and date(h.enddate)) and (h.locid is null or mu.locationId = h.locid) \n");
        sql.append(" WHERE (mu.deleted IS NOT TRUE or (mu.deleted is true and ref.code = '").append(EMPLOYEE_STATUS_RESIGNED);
        sql.append("' and e.enddate is not null and e.enddate >= '").append(from).append("')) ");
        if (employeeIds != null && !employeeIds.isEmpty()) {
            sql.append(" and e.id in (").append(employeeIds).append(")");
        }
        if (employeeID != null) {
            sql.append(" and e.id=").append(employeeID).append(" ");
        }
        if (teamID != null) {
            sql.append(" and t.id=").append(teamID).append(" ");
        }
        if (locationID != null) {
            sql.append(" and lc.id=").append(locationID).append(" ");
        }

        if (!ServerUtils.hasPermission(PermissionConstants.HRMS_ATTENDANCE_TRACKING)) {
            if ((viewAsFilter == null || Integer.valueOf(0).equals(viewAsFilter)) && !roleManager.hasEitherRoles(user, EdsRole.DR, EdsRole.ADMIN, EdsRole.CLIENT)) {
                sql.append(" and (t.leaderid=").append(user.getObjectID()).append(" ");
                sql.append(" or mu.id=").append(user.getObjectID()).append(")");
            } else if (EdsRole.TL.equals(viewAsFilter)) {
                sql.append(" and (t.leaderid=").append(user.getObjectID()).append(") ");
            } else if (EdsRole.PM.equals(viewAsFilter)) {
                sql.append(" and (t.leaderid=").append(user.getObjectID()).append(") ");
            } else if (EdsRole.MEM.equals(viewAsFilter)) {
                sql.append(" and mu.id= ").append(user.getObjectID());
            } else if (EdsRole.CLIENT.equals(viewAsFilter) || user.isClientContact()) {
                sql.append(" and mu.id= ").append(user.getClientContact().getClientID());
            }
        }

        sql.append(" ORDER BY dates asc");

        return findNative(sql.toString());
    }

    public Map<Integer, Map<Integer, FingerprintTimeDto>> getFingerprintData(
            Integer departmentId,
            String employeeIds,
            Integer viewAsFilter,
            Date from,
            Date to
    ) {

        boolean fingerPrint = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.FINGERPRINT_DEVICE_ENABLED);
        boolean enableActualInOut = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.TERMINAL_ACTUAL_IN_OUT_ENABLE);
        boolean enabledTimeSlotActual = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.TERMINAL_TIME_SLOT_ACTUAL_ENABLE);
        boolean dailyWorkEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.TERMIANL_DAILY_WORK_ENABLE);

        String timeZoneCurrentUser = fingerPrint ? "GMT+00:00" : getUser().getUserTimezone().getID();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fromStr = sdf.format(from);
        String toStr   = sdf.format(to);

        StringBuilder sql = new StringBuilder();
        if (dailyWorkEnabled) {
            sql.append("WITH extended_base AS ( ")
               .append("  SELECT ufd.userid, fp.id, fp.startdate, fp.statusstring, ")
               .append("         ad.id AS adj_id, ad.description AS adj_desc, ad.source AS adj_source ")
               .append("  FROM ").append(getCompanyId()).append(".fingerprint fp ")
               .append("  JOIN ").append(getCompanyId()).append(".userfingerprintdevice ufd ON fp.fingerprintId = ufd.fingerprint_id AND fp.deviceUUID = ufd.device_id ")
               .append("  LEFT JOIN ").append(getCompanyId()).append(".fingerprint_adjustment ad ON ad.fingerprint_id = fp.id ")
               .append("  WHERE fp.timeslotid IS NULL AND fp.statusstring IN ('IN','OUT') ")
               .append("    AND fp.startdate >= '").append(fromStr).append("' ")
               .append("    AND fp.startdate < CAST('").append(toStr).append("' AS timestamp) + interval '31 days' ");
            if (!ServerUtils.isNullOrEmpty(employeeIds)) {
                sql.append("    AND ufd.userid IN (").append(employeeIds).append(") ");
            }
            sql.append("), ")
               .append("dedup AS MATERIALIZED ( ")
               .append("  SELECT * FROM ( ")
               .append("    SELECT *, ")
               .append("           LAG(startdate) OVER (PARTITION BY userid ORDER BY startdate, id) AS prev_time, ")
               .append("           LAG(statusstring) OVER (PARTITION BY userid ORDER BY startdate, id) AS prev_status ")
               .append("    FROM extended_base ")
               .append("  ) t WHERE prev_time IS NULL ")
               .append("        OR prev_status IS DISTINCT FROM statusstring ")
               .append("        OR startdate - prev_time > interval '1 minute' ")
               .append("), ")
               .append("ordered AS ( ")
               .append("  SELECT *, MAX(startdate) FILTER (WHERE statusstring = 'OUT') ")
               .append("      OVER (PARTITION BY userid ORDER BY startdate, id ROWS BETWEEN UNBOUNDED PRECEDING AND 1 PRECEDING) AS prev_out_time ")
               .append("  FROM dedup ")
               .append("), ")
               .append("paired_in_times AS ( ")
               .append("  SELECT i.userid, i.startdate AS in_time ")
               .append("  FROM ordered o ")
               .append("  LEFT JOIN LATERAL ( ")
               .append("    SELECT i.* FROM ordered i ")
               .append("    WHERE i.userid = o.userid AND i.statusstring = 'IN' ")
               .append("      AND i.startdate > COALESCE(o.prev_out_time, TIMESTAMP '1900-01-01') ")
               .append("      AND i.startdate <= o.startdate ")
               .append("    ORDER BY i.startdate DESC, i.id DESC LIMIT 1 ")
               .append("  ) i ON TRUE ")
               .append("  WHERE o.statusstring = 'OUT' AND i.id IS NOT NULL ")
               .append("), ")
               .append("paired_out AS ( ")
               .append("  SELECT o.userid, i.startdate AS in_time, o.startdate AS out_time, ")
               .append("         i.adj_id AS in_adj_id, i.adj_desc AS in_adj_desc, i.adj_source AS in_adj_source, ")
               .append("         o.adj_id AS out_adj_id, o.adj_desc AS out_adj_desc, o.adj_source AS out_adj_source ")
               .append("  FROM ordered o ")
               .append("  LEFT JOIN LATERAL ( ")
               .append("    SELECT i.* FROM ordered i ")
               .append("    WHERE i.userid = o.userid AND i.statusstring = 'IN' ")
               .append("      AND i.startdate > COALESCE(o.prev_out_time, TIMESTAMP '1900-01-01') ")
               .append("      AND i.startdate <= o.startdate ")
               .append("    ORDER BY i.startdate DESC, i.id DESC LIMIT 1 ")
               .append("  ) i ON TRUE ")
               .append("  WHERE o.statusstring = 'OUT' AND i.id IS NOT NULL ")
               .append("    AND i.startdate >= '").append(fromStr).append("' ")
               .append("    AND i.startdate < '").append(toStr).append("' ")
               .append("  UNION ALL ")
               .append("  SELECT i.userid, i.startdate AS in_time, CAST(NULL AS timestamp) AS out_time, ")
               .append("         i.adj_id AS in_adj_id, i.adj_desc AS in_adj_desc, i.adj_source AS in_adj_source, ")
               .append("         CAST(NULL AS bigint) AS out_adj_id, CAST(NULL AS varchar) AS out_adj_desc, CAST(NULL AS varchar) AS out_adj_source ")
               .append("  FROM dedup i ")
               .append("  WHERE i.statusstring = 'IN' ")
               .append("    AND i.startdate >= '").append(fromStr).append("' ")
               .append("    AND i.startdate < '").append(toStr).append("' ")
               .append("    AND NOT EXISTS ( ")
               .append("      SELECT 1 FROM paired_in_times pit ")
               .append("      WHERE pit.userid = i.userid AND pit.in_time = i.startdate ")
               .append("    ) ")
               .append("), ")
               .append("daily_segments AS ( ")
               .append("  SELECT p.userid, CAST(date_trunc('day', p.in_time) AS date) AS work_date, ")
               .append("         p.in_time AS seg_in, ")
               .append("         p.out_time AS seg_out, ")
               .append("         p.in_adj_id, p.in_adj_desc, p.in_adj_source, ")
               .append("         p.out_adj_id, p.out_adj_desc, p.out_adj_source ")
               .append("  FROM paired_out p ")
               .append("  WHERE CAST(date_trunc('day', p.in_time) AS date) >= DATE('").append(fromStr).append("') ")
               .append("    AND CAST(date_trunc('day', p.in_time) AS date) < DATE('").append(toStr).append("') ")
               .append(") ")
               .append("SELECT ds.userid, ds.work_date AS date, ")
               .append("  CAST(EXTRACT(HOUR FROM MIN(ds.seg_in))*60 + EXTRACT(MINUTE FROM MIN(ds.seg_in)) AS INTEGER) AS intime, ")
               .append("  CAST(EXTRACT(HOUR FROM MAX(ds.seg_out))*60 + EXTRACT(MINUTE FROM MAX(ds.seg_out)) AS INTEGER) AS outtime, ");
            if (enableActualInOut) {
                sql.append("  CAST(SUM(EXTRACT(EPOCH FROM (ds.seg_out - ds.seg_in)) / 60) AS INTEGER) AS actualtime, ");
            } else {
                sql.append("  CAST(EXTRACT(EPOCH FROM (MAX(ds.seg_out) - MIN(ds.seg_in))) / 60 AS INTEGER) AS actualtime, ");
            }
            sql.append("  0 AS precalculatedtime, ")
               .append("  BOOL_OR(ds.in_adj_id IS NOT NULL) AS inAdjust, ")
               .append("  BOOL_OR(ds.out_adj_id IS NOT NULL) AS outAdjust, ")
               .append("  MAX(ds.in_adj_desc) AS inDescription, ")
               .append("  MAX(ds.out_adj_desc) AS outDescription, ")
               .append("  MAX(ds.in_adj_source) AS inSource, ")
               .append("  MAX(ds.out_adj_source) AS outSource ")
               .append("FROM daily_segments ds ")
               .append("GROUP BY ds.userid, ds.work_date ")
               .append("ORDER BY ds.userid, ds.work_date");
        } else if (enabledTimeSlotActual) {

  sql.append("WITH base AS ( ")
         .append("  SELECT ufd.userid, fp.id, DATE(fp.startdate) AS work_date, fp.startdate, fp.statusstring, ad.id AS adj_id, ad.description AS adj_desc, ad.source AS adj_source ")
         .append("  FROM ").append(getCompanyId()).append(".fingerprint fp ")
         .append("  JOIN ").append(getCompanyId()).append(".userfingerprintdevice ufd ON fp.fingerprintId = ufd.fingerprint_id AND fp.deviceUUID = ufd.device_id ")
         .append("  LEFT JOIN ").append(getCompanyId()).append(".fingerprint_adjustment ad ON ad.fingerprint_id = fp.id ")
         .append("  WHERE fp.timeslotid IS NULL AND fp.statusstring IN ('IN','OUT') ")
         .append("    AND fp.startdate >= '").append(fromStr).append("' ")
         .append("    AND fp.startdate <  '").append(toStr).append("' ");
      if (!ServerUtils.isNullOrEmpty(employeeIds)) {
          sql.append("    AND ufd.userid IN (").append(employeeIds).append(") ");
      }
      sql.append("), ")
         .append("dedup AS MATERIALIZED ( ")
         .append("  SELECT * FROM ( ")
         .append("    SELECT *, ")
         .append("           LAG(startdate) OVER (PARTITION BY userid, work_date ORDER BY startdate, id) AS prev_time, ")
         .append("           LAG(statusstring) OVER (PARTITION BY userid, work_date ORDER BY startdate, id) AS prev_status ")
         .append("    FROM base ")
         .append("  ) t WHERE prev_time IS NULL ")
         .append("        OR prev_status IS DISTINCT FROM statusstring ")
         .append("        OR startdate - prev_time > interval '1 minute' ")
         .append("), ")
         .append("valid_pairs AS ( ")
         .append("  SELECT o.userid, o.work_date, i.startdate AS in_time, o.startdate AS out_time, o.id ")
         .append("  FROM dedup o ")
         .append("  JOIN LATERAL ( ")
         .append("    SELECT i.startdate FROM dedup i ")
         .append("    WHERE i.userid = o.userid AND i.work_date = o.work_date ")
         .append("      AND i.statusstring = 'IN' AND i.startdate < o.startdate ")
         .append("      AND NOT EXISTS ( ")
         .append("        SELECT 1 FROM dedup x ")
         .append("        WHERE x.userid = i.userid AND x.work_date = i.work_date ")
         .append("          AND x.statusstring = 'OUT' ")
         .append("          AND x.startdate > i.startdate AND x.startdate < o.startdate ")
         .append("      ) ")
         .append("    ORDER BY i.startdate DESC LIMIT 1 ")
         .append("  ) i ON TRUE ")
         .append("  WHERE o.statusstring = 'OUT' ")
         .append("), ")
         .append("daily_duration AS ( ")
         .append("  SELECT userid, work_date, CAST(SUM(EXTRACT(EPOCH FROM (out_time - in_time))/60) AS INTEGER) AS total_duration_minutes ")
         .append("  FROM valid_pairs GROUP BY userid, work_date ")
         .append("), ")
         .append("daily_bounds AS ( ")
         .append("  SELECT userid, work_date, MIN(CASE WHEN statusstring='IN' THEN startdate END) AS in_time, MAX(CASE WHEN statusstring='OUT' THEN startdate END) AS out_time ")
         .append("  FROM dedup GROUP BY userid, work_date ")
         .append("), ")
         .append("adjustments AS ( ")
         .append("  SELECT userid, work_date, BOOL_OR(adj_id IS NOT NULL AND statusstring='IN') AS inAdjust, BOOL_OR(adj_id IS NOT NULL AND statusstring='OUT') AS outAdjust, ")
         .append("    MAX(CASE WHEN adj_id IS NOT NULL AND statusstring='IN' THEN adj_desc END) AS inDescription, ")
         .append("    MAX(CASE WHEN adj_id IS NOT NULL AND statusstring='OUT' THEN adj_desc END) AS outDescription, ")
         .append("    MAX(CASE WHEN adj_id IS NOT NULL AND statusstring='IN' THEN adj_source END) AS inSource, ")
         .append("    MAX(CASE WHEN adj_id IS NOT NULL AND statusstring='OUT' THEN adj_source END) AS outSource ")
         .append("  FROM dedup GROUP BY userid, work_date ")
         .append("), ")
         .append("timeslot_calc AS ( ")
         .append("  SELECT ")
         .append("    p.userid, ")
         .append("    p.work_date, ")
         .append("    SUM( ")
         .append("      GREATEST( ")
         .append("        0, ")
         .append("        LEAST(EXTRACT(HOUR FROM p.out_time)*60+EXTRACT(MINUTE FROM p.out_time), ti.endtime) ")
         .append("        - ")
         .append("        GREATEST(EXTRACT(HOUR FROM p.in_time)*60+EXTRACT(MINUTE FROM p.in_time), ti.starttime) ")
         .append("      ) ")
         .append("      - ")
         .append("      GREATEST( ")
         .append("        0, ")
         .append("        LEAST(EXTRACT(HOUR FROM p.out_time)*60+EXTRACT(MINUTE FROM p.out_time), ti.lunchEnd) ")
         .append("        - ")
         .append("        GREATEST(EXTRACT(HOUR FROM p.in_time)*60+EXTRACT(MINUTE FROM p.in_time), ti.lunchStart) ")
         .append("      ) ")
         .append("    ) AS timeslotworkedtime, ")
         .append("    ti.id ")
         .append("  FROM valid_pairs p ")
         .append("  JOIN ").append(getCompanyId()).append(".employee e ON e.id = p.userid ")
         .append("  JOIN ").append(getCompanyId()).append(".timeslotitem ti ON ti.timeslotid = e.timeslotid AND ti.day = EXTRACT(DOW FROM p.work_date) AND ti.exceptionaldate IS NULL ")
         .append("  GROUP BY p.userid, p.work_date, ti.id ")
         .append(") ")
         .append("SELECT ")
         .append("  b.userid, ")
         .append("  b.work_date AS date, ")
         .append("  EXTRACT(HOUR FROM b.in_time)*60 + EXTRACT(MINUTE FROM b.in_time) AS intime, ")
         .append("  EXTRACT(HOUR FROM b.out_time)*60 + EXTRACT(MINUTE FROM b.out_time) AS outtime, ")
         .append("  dd.total_duration_minutes AS actualtime, ")
         .append("  0 AS precalculatedtime, ")
         .append("  a.inAdjust, a.outAdjust, a.inDescription, a.outDescription, a.inSource, a.outSource, ")
         .append("  dd.total_duration_minutes, tc.timeslotworkedtime, tc.id ")
         .append("FROM daily_bounds b ")
         .append("LEFT JOIN daily_duration dd ON b.userid = dd.userid AND b.work_date = dd.work_date ")
         .append("LEFT JOIN adjustments a ON b.userid = a.userid AND b.work_date = a.work_date ")
         .append("LEFT JOIN timeslot_calc tc ON b.userid = tc.userid AND b.work_date = tc.work_date ")
         .append("ORDER BY b.userid, b.work_date");
        } else if (!enableActualInOut) {
            String workDateExpr =
                    "CASE " +
                    "WHEN fp.statusstring = 'OUT' AND ts.validoutstart IS NOT NULL AND ts.validoutend IS NOT NULL " +
                    "AND NOT (ts.validoutstart = 0 AND ts.validoutend = 0) " +
                    "AND (EXTRACT(HOUR FROM fp.startdate) * 60 + EXTRACT(MINUTE FROM fp.startdate)) BETWEEN ts.validoutstart AND ts.validoutend " +
                    "THEN DATE(fp.startdate - INTERVAL '1 day') " +
                    "WHEN fp.statusstring = 'IN' AND ts.validinstart IS NOT NULL AND ts.validinend IS NOT NULL " +
                    "AND NOT (ts.validinstart = 0 AND ts.validinend = 0) " +
                    "AND (EXTRACT(HOUR FROM fp.startdate) * 60 + EXTRACT(MINUTE FROM fp.startdate)) BETWEEN ts.validinstart AND ts.validinend " +
                    "THEN DATE(fp.startdate - INTERVAL '1 day') " +
                    "ELSE DATE(fp.startdate) " +
                    "END";
            sql.append("SELECT ")
                    .append("  x.userid, ")
                    .append("  x.date, ")
                    .append("  EXTRACT(HOUR FROM x.in_time) * 60 + EXTRACT(MINUTE FROM x.in_time) AS intime, ")
                    .append("  EXTRACT(HOUR FROM x.out_time) * 60 + EXTRACT(MINUTE FROM x.out_time) AS outtime, ")
                    .append("  GREATEST(0, CAST(EXTRACT(EPOCH FROM (x.out_time - x.in_time)) / 60 AS INTEGER)) AS actualtime, ")
                    .append("  EXTRACT(HOUR FROM x.now_time) * 60 + EXTRACT(MINUTE FROM x.now_time) ")
                    .append("    - (EXTRACT(HOUR FROM x.in_time) * 60 + EXTRACT(MINUTE FROM x.in_time)) AS precalculatedtime, ")
                    .append("  BOOL_OR(x.adj_id IS NOT NULL AND x.statusstring = 'IN'  AND x.startdate = x.in_time)  AS inAdjust, ")
                    .append("  BOOL_OR(x.adj_id IS NOT NULL AND x.statusstring = 'OUT' AND x.startdate = x.out_time) AS outAdjust, ")
                    .append("  MAX(CASE WHEN x.adj_id IS NOT NULL AND x.statusstring = 'IN'  AND x.startdate = x.in_time ")
                    .append("           THEN x.adj_desc END) AS inDescription, ")
                    .append("  MAX(CASE WHEN x.adj_id IS NOT NULL AND x.statusstring = 'OUT' AND x.startdate = x.out_time ")
                    .append("           THEN x.adj_desc END) AS outDescription, ")
                    .append("  MAX(CASE WHEN x.adj_id IS NOT NULL AND x.statusstring = 'IN' AND x.startdate = x.in_time THEN x.adj_source END) AS inSource, ")
                    .append("  MAX(CASE WHEN x.adj_id IS NOT NULL AND x.statusstring = 'OUT' AND x.startdate = x.out_time THEN x.adj_source END) AS outSource ")
                    .append("FROM ( ")
                    .append("  SELECT ")
                    .append("    ufd.userid, ")
                    .append("    ").append(workDateExpr).append(" AS date, ")
                    .append("    fp.statusstring, ")
                    .append("    fp.startdate, ")
                    .append("    MIN(CASE WHEN fp.statusstring='IN'  THEN fp.startdate END) ")
                    .append("      OVER (PARTITION BY ufd.userid, ").append(workDateExpr).append(") AS in_time, ")
                    .append("    MAX(CASE WHEN fp.statusstring='OUT' THEN fp.startdate END) ")
                    .append("      OVER (PARTITION BY ufd.userid, ").append(workDateExpr).append(") AS out_time, ")
                    .append("    ct.time AS now_time, ")
                    .append("    ad.id          AS adj_id, ")
                    .append("    ad.description AS adj_desc, ")
                    .append("    ad.source AS adj_source ")
                    .append("  FROM ").append(getCompanyId()).append(".fingerprint fp ")
                    .append("  JOIN ").append(getCompanyId()).append(".userfingerprintdevice ufd ")
                    .append("    ON fp.fingerprintId = ufd.fingerprint_id ")
                    .append("   AND fp.deviceUUID     = ufd.device_id ")
                    .append("  LEFT JOIN ").append(getCompanyId()).append(".employee e ")
                    .append("    ON e.id = ufd.userid ")
                    .append("  LEFT JOIN ").append(getCompanyId()).append(".timeslot ts ")
                    .append("    ON ts.id = e.timeslotid ")
                    .append("  LEFT JOIN ").append(getCompanyId()).append(".fingerprint_adjustment ad ")
                    .append("    ON ad.fingerprint_id = fp.id ")
                    .append("  CROSS JOIN (SELECT now() AT TIME ZONE '").append(timeZoneCurrentUser).append("' AS time) ct ")
                    .append("  WHERE fp.timeslotid IS NULL ")
                    .append("    AND fp.startdate IS NOT NULL ")
                    .append("    AND fp.statusstring IN ('IN','OUT') ")
                    .append("    AND fp.startdate > '").append(fromStr).append("' ")
                    .append("    AND fp.startdate < '").append(toStr).append("' ");
            if (!ServerUtils.isNullOrEmpty(employeeIds)) {
                sql.append("    AND ufd.userid IN (").append(employeeIds).append(") ");
            }
            sql.append(") AS x ")
                    .append("GROUP BY x.userid, x.date, x.in_time, x.out_time, x.now_time ")
                    .append("ORDER BY x.userid, x.date");

        } else {
            sql.append("WITH base AS ( ")
                    .append("SELECT ufd.userid, fp.id, DATE(fp.startdate) AS work_date, fp.startdate, fp.statusstring, ad.id AS adj_id, ad.description AS adj_desc, ad.source AS adj_source ")
                    .append("FROM ").append(getCompanyId()).append(".fingerprint fp ")
                    .append("JOIN ").append(getCompanyId()).append(".userfingerprintdevice ufd ON fp.fingerprintId = ufd.fingerprint_id AND fp.deviceUUID = ufd.device_id ")
                    .append("LEFT JOIN ").append(getCompanyId()).append(".fingerprint_adjustment ad ON ad.fingerprint_id = fp.id ")
                    .append("WHERE fp.timeslotid IS NULL ")
                    .append("AND fp.statusstring IN ('IN','OUT') ")
                    .append("AND fp.startdate >= '").append(fromStr).append("' ")
                    .append("AND fp.startdate <  '").append(toStr).append("' ");
            if (!ServerUtils.isNullOrEmpty(employeeIds)) {
                sql.append("AND ufd.userid IN (").append(employeeIds).append(") ");
            }
            sql.append("), ")
                    .append("ordered AS ( ")
                    .append("SELECT *, ")
                    .append("LAG(startdate) OVER (PARTITION BY userid, work_date ORDER BY startdate, id) AS prev_time, ")
                    .append("LAG(statusstring) OVER (PARTITION BY userid, work_date ORDER BY startdate, id) AS prev_status ")
                    .append("FROM base ")
                    .append("), ")
                    .append("dedup AS ( ")
                    .append("SELECT * FROM ordered WHERE prev_time IS NULL ")
                    .append("OR prev_status IS DISTINCT FROM statusstring ")
                    .append("OR startdate - prev_time > interval '1 minute' ")
                    .append("), ")
                    .append("paired AS ( ")
                    .append("SELECT o.userid, o.work_date, i.startdate AS in_time, o.startdate AS out_time, CAST(EXTRACT(EPOCH FROM (o.startdate - i.startdate)) AS bigint) AS duration ")                    .append("FROM dedup o ")
                    .append("JOIN LATERAL ( ")
                    .append("SELECT i.* FROM dedup i WHERE i.userid = o.userid AND i.work_date = o.work_date AND i.statusstring = 'IN' AND i.startdate < o.startdate ORDER BY i.startdate DESC LIMIT 1 ")
                    .append(") i ON TRUE ")
                    .append("WHERE o.statusstring = 'OUT' ")
                    .append("), ")
                    .append("daily_duration AS ( ")
                    .append("SELECT userid, work_date, CAST(SUM(duration) / 60 AS INTEGER) AS total_duration_minutes FROM paired GROUP BY userid, work_date ")
                    .append("), ")
                    .append("daily_bounds AS ( ")
                    .append("SELECT userid, work_date, MIN(CASE WHEN statusstring = 'IN' THEN startdate END) AS in_time, MAX(CASE WHEN statusstring = 'OUT' THEN startdate END) AS out_time FROM dedup GROUP BY userid, work_date ")
                    .append("), ")
                    .append("adjustments AS ( ")
                    .append("SELECT userid, work_date, BOOL_OR(adj_id IS NOT NULL AND statusstring = 'IN') AS inAdjust, BOOL_OR(adj_id IS NOT NULL AND statusstring = 'OUT') AS outAdjust, ")
                    .append("MAX(CASE WHEN adj_id IS NOT NULL AND statusstring='IN' THEN adj_desc END) AS inDescription, ")
                    .append("MAX(CASE WHEN adj_id IS NOT NULL AND statusstring='OUT' THEN adj_desc END) AS outDescription, ")
                    .append("MAX(CASE WHEN adj_id IS NOT NULL AND statusstring='IN' THEN adj_source END) AS inSource, ")
                    .append("MAX(CASE WHEN adj_id IS NOT NULL AND statusstring='OUT' THEN adj_source END) AS outSource ")
                    .append("FROM dedup GROUP BY userid, work_date ")
                    .append(") ")
                    .append("SELECT b.userid, b.work_date AS date, ")
                    .append("EXTRACT(HOUR FROM b.in_time)*60 + EXTRACT(MINUTE FROM b.in_time) AS intime, ")
                    .append("EXTRACT(HOUR FROM b.out_time)*60 + EXTRACT(MINUTE FROM b.out_time) AS outtime, ")
                    .append("CAST(EXTRACT(EPOCH FROM (b.out_time - b.in_time + INTERVAL '1 day'))/60 AS INTEGER) % 1440 AS actualtime, ")
                    .append("(EXTRACT(HOUR FROM now() AT TIME ZONE '").append(timeZoneCurrentUser).append("')*60 + EXTRACT(MINUTE FROM now() AT TIME ZONE '").append(timeZoneCurrentUser).append("')) ")
                    .append("- (EXTRACT(HOUR FROM b.in_time)*60 + EXTRACT(MINUTE FROM b.in_time)) AS precalculatedtime, ")
                    .append("a.inAdjust, a.outAdjust, a.inDescription, a.outDescription, a.inSource, a.outSource, dd.total_duration_minutes ")
                    .append("FROM daily_bounds b ")
                    .append("LEFT JOIN daily_duration dd ON b.userid = dd.userid AND b.work_date = dd.work_date ")
                    .append("LEFT JOIN adjustments a ON b.userid = a.userid AND b.work_date = a.work_date ")
                    .append("ORDER BY b.userid, b.work_date");
        }

        List<Object[]> rows = findNative(sql.toString());
        Map<Integer, Map<Integer, FingerprintTimeDto>> result = new HashMap<>();

        for (Object[] row : rows) {

            Integer userId    = ((Number) row[0]).intValue();
            Date date      = (Date) row[1];
            int day        = date.getDate();

            Integer intime = row[2] != null ? ((Number) row[2]).intValue() : null;
            Integer outtime = row[3] != null ? ((Number) row[3]).intValue() : null;
            Integer actualtime = null;
            if (dailyWorkEnabled) {
                actualtime = row[4] != null ? ((Number) row[4]).intValue() : null;
            } else if (enabledTimeSlotActual) {
                EdsTimeSlotItem edsTimeSlot = timeSlotItemManager.get(row[14] != null ? ((Number) row[14]).intValue() : null);
                if (edsTimeSlot != null) {
                    Integer lateMinutes = edsTimeSlot.getTimeSlot().getLateMinutes();
                    Integer earlyLeaveMinutes = edsTimeSlot.getTimeSlot().getEarlyLeaveMinutes();
                    Integer startTime = edsTimeSlot.getStartTime();
                    Integer endTime = edsTimeSlot.getEndTime();

                    actualtime = row[13] != null ? ((Number) row[13]).intValue() : null;
                    if (edsTimeSlot != null && actualtime != null) {
                        if (lateMinutes != 0 && (intime - startTime) <= lateMinutes) {
                            actualtime += Math.max(0, intime - startTime);
                        }

                        if (earlyLeaveMinutes != 0 && outtime + earlyLeaveMinutes < endTime) {
                            actualtime -= endTime - (outtime + earlyLeaveMinutes);
                        }
                    }
                }
            } else if (enableActualInOut) {
                actualtime = row[12] != null ? ((Number) row[12]).intValue() : null;
            } else {
                actualtime = row[4] != null ? ((Number) row[4]).intValue() : null;
            }

                Integer preCalcTime = row[5] != null ? ((Number) row[5]).intValue() : null;
                Boolean isInAdjustment = row[6] != null ? (Boolean) row[6] : null;
                Boolean isOutAdjustment = row[7] != null ? (Boolean) row[7] : null;
                String inDescription = row[8] != null ? (String) row[8] : null;
                String outDescription = row[9] != null ? (String) row[9] : null;
                String inSource = row[10] != null ? (String) row[10] : null;
                String outSource = row[11] != null ? (String) row[11] : null;

                FingerprintTimeDto dto = new FingerprintTimeDto(
                        intime,
                        outtime,
                        actualtime,
                        preCalcTime,
                        isInAdjustment,
                        isOutAdjustment,
                        inDescription,
                        outDescription,
                        inSource,
                        outSource
                );

                Map<Integer, FingerprintTimeDto> dayMap = result.computeIfAbsent(
                        userId,
                        k -> new TreeMap<>()
                );

                dayMap.put(day, dto);
        }

        return result;
    }


    private List<Object[]> getInUotReportDefault(Integer departmentId, Integer employeeId, Integer viewAsFilter, Date from, Date to) {
        EdsUser user = getUser();
        String companyId = getCompanyId();

        boolean areCoffeTimeAndLunchTimeExcluded = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ATTENDANCE_LUNCH_AND_COFFEE_TIME);

        boolean fingerPrint = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.FINGERPRINT_DEVICE_ENABLED);
        String timeZoneCurrentUser = fingerPrint ? "GMT+00:00" : user.getUserTimezone().getID()/*"(SELECT my.timezone FROM " + companyId + ".myuser my WHERE my.id = " + user.getObjectID() + ")"*/;
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT tt2.startdate at time zone '").append(timeZoneCurrentUser).append("' start_date, ");
        sql.append("tt2.enddate at time zone '").append(timeZoneCurrentUser);
        sql.append("' end_date,tmpsummary.*,tt2.id timetrack_id,text(tt2.enddate-tt2.startdate) as difference");
        if (fingerPrint) {
            sql.append(" FROM ").append(companyId).append(".fingerprint tt2");
            sql.append(" join ").append(companyId).append(".userfingerprintdevice fd on fd.fingerprint_id=tt2.fingerprintId  and tt2.deviceuuid=fd.device_id ");
        } else {
            sql.append(" FROM ").append(companyId).append(".timetrack tt2");
        }
        sql.append(" inner join");

        sql.append(" (SELECT");
        if (fingerPrint) {
            sql.append(" mu.id as employeeid,to_char(tt.startdate").append(",'yyyy-MM-dd') as date, ");
        } else {
            sql.append(" mu.id as employeeid,to_char(tt.startdate at time zone '").append(timeZoneCurrentUser).append("','yyyy-MM-dd') as date, ");
        }
        sql.append(" tt.statusid,rf.code,");
        sql.append(" text(sum(date_trunc('minute', tt.enddate)-date_trunc('minute', tt.startdate))");
        if (areCoffeTimeAndLunchTimeExcluded) {
            sql.append("- (cast(TO_CHAR(cast(tsi.lunchend || ' minute' as interval), 'HH24:MI') as time) - cast(TO_CHAR(cast(tsi.lunchstart || 'minute' as interval), 'HH24:MI') as time)) ");
            sql.append("- (cast(TO_CHAR(cast(tsi.coffeeend || ' minute' as interval), 'HH24:MI') as time) - cast(TO_CHAR(cast(tsi.coffeestart || 'minute' as interval), 'HH24:MI') as time))");
        }
        sql.append("),tsi.starttime,tsi.endtime,mu.firstname ||' ' || mu.lastname,");
        sql.append(" tmptsh.timespent,t.id, t.name ");


        if (fingerPrint) {
            sql.append(" FROM ").append(companyId).append(".fingerprint tt ");
            sql.append(" join ").append(companyId).append(".userfingerprintdevice fd on fd.fingerprint_id=tt.fingerprintId  and tt.deviceuuid=fd.device_id ");
        } else {
            sql.append(" FROM ").append(companyId).append(".timetrack tt");
        }
        sql.append(" left outer join ").append(companyId).append(".teamemployee te on (" + (fingerPrint ? "fd.userid" : "tt.employeeid") + "=te.employeeid)");
        sql.append(" left outer join ").append(companyId).append(".team t on (t.id=te.teamid)");
        sql.append(" left outer join ").append(companyId).append(".employee e on (e.id=" + (fingerPrint ? "fd.userid" : "tt.employeeid") + ")");
        sql.append(" left outer join ").append(companyId).append(".myuser mu on (mu.id=e.id)");
        sql.append(" left outer join ").append(companyId).append(".timeslotitem tsi on (tsi.timeslotid=e.timeslotid)");
        sql.append(" left outer join ").append(companyId).append(".reference rf on(rf.id=tt.statusid)");
        sql.append(" left outer join");
        sql.append(" (SELECT");
        sql.append(" tsh.employeeid as employeeid, to_char(tsh.date,'yyyy-MM-dd') as date,");
        sql.append(" sum(tsh.timespent) as timespent");
        sql.append(" FROM ").append(companyId).append(".timesheet tsh");
        sql.append(" left outer join ").append(companyId).append(".myuser mu1 on (mu1.id=tsh.employeeid)");
        sql.append(" left outer join ").append(companyId).append(".team t on (t.id=tsh.teamid)");
        sql.append(" WHERE tsh.timespent >0 and mu1.deleted<>true ");
        if (employeeId != null) {
            sql.append(" and tsh.employeeid=").append(employeeId).append(" ");
        }
        if (departmentId != null) {
            sql.append(" and t.id=").append(departmentId).append(" ");
        }
        sql.append(" and (tsh.date between '").append(from).append("' and '").append(to).append("')");
        sql.append(" GROUP BY tsh.employeeid,to_char(tsh.date,'yyyy-MM-dd')");
        sql.append(" ) tmptsh");
        if (fingerPrint) {
            sql.append(" ON e.id=tmptsh.employeeid and tmptsh.date=to_char(tt.startdate").append(",'yyyy-MM-dd')");
        } else {
            sql.append(" ON e.id=tmptsh.employeeid and tmptsh.date=to_char(tt.startdate at time zone '").append(timeZoneCurrentUser).append("','yyyy-MM-dd')");
        }
        sql.append(" WHERE mu.deleted<>true ");
        if (employeeId != null) {
            sql.append(" and e.id=").append(employeeId).append(" ");
        }
        if (departmentId != null) {
            sql.append(" and t.id=").append(departmentId).append(" ");
        }
        sql.append(" and (tt.startdate between '").append(from).append("' and '").append(to).append("')");
        sql.append(" and te.isdeleted<>true");
        sql.append(" and t.isdeleted<>true");
        if (fingerPrint) {
            sql.append(" and EXTRACT(dow FROM tt.startdate").append(")=tsi.day");
        } else {
            sql.append(" and EXTRACT(dow FROM tt.startdate at time zone '").append(timeZoneCurrentUser).append("')=tsi.day");
        }

        if ((viewAsFilter == null || Integer.valueOf(0).equals(viewAsFilter)) &&
                !roleManager.hasEitherRoles(user, EdsRole.DR, EdsRole.ADMIN, EdsRole.CLIENT)) {
            sql.append(" and (t.leaderid=").append(user.getObjectID()).append(" ");
            sql.append(" or e.id=").append(user.getObjectID()).append(")");
        } else if (EdsRole.TL.equals(viewAsFilter)) {
            sql.append(" and (t.leaderid=").append(user.getObjectID()).append(") ");
        } else if (EdsRole.PM.equals(viewAsFilter)) {
            sql.append(" and (t.leaderid=").append(user.getObjectID()).append(") ");
        } else if (EdsRole.MEM.equals(viewAsFilter)) {
            sql.append(" and e.id= ").append(user.getObjectID());
        } else if (EdsRole.CLIENT.equals(viewAsFilter) || user.isClientContact()) {
            sql.append(" and e.id= ").append(user.getClientContact().getClientID());
        }
        sql.append(" GROUP BY ");
        if (fingerPrint) {
            sql.append(" mu.id, to_char(tt.startdate ").append(",'yyyy-MM-dd'),");
        } else {
            sql.append(" mu.id, to_char(tt.startdate at time zone '").append(timeZoneCurrentUser).append("','yyyy-MM-dd'),");
        }
        sql.append(" tt.statusid,rf.code,tsi.starttime,tsi.endtime,mu.firstname ||' ' || mu.lastname,");
        sql.append(" tmptsh.timespent,t.id, t.name ");
        if (areCoffeTimeAndLunchTimeExcluded) {
            sql.append(", tsi.lunchend, tsi.lunchstart, tsi.coffeeend, tsi.coffeestart ");
        }
        sql.append(" ) tmpsummary ON (");
        if (fingerPrint) {
            sql.append(" fd.userid=tmpsummary.employeeid ");
            sql.append(" and tmpsummary.date=to_char(tt2.startdate ").append(",'yyyy-MM-dd') ");
        } else {
            sql.append(" tt2.employeeid=tmpsummary.employeeid ");
            sql.append(" and tmpsummary.date=to_char(tt2.startdate at time zone '").append(timeZoneCurrentUser).append("','yyyy-MM-dd') ");
        }
        sql.append(" and tt2.statusid=tmpsummary.statusid) ");
        if (fingerPrint) {
            sql.append(" order by tt2.startdate ").append(" asc ");
        } else {
            sql.append(" order by tt2.startdate at time zone '").append(timeZoneCurrentUser).append("' asc ");
        }
        return findNative(sql.toString());
    }

    @Override
    public List<EdsTimeSlot> getTimeslots() {
        return getTimeslots(null);
    }

    public SelectItem[] getTimeslotsAsSelectItem(ListingFilterParameter fp) {
        List<EdsTimeSlot> list = getTimeslots(fp);
        SelectItem[] result = new SelectItem[list.size()];
        String locale = SecurityContext.getInstance().getUserLocale() != null ? SecurityContext.getInstance().getUserLocale().getLanguage() : "";

        for (int i = 0; i < list.size(); i++) {
            Integer id = null;
            String name = null, code = null, description = null;
            boolean selected = false;

            if (result[i] == null) {
                id = list.get(i).getObjectID();
                name = switch (locale) {
                    case "en" -> list.get(i).getLocale() != null && list.get(i).getLocale().getLocaleByCode("en") != null ? list.get(i).getLocale().getLocaleByCode("en") : list.get(i).getName();
                    case "ru" -> list.get(i).getLocale() != null && list.get(i).getLocale().getLocaleByCode("ru") != null ? list.get(i).getLocale().getLocaleByCode("ru") : list.get(i).getName();
                    case "uz" -> list.get(i).getLocale() != null && list.get(i).getLocale().getLocaleByCode("uz") != null ? list.get(i).getLocale().getLocaleByCode("uz") : list.get(i).getName();
                    case "ar" -> list.get(i).getLocale() != null && list.get(i).getLocale().getLocaleByCode("ar") != null ? list.get(i).getLocale().getLocaleByCode("ar") : list.get(i).getName();
                    default -> list.get(i).getName();
                };

                if (id != null) {
                    if (name != null) {
                        if (description != null) {
                            result[i] = new SelectItem(id, name, description, selected);
                        } else {
                            result[i] = new SelectItem(id, name, null, selected);
                        }
                        result[i].setCode(code);
                    } else {
                        result[i] = new SelectItem(id);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public EdsTimeSlot getTimeSlotByShortName(String shortName) {
        return (EdsTimeSlot) findNativeSingle("select * from  " + getCompanyId() + " .timeslot where short_name = '" + shortName + "'", EdsTimeSlot.class);
    }
}
