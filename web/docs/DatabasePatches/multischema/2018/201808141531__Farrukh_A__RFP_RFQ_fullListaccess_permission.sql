DELETE FROM permission WHERE code = 'ACCOUNTING_REQUEST_FOR_PURCHASE_LIST_FULL_ACCESS';
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
  VALUES ('ACCOUNTING_REQUEST_FOR_PURCHASE_LIST_FULL_ACCESS', 'ACCOUNTING', 'Request for Purchase List Full access', 1,
  (SELECT id FROM permission WHERE code = 'ACCOUNTING_REQUEST_FOR_PURCHASE_LIST'),'REQUEST_FOR_PURCHASES');

DELETE FROM "anv".permission_context WHERE permissioncode = 'ACCOUNTING_REQUEST_FOR_PURCHASE_LIST_FULL_ACCESS' AND contextcode = 'REQUEST_FOR_PURCHASES';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_REQUEST_FOR_PURCHASE_LIST_FULL_ACCESS', 'REQUEST_FOR_PURCHASES');

DELETE FROM "anv".rolepermission WHERE permissioncode = 'ACCOUNTING_REQUEST_FOR_PURCHASE_LIST_FULL_ACCESS';
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_REQUEST_FOR_PURCHASE_LIST_FULL_ACCESS', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_REQUEST_FOR_PURCHASE_LIST_FULL_ACCESS', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_REQUEST_FOR_PURCHASE_LIST_FULL_ACCESS', 'ALLOW', 'ACCOUNTANT');

DELETE FROM "anv".permission_context WHERE permissioncode = 'ACCOUNTING_REQUEST_FOR_PURCHASE_LIST_FULL_ACCESS' AND contextcode = 'REQUEST_FOR_PURCHASES';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_REQUEST_FOR_PURCHASE_LIST_FULL_ACCESS', 'REQUEST_FOR_PURCHASES');

DELETE FROM "0".rolepermission WHERE permissioncode = 'ACCOUNTING_REQUEST_FOR_PURCHASE_LIST_FULL_ACCESS';
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_REQUEST_FOR_PURCHASE_LIST_FULL_ACCESS', 'ALLOW', 'ADMIN');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_REQUEST_FOR_PURCHASE_LIST_FULL_ACCESS', 'ALLOW', 'DR');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_REQUEST_FOR_PURCHASE_LIST_FULL_ACCESS', 'ALLOW', 'ACCOUNTANT');


DELETE FROM permission WHERE code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST_FULL_ACCESS';
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
  VALUES ('ACCOUNTING_REQUEST_FOR_QUOTE_LIST_FULL_ACCESS', 'ACCOUNTING', 'Request for Quote List Full access', 1,
  (SELECT id FROM permission WHERE code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST'),'REQUEST_FOR_QUOTES');

DELETE FROM "anv".permission_context WHERE permissioncode = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST_FULL_ACCESS' AND contextcode = 'REQUEST_FOR_QUOTES';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_REQUEST_FOR_QUOTE_LIST_FULL_ACCESS', 'REQUEST_FOR_QUOTES');

DELETE FROM "anv".rolepermission WHERE permissioncode = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST_FULL_ACCESS';
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_REQUEST_FOR_QUOTE_LIST_FULL_ACCESS', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_REQUEST_FOR_QUOTE_LIST_FULL_ACCESS', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_REQUEST_FOR_QUOTE_LIST_FULL_ACCESS', 'ALLOW', 'ACCOUNTANT');

DELETE FROM "anv".permission_context WHERE permissioncode = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST_FULL_ACCESS' AND contextcode = 'REQUEST_FOR_QUOTES';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_REQUEST_FOR_QUOTE_LIST_FULL_ACCESS', 'REQUEST_FOR_QUOTES');

DELETE FROM "0".rolepermission WHERE permissioncode = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST_FULL_ACCESS';
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_REQUEST_FOR_QUOTE_LIST_FULL_ACCESS', 'ALLOW', 'ADMIN');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_REQUEST_FOR_QUOTE_LIST_FULL_ACCESS', 'ALLOW', 'DR');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_REQUEST_FOR_QUOTE_LIST_FULL_ACCESS', 'ALLOW', 'ACCOUNTANT');