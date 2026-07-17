insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
values ('HRMS_SHOW_IN_CANDIDATE_OWNER', 'HRMS', 'f', 'Show in Candidate Owner', 4, (select id from permission where code='HRMS_CANDIDATE_LIST_VIEW'), 'RECRUITMENT_SYSTEM');

insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_SHOW_IN_CANDIDATE_OWNER', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_SHOW_IN_CANDIDATE_OWNER', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_SHOW_IN_CANDIDATE_OWNER', 'HR', 'ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_SHOW_IN_CANDIDATE_OWNER', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_SHOW_IN_CANDIDATE_OWNER', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_SHOW_IN_CANDIDATE_OWNER', 'HR', 'ALLOW');
