DELETE FROM permission WHERE code = 'ACCOUNTING_CAN_APPROVE_SALES_INVOICE';
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
  VALUES ('ACCOUNTING_CAN_APPROVE_SALES_INVOICE', 'ACCOUNTING', 'Approve/Reject', 20,
  (SELECT id FROM permission WHERE code = 'ACCOUNTING_SALES_INVOICE_LIST'),'SALES_INVOICING');

DELETE FROM "0".permission_context WHERE permissioncode = 'ACCOUNTING_CAN_APPROVE_SALES_INVOICE' AND contextcode = 'ACCOUNTING';
insert into "0".permission_context (permissioncode, contextcode) values ('ACCOUNTING_CAN_APPROVE_SALES_INVOICE', 'ACCOUNTING');


DELETE FROM "0".rolepermission WHERE permissioncode = 'ACCOUNTING_CAN_APPROVE_SALES_INVOICE';
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_CAN_APPROVE_SALES_INVOICE', 'ALLOW', 'ADMIN');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_CAN_APPROVE_SALES_INVOICE', 'ALLOW', 'DR');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_CAN_APPROVE_SALES_INVOICE', 'ALLOW', 'ACCOUNTANT');


DELETE FROM "anv".permission_context WHERE permissioncode = 'ACCOUNTING_CAN_APPROVE_SALES_INVOICE' AND contextcode = 'ACCOUNTING';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_CAN_APPROVE_SALES_INVOICE', 'ACCOUNTING');

DELETE FROM "anv".rolepermission WHERE permissioncode = 'ACCOUNTING_CAN_APPROVE_SALES_INVOICE';
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_CAN_APPROVE_SALES_INVOICE', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_CAN_APPROVE_SALES_INVOICE', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_CAN_APPROVE_SALES_INVOICE', 'ALLOW', 'ACCOUNTANT');

