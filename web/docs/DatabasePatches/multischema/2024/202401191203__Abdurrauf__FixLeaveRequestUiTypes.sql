update "anv".modelfield set widget = 'RadioButton' where form_id = 'LEAVE_REQUEST_FORM' and field_id = 'TYPE';
update "anv".modelfield set widget = 'RadioButton' where form_id = 'LEAVE_REQUEST_FORM' and field_id = 'TAKE_LIVE_TYPE';
update "anv".modelfield set widget = 'LOOKUP' where form_id = 'LEAVE_REQUEST_FORM' and field_id = 'EMPLOYEES';
