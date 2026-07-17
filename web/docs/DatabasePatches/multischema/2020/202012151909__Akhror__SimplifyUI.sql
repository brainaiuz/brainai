update "0".modelfield set hide = true where form_id in ('CLIENT_FORM', 'SUPPLIER_FORM') and section = 'CRM_ACCOUNT_INFORMATION' and field_id in ('CRM_ACCOUNT_FAX', 'CRM_ACCOUNT_OWNER');
update "0".modelfield set hide = true where form_id = 'BANK_ACCOUNT_FORM' and section = 'ACCOUNT_INFORMATION' and field_id = 'ACTIVE';
update "0".modelfield set hide = true where form_id = 'TASK_MAX_FORM' and section = 'DEPENDENCIES';
update "0".modelfield set hide = true where form_id = 'TASK_MAX_FORM' and field_id in ('CLIENT', 'CREATED_BY', 'CREATED_DATE', 'UPDATED_BY', 'UPDATED_DATE', 'PROJECT_MANAGER',
                                                                                      'BACKUP_MANAGERS', 'ACTUAL_START_DATE', 'ACTUAL_END_DATE', 'ACTUAL_TIME_SPENT', 'ESTIMATED_TIME',
                                                                                      'WAITING_HOURS', 'REJECTED_HOURS', 'ACTUAL_COST', 'ESTIMATED_COST');
update "0".modelfield set hide = true where form_id = 'PROJECT_FORM' and section IS NULL;

update "0".modelfield set hide = true where form_id = 'LEAD_FORM' and fsection = 'LEAD_INFORMATION' and field_id in ('CREATED_DATE', 'UPDATED_DATE', 'JOB_TITLE');
update "0".modelfield set columntype = 'COL_1' where form_id = 'LEAD_FORM' and fsection = 'LEAD_INFORMATION'
                                                      and field_id in ('ASSIGNEE', 'FIRST_NAME', 'LAST_NAME');
update "0".modelfield set columntype = 'COL_2' where form_id = 'LEAD_FORM' and fsection = 'LEAD_INFORMATION'
                                                      and field_id in ('CRM_ACCOUNT_NAME', 'LEAD_SOURCE', 'CRM_CAMPAIGN_NAME');
update "0".modelfield set columntype = 'COL_3' where form_id = 'LEAD_FORM' and fsection = 'LEAD_INFORMATION'
                                                      and field_id in ('STATUS', 'PHONE', 'EMAIL');

update "0".modelfield set forder = 0 where form_id = 'LEAD_FORM' and fsection = 'LEAD_INFORMATION' and field_id in ('ASSIGNEE','CRM_ACCOUNT_NAME', 'STATUS');
update "0".modelfield set forder = 1 where form_id = 'LEAD_FORM' and fsection = 'LEAD_INFORMATION' and field_id in ('FIRST_NAME','LEAD_SOURCE', 'PHONE');
update "0".modelfield set forder = 2 where form_id = 'LEAD_FORM' and fsection = 'LEAD_INFORMATION' and field_id in ('LAST_NAME','CRM_CAMPAIGN_NAME', 'EMAIL');


update "0".modelfield set hide = true where form_id = 'OPPORTUNITY_FORM' and fsection = 'OPPORTUNITY_INFORMATION' and field_id in ('CRM_OPPORTUNITY_EXPECTED_REVENUE', 'CRM_OPPORTUNITY_PROBABILITY');

update "0".modelfield set columntype = 'COL_1' where form_id = 'OPPORTUNITY_FORM' and fsection = 'OPPORTUNITY_INFORMATION'
                                                      and field_id in ('CRM_OPPORTUNITY_ASSIGNEE', 'CRM_OPPORTUNITY_NAME', 'CRM_OPPORTUNITY_ACCOUNT_NAME');
