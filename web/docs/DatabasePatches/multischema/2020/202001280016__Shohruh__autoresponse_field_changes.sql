update "anv".autoresponse set smtpauth = false where smtpconnectionnotauth=true;
alter table "anv".autoresponse drop column if exists smtpconnectionnotauth;
