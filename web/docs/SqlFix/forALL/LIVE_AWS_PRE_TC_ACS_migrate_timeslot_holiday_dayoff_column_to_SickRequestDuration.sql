
--Schema Update dan kiyin urilsin!!!

update "anv".sickrequestduration s
set timeSlot = (select COALESCE(a.timeSlot,0) from "anv".sickrequest sr join "anv".attendancerawdata a on sr.employeeid=a.employeeid where a.date = s.date limit 1) where s.date is not null;

update "anv".sickrequestduration s
set dayOff = (select a.dayOff from "anv".sickrequest sr join "anv".attendancerawdata a on sr.employeeid=a.employeeid where a.date = s.date limit 1) where s.date is not null;

update "anv".sickrequestduration s
set holiday = (select a.holiday from "anv".sickrequest sr join "anv".attendancerawdata a on sr.employeeid=a.employeeid where a.date = s.date limit 1) where s.date is not null;

update "anv".sickrequestduration s
set holidayfromannualleave = (select a.holidayfromannualleave from "anv".sickrequest sr join "anv".attendancerawdata a on sr.employeeid=a.employeeid where a.date = s.date limit 1) where s.date is not null;