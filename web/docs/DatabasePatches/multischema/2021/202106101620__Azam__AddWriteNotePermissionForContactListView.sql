
delete from permission where code='CRM_CONTACT_WRITE_NOTE';
insert into permission (code, context, name, sorder, parent, modulecode) values
('CRM_CONTACT_WRITE_NOTE', 'CRM', 'Write A Note', 11, (select id from permission where code = 'CRM_CONTACTS_LIST'),'CONTACT_MANAGEMENT');

delete from "anv".permission_context where permissioncode = 'CRM_CONTACT_WRITE_NOTE';
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_CONTACT_WRITE_NOTE', 'CRM');

delete from "anv".rolepermission where permissioncode = 'CRM_CONTACT_WRITE_NOTE';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_CONTACT_WRITE_NOTE', 'ALLOW', 'DR'),
                                                                           ('CRM_CONTACT_WRITE_NOTE', 'ALLOW', 'ADMIN'),
                                                                           ('CRM_CONTACT_WRITE_NOTE', 'ALLOW', 'SALESMAN'),
                                                                           ('CRM_CONTACT_WRITE_NOTE', 'ALLOW', 'CUSTOMER_SERVICE_REPRESENTATIVE'),
                                                                           ('CRM_CONTACT_WRITE_NOTE', 'ALLOW', 'CUSTOMER_SERVICE_MANAGER'),
                                                                           ('CRM_CONTACT_WRITE_NOTE', 'ALLOW', 'ACCOUNTANT');