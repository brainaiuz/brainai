DELETE FROM permission WHERE code = 'RFQ_ADD_NEW_ACTIVITY_EVENT';
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
  VALUES ('RFQ_ADD_NEW_ACTIVITY_EVENT', 'ACCOUNTING', 'New Event', 11,
  (SELECT id FROM permission WHERE code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST'),'REQUEST_FOR_QUOTES');

DELETE FROM "0".permission_context WHERE permissioncode = 'RFQ_ADD_NEW_ACTIVITY_EVENT' AND contextcode = 'REQUEST_FOR_PURCHASES';
insert into "0".permission_context (permissioncode, contextcode) values ('RFQ_ADD_NEW_ACTIVITY_EVENT', 'REQUEST_FOR_PURCHASES');

DELETE FROM "0".rolepermission WHERE permissioncode = 'RFQ_ADD_NEW_ACTIVITY_EVENT';
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('RFQ_ADD_NEW_ACTIVITY_EVENT', 'ALLOW', 'ADMIN');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('RFQ_ADD_NEW_ACTIVITY_EVENT', 'ALLOW', 'DR');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('RFQ_ADD_NEW_ACTIVITY_EVENT', 'ALLOW', 'ACCOUNTANT');


DELETE FROM "anv".permission_context WHERE permissioncode = 'RFQ_ADD_NEW_ACTIVITY_EVENT' AND contextcode = 'REQUEST_FOR_PURCHASES';
insert into "anv".permission_context (permissioncode, contextcode) values ('RFQ_ADD_NEW_ACTIVITY_EVENT', 'REQUEST_FOR_PURCHASES');

DELETE FROM "anv".rolepermission WHERE permissioncode = 'RFQ_ADD_NEW_ACTIVITY_EVENT';
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('RFQ_ADD_NEW_ACTIVITY_EVENT', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('RFQ_ADD_NEW_ACTIVITY_EVENT', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('RFQ_ADD_NEW_ACTIVITY_EVENT', 'ALLOW', 'ACCOUNTANT');


DELETE FROM permission WHERE code = 'RFQ_ADD_NEW_ACTIVITY_LOG_A_CALL';
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
  VALUES ('RFQ_ADD_NEW_ACTIVITY_LOG_A_CALL', 'ACCOUNTING', 'New Log a Call', 12,
  (SELECT id FROM permission WHERE code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST'),'REQUEST_FOR_QUOTES');

DELETE FROM "0".permission_context WHERE permissioncode = 'RFQ_ADD_NEW_ACTIVITY_LOG_A_CALL' AND contextcode = 'REQUEST_FOR_QUOTES';
insert into "0".permission_context (permissioncode, contextcode) values ('RFQ_ADD_NEW_ACTIVITY_LOG_A_CALL', 'REQUEST_FOR_QUOTES');

DELETE FROM "0".rolepermission WHERE permissioncode = 'RFQ_ADD_NEW_ACTIVITY_LOG_A_CALL';
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('RFQ_ADD_NEW_ACTIVITY_LOG_A_CALL', 'ALLOW', 'ADMIN');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('RFQ_ADD_NEW_ACTIVITY_LOG_A_CALL', 'ALLOW', 'DR');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('RFQ_ADD_NEW_ACTIVITY_LOG_A_CALL', 'ALLOW', 'ACCOUNTANT');

DELETE FROM "anv".permission_context WHERE permissioncode = 'RFQ_ADD_NEW_ACTIVITY_LOG_A_CALL' AND contextcode = 'REQUEST_FOR_QUOTES';
insert into "anv".permission_context (permissioncode, contextcode) values ('RFQ_ADD_NEW_ACTIVITY_LOG_A_CALL', 'REQUEST_FOR_QUOTES');

DELETE FROM "anv".rolepermission WHERE permissioncode = 'RFQ_ADD_NEW_ACTIVITY_LOG_A_CALL';
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('RFQ_ADD_NEW_ACTIVITY_LOG_A_CALL', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('RFQ_ADD_NEW_ACTIVITY_LOG_A_CALL', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('RFQ_ADD_NEW_ACTIVITY_LOG_A_CALL', 'ALLOW', 'ACCOUNTANT');