update "0".modelfield set columntype = 'COL_2' where form_id = 'OPPORTUNITY_FORM' and fsection = 'OPPORTUNITY_INFORMATION'
                                                      and field_id in ('CRM_OPPORTUNITY_STAGE', 'CRM_OPPORTUNITY_LEAD_SOURCE', 'CRM_OPPORTUNITY_CONTACT_NAME');
update "0".modelfield set columntype = 'COL_3' where form_id = 'OPPORTUNITY_FORM' and fsection = 'OPPORTUNITY_INFORMATION'
                                                      and field_id in ('CRM_OPPORTUNITY_AMOUNT', 'CRM_OPPORTUNITY_CLOSING_DATE');

update "0".modelfield set forder = 0 where form_id = 'OPPORTUNITY_FORM' and fsection = 'OPPORTUNITY_INFORMATION' and field_id in ('CRM_OPPORTUNITY_ASSIGNEE','CRM_OPPORTUNITY_STAGE', 'CRM_OPPORTUNITY_AMOUNT');
update "0".modelfield set forder = 1 where form_id = 'OPPORTUNITY_FORM' and fsection = 'OPPORTUNITY_INFORMATION' and field_id in ('CRM_OPPORTUNITY_NAME','CRM_OPPORTUNITY_LEAD_SOURCE', 'CRM_OPPORTUNITY_CLOSING_DATE');
update "0".modelfield set forder = 2 where form_id = 'OPPORTUNITY_FORM' and fsection = 'OPPORTUNITY_INFORMATION' and field_id in ('CRM_OPPORTUNITY_ACCOUNT_NAME','CRM_OPPORTUNITY_CONTACT_NAME');

update "0".modelfield set hide = true where form_id = 'ACCOUNT_FORM' and fsection = 'CRM_ACCOUNT_INFORMATION' and field_id in ('CRM_ACCOUNT_FAX');

update "0".modelfield set columntype = 'COL_1' where form_id = 'ACCOUNT_FORM' and fsection = 'CRM_ACCOUNT_INFORMATION'
                                                      and field_id in ('CRM_ACCOUNT_NUMBER', 'CRM_ACCOUNT_NAME');
update "0".modelfield set columntype = 'COL_2' where form_id = 'ACCOUNT_FORM' and fsection = 'CRM_ACCOUNT_INFORMATION'
                                                      and field_id in ('CRM_ACCOUNT_OWNER', 'PRIMARY_CONTACT');
update "0".modelfield set columntype = 'COL_3' where form_id = 'ACCOUNT_FORM' and fsection = 'CRM_ACCOUNT_INFORMATION'
                                                      and field_id in ('CRM_ACCOUNT_PHONE', 'CRM_ACCOUNT_EMAIL', 'CRM_ACCOUNT_WEBSITE');

update "0".modelfield set forder = 0 where form_id = 'ACCOUNT_FORM' and fsection = 'CRM_ACCOUNT_INFORMATION' and field_id in ('CRM_ACCOUNT_NUMBER','CRM_ACCOUNT_OWNER', 'CRM_ACCOUNT_PHONE');
update "0".modelfield set forder = 1 where form_id = 'ACCOUNT_FORM' and fsection = 'CRM_ACCOUNT_INFORMATION' and field_id in ('CRM_ACCOUNT_NAME','PRIMARY_CONTACT', 'CRM_ACCOUNT_EMAIL');
update "0".modelfield set forder = 2 where form_id = 'ACCOUNT_FORM' and fsection = 'CRM_ACCOUNT_INFORMATION' and field_id in ('CRM_ACCOUNT_WEBSITE');

update "0".modelfield set columntype = 'COL_1' where form_id = 'ACCOUNT_FORM' and fsection = 'CRM_ACCOUNT_FINANCIAL_INFORMATION'
                                                      and field_id in ('PAYMENT_METHOD', 'CLIENT_INVOICE_TERM');
update "0".modelfield set columntype = 'COL_2' where form_id = 'ACCOUNT_FORM' and fsection = 'CRM_ACCOUNT_FINANCIAL_INFORMATION'
                                                      and field_id in ('VAT_NUMBER', 'CLIENT_VAT');
