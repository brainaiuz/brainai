update "anv".modelfield set field_id='DATE_PERIOD' where field_id='START_DATE' and form_id='LEAVE_REQUEST_FORM';
update "anv".modelfield set field_id='APPROVER' where field_id='HR_MANAGER' and form_id='LEAVE_REQUEST_FORM';
update "anv".modelfield set hideincustomizeform=true where field_id='DUE_DATE' and form_id='LEAVE_REQUEST_FORM';
update "anv".modelfield set hideincustomizeform=true where field_id='TAKEN_FROM_ALLOWANCE' and form_id='LEAVE_REQUEST_FORM';
update "anv".modelfield set hideincustomizeform=true where field_id='PAID' and form_id='LEAVE_REQUEST_FORM';
update "anv".modelfield set hideincustomizeform=true where field_id='ATTACHMENTS' and form_id='LEAVE_REQUEST_FORM';

update "0".modelfield set field_id='DATE_PERIOD' where field_id='START_DATE' and form_id='LEAVE_REQUEST_FORM';
update "0".modelfield set field_id='APPROVER' where field_id='HR_MANAGER' and form_id='LEAVE_REQUEST_FORM';
update "0".modelfield set hideincustomizeform=true where field_id='DUE_DATE' and form_id='LEAVE_REQUEST_FORM';
update "0".modelfield set hideincustomizeform=true where field_id='TAKEN_FROM_ALLOWANCE' and form_id='LEAVE_REQUEST_FORM';
update "0".modelfield set hideincustomizeform=true where field_id='PAID' and form_id='LEAVE_REQUEST_FORM';
update "0".modelfield set hideincustomizeform=true where field_id='ATTACHMENTS' and form_id='LEAVE_REQUEST_FORM';

update "0_template".modelfield set field_id='DATE_PERIOD' where field_id='START_DATE' and form_id='LEAVE_REQUEST_FORM';
update "0_template".modelfield set field_id='APPROVER' where field_id='HR_MANAGER' and form_id='LEAVE_REQUEST_FORM';
update "0_template".modelfield set hideincustomizeform=true where field_id='DUE_DATE' and form_id='LEAVE_REQUEST_FORM';
update "0_template".modelfield set hideincustomizeform=true where field_id='TAKEN_FROM_ALLOWANCE' and form_id='LEAVE_REQUEST_FORM';
update "0_template".modelfield set hideincustomizeform=true where field_id='PAID' and form_id='LEAVE_REQUEST_FORM';
update "0_template".modelfield set hideincustomizeform=true where field_id='ATTACHMENTS' and form_id='LEAVE_REQUEST_FORM';