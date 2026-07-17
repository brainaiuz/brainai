delete
from "anv".permission_context
where permissioncode = 'HRMS_LOCATION'
  and contextcode = 'SETTINGS';
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_LOCATION', 'SETTINGS');

