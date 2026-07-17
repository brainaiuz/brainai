insert into permission (code, context, name, sorder, parent, modulecode)
  values('ACCOUNTING_VARIATION_ADD', 'ACCOUNTING', 'Variation Add', 5, (SELECT id from permission where code='ACCOUNTING_PRODUCT_LIST'), 'PRODUCTS_SERVICES');
insert into permission (code, context, name, sorder, parent, modulecode)
  values('ACCOUNTING_VARIATION_DELETE', 'ACCOUNTING', 'Variation Delete', 6, (SELECT id from permission where code='ACCOUNTING_PRODUCT_LIST'), 'PRODUCTS_SERVICES');


--PRODUCT
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_VARIATION_ADD', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_VARIATION_ADD', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_VARIATION_ADD', 'ACCOUNTANT', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_VARIATION_DELETE', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_VARIATION_DELETE', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_VARIATION_DELETE', 'ACCOUNTANT', 'ALLOW');

--PRODUCT
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_VARIATION_ADD', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_VARIATION_ADD', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_VARIATION_ADD', 'ACCOUNTANT', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_VARIATION_DELETE', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_VARIATION_DELETE', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_VARIATION_DELETE', 'ACCOUNTANT', 'ALLOW');

insert into "anv".permission_context (permissioncode, contextcode)
select 'ACCOUNTING_VARIATION_ADD', 'ACCOUNTING'
from permission
where not exists(select permissioncode
                 from "anv".permission_context
                 where permissioncode = 'ACCOUNTING_VARIATION_ADD'
                   and contextcode = 'ACCOUNTING') limit 1;


insert into "0".permission_context (permissioncode, contextcode)
select 'ACCOUNTING_VARIATION_DELETE', 'ACCOUNTING'
from permission
where not exists(select permissioncode
                 from "0".permission_context
                 where permissioncode = 'ACCOUNTING_VARIATION_DELETE'
                   and contextcode = 'ACCOUNTING') limit 1;
