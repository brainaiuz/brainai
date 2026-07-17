delete from "0".customformsection where form_id='LEAVE_REQUEST_FORM' and section='ATTACHMENTS';
delete from "0".modelfield where form_id='LEAVE_REQUEST_FORM' and field_id='ATTACHMENTS';
update "0".customformsection set active=false where form_id='LEAVE_REQUEST_FORM' and section='ADDITIONAL_INFORMATION';
update "0".customformsection set active=false where form_id='LEAVE_REQUEST_FORM' and section='SEND_NOTIFICATION';

delete from "anv".customformsection where form_id='LEAVE_REQUEST_FORM' and section='ATTACHMENTS';
delete from "anv".modelfield where form_id='LEAVE_REQUEST_FORM' and field_id='ATTACHMENTS';
update "anv".customformsection set active=false where form_id='LEAVE_REQUEST_FORM' and section='ADDITIONAL_INFORMATION';
update "anv".customformsection set active=false where form_id='LEAVE_REQUEST_FORM' and section='SEND_NOTIFICATION';

delete from "0_template".customformsection where form_id='LEAVE_REQUEST_FORM' and section='ATTACHMENTS';
delete from "0_template".modelfield where form_id='LEAVE_REQUEST_FORM' and field_id='ATTACHMENTS';
update "0_template".customformsection set active=false where form_id='LEAVE_REQUEST_FORM' and section='ADDITIONAL_INFORMATION';
update "0_template".customformsection set active=false where form_id='LEAVE_REQUEST_FORM' and section='SEND_NOTIFICATION';