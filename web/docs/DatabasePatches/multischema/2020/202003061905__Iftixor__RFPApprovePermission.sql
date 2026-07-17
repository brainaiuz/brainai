DELETE FROM permission WHERE code = 'ACCOUNTING_REQUEST_FOR_PURCHASE_APPROVE';
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
  VALUES ('ACCOUNTING_REQUEST_FOR_PURCHASE_APPROVE', 'ACCOUNTING', 'Approve/Reject', 7,
  (SELECT id FROM permission WHERE code = 'ACCOUNTING_REQUEST_FOR_PURCHASE_LIST'),'SALES_INVOICING');

DELETE FROM "0".permission_context WHERE permissioncode = 'ACCOUNTING_REQUEST_FOR_PURCHASE_APPROVE' AND contextcode = 'ACCOUNTING';
insert into "0".permission_context (permissioncode, contextcode) values ('ACCOUNTING_REQUEST_FOR_PURCHASE_APPROVE', 'ACCOUNTING');


DELETE FROM "0".rolepermission WHERE permissioncode = 'ACCOUNTING_REQUEST_FOR_PURCHASE_APPROVE';
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_REQUEST_FOR_PURCHASE_APPROVE', 'ALLOW', 'ADMIN');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_REQUEST_FOR_PURCHASE_APPROVE', 'ALLOW', 'DR');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_REQUEST_FOR_PURCHASE_APPROVE', 'ALLOW', 'ACCOUNTANT');


DELETE FROM "anv".permission_context WHERE permissioncode = 'ACCOUNTING_REQUEST_FOR_PURCHASE_APPROVE' AND contextcode = 'ACCOUNTING';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_REQUEST_FOR_PURCHASE_APPROVE', 'ACCOUNTING');

DELETE FROM "anv".rolepermission WHERE permissioncode = 'ACCOUNTING_REQUEST_FOR_PURCHASE_APPROVE';
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_REQUEST_FOR_PURCHASE_APPROVE', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_REQUEST_FOR_PURCHASE_APPROVE', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_REQUEST_FOR_PURCHASE_APPROVE', 'ALLOW', 'ACCOUNTANT');


