

UPDATE public.modelfield set helpmessage='Including international dialing code',systemmandatory=TRUE where form_id='COMPANY_SETTINGS_FORM' and field_id='PHONE';
UPDATE public.modelfield set helpmessage='Business e-email',systemmandatory=TRUE where form_id='COMPANY_SETTINGS_FORM' and field_id='EMAIL';



INSERT INTO public.modelfield (field_id, form_id, section, sorder, widget,helpmessage,systemmandatory)
 select 'MOBILE_NUMBER', 'COMPANY_SETTINGS_FORM', 'CS_COMPANY_DETAILS', (select (sorder)+1 from modelfield where form_ID='COMPANY_SETTINGS_FORM' and field_ID='PHONE'), 'TextBox','Including international dialing code',true
  from modelfield
  where not exists (select id from modelfield where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'MOBILE_NUMBER') limit 1;


update modelfield set sorder =(select (sorder)+1 from modelfield where form_ID='COMPANY_SETTINGS_FORM' and field_ID='PHONE') where field_ID='MOBILE_NUMBER' and sorder is null;


UPDATE modelfield set sorder=sorder+1
where form_ID='COMPANY_SETTINGS_FORM' and sorder >= (select sorder from modelfield where form_ID='COMPANY_SETTINGS_FORM' and field_ID='MOBILE_NUMBER') and  field_ID != 'MOBILE_NUMBER';
