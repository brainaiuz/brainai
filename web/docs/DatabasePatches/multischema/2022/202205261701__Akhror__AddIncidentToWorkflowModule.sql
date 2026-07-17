insert into "anv".reference(code, name, sorder, parentid) values ('_WORKFLOW_MODULE_INCIDENT', 'Incident', 10, (select id from "anv".reference where code = '_WORKFLOW_MODULE'));

update "anv".modelfield set usablebyworkflow = true where form_id = 'INCIDENT_FORM' and field_id not in ('ATTACHMENTS', 'DESCRIPTION', 'PERIOD');
update "anv".modelfield set source = 'REFERENCE@_PERFORMANCE_NOTE_PRIORITIES', widget = 'text' where field_id = 'PRIORITY' and form_id = 'INCIDENT_FORM';
update "anv".modelfield set source = 'HRMS@SUPERVISOR', widget = 'text' where field_id = 'RELATED_EMPLOYEES' and form_id = 'INCIDENT_FORM';
update "anv".modelfield set source = 'HRMS@SUPERVISOR', widget = 'text' where field_id = 'REPORTED_BY' and form_id = 'INCIDENT_FORM';
update "anv".modelfield set source = 'HRMS@SUPERVISOR', widget = 'text' where field_id = 'RESOLVER' and form_id = 'INCIDENT_FORM';
update "anv".modelfield set source = 'Public;Private', widget = 'text' where field_id = 'VISIBILITY' and form_id = 'INCIDENT_FORM';
update "anv".modelfield set source = 'REFERENCE@_ISSUE_STATUS', widget = 'text' where field_id = 'STATUS' and form_id = 'INCIDENT_FORM';