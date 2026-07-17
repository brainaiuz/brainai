delete from "0".reference  where code = '_WORKFLOW_MODULE_ACCOUNT';
insert into "0".reference (code, name, sorder, parentid)
values ('_WORKFLOW_MODULE_ACCOUNT', 'Crm Account', (select max(sorder) from "0".reference where parentid = (select id from "0".reference where code = '_WORKFLOW_MODULE')),
(select id from "0".reference where code = '_WORKFLOW_MODULE'));

delete from "anv".reference  where code = '_WORKFLOW_MODULE_ACCOUNT';
insert into "anv".reference (code, name, sorder, parentid)
values ('_WORKFLOW_MODULE_ACCOUNT', 'Crm Account', (select max(sorder) from "anv".reference where parentid = (select id from "anv".reference where code = '_WORKFLOW_MODULE')),
(select id from "anv".reference where code = '_WORKFLOW_MODULE'));

update "0".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'CRM@ACCOUNT_OWNER'  where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_OWNER';
update "0".modelfield set usableByWorkflow = true, disableUpdate = true, type = 'Text'   where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_NAME';
update "0".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'CRM@ACCOUNT'  where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_PARENT';
update "0".modelfield set usableByWorkflow = true, disableUpdate = true, type = 'Text'   where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_NUMBER';
update "0".modelfield set usableByWorkflow = true, disableUpdate = true, type = 'Text', source = 'CRM@CONTACT'  where form_id = 'ACCOUNT_FORM' and field_id = 'PRIMARY_CONTACT';
update "0".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'REFERENCE@_CRM_ACCOUNT_TYPE' where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_TYPE';
update "0".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'REFERENCE@_COMPANY_WORKAREA' where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_INDUSTRY';
update "0".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'REFERENCE@_OWNERSHIP'        where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_OWNERSHIP';
update "0".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text'  where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_EMAIL';
update "0".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text'  where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_PHONE';
update "0".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text'  where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_WEBSITE';
update "0".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text'  where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_FAX';
update "0".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'REFERENCE@CONTACT_ORGANIZATION_TYPES' where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_ORGANIZATION_TYPE';
update "0".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'REFERENCE@CONTACT_NUMBER_OF_EMPLOYEES' where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_NUMBER_OF_EMPLOYEE';
update "0".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'REFERENCE@CONTACT_ANNUAL_REVENUE' where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_ANNUAL_REVENUE';
update "0".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'REFERENCE@_LEAD_RATING' where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_RATING';
update "0".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'ACCOUNTING@CURRENCY'  where form_id = 'ACCOUNT_FORM' and field_id = 'CURRENCY';
update "0".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text'  where form_id = 'ACCOUNT_FORM' and field_id = 'VAT_NUMBER';
update "0".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'CRM@PAYMENT_METHOD' where form_id = 'ACCOUNT_FORM' and field_id = 'PAYMENT_METHOD';
update "0".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text'  where form_id = 'ACCOUNT_FORM' and field_id = 'REGISTRATION_NUMBER';
update "0".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'ACCOUNTING@CLIENT_INVOICE_TERM'  where form_id = 'ACCOUNT_FORM' and field_id = 'CLIENT_INVOICE_TERM';

update modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'CRM@ACCOUNT_OWNER'  where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_OWNER';
update modelfield set usableByWorkflow = true, disableUpdate = true, type = 'Text'   where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_NAME';
update modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'CRM@ACCOUNT'  where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_PARENT';
update modelfield set usableByWorkflow = true, disableUpdate = true, type = 'Text'   where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_NUMBER';
update modelfield set usableByWorkflow = true, disableUpdate = true, type = 'Text', source = 'CRM@CONTACT'  where form_id = 'ACCOUNT_FORM' and field_id = 'PRIMARY_CONTACT';
update modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'REFERENCE@_CRM_ACCOUNT_TYPE' where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_TYPE';
update modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'REFERENCE@_COMPANY_WORKAREA' where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_INDUSTRY';
update modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'REFERENCE@_OWNERSHIP'        where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_OWNERSHIP';
update modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text'  where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_EMAIL';
update modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text'  where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_PHONE';
update modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text'  where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_WEBSITE';
update modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text'  where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_FAX';
update modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'REFERENCE@CONTACT_ORGANIZATION_TYPES' where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_ORGANIZATION_TYPE';
update modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'REFERENCE@CONTACT_NUMBER_OF_EMPLOYEES' where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_NUMBER_OF_EMPLOYEE';
update modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'REFERENCE@CONTACT_ANNUAL_REVENUE' where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_ANNUAL_REVENUE';
update modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'REFERENCE@_LEAD_RATING' where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_RATING';
update modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'ACCOUNTING@CURRENCY'  where form_id = 'ACCOUNT_FORM' and field_id = 'CURRENCY';
update modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text'  where form_id = 'ACCOUNT_FORM' and field_id = 'VAT_NUMBER';
update modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'CRM@PAYMENT_METHOD' where form_id = 'ACCOUNT_FORM' and field_id = 'PAYMENT_METHOD';
update modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text'  where form_id = 'ACCOUNT_FORM' and field_id = 'REGISTRATION_NUMBER';
update modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'ACCOUNTING@CLIENT_INVOICE_TERM'  where form_id = 'ACCOUNT_FORM' and field_id = 'CLIENT_INVOICE_TERM';

update "anv".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'CRM@ACCOUNT_OWNER'  where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_OWNER';
update "anv".modelfield set usableByWorkflow = true, disableUpdate = true, type = 'Text'   where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_NAME';
update "anv".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'CRM@ACCOUNT'  where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_PARENT';
update "anv".modelfield set usableByWorkflow = true, disableUpdate = true, type = 'Text'   where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_NUMBER';
update "anv".modelfield set usableByWorkflow = true, disableUpdate = true, type = 'Text', source = 'CRM@CONTACT'  where form_id = 'ACCOUNT_FORM' and field_id = 'PRIMARY_CONTACT';
update "anv".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'REFERENCE@_CRM_ACCOUNT_TYPE' where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_TYPE';
update "anv".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'REFERENCE@_COMPANY_WORKAREA' where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_INDUSTRY';
update "anv".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'REFERENCE@_OWNERSHIP'        where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_OWNERSHIP';
update "anv".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text'  where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_EMAIL';
update "anv".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text'  where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_PHONE';
update "anv".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text'  where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_WEBSITE';
update "anv".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text'  where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_FAX';
update "anv".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'REFERENCE@CONTACT_ORGANIZATION_TYPES' where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_ORGANIZATION_TYPE';
update "anv".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'REFERENCE@CONTACT_NUMBER_OF_EMPLOYEES' where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_NUMBER_OF_EMPLOYEE';
update "anv".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'REFERENCE@CONTACT_ANNUAL_REVENUE' where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_ANNUAL_REVENUE';
update "anv".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'REFERENCE@_LEAD_RATING' where form_id = 'ACCOUNT_FORM' and field_id = 'CRM_ACCOUNT_RATING';
update "anv".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'ACCOUNTING@CURRENCY'  where form_id = 'ACCOUNT_FORM' and field_id = 'CURRENCY';
update "anv".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text'  where form_id = 'ACCOUNT_FORM' and field_id = 'VAT_NUMBER';
update "anv".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'CRM@PAYMENT_METHOD' where form_id = 'ACCOUNT_FORM' and field_id = 'PAYMENT_METHOD';
update "anv".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text'  where form_id = 'ACCOUNT_FORM' and field_id = 'REGISTRATION_NUMBER';
update "anv".modelfield set usableByWorkflow = true, disableUpdate = false, type = 'Text', source = 'ACCOUNTING@CLIENT_INVOICE_TERM'  where form_id = 'ACCOUNT_FORM' and field_id = 'CLIENT_INVOICE_TERM';