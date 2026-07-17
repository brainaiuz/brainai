insert into "anv".permission_context (permissioncode, contextcode)
select 'ACCOUNTING_CURRENCY_RATE_EDIT', 'ACCOUNTING'
from permission
where not exists(select permissioncode
                 from "anv".permission_context
                 where permissioncode = 'ACCOUNTING_CURRENCY_RATE_EDIT'
                   and contextcode = 'ACCOUNTING') limit 1;

                   insert into "0".permission_context (permissioncode, contextcode)
select 'ACCOUNTING_CURRENCY_RATE_EDIT', 'ACCOUNTING'
from permission
where not exists(select permissioncode
                 from "0".permission_context
                 where permissioncode = 'ACCOUNTING_CURRENCY_RATE_EDIT'
                   and contextcode = 'ACCOUNTING') limit 1;