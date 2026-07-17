delete from permission where code='CRM_COPY_CONTACT';
insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
values ('CRM_COPY_CONTACT', 'CRM', 'f', 'Contact Copy', (select max(sorder)+1 from permission where parent = (select id from permission where code='CRM_CONTACTS_LIST')), (select id from permission where code='CRM_CONTACTS_LIST'), 'CONTACT_MANAGEMENT');

delete from "anv".permission_context where permissioncode = 'CRM_COPY_CONTACT';
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_COPY_CONTACT', 'CRM');

delete from "anv".rolepermission where permissioncode = 'CRM_COPY_CONTACT';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_COPY_CONTACT', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_COPY_CONTACT', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_COPY_CONTACT', 'ALLOW', 'SALESPERSON');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_COPY_CONTACT', 'ALLOW', 'SALESMAN');




