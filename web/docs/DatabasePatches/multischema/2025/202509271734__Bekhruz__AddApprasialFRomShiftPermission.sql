insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_APPRAISAL_ADD_FROM_SHIFT', 'HRMS', 'Add Appraisal from Shift', 11, (select id from permission where code = 'HRMS_SIMPLE_APPRAISALS' limit 1), 'PERFORMANCE_APPRAISAL');

insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_APPRAISAL_ADD_FROM_SHIFT', 'HRMS');