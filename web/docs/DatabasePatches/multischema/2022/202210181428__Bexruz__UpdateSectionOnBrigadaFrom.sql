update "anv".customformsection set section = 'NOTES_AND_ATTACHMENTS' where section = 'ATTACHMENTS' and form_id = 'BRIGADA_FORM';
update "anv".modelfield set fsection = 'NOTES_AND_ATTACHMENTS' where form_id = 'BRIGADA_FORM' and field_id = 'PROJECT_NOTE';
update "anv".modelfield set fsection = 'NOTES_AND_ATTACHMENTS' where form_id = 'BRIGADA_FORM' and field_id = 'ATTACHMENTS';

