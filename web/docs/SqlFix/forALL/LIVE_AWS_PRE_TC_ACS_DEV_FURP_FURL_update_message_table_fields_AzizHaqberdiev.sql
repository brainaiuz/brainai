ALTER TABLE message ALTER COLUMN bcc TYPE text;
ALTER TABLE message ALTER COLUMN cc TYPE text;

ALTER TABLE workflow_message ALTER COLUMN bcc TYPE text;
ALTER TABLE workflow_message ALTER COLUMN cc TYPE text;

ALTER TABLE "anv".user_message ALTER COLUMN bcc TYPE text;
ALTER TABLE "anv".user_message ALTER COLUMN cc TYPE text;
