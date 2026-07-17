insert into "anv".permission_context (permissioncode, contextcode)
select 'HRMS_ANNUAL_ALLOWANCE', 'SETTINGS'
from permission
where not exists(select permissioncode
                 from "anv".permission_context
                 where permissioncode = 'HRMS_ANNUAL_ALLOWANCE'
                   and contextcode = 'SETTINGS') limit 1;

insert into "0".permission_context (permissioncode, contextcode)
select 'HRMS_ANNUAL_ALLOWANCE', 'SETTINGS'
from permission
where not exists(select permissioncode
                 from "0".permission_context
                 where permissioncode = 'HRMS_ANNUAL_ALLOWANCE'
                   and contextcode = 'SETTINGS') limit 1;