

update modelfield set widget='DatePicker' where form_id='PROJECT_FORM' and field_id='DUE_DATE_REMINDER';
update modelfield set widget='UNKNOWN' where form_id='PROJECT_FORM' and field_id='CLIENT';

update "anv".modelfield set widget='DatePicker' where form_id='PROJECT_FORM' and field_id='DUE_DATE_REMINDER';
update "anv".modelfield set widget='UNKNOWN' where form_id='PROJECT_FORM' and field_id='CLIENT';