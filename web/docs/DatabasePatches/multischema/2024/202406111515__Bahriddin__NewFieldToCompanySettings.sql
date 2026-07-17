insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('COMPANY_SETTINGS_FORM', 'NAME_FORMAT', false, false, 'COL_1', 'CS_COMPANY_SETTINGS', 2);

UPDATE companysystemsettings SET nameFormat = 'FIRST_LAST_MIDDLE' WHERE nameFormat IS NULL;