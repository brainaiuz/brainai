package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsTimeTrack;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.StaffInOut;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.TimeTrackManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Repository("timeTrackManager")
public class TimeTrackManagerImpl extends BaseManager<EdsTimeTrack> implements TimeTrackManager {

    private ReferenceManager referenceManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;

    public TimeTrackManagerImpl() {
        super(EdsTimeTrack.class);
    }

    @Autowired
    public void setReferenceManager(ReferenceManager referenceManager) {
        this.referenceManager = referenceManager;
    }

    public EdsTimeTrack getLatestTimeEntry(Integer employeeID) {
        return (EdsTimeTrack) findSingle("select t from EdsTimeTrack t where t.employee.objectID = ? and (t.endDate is null) order by t.startDate desc", employeeID);
    }

    public List<EdsTimeTrack> getTimeEntriesByToday(Integer employeeId, String today) {
        return find("select tt from EdsTimeTrack tt " +
                "where tt.employee.objectID=? and " +
                "(to_char(tt.startDate,'yyyy-mm-dd')='" + today + "') " +
                "order by tt.startDate desc", employeeId);
    }

    public List getMonthTimeTrack(Integer teamId, Integer locationid, Date startDate, Date endDate, boolean withLunchTime) {
        return getMonthTimeTrackDefault(teamId, locationid, startDate, endDate, withLunchTime);
    }

