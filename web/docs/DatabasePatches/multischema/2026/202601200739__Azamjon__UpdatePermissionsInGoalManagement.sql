update permission set code='HRMS_PERSONAL_GOAL_SEE_ALL' where code='HRMS_GOAL_SEE_ALL';
update "anv".permission_context set permissioncode='HRMS_PERSONAL_GOAL_SEE_ALL' where permissioncode = 'HRMS_GOAL_SEE_ALL';
update "anv".rolepermission set permissioncode='HRMS_PERSONAL_GOAL_SEE_ALL' where permissioncode = 'HRMS_GOAL_SEE_ALL';