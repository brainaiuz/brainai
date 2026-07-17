

DELETE from "0".permission_context where permissioncode='CRM_CONTACT_CATEGORY_MOVE';
DELETE from "0".permission_context where permissioncode='CRM_CONTACT_CATEGORY_COPY';
DELETE from "0".permission_context where permissioncode='CRM_COPY_TO_CATEGORY';
DELETE from "0".permission_context where permissioncode='CRM_MOVE_TO_CATEGORY';

DELETE from "anv".permission_context where permissioncode='CRM_CONTACT_CATEGORY_MOVE';
DELETE from "anv".permission_context where permissioncode='CRM_CONTACT_CATEGORY_COPY';
DELETE from "anv".permission_context where permissioncode='CRM_COPY_TO_CATEGORY';
DELETE from "anv".permission_context where permissioncode='CRM_MOVE_TO_CATEGORY';


DELETE FROM permission where code='CRM_CONTACT_CATEGORY_MOVE';
DELETE FROM permission where code='CRM_CONTACT_CATEGORY_COPY';
DELETE FROM permission where code='CRM_COPY_TO_CATEGORY';
DELETE FROM permission where code='CRM_MOVE_TO_CATEGORY';


DELETE FROM "0".rolepermission where permissioncode='CRM_CONTACT_CATEGORY_MOVE';
DELETE FROM "0".rolepermission where permissioncode='CRM_CONTACT_CATEGORY_COPY';
DELETE FROM "0".rolepermission where permissioncode='CRM_COPY_TO_CATEGORY';
DELETE FROM "0".rolepermission where permissioncode='CRM_MOVE_TO_CATEGORY';

DELETE FROM "anv".rolepermission where permissioncode='CRM_CONTACT_CATEGORY_MOVE';
DELETE FROM "anv".rolepermission where permissioncode='CRM_CONTACT_CATEGORY_COPY';
DELETE FROM "anv".rolepermission where permissioncode='CRM_COPY_TO_CATEGORY';
DELETE FROM "anv".rolepermission where permissioncode='CRM_MOVE_TO_CATEGORY';



---MOVE
insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
values ('CRM_CONTACT_CATEGORY_MOVE', 'CRM', 'f', 'Contact Category Move', (select max(sorder)+1 from permission where parent = (select id from permission where code='CRM_CONTACTS_LIST')), (select id from permission where code='CRM_CONTACTS_LIST'), 'CONTACT_MANAGEMENT');

---COPY
insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
values ('CRM_CONTACT_CATEGORY_COPY', 'CRM', 'f', 'Contact Category Copy', (select max(sorder)+1 from permission where parent = (select id from permission where code='CRM_CONTACTS_LIST')), (select id from permission where code='CRM_CONTACTS_LIST'), 'CONTACT_MANAGEMENT');


--For zero

insert into "0".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_MOVE', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_MOVE', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_MOVE', 'SALESMAN', 'ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_COPY', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_COPY', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_COPY', 'SALESMAN', 'ALLOW');


---For all schemas

insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_MOVE', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_MOVE', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_MOVE', 'SALESMAN', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_COPY', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_COPY', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_COPY', 'SALESMAN', 'ALLOW');


INSERT INTO "0".permission_context (permissioncode,contextcode) VALUES ('CRM_CONTACT_CATEGORY_MOVE','CRM');
INSERT INTO "0".permission_context (permissioncode,contextcode) VALUES ('CRM_CONTACT_CATEGORY_COPY','CRM');


INSERT INTO "anv".permission_context (permissioncode,contextcode) VALUES ('CRM_CONTACT_CATEGORY_MOVE','CRM');
INSERT INTO "anv".permission_context (permissioncode,contextcode) VALUES ('CRM_CONTACT_CATEGORY_COPY','CRM');


