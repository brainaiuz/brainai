delete from permission where code = 'HRMS_QUICK_INTERVIEW_CANDIDATE';
insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('HRMS_QUICK_INTERVIEW_CANDIDATE', 'HRMS', false, 'Quick Add Interview', 50,
        (select id from permission where code = 'HRMS_INTERVIEW_CANDIDATE'), true, 'RECRUITMENT_SYSTEM');

delete from "anv".permission_context where permissioncode = 'HRMS_QUICK_INTERVIEW_CANDIDATE';
insert into "anv".permission_context(permissioncode, contextcode)
values ('HRMS_QUICK_INTERVIEW_CANDIDATE', 'HRMS');

delete from permission where code = 'HRMS_QUICK_CALL_CANDIDATE';
insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('HRMS_QUICK_CALL_CANDIDATE', 'HRMS', false, 'Quick Add Log a Call', 50,
        (select id from permission where code = 'HRMS_CALL_CANDIDATE'), true, 'RECRUITMENT_SYSTEM');

delete from "anv".permission_context where permissioncode = 'HRMS_QUICK_CALL_CANDIDATE';
insert into "anv".permission_context(permissioncode, contextcode)
values ('HRMS_QUICK_CALL_CANDIDATE', 'HRMS');

