
INSERT INTO "0".customformsection (active, custom, form_id, section, sorder) VALUES (true, false, 'PAYROLL_STARTER_FORM', 'ACCOUNT_INFORMATION', 7);
INSERT INTO "anv".customformsection (active, custom, form_id, section, sorder) VALUES (true, false, 'PAYROLL_STARTER_FORM', 'ACCOUNT_INFORMATION', 7);



update "0".modelfield set columntype='COL_1', forder=0, fsection='ACCOUNT_INFORMATION' where form_id='PAYROLL_STARTER_FORM' and field_id='ACCOUNT_ROLES';
update "0".modelfield set columntype='COL_2', forder=0, fsection='ACCOUNT_INFORMATION' where form_id='PAYROLL_STARTER_FORM' and field_id='ACCOUNT_STATUS';
update "0".modelfield set columntype='COL_2', forder=1, fsection='ACCOUNT_INFORMATION' where form_id='PAYROLL_STARTER_FORM' and field_id='ESS_USER';
update "0".modelfield set columntype='COL_2', forder=2, fsection='ACCOUNT_INFORMATION' where form_id='PAYROLL_STARTER_FORM' and field_id='NO_ACCESS';



update "anv".modelfield set columntype='COL_1', forder=0, fsection='ACCOUNT_INFORMATION' where form_id='PAYROLL_STARTER_FORM' and field_id='ACCOUNT_ROLES';
update "anv".modelfield set columntype='COL_2', forder=0, fsection='ACCOUNT_INFORMATION' where form_id='PAYROLL_STARTER_FORM' and field_id='ACCOUNT_STATUS';
update "anv".modelfield set columntype='COL_2', forder=1, fsection='ACCOUNT_INFORMATION' where form_id='PAYROLL_STARTER_FORM' and field_id='ESS_USER';
update "anv".modelfield set columntype='COL_2', forder=2, fsection='ACCOUNT_INFORMATION' where form_id='PAYROLL_STARTER_FORM' and field_id='NO_ACCESS';



UPDATE "0".modelfield set sectionStyle  = 'slideDown-box  group expand hideCustomField' WHERE form_id = 'PAYROLL_STARTER_FORM' AND section = 'ACCOUNT_INFORMATION';
UPDATE "0".modelfield set fieldSetStyle = 'slideDown-content group labelLine' WHERE form_id = 'PAYROLL_STARTER_FORM' AND section = 'ACCOUNT_INFORMATION';
UPDATE "0".modelfield set fieldSetStyle = 'slideDown-content group nobrd' WHERE noLabelFor is not null AND noLabelFor != '' AND form_id = 'PAYROLL_STARTER_FORM';
UPDATE "0".modelfield set halfSetStyle = 'halfSet-1 left' WHERE form_id = 'PAYROLL_STARTER_FORM' AND section = 'ACCOUNT_INFORMATION';
UPDATE "0".modelfield set halfSetStyle = '' WHERE noLabelFor is not null AND noLabelFor != '' AND form_id = 'PAYROLL_STARTER_FORM' AND section = 'ACCOUNT_INFORMATION';
UPDATE "0".modelfield set rowStyle = 'row hideCustomField' WHERE form_id = 'PAYROLL_STARTER_FORM' AND section = 'ACCOUNT_INFORMATION';
UPDATE "0".modelfield set fieldStyle = 'field' WHERE form_id = 'PAYROLL_STARTER_FORM' AND section = 'ACCOUNT_INFORMATION';

UPDATE "anv".modelfield set sectionStyle  = 'slideDown-box  group expand hideCustomField' WHERE form_id = 'PAYROLL_STARTER_FORM' AND section = 'ACCOUNT_INFORMATION';
UPDATE "anv".modelfield set fieldSetStyle = 'slideDown-content group labelLine' WHERE form_id = 'PAYROLL_STARTER_FORM' AND section = 'ACCOUNT_INFORMATION';
UPDATE "anv".modelfield set fieldSetStyle = 'slideDown-content group nobrd' WHERE noLabelFor is not null AND noLabelFor != '' AND form_id = 'PAYROLL_STARTER_FORM' AND section = 'ACCOUNT_INFORMATION';
UPDATE "anv".modelfield set halfSetStyle = 'halfSet-1 left' WHERE form_id = 'PAYROLL_STARTER_FORM';
UPDATE "anv".modelfield set halfSetStyle = '' WHERE noLabelFor is not null AND noLabelFor != '' AND form_id = 'PAYROLL_STARTER_FORM' AND section = 'ACCOUNT_INFORMATION';
UPDATE "anv".modelfield set rowStyle = 'row hideCustomField' WHERE form_id = 'PAYROLL_STARTER_FORM' AND section = 'ACCOUNT_INFORMATION';
UPDATE "anv".modelfield set fieldStyle = 'field' WHERE form_id = 'PAYROLL_STARTER_FORM' AND section = 'ACCOUNT_INFORMATION';