update "anv".modelfield  set usableByWorkflow = true where form_id = 'POSITION_FORM' and field_id in ('POSITION_CODE', 'POSITION_TITLE', 'EMPLOYEES', 'JOB_FAMILY_PANEL', 'DESCRIPTION');
insert into "anv".reference(code, name, sorder, parentid) values ('_WORKFLOW_MODULE_POSITION', 'Position', 20, (select id from "anv".reference where code = '_WORKFLOW_MODULE' limit 1));

update "anv".modelfield set source = 'HRMS@JOB_FAMILY', widget = 'LOOKUP' where form_id = 'POSITION_FORM' and field_id = 'JOB_FAMILY_PANEL';
update "anv".modelfield set source = 'HRMS@SUPERVISOR', widget = 'LOOKUP' where form_id = 'POSITION_FORM' and field_id = 'EMPLOYEES';