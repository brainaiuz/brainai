DELETE
FROM permission
WHERE code IN
      ('ACCOUNTING_GRN_LIST', 'ACCOUNTING_GRN_DELETE', 'ACCOUNTING_GRN_SUMMARY', 'ACCOUNTING_GRN_CONVERT_TO_INVOICE',
       'ACCOUNTING_PURCHASE_ORDER_GRN_EXPORT');
DELETE
FROM "anv".permission_context
WHERE permissioncode IN
      ('ACCOUNTING_GRN_LIST', 'ACCOUNTING_GRN_SUMMARY');

DELETE
FROM "anv".rolepermission
WHERE permissioncode IN
      ('ACCOUNTING_GRN_LIST', 'ACCOUNTING_GRN_SUMMARY');


INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'ACCOUNTING_GRN_LIST',
       'ACCOUNTING',
       'GRN',
       (SELECT MAX(sorder) + 1
        FROM permission
        WHERE parent = 23),
       p.id,
       'PURCHASE_ORDERS'
FROM permission p
WHERE p.code = 'ACCOUNTING_MAIN_MENU';

INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('ACCOUNTING_GRN_LIST', 'ACCOUNTING');

INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('ACCOUNTING_GRN_LIST', 'ALLOW', 'ADMIN'),
       ('ACCOUNTING_GRN_LIST', 'ALLOW', 'DR'),
       ('ACCOUNTING_GRN_LIST', 'ALLOW', 'ACCOUNTANT');

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'ACCOUNTING_GRN_DELETE', 'ACCOUNTING', 'Delete', 1, p.id, 'PURCHASE_ORDERS'
FROM permission p
WHERE p.code = 'ACCOUNTING_GRN_LIST';

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'ACCOUNTING_GRN_SUMMARY', 'ACCOUNTING', 'Summary', 2, p.id, 'PURCHASE_ORDERS'
FROM permission p
WHERE p.code = 'ACCOUNTING_GRN_LIST';

INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('ACCOUNTING_GRN_SUMMARY', 'ACCOUNTING');

INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('ACCOUNTING_GRN_SUMMARY', 'ALLOW', 'ADMIN'),
       ('ACCOUNTING_GRN_SUMMARY', 'ALLOW', 'DR'),
       ('ACCOUNTING_GRN_SUMMARY', 'ALLOW', 'ACCOUNTANT');

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'ACCOUNTING_GRN_CONVERT_TO_INVOICE', 'ACCOUNTING', 'Convert To SI', 3, p.id, 'PURCHASE_ORDERS'
FROM permission p
WHERE p.code = 'ACCOUNTING_GRN_LIST';

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'ACCOUNTING_PURCHASE_ORDER_GRN_EXPORT', 'ACCOUNTING', 'Export', 4, p.id, 'PURCHASE_ORDERS'
FROM permission p
WHERE p.code = 'ACCOUNTING_GRN_LIST';


