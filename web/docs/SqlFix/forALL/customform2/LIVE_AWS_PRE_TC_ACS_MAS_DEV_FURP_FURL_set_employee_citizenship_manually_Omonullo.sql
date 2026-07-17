update "anv".employee e set citizenship = ep.countryid
from "anv".employeeprofile ep
where e.profileid = ep.id
and citizenship is null;