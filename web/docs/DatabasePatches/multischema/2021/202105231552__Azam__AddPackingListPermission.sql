
DELETE FROM permission WHERE code='ACCOUNTING_PACKING_LIST';
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
VALUES ('ACCOUNTING_PACKING_LIST', 'ACCOUNTING', 'Packing List', 51, (SELECT id FROM permission WHERE code = 'ACCOUNTING_SALES_ORDER_LIST'), 'SALES_ORDERS');

DELETE FROM "anv".permission_context WHERE permissioncode = 'ACCOUNTING_PACKING_LIST' AND contextcode = 'ACCOUNTING';
INSERT INTO "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_PACKING_LIST', 'ACCOUNTING');

DELETE FROM "anv".rolepermission WHERE permissioncode = 'ACCOUNTING_PACKING_LIST';
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_PACKING_LIST', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_PACKING_LIST', 'ALLOW', 'ACCOUNTANT');