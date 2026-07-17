delete
from permission
where code = 'EMPLOYEE_STEP_TESTING_LIST';
delete
from permission
where code = 'EMPLOYEE_STEP_TESTING_EDIT';
delete
from permission
where code = 'EMPLOYEE_STEP_TESTING_ADD';
delete
from permission
where code = 'EMPLOYEE_STEP_TESTING_EXPORT';
delete
from permission
where code = 'EMPLOYEE_STEP_TESTING_DELETE';


update permission
set name = 'Sanat'
where code = 'EMPLOYEE_STEP_SANAT_LIST';
update permission
set name = 'Delete'
where code = 'EMPLOYEE_STEP_SANAT_DELETE';
update permission
set name = 'Export'
where code = 'EMPLOYEE_STEP_SANAT_EXPORT';
update permission
set name = 'Add'
where code = 'EMPLOYEE_STEP_SANAT_ADD';
update permission
set name = 'Edit'
where code = 'EMPLOYEE_STEP_SANAT_EDIT';


update permission
set name = 'Visa Application'
where code = 'EMPLOYEE_STEP_VISA_APPLICATION_LIST';
update permission
set name = 'Add'
where code = 'EMPLOYEE_STEP_VISA_APPLICATION_ADD';
update permission
set name = 'Edit'
where code = 'EMPLOYEE_STEP_VISA_APPLICATION_EDIT';
update permission
set name = 'Delete'
where code = 'EMPLOYEE_STEP_VISA_APPLICATION_DELETE';
update permission
set name = 'Export'
where code = 'EMPLOYEE_STEP_VISA_APPLICATION_EXPORT';