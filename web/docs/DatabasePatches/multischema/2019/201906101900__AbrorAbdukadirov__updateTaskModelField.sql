delete from "0".reference  where code = '_WORKFLOW_MODULE_TASK';
insert into "0".reference (code, name, sorder, parentid)
values ('_WORKFLOW_MODULE_TASK', 'Task', (select max(sorder) from "0".reference where parentid = (select id from "0".reference where code = '_WORKFLOW_MODULE')),
        (select id from "0".reference where code = '_WORKFLOW_MODULE'));

update "0".modelfield set usableByWorkflow = true, disableUpdate = true, type = 'Text' where form_id = 'TASK_MAX_FORM' and field_id = 'NUMBER';
update "0".modelfield set usableByWorkflow = true, type = 'Text' where form_id = 'TASK_MAX_FORM' and field_id = 'NAME';
update "0".modelfield set usableByWorkflow = true, type = 'Text' where form_id = 'TASK_MAX_FORM' and field_id = 'DESCRIPTION';
update "0".modelfield set usableByWorkflow = true, type = 'Date' where form_id = 'TASK_MAX_FORM' and field_id = 'START_DATE';
update "0".modelfield set usableByWorkflow = true, type = 'Date' where form_id = 'TASK_MAX_FORM' and field_id = 'DUE_DATE';
update "0".modelfield set usableByWorkflow = true, source = 'PM@TASK_PROJECT' where form_id = 'TASK_MAX_FORM' and field_id = 'PROJECT';
update "0".modelfield set usableByWorkflow = true, source = 'REFERENCE@_TASK_STATUS' where form_id = 'TASK_MAX_FORM' and field_id = 'STATUS';
update "0".modelfield set usableByWorkflow = true, source = 'REFERENCE@_TASK_PRIORITY' where form_id = 'TASK_MAX_FORM' and field_id = 'PRIORITY';

update modelfield set usableByWorkflow = true, disableUpdate = true, type = 'Text' where form_id = 'TASK_MAX_FORM' and field_id = 'NUMBER';
update modelfield set usableByWorkflow = true, type = 'Text' where form_id = 'TASK_MAX_FORM' and field_id = 'NAME';
update modelfield set usableByWorkflow = true, type = 'Text' where form_id = 'TASK_MAX_FORM' and field_id = 'DESCRIPTION';
update modelfield set usableByWorkflow = true, type = 'Date' where form_id = 'TASK_MAX_FORM' and field_id = 'START_DATE';
update modelfield set usableByWorkflow = true, type = 'Date' where form_id = 'TASK_MAX_FORM' and field_id = 'DUE_DATE';
update modelfield set usableByWorkflow = true, source = 'PM@TASK_PROJECT' where form_id = 'TASK_MAX_FORM' and field_id = 'PROJECT';
update modelfield set usableByWorkflow = true, source = 'REFERENCE@_TASK_STATUS' where form_id = 'TASK_MAX_FORM' and field_id = 'STATUS';
update modelfield set usableByWorkflow = true, source = 'REFERENCE@_TASK_PRIORITY' where form_id = 'TASK_MAX_FORM' and field_id = 'PRIORITY';

update "anv".modelfield set usableByWorkflow = true, disableUpdate = true, type = 'Text' where form_id = 'TASK_MAX_FORM' and field_id = 'NUMBER';
update "anv".modelfield set usableByWorkflow = true, type = 'Text' where form_id = 'TASK_MAX_FORM' and field_id = 'NAME';
update "anv".modelfield set usableByWorkflow = true, type = 'Text' where form_id = 'TASK_MAX_FORM' and field_id = 'DESCRIPTION';
update "anv".modelfield set usableByWorkflow = true, type = 'Date' where form_id = 'TASK_MAX_FORM' and field_id = 'START_DATE';
update "anv".modelfield set usableByWorkflow = true, type = 'Date' where form_id = 'TASK_MAX_FORM' and field_id = 'DUE_DATE';
update "anv".modelfield set usableByWorkflow = true, source = 'PM@TASK_PROJECT' where form_id = 'TASK_MAX_FORM' and field_id = 'PROJECT';
update "anv".modelfield set usableByWorkflow = true, source = 'REFERENCE@_TASK_STATUS' where form_id = 'TASK_MAX_FORM' and field_id = 'STATUS';
update "anv".modelfield set usableByWorkflow = true, source = 'REFERENCE@_TASK_PRIORITY' where form_id = 'TASK_MAX_FORM' and field_id = 'PRIORITY';

delete from "anv".reference  where code = '_WORKFLOW_MODULE_TASK';
insert into "anv".reference (code, name, sorder, parentid)
values ('_WORKFLOW_MODULE_TASK', 'Task', (select max(sorder) from "anv".reference where parentid = (select id from "anv".reference where code = '_WORKFLOW_MODULE')),
        (select id from "anv".reference where code = '_WORKFLOW_MODULE'));
