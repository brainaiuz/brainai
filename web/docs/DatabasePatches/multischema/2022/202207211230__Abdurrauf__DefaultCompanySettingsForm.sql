DELETE FROM "anv".customformsection where form_id = 'COMPANY_SETTINGS_FORM' and section = 'CS_COMPANY_DETAILS';
DELETE FROM "anv".customformsection where form_id = 'COMPANY_SETTINGS_FORM' and section = 'ADDRESS_INFORMATION';
DELETE FROM "anv".customformsection where form_id = 'COMPANY_SETTINGS_FORM' and section = 'CS_COMPANY_SETTINGS';
DELETE FROM "anv".customformsection where form_id = 'COMPANY_SETTINGS_FORM' and section = 'COMPANY_LOGOS';
DELETE FROM "anv".customformsection where form_id = 'COMPANY_SETTINGS_FORM' and section = 'SYSTEM_ACCESS_DETAILS';
DELETE FROM "anv".customformsection where form_id = 'COMPANY_SETTINGS_FORM' and section = 'CS_FINANCIAL_WIDGET';
DELETE FROM "anv".customformsection where form_id = 'COMPANY_SETTINGS_FORM' and section = 'ENABLE_STORAGE_TYPES';
DELETE FROM "anv".customformsection where form_id = 'COMPANY_SETTINGS_FORM' and section = 'ADDITIONAL_INFORMATION';

INSERT INTO "anv".customformsection (active, custom, expanded, form_id, section, sorder) VALUES (true, false, true, 'COMPANY_SETTINGS_FORM', 'CS_COMPANY_DETAILS', 0);
INSERT INTO "anv".customformsection (active, custom, expanded, form_id, section, sorder) VALUES (true, false, true, 'COMPANY_SETTINGS_FORM', 'ADDRESS_INFORMATION', 1);
INSERT INTO "anv".customformsection (active, custom, expanded, form_id, section, sorder) VALUES (true, false, true, 'COMPANY_SETTINGS_FORM', 'CS_COMPANY_SETTINGS', 2);
INSERT INTO "anv".customformsection (active, custom, expanded, form_id, section, sorder) VALUES (true, false, false, 'COMPANY_SETTINGS_FORM', 'COMPANY_LOGOS', 3);
INSERT INTO "anv".customformsection (active, custom, expanded, form_id, section, sorder) VALUES (true, false, false, 'COMPANY_SETTINGS_FORM', 'SYSTEM_ACCESS_DETAILS', 4);
INSERT INTO "anv".customformsection (active, custom, expanded, form_id, section, sorder) VALUES (true, false, false, 'COMPANY_SETTINGS_FORM', 'CS_FINANCIAL_WIDGET', 5);
INSERT INTO "anv".customformsection (active, custom, expanded, form_id, section, sorder) VALUES (true, false, false, 'COMPANY_SETTINGS_FORM', 'ENABLE_STORAGE_TYPES', 6);
INSERT INTO "anv".customformsection (active, custom, expanded, form_id, section, sorder) VALUES (true, false, false, 'COMPANY_SETTINGS_FORM', 'ADDITIONAL_INFORMATION', 7);

UPDATE "anv".modelfield SET hide = true where form_id = 'COMPANY_SETTINGS_FORM';

UPDATE "anv".modelfield SET fsection = 'ADDRESS_INFORMATION', columntype = 'COL_1', hide = false, hideincustomizeform = false, forder = 0 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'BILLING_ADDRESS';
UPDATE "anv".modelfield SET fsection = 'ADDRESS_INFORMATION', columntype = 'COL_2', hide = false, hideincustomizeform = false, forder = 0 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'MAILING_ADDRESS';
UPDATE "anv".modelfield SET fsection = 'ADDRESS_INFORMATION', columntype = 'COL_1', hide = false, hideincustomizeform = false, forder = 1 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'CS_ADDRESS_SAME';
UPDATE "anv".modelfield SET fsection = 'ADDRESS_INFORMATION', columntype = 'COL_1', hide = false, hideincustomizeform = true, forder = 0 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'CRM_ACCOUNT_BILLING_ADDRESS';
UPDATE "anv".modelfield SET fsection = 'ADDRESS_INFORMATION', columntype = 'COL_1', hide = false, hideincustomizeform = true, forder = 0 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'CRM_ACCOUNT_SHIPPING_ADDRESS';
UPDATE "anv".modelfield SET fsection = 'ADDRESS_INFORMATION', columntype = 'COL_1', hide = false, hideincustomizeform = true, forder = 0 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'HIDDEN_FIELD';

UPDATE "anv".modelfield SET fsection = 'COMPANY_LOGOS', columntype = 'COL_1', hide = false, hideincustomizeform = false, forder = 0 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'PDF_LOGO';
UPDATE "anv".modelfield SET fsection = 'COMPANY_LOGOS', columntype = 'COL_1', hide = false, hideincustomizeform = true, forder = 0 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'COMPANY_LOGO_LABEL';
UPDATE "anv".modelfield SET fsection = 'COMPANY_LOGOS', columntype = 'COL_1', hide = false, hideincustomizeform = true, forder = 0 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'COMPANY_LOGO';
UPDATE "anv".modelfield SET fsection = 'COMPANY_LOGOS', columntype = 'COL_1', hide = false, hideincustomizeform = true, forder = 0 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'PDF_LOGO_LABEL';

