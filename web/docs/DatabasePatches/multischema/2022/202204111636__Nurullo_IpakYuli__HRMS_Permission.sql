update permission
set parent=(select id from permission where code = 'HRMS_MAIN_MENU')
where code = 'MROT_FORM_300205'
  and companyid = 300205;

update permission
set parent=(select id from permission where code = 'HRMS_MAIN_MENU')
where code = 'EDINAYA_TARIFNAYA_SETKA_FORM_300205'
  and companyid = 300205;

update permission
set parent=(select id from permission where code = 'HRMS_MAIN_MENU')
where code = 'ROTACII_(PO_DOLZHNOSTI)_FORM_300205'
  and companyid = 300205;

update permission
set parent=(select id from permission where code = 'HRMS_MAIN_MENU')
where code = 'ROTACII_(PO_OTDELAM)_FORM_300205'
  and companyid = 300205;

update permission
set parent=(select id from permission where code = 'HRMS_MAIN_MENU')
where code = 'EZHEGODNIJ_OTPUSK_2_FORM_300205'
  and companyid = 300205;


delete
from "anv".rolepermission
where permissioncode = 'EMPLOYEE_STEP_SANAT_LIST';
delete
from "anv".permission_context
where permissioncode = 'EMPLOYEE_STEP_SANAT_LIST';
delete
from permission
where code = 'EMPLOYEE_STEP_SANAT_LIST';

delete
from "anv".rolepermission
where permissioncode = 'EMPLOYEE_STEP_SANAT_DELETE';
delete
from "anv".permission_context
where permissioncode = 'EMPLOYEE_STEP_SANAT_DELETE';
delete
from permission
where code = 'EMPLOYEE_STEP_SANAT_DELETE';

delete
from "anv".rolepermission
where permissioncode = 'EMPLOYEE_STEP_SANAT_EXPORT';
delete
from "anv".permission_context
where permissioncode = 'EMPLOYEE_STEP_SANAT_EXPORT';
delete
from permission
where code = 'EMPLOYEE_STEP_SANAT_EXPORT';

delete
from "anv".rolepermission
where permissioncode = 'EMPLOYEE_STEP_SANAT_ADD';
delete
from "anv".permission_context
where permissioncode = 'EMPLOYEE_STEP_SANAT_ADD';
delete
from permission
where code = 'EMPLOYEE_STEP_SANAT_ADD';

delete
from "anv".rolepermission
where permissioncode = 'EMPLOYEE_STEP_SANAT_EDIT';
delete
from "anv".permission_context
where permissioncode = 'EMPLOYEE_STEP_SANAT_EDIT';
delete
from permission
where code = 'EMPLOYEE_STEP_SANAT_EDIT';


