
delete from permission where code = 'CRM_SUPPLIER_ADD';
delete from permission where code = 'CRM_CLIENT_ADD';

update permission set  parent=(select id from permission where code='CRM_ACCOUNTS_LIST') where code='CRM_ACCOUNT_NUMBER_EDIT';
update permission set  parent=(select id from permission where code='CRM_ACCOUNTS_LIST') where code='CRM_ACCOUNT_OWNER_EDIT';


delete from permission where code='CRM_ACCOUNT_QUICK_ADD';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('CRM_ACCOUNT_QUICK_ADD', 'CRM', 'Quick Add', 3,
        (select id from permission where code = 'CRM_ACCOUNTS_LIST'),'CRM_MODULE');


delete from "anv".permission_context where permissioncode = 'CRM_ACCOUNT_QUICK_ADD';
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_ACCOUNT_QUICK_ADD', 'CRM');


delete from "anv".rolepermission where permissioncode = 'CRM_ACCOUNT_QUICK_ADD';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_ACCOUNT_QUICK_ADD', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_ACCOUNT_QUICK_ADD', 'ALLOW', 'SALESMAN');