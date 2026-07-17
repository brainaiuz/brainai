delete
from "0".model
where formid = 'WORKFLOW_TELEGRAM_ALERT_FORM';
delete
from "anv".model
where formid = 'WORKFLOW_TELEGRAM_ALERT_FORM';
insert into "0".model(active, formid, title, viewname)
values (true, 'WORKFLOW_TELEGRAM_ALERT_FORM', 'Workflow Telegram Alert', 'workflowtelegramalert');
insert into "anv".model(active, formid, title, viewname)
values (true, 'WORKFLOW_TELEGRAM_ALERT_FORM', 'Workflow Telegram Alert', 'workflowtelegramalert');

delete
from "0".modelfield
where form_id = 'WORKFLOW_TELEGRAM_ALERT_FORM';


delete
from "0".customformsection
where form_id = 'WORKFLOW_TELEGRAM_ALERT_FORM';

insert into "0".modelfield(form_id, fsection, columntype, mandatory, forder, field_id)
values ('WORKFLOW_TELEGRAM_ALERT_FORM', 'INFORMATION', 'COL_1', true, 0, 'TELEGRAM_BOT'),
       ('WORKFLOW_TELEGRAM_ALERT_FORM', 'INFORMATION', 'COL_2', true, 1, 'RECEIVER');

INSERT INTO "0".customformsection (form_id, section, active, custom, sorder)
VALUES ('WORKFLOW_TELEGRAM_ALERT_FORM', 'INFORMATION', true, false, 1);

insert into "0".modelfield
    (form_id, fsection, columntype, mandatory, forder, field_id)
values ('WORKFLOW_TELEGRAM_ALERT_FORM', 'CONTENT', 'COL_1', true, 0, 'CONTENT');

INSERT INTO "0".customformsection (form_id, section, active, custom, sorder)
VALUES ('WORKFLOW_TELEGRAM_ALERT_FORM', 'CONTENT', true, false, 2);

insert into "0".modelfield
    (form_id, fsection, columntype, mandatory, forder, field_id)
values ('WORKFLOW_TELEGRAM_ALERT_FORM', 'WORKFLOW_TIME_BASED_HEADER', 'COL_1', true, 0, 'WORKFLOW_TIME_BASED');

INSERT INTO "0".customformsection (form_id, section, active, custom, sorder)
VALUES ('WORKFLOW_TELEGRAM_ALERT_FORM', 'WORKFLOW_TIME_BASED_HEADER', true, false, 3);






delete
from "anv".modelfield
where form_id = 'WORKFLOW_TELEGRAM_ALERT_FORM';


delete
from "anv".customformsection
where form_id = 'WORKFLOW_TELEGRAM_ALERT_FORM';

insert into "anv".modelfield(form_id, fsection, columntype, mandatory, forder, field_id)
values ('WORKFLOW_TELEGRAM_ALERT_FORM', 'INFORMATION', 'COL_1', true, 0, 'TELEGRAM_BOT'),
       ('WORKFLOW_TELEGRAM_ALERT_FORM', 'INFORMATION', 'COL_2', true, 1, 'RECEIVER');

INSERT INTO "anv".customformsection (form_id, section, active, custom, sorder)
VALUES ('WORKFLOW_TELEGRAM_ALERT_FORM', 'INFORMATION', true, false, 1);

insert into "anv".modelfield
(form_id, fsection, columntype, mandatory, forder, field_id)
values ('WORKFLOW_TELEGRAM_ALERT_FORM', 'CONTENT', 'COL_1', true, 0, 'CONTENT');

INSERT INTO "anv".customformsection (form_id, section, active, custom, sorder)
VALUES ('WORKFLOW_TELEGRAM_ALERT_FORM', 'CONTENT', true, false, 2);

insert into "anv".modelfield
(form_id, fsection, columntype, mandatory, forder, field_id)
values ('WORKFLOW_TELEGRAM_ALERT_FORM', 'WORKFLOW_TIME_BASED_HEADER', 'COL_1', true, 0, 'WORKFLOW_TIME_BASED');

INSERT INTO "anv".customformsection (form_id, section, active, custom, sorder)
VALUES ('WORKFLOW_TELEGRAM_ALERT_FORM', 'WORKFLOW_TIME_BASED_HEADER', true, false, 3);
