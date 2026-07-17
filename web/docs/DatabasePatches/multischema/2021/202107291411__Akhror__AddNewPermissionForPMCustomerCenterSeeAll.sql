delete
from permission
where code = 'PM_SEE_ALL_CUSTOMERS_LIST';
insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('PM_SEE_ALL_CUSTOMERS_LIST', 'PM', false, 'See All', 50,
        (select id from permission where code = 'PM_CUSTOMER_LIST'), true, 'CUSTOMER_CENTER');

delete
from "anv".permission_context
where permissioncode = 'PM_SEE_ALL_CUSTOMERS_LIST';
insert into "anv".permission_context(permissioncode, contextcode)
values ('PM_SEE_ALL_CUSTOMERS_LIST', 'PM');
