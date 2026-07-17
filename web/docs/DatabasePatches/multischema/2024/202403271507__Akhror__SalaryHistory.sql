delete
from "anv".companypayrollsettings
where key = 'ENABLED_SALARY_HISTORY';

delete
from "anv".salaryhistory;
insert into "anv".salaryhistory(employeeID, effectiveDate, salary)
select e.id, e.startdate, cast(s.value as numeric)
from "anv".employeepayrollsettings s
         left join "anv".employee e on s.employeeid = e.id
where s.key = 'SALARY'
  and s.value is not null;