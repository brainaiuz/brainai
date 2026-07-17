DELETE FROM permission WHERE code = 'RFQ_SEND_EMAIL';
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
  VALUES ('RFQ_SEND_EMAIL', 'ACCOUNTING', 'Send Email', 13,
  (SELECT id FROM permission WHERE code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST'),'REQUEST_FOR_QUOTES');

DELETE FROM "0".permission_context WHERE permissioncode = 'RFQ_SEND_EMAIL' AND contextcode = 'REQUEST_FOR_PURCHASES';
insert into "0".permission_context (permissioncode, contextcode) values ('RFQ_SEND_EMAIL', 'REQUEST_FOR_PURCHASES');

DELETE FROM "0".rolepermission WHERE permissioncode = 'RFQ_SEND_EMAIL';
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('RFQ_SEND_EMAIL', 'ALLOW', 'ADMIN');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('RFQ_SEND_EMAIL', 'ALLOW', 'DR');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('RFQ_SEND_EMAIL', 'ALLOW', 'ACCOUNTANT');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('RFQ_SEND_EMAIL', 'ALLOW', 'MEM');


DELETE FROM "anv".permission_context WHERE permissioncode = 'RFQ_SEND_EMAIL' AND contextcode = 'REQUEST_FOR_PURCHASES';
insert into "anv".permission_context (permissioncode, contextcode) values ('RFQ_SEND_EMAIL', 'REQUEST_FOR_PURCHASES');

DELETE FROM "anv".rolepermission WHERE permissioncode = 'RFQ_SEND_EMAIL';
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('RFQ_SEND_EMAIL', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('RFQ_SEND_EMAIL', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('RFQ_SEND_EMAIL', 'ALLOW', 'ACCOUNTANT');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('RFQ_SEND_EMAIL', 'ALLOW', 'MEM');

