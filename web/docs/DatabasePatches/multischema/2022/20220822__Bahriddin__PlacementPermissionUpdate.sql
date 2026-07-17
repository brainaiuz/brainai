delete
from permission
where code = 'HRMS_APPROVE_AND_HIRE_PLACEMENT';

delete
from "anv".permission_context
where permissioncode = 'HRMS_APPROVE_AND_HIRE_PLACEMENT';


delete
from "anv".rolepermission
where permissioncode = 'HRMS_APPROVE_AND_HIRE_PLACEMENT';