UPDATE "anv".modelfield SET fsection = 'CS_COMPANY_DETAILS', columntype = 'COL_1', hide = false, hideincustomizeform = false, forder = 0 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'CS_COMPANY_NAME';
UPDATE "anv".modelfield SET fsection = 'CS_COMPANY_DETAILS', columntype = 'COL_2', hide = false, hideincustomizeform = false, forder = 0 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'CS_LICENSE_NO';
UPDATE "anv".modelfield SET fsection = 'CS_COMPANY_DETAILS', columntype = 'COL_3', hide = false, hideincustomizeform = false, forder = 0 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'PHONE';
UPDATE "anv".modelfield SET fsection = 'CS_COMPANY_DETAILS', columntype = 'COL_1', hide = false, hideincustomizeform = false, forder = 1 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'INDUSTRY';
UPDATE "anv".modelfield SET fsection = 'CS_COMPANY_DETAILS', columntype = 'COL_2', hide = false, hideincustomizeform = false, forder = 1 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'CS_LICENSE_START_DATE';
UPDATE "anv".modelfield SET fsection = 'CS_COMPANY_DETAILS', columntype = 'COL_3', hide = false, hideincustomizeform = false, forder = 1 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'MOBILE_NUMBER';
UPDATE "anv".modelfield SET fsection = 'CS_COMPANY_DETAILS', columntype = 'COL_1', hide = false, hideincustomizeform = false, forder = 2 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'CS_NO_OF_EMPLOYEES';
UPDATE "anv".modelfield SET fsection = 'CS_COMPANY_DETAILS', columntype = 'COL_2', hide = false, hideincustomizeform = false, forder = 2 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'EXPIRATION_DATE';
UPDATE "anv".modelfield SET fsection = 'CS_COMPANY_DETAILS', columntype = 'COL_3', hide = false, hideincustomizeform = false, forder = 2 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'EMAIL';
UPDATE "anv".modelfield SET fsection = 'CS_COMPANY_DETAILS', columntype = 'COL_3', hide = false, hideincustomizeform = false, forder = 3 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'CS_WEBSITE';

UPDATE "anv".modelfield SET fsection = 'CS_COMPANY_SETTINGS', columntype = 'COL_1', hide = false, hideincustomizeform = false, forder = 0 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'CS_WEEK_START_ON';
UPDATE "anv".modelfield SET fsection = 'CS_COMPANY_SETTINGS', columntype = 'COL_1', hide = false, hideincustomizeform = true, forder = 0 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'CS_KPI_MODE';
UPDATE "anv".modelfield SET fsection = 'CS_COMPANY_SETTINGS', columntype = 'COL_1', hide = false, hideincustomizeform = true, forder = 0 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'CS_SYTEM_THEME_COLOR';
UPDATE "anv".modelfield SET fsection = 'CS_COMPANY_SETTINGS', columntype = 'COL_2', hide = false, hideincustomizeform = false, forder = 0 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'CS_SSL';
UPDATE "anv".modelfield SET fsection = 'CS_COMPANY_SETTINGS', columntype = 'COL_3', hide = false, hideincustomizeform = false, forder = 0 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'CS_COMPANY_TIME_ZONE';
UPDATE "anv".modelfield SET fsection = 'CS_COMPANY_SETTINGS', columntype = 'COL_1', hide = false, hideincustomizeform = false, forder = 1 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'LANGUAGE';
UPDATE "anv".modelfield SET fsection = 'CS_COMPANY_SETTINGS', columntype = 'COL_2', hide = false, hideincustomizeform = false, forder = 1 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'CS_COMPANY_BBC_EMAIL';
UPDATE "anv".modelfield SET fsection = 'CS_COMPANY_SETTINGS', columntype = 'COL_3', hide = false, hideincustomizeform = false, forder = 1 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'CS_SHORT_DATE_FORMAT';
UPDATE "anv".modelfield SET fsection = 'CS_COMPANY_SETTINGS', columntype = 'COL_1', hide = false, hideincustomizeform = false, forder = 2 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'CS_PDF_FONT_TYPE';
UPDATE "anv".modelfield SET fsection = 'CS_COMPANY_SETTINGS', columntype = 'COL_2', hide = false, hideincustomizeform = false, forder = 2 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'SHOW_ACCOUNTING';
UPDATE "anv".modelfield SET fsection = 'CS_COMPANY_SETTINGS', columntype = 'COL_3', hide = false, hideincustomizeform = false, forder = 2 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'CS_LONG_DATE_FORMAT';

UPDATE "anv".modelfield SET fsection = 'CS_FINANCIAL_WIDGET', columntype = 'COL_1', hide = false, hideincustomizeform = false, forder = 0 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'CURRENCY';
UPDATE "anv".modelfield SET fsection = 'CS_FINANCIAL_WIDGET', columntype = 'COL_2', hide = false, hideincustomizeform = false, forder = 0 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'CS_FINANCIAL_YEAR_END';
UPDATE "anv".modelfield SET fsection = 'CS_FINANCIAL_WIDGET', columntype = 'COL_3', hide = false, hideincustomizeform = false, forder = 0 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'CS_CONVERSION_DATE';

UPDATE "anv".modelfield SET fsection = 'SYSTEM_ACCESS_DETAILS', columntype = 'COL_1', hide = false, hideincustomizeform = false, forder = 0 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'IP_ADDRESS';
UPDATE "anv".modelfield SET fsection = 'SYSTEM_ACCESS_DETAILS', columntype = 'COL_2', hide = false, hideincustomizeform = false, forder = 0 where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'PASSWORD_EXPIRES_IN';
