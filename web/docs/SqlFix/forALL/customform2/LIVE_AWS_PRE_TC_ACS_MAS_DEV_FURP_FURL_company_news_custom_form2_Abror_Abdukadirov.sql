delete from "anv".model  where formid = 'HRMS_COMPANY_NEWS_FORM';
delete from "anv".modelfield  where form_id = 'HRMS_COMPANY_NEWS_FORM';

delete from model  where formid = 'HRMS_COMPANY_NEWS_FORM';
delete from modelfield where form_ID ='HRMS_COMPANY_NEWS_FORM';

insert into model(formID, active, title) values('HRMS_COMPANY_NEWS_FORM',true, 'Company News');

insert into modelfield(form_ID,                   field_ID,                   sorder,  mandatory, hide,     systemmandatory,      section,          widget,           noLabelFor,                  type) values
                       ('HRMS_COMPANY_NEWS_FORM',  'NEWS_SUBJECT',             01,      true,      false,    true,                'HRMS_COMPANY_NEWS',   'TextBox',       '',                          'Text'),
                       ('HRMS_COMPANY_NEWS_FORM',  'NEWS_SHORT_DESCRIPTION',   02,      false,     false,    false,               'HRMS_COMPANY_NEWS',   'UNKNOWN',       '',                          'Text'),
                       ('HRMS_COMPANY_NEWS_FORM',  'NEWS_FULL_TEXT',           03,      true,      false,    true,                'HRMS_COMPANY_NEWS',   'UNKNOWN',       '',                          'Text'),
                       ('HRMS_COMPANY_NEWS_FORM',  'NEWS_AUTHOR',              04,      false,     false,    false,               'HRMS_COMPANY_NEWS',   'DropDown',       '',                          'Text'),
                       ('HRMS_COMPANY_NEWS_FORM',  'NEWS_PUBLISH_DATE',        05,      false,     false,    false,               'HRMS_COMPANY_NEWS',   'DatePicker',    '',                          'Date'),
                       ('HRMS_COMPANY_NEWS_FORM',  'NEWS_VISIBILITY',   	     06,      false,     false,    false,               'HRMS_COMPANY_NEWS',   'DropDown',      '',                          'Text'),
                       ('HRMS_COMPANY_NEWS_FORM',  'NEWS_CATEGORIES',          07,      false,     false,    false,               'HRMS_COMPANY_NEWS',   'DropDown',      '',                          'Text'),
                       ('HRMS_COMPANY_NEWS_FORM',  'LOCATION',                 08,      false,     false,    false,               'HRMS_COMPANY_NEWS',   'DropDown',      '',                          'Text'),
                       ('HRMS_COMPANY_NEWS_FORM',  'NEWS_UPLOAD_FILE',         08,      false,     false,    false,               'HRMS_COMPANY_NEWS',   'UNKNOWN',       '',                          'Text'),
                       ('HRMS_COMPANY_NEWS_FORM',  'COMMENT_BOX',              09,      false,     false,    false,               'COMMENTS',            'UNKNOWN',       'addForm,editForm,viewForm', 'Text');

update modelfield set sectionStyle = 'slideDown-box  group expand hideCustomField' where form_id='HRMS_COMPANY_NEWS_FORM';
update modelfield set fieldSetStyle = 'slideDown-content group labelLine' where form_id='HRMS_COMPANY_NEWS_FORM';
update modelfield set fieldSetStyle = 'slideDown-content group nobrd' where noLabelFor is not null and noLabelFor != '' and form_id='HRMS_COMPANY_NEWS_FORM';
update modelfield set halfSetStyle = 'halfSet-1' where form_id='HRMS_COMPANY_NEWS_FORM';
update modelfield set halfSetStyle = '' where noLabelFor is not null and noLabelFor != '' and form_id='HRMS_COMPANY_NEWS_FORM';
update modelfield set rowStyle = 'row hideCustomField' where form_id='HRMS_COMPANY_NEWS_FORM';
update modelfield set fieldStyle = 'field' where form_id='HRMS_COMPANY_NEWS_FORM' and section != 'COMMENTS';
