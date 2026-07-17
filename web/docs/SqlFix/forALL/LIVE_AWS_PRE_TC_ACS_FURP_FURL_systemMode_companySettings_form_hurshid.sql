
INSERT INTO public.modelfield (field_id, form_id, section, sorder, widget)
  select 'CS_KPI_MODE', 'COMPANY_SETTINGS_FORM', 'CS_COMPANY_SETTINGS', (select (sorder)+1 from modelfield where form_ID='COMPANY_SETTINGS_FORM' and field_ID='CS_COMPANY_BBC_EMAIL'), 'UNKNOWN'
  from modelfield
  where not exists (select id from modelfield where form_id = 'COMPANY_SETTINGS_FORM' and field_id = 'CS_KPI_MODE') limit 1;

update modelfield set sorder=sorder+1
where form_ID='COMPANY_SETTINGS_FORM' and sorder >= (select sorder from modelfield where form_ID='COMPANY_SETTINGS_FORM' and field_ID='CS_KPI_MODE') and  field_ID != 'CS_KPI_MODE';
