DELETE from "0".mymodule WHERE code='CRM_CONTACT_CATEGORY';
INSERT INTO "0".mymodule(code) VALUES ('CRM_CONTACT_CATEGORY');

DELETE from "anv".mymodule WHERE code='CRM_CONTACT_CATEGORY';
INSERT INTO "anv".mymodule(code) VALUES ('CRM_CONTACT_CATEGORY');

DELETE from "0".permission_context where permissioncode = 'CRM_CONTACT_CATEGORY_LIST';
DELETE from "0".permission_context where permissioncode='CRM_CONTACT_CATEGORY_ADD';
DELETE from "0".permission_context where permissioncode='CRM_CONTACT_CATEGORY_EDIT';
DELETE from "0".permission_context where permissioncode='CRM_CONTACT_CATEGORY_DELETE';
DELETE from "0".permission_context where permissioncode='CRM_CONTACT_CATEGORY_MOVE';
DELETE from "0".permission_context where permissioncode='CRM_CONTACT_CATEGORY_COPY';
DELETE from "0".permission_context where permissioncode='CRM_COPY_TO_CATEGORY';
DELETE from "0".permission_context where permissioncode='CRM_MOVE_TO_CATEGORY';
DELETE from "0".permission_context where permissioncode='CRM_CONTACT_CATEGORY_SHARE';


DELETE from "anv".permission_context where permissioncode='CRM_CONTACT_CATEGORY_LIST';
DELETE from "anv".permission_context where permissioncode='CRM_CONTACT_CATEGORY_ADD';
DELETE from "anv".permission_context where permissioncode='CRM_CONTACT_CATEGORY_EDIT';
DELETE from "anv".permission_context where permissioncode='CRM_CONTACT_CATEGORY_DELETE';
DELETE from "anv".permission_context where permissioncode='CRM_CONTACT_CATEGORY_MOVE';
DELETE from "anv".permission_context where permissioncode='CRM_CONTACT_CATEGORY_COPY';
DELETE from "anv".permission_context where permissioncode='CRM_COPY_TO_CATEGORY';
DELETE from "anv".permission_context where permissioncode='CRM_MOVE_TO_CATEGORY';
DELETE from "anv".permission_context where permissioncode='CRM_CONTACT_CATEGORY_SHARE';


DELETE FROM permission where code='CRM_CONTACT_CATEGORY_LIST';
DELETE FROM permission where code='CRM_CONTACT_CATEGORY_ADD';
DELETE FROM permission where code='CRM_CONTACT_CATEGORY_EDIT';
DELETE FROM permission where code='CRM_CONTACT_CATEGORY_DELETE';
DELETE FROM permission where code='CRM_CONTACT_CATEGORY_MOVE';
DELETE FROM permission where code='CRM_CONTACT_CATEGORY_COPY';
DELETE FROM permission where code='CRM_COPY_TO_CATEGORY';
DELETE FROM permission where code='CRM_MOVE_TO_CATEGORY';
DELETE FROM permission where code='CRM_CONTACT_CATEGORY_SHARE';


DELETE FROM "0".rolepermission where permissioncode='CRM_CONTACT_CATEGORY_LIST';
DELETE FROM "0".rolepermission where permissioncode='CRM_CONTACT_CATEGORY_ADD';
DELETE FROM "0".rolepermission where permissioncode='CRM_CONTACT_CATEGORY_EDIT';
DELETE FROM "0".rolepermission where permissioncode='CRM_CONTACT_CATEGORY_DELETE';
DELETE FROM "0".rolepermission where permissioncode='CRM_CONTACT_CATEGORY_MOVE';
DELETE FROM "0".rolepermission where permissioncode='CRM_CONTACT_CATEGORY_COPY';
DELETE FROM "0".rolepermission where permissioncode='CRM_COPY_TO_CATEGORY';
DELETE FROM "0".rolepermission where permissioncode='CRM_MOVE_TO_CATEGORY';
DELETE FROM "0".rolepermission where permissioncode='CRM_CONTACT_CATEGORY_SHARE';

DELETE FROM "anv".rolepermission where permissioncode='CRM_CONTACT_CATEGORY_LIST';
DELETE FROM "anv".rolepermission where permissioncode='CRM_CONTACT_CATEGORY_ADD';
DELETE FROM "anv".rolepermission where permissioncode='CRM_CONTACT_CATEGORY_EDIT';
DELETE FROM "anv".rolepermission where permissioncode='CRM_CONTACT_CATEGORY_DELETE';
DELETE FROM "anv".rolepermission where permissioncode='CRM_CONTACT_CATEGORY_MOVE';
DELETE FROM "anv".rolepermission where permissioncode='CRM_CONTACT_CATEGORY_COPY';
DELETE FROM "anv".rolepermission where permissioncode='CRM_COPY_TO_CATEGORY';
DELETE FROM "anv".rolepermission where permissioncode='CRM_MOVE_TO_CATEGORY';
DELETE FROM "anv".rolepermission where permissioncode='CRM_CONTACT_CATEGORY_SHARE';



