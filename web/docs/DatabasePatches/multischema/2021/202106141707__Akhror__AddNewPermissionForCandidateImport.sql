delete from permission where code = 'HRMS_CANDIDATE_IMPORT';
insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('HRMS_CANDIDATE_IMPORT', 'HRMS', false, 'Import', 50,
        (select id from permission where code = 'HRMS_CANDIDATE_LIST_VIEW'), true, 'RECRUITMENT_SYSTEM');

delete from "anv".permission_context where permissioncode = 'HRMS_CANDIDATE_IMPORT';
insert into "anv".permission_context(permissioncode, contextcode)
values ('HRMS_CANDIDATE_IMPORT', 'HRMS');

delete from "anv".rolepermission where permissioncode = 'HRMS_CANDIDATE_IMPORT';
insert into "anv".rolepermission(permissioncode, access, rolecode)
values ('HRMS_CANDIDATE_IMPORT', 'ALLOW', 'HR');