update "0".modelfield set columntype = 'COL_3' where form_id = 'ACCOUNT_FORM' and fsection = 'CRM_ACCOUNT_FINANCIAL_INFORMATION'
                                                      and field_id in ('CURRENCY');

update "0".modelfield set forder = 0 where form_id = 'ACCOUNT_FORM' and fsection = 'CRM_ACCOUNT_FINANCIAL_INFORMATION' and field_id in ('PAYMENT_METHOD', 'VAT_NUMBER', 'CURRENCY');
update "0".modelfield set forder = 1 where form_id = 'ACCOUNT_FORM' and fsection = 'CRM_ACCOUNT_FINANCIAL_INFORMATION' and field_id in ('CLIENT_INVOICE_TERM', 'CLIENT_VAT');

update "0".modelfield set columntype = 'COL_2' where form_id = 'CONTACT_FORM' and fsection = 'CONTACT_INFORMATION'
                                                      and field_id in ('PHONE', 'EMAIL', 'CRM_CAMPAIGN_NAME');
update "0".modelfield set columntype = 'COL_3' where form_id = 'CONTACT_FORM' and fsection = 'CONTACT_INFORMATION'
                                                      and field_id in ('CRM_ACCOUNT_NAME', 'CRM_ACCOUNT_INDUSTRY');

update "0".modelfield set forder = 0 where form_id = 'CONTACT_FORM' and fsection = 'CONTACT_INFORMATION' and field_id in ('PHONE', 'CRM_ACCOUNT_NAME');
update "0".modelfield set forder = 1 where form_id = 'CONTACT_FORM' and fsection = 'CONTACT_INFORMATION' and field_id in ('EMAIL', 'CRM_ACCOUNT_INDUSTRY');
update "0".modelfield set forder = 2 where form_id = 'CONTACT_FORM' and fsection = 'CONTACT_INFORMATION' and field_id in ('CRM_CAMPAIGN_NAME');

update "0".modelfield set hide = true where form_id = 'CASE_FORM' and fsection = 'CASE_INFORMATION' and field_id in ('RESOLVER');

update "0".modelfield set columntype = 'COL_2' where form_id = 'CASE_FORM' and fsection = 'CASE_INFORMATION'
                                                      and field_id in ('TYPE');
update "0".modelfield set columntype = 'COL_3' where form_id = 'CASE_FORM' and fsection = 'CASE_INFORMATION'
                                                      and field_id in ('CASE_ORIGIN', 'CASE_REASON');

update "0".modelfield set forder = 1 where form_id = 'CASE_FORM' and fsection = 'CASE_INFORMATION' and field_id in ('TYPE', 'CASE_REASON');
update "0".modelfield set forder = 0 where form_id = 'CASE_FORM' and fsection = 'CASE_INFORMATION' and field_id in ('CASE_ORIGIN');

update "0".modelfield set hide = true where form_id = 'PROJECT_FORM' and fsection = 'DETAILS' and field_id in ('DUE_DATE_REMINDER');

update "0".modelfield set columntype = 'COL_3', forder = 1 where form_id = 'PROJECT_FORM' and fsection = 'DETAILS' and field_id = 'DESCRIPTION';
update "0".modelfield set columntype = 'COL_1' where form_id = 'PROJECT_FORM' and fsection = 'DETAILS' and field_id = 'CLIENT';

update "0".modelfield set hide = true where form_id = 'TASK_MAX_FORM' and field_id = 'BILLIBLE';
update "0".modelfield set hide = true where form_id = 'OPPORTUNITY_FORM' and fsection = 'OPPORTUNITY_INFORMATION' and field_id = 'TAX_CALC_TYPE';
update "0".modelfield set fsection = 'ACCOUNT_INFORMATION', columntype = 'COL_2' where form_id = 'BANK_ACCOUNT_FORM' and field_id = 'PHONE_NUMBER';





