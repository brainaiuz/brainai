
--summary account
delete from permission where code='CRM_ACCOUNTS_SUMMARY';
insert into permission (code, context, name, sorder, parent, modulecode) values ('CRM_ACCOUNTS_SUMMARY', 'CRM', 'Summary',
                                                                                 (SELECT max(sorder) + 1 from permission WHERE code = 'CRM_ACCOUNTS_LIST'),
                                                                                 (select id from permission where code = 'CRM_ACCOUNTS_LIST'), 'CRM_MODULE');

delete from "anv".permission_context where permissioncode = 'CRM_ACCOUNTS_SUMMARY';
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_ACCOUNTS_SUMMARY', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_ACCOUNTS_SUMMARY', 'CRM');

delete from "anv".rolepermission where permissioncode = 'CRM_ACCOUNTS_SUMMARY';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_ACCOUNTS_SUMMARY', 'ALLOW', 'ADMIN'),
                                                                           ('CRM_ACCOUNTS_SUMMARY', 'ALLOW', 'ACCOUNTANT'),
                                                                           ('CRM_ACCOUNTS_SUMMARY', 'ALLOW', 'SALESMAN'),
                                                                           ('CRM_ACCOUNTS_SUMMARY', 'ALLOW', 'SALESPERSON'),
                                                                           ('CRM_ACCOUNTS_SUMMARY', 'ALLOW', 'DR'),
                                                                           ('CRM_ACCOUNTS_SUMMARY', 'ALLOW', 'CUSTOMER_SERVICE_REPRESENTATIVE'),
                                                                           ('CRM_ACCOUNTS_SUMMARY', 'ALLOW', 'PM'),
                                                                           ('CRM_ACCOUNTS_SUMMARY', 'ALLOW', 'TL');


--summary contact
delete from permission where code='CRM_CONTACTS_SUMMARY';
insert into permission (code, context, name, sorder, parent, modulecode) values ('CRM_CONTACTS_SUMMARY', 'CRM', 'Summary',
                                                                                 (SELECT max(sorder) + 1 from permission WHERE code = 'CRM_CONTACTS_LIST'),
                                                                                 (select id from permission where code = 'CRM_CONTACTS_LIST'), 'CONTACT_MANAGEMENT');

delete from "anv".permission_context where permissioncode = 'CRM_CONTACTS_SUMMARY';
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_CONTACTS_SUMMARY', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_CONTACTS_SUMMARY', 'CRM');
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_CONTACTS_SUMMARY', 'PM');

delete from "anv".rolepermission where permissioncode = 'CRM_CONTACTS_SUMMARY';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_CONTACTS_SUMMARY', 'ALLOW', 'ADMIN'),
                                                                           ('CRM_CONTACTS_SUMMARY', 'ALLOW', 'ACCOUNTANT'),
                                                                           ('CRM_CONTACTS_SUMMARY', 'ALLOW', 'SALESMAN'),
                                                                           ('CRM_CONTACTS_SUMMARY', 'ALLOW', 'SALESPERSON'),
                                                                           ('CRM_CONTACTS_SUMMARY', 'ALLOW', 'DR'),
                                                                           ('CRM_CONTACTS_SUMMARY', 'ALLOW', 'CUSTOMER_SERVICE_REPRESENTATIVE'),
                                                                           ('CRM_CONTACTS_SUMMARY', 'ALLOW', 'PM'),
                                                                           ('CRM_CONTACTS_SUMMARY', 'ALLOW', 'TL');
