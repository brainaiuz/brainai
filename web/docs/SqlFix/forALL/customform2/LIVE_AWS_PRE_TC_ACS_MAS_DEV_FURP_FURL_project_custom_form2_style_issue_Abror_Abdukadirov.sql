UPDATE "0".modelfield set sectionStyle  = 'slideDown-box  group expand hideCustomField' WHERE form_id = 'PROJECT_FORM';
UPDATE "0".modelfield set fieldSetStyle = 'slideDown-content group labelLine' WHERE form_id = 'PROJECT_FORM';
UPDATE "0".modelfield set fieldSetStyle = 'slideDown-content group nobrd' WHERE noLabelFor is not null AND noLabelFor != '' AND form_id = 'PROJECT_FORM' and section != 'INVOLVED_EMPLOYEES';
UPDATE "0".modelfield set halfSetStyle = 'halfSet-1 left' WHERE form_id = 'PROJECT_FORM';
UPDATE "0".modelfield set halfSetStyle = '' WHERE noLabelFor is not null AND noLabelFor != '' AND form_id = 'PROJECT_FORM';
UPDATE "0".modelfield set rowStyle = 'row hideCustomField' WHERE form_id = 'PROJECT_FORM';
UPDATE "0".modelfield set fieldStyle = 'field' WHERE form_id = 'PROJECT_FORM';


UPDATE modelfield set sectionStyle  = 'slideDown-box  group expand hideCustomField' WHERE form_id = 'PROJECT_FORM';
UPDATE modelfield set fieldSetStyle = 'slideDown-content group labelLine' WHERE form_id = 'PROJECT_FORM';
UPDATE modelfield set fieldSetStyle = 'slideDown-content group nobrd' WHERE noLabelFor is not null AND noLabelFor != '' AND form_id = 'PROJECT_FORM' and section != 'INVOLVED_EMPLOYEES';
UPDATE modelfield set halfSetStyle = 'halfSet-1 left' WHERE form_id = 'PROJECT_FORM';
UPDATE modelfield set halfSetStyle = '' WHERE noLabelFor is not null AND noLabelFor != '' AND form_id = 'PROJECT_FORM';
UPDATE modelfield set rowStyle = 'row hideCustomField' WHERE form_id = 'PROJECT_FORM';
UPDATE modelfield set fieldStyle = 'field' WHERE form_id = 'PROJECT_FORM';


UPDATE "anv".modelfield set sectionStyle  = 'slideDown-box  group expand hideCustomField' WHERE form_id = 'PROJECT_FORM';
UPDATE "anv".modelfield set fieldSetStyle = 'slideDown-content group labelLine' WHERE form_id = 'PROJECT_FORM';
UPDATE "anv".modelfield set fieldSetStyle = 'slideDown-content group nobrd' WHERE noLabelFor is not null AND noLabelFor != '' AND form_id = 'PROJECT_FORM' and section != 'INVOLVED_EMPLOYEES';
UPDATE "anv".modelfield set halfSetStyle = 'halfSet-1 left' WHERE form_id = 'PROJECT_FORM';
UPDATE "anv".modelfield set halfSetStyle = '' WHERE noLabelFor is not null AND noLabelFor != '' AND form_id = 'PROJECT_FORM';
UPDATE "anv".modelfield set rowStyle = 'row hideCustomField' WHERE form_id = 'PROJECT_FORM';
UPDATE "anv".modelfield set fieldStyle = 'field' WHERE form_id = 'PROJECT_FORM';