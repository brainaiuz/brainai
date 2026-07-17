
delete from permission where code='CRM_SALES_ORDER_ADD';
delete from permission where code='CRM_SALES_ORDER_EDIT';
delete from permission where code='CRM_SALES_ORDER_SUMMARY';
delete from permission where code='CRM_SALES_ORDER_DELETE';
delete from permission where code='CRM_SALES_ORDER_PDF';
insert into permission (code, context, name, sorder, parent, modulecode)
values
       ('CRM_SALES_ORDER_ADD', 'CRM', 'Add', 1, (select id from permission where code = 'CRM_SALES_ORDER_LIST'),'SALES_ORDERS'),
       ('CRM_SALES_ORDER_EDIT', 'CRM', 'Edit', 2, (select id from permission where code = 'CRM_SALES_ORDER_LIST'),'SALES_ORDERS'),
       ('CRM_SALES_ORDER_DELETE', 'CRM', 'Delete', 3, (select id from permission where code = 'CRM_SALES_ORDER_LIST'),'SALES_ORDERS'),
       ('CRM_SALES_ORDER_SUMMARY', 'CRM', 'Summary', 4, (select id from permission where code = 'CRM_SALES_ORDER_LIST'),'SALES_ORDERS'),
       ('CRM_SALES_ORDER_PDF', 'CRM', 'PDF', 5, (select id from permission where code = 'CRM_SALES_ORDER_LIST'),'SALES_ORDERS');


delete from "anv".permission_context where permissioncode = 'CRM_SALES_ORDER_ADD';
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_SALES_ORDER_ADD', 'CRM');

delete from "anv".permission_context where permissioncode = 'CRM_SALES_ORDER_EDIT';
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_SALES_ORDER_EDIT', 'CRM');

delete from "anv".permission_context where permissioncode = 'CRM_SALES_ORDER_SUMMARY';
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_SALES_ORDER_SUMMARY', 'CRM');

delete from "anv".permission_context where permissioncode = 'CRM_SALES_ORDER_DELETE';
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_SALES_ORDER_DELETE', 'CRM');

delete from "anv".permission_context where permissioncode = 'CRM_SALES_ORDER_PDF';
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_SALES_ORDER_PDF', 'CRM');


delete from "anv".rolepermission where permissioncode = 'CRM_SALES_ORDER_ADD';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_SALES_ORDER_ADD', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_SALES_ORDER_ADD', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_SALES_ORDER_ADD', 'ALLOW', 'ACCOUNTANT');

delete from "anv".rolepermission where permissioncode = 'CRM_SALES_ORDER_EDIT';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_SALES_ORDER_EDIT', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_SALES_ORDER_EDIT', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_SALES_ORDER_EDIT', 'ALLOW', 'ACCOUNTANT');

delete from "anv".rolepermission where permissioncode = 'CRM_SALES_ORDER_SUMMARY';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_SALES_ORDER_SUMMARY', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_SALES_ORDER_SUMMARY', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_SALES_ORDER_SUMMARY', 'ALLOW', 'ACCOUNTANT');

delete from "anv".rolepermission where permissioncode = 'CRM_SALES_ORDER_DELETE';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_SALES_ORDER_DELETE', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_SALES_ORDER_DELETE', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_SALES_ORDER_DELETE', 'ALLOW', 'ACCOUNTANT');

delete from "anv".rolepermission where permissioncode = 'CRM_SALES_ORDER_PDF';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_SALES_ORDER_PDF', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_SALES_ORDER_PDF', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_SALES_ORDER_PDF', 'ALLOW', 'ACCOUNTANT');
