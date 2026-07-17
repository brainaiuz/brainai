delete from permission where code ='HRMS_APPROVE_LIVE_STATUS';

delete from "anv".permission_context where permissioncode = 'HRMS_APPROVE_LIVE_STATUS';

delete from "anv".rolepermission where permissioncode = 'HRMS_APPROVE_LIVE_STATUS';
