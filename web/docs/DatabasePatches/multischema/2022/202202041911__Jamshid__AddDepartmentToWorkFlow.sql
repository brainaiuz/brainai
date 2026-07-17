update "anv".modelfield  set usableByWorkflow = true where form_id = 'DEPARTMENT_FORM' and field_id not in ('DEPARTMENT_LEADER2', 'DEPARTMENT_LEADER3', 'DEPARTMENT_LEADER4', 'DEPARTMENT_LEADER5');
delete from "anv".reference where code = '_WORKFLOW_MODULE_DEPARTMENT';
insert into "anv".reference(code, name, sorder, parentid) values ('_WORKFLOW_MODULE_DEPARTMENT', 'Department', 20, (select id from "anv".reference where code = '_WORKFLOW_MODULE' limit 1));

update "anv".modelfield set source = 'HRMS@DEPARTMENT', widget = 'LOOKUP' where form_id = 'DEPARTMENT_FORM' and field_id = 'DEPARTMENT_PARENT';
update "anv".modelfield set source = 'HRMS@SUPERVISOR', widget = 'LOOKUP' where form_id = 'DEPARTMENT_FORM' and field_id in ('DEPARTMENT_CREATED_BY', 'DEPARTMENT_EMPLOYEES', 'DEPARTMENT_LEADER');
update "anv".modelfield set widget = 'DatePicker' where form_id = 'DEPARTMENT_FORM' and field_id = 'DEPARTMENT_START_DATE';