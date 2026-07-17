DELETE FROM "model" WHERE formid = 'INTEGRATION_SETTINGS_FORM';
DELETE FROM "modelfield" WHERE form_id = 'INTEGRATION_SETTINGS_FORM';

INSERT INTO model (active, formID, title, viewname) VALUES (TRUE, 'INTEGRATION_SETTINGS_FORM', 'Integration Settings Form', 'integrationSettings');
INSERT INTO modelfield (form_ID, field_ID,              sorder, mandatory, hide, isCustomField, section,    defaultValue, widget, systemmandatory, nolabelfor, nowrapperfor, fullWidth, split)
VALUES
        ('INTEGRATION_SETTINGS_FORM', 'TARGET_URL',        1,     TRUE,    FALSE,   FALSE, 'TARGET_INTEGRATION', '',     'TextBox',      FALSE,        '',          '',         TRUE,   FALSE),
        ('INTEGRATION_SETTINGS_FORM', 'TARGET_USERNAME',   2,     TRUE,    FALSE,   FALSE, 'TARGET_INTEGRATION', '',     'TextBox',      FALSE,        '',          '',         TRUE,   FALSE),
        ('INTEGRATION_SETTINGS_FORM', 'TARGET_PASSWORD',   3,     TRUE,    FALSE,   FALSE, 'TARGET_INTEGRATION', '',     'TextBox',      FALSE,        '',          '',         TRUE,   FALSE),
        ('INTEGRATION_SETTINGS_FORM', 'TARGET_CONTROLLER', 4,     TRUE,    FALSE,   FALSE, 'TARGET_INTEGRATION', '',     'TextBox',      FALSE,        '',          '',         TRUE,   FALSE);
