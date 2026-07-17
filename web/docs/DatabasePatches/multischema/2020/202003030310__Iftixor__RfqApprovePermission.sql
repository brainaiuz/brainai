DELETE FROM permission WHERE code = 'RFQ_APPROVE';
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
  VALUES ('RFQ_APPROVE', 'ACCOUNTING', 'Approve', 14,
  (SELECT id FROM permission WHERE code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST'),'REQUEST_FOR_QUOTES');

DELETE FROM "0".permission_context WHERE permissioncode = 'RFQ_APPROVE' AND contextcode = 'ACCOUNTING';
insert into "0".permission_context (permissioncode, contextcode) values ('RFQ_APPROVE', 'ACCOUNTING');

DELETE FROM "0".rolepermission WHERE permissioncode = 'RFQ_APPROVE';
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('RFQ_APPROVE', 'ALLOW', 'ADMIN');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('RFQ_APPROVE', 'ALLOW', 'DR');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('RFQ_APPROVE', 'ALLOW', 'ACCOUNTANT');


DELETE FROM "anv".permission_context WHERE permissioncode = 'RFQ_APPROVE' AND contextcode = 'ACCOUNTING';
insert into "anv".permission_context (permissioncode, contextcode) values ('RFQ_APPROVE', 'ACCOUNTING');

DELETE FROM "anv".rolepermission WHERE permissioncode = 'RFQ_APPROVE';
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('RFQ_APPROVE', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('RFQ_APPROVE', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('RFQ_APPROVE', 'ALLOW', 'ACCOUNTANT');
