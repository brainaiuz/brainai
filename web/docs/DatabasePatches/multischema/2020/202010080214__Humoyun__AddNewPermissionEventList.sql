
delete from "anv".modelfield where form_ID = 'LOGACALL_FORM' and field_id = 'CALL_CLONE';
insert into "anv".modelfield
(form_id,           fsection,                section,               nolabelfor,        fieldstyle,      columntype,   fieldsetstyle,                           rowstyle,                mandatory,    sectionstyle,                                      widget,            forder,     field_id) values
('LOGACALL_FORM',	  'CALL_INFORMATION',      'CALL_INFORMATION',    null,              'field',         'COL_1',	     'slideDown-content group labelLine',     'row hideCustomField',   false,        'slideDown-box  group expand hideCustomField',    'MaterialPanel',    6,	       'CALL_CLONE');



update permission set  name='Quick Add Interview', sorder = 2 where parent=(select id from permission where code = 'HRMS_ACTIVITIES_VIEW') and code='HRMS_ADD_NEW_ACTIVITY_EVENT';
update permission set  name='Quick Add Call Log', sorder = 4 where parent=(select id from permission where code = 'HRMS_ACTIVITIES_VIEW') and code='HRMS_ADD_NEW_ACTIVITY_LOG_A_CALL';
update permission set  name='Edit', sorder = 5 where parent=(select id from permission where code = 'HRMS_ACTIVITIES_VIEW') and code='HRMS_EDIT_ACTIVITY';
update permission set  name='Delete', sorder = 6 where parent=(select id from permission where code = 'HRMS_ACTIVITIES_VIEW') and code='HRMS_REMOVE_ACTIVITY';
update permission set  name='Summary', sorder = 7 where parent=(select id from permission where code = 'HRMS_ACTIVITIES_VIEW') and code='HRMS_SUMMARY_ACTIVITY';

delete from permission where code='HRMS_FULL_ADD_NEW_ACTIVITY_EVENT';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_FULL_ADD_NEW_ACTIVITY_EVENT', 'HRMS', 'Add Interview', 1,
        (select id from permission where code = 'HRMS_ACTIVITIES_VIEW'),'RECRUITMENT_SYSTEM');

delete from "anv".permission_context where permissioncode = 'HRMS_FULL_ADD_NEW_ACTIVITY_EVENT';
insert into "anv".permission_context (permissioncode, contextcode) values ('HRMS_FULL_ADD_NEW_ACTIVITY_EVENT', 'HRMS');

delete from "anv".rolepermission where permissioncode = 'HRMS_FULL_ADD_NEW_ACTIVITY_EVENT';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('HRMS_FULL_ADD_NEW_ACTIVITY_EVENT', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('HRMS_FULL_ADD_NEW_ACTIVITY_EVENT', 'ALLOW', 'SALESMAN');

delete from permission where code='HRMS_FULL_ADD_NEW_ACTIVITY_LOG_A_CALL';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_FULL_ADD_NEW_ACTIVITY_LOG_A_CALL', 'HRMS', 'Add Call Log', 3,
        (select id from permission where code = 'HRMS_ACTIVITIES_VIEW'),'RECRUITMENT_SYSTEM');

delete from "anv".permission_context where permissioncode = 'HRMS_FULL_ADD_NEW_ACTIVITY_LOG_A_CALL';
insert into "anv".permission_context (permissioncode, contextcode) values ('HRMS_FULL_ADD_NEW_ACTIVITY_LOG_A_CALL', 'HRMS');

