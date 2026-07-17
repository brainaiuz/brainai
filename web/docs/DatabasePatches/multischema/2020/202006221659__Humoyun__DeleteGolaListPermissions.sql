
delete from permission where parent = (select id from permission where code='HRMS_GOALS');

delete from permission where code = 'HRMS_GOALS';

delete from "anv".permission_context where  permissioncode = 'HRMS_GOALS';

delete from "anv".rolepermission where permissioncode = 'HRMS_GOALS';