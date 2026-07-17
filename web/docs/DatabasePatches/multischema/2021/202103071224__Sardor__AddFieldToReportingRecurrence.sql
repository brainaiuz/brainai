delete from "anv".model where formid='TELEGRAM_RECURRENCE_FORM';
INSERT INTO "anv".model (active, formid, title, viewname, stepform, certificateform, customform)
VALUES (true, 'TELEGRAM_RECURRENCE_FORM','Telegram Recurrence', 'telegramrecurrenceform',false,false,false);

delete from "anv".customformsection where form_id='TELEGRAM_RECURRENCE_FORM' and section ='WORKFLOW_TIME_BASED_HEADER';
INSERT INTO "anv".customformsection (form_id, section, active, custom, sorder, expanded, label)
VALUES ('TELEGRAM_RECURRENCE_FORM', 'WORKFLOW_TIME_BASED_HEADER', true, false,3,false, null);

delete from "anv".customformsection where form_id='TELEGRAM_RECURRENCE_FORM' and section ='INFORMATION';
INSERT INTO "anv".customformsection (form_id, section, active, custom, sorder, expanded, label)
VALUES ('TELEGRAM_RECURRENCE_FORM', 'INFORMATION', true, false,1,false, null);

delete from "anv".customformsection where form_id='TELEGRAM_RECURRENCE_FORM' and section ='CONTENT';
INSERT INTO "anv".customformsection (form_id, section, active, custom, sorder, expanded, label)
VALUES ('TELEGRAM_RECURRENCE_FORM', 'CONTENT', true, false,2,false, null);

delete from "anv".modelfield where field_id='RECEIVER' and form_id='TELEGRAM_RECURRENCE_FORM' and fsection='INFORMATION';
INSERT INTO "anv".modelfield (field_id, form_id, hide, mandatory, columntype, forder, fsection)
VALUES ('RECEIVER', 'TELEGRAM_RECURRENCE_FORM',false,true,'COL_3',2,'INFORMATION');

delete from "anv".modelfield where field_id='TELEGRAM_BOT' and form_id='TELEGRAM_RECURRENCE_FORM' and fsection='INFORMATION';
INSERT INTO "anv".modelfield (field_id, form_id, hide, mandatory, columntype, forder, fsection)
VALUES ('TELEGRAM_BOT', 'TELEGRAM_RECURRENCE_FORM',false,true,'COL_2',1,'INFORMATION');

delete from "anv".modelfield where field_id='RECURRENCE_RULE_NAME' and form_id='TELEGRAM_RECURRENCE_FORM' and fsection='INFORMATION';
INSERT INTO "anv".modelfield (field_id, form_id, hide, mandatory,columntype, forder, fsection)
VALUES ('RECURRENCE_RULE_NAME', 'TELEGRAM_RECURRENCE_FORM',false,true,'COL_1',0,'INFORMATION');

delete from "anv".modelfield where field_id='WORKFLOW_TIME_BASED' and form_id='TELEGRAM_RECURRENCE_FORM' and fsection='WORKFLOW_TIME_BASED';
INSERT INTO "anv".modelfield (field_id, form_id, hide, mandatory,columntype, forder, fsection)
VALUES ('WORKFLOW_TIME_BASED', 'TELEGRAM_RECURRENCE_FORM',false,true,'COL_1',0,'WORKFLOW_TIME_BASED');

delete from "anv".modelfield where field_id='CONTENT' and form_id='TELEGRAM_RECURRENCE_FORM' and fsection='CONTENT';
INSERT INTO "anv".modelfield (field_id, form_id, hide,mandatory, columntype, forder, fsection)
VALUES ('CONTENT', 'TELEGRAM_RECURRENCE_FORM',false,true,'COL_1',0,'CONTENT');