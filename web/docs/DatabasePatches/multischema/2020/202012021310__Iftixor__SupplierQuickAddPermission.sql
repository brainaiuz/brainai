
insert into permission (code, context, name, sorder, parent, modulecode)
values('ACCOUNTING_SUPPLIER_QUICK_ADD', 'ACCOUNTING', 'Quick Add', 5, (SELECT id from permission where code='ACCOUNTING_SUPPLIER_LIST'), 'SUPPLIER_CENTER');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_SUPPLIER_QUICK_ADD', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_SUPPLIER_QUICK_ADD', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_SUPPLIER_QUICK_ADD', 'ACCOUNTANT', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_SUPPLIER_QUICK_ADD', 'SALESMAN', 'ALLOW');

insert into "anv".permission_context (permissioncode, contextcode)
select 'ACCOUNTING_SUPPLIER_QUICK_ADD', 'ACCOUNTING'
from permission
where not exists(select permissioncode
                 from "anv".permission_context
                 where permissioncode = 'ACCOUNTING_SUPPLIER_QUICK_ADD'
                   and contextcode = 'ACCOUNTING') limit 1;


