insert into "0".leave_reason (code, name, description, leavedays, shortname, hasprorata, attendancelr, autoapprove, color,includeDayOffs,includeHolidays, isactive, isSystemReference, updatedDate)
select code, name, description, leavedays, shortname, hasprorata, attendancelr, autoapprove, color,includeDayOffs,includeHolidays, isactive, isSystemReference, updatedDate from "0".reference
where deleted is false and parentid=(select id from "0".reference where code=_LEAVE_REQUEST_TYPE limit 1);

delete from "anv".rolepermission where permissioncode='HRMS_LIVE_REQUEST' and rolecode='MEM';
delete from "anv".rolepermission where permissioncode='HRMS_LIVE_REQUEST' and rolecode='ESS_USER';
delete from "anv".rolepermission where permissioncode='HRMS_LIVE_REQUEST' and rolecode='CUSTOMER_SERVICE_REPRESENTATIVE';
delete from "anv".rolepermission where permissioncode='HRMS_LIVE_REQUEST' and rolecode='CUSTOMER_SERVICE_MANAGER';

insert into "anv".leave_reason (code, name, description, leavedays, shortname, hasprorata, attendancelr, autoapprove, color,includeDayOffs,includeHolidays, isactive, isSystemReference, updatedDate)
select code, name, description, leavedays, shortname, hasprorata, attendancelr, autoapprove, color,includeDayOffs,includeHolidays, isactive, isSystemReference, updatedDate from "anv".reference
where deleted is false and parentid=(select id from "anv".reference where code='_LEAVE_REQUEST_TYPE' limit 1);

update "anv".annualleave al set allowancedays=annualallowanceminutes/(select tm.dlavtm from "anv".employee e
join (select t.id,
sum((ti.endTime-ti.startTime)-(ti.lunchEnd-ti.lunchStart)-(ti.coffeeEnd-ti.coffeeStart))/count(ti.id) dlavtm
from "anv".timeslot t
join "anv".timeslotitem ti on t.id=ti.timeslotid
where t.deleted is not true and ti.exceptionaldate is null
and (ti.startTime>0 or ti.endTime>0)
group by t.id) tm on e.timeslotid=tm.id where al.employeeid=e.id);

update "anv".annualleave al set reason_code=(select r.code from "anv".reference r where al.reasonid=r.id);
update "anv".sickrequest sr set reason_code=(select r.code from "anv".reference r where sr.reason=r.id);


update "anv".sickrequestduration s set day=durationTime::numeric/(select max(distinct a.timeslot)
from "anv".sickrequest sr
join "anv".leave_reason r on sr.reason_code=r.code
join "anv".sickrequestduration sd on sr.id=sd.sickrequestid
join "anv".attendancerawdata a on sr.employeeid=a.employeeid and sd.date=a.date
where s.id=sd.id and COALESCE(a.timeslot,0)!=0
and (a.holidayfromannualleave is true or a.fromannualleavetime>0 or r.includeDayOffs is true or a.dayoff is not true));

update "anv".sickrequestduration s set day=0 where id in (select sd.id
from "anv".sickrequest sr
join "anv".leave_reason r on sr.reason_code=r.code
join "anv".sickrequestduration sd on sr.id=sd.sickrequestid
join "anv".attendancerawdata a on sr.employeeid=a.employeeid and sd.date=a.date
where s.id=sd.id
and a.dayoff is true and a.holidayfromannualleave is not true and a.fromannualleavetime=0 and r.includeDayOffs is not true);

--update holidays
update "anv".sickrequestduration s set holiday=(select distinct a.holiday
from "anv".sickrequest sr
join "anv".sickrequestduration sd on sr.id=sd.sickrequestid
join "anv".attendancerawdata a on sr.employeeid=a.employeeid and sd.date=a.date
where s.id=sd.id)
where id in (select sd.id
from "anv".sickrequest sr
join "anv".sickrequestduration sd on sr.id=sd.sickrequestid
join "anv".attendancerawdata a on sr.employeeid=a.employeeid and sd.date=a.date
where s.id=sd.id);

update "anv".sickrequestduration s set holidayfromannualleave=(select distinct a.holidayfromannualleave
from "anv".sickrequest sr
join "anv".sickrequestduration sd on sr.id=sd.sickrequestid
join "anv".attendancerawdata a on sr.employeeid=a.employeeid and sd.date=a.date
where s.id=sd.id)
where id in (select sd.id
from "anv".sickrequest sr
join "anv".sickrequestduration sd on sr.id=sd.sickrequestid
join "anv".attendancerawdata a on sr.employeeid=a.employeeid and sd.date=a.date
where s.id=sd.id);

--update timeslots
update "anv".sickrequestduration s set timeslot=(select max(distinct a.timeslot)
from "anv".sickrequest sr
join "anv".sickrequestduration sd on sr.id=sd.sickrequestid
join "anv".attendancerawdata a on sr.employeeid=a.employeeid and sd.date=a.date
where s.id=sd.id and a.timeslot is not null)
where id in (select sd.id
from "anv".sickrequest sr
join "anv".sickrequestduration sd on sr.id=sd.sickrequestid
join "anv".attendancerawdata a on sr.employeeid=a.employeeid and sd.date=a.date
where s.id=sd.id and a.timeslot is not null);

update "anv".sickrequestduration s set isPaid=(select distinct paidtime>0
from "anv".sickrequest sr
join "anv".sickrequestduration sd on sr.id=sd.sickrequestid
join "anv".attendancerawdata a on sr.employeeid=a.employeeid and sd.date=a.date
where a.leave=a.paidtime and s.id=sd.id limit 1)
where id in (select sd.id
from "anv".sickrequest sr
join "anv".sickrequestduration sd on sr.id=sd.sickrequestid
join "anv".attendancerawdata a on sr.employeeid=a.employeeid and sd.date=a.date
where a.leave=a.paidtime and s.id=sd.id);
