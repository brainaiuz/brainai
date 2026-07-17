--oldindan sign up uchun create qip quyilgan pustoy schemalargayam urilsin
DELETE FROM "0".modelfield;

INSERT INTO "0".modelfield(label, customlabel, form_id, field_id, sorder, widget, type, mandatory, systemmandatory, hide, iscustomfield, section, noLabelFor, noWrapperFor, fullWidth, sectionStyle, fieldSetStyle, halfSetStyle, rowStyle, fieldStyle, split, source, usableByWorkflow, disableUpdate, isEntityField, place, defaultvalue, helpMessage, hideInCustomizeForm, systemDisable, isWorkflowAttribute)
SELECT label, customlabel, form_id, field_id, sorder, widget, type, mandatory, systemmandatory, true, iscustomfield, section, noLabelFor, noWrapperFor, fullWidth, sectionStyle, fieldSetStyle, halfSetStyle, rowStyle, fieldStyle, split, source, usableByWorkflow, disableUpdate, isEntityField, place, defaultvalue, helpMessage, hideInCustomizeForm, systemDisable, isWorkflowAttribute
FROM modelfield where form_id in ('LEAD_FORM','OPPORTUNITY_FORM','ACCOUNT_FORM','HRMS_EMPLOYEE_FORM');

update "0".modelfield set hide = false where form_id = 'LEAD_FORM' and field_id in ('FIRST_NAME','LAST_NAME','EMAIL','PHONE','CRM_ACCOUNT_NAME','STATUS','CRM_NOTE');
update "0".modelfield set hide = false where form_id = 'OPPORTUNITY_FORM' and field_id in ('CRM_NOTE','CRM_OPPORTUNITY_NAME','CRM_OPPORTUNITY_AMOUNT','CRM_OPPORTUNITY_CLOSING_DATE','CRM_OPPORTUNITY_STAGE','CRM_OPPORTUNITY_PROBABILITY','CRM_OPPORTUNITY_EXPECTED_REVENUE');
update "0".modelfield set hide = false where form_id = 'ACCOUNT_FORM' and field_id in ('CRM_ACCOUNT_NAME','CRM_ACCOUNT_TYPE','CRM_ACCOUNT_EMAIL','CRM_ACCOUNT_PHONE','CRM_ACCOUNT_WEBSITE','CRM_ACCOUNT_NUMBER_OF_EMPLOYEE','CRM_ACCOUNT_BILLING_ADDRESS');
update "0".modelfield set hide = false where form_id = 'HRMS_EMPLOYEE_FORM' and field_id in ('FIRST_NAME','LAST_NAME','BIRTH_DAY','EMAIL','PHONE','DEPARTMENT','POSITION','ACCOUNT_ROLES','NO_ACCESS','ATTACHMENTS');
