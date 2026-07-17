delete from "anv".modelfield where field_id='RECURRENCE_STATUS' and form_id='TELEGRAM_RECURRENCE_FORM' and fsection='INFORMATION';
INSERT INTO "anv".modelfield (field_id, form_id, hide, mandatory,columntype, forder, fsection)
VALUES ('RECURRENCE_STATUS', 'TELEGRAM_RECURRENCE_FORM',false,true,'COL_1',3,'INFORMATION');