
update permission set  name='Add', sorder = 1 where parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST') and code='ACCOUNTING_PRODUCT_ADD';
update permission set  name='Group Add', sorder = 3 where parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST') and code='ACCOUNTING_PRODUCT_GROUP_ADD';
update permission set  name='Edit', sorder = 4 where parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST') and code='ACCOUNTING_PRODUCT_EDIT';
update permission set  name='Delete', sorder = 5 where parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST') and code='ACCOUNTING_PRODUCT_DELETE';
update permission set  name='Summary', sorder = 6 where parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST') and code='ACCOUNTING_PRODUCT_SUMMARY';
update permission set  name='Variation Add', sorder = 7 where parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST') and code='ACCOUNTING_VARIATION_ADD';
update permission set  name='Variation Delete', sorder = 8 where parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST') and code='ACCOUNTING_VARIATION_DELETE';
update permission set  name='Build Assembly', sorder = 9 where parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST') and code='ACCOUNTING_BUILD_ASSEMBLY';
update permission set  name='Cost', sorder = 10 where parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST') and code='ACCOUNTING_PRODUCT_COST';


delete from permission where code='ACCOUNTING_PRODUCT_QUICK_ADD';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('ACCOUNTING_PRODUCT_QUICK_ADD', 'ACCOUNTING', 'Quick Add', 2,
        (select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'),'PRODUCTS_SERVICES');

delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PRODUCT_QUICK_ADD';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_PRODUCT_QUICK_ADD', 'ACCOUNTING');

delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_PRODUCT_QUICK_ADD';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_PRODUCT_QUICK_ADD', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_PRODUCT_QUICK_ADD', 'ALLOW', 'SALESMAN');




update permission set  name='Add', sorder = 1 where parent=(select id from permission where code = 'LOGISTICS_PRODUCT_LIST') and code='LOGISTICS_PRODUCT_ADD';
update permission set  name='Edit', sorder = 3 where parent=(select id from permission where code = 'LOGISTICS_PRODUCT_LIST') and code='LOGISTICS_PRODUCT_EDIT';
update permission set  name='Delete', sorder = 4 where parent=(select id from permission where code = 'LOGISTICS_PRODUCT_LIST') and code='LOGISTICS_PRODUCT_DELETE';
update permission set  name='Summary', sorder = 5 where parent=(select id from permission where code = 'LOGISTICS_PRODUCT_LIST') and code='LOGISTICS_PRODUCT_SUMMARY';
update permission set  name='Variation Add', sorder = 6 where parent=(select id from permission where code = 'LOGISTICS_PRODUCT_LIST') and code='LOGISTICS_VARIATION_ADD';
update permission set  name='Variation Delete', sorder = 7 where parent=(select id from permission where code = 'LOGISTICS_PRODUCT_LIST') and code='LOGISTICS_VARIATION_DELETE';
update permission set  name='Build Assembly', sorder = 8 where parent=(select id from permission where code = 'LOGISTICS_PRODUCT_LIST') and code='LOGISTICS_BUILD_ASSEMBLY';
update permission set  name='Cost', sorder = 9 where parent=(select id from permission where code = 'LOGISTICS_PRODUCT_LIST') and code='LOGISTICS_PRODUCT_COST';


delete from permission where code='LOGISTICS_PRODUCT_QUICK_ADD';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('LOGISTICS_PRODUCT_QUICK_ADD', 'LOGISTICS', 'Quick Add', 2,
        (select id from permission where code = 'LOGISTICS_PRODUCT_LIST'),'LOGISTICS_MODULE');

delete from "anv".permission_context where permissioncode = 'LOGISTICS_PRODUCT_QUICK_ADD';
insert into "anv".permission_context (permissioncode, contextcode) values ('LOGISTICS_PRODUCT_QUICK_ADD', 'LOGISTICS');

delete from "anv".rolepermission where permissioncode = 'LOGISTICS_PRODUCT_QUICK_ADD';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('LOGISTICS_PRODUCT_QUICK_ADD', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('LOGISTICS_PRODUCT_QUICK_ADD', 'ALLOW', 'SALESMAN');