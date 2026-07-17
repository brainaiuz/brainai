package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserFingerPrintDevice;
import com.edatasite.workforce.core.domain.EdsUsersFingerPrint;
import com.edatasite.workforce.core.domain.EdsUsersFingerPrintAdjustment;
import com.edatasite.workforce.core.domain.GymInOutTO;
import com.edatasite.workforce.core.domain.StaffInOut;
import com.edatasite.workforce.core.domain.enums.FingerprintSource;
import com.edatasite.workforce.gwt.availability.client.rpc.AttendanceMarkListItem;
import com.edatasite.workforce.gwt.availability.client.rpc.InOutPairDto;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.TimeSlotManager;
import com.edatasite.workforce.gwt.core.server.db.UserFingerPrintAdjustmentManager;
import com.edatasite.workforce.gwt.core.server.db.UserFingerPrintDeviceManager;
import com.edatasite.workforce.gwt.core.server.db.UserFingerPrintmanager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.DateUtils;
import com.edatasite.workforce.rest.v3.release10.core.to.InOutReportTO;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.F_EMPLOYEE_ATTENDANCE;

/**
 * User: Djuraev  * Date: 4/12/14
 */
@Repository("userFingerPrintmanager")
public class UserFingerPrintmanagerImpl extends BaseManager<EdsUsersFingerPrint> implements UserFingerPrintmanager {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserFingerPrintmanagerImpl.class);
    private static final SimpleDateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat NC_DATE_FORMAT = new SimpleDateFormat("ddMMyyyy HH:mm:ss");

    /**
     * Converts a "NC" date string ({@code ddMMyyyy HH:mm:ss}) to an ISO date string ({@code yyyy-MM-dd}).
     */
    private static String parseNcDateToIso(String ncDate) {
        try {
            return ISO_DATE_FORMAT.format(NC_DATE_FORMAT.parse(ncDate));
        } catch (Exception e) {
            return null;
        }
    }

    @Autowired
    private UserFingerPrintDeviceManager userFingerPrintDeviceManager;

    @Autowired
    private TimeSlotManager timeSlotManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private UserFingerPrintAdjustmentManager userFingerPrintAdjustmentManager;
    @Autowired
    private DocumentsService documentsService;

    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;


    public UserFingerPrintmanagerImpl() {
        super(EdsUsersFingerPrint.class);
    }

    public EdsUsersFingerPrint getLatestTimeEntry(String fingerprintId, Date checkTime, Boolean isNull) {
        String checkDate = ISO_DATE_FORMAT.format(checkTime);
        List<EdsUsersFingerPrint> list;
        String nNull;
        if (isNull)
            nNull = "null";
        else
            nNull = "not null";
        list = find("select uf from EdsUsersFingerPrint uf where uf.fingerprintId = ? " +
                "and (to_char(uf.startDate,'yyyy-MM-dd')='" + checkDate + "') and (uf.endDate is " + nNull + ") " +
                "order by uf.startDate desc", fingerprintId);
        if (list.size() == 0) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public EdsUsersFingerPrint getLatestTimeEntry(String fingerprintId, Date checkTime, String deviceUUD) {
        return (EdsUsersFingerPrint) findSingle("select uf from EdsUsersFingerPrint uf " +
                "where uf.fingerprintId = ? and uf.startDate='" + checkTime + "' and uf.deviceUUID = '" + deviceUUD + "' ", fingerprintId);
    }

    @Override
    public void deleteEqualsStartDate(Integer employeeID, Date startDate) {
        updateNative("delete from " + getCompanyId() + ".fingerprint  " +
                " where id in (select tt.id from " + getCompanyId() + ".fingerprint tt " +
                " join " + getCompanyId() + ".userfingerprintdevice fd on  fd.fingerprint_id=tt.fingerprintId  and tt.deviceuuid=fd.device_id " +
                " where fd.userId=" + employeeID + " and tt.startDate is not null and to_char(tt.startDate,'yyyy-MM-dd')='" + ISO_DATE_FORMAT.format(startDate) + "') and statusstring != '2' and statusstring != '3'");
    }

    @Override
    public void deleteByShiftId(Integer shiftId) {
        updateNative("delete from " + getCompanyId() + ".fingerprint where shiftId = " + shiftId);
    }

    @Override
    public void deleteRecordBetweenDates(Integer employeeID, Date startDate, Date endDate) {
        updateNative("delete from " + getCompanyId() + ".fingerprint  " +
                " where id in (select tt.id from " + getCompanyId() + ".fingerprint tt " +
                " join " + getCompanyId() + ".userfingerprintdevice fd on  fd.fingerprint_id=tt.fingerprintId  and tt.deviceuuid=fd.device_id " +
                " where fd.userId=" + employeeID + " and tt.startDate is not null and to_char(tt.startDate,'yyyy-MM-dd') between '" + ISO_DATE_FORMAT.format(startDate) + "' and '" + ISO_DATE_FORMAT.format(endDate) + "')");
    }

    @Override
    public DateNonConvertable getFingerPrint(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        if (fp.getStartDateNC() != null) {
            sql.append("select  min(tt.startdate) as startdate from ").append(getCompanyId()).append(".fingerprint tt  ");
            sql.append(" join ").append(getCompanyId()).append(".userfingerprintdevice fd on  fd.fingerprint_id=tt.fingerprintId  and tt.deviceuuid=fd.device_id  ");
            sql.append(" where 1=1  ");
            if (fp.getEmployeeId() != null) {
                sql.append("and fd.userId=").append(fp.getEmployeeId()).append(" ");
            }
            if (fp.getStatusID() != null) {
                sql.append("and tt.statusId= ").append(fp.getStatusID()).append(" ");
            }
            sql.append("and tt.startdate is not null and to_char(tt.startdate,'yyyy-MM-dd')='").append(fp.getStartDateNC()).append("' ");
        } else {
            sql.append("select  max(tt.enddate) as enddate from ").append(getCompanyId()).append(".fingerprint tt  ");
            sql.append(" join ").append(getCompanyId()).append(".userfingerprintdevice fd on  fd.fingerprint_id=tt.fingerprintId  and tt.deviceuuid=fd.device_id  ");
            sql.append(" where 1=1  ");
            if (fp.getEmployeeId() != null) {
                sql.append("and fd.userId=").append(fp.getEmployeeId()).append(" ");
            }
            if (fp.getStatusID() != null) {
                sql.append("and tt.statusId= ").append(fp.getStatusID()).append(" ");
            }
            sql.append("and tt.enddate is not null and to_char(tt.enddate,'yyyy-MM-dd')='").append(fp.getEndDateNC()).append("' ");
        }
        Object object = findNativeSingle(sql.toString());
        if (object == null) {
            return null;
        }
        return new DateNonConvertable((Date) object);
    }

    public List<StaffInOut> getEmployeeTimeTrackInPeriod(Integer employeeId, Date startDate, Date endDate, boolean fingerprintIsCustom) {
        if (fingerprintIsCustom) {
            return getEmployeeFingerPrintDataForCustom(employeeId, startDate);
        }
        return getEmployeeFingerPrintDataForDefault(employeeId, startDate, endDate);
    }

    @Override
    public List<InOutReportTO> getEmployeeAttendanceReport(Integer employeeId, Date startDate, Date endDate) {
        StringBuilder sql = new StringBuilder();
        sql.append("select f.id as employeeId, f.fingerprintid as employeeId, f.startdate as startDate, f.statusstring as status, f.timeslotid as tmeSlotId from ").append(getCompanyId()).append(".fingerprint f ")
                .append(" left join ").append(getCompanyId()).append(".myuser u ").append(" on f.fingerprintid=cast(u.id as varchar) ")
                .append(" where f.fingerprintid='").append(employeeId).append("' and f.startDate is not null ")
                .append(" and to_char(f.startDate,'yyyy-MM-dd')>'").append(ISO_DATE_FORMAT.format(startDate)).append("'")
                .append(" and to_char(f.startDate,'yyyy-MM-dd')<'").append(ISO_DATE_FORMAT.format(endDate)).append("'")
                .append(" and f.statusid = 20 ");
        return (List<InOutReportTO>) findNative(sql.toString(), EdsUsersFingerPrint.class);
    }

    @Override
    public Long countAttendanceDays(Integer employeeId, Date startDate, Date endDate) {
        String sql = """
                select count(distinct to_char(f.startDate, 'yyyy-MM-dd')) as dayCount
                from %s.fingerprint f
                left join %s.myuser u on f.fingerprintid = cast(u.id as varchar)
                where f.fingerprintid = :employeeId
                  and f.startDate is not null
                  and to_char(f.startDate, 'yyyy-MM-dd') >= :startDate
                  and to_char(f.startDate, 'yyyy-MM-dd') < :endDate
                  and f.statusid = 20
                """.formatted(getCompanyId(), getCompanyId());

        Query query = slaveEntityManager.createNativeQuery(sql);
        query.setParameter("employeeId", employeeId.toString());
        query.setParameter("startDate", ISO_DATE_FORMAT.format(startDate));
        query.setParameter("endDate", ISO_DATE_FORMAT.format(endDate));

        Number count = (Number) query.getSingleResult();
        return count != null ? count.longValue() : 0L;
    }

    private List<StaffInOut> getEmployeeFingerPrintDataForDefault(Integer employeeId, Date startDate, Date endDate) {
        DateFormat format = ISO_DATE_FORMAT;
        StringBuilder sql = new StringBuilder();
        sql.append("select tt.*, 0 as clazz_ from ").append(getCompanyId()).append(".fingerprint tt ");
        sql.append(" join ").append(getCompanyId()).append(".userfingerprintdevice fd on  fd.fingerprint_id=tt.fingerprintId  and tt.deviceuuid=fd.device_id ");
        sql.append(" where fd.userId=").append(employeeId).append(" and tt.startDate is not null and tt.endDate is not null ");
        sql.append(" and to_char(tt.startDate,'yyyy-MM-dd')='").append(format.format(startDate)).append("'");
        sql.append(" and to_char(tt.endDate,'yyyy-MM-dd')='").append(format.format(endDate)).append("'");
        sql.append(" and tt.statusid = 20 ");
        return (List<StaffInOut>) findNative(sql.toString(), EdsUsersFingerPrint.class);
    }

    private List<StaffInOut> getEmployeeFingerPrintDataForCustom(Integer employeeId, Date startDate) {

        Calendar endDate = new GregorianCalendar();
        endDate.setTime(startDate);
        endDate.add(Calendar.DATE, 1);
        List<Object[]> values = timeSlotManager.getResult(null, employeeId, null, startDate, endDate.getTime());
        List<StaffInOut> staffInOuts = new ArrayList<>();
        for (Object[] value : values) {
            EdsUsersFingerPrint inOut = new EdsUsersFingerPrint();
            if (value[0] != null) {
                inOut.setStartDate((Date) value[0]);
            }
            if (value[1] != null) {
                inOut.setEndDate((Date) value[1]);
            }
            if (value[15] != null) {
                inOut.setTimeSlotId((Integer) value[15]);
            }
            staffInOuts.add(inOut);
        }
        return staffInOuts;
    }

    @Override
    public void insertTimeTrack(Integer companyID, Integer employeeID, EdsUserFingerPrintDevice userFingerPrintDevice, Date startDate, Date endDate, Integer statusID, boolean isCustomFingerPrint) {
        insertTimeTrack(companyID, employeeID, userFingerPrintDevice, startDate, endDate, statusID, isCustomFingerPrint, null, null, false);
    }

    public void insertTimeTrack(Integer companyID, Integer employeeID, EdsUserFingerPrintDevice userFingerPrintDevice, Date startDate, Date endDate, Integer statusID, boolean isCustomFingerPrint, Integer shiftId, Integer timeSlotId, boolean isOvertime) {
        if (isCustomFingerPrint) {
            insertFingerPrintDataForCustom(companyID, employeeID, startDate, endDate, userFingerPrintDevice, shiftId, timeSlotId, isOvertime);
        } else {
            insertFingerPrintDataForDefault(companyID, employeeID, startDate, endDate, statusID, userFingerPrintDevice, shiftId, timeSlotId);
        }
    }

    private void insertFingerPrintDataForDefault(Integer companyID, Integer employeeID, Date startDate, Date endDate, Integer statusID, EdsUserFingerPrintDevice userFingerPrintDevice, Integer shiftId, Integer timeSlotId) {
        String sql;
        if (userFingerPrintDevice != null && userFingerPrintDevice.getFingerprintId() != null) {
            sql = "INSERT INTO \"" + companyID + "\".fingerprint (employeeId, startDate, endDate, statusid, fingerprintId, deviceUUID, shiftId,timeSlotId) VALUES (" + employeeID + ",'" + startDate + "','" + endDate + "'," + statusID + ",'" + userFingerPrintDevice.getFingerprintId() + "','" + userFingerPrintDevice.getDeviceId() + "'," + shiftId + "'," + timeSlotId + ")";
        } else {
            sql = "INSERT INTO \"" + companyID + "\".fingerprint (employeeId, startDate, endDate, statusid, shiftId,timeSlotId) VALUES (" + employeeID + ",'" + startDate + "','" + endDate + "'," + statusID + "," + shiftId + "," + timeSlotId + ")";
        }
        updateNative(sql);
    }

    private void insertFingerPrintDataForCustom(Integer companyID, Integer employeeID, Date startDate, Date endDate, EdsUserFingerPrintDevice userFingerPrintDevice, Integer shiftId, Integer timeSlotId, boolean isOvertime) {
        String sql;
        if (userFingerPrintDevice == null || userFingerPrintDevice.getFingerprintId() == null) {
            String fakeFPDeviceId = genericSettingsManager.getValueByKey(GenericSettingsEnum.FAKE_FINGERPRINT_DEVICE_ID);
            EdsUser user = userManager.get(employeeID);
            EdsUserFingerPrintDevice edsUserFingerPrintDevice = new EdsUserFingerPrintDevice();
            edsUserFingerPrintDevice.setDeviceId(userFingerPrintDevice == null ? fakeFPDeviceId : userFingerPrintDevice.getDeviceId());
            edsUserFingerPrintDevice.setUser(user);
            if (StringUtils.isNotBlank(user.getEmployee().getProfile().getEmployeeCode())) {
                try {
                    edsUserFingerPrintDevice.setFingerprintId(user.getEmployee().getProfile().getEmployeeCode());
                } catch (Exception e) {
                    edsUserFingerPrintDevice.setFingerprintId(user.getUUID());
                }
            } else {
                edsUserFingerPrintDevice.setFingerprintId(user.getUUID());
            }
            userFingerPrintDeviceManager.create(edsUserFingerPrintDevice);
            userFingerPrintDevice = edsUserFingerPrintDevice;
        }
        if (isOvertime) {
            sql = "INSERT INTO \"" + companyID + "\".fingerprint (employeeId, startDate, endDate, statusString, fingerprintId, deviceUUID, shiftId,timeSlotId) VALUES (" + employeeID + ",'" + startDate + "','" + endDate + "','3','" + userFingerPrintDevice.getFingerprintId() + "','" + userFingerPrintDevice.getDeviceId() + "'," + shiftId + "," + timeSlotId + ");";
        } else if (DateUtils.areOnTheSameDay(startDate, endDate)) {
            sql = "INSERT INTO \"" + companyID + "\".fingerprint (employeeId, startDate, statusString, fingerprintId, deviceUUID, shiftId,timeSlotId) VALUES (" + employeeID + ",'" + startDate + "','0','" + userFingerPrintDevice.getFingerprintId() + "','" + userFingerPrintDevice.getDeviceId() + "'," + shiftId + "," + timeSlotId + ");";
            sql = sql + "INSERT INTO \"" + companyID + "\".fingerprint (employeeId, startDate, statusString, fingerprintId, deviceUUID, shiftId,timeSlotId) VALUES (" + employeeID + ",'" + endDate + "','1','" + userFingerPrintDevice.getFingerprintId() + "','" + userFingerPrintDevice.getDeviceId() + "'," + shiftId + "," + timeSlotId + ");";
        } else {
            Calendar start = new GregorianCalendar();
            start.setTime(startDate);
            Calendar end = new GregorianCalendar();
            end.setTime(endDate);
            ServerUtils.setEndOfTheDay(start);
            ServerUtils.setBeginningOfTheDay(end);
            sql = "INSERT INTO \"" + companyID + "\".fingerprint (employeeId, startDate, endDate, statusString, fingerprintId, deviceUUID, shiftId,timeSlotId) VALUES (" + employeeID + ",'" + startDate + "','" + start.getTime() + "','2','" + userFingerPrintDevice.getFingerprintId() + "','" + userFingerPrintDevice.getDeviceId() + "'," + shiftId + "," + timeSlotId + ");";
            sql = sql + "INSERT INTO \"" + companyID + "\".fingerprint (employeeId, startDate, endDate, statusString, fingerprintId, deviceUUID, shiftId,timeSlotId) VALUES (" + employeeID + ",'" + end.getTime() + "','" + endDate + "','2','" + userFingerPrintDevice.getFingerprintId() + "','" + userFingerPrintDevice.getDeviceId() + "'," + shiftId + "," + timeSlotId + ");";
        }
        updateNative(sql);
    }

    public List<GymInOutTO> getUserGymInOutRecords(Integer userId) {
        String hql = """
                SELECT new com.edatasite.workforce.core.domain.GymInOutTO(fp.fingerprintId, MIN(fp.startDate), MAX(fpe.startDate))
                FROM EdsUsersFingerPrint fp
                LEFT JOIN EdsUsersFingerPrint fpe ON fp.fingerprintId = fpe.fingerprintId
                    AND function('date', fp.startDate) = function('date', fpe.startDate)
                WHERE fp.fingerprintId = :userId
                    AND fpe.statusString = 'OUT'
                GROUP BY fp.fingerprintId, function('date', fp.startDate)
                HAVING MIN(fp.startDate) IS NOT NULL
                ORDER BY function('date', fp.startDate) DESC
                """;

        return masterEntityManager.createQuery(hql, GymInOutTO.class)
                .setParameter("userId", userId.toString())
                .getResultList();
    }

    @Override
    public List<InOutPairDto> getUserInOutPairs(Integer userId, Date date) {
        return getUserInOutPairs(userId, date, date);
    }

    @Override
    public Integer[] getLateAndEarlyCount(Date startDate, Date endDate, Integer employeeID) {
        StringBuilder sql = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone(userManager.getUser().getTimezone() != null ? userManager.getUser().getTimezone() : "+0"));
        String fromStr = sdf.format(startDate);
        String toStr = sdf.format(endDate);

        sql.append("  SELECT COUNT(CASE WHEN x.in_time > (x.scheduled_start + x.late_minutes) THEN 1 END) AS late_count,");
        sql.append("  COUNT(CASE WHEN x.out_time < (x.scheduled_end - x.early_leave_minutes) THEN 1 END) AS early_count");
        sql.append("  FROM (   SELECT  ufd.userid,DATE(fp.startdate) AS date,");
        sql.append("  EXTRACT(HOUR FROM MIN(CASE WHEN fp.statusstring = 'IN' THEN fp.startdate END)) * 60 + ");
        sql.append("  EXTRACT(MINUTE FROM MIN(CASE WHEN fp.statusstring = 'IN' THEN fp.startdate END)) AS in_time,");
        sql.append("  EXTRACT(HOUR FROM MAX(CASE WHEN fp.statusstring = 'OUT' THEN fp.startdate END)) * 60 +");
        sql.append("  EXTRACT(MINUTE FROM MAX(CASE WHEN fp.statusstring = 'OUT' THEN fp.startdate END)) AS out_time,");
        sql.append("  ti.startTime AS scheduled_start,");
        sql.append("  ti.endTime AS scheduled_end,");
        sql.append("  COALESCE(t.late_minutes, 0)        AS late_minutes,");
        sql.append("  COALESCE(t.early_leave_minutes, 0) AS early_leave_minutes");
        sql.append("  FROM " + getCompanyId() + ".fingerprint fp");
        sql.append("  JOIN " + getCompanyId() + ".userfingerprintdevice ufd ON fp.fingerprintId = ufd.fingerprint_id AND fp.deviceUUID = ufd.device_id");
        sql.append("  LEFT JOIN " + getCompanyId() + ".employee e ON e.id = ufd.userid ");
        sql.append("  LEFT JOIN " + getCompanyId() + ".timeslot t ON t.id = e.timeslotid ");
        sql.append("  LEFT JOIN " + getCompanyId() + ".timeslotitem ti ON ti.timeslotid = t.id  ");
        sql.append("  AND (ti.exceptionalDate = DATE(fp.startdate) OR (ti.exceptionalDate IS NULL AND ti.day = EXTRACT(DOW FROM fp.startdate)))");
        sql.append("  LEFT JOIN " + getCompanyId() + ".attendancerawdata ar");
        sql.append("  ON ar.employeeId = ufd.userid");
        sql.append("  AND DATE(fp.startdate) = ar.date");
        sql.append("  AND ar.dayOff IS NOT TRUE");
        sql.append("  AND ar.holiday IS NOT TRUE");
        sql.append("  WHERE fp.startdate >= '" + fromStr + "' ");
        sql.append("  AND fp.startdate <= '" + toStr + "' ");
        sql.append("  AND ufd.userid = " + employeeID);
        sql.append("  AND fp.statusstring IN ('IN', 'OUT')");
        sql.append("  AND ti.startTime != 0 ");
        sql.append("  AND ti.endTime != 0 ");
        sql.append("  GROUP BY");
        sql.append("  ufd.userid, DATE(fp.startdate), ti.startTime, ti.endTime,t.late_minutes,t.early_leave_minutes");
        sql.append("  ) AS x");
        sql.append("  WHERE NOT (x.scheduled_start = 0 AND x.scheduled_end = 0);");

        List<Object> result = (List<Object>) findNative(sql.toString());
        Integer lateCount = 0;
        Integer earlyCount = 0;
        if (result != null && !result.isEmpty() && result.get(0) instanceof Object[]) {
            Object[] row = (Object[]) result.get(0);
            lateCount = row[0] != null ? ((Number) row[0]).intValue() : 0;
            earlyCount = row[1] != null ? ((Number) row[1]).intValue() : 0;
        }
        return new Integer[]{lateCount, earlyCount};
    }

    @Override
    public Map<Integer, Integer[]> getLateAndEarlyCountBatch(Date startDate, Date endDate, Set<Integer> employeeIds) {
        Map<Integer, Integer[]> result = new HashMap<>();
        if (employeeIds == null || employeeIds.isEmpty()) return result;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone(userManager.getUser().getTimezone() != null ? userManager.getUser().getTimezone() : "+0"));
        String fromStr = sdf.format(startDate);
        String toStr = sdf.format(endDate);
        String ids = employeeIds.stream().map(Object::toString).collect(Collectors.joining(","));
        StringBuilder sql = new StringBuilder();
        sql.append("  SELECT x.userid,");
        sql.append("  COUNT(CASE WHEN x.in_time > (x.scheduled_start + x.late_minutes) THEN 1 END) AS late_count,");
        sql.append("  COUNT(CASE WHEN x.out_time < (x.scheduled_end - x.early_leave_minutes) THEN 1 END) AS early_count");
        sql.append("  FROM (   SELECT  ufd.userid,DATE(fp.startdate) AS date,");
        sql.append("  EXTRACT(HOUR FROM MIN(CASE WHEN fp.statusstring = 'IN' THEN fp.startdate END)) * 60 + ");
        sql.append("  EXTRACT(MINUTE FROM MIN(CASE WHEN fp.statusstring = 'IN' THEN fp.startdate END)) AS in_time,");
        sql.append("  EXTRACT(HOUR FROM MAX(CASE WHEN fp.statusstring = 'OUT' THEN fp.startdate END)) * 60 +");
        sql.append("  EXTRACT(MINUTE FROM MAX(CASE WHEN fp.statusstring = 'OUT' THEN fp.startdate END)) AS out_time,");
        sql.append("  ti.startTime AS scheduled_start,");
        sql.append("  ti.endTime AS scheduled_end,");
        sql.append("  COALESCE(t.late_minutes, 0)        AS late_minutes,");
        sql.append("  COALESCE(t.early_leave_minutes, 0) AS early_leave_minutes");
        sql.append("  FROM " + getCompanyId() + ".fingerprint fp");
        sql.append("  JOIN " + getCompanyId() + ".userfingerprintdevice ufd ON fp.fingerprintId = ufd.fingerprint_id AND fp.deviceUUID = ufd.device_id");
        sql.append("  LEFT JOIN " + getCompanyId() + ".employee e ON e.id = ufd.userid ");
        sql.append("  LEFT JOIN " + getCompanyId() + ".timeslot t ON t.id = e.timeslotid ");
        sql.append("  LEFT JOIN " + getCompanyId() + ".timeslotitem ti ON ti.timeslotid = t.id  ");
        sql.append("  AND (ti.exceptionalDate = DATE(fp.startdate) OR (ti.exceptionalDate IS NULL AND ti.day = EXTRACT(DOW FROM fp.startdate)))");
        sql.append("  LEFT JOIN " + getCompanyId() + ".attendancerawdata ar");
        sql.append("  ON ar.employeeId = ufd.userid");
        sql.append("  AND DATE(fp.startdate) = ar.date");
        sql.append("  AND ar.dayOff IS NOT TRUE");
        sql.append("  AND ar.holiday IS NOT TRUE");
        sql.append("  WHERE fp.startdate >= '" + fromStr + "' ");
        sql.append("  AND fp.startdate <= '" + toStr + "' ");
        sql.append("  AND ufd.userid IN (" + ids + ")");
        sql.append("  AND fp.statusstring IN ('IN', 'OUT')");
        sql.append("  AND ti.startTime != 0 ");
        sql.append("  AND ti.endTime != 0 ");
        sql.append("  GROUP BY");
        sql.append("  ufd.userid, DATE(fp.startdate), ti.startTime, ti.endTime,t.late_minutes,t.early_leave_minutes");
        sql.append("  ) AS x");
        sql.append("  WHERE NOT (x.scheduled_start = 0 AND x.scheduled_end = 0)");
        sql.append("  GROUP BY x.userid");
        List<Object> rows = (List<Object>) findNative(sql.toString());
        if (rows != null) {
            for (Object row : rows) {
                Object[] r = (Object[]) row;
                Integer uid = r[0] != null ? ((Number) r[0]).intValue() : null;
                int lateCount = r[1] != null ? ((Number) r[1]).intValue() : 0;
                int earlyCount = r[2] != null ? ((Number) r[2]).intValue() : 0;
                if (uid != null) result.put(uid, new Integer[]{lateCount, earlyCount});
            }
        }
        return result;
    }

    @Override
    public Map<Integer, Integer> getUserDailySummaryCountBatch(Date fromDate, Date toDate, Set<Integer> employeeIds) {
        Map<Integer, Integer> result = new HashMap<>();
        if (employeeIds == null || employeeIds.isEmpty()) return result;
        String ids = employeeIds.stream().map(Object::toString).collect(Collectors.joining(","));
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String fromStr = df.format(fromDate);
        String toStr = df.format(toDate);
        String sql = "SELECT d.userid, COUNT(DISTINCT DATE(f.startdate)) AS worked_days" +
                " FROM " + getCompanyId() + ".fingerprint f" +
                " JOIN " + getCompanyId() + ".userfingerprintdevice d ON d.fingerprint_id = f.fingerprintid AND d.device_id = f.deviceuuid" +
                " WHERE d.userid IN (" + ids + ")" +
                " AND f.startdate >= '" + fromStr + "'" +
                " AND f.startdate < CAST('" + toStr + "' AS date) + INTERVAL '1 day'" +
                " AND f.statusstring = 'IN'" +
                " GROUP BY d.userid";
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
    public List<InOutPairDto> getUserInOutPairs(Integer userId, Date fromDate, Date toDate) {
        boolean terminalWorkDayEnable = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.TERMIANL_DAILY_WORK_ENABLE);
        String queryStr = terminalWorkDayEnable
                ? userInOutPairsDailySplitQuery(userId, ISO_DATE_FORMAT.format(fromDate), ISO_DATE_FORMAT.format(toDate))
                : userInOutPairsQuery(userId, ISO_DATE_FORMAT.format(fromDate), ISO_DATE_FORMAT.format(toDate));
        List<Object[]> results = findNative(queryStr);
        return mapInOutPair(results);
    }

    @Override
    public List<InOutPairDto> getUserInOutPairsForApi(Integer userId, Date fromDate, Date toDate) {
        boolean terminalWorkDayEnable = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.TERMIANL_DAILY_WORK_ENABLE);
        if (terminalWorkDayEnable) {
            String cte = userInOutPairsDailySplitQuery(userId, ISO_DATE_FORMAT.format(fromDate), ISO_DATE_FORMAT.format(toDate)).trim();
            if (cte.endsWith(";")) cte = cte.substring(0, cte.length() - 1);
            String sql = format("""
                    with pairs_data as (
                        ${cte}
                    )
                    select p.in_id,
                           p.out_id,
                           p.in_time,
                           p.out_time,
                           case when p.duration is null then 0 else p.duration end as duration,
                           p.in_devicename,
                           p.out_devicename,
                           p.pair_status,
                           cast(case when p.duration is null then 0 else p.duration end as bigint) as durationPerDay
                    from pairs_data p""", Map.of("cte", cte, "companyId", getCompanyId()));
            List<Object[]> results = findNative(sql);
            return mapInOutPair(results);
        }

        String cte = userInOutPairsQuery(userId, ISO_DATE_FORMAT.format(fromDate), ISO_DATE_FORMAT.format(toDate)).trim();
        if (cte.endsWith(";")) cte = cte.substring(0, cte.length() - 1);
        String sql = format("""
                with pairs_data as (
                    ${cte}
                ),
                daily_bounds as (
                    select coalesce(date(in_time), date(out_time)) as work_date,
                           min(in_time) as earliest_in,
                           max(out_time) as latest_out
                    from pairs_data
                    group by coalesce(date(in_time), date(out_time))
                )
                select p.in_id,
                       p.out_id,
                       p.in_time,
                       p.out_time,
                       case
                          when p.duration is null then 0
                          else p.duration
                      end as duration,
                       p.in_devicename,
                       p.out_devicename,
                       p.pair_status,
                      cast(case
                        when gs.value = 'YES' and ti.id is not null
                        then greatest(0,
                            cast(extract(EPOCH from (
                                least(db.latest_out, date_trunc('day', db.latest_out) + cast(ti.endtime || ' minutes' as interval))
                                -
                                greatest(db.earliest_in, date_trunc('day', db.earliest_in) + cast(ti.starttime || ' minutes' as interval))
                            )) as bigint)
                            -
                            case
                                when (date_trunc('day', db.earliest_in) + cast(ti.lunchstart || ' minutes' as interval))
                                     < least(db.latest_out, date_trunc('day', db.latest_out) + cast(ti.endtime || ' minutes' as interval))
                                and (date_trunc('day', db.earliest_in) + cast(ti.lunchend || ' minutes' as interval))
                                    > greatest(db.earliest_in, date_trunc('day', db.earliest_in) + cast(ti.starttime || ' minutes' as interval))
                                then (ti.lunchend - ti.lunchstart) * 60
                                else 0
                            end
                        )
                        else sum(case when p.duration is not null then p.duration else 0 end)
                            over (partition by coalesce(date(p.in_time), date(p.out_time)))
                    end as bigint) as durationPerDay
                from pairs_data p
                         left join daily_bounds db on coalesce(date(p.in_time), date(p.out_time)) = db.work_date
                         left join ${companyId}.genericSettings gs on gs.key = 'TERMINAL_TIME_SLOT_ACTUAL_ENABLE'
                         left join ${companyId}.employee e on e.id = ${userId}
                         left join ${companyId}.timeslotitem ti on ti.timeslotid = e.timeslotid and ti.day = extract(dow from p.in_time)""", Map.of("companyId", getCompanyId(), "cte", cte, "userId", userId));
        List<Object[]> results = findNative(sql);
        return mapInOutPair(results);
    }

    private String userInOutPairsQuery(Integer userId, String fromDate, String toDate) {
        StringBuilder sql = new StringBuilder();
        sql.append("WITH logs AS (")
                .append("  SELECT")
                .append("    f.id,")
                .append("    f.fingerprintid,")
                .append("    f.startdate,")
                .append("    f.statusstring,")
                .append("    f.devicename")
                .append("  FROM ").append(getCompanyId()).append(".fingerprint f")
                .append("  LEFT JOIN ").append(getCompanyId()).append(".userfingerprintdevice d ON d.fingerprint_id = f.fingerprintid")
                .append("   AND d.device_id = f.deviceuuid")
                .append("  WHERE d.userid = ").append(userId)
                .append("    AND f.startdate >= '").append(fromDate).append("'")
                .append("    AND f.startdate < CAST('").append(toDate).append("' AS date) + interval '1 day'")
                .append(" ),")
                .append(" dedup AS (")
                .append("  SELECT")
                .append("    t.id,")
                .append("    t.fingerprintid,")
                .append("    t.startdate,")
                .append("    t.statusstring,")
                .append("    t.devicename")
                .append("  FROM (")
                .append("    SELECT")
                .append("      l.*,")
                .append("      LAG(startdate) OVER (")
                .append("        ORDER BY startdate, id")
                .append("      ) AS prev_time,")
                .append("      LAG(statusstring) OVER (")
                .append("        ORDER BY startdate, id")
                .append("      ) AS prev_status")
                .append("    FROM logs l")
                .append("  ) t")
                .append("  WHERE prev_time IS NULL")
                .append("     OR prev_status IS DISTINCT FROM statusstring")
                .append("     OR startdate - prev_time > interval '1 minutes'")
                .append(" ),")
                .append(" ordered AS (")
                .append("  SELECT")
                .append("    *,")
                .append("    MAX(startdate) FILTER (WHERE statusstring = 'OUT')")
                .append("      OVER (")
                .append("        ORDER BY startdate, id")
                .append("        ROWS BETWEEN UNBOUNDED PRECEDING AND 1 PRECEDING")
                .append("      ) AS prev_out_time")
                .append("  FROM dedup")
                .append(" ),")
                .append(" paired_out AS (")
                .append("  SELECT")
                .append("    o.startdate AS sort_time,")
                .append("    i.id        AS in_id,")
                .append("    o.id        AS out_id,")
                .append("    i.startdate AS in_time,")
                .append("    o.startdate AS out_time,")
                .append("    CASE")
                .append("      WHEN i.id IS NULL THEN CAST(NULL AS bigint)")
                .append("      ELSE CAST(EXTRACT(EPOCH FROM (o.startdate - i.startdate)) AS bigint)")
                .append("    END AS duration,")
                .append("    i.devicename AS in_devicename,")
                .append("    o.devicename AS out_devicename,")
                .append("    CASE")
                .append("      WHEN i.id IS NULL THEN 'MISSING IN'")
                .append("      ELSE 'OK'")
                .append("    END AS pair_status")
                .append("  FROM ordered o")
                .append("  LEFT JOIN LATERAL (")
                .append("    SELECT i.*")
                .append("    FROM ordered i")
                .append("    WHERE i.statusstring = 'IN'")
                .append("      AND i.startdate > COALESCE(o.prev_out_time, TIMESTAMP '1900-01-01')")
                .append("      AND i.startdate <= o.startdate")
                .append("    ORDER BY i.startdate DESC, i.id DESC")
                .append("    LIMIT 1")
                .append("  ) i ON TRUE")
                .append("  WHERE o.statusstring = 'OUT'")
                .append(" ),")
                .append(" used_in AS (")
                .append("  SELECT DISTINCT in_id")
                .append("  FROM paired_out")
                .append("  WHERE in_id IS NOT NULL")
                .append(" ),")
                .append(" extra_in AS (")
                .append("  SELECT")
                .append("    i.startdate AS sort_time,")
                .append("    i.id AS in_id,")
                .append("    CAST(NULL AS bigint) AS out_id,")
                .append("    i.startdate AS in_time,")
                .append("    CAST(NULL AS timestamp) AS out_time,")
                .append("    CAST(NULL AS bigint) AS duration,")
                .append("    i.devicename AS in_devicename,")
                .append("    CAST(NULL AS text) AS out_devicename,")
                .append("    'MISSING OUT' AS pair_status")
                .append("  FROM ordered i")
                .append("  WHERE i.statusstring = 'IN'")
                .append("    AND NOT EXISTS (")
                .append("      SELECT 1")
                .append("      FROM used_in u")
                .append("      WHERE u.in_id = i.id")
                .append("    )")
                .append(" )")
                .append(" SELECT")
                .append("  in_id,")
                .append("  out_id,")
                .append("  in_time,")
                .append("  out_time,")
                .append("  duration,")
                .append("  in_devicename,")
                .append("  out_devicename,")
                .append("  pair_status")
                .append(" FROM (")
                .append("  SELECT")
                .append("    in_id, out_id, in_time, out_time, duration,")
                .append("    in_devicename, out_devicename, pair_status,")
                .append("    sort_time")
                .append("  FROM paired_out")
                .append("  UNION ALL")
                .append("  SELECT")
                .append("    in_id, out_id, in_time, out_time, duration,")
                .append("    in_devicename, out_devicename, pair_status,")
                .append("    sort_time")
                .append("  FROM extra_in")
                .append(" ) x")
                .append(" ORDER BY sort_time;");
        return sql.toString();
    }

    private String userInOutPairsDailySplitQuery(Integer userId, String fromDate, String toDate) {
        StringBuilder sql = new StringBuilder();
        sql.append("WITH logs AS (")
                .append("  SELECT f.id, f.fingerprintid, f.startdate, f.statusstring, f.devicename")
                .append("  FROM ").append(getCompanyId()).append(".fingerprint f")
                .append("  LEFT JOIN ").append(getCompanyId()).append(".userfingerprintdevice d ON d.fingerprint_id = f.fingerprintid AND d.device_id = f.deviceuuid")
                .append("  WHERE d.userid = ").append(userId)
                .append("    AND f.startdate >= '").append(fromDate).append("'")
                .append("    AND f.startdate < CAST('").append(toDate).append("' AS date) + interval '31 days'")
                .append(" ),")
                .append(" dedup AS (")
                .append("  SELECT t.id, t.fingerprintid, t.startdate, t.statusstring, t.devicename")
                .append("  FROM (")
                .append("    SELECT l.*, LAG(startdate) OVER (ORDER BY startdate, id) AS prev_time,")
                .append("      LAG(statusstring) OVER (ORDER BY startdate, id) AS prev_status")
                .append("    FROM logs l")
                .append("  ) t")
                .append("  WHERE prev_time IS NULL OR prev_status IS DISTINCT FROM statusstring OR startdate - prev_time > interval '1 minutes'")
                .append(" ),")
                .append(" ordered AS (")
                .append("  SELECT *, MAX(startdate) FILTER (WHERE statusstring = 'OUT') OVER (ORDER BY startdate, id ROWS BETWEEN UNBOUNDED PRECEDING AND 1 PRECEDING) AS prev_out_time")
                .append("  FROM dedup")
                .append(" ),")
                .append(" paired_out AS (")
                .append("  SELECT o.startdate AS sort_time, i.id AS in_id, o.id AS out_id, i.startdate AS in_time, o.startdate AS out_time,")
                .append("    CASE WHEN i.id IS NULL THEN CAST(NULL AS bigint) ELSE CAST(EXTRACT(EPOCH FROM (o.startdate - i.startdate)) AS bigint) END AS duration,")
                .append("    i.devicename AS in_devicename, o.devicename AS out_devicename,")
                .append("    CASE WHEN i.id IS NULL THEN 'MISSING IN' ELSE 'OK' END AS pair_status")
                .append("  FROM ordered o")
                .append("  LEFT JOIN LATERAL (")
                .append("    SELECT i.* FROM ordered i WHERE i.statusstring = 'IN'")
                .append("      AND i.startdate > COALESCE(o.prev_out_time, TIMESTAMP '1900-01-01')")
                .append("      AND i.startdate <= o.startdate")
                .append("    ORDER BY i.startdate DESC, i.id DESC LIMIT 1")
                .append("  ) i ON TRUE")
                .append("  WHERE o.statusstring = 'OUT'")
                .append(" ),")
                .append(" used_in AS (SELECT DISTINCT in_id FROM paired_out WHERE in_id IS NOT NULL),")
                .append(" extra_in AS (")
                .append("  SELECT i.startdate AS sort_time, i.id AS in_id, CAST(NULL AS bigint) AS out_id, i.startdate AS in_time,")
                .append("    CAST(NULL AS timestamp) AS out_time, CAST(NULL AS bigint) AS duration,")
                .append("    i.devicename AS in_devicename, CAST(NULL AS text) AS out_devicename, 'MISSING OUT' AS pair_status")
                .append("  FROM ordered i WHERE i.statusstring = 'IN'")
                .append("    AND NOT EXISTS (SELECT 1 FROM used_in u WHERE u.in_id = i.id)")
                .append(" ),")
                .append(" all_pairs AS (")
                .append("  SELECT in_id, out_id, in_time, out_time, duration, in_devicename, out_devicename, pair_status, sort_time FROM paired_out")
                .append("  UNION ALL")
                .append("  SELECT in_id, out_id, in_time, out_time, duration, in_devicename, out_devicename, pair_status, sort_time FROM extra_in")
                .append(" ),")
                .append(" daily_split AS (")
                .append("  SELECT")
                .append("    CASE WHEN day_s = date_trunc('day', ap.in_time) THEN ap.in_id ELSE NULL END AS in_id,")
                .append("    CASE WHEN ap.out_time IS NOT NULL AND day_s = date_trunc('day', ap.out_time) THEN ap.out_id ELSE NULL END AS out_id,")
                .append("    greatest(ap.in_time, day_s) AS in_time,")
                .append("    CASE WHEN ap.out_time IS NOT NULL THEN least(ap.out_time, day_s + interval '1 day' - interval '1 second') ELSE NULL END AS out_time,")
                .append("    CASE WHEN ap.out_time IS NOT NULL")
                .append("      THEN CAST(EXTRACT(EPOCH FROM (least(ap.out_time, day_s + interval '1 day' - interval '1 second') - greatest(ap.in_time, day_s))) AS bigint)")
                .append("      ELSE NULL END AS duration,")
                .append("    ap.in_devicename, ap.out_devicename,")
                .append("    'OK' AS pair_status,")
                .append("    greatest(ap.in_time, day_s) AS sort_time")
                .append("  FROM all_pairs ap")
                .append("  CROSS JOIN LATERAL generate_series(")
                .append("    date_trunc('day', ap.in_time),")
                .append("    COALESCE(date_trunc('day', ap.out_time), date_trunc('day', ap.in_time)),")
                .append("    interval '1 day'")
                .append("  ) day_s")
                .append("  WHERE ap.pair_status = 'OK'")
                .append("    AND date(ap.in_time) >= '").append(fromDate).append("'")
                .append("    AND date(ap.in_time) < CAST('").append(toDate).append("' AS date) + interval '1 day'")
                .append("    AND CAST(day_s AS date) >= '").append(fromDate).append("'")
                .append("    AND CAST(day_s AS date) < CAST('").append(toDate).append("' AS date) + interval '1 day'")
                .append(" )")
                .append(" SELECT in_id, out_id, in_time, out_time, duration, in_devicename, out_devicename, pair_status")
                .append(" FROM (")
                .append("  SELECT in_id, out_id, in_time, out_time, duration, in_devicename, out_devicename, pair_status, sort_time FROM daily_split")
                .append("  UNION ALL")
                .append("  SELECT in_id, out_id, in_time, out_time, duration, in_devicename, out_devicename, pair_status, sort_time FROM all_pairs")
                .append("  WHERE pair_status = 'MISSING OUT'")
                .append("    AND in_time >= '").append(fromDate).append("'")
                .append("    AND in_time < CAST('").append(toDate).append("' AS date) + interval '1 day'")
                .append("  UNION ALL")
                .append("  SELECT in_id, out_id, in_time, out_time, duration, in_devicename, out_devicename, pair_status, out_time AS sort_time FROM all_pairs")
                .append("  WHERE pair_status = 'MISSING IN'")
                .append("    AND out_time >= '").append(fromDate).append("'")
                .append("    AND out_time < CAST('").append(toDate).append("' AS date) + interval '1 day'")
                .append(" ) combined")
                .append(" ORDER BY sort_time;");
        return sql.toString();
    }

    @Override
    public List<InOutPairDto> getUserDailySummary(Integer userId, Date fromDate, Date toDate) {
        boolean terminalWorkDayEnable = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.TERMIANL_DAILY_WORK_ENABLE);
        if (terminalWorkDayEnable) {
            return getUserDailySummaryWithSplit(userId, fromDate, toDate);
        }

        String sql = format("""
                with day_logs as (select f.id, f.startdate, f.statusstring, f.devicename
                    from ${companyId}.fingerprint f
                    join ${companyId}.userfingerprintdevice d
                    ON d.fingerprint_id = f.fingerprintid and d.device_id = f.deviceuuid
                    where d.userid = :userId
                      and f.startdate >= :fromDate
                      and f.startdate < cast(:toDate as date) + interval '1 day'),
                     first_in as (select distinct ON (date(startdate)) date(startdate) as work_date,
                             id              as in_id,
                             startdate       as in_time,
                             devicename      as devicename
                         from day_logs
                         where statusstring = 'IN'
                         order by date(startdate), startdate, id),
                     last_out as (select distinct ON (date(startdate)) date(startdate) as work_date,
                             id              as out_id,
                             startdate       as out_time,
                             devicename      as devicename
                         from day_logs
                         where statusstring = 'OUT'
                         order by date(startdate), startdate desc, id desc)
                select fi.in_id,
                    lo.out_id,
                    fi.in_time,
                    lo.out_time,
                    case
                        when gs.value = 'YES' and ti.id is not null then
                            cast(greatest(0,
                                extract(EPOCH from (
                                    least(lo.out_time, date_trunc('day', lo.out_time) + cast(ti.endtime || ' minutes' as interval))
                                    -
                                    greatest(fi.in_time, date_trunc('day', fi.in_time) + cast(ti.starttime || ' minutes' as interval))
                                ))
                                -
                                case
                                    when (date_trunc('day', fi.in_time) + cast(ti.lunchstart || ' minutes' as interval))
                                         < least(lo.out_time, date_trunc('day', lo.out_time) + cast(ti.endtime || ' minutes' as interval))
                                    and (date_trunc('day', fi.in_time) + cast(ti.lunchend || ' minutes' as interval))
                                        > greatest(fi.in_time, date_trunc('day', fi.in_time) + cast(ti.starttime || ' minutes' as interval))
                                    then (ti.lunchend - ti.lunchstart) * 60
                                    else 0
                                end
                            ) as bigint)
                        when fi.in_time is not null and lo.out_time is not null
                            then cast(extract(EPOCH from (lo.out_time - fi.in_time)) as bigint)
                        else 0
                        end as duration,
                    fi.devicename as in_device_name,
                    lo.devicename as out_device_name,
                    'FILO'  as pair_status
                from first_in fi
                                  left join last_out lo using (work_date)
                                  left join ${companyId}.genericSettings gs on gs.key = 'TERMINAL_TIME_SLOT_ACTUAL_ENABLE'
                                  left join ${companyId}.employee e on e.id = :userId
                                  left join ${companyId}.timeslotitem ti on ti.timeslotid = e.timeslotid and ti.day = extract(dow from fi.in_time)
                         order by work_date
                """, Map.of("companyId", getCompanyId()));

        List<Object[]> results = findNativeByNamedParams(sql, Map.of("userId", userId, "fromDate", fromDate, "toDate", toDate));

        return mapInOutPair(results);
    }

    private List<InOutPairDto> getUserDailySummaryWithSplit(Integer userId, Date fromDate, Date toDate) {
        String sql = format("""
                with extended_logs as (
                    select f.id, f.startdate, f.statusstring, f.devicename
                    from ${companyId}.fingerprint f
                    join ${companyId}.userfingerprintdevice d
                        ON d.fingerprint_id = f.fingerprintid and d.device_id = f.deviceuuid
                    where d.userid = :userId
                      and f.startdate >= :fromDate
                      and f.startdate < cast(:toDate as date) + interval '31 days'
                ),
                first_in as (
                    select distinct ON (date(startdate)) date(startdate) as work_date,
                        id as in_id, startdate as in_time, devicename
                    from extended_logs
                    where statusstring = 'IN'
                      and startdate >= :fromDate
                      and startdate < cast(:toDate as date) + interval '1 day'
                    order by date(startdate), startdate, id
                ),
                session_pairs as (
                    select fi.work_date,
                           fi.in_id,
                           fi.in_time,
                           fi.devicename    as in_devicename,
                           out_rec.id       as out_id,
                           out_rec.startdate as out_time,
                           out_rec.devicename as out_devicename
                    from first_in fi
                    left join lateral (
                        select dl.id, dl.startdate, dl.devicename
                        from extended_logs dl
                        where dl.statusstring = 'OUT'
                          and dl.startdate > fi.in_time
                          and dl.startdate < coalesce(
                              (select min(fi2.in_time) from first_in fi2 where fi2.in_time > fi.in_time),
                              fi.in_time + interval '31 days'
                          )
                        order by dl.startdate desc, dl.id desc
                        limit 1
                    ) out_rec on true
                ),
                daily_segments as (
                    select
                        CAST(day_s AS date)                                                               as work_date,
                        sp.in_id,
                        sp.out_id,
                        greatest(sp.in_time, day_s)                                                       as seg_in,
                        case
                            when sp.out_time is not null
                                then least(sp.out_time, day_s + interval '1 day' - interval '1 second')
                            else null
                        end                                                                               as seg_out,
                        sp.in_devicename,
                        sp.out_devicename
                    from session_pairs sp
                    cross join lateral generate_series(
                        date_trunc('day', sp.in_time),
                        coalesce(date_trunc('day', sp.out_time), date_trunc('day', sp.in_time)),
                        interval '1 day'
                    ) day_s
                    where CAST(day_s AS date) >= :fromDate
                      and CAST(day_s AS date) <= cast(:toDate as date)
                )
                select ds.in_id,
                       ds.out_id,
                       ds.seg_in                                                                          as in_time,
                       ds.seg_out                                                                         as out_time,
                       case
                           when ds.seg_in is not null and ds.seg_out is not null
                               then cast(extract(epoch from (ds.seg_out - ds.seg_in)) as bigint)
                           else 0
                       end                                                                                as duration,
                       ds.in_devicename                                                                   as in_device_name,
                       ds.out_devicename                                                                  as out_device_name,
                       'FILO'                                                                             as pair_status
                from daily_segments ds
                order by work_date
                """, Map.of("companyId", getCompanyId()));

        List<Object[]> results = findNativeByNamedParams(sql, Map.of("userId", userId, "fromDate", fromDate, "toDate", toDate));
        return mapInOutPair(results);
    }

    private List<InOutPairDto> mapInOutPair(List<Object[]> results) {
        List<InOutPairDto> pairs = new ArrayList<>();
        for (Object[] row : results) {
            InOutPairDto dto = new InOutPairDto();
            dto.setInId(row[0] != null ? ((Number) row[0]).intValue() : null);
            dto.setOutId(row[1] != null ? ((Number) row[1]).intValue() : null);
            dto.setInTime(row[2] != null ? new DateNonConvertable((Date) row[2]) : null);
            dto.setOutTime(row[3] != null ? new DateNonConvertable((Date) row[3]) : null);
            dto.setDurationSeconds(row[4] != null ? ((Number) row[4]).longValue() : null);
            dto.setInDevice(row.length > 5 && row[5] != null ? ((String) row[5]).trim() : null);
            dto.setOutDevice(row.length > 6 && row[6] != null ? ((String) row[6]).trim() : null);
            dto.setPairStatus(row.length > 7 ? (String) row[7] : null);
            dto.setDailyDuration(row.length > 8 ? ((Number) row[8]).longValue() : null);
            pairs.add(dto);
        }
        return appendSnapshots(pairs);
    }


    private List<InOutPairDto> appendSnapshots(List<InOutPairDto> userInOutPairs) {
        if (userInOutPairs.isEmpty()) return new ArrayList<>();
        Integer[] inIds = userInOutPairs.stream().map(InOutPairDto::getInId).filter(Objects::nonNull).toArray(Integer[]::new);
        Integer[] outIds = userInOutPairs.stream().map(InOutPairDto::getOutId).filter(Objects::nonNull).toArray(Integer[]::new);

        List<EdsUsersFingerPrintAdjustment> adjustments = userFingerPrintAdjustmentManager.getByFingerprint(ArrayUtils.addAll(inIds, outIds));
        Map<Integer, List<String>> snapshots = new HashMap<>();
        Map<Integer, EdsUsersFingerPrintAdjustment> adjustmentMap = new HashMap<>();
        for (EdsUsersFingerPrintAdjustment adjustment : adjustments) {
            if (adjustment == null) {
                continue;
            }
            ArrayList<FileResource> fileResources = documentsService.getFileResources(F_EMPLOYEE_ATTENDANCE, adjustment.getObjectID(), adjustment.getObjectID());
            snapshots.put(adjustment.getFingerprint().getObjectID(), fileResources.stream().map(FileResource::getDownloadUrl).collect(Collectors.toList()));
            adjustmentMap.put(adjustment.getFingerprint().getObjectID(), adjustment);
        }
        EdsUsersFingerPrintAdjustment defaultValue = new EdsUsersFingerPrintAdjustment();
        List<InOutPairDto> list = new ArrayList<>();
        for (InOutPairDto u : userInOutPairs) {
            if (u.getInId() != null) {
                u.setInSnapshotLink(snapshots != null && !snapshots.isEmpty() && snapshots.get(u.getInId()) != null && !snapshots.get(u.getInId()).isEmpty() ? snapshots.get(u.getInId()).get(0) : null);
                u.setInLatitude(adjustmentMap.getOrDefault(u.getInId(), defaultValue).getLatitude());
                u.setInLongitude(adjustmentMap.getOrDefault(u.getInId(), defaultValue).getLongitude());
                EdsProject inProject = adjustmentMap.getOrDefault(u.getInId(), defaultValue).getProject();
                u.setInProject(inProject != null ? new SelectItem(inProject.getObjectID(), inProject.getName()) : null);
                FingerprintSource inSource = adjustmentMap.getOrDefault(u.getInId(), defaultValue).getSource();
                u.setInSource(inSource != null ? inSource.name() : "UNKNOWN");
            }
            if (u.getOutId() != null) {
                u.setOutSnapshotLink(snapshots != null && !snapshots.isEmpty() && snapshots.get(u.getOutId()) != null && !snapshots.get(u.getOutId()).isEmpty() ? snapshots.get(u.getOutId()).get(0) : null);
                u.setOutLatitude(adjustmentMap.getOrDefault(u.getOutId(), defaultValue).getLatitude());
                u.setOutLongitude(adjustmentMap.getOrDefault(u.getOutId(), defaultValue).getLongitude());
                EdsProject outProject = adjustmentMap.getOrDefault(u.getOutId(), defaultValue).getProject();
                u.setOutProject(outProject != null ? new SelectItem(outProject.getObjectID(), outProject.getName()) : null);
                FingerprintSource outSource = adjustmentMap.getOrDefault(u.getOutId(), defaultValue).getSource();
                u.setOutSource(outSource != null ? outSource.name() : "UNKNOWN");
            }
            list.add(u);
        }
        return list;
    }

    @Override
    public ListResult<AttendanceMarkListItem> getAttendanceMarkList(ListingFilterParameter fp) {
        String schema = getCompanyId();
        StringBuilder where = buildAttendanceMarkWhere(fp, schema);

        String joins = buildAttendanceMarkJoins(schema);
        String countSql = "SELECT COUNT(fp.id) " + joins + where;
        log.debug("getAttendanceMarkList WHERE: {}", where);
        Number total = (Number) findNativeSingle(countSql);
        int totalCount = total != null ? total.intValue() : 0;
        log.debug("getAttendanceMarkList total count: {}", totalCount);

        int limit = fp.getLimit() > 0 ? fp.getLimit() : 20;
        int offset = fp.getStart() != null ? fp.getStart() : 0;
        String dataSql = buildAttendanceMarkDataSql(schema, where, limit, offset, fp);

        List<Object[]> rows = findNative(dataSql);
        ArrayList<AttendanceMarkListItem> items = new ArrayList<>(rows.size());
        for (Object[] row : rows) {
            items.add(mapAttendanceMarkRow(row));
        }
        return new ListResult<>(items, totalCount);
    }

    @Override
    public List<AttendanceMarkListItem> getAttendanceMarkListForExport(ListingFilterParameter fp) {
        String schema = getCompanyId();
        StringBuilder where = buildAttendanceMarkWhere(fp, schema);
        int limit = fp.getLimit() > 0 ? fp.getLimit() : 20;
        int offset = fp.getStart() != null ? fp.getStart() : 0;
        String dataSql = buildAttendanceMarkDataSql(schema, where, limit, offset, fp);
        List<Object[]> rows = findNative(dataSql);
        ArrayList<AttendanceMarkListItem> items = new ArrayList<>(rows.size());
        for (Object[] row : rows) {
            items.add(mapAttendanceMarkRow(row));
        }
        return items;
    }

    @Override
    public void approveAttendanceMarks(List<Integer> fingerprintIds) {
        if (fingerprintIds == null || fingerprintIds.isEmpty()) return;
        String schema = getCompanyId();
        Integer userId = getUser().getObjectID();
        String ids = fingerprintIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        updateNative("UPDATE " + schema + ".fingerprint_adjustment " +
                "SET approval_status = 'APPROVED', updated_at = NOW(), updated_by = " + userId + " " +
                "WHERE fingerprint_id IN (" + ids + ")");
        updateNative("INSERT INTO " + schema + ".fingerprint_adjustment (fingerprint_id, approval_status, created_by, created_at) " +
                "SELECT fp.id, 'APPROVED', " + userId + ", NOW() " +
                "FROM " + schema + ".fingerprint fp " +
                "WHERE fp.id IN (" + ids + ") " +
                "AND NOT EXISTS (SELECT 1 FROM " + schema + ".fingerprint_adjustment fa WHERE fa.fingerprint_id = fp.id)");
    }

    private StringBuilder buildAttendanceMarkWhere(ListingFilterParameter fp, String schema) {
        StringBuilder where = new StringBuilder(" WHERE (mu.deleted IS NULL OR mu.deleted = false) AND fd.userid IS NOT NULL ");

        if (fp.getStartDateNC() != null && !fp.getStartDateNC().isEmpty()) {
            String startIso = parseNcDateToIso(fp.getStartDateNC());
            log.debug("getAttendanceMarkList: startDateNC='{}' → startIso='{}'", fp.getStartDateNC(), startIso);
            if (startIso != null) {
                where.append(" AND fp.startdate >= CAST('").append(startIso).append("' AS DATE) ");
            }
        }
        if (fp.getEndDateNC() != null && !fp.getEndDateNC().isEmpty()) {
            String endIso = parseNcDateToIso(fp.getEndDateNC());
            log.debug("getAttendanceMarkList: endDateNC='{}' → endIso='{}'", fp.getEndDateNC(), endIso);
            if (endIso != null) {
                where.append(" AND fp.startdate < (CAST('").append(endIso).append("' AS DATE) + INTERVAL '1 day') ");
            }
        }
        if (fp.getEmployeeId() != null) {
            where.append(" AND fd.userid = ").append(fp.getEmployeeId());
        }
        if (fp.getDepartmentId() != null) {
            where.append(" AND te.teamid = ").append(fp.getDepartmentId());
        }
        if (fp.getPositionID() != null) {
            where.append(" AND e.positionid = ").append(fp.getPositionID());
        }
        if (fp.getLocationId() != null) {
            where.append(" AND mu.locationid = ").append(fp.getLocationId());
        }
        if (fp.getDeviceID() != null && !fp.getDeviceID().isEmpty()) {
            where.append(" AND fp.deviceuuid = '").append(fp.getDeviceID().replace("'", "''")).append("' ");
        }
        if (fp.getTimeSlotID() != null) {
            where.append(" AND e.timeslotid = ").append(fp.getTimeSlotID());
        }
        if (fp.getSupervisorId() != null) {
            where.append(" AND ep.reportsto = ").append(fp.getSupervisorId());
        }
        if (fp.getEmployeeStatusID() != null) {
            where.append(" AND mu.accountstatusid = ").append(fp.getEmployeeStatusID());
        }
        if (fp.getRoleID() != null) {
            where.append(" AND fd.userid IN (SELECT mr.users_id FROM ").append(schema)
                    .append(".myuser_role mr WHERE mr.roles_id = ").append(fp.getRoleID()).append(")");
        }
        if (fp.isValidSearchKey()) {
            String key = fp.getSqlSearchKey();
            where.append(" AND (lower(mu.firstname || ' ' || mu.lastname) LIKE '").append(key)
                    .append("' OR lower(COALESCE(ep.employeecode,'')) LIKE '").append(key).append("') ");
        }

        if (fp.getFacetFilter() != null && fp.getFacetFilter().isApplyFilter()) {
            FacetFilterRpc filterRpc = fp.getFacetFilter();
            HashMap<String, FacetContentRpc> facetContentMap = filterRpc.getFacetContentMap();
            appendFacetIn(where, facetContentMap, "employee", "fd.userid");
            appendFacetIn(where, facetContentMap, "department", "te.teamid");
            appendFacetIn(where, facetContentMap, "position", "e.positionid");
            appendFacetIn(where, facetContentMap, "location", "mu.locationid");
            appendFacetIn(where, facetContentMap, "timeslot", "e.timeslotid");
            appendFacetIn(where, facetContentMap, "supervisor", "ep.reportsto");
            appendFacetIn(where, facetContentMap, "status", "mu.accountstatusid");
            FacetContentRpc terminalContent = facetContentMap.get("terminal");
            if (terminalContent != null && terminalContent.getFacetItems().length > 0) {
                StringBuilder ids = new StringBuilder();
                for (SelectItem item : terminalContent.getFacetItems()) {
                    if (!ids.isEmpty()) ids.append(",");
                    ids.append(item.getId());
                }
                where.append(" AND fp.deviceuuid IN (SELECT companyuniqueid FROM ").append(schema)
                        .append(".attendance_terminal WHERE id IN (").append(ids).append("))");
            }
            if ("date".equals(filterRpc.getSelectedDateSolrCodeName())) {
                if (filterRpc.getStartDate() != null) {
                    where.append(" and fp.startdate >= '").append(ISO_DATE_FORMAT.format(filterRpc.getStartDate())).append("'");
                }
                if (filterRpc.getEndDate() != null) {
                    where.append(" and fp.startdate <= '").append(ISO_DATE_FORMAT.format(filterRpc.getEndDate())).append("'");
                }
            }
            FacetContentRpc deviceTypeContent = facetContentMap.get("source");
            if (deviceTypeContent != null && deviceTypeContent.getFacetItems().length > 0) {
                List<String> clauses = new ArrayList<>();
                for (SelectItem item : deviceTypeContent.getFacetItems()) {
                    String code = item.getCode();
                    if (code == null) continue;
                    if (FingerprintSource.UNKNOWN.name().equals(code)) {
                        clauses.add("(fa.source IS NULL OR fa.source = 'UNKNOWN')");
                    } else {
                        clauses.add("fa.source = '" + code + "'");
                    }
                }
                if (!clauses.isEmpty()) {
                    where.append(" AND (").append(String.join(" OR ", clauses)).append(")");
                }
            }
            FacetContentRpc isAutoContent = facetContentMap.get("isAuto");
            if (isAutoContent != null && isAutoContent.getFacetItems().length > 0) {
                List<String> clauses = new ArrayList<>();
                for (SelectItem item : isAutoContent.getFacetItems()) {
                    String code = item.getCode();
                    if ("YES".equals(code)) {
                        clauses.add("fa.is_auto = true");
                    } else if ("NO".equals(code)) {
                        clauses.add("(fa.is_auto IS NULL OR fa.is_auto = false)");
                    }
                }
                if (!clauses.isEmpty()) {
                    where.append(" AND (").append(String.join(" OR ", clauses)).append(")");
                }
            }
            FacetContentRpc roleContent = facetContentMap.get("role");
            if (roleContent != null && roleContent.getFacetItems().length > 0) {
                StringBuilder ids = new StringBuilder();
                for (SelectItem item : roleContent.getFacetItems()) {
                    if (!ids.isEmpty()) ids.append(",");
                    ids.append(item.getId());
                }
                where.append(" AND fd.userid IN (SELECT mr.users_id FROM ").append(schema)
                        .append(".myuser_role mr WHERE mr.roles_id IN (").append(ids).append("))");
            }
        }

        return where;
    }

    private String buildAttendanceMarkJoins(String schema) {
        return " FROM " + schema + ".fingerprint fp"
                + " LEFT JOIN " + schema + ".userfingerprintdevice fd ON fd.fingerprint_id = fp.fingerprintid AND fd.device_id = fp.deviceuuid"
                + " LEFT JOIN " + schema + ".myuser mu ON mu.id = fd.userid"
                + " LEFT JOIN " + schema + ".employee e ON e.id = fd.userid"
                + " LEFT JOIN " + schema + ".employeeprofile ep ON ep.id = e.profileid"
                + " LEFT JOIN " + schema + ".teamemployee te ON te.id = e.employeedepartmentid"
                + " LEFT JOIN " + schema + ".team t ON t.id = te.teamid"
                + " LEFT JOIN " + schema + ".position pos ON pos.id = e.positionid"
                + " LEFT JOIN " + schema + ".reference muref ON muref.id = mu.accountstatusid"
                + " LEFT JOIN " + schema + ".fingerprint_adjustment fa ON fa.fingerprint_id = fp.id"
                + " LEFT JOIN " + schema + ".myuser mu_creator ON mu_creator.id = fa.created_by"
                + " LEFT JOIN " + schema + ".myuser mu_updater ON mu_updater.id = fa.updated_by"
                + " LEFT JOIN " + schema + ".location l ON l.id = mu.locationid"
                + " LEFT JOIN " + schema + ".timeslot ts ON ts.id = e.timeslotid"
                + " LEFT JOIN " + schema + ".myuser sup ON sup.id = ep.reportsto"
                + " LEFT JOIN " + schema + ".attendance_terminal aterminal ON aterminal.companyuniqueid = fp.deviceuuid"
                + " LEFT JOIN LATERAL ("
                + "   SELECT string_agg(r.name, ', ' ORDER BY r.id) AS role_names,"
                + "          string_agg(CAST(r.id AS TEXT), ',' ORDER BY r.id) AS role_ids,"
                + "          string_agg(r.code, ',' ORDER BY r.id) AS role_codes"
                + "   FROM " + schema + ".myuser_role mr"
                + "   JOIN " + schema + ".role r ON r.id = mr.roles_id"
                + "   WHERE mr.users_id = fd.userid"
                + " ) roles_agg ON TRUE";
    }

    private String buildAttendanceMarkOrderBy(ListingFilterParameter fp) {
        String field = fp.getSortField();
        String dir = (fp.getSortDir() != null && fp.getSortDir() == 1) ? "ASC" : "DESC";
        String expr;
        if (field == null) {
            expr = null;
        } else expr = switch (field) {
            case "employeeName" -> "COALESCE(mu.firstname, '') || ' ' || COALESCE(mu.lastname, '')";
            case "employeeCode" -> "COALESCE(ep.employeecode, '')";
            case "date", "createdDate" -> "fp.startdate";
            case "department" -> "t.name";
            case "position" -> "pos.name";
            case "location" -> "l.name";
            case "timeslot" -> "ts.name";
            case "supervisor" -> "COALESCE(sup.firstname, '') || ' ' || COALESCE(sup.lastname, '')";
            case "terminal" -> "fp.devicename";
            case "deviceId" -> "fp.deviceuuid";
            case "source" -> "fa.source";
            case "validated" -> "fp.statusstring";
            case "employeeStatus" -> "muref.name";
            case "note" -> "fa.description";
            case "createdByName" -> "COALESCE(mu_creator.firstname, '') || ' ' || COALESCE(mu_creator.lastname, '')";
            case "updatedAt" -> "fa.updated_at";
            case "updatedByName" -> "COALESCE(mu_updater.firstname, '') || ' ' || COALESCE(mu_updater.lastname, '')";
            case "approvalStatus" -> "fa.approval_status";
            case "isAuto" -> "fa.is_auto";
            default -> null;
        };
        if (expr == null) {
            return " ORDER BY fp.startdate DESC NULLS LAST";
        }
        return " ORDER BY " + expr + " " + dir + " NULLS LAST, fp.startdate DESC NULLS LAST";
    }

    private String buildAttendanceMarkDataSql(String schema, StringBuilder where, int limit, int offset, ListingFilterParameter fp) {
        return "SELECT"
                + " fp.id,"                                                                             // [0]
                + " fd.userid,"                                                                        // [1]
                + " COALESCE(ep.employeecode, '') AS employeecode,"                                    // [2]
                + " COALESCE(mu.firstname, '') || ' ' || COALESCE(mu.lastname, '') AS employeename,"   // [3]
                + " fp.startdate AS mark_date,"                                                        // [4]
                + " fp.startdate AS date,"                                                             // [5]
                + " fp.deviceuuid AS deviceid,"                                                        // [6]
                + " fp.devicename AS terminal,"                                                        // [7]
                + " fp.statusstring AS validated,"                                                     // [8]
                + " fa.source AS source,"                                                              // [9]
                + " t.name AS department,"                                                             // [10]
                + " pos.name AS position_name,"                                                        // [11]
                + " muref.name AS employee_status,"                                                    // [12]
                + " l.name AS location_name,"                                                          // [13]
                + " ts.name AS timeslot_name,"                                                         // [14]
                + " COALESCE(sup.firstname, '') || ' ' || COALESCE(sup.lastname, '') AS supervisor_name," // [15]
                + " NULL AS picture_url,"                                                              // [16]
                + " roles_agg.role_names AS role_names,"                                               // [17]
                + " roles_agg.role_ids AS role_ids,"                                                   // [18]
                + " t.id AS department_id,"                                                            // [19]
                + " e.positionid AS position_id,"                                                      // [20]
                + " mu.locationid AS location_id,"                                                     // [21]
                + " e.timeslotid AS timeslot_id,"                                                      // [22]
                + " ep.reportsto AS supervisor_id,"                                                    // [23]
                + " mu.accountstatusid AS employee_status_id,"                                         // [24]
                + " aterminal.id AS terminal_id,"                                                      // [25]
                + " fa.id AS adjustment_id,"                                                           // [26]
                + " fa.latitude AS latitude,"                                                          // [27]
                + " fa.longitude AS longitude,"                                                        // [28]
                + " fa.description AS note,"                                                           // [29]
                + " COALESCE(mu_creator.firstname, '') || ' ' || COALESCE(mu_creator.lastname, '') AS created_by_name," // [30]
                + " fa.updated_at AS updated_at,"                                                      // [31]
                + " COALESCE(mu_updater.firstname, '') || ' ' || COALESCE(mu_updater.lastname, '') AS updated_by_name," // [32]
                + " mu.photoid AS photo_id,"                                                           // [33]
                + " COALESCE(fa.approval_status, 'PENDING') AS approval_status,"                       // [34]
                + " roles_agg.role_codes AS role_codes,"                                               // [35]
                + " fa.created_at AS created_at,"                                                      // [36]
                + " fa.is_auto AS is_auto"                                                             // [37]
                + buildAttendanceMarkJoins(schema) + where
                + buildAttendanceMarkOrderBy(fp)
                + " LIMIT " + limit + " OFFSET " + offset;
    }

    private AttendanceMarkListItem mapAttendanceMarkRow(Object[] row) {
        AttendanceMarkListItem item = new AttendanceMarkListItem();
        item.setObjectId(row[0] != null ? ((Number) row[0]).intValue() : null);
        item.setEmployeeId(row[1] != null ? ((Number) row[1]).intValue() : null);
        item.setEmployeeCode(row[2] != null ? row[2].toString() : null);
        item.setEmployeeName(row[3] != null ? row[3].toString() : null);
        item.setDate(row[4] instanceof java.util.Date ? new DateNonConvertable((java.util.Date) row[4]) : null);
        item.setCreatedDate(row[36] instanceof java.util.Date ? new DateNonConvertable((java.util.Date) row[36]) : null);
        item.setDeviceId(row[6] != null ? row[6].toString() : null);
        item.setTerminal(row[7] != null ? row[7].toString() : null);
        item.setValidated(row[8] != null ? row[8].toString() : null);
        item.setSource(row[9] != null ? row[9].toString() : null);
        item.setDepartment(row[10] != null ? row[10].toString() : null);
        item.setPosition(row[11] != null ? row[11].toString() : null);
        item.setEmployeeStatus(row[12] != null ? row[12].toString() : null);
        item.setLocation(row[13] != null ? row[13].toString() : null);
        item.setTimeslotName(row[14] != null ? row[14].toString() : null);
        String supervisorName = row[15] != null ? row[15].toString().trim() : null;
        item.setSupervisor(supervisorName != null && !supervisorName.isEmpty() ? supervisorName : null);
        // pictureUrl is resolved in AvailabilityServiceImpl via documentsService.getFileResources()
        item.setRole(localizeRoles(row[17], row[35]));
        item.setRoleIds(row[18] != null ? row[18].toString() : null);
        item.setDepartmentId(row[19] != null ? ((Number) row[19]).intValue() : null);
        item.setPositionId(row[20] != null ? ((Number) row[20]).intValue() : null);
        item.setLocationId(row[21] != null ? ((Number) row[21]).intValue() : null);
        item.setTimeslotId(row[22] != null ? ((Number) row[22]).intValue() : null);
        item.setSupervisorId(row[23] != null ? ((Number) row[23]).intValue() : null);
        item.setEmployeeStatusId(row[24] != null ? ((Number) row[24]).intValue() : null);
        item.setTerminalId(row[25] != null ? ((Number) row[25]).intValue() : null);
        item.setAdjustmentId(row[26] != null ? ((Number) row[26]).intValue() : null);
        item.setLatitude(row[27] != null ? ((Number) row[27]).doubleValue() : null);
        item.setLongitude(row[28] != null ? ((Number) row[28]).doubleValue() : null);
        item.setNote(row[29] != null ? row[29].toString() : null);
        String createdByName = row[30] != null ? row[30].toString().trim() : null;
        item.setCreatedByName(createdByName != null && !createdByName.isEmpty() ? createdByName : null);
        item.setUpdatedAt(row[31] instanceof java.util.Date ? new DateNonConvertable((java.util.Date) row[31]) : null);
        String updatedByName = row[32] != null ? row[32].toString().trim() : null;
        item.setUpdatedByName(updatedByName != null && !updatedByName.isEmpty() ? updatedByName : null);
        item.setPhotoId(row[33] != null ? ((Number) row[33]).intValue() : null);
        item.setApprovalStatus(row[34] != null ? row[34].toString() : "PENDING");
        item.setIsAuto(row[37] instanceof Boolean ? (Boolean) row[37] : null);
        return item;
    }

    private String localizeRoles(Object namesObj, Object codesObj) {
        if (namesObj == null) return null;
        String[] names = namesObj.toString().split(", ");
        String[] codes = codesObj != null ? codesObj.toString().split(",") : new String[0];
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < names.length; i++) {
            if (!result.isEmpty()) result.append(", ");
            String code = i < codes.length ? codes[i].trim() : null;
            String name = names[i].trim();
            result.append(code != null ? commonLocalizer.localize(code, name) : name);
        }
        return result.toString();
    }

    private void appendFacetIn(StringBuilder where, HashMap<String, FacetContentRpc> facetContentMap, String key, String column) {
        FacetContentRpc content = facetContentMap.get(key);
        if (content != null && content.getFacetItems().length > 0) {
            StringBuilder ids = new StringBuilder();
            boolean hasNull = false;
            for (SelectItem item : content.getFacetItems()) {
                if (item.getId() == -1) {
                    hasNull = true;
                } else {
                    if (!ids.isEmpty()) ids.append(",");
                    ids.append(item.getId());
                }
            }
            if (!ids.isEmpty() && hasNull) {
                where.append(" AND (").append(column).append(" IN (").append(ids).append(") OR ").append(column).append(" IS NULL)");
            } else if (!ids.isEmpty()) {
                where.append(" AND ").append(column).append(" IN (").append(ids).append(")");
            } else {
                where.append(" AND ").append(column).append(" IS NULL");
            }
        }
    }

    public static String format(String template, Map<String, Object> parameters) {
        StringBuilder newTemplate = new StringBuilder(template);
        List<Object> valueList = new ArrayList<>();

        Matcher matcher = Pattern.compile("[$][{](\\w+)}").matcher(template);

        while (matcher.find()) {
            String key = matcher.group(1);

            String paramName = "${" + key + "}";
            int index = newTemplate.indexOf(paramName);
            if (index != -1) {
                newTemplate.replace(index, index + paramName.length(), "%s");
                valueList.add(parameters.get(key));
            }
        }

        return String.format(newTemplate.toString(), valueList.toArray());
    }

}
