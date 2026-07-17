update modelfield set usableByWorkflow = true, disableUpdate = true, type = 'Text' where form_id = 'PROJECT_FORM' and field_id = 'NUMBER';
update modelfield set usableByWorkflow = true, type = 'Text' where form_id = 'PROJECT_FORM' and field_id = 'NAME';
update modelfield set usableByWorkflow = true, type = 'Text' where form_id = 'PROJECT_FORM' and field_id = 'DESCRIPTION';
update modelfield set usableByWorkflow = true, type = 'Date' where form_id = 'PROJECT_FORM' and field_id = 'START_DATE';
update modelfield set usableByWorkflow = true, type = 'Date' where form_id = 'PROJECT_FORM' and field_id = 'DUE_DATE';
update modelfield set usableByWorkflow = true, source = 'PM@PROJECT_CLIENT' where form_id = 'PROJECT_FORM' and field_id = 'CLIENT';
update modelfield set usableByWorkflow = true, source = 'REFERENCE@_PROJECT_STATUS' where form_id = 'PROJECT_FORM' and field_id = 'STATUS';
update modelfield set usableByWorkflow = true, source = 'HRMS@LOCATION' where form_id = 'PROJECT_FORM' and field_id = 'LOCATION';
update modelfield set usableByWorkflow = true, disableUpdate = true, source = 'HRMS@SUPERVISOR' where form_id = 'PROJECT_FORM' and field_id = 'MANAGER';
update modelfield set usableByWorkflow = true, disableUpdate = true, source = 'HRMS@SUPERVISOR' where form_id = 'PROJECT_FORM' and field_id = 'BACKUP_MANAGER';

update "anv".modelfield set usableByWorkflow = true, disableUpdate = true, type = 'Text' where form_id = 'PROJECT_FORM' and field_id = 'NUMBER';
update "anv".modelfield set usableByWorkflow = true, type = 'Text' where form_id = 'PROJECT_FORM' and field_id = 'NAME';
update "anv".modelfield set usableByWorkflow = true, type = 'Text' where form_id = 'PROJECT_FORM' and field_id = 'DESCRIPTION';
update "anv".modelfield set usableByWorkflow = true, type = 'Date' where form_id = 'PROJECT_FORM' and field_id = 'START_DATE';
update "anv".modelfield set usableByWorkflow = true, type = 'Date' where form_id = 'PROJECT_FORM' and field_id = 'DUE_DATE';
update "anv".modelfield set usableByWorkflow = true, source = 'PM@PROJECT_CLIENT' where form_id = 'PROJECT_FORM' and field_id = 'CLIENT';
update "anv".modelfield set usableByWorkflow = true, source = 'REFERENCE@_PROJECT_STATUS' where form_id = 'PROJECT_FORM' and field_id = 'STATUS';
update "anv".modelfield set usableByWorkflow = true, source = 'HRMS@LOCATION' where form_id = 'PROJECT_FORM' and field_id = 'LOCATION';
update "anv".modelfield set usableByWorkflow = true, disableUpdate = true, source = 'HRMS@SUPERVISOR' where form_id = 'PROJECT_FORM' and field_id = 'MANAGER';
update "anv".modelfield set usableByWorkflow = true, disableUpdate = true, source = 'HRMS@SUPERVISOR' where form_id = 'PROJECT_FORM' and field_id = 'BACKUP_MANAGER';

delete from "0".reference  where code = '_WORKFLOW_MODULE_PROJECT';
insert into "0".reference (code, name, sorder, parentid)
values ('_WORKFLOW_MODULE_PROJECT', 'Project', (select max(sorder) from "0".reference where parentid = (select id from "0".reference where code = '_WORKFLOW_MODULE')),
(select id from "0".reference where code = '_WORKFLOW_MODULE'));

delete from "anv".reference  where code = '_WORKFLOW_MODULE_PROJECT';
insert into "anv".reference (code, name, sorder, parentid)
values ('_WORKFLOW_MODULE_PROJECT', 'Project', (select max(sorder) from "anv".reference where parentid = (select id from "anv".reference where code = '_WORKFLOW_MODULE')),
(select id from "anv".reference where code = '_WORKFLOW_MODULE'));
