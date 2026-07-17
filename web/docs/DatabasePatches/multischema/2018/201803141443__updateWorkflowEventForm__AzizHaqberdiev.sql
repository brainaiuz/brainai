update modelfield set noLabelFor = null, nowrapperFor = null where form_id in ('WORKFLOW_EVENT_FORM', 'WORKFLOW_CALL_LOG_FORM');
update "0".modelfield set noLabelFor = null, nowrapperFor = null where form_id in ('WORKFLOW_EVENT_FORM', 'WORKFLOW_CALL_LOG_FORM');
update "anv".modelfield set noLabelFor = null, nowrapperFor = null where form_id in ('WORKFLOW_EVENT_FORM', 'WORKFLOW_CALL_LOG_FORM');

update modelfield set field_id = 'SUBJECT' where form_id in ('WORKFLOW_EVENT_FORM', 'WORKFLOW_CALL_LOG_FORM') and field_id = 'WHAT';
update "0".modelfield set field_id = 'SUBJECT' where form_id in ('WORKFLOW_EVENT_FORM', 'WORKFLOW_CALL_LOG_FORM') and field_id = 'WHAT';
update "anv".modelfield set field_id = 'SUBJECT' where form_id in ('WORKFLOW_EVENT_FORM', 'WORKFLOW_CALL_LOG_FORM') and field_id = 'WHAT';