    private List getMonthTimeTrackDefault(Integer teamId, Integer locationid, Date startDate, Date endDate, boolean withLunchTime) {
        EdsUser user = genericSettingsManager.getUser();
        boolean isFingerPrintEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.FINGERPRINT_DEVICE_ENABLED);
        String timeZoneCurrentUser = user.getUserTimezone().getID();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String companyId = getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append(" with fingerprintdata as (select e.id, ");
        sql.append(" date(d.from_date at time zone '").append(timeZoneCurrentUser).append("') from_date, ");
        sql.append(" CASE ");
        sql.append(" WHEN SUM((EXTRACT(EPOCH FROM tt.endDate at time zone '").append(timeZoneCurrentUser).append("') - ");
        sql.append(" EXTRACT(EPOCH FROM tt.startDate at time zone '").append(timeZoneCurrentUser).append("')) / 60 ");
        if (withLunchTime) {
            sql.append(" - (tsi.lunchend - tsi.lunchstart) - (tsi.coffeeend - tsi.coffeestart) ");
        }
        sql.append(" ) < 0 THEN 0 ");
        sql.append(" ELSE SUM((EXTRACT(EPOCH FROM tt.endDate at time zone '").append(timeZoneCurrentUser).append("') - ");
        sql.append(" EXTRACT(EPOCH FROM tt.startDate at time zone '").append(timeZoneCurrentUser).append("')) / 60  ");
        if (withLunchTime) {
            sql.append(" - (tsi.lunchend - tsi.lunchstart) - (tsi.coffeeend - tsi.coffeestart) ");
        }
        sql.append(") END from ").append(getPublic()).append(".datejoin d ");
        if (isFingerPrintEnabled) {
            sql.append(" left join ").append(companyId).append(".fingerprint tt ");
            sql.append(" on date(d.from_date) = date(tt.startdate) and tt.statusid = 20 ");
            sql.append(" join ").append(companyId).append(".userfingerprintdevice fd ");
            sql.append(" on fd.fingerprint_id = tt.fingerprintId and tt.deviceuuid = fd.device_id ");
        } else {
            sql.append(" left outer join ").append(getCompanyId()).append(".timetrack tt on date(d.from_date)=date(tt.startdate)");
        }
        sql.append(" left join ").append(companyId).append(".employee e on e.id = ").append(isFingerPrintEnabled ? " fd.userid " : "tt.employeeid");
        sql.append(" left join ").append(companyId).append(".myuser m on e.id = m.id ");
        sql.append(" left join ").append(companyId).append(".attendancerawdata ard ");
        sql.append(" on e.id = ard.employeeid and date(d.from_date) = date(ard.date) and ");
        sql.append(" ard.holiday is not true and ");
        sql.append(" ard.leave = 0 ");
        sql.append(" left join ").append(companyId).append(".timeslotitem tsi ");
        sql.append(" on tsi.timeslotid = e.timeslotid and ");
        sql.append(" tsi.day = extract(dow from d.from_date at time zone '").append(timeZoneCurrentUser).append("') ");
        sql.append(" where (d.from_date between '").append(format.format(startDate)).append("' ");
        sql.append(" and '").append(format.format(endDate)).append("') ");
        sql.append(" and m.deleted <> true ");
        sql.append(" group by e.id, d.from_date at time zone '").append(timeZoneCurrentUser).append("') ");
        sql.append(" select e.id, ");
        sql.append(" d.from_date at time zone '").append(timeZoneCurrentUser).append("', ");
        sql.append(" coalesce(fp.sum, ");
        if (withLunchTime) {
            sql.append(" case when tsie.id is not null then tsie.endtime - tsie.starttime ");
            sql.append(" - (tsie.coffeeEnd - tsie.coffeeStart) - (tsie.lunchEnd - tsie.lunchStart) else tsi.endtime - tsi.starttime - (tsi.coffeeEnd - tsi.coffeeStart) - (tsi.lunchEnd - tsi.lunchStart) end) ");

            sql.append(", case when tsie.id is not null then tsie.endtime - tsie.starttime ");
            sql.append(" - (tsie.coffeeEnd - tsie.coffeeStart) - (tsie.lunchEnd - tsie.lunchStart) else tsi.endtime - tsi.starttime - (tsi.coffeeEnd - tsi.coffeeStart) - (tsi.lunchEnd - tsi.lunchStart) end ");
        } else {
            sql.append(" case when tsie.id is not null then tsie.endtime - tsie.starttime else tsi.endtime - tsi.starttime end), ");
            sql.append(" case when tsie.id is not null then tsie.endtime - tsie.starttime else tsi.endtime - tsi.starttime end ");
        }
        sql.append(" from ").append(companyId).append(".employee e ");
        sql.append(" inner join ").append(companyId).append(".myuser m on m.id = e.id ");
        sql.append(" left outer join ").append(companyId).append(".teamemployee te on te.id = e.employeeDepartmentId ");
        sql.append(" left outer join ").append(companyId).append(".team t on t.id = te.teamid ");
        sql.append(" join ").append(getPublic()).append(".datejoin d on d.from_date between '").append(format.format(startDate)).append("' ");
        sql.append(" and '").append(format.format(endDate)).append("' ");
        sql.append(" left join ").append(companyId).append(".timeslotitem tsie ");
        sql.append(" on tsie.timeslotid = e.timeslotid and ");
        sql.append(" to_char(tsie.exceptionaldate, 'yyyy-MM-dd') = to_char(d.from_date, 'yyyy-MM-dd') ");
        sql.append(" left join ").append(companyId).append(".timeslotitem tsi ");
        sql.append(" on tsi.timeslotid = e.timeslotid and ");
        sql.append(" tsi.day = extract(dow from d.from_date at time zone '").append(timeZoneCurrentUser).append("') and tsi.exceptionaldate is null ");
        sql.append(" LEFT JOIN fingerprintdata fp ON fp.id = e.id and date(d.from_date) = date(fp.from_date) ");
        sql.append(" where m.deleted <> true ");
        sql.append(locationid != null ? " and m.locationid=" + locationid + " " : " ");
        sql.append(teamId != null ? " and t.id=" + teamId + " and te.isdeleted<>true " : " ");
        sql.append(" order by e.id");
        return findNative(sql.toString());
    }

    public void deleteEqualsStartDate(Integer employeeId, Date startDate) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        updateNative("delete from " + getCompanyId() + ".timetrack  \n" +
                "where employeeId=" + employeeId + " and startDate is not null and to_char(startDate,'yyyy-MM-dd')='" + format.format(startDate) + "'");
    }

    public void deleteByShiftId(Integer shiftId) {
        updateNative("delete from " + getCompanyId() + ".timetrack where shiftId = " + shiftId);
    }

    public List<StaffInOut> getEmployeeTimeTrackInPeriod(Integer employeeId, Date startDate, Date endDate) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return (List<StaffInOut>) find("select tt from EdsTimeTrack tt where tt.employee.objectID=? and tt.startDate is not null and to_char(tt.startDate,'yyyy-MM-dd')=? and tt.endDate is not null and to_char(tt.endDate,'yyyy-MM-dd')=? and tt.status.objectID = ?", employeeId, format.format(startDate), format.format(endDate), 20);
    }

    public void insertTimeTrack(Integer companyID, Integer employeeID, Date startDate, Integer statusID, boolean isFingerPrintUser) {
        String sql = "INSERT INTO \"" + companyID + "\".timetrack (employeeId, startDate, statusid, fingerPrintUser) VALUES (" + employeeID + ",'" + startDate + "'," + statusID + "," + isFingerPrintUser + ")";
        updateNative(sql);
    }

    public void insertTimeTrack(Integer companyID, Integer employeeID, Date startDate, Date endDate, Integer statusID, boolean isFingerPrintUser) {
        String sql = "INSERT INTO \"" + companyID + "\".timetrack (employeeId, startDate, endDate, statusid, fingerPrintUser) VALUES (" + employeeID + ",'" + startDate + "','" + endDate + "'," + statusID + "," + isFingerPrintUser + ")";
        updateNative(sql);
    }

    public void insertTimeTrack(Integer companyID, Integer employeeID, Date startDate, Date endDate, Integer statusID, String location) {
        String sql = "INSERT INTO \"" + companyID + "\".timetrack (employeeId, startDate, endDate, statusid, location) VALUES (" + employeeID + ",'" + startDate + "','" + endDate + "'," + statusID + ",'" + location + "')";
        updateNative(sql);
    }

    public List<EdsTimeTrack> getUserAvailableTimeTrackInPeriod(Integer userId, Date startDate, Date endDate) {
        EdsReference available = referenceManager.findReference(Constants.TIME_TRACK_STATUS, Constants.AVAILABLE);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return find("FROM EdsTimeTrack tt WHERE tt.employee.objectID=? AND tt.startDate IS NOT NULL " +
                        "AND to_char(tt.startDate,'yyyy-MM-dd')=? AND tt.endDate IS NOT NULL " +
                        "AND to_char(tt.endDate,'yyyy-MM-dd')=? AND tt.status=?",
                userId, format.format(startDate), format.format(endDate), available
        );
    }
}
