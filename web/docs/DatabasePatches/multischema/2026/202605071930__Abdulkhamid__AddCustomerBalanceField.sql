
INSERT INTO "0".modelfield(form_ID, field_ID, sorder, label, mandatory, hide, isCustomField, section, defaultValue, widget, systemmandatory,  nowrapperfor, fullWidth, split,  disableUpdate, isWorkflowAttribute)
VALUES ('CLIENT_FORM', 'clientbalance', 37, 'Balance', false, false, false, 'CRM_ACCOUNT_FINANCIAL_INFORMATION', '', 'HTML', false,  '', false, false, false, false);

INSERT INTO "anv".modelfield(form_ID, field_ID, sorder, label, mandatory, hide, isCustomField, section, defaultValue, widget, systemmandatory,  nowrapperfor, fullWidth, split, disableUpdate, isWorkflowAttribute)
VALUES ('CLIENT_FORM', 'clientbalance', 37, 'Balance', false, false, false, 'CRM_ACCOUNT_FINANCIAL_INFORMATION', '', 'HTML', false, '', false, false, false, false);

UPDATE "0".modelfield SET forder = 2, hide = false, hideInCustomizeForm = false, columnType = 'COL_2', fsection = 'CRM_ACCOUNT_FINANCIAL_INFORMATION'
WHERE form_id = 'CLIENT_FORM' AND field_id = 'clientbalance';

UPDATE "anv".modelfield SET forder = 2, hide = false, hideInCustomizeForm = false, columnType = 'COL_2', fsection = 'CRM_ACCOUNT_FINANCIAL_INFORMATION'
WHERE form_id = 'CLIENT_FORM' AND field_id = 'clientbalance';
