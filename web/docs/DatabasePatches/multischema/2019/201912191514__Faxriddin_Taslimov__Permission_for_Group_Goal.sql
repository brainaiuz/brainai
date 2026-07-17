

delete from permission where code in ('HRMS_GROUP_PERSONAL_GOALS','HRMS_GROUP_VIE_ALL_PERSONAL_GOALS',
'HRMS_ADD_NEW_GROUP_PERSONAL_GOALS','HRMS_GROUP_PERSONAL_GOAL_SUMMARY',
'HRMS_EDIT_GROUP_PERSONAL_GOAL','HRMS_GROUP_PERSONAL_GOAL_REMOVE');

insert into permission (code,           context,     ismainmenu,   name,                   sorder,  parent,                                                            iscore,  modulecode,         isadvancedmode)
values
('HRMS_GROUP_PERSONAL_GOALS',          'HRMS',       false,        'Group Goals',          14,     (select id from permission where code='HRMS_GOAL_MANAGEMENT_TAB'),  false,  'GOAL_MANAGEMENT',  false ),
('HRMS_GROUP_VIE_ALL_PERSONAL_GOALS',  'HRMS',       false,        'View all Group Goals', 14,     (select id from permission where code='HRMS_GROUP_PERSONAL_GOALS'), false,  'GOAL_MANAGEMENT',  false ),
('HRMS_ADD_NEW_GROUP_PERSONAL_GOALS',  'HRMS',       false,        'Add Group Goals',      14,     (select id from permission where code='HRMS_GROUP_PERSONAL_GOALS'), false,  'GOAL_MANAGEMENT', false ),
('HRMS_GROUP_PERSONAL_GOAL_SUMMARY',   'HRMS',       false,        'View Group Goals',     14,     (select id from permission where code='HRMS_GROUP_PERSONAL_GOALS'), false,  'GOAL_MANAGEMENT', false ),
('HRMS_EDIT_GROUP_PERSONAL_GOAL',      'HRMS',       false,        'Edit Group Goals',     14,     (select id from permission where code='HRMS_GROUP_PERSONAL_GOALS'), false,  'GOAL_MANAGEMENT', false ),
('HRMS_GROUP_PERSONAL_GOAL_REMOVE',    'HRMS',       false,        'Delete Group Goals',   14,     (select id from permission where code='HRMS_GROUP_PERSONAL_GOALS'), false,  'GOAL_MANAGEMENT', false );


insert into "0".permission_context (permissioncode,contextcode) values ('HRMS_GROUP_PERSONAL_GOALS','HRMS');
insert into "0".permission_context (permissioncode,contextcode) values ('HRMS_GROUP_VIE_ALL_PERSONAL_GOALS','HRMS');
insert into "0".permission_context (permissioncode,contextcode) values ('HRMS_ADD_NEW_GROUP_PERSONAL_GOALS','HRMS');
insert into "0".permission_context (permissioncode,contextcode) values ('HRMS_GROUP_PERSONAL_GOAL_SUMMARY','HRMS');
insert into "0".permission_context (permissioncode,contextcode) values ('HRMS_EDIT_GROUP_PERSONAL_GOAL','HRMS');
insert into "0".permission_context (permissioncode,contextcode) values ('HRMS_GROUP_PERSONAL_GOAL_REMOVE','HRMS');

insert into "0".rolepermission (permissioncode,access,rolecode) values ('HRMS_GROUP_PERSONAL_GOALS','ALLOW','ADMIN');
insert into "0".rolepermission (permissioncode,access,rolecode) values ('HRMS_GROUP_PERSONAL_GOALS','ALLOW','HR');
insert into "0".rolepermission (permissioncode,access,rolecode) values ('HRMS_GROUP_PERSONAL_GOALS','ALLOW','DR');

insert into "0".rolepermission (permissioncode,access,rolecode) values ('HRMS_GROUP_VIE_ALL_PERSONAL_GOALS','ALLOW','ADMIN');
insert into "0".rolepermission (permissioncode,access,rolecode) values ('HRMS_GROUP_VIE_ALL_PERSONAL_GOALS','ALLOW','HR');
insert into "0".rolepermission (permissioncode,access,rolecode) values ('HRMS_GROUP_VIE_ALL_PERSONAL_GOALS','ALLOW','DR');

insert into "0".rolepermission (permissioncode,access,rolecode) values ('HRMS_ADD_NEW_GROUP_PERSONAL_GOALS','ALLOW','ADMIN');
insert into "0".rolepermission (permissioncode,access,rolecode) values ('HRMS_ADD_NEW_GROUP_PERSONAL_GOALS','ALLOW','HR');
insert into "0".rolepermission (permissioncode,access,rolecode) values ('HRMS_ADD_NEW_GROUP_PERSONAL_GOALS','ALLOW','DR');

insert into "0".rolepermission (permissioncode,access,rolecode) values ('HRMS_GROUP_PERSONAL_GOAL_SUMMARY','ALLOW','ADMIN');
insert into "0".rolepermission (permissioncode,access,rolecode) values ('HRMS_GROUP_PERSONAL_GOAL_SUMMARY','ALLOW','HR');
insert into "0".rolepermission (permissioncode,access,rolecode) values ('HRMS_GROUP_PERSONAL_GOAL_SUMMARY','ALLOW','DR');

insert into "0".rolepermission (permissioncode,access,rolecode) values ('HRMS_EDIT_GROUP_PERSONAL_GOAL','ALLOW','ADMIN');
insert into "0".rolepermission (permissioncode,access,rolecode) values ('HRMS_EDIT_GROUP_PERSONAL_GOAL','ALLOW','HR');
insert into "0".rolepermission (permissioncode,access,rolecode) values ('HRMS_EDIT_GROUP_PERSONAL_GOAL','ALLOW','DR');

insert into "0".rolepermission (permissioncode,access,rolecode) values ('HRMS_GROUP_PERSONAL_GOAL_REMOVE','ALLOW','ADMIN');
insert into "0".rolepermission (permissioncode,access,rolecode) values ('HRMS_GROUP_PERSONAL_GOAL_REMOVE','ALLOW','HR');
insert into "0".rolepermission (permissioncode,access,rolecode) values ('HRMS_GROUP_PERSONAL_GOAL_REMOVE','ALLOW','DR');


insert into "anv".permission_context (permissioncode,contextcode) values ('HRMS_GROUP_PERSONAL_GOALS','HRMS');
insert into "anv".permission_context (permissioncode,contextcode) values ('HRMS_ADD_NEW_GROUP_PERSONAL_GOALS','HRMS');
insert into "anv".permission_context (permissioncode,contextcode) values ('HRMS_GROUP_PERSONAL_GOAL_SUMMARY','HRMS');
insert into "anv".permission_context (permissioncode,contextcode) values ('HRMS_EDIT_GROUP_PERSONAL_GOAL','HRMS');
insert into "anv".permission_context (permissioncode,contextcode) values ('HRMS_GROUP_PERSONAL_GOAL_REMOVE','HRMS');

insert into "anv".rolepermission (permissioncode,access,rolecode) values ('HRMS_GROUP_PERSONAL_GOALS','ALLOW','ADMIN');
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('HRMS_GROUP_PERSONAL_GOALS','ALLOW','HR');
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('HRMS_GROUP_PERSONAL_GOALS','ALLOW','DR');

insert into "anv".rolepermission (permissioncode,access,rolecode) values ('HRMS_ADD_NEW_GROUP_PERSONAL_GOALS','ALLOW','ADMIN');
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('HRMS_ADD_NEW_GROUP_PERSONAL_GOALS','ALLOW','HR');
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('HRMS_ADD_NEW_GROUP_PERSONAL_GOALS','ALLOW','DR');

insert into "anv".rolepermission (permissioncode,access,rolecode) values ('HRMS_GROUP_PERSONAL_GOAL_SUMMARY','ALLOW','ADMIN');
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('HRMS_GROUP_PERSONAL_GOAL_SUMMARY','ALLOW','HR');
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('HRMS_GROUP_PERSONAL_GOAL_SUMMARY','ALLOW','DR');

insert into "anv".rolepermission (permissioncode,access,rolecode) values ('HRMS_EDIT_GROUP_PERSONAL_GOAL','ALLOW','ADMIN');
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('HRMS_EDIT_GROUP_PERSONAL_GOAL','ALLOW','HR');
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('HRMS_EDIT_GROUP_PERSONAL_GOAL','ALLOW','DR');

insert into "anv".rolepermission (permissioncode,access,rolecode) values ('HRMS_GROUP_PERSONAL_GOAL_REMOVE','ALLOW','ADMIN');
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('HRMS_GROUP_PERSONAL_GOAL_REMOVE','ALLOW','HR');
insert into "anv".rolepermission (permissioncode,access,rolecode) values ('HRMS_GROUP_PERSONAL_GOAL_REMOVE','ALLOW','DR');