---LIST
insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
values ('CRM_CONTACT_CATEGORY_LIST', 'SETTINGS', 'f', 'Contact Category List', (select max(sorder)+1 from permission where parent = (select id from permission where code='SETTINGS_MAIN_MENU')), (select id from permission where code='SETTINGS_MAIN_MENU'), 'CRM_CONTACT_CATEGORY');

---ADD
insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
values ('CRM_CONTACT_CATEGORY_ADD', 'SETTINGS', 'f', 'Contact Category Add', 1, (select id from permission where code='CRM_CONTACT_CATEGORY_LIST'), 'CRM_CONTACT_CATEGORY');

---EDIT
insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
values ('CRM_CONTACT_CATEGORY_EDIT', 'SETTINGS', 'f', 'Contact Category Edit', 2, (select id from permission where code='CRM_CONTACT_CATEGORY_LIST'), 'CRM_CONTACT_CATEGORY');

---DELETE
insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
values ('CRM_CONTACT_CATEGORY_DELETE', 'SETTINGS', 'f', 'Contact Category Delete', 3, (select id from permission where code='CRM_CONTACT_CATEGORY_LIST'), 'CRM_CONTACT_CATEGORY');

---MOVE
insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
values ('CRM_CONTACT_CATEGORY_MOVE', 'SETTINGS', 'f', 'Contact Category Move', 4, (select id from permission where code='CRM_CONTACT_CATEGORY_LIST'), 'CRM_CONTACT_CATEGORY');

---COPY
insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
values ('CRM_CONTACT_CATEGORY_COPY', 'SETTINGS', 'f', 'Contact Category Copy', 5, (select id from permission where code='CRM_CONTACT_CATEGORY_LIST'), 'CRM_CONTACT_CATEGORY');

---SHATE
insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
values ('CRM_CONTACT_CATEGORY_SHARE', 'SETTINGS', 'f', 'Contact Category Share', 6, (select id from permission where code='CRM_CONTACT_CATEGORY_LIST'), 'CRM_CONTACT_CATEGORY');

--For zero
insert into "0".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_LIST', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_LIST', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_LIST', 'SALESMAN', 'ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_ADD', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_ADD', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_ADD', 'SALESMAN', 'ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_EDIT', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_EDIT', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_EDIT', 'SALESMAN', 'ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_DELETE', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_DELETE', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_DELETE', 'SALESMAN', 'ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_MOVE', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_MOVE', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_MOVE', 'SALESMAN', 'ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_COPY', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_COPY', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_COPY', 'SALESMAN', 'ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_SHARE', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_SHARE', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_SHARE', 'SALESMAN', 'ALLOW');



---For all schemas
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_LIST', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_LIST', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_LIST', 'SALESMAN', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_ADD', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_ADD', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_ADD', 'SALESMAN', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_EDIT', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_EDIT', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_EDIT', 'SALESMAN', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_DELETE', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_DELETE', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_DELETE', 'SALESMAN', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_MOVE', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_MOVE', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_MOVE', 'SALESMAN', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_COPY', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_COPY', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_COPY', 'SALESMAN', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_SHARE', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_SHARE', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_CONTACT_CATEGORY_SHARE', 'SALESMAN', 'ALLOW');


INSERT INTO "0".permission_context (permissioncode,contextcode) VALUES ('CRM_CONTACT_CATEGORY_LIST','SETTINGS');
INSERT INTO "0".permission_context (permissioncode,contextcode) VALUES ('CRM_CONTACT_CATEGORY_ADD','SETTINGS');
INSERT INTO "0".permission_context (permissioncode,contextcode) VALUES ('CRM_CONTACT_CATEGORY_EDIT','SETTINGS');
INSERT INTO "0".permission_context (permissioncode,contextcode) VALUES ('CRM_CONTACT_CATEGORY_DELETE','SETTINGS');
INSERT INTO "0".permission_context (permissioncode,contextcode) VALUES ('CRM_CONTACT_CATEGORY_MOVE','SETTINGS');
INSERT INTO "0".permission_context (permissioncode,contextcode) VALUES ('CRM_CONTACT_CATEGORY_COPY','SETTINGS');
INSERT INTO "0".permission_context (permissioncode,contextcode) VALUES ('CRM_CONTACT_CATEGORY_SHARE','SETTINGS');


INSERT INTO "anv".permission_context (permissioncode,contextcode) VALUES ('CRM_CONTACT_CATEGORY_LIST','SETTINGS');
INSERT INTO "anv".permission_context (permissioncode,contextcode) VALUES ('CRM_CONTACT_CATEGORY_ADD','SETTINGS');
INSERT INTO "anv".permission_context (permissioncode,contextcode) VALUES ('CRM_CONTACT_CATEGORY_EDIT','SETTINGS');
INSERT INTO "anv".permission_context (permissioncode,contextcode) VALUES ('CRM_CONTACT_CATEGORY_DELETE','SETTINGS');
INSERT INTO "anv".permission_context (permissioncode,contextcode) VALUES ('CRM_CONTACT_CATEGORY_MOVE','SETTINGS');
INSERT INTO "anv".permission_context (permissioncode,contextcode) VALUES ('CRM_CONTACT_CATEGORY_COPY','SETTINGS');
INSERT INTO "anv".permission_context (permissioncode,contextcode) VALUES ('CRM_CONTACT_CATEGORY_SHARE','SETTINGS');


