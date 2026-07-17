delete from permission where code='POSITION_LIST_BY_LOCATION';
insert into permission (code, context, name, parent, modulecode)
values ('POSITION_LIST_BY_LOCATION', 'SETTINGS', 'Position List By Location',(select id from permission where code = 'HRMS_POSITION'),
        'HRMS_MODULE');

delete from "anv".permission_context where permissioncode = 'POSITION_LIST_BY_LOCATION';
insert into "anv".permission_context (permissioncode, contextcode)
values ('POSITION_LIST_BY_LOCATION', 'SETTINGS');




delete from permission where code='POSITION_LIST_SEE_ALL';
insert into permission (code, context, name, parent, modulecode,sorder)
values ('POSITION_LIST_SEE_ALL', 'SETTINGS', 'See all',(select id from permission where code = 'HRMS_POSITION'),
        'HRMS_MODULE',(select sorder from permission where code = 'HRMS_POSITION_RATES'));

delete from "anv".permission_context where permissioncode = 'POSITION_LIST_SEE_ALL';
insert into "anv".permission_context (permissioncode, contextcode)
values ('POSITION_LIST_SEE_ALL', 'SETTINGS');


INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('POSITION_LIST_SEE_ALL', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('POSITION_LIST_SEE_ALL', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('POSITION_LIST_SEE_ALL', 'ALLOW','HR');







