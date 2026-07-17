DELETE FROM permission WHERE code = 'ACCOUNTING_CAN_APPROVE_SALES_ORDER';
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
  VALUES ('ACCOUNTING_CAN_APPROVE_SALES_ORDER', 'ACCOUNTING', 'Approve/Reject', 12,
  (SELECT id FROM permission WHERE code = 'ACCOUNTING_SALES_ORDER_LIST'),'SALES_ORDERS');

DELETE FROM "0".permission_context WHERE permissioncode = 'ACCOUNTING_CAN_APPROVE_SALES_ORDER' AND contextcode = 'ACCOUNTING';
insert into "0".permission_context (permissioncode, contextcode) values ('ACCOUNTING_CAN_APPROVE_SALES_ORDER', 'ACCOUNTING');


DELETE FROM "0".rolepermission WHERE permissioncode = 'ACCOUNTING_CAN_APPROVE_SALES_ORDER';
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_CAN_APPROVE_SALES_ORDER', 'ALLOW', 'ADMIN');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_CAN_APPROVE_SALES_ORDER', 'ALLOW', 'DR');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_CAN_APPROVE_SALES_ORDER', 'ALLOW', 'ACCOUNTANT');


DELETE FROM "anv".permission_context WHERE permissioncode = 'ACCOUNTING_CAN_APPROVE_SALES_ORDER' AND contextcode = 'ACCOUNTING';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_CAN_APPROVE_SALES_ORDER', 'ACCOUNTING');

DELETE FROM "anv".rolepermission WHERE permissioncode = 'ACCOUNTING_CAN_APPROVE_SALES_ORDER';
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_CAN_APPROVE_SALES_ORDER', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_CAN_APPROVE_SALES_ORDER', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_CAN_APPROVE_SALES_ORDER', 'ALLOW', 'ACCOUNTANT');
