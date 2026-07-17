insert into permission (code, context, name, parent, modulecode)
values ('HRMS_POSITION_MULTI_ADD', 'SETTINGS', 'Multi Add', (select id from permission where code = 'HRMS_POSITION'),
        'HRMS_MODULE');

insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_POSITION_MULTI_ADD', 'SETTINGS');