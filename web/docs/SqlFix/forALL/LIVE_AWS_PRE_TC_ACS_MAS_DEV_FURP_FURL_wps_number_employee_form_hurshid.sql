


update modelfield set section='BANK_ACCOUNT_INFORMATION', sorder=(select max(sorder)+1 from modelfield where form_ID='HRMS_EMPLOYEE_FORM' and section='BANK_ACCOUNT_INFORMATION')
where form_ID='HRMS_EMPLOYEE_FORM' and field_id='WPS_NUMBER';

update modelfield set sorder=sorder+1
where form_ID='HRMS_EMPLOYEE_FORM' and sorder >= (select sorder from modelfield where form_ID='HRMS_EMPLOYEE_FORM' and field_ID='WPS_NUMBER') and  field_ID != 'WPS_NUMBER';
