package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsAttendanceHour;
import com.edatasite.workforce.core.domain.EdsShift;
import com.edatasite.workforce.gwt.core.client.enums.AttendanceHoursType;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AttendanceHoursManager;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.DateUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("attendanceHoursManager")
public class AttendanceHoursManagerImpl extends BaseManager<EdsAttendanceHour> implements AttendanceHoursManager {
    public AttendanceHoursManagerImpl() {
        super(EdsAttendanceHour.class);
    }

    @Override
    public void insertEmployeeHours(Integer employeeId, Date start, Date end, EdsShift shift, Integer timeslotId) {
        StringBuilder query = new StringBuilder();

        String type = shift != null ? AttendanceHoursType.getByCode(shift.getLookupType()).name() : AttendanceHoursType.MANUAL_OR_SHIFT.name();
        Integer shiftId = shift != null ? shift.getObjectID() : null;

        if (DateUtils.areOnTheSameDay(start, end)) {
            query.append("INSERT INTO ")
                    .append(getCompanyId())
                    .append(".attendance_hour (employee_id, start_date, end_date, shift_id, timeSlot_id, type) VALUES (")
                    .append(employeeId).append(", '")
                    .append(start).append("', '")
                    .append(end).append("', ")
                    .append(shiftId).append(", ")
                    .append(timeslotId).append(", '")
                    .append(type).append("');");
        } else {
            type = AttendanceHoursType.NIGHT_SHIFT.name();
            Calendar startDate = new GregorianCalendar();
            startDate.setTime(start);
            Calendar endDate = new GregorianCalendar();
            endDate.setTime(end);
            ServerUtils.setEndOfTheDay(startDate);
            ServerUtils.setBeginningOfTheDay(endDate);

            query.append("INSERT INTO ")
                    .append(getCompanyId())
                    .append(".attendance_hour (employee_id, start_date, end_date, shift_id, timeSlot_id, type) VALUES (")
                    .append(employeeId).append(", '")
                    .append(start).append("', '")
                    .append(startDate.getTime()).append("', ")
                    .append(shiftId).append(", ")
                    .append(timeslotId).append(", '")
                    .append(type).append("');");

            query.append("INSERT INTO ")
                    .append(getCompanyId())
                    .append(".attendance_hour (employee_id, start_date, end_date, shift_id, timeSlot_id, type) VALUES (")
                    .append(employeeId).append(", '")
                    .append(endDate.getTime()).append("', '")
                    .append(end).append("', ")
                    .append(shiftId).append(", ")
                    .append(timeslotId).append(", '")
                    .append(type).append("');");
        }

        updateNative(query.toString());
    }

    @Override
    public void deleteEqualsStartDate(Integer employeeId, Date date, AttendanceHoursType type) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder query = new StringBuilder()
                .append("delete from ").append(getCompanyId()).append(".attendance_hour where employee_id = ").append(employeeId)
                .append(" and to_char(start_date,'yyyy-MM-dd') = '").append(format.format(date))
                .append("' and type = '").append(type).append("'");
        updateNative(query.toString());
    }

    @Override
    public void deleteByShiftId(Integer shiftId) {
        updateNative("delete from " + getCompanyId() + ".attendance_hour where shift_id = " + shiftId);
    }

    public Map<Date, BigDecimal> getAttendanceHours(ListingFilterParameter fp) {
        String sql = " select t.date, sum(t.hours) \n" +
                " from (select to_char(ah.start_date, 'yyyy-MM-dd')                                         as date, \n" +
                "             coalesce((SELECT EXTRACT(epoch from (ah.end_date - ah.start_date)) / 3600), 0) as hours \n" +
                "      from " + getCompanyId() + ".attendance_hour ah \n" +
                "               join " + getCompanyId() + ".shift s on s.id = ah.shift_id \n" +
                "               join " + getCompanyId() + ".reference r on s.overallstatus = r.id \n" +
                "      where " + ServerUtils.checkForDeleted("s.deleted") + " \n" +
                "        and to_char(s.period, 'yyyy-MM') = '" + new SimpleDateFormat("yyyy-MM").format(fp.getStartDate()) + "' \n" +
                "        and ah.employee_id = " + fp.getEmployeeId() + " \n" +
                "        and s.lookuptype = " + LookUpConstants.BRIGADA_ID + " \n" +
                "        and r.code = '" + Constants.SHIFT_APPROVED + "') t \n" +
                " where t.hours > 0 \n" +
                " group by t.date";
        List<Object[]> result = findNative(sql);
        Map<Date, BigDecimal> resultMap = new HashMap<>();
        if (result == null || result.isEmpty()) {
            return resultMap;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (Object[] item : result) {
            try {
                String dateString = (String) item[0];
                Double hoursDouble = (Double) item[1];
                Date date = dateFormat.parse(dateString);
                BigDecimal hours = BigDecimal.valueOf(hoursDouble);
                resultMap.put(date, hours);
            } catch (ParseException ignoder) {
            }
        }
        return resultMap;
    }
}
