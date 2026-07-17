insert into "anv".attendance_hour(employee_id, start_date, end_date, shift_id, timeslot_id, type) (select fp.employeeid,
                                                                                                          start_date,
                                                                                                          end_date,
                                                                                                          fp.shiftId,
                                                                                                          fp.timeSlotId,
                                                                                                          'MANUAL_OR_SHIFT'
                                                                                                   from (select employeeid,
                                                                                                                min(startDate) as start_date,
                                                                                                                shiftId,
                                                                                                                timeSlotId, date (startdate) fdate from "anv".fingerprint
                                                                                                   where statusstring = '0'
                                                                                                   group by employeeid,
                                                                                                       date (startDate),
                                                                                                       shiftId,
                                                                                                       timeSlotId) fp
                                                                                                              full outer join (select employeeid, max(startdate) end_date, date(startdate) fdate
                                                                                                                               from "anv".fingerprint
                                                                                                                               where statusstring = '1'
                                                                                                                               group by employeeid, date(startdate)) fp2
ON fp.employeeid = fp2.employeeid AND fp.fdate = fp2.fdate);


insert into "anv".attendance_hour(employee_id, start_date, end_date, shift_id, timeslot_id, type) (select employeeid,
                                                                                                          startdate,
                                                                                                          enddate,
                                                                                                          shiftid,
                                                                                                          timeslotid,
                                                                                                          'NIGHT_SHIFT'
                                                                                                   from "anv".fingerprint
                                                                                                   where statusstring = '2');
insert into "anv".attendance_hour(employee_id, start_date, end_date, shift_id, timeslot_id, type) (select employeeid, startdate, enddate, shiftid, timeslotid, 'OVERTIME'
                                                                                                   from "anv".fingerprint
                                                                                                   where statusstring = '3');