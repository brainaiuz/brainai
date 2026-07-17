UPDATE modelfield set systemMandatory=true WHERE field_id in('CRM_OPPORTUNITY_STAGE','CRM_OPPORTUNITY_NAME','CRM_OPPORTUNITY_CLOSING_DATE','CRM_OPPORTUNITY_ACCOUNT_NAME') and form_id='OPPORTUNITY_FORM';
UPDATE modelfield set systemMandatory=true WHERE field_id in('LAST_NAME','FIRST_NAME') and form_id='LEAD_FORM';
UPDATE modelfield set systemMandatory=true WHERE field_id in('LAST_NAME','FIRST_NAME','EMAIL') and form_id='CONTACT_FORM';
UPDATE modelfield set systemMandatory=true WHERE field_id in('NAME','PROJECT') and form_id in ('TASK_MAX_FORM','TASK_MIN_FORM');