delete from "anv".rolepermission where permissioncode = 'HRMS_FULL_ADD_NEW_ACTIVITY_LOG_A_CALL';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('HRMS_FULL_ADD_NEW_ACTIVITY_LOG_A_CALL', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('HRMS_FULL_ADD_NEW_ACTIVITY_LOG_A_CALL', 'ALLOW', 'SALESMAN');



update permission set  name='Quick Add Event', sorder = 3 where parent=(select id from permission where code = 'CRM_ACTIVITIES_LIST') and code='CRM_ADD_NEW_ACTIVITY_EVENT';
update permission set  name='Quick Add Log a Call', sorder = 5 where parent=(select id from permission where code = 'CRM_ACTIVITIES_LIST') and code='CRM_ADD_NEW_ACTIVITY_LOG_A_CALL';
update permission set  name='Edit', sorder = 6 where parent=(select id from permission where code = 'CRM_ACTIVITIES_LIST') and code='CRM_EDIT_ACTIVITY';
update permission set  name='Delete', sorder = 7 where parent=(select id from permission where code = 'CRM_ACTIVITIES_LIST') and code='CRM_REMOVE_ACTIVITY';
update permission set  name='View Call', sorder = 9 where parent=(select id from permission where code = 'CRM_ACTIVITIES_LIST') and code='CRM_ADD_NEW_CAMPAIGN';
update permission set  name='Summary Log a Call', sorder = 10 where parent=(select id from permission where code = 'CRM_ACTIVITIES_LIST') and code='CRM_ACTIVITIES_LOG_CALL_VIEW';
update permission set  name='Attachments', sorder = 11 where parent=(select id from permission where code = 'CRM_ACTIVITIES_LIST') and code='CRM_ACTIVITY_SEE_ATTACHMENTS';
update permission set  name='All Attachments', sorder = 12 where parent=(select id from permission where code = 'CRM_ACTIVITIES_LIST') and code='CRM_ACTIVITY_SEE_ALL_ATTACHMENTS';


delete from permission where code='CRM_FULL_ADD_NEW_ACTIVITY_EVENT';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('CRM_FULL_ADD_NEW_ACTIVITY_EVENT', 'CRM', 'Add Event', 2,
        (select id from permission where code = 'CRM_ACTIVITIES_LIST'),'ACTIVITIES');

delete from "anv".permission_context where permissioncode = 'CRM_FULL_ADD_NEW_ACTIVITY_EVENT';
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_FULL_ADD_NEW_ACTIVITY_EVENT', 'CRM');

delete from "anv".rolepermission where permissioncode = 'CRM_FULL_ADD_NEW_ACTIVITY_EVENT';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_FULL_ADD_NEW_ACTIVITY_EVENT', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_FULL_ADD_NEW_ACTIVITY_EVENT', 'ALLOW', 'SALESMAN');

delete from permission where code='CRM_FULL_ADD_NEW_ACTIVITY_LOG_A_CALL';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('CRM_FULL_ADD_NEW_ACTIVITY_LOG_A_CALL', 'CRM', 'Add Log a Call', 4,
        (select id from permission where code = 'CRM_ACTIVITIES_LIST'),'ACTIVITIES');

delete from "anv".permission_context where permissioncode = 'CRM_FULL_ADD_NEW_ACTIVITY_LOG_A_CALL';
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_FULL_ADD_NEW_ACTIVITY_LOG_A_CALL', 'CRM');

delete from "anv".rolepermission where permissioncode = 'CRM_FULL_ADD_NEW_ACTIVITY_LOG_A_CALL';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_FULL_ADD_NEW_ACTIVITY_LOG_A_CALL', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_FULL_ADD_NEW_ACTIVITY_LOG_A_CALL', 'ALLOW', 'SALESMAN');

delete from permission where code='CRM_ACTIVITY_SUMMARY';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('CRM_ACTIVITY_SUMMARY', 'CRM', 'Summary', 8,
        (select id from permission where code = 'CRM_ACTIVITIES_LIST'),'ACTIVITIES');

delete from "anv".permission_context where permissioncode = 'CRM_ACTIVITY_SUMMARY';
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_ACTIVITY_SUMMARY', 'CRM');

delete from "anv".rolepermission where permissioncode = 'CRM_ACTIVITY_SUMMARY';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_ACTIVITY_SUMMARY', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_ACTIVITY_SUMMARY', 'ALLOW', 'SALESMAN');
