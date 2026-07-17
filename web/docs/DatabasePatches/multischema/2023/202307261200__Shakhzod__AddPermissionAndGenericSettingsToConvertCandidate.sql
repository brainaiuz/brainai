insert into permission (code, context, name, parent, modulecode)
values ('HRMS_CONVERT_CANDIDATE', 'HRMS', 'Convert To Candidate',
        (select id from permission where code = 'HRMS_EMPLOYEES'), 'HRMS_MODULE');

insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_CONVERT_CANDIDATE', 'HRMS');


insert into "anv".genericsettings (key, value)
values ('ENABLE_CONVERT_TO_CANDIDATE', 'NO');