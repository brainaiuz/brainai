



delete from "anv".modelfield where form_id = 'OPPORTUNITY_FORM' and field_id = 'PROJECT_FIELD';
insert into "anv".modelfield(form_ID,field_ID, label, columntype,fsection,sorder, mandatory,hide,isCustomField,section,defaultValue, widget, systemmandatory,nolabelfor,nowrapperfor,fullWidth, split) values
('OPPORTUNITY_FORM', 'PROJECT_FIELD',  'Project', 'COL_1', 'OPPORTUNITY_INFORMATION',7,false,true ,false,'OPPORTUNITY_INFORMATION','','LOOKUP',  false,'','',false,   false);
