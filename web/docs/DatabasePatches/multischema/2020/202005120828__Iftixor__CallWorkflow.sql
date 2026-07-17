
delete from "0".reference  where code = '_WORKFLOW_MODULE_LOGACALL';
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('_WORKFLOW_MODULE_LOGACALL', false, true, 'Log a Call', true,
       (select max(sorder) from "0".reference where parentid = (select id from "0".reference where code = '_WORKFLOW_MODULE')),
       (select id from "0".reference where code='_WORKFLOW_MODULE'), true);



delete from "anv".reference  where code = '_WORKFLOW_MODULE_LOGACALL';
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('_WORKFLOW_MODULE_LOGACALL', false, true, 'Log a Call', true,5,(select id from "anv".reference where code='_WORKFLOW_MODULE'), true);





delete from modelfield where form_ID = 'LOGACALL_FORM';
insert into modelfield (form_ID, field_ID, sorder, widget, source, usableByWorkflow, type, disableUpdate, isWorkflowAttribute, split, mandatory, systemMandatory, hide, isCustomField, fullWidth, isEntityField, hideInCustomizeForm, systemDisable)
values
('LOGACALL_FORM', 'SUBJECT', 1, 'TextBox', null, true, 'Text', false, false, false, false, false, false, false, false, false, false, false),
('LOGACALL_FORM', 'DESCRIPTION', 2, 'TextArea', null, true, 'Text', false, false, false, false, false, false, false, false, false, false, false),
('LOGACALL_FORM', 'LOCATION', 3, 'TextBox', null, true, 'Text', false, false, false, false, false, false, false, false, false, false, false),
('LOGACALL_FORM', 'CREATOR', 4, 'LOOKUP', 'CRM@EMPLOYEE', true, 'Text', false, false, false, false, false, false, false, false, false, false, false),
('LOGACALL_FORM', 'START_DATE', 5, 'DatePicker', null, true, 'Date', false, false, false, false, false, false, false, false, false, false, false),
('LOGACALL_FORM', 'END_DATE', 6, 'DatePicker', null, true, 'Date', false, false, false, false, false, false, false, false, false, false, false),
('LOGACALL_FORM', 'INBOUND', 7, 'Checkbox', null, true, 'text', false, false, false, false, false, false, false, false, false, false, false),
('LOGACALL_FORM', 'OUTBOUND', 8, 'Checkbox', null, true, 'text', false, false, false, false, false, false, false, false, false, false, false),
('LOGACALL_FORM', 'MISSED', 9, 'Checkbox', null, true, 'text', false, false, false, false, false, false, false, false, false, false, false),
('LOGACALL_FORM', 'CURRENT', 10, 'Checkbox', null, true, 'text', false, false, false, false, false, false, false, false, false, false, false),
('LOGACALL_FORM', 'COMPLETED', 11, 'Checkbox', null, true, 'text', false, false, false, false, false, false, false, false, false, false, false),
('LOGACALL_FORM', 'SCHEDULE', 12, 'Checkbox', null, true, 'text', false, false, false, false, false, false, false, false, false, false, false),
('LOGACALL_FORM', 'CALL_DURATION_TIME', 13, 'TextBox', null, true, 'Number', false, false, false, false, false, false, false, false, false, false, false);
