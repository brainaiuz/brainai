DELETE FROM "0".modelfield WHERE form_id='COMPANY_SETTINGS_FORM' AND field_id='SHOW_POWERED_BY_IN_PDF';
INSERT INTO "0".modelfield
(disableupdate, field_id, form_id, fullwidth, hide, hideincustomizeform, iscustomfield,
isentityfield, mandatory, place, section,
sorder,
split, systemdisable, systemmandatory, type, widget, isworkflowattribute, columntype,
forder,
fsection, usableByWorkflow)
VALUES
('false', 'SHOW_POWERED_BY_IN_PDF', 'COMPANY_SETTINGS_FORM', 'false', 'false', 'false', 'false',
'false', 'false', 0, 'CS_COMPANY_SETTINGS',
(SELECT COALESCE(MAX(sorder), 0) + 1 FROM "anv".modelfield WHERE form_id='COMPANY_SETTINGS_FORM' AND section='CS_COMPANY_SETTINGS'),
'false', 'false', 'false', 'text', 'UNKNOWN', 'false', 'COL_1',
(SELECT COALESCE(MAX(forder), 0) + 1 FROM "anv".modelfield WHERE form_id='COMPANY_SETTINGS_FORM' AND fsection='CS_COMPANY_SETTINGS'),
'CS_COMPANY_SETTINGS', false);


DELETE FROM "anv".modelfield WHERE form_id='COMPANY_SETTINGS_FORM' AND field_id='SHOW_POWERED_BY_IN_PDF';
INSERT INTO "anv".modelfield
(disableupdate, field_id, form_id, fullwidth, hide, hideincustomizeform, iscustomfield,
isentityfield, mandatory, place, section,
sorder,
split, systemdisable, systemmandatory, type, widget, isworkflowattribute, columntype,
forder,
fsection, usableByWorkflow)
VALUES
('false', 'SHOW_POWERED_BY_IN_PDF', 'COMPANY_SETTINGS_FORM', 'false', 'false', 'false', 'false',
'false', 'false', 0, 'CS_COMPANY_SETTINGS',
(SELECT COALESCE(MAX(sorder), 0) + 1 FROM "anv".modelfield WHERE form_id='COMPANY_SETTINGS_FORM' AND section='CS_COMPANY_SETTINGS'),
'false', 'false', 'false', 'text', 'UNKNOWN', 'false', 'COL_1',
(SELECT COALESCE(MAX(forder), 0) + 1 FROM "anv".modelfield WHERE form_id='COMPANY_SETTINGS_FORM' AND fsection='CS_COMPANY_SETTINGS'),
'CS_COMPANY_SETTINGS', false);