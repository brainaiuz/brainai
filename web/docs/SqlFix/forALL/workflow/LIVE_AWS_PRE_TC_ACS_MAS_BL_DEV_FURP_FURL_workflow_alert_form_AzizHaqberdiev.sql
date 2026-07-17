delete from model where formID = 'WORKFLOW_ALERT_FORM';
delete from modelfield where form_ID = 'WORKFLOW_ALERT_FORM';

insert into model(formID, active, title) values('WORKFLOW_ALERT_FORM', true, 'Workflow Alert');
INSERT INTO modelfield (form_ID, field_ID, sorder, mandatory, hide, isCustomField, section, defaultValue, widget, systemmandatory, nolabelfor, nowrapperfor)
VALUES
  ('WORKFLOW_ALERT_FORM', 'FROM_EMAIL', 1, FALSE, FALSE, FALSE, 'INFORMATION', '', 'DropDown', FALSE, NULL, NULL),
  ('WORKFLOW_ALERT_FORM', 'FROM_NAME', 2, FALSE, FALSE, FALSE, 'INFORMATION', '', 'TextBox', FALSE, NULL, NULL),
  ('WORKFLOW_ALERT_FORM', 'RECEPIENT', 3, TRUE, FALSE, FALSE, 'INFORMATION', '', 'TextBox', FALSE, NULL, NULL),
  ('WORKFLOW_ALERT_FORM', 'BCC_CC_PANEL', 4, FALSE, FALSE, FALSE, 'INFORMATION', '', 'UNKNOWN', FALSE, NULL,
   'addForm,editForm,viewForm'),
  ('WORKFLOW_ALERT_FORM', 'TEMPLATE', 5, FALSE, FALSE, FALSE, 'INFORMATION', '', 'DropDown', FALSE, NULL, NULL),
  ('WORKFLOW_ALERT_FORM', 'SUBJECT', 6, TRUE, FALSE, FALSE, 'INFORMATION', '', 'TextBox', TRUE, NULL, NULL),
  ('WORKFLOW_ALERT_FORM', 'INCLUDE_PDF', 7, FALSE, FALSE, FALSE, 'INFORMATION', '', 'CheckBox', FALSE, NULL, NULL),
  ('WORKFLOW_ALERT_FORM', 'CONTENT', 8, FALSE, FALSE, FALSE, 'CONTENT', '', 'TextArea', FALSE, NULL, NULL),
  ('WORKFLOW_ALERT_FORM', 'WORKFLOW_TIME_BASED', 9, FALSE, FALSE, FALSE, 'WORKFLOW_TIME_BASED_HEADER', '', 'UNKNOWN',
   FALSE, 'addForm,editForm,viewForm', 'addForm,editForm,viewForm');

UPDATE modelfield set sectionStyle = 'slideDown-box  group expand hideCustomField' where form_id= 'WORKFLOW_ALERT_FORM';
UPDATE modelfield set fieldSetStyle = 'slideDown-content group labelLine' where form_id= 'WORKFLOW_ALERT_FORM';
UPDATE modelfield set fieldSetStyle = 'slideDown-content group nobrd' WHERE noLabelFor is not null AND noLabelFor != '' and form_id= 'WORKFLOW_ALERT_FORM';
UPDATE modelfield set halfSetStyle = 'halfSet-1 left' where form_id= 'WORKFLOW_ALERT_FORM';
UPDATE modelfield set halfSetStyle = '' WHERE noLabelFor is not null AND noLabelFor != '' and form_id= 'WORKFLOW_ALERT_FORM';
UPDATE modelfield set rowStyle = 'row hideCustomField' where form_id= 'WORKFLOW_ALERT_FORM';
UPDATE modelfield set fieldStyle = 'field' where form_id= 'WORKFLOW_ALERT_FORM';

delete from "anv".model where formID = 'WORKFLOW_ALERT_FORM';
delete from "anv".modelfield where form_ID = 'WORKFLOW_ALERT_FORM';