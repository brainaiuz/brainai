insert into "anv".labour_period (employeeid,startDate,endDate)
select employeeid,startDate,endDate
from (select u.id employeeid,date(from_date) startDate,date(from_date)+ interval '1 year' - interval '1 day'  enddate
from "anv".myuser u
join "anv".employee e on u.id=e.id
join "anv".reference st on u.accountstatusid=st.id
join datejoin lb1 on date(e.startDate)=date(from_date)
where u.deleted is not true or st.code='RESIGNED_EMPLOYEE'
union
select u.id employeeid,date(from_date) startDate,date(from_date)+ interval '1 year' - interval '1 day'  enddate
from "anv".myuser u
join "anv".employee e on u.id=e.id
join "anv".reference st on u.accountstatusid=st.id
join datejoin lb1 on date(e.startDate+ interval '1 year')=date(from_date)
where u.deleted is not true or st.code='RESIGNED_EMPLOYEE'
union
select u.id employeeid,date(from_date) startDate,date(from_date)+ interval '1 year' - interval '1 day'  enddate
from "anv".myuser u
join "anv".employee e on u.id=e.id
join "anv".reference st on u.accountstatusid=st.id
join datejoin lb1 on date(e.startDate+ interval '2 year')=date(from_date)
where u.deleted is not true or st.code='RESIGNED_EMPLOYEE'
union
select u.id employeeid,date(from_date) startDate,date(from_date)+ interval '1 year' - interval '1 day'  enddate
from "anv".myuser u
join "anv".employee e on u.id=e.id
join "anv".reference st on u.accountstatusid=st.id
join datejoin lb1 on date(e.startDate+ interval '3 year')=date(from_date)
where u.deleted is not true or st.code='RESIGNED_EMPLOYEE'
union
select u.id employeeid,date(from_date) startDate,date(from_date)+ interval '1 year' - interval '1 day'  enddate
from "anv".myuser u
join "anv".employee e on u.id=e.id
join "anv".reference st on u.accountstatusid=st.id
join datejoin lb1 on date(e.startDate+ interval '4 year')=date(from_date)
where u.deleted is not true or st.code='RESIGNED_EMPLOYEE'
union
select u.id employeeid,date(from_date) startDate,date(from_date)+ interval '1 year' - interval '1 day'  enddate
from "anv".myuser u
join "anv".employee e on u.id=e.id
join "anv".reference st on u.accountstatusid=st.id
join datejoin lb1 on date(e.startDate+ interval '5 year')=date(from_date)
where u.deleted is not true or st.code='RESIGNED_EMPLOYEE'
union
select u.id employeeid,date(from_date) startDate,date(from_date)+ interval '1 year' - interval '1 day'  enddate
from "anv".myuser u
join "anv".employee e on u.id=e.id
join "anv".reference st on u.accountstatusid=st.id
join datejoin lb1 on date(e.startDate+ interval '6 year')=date(from_date)
where u.deleted is not true or st.code='RESIGNED_EMPLOYEE'
union
select u.id employeeid,date(from_date) startDate,date(from_date)+ interval '1 year' - interval '1 day'  enddate
from "anv".myuser u
join "anv".employee e on u.id=e.id
join "anv".reference st on u.accountstatusid=st.id
join datejoin lb1 on date(e.startDate+ interval '7 year')=date(from_date)
where u.deleted is not true or st.code='RESIGNED_EMPLOYEE'
union
select u.id employeeid,date(from_date) startDate,date(from_date)+ interval '1 year' - interval '1 day'  enddate
from "anv".myuser u
join "anv".employee e on u.id=e.id
join "anv".reference st on u.accountstatusid=st.id
join datejoin lb1 on date(e.startDate+ interval '8 year')=date(from_date)
where u.deleted is not true or st.code='RESIGNED_EMPLOYEE'
union
select u.id employeeid,date(from_date) startDate,date(from_date)+ interval '1 year' - interval '1 day'  enddate
from "anv".myuser u
join "anv".employee e on u.id=e.id
join "anv".reference st on u.accountstatusid=st.id
join datejoin lb1 on date(e.startDate+ interval '9 year')=date(from_date)
where u.deleted is not true or st.code='RESIGNED_EMPLOYEE'
union
select u.id employeeid,date(from_date) startDate,date(from_date)+ interval '1 year' - interval '1 day'  enddate
from "anv".myuser u
join "anv".employee e on u.id=e.id
join "anv".reference st on u.accountstatusid=st.id
join datejoin lb1 on date(e.startDate+ interval '10 year')=date(from_date)
where u.deleted is not true or st.code='RESIGNED_EMPLOYEE') t
