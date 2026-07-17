DELETE
FROM permission
WHERE code IN
      ('GDN_EXCEL', 'ACCOUNTING_GDN_SUMMARY', 'ACCOUNTING_GDN_LIST', 'ACCOUNTING_GDN_DELETE', 'ACCOUNTING_GDN_EXPORT',
       'ACCOUNTING_GDN_CONVERT_TO_INVOICE', 'SAVE_FILTER', 'RESET_FILTER', 'GDN_PDF');
DELETE
FROM "anv".permission_context
WHERE permissioncode IN
      ('GDN_EXCEL', 'GDN_PDF', 'ACCOUNTING_GDN_SUMMARY', 'ACCOUNTING_GDN_LIST', 'ACCOUNTING_GDN_EXPORT');

DELETE
FROM "anv".rolepermission
WHERE permissioncode IN
      ('GDN_EXCEL', 'GDN_PDF', 'ACCOUNTING_GDN_SUMMARY', 'ACCOUNTING_GDN_LIST', 'ACCOUNTING_GDN_EXPORT');


INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'ACCOUNTING_GDN_LIST',
       'ACCOUNTING',
       'GDN',
       (SELECT MAX(sorder) + 1
        FROM permission
        WHERE parent = 23),
       p.id,
       'SALES_ORDERS'
FROM permission p
WHERE p.code = 'ACCOUNTING_MAIN_MENU';

INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('ACCOUNTING_GDN_LIST', 'ACCOUNTING');

INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('ACCOUNTING_GDN_LIST', 'ALLOW', 'ADMIN'),
       ('ACCOUNTING_GDN_LIST', 'ALLOW', 'DR'),
       ('ACCOUNTING_GDN_LIST', 'ALLOW', 'ACCOUNTANT');

INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('ACCOUNTING_GDN_SUMMARY', 'ACCOUNTING');

INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('ACCOUNTING_GDN_SUMMARY', 'ALLOW', 'ADMIN'),
       ('ACCOUNTING_GDN_SUMMARY', 'ALLOW', 'DR'),
       ('ACCOUNTING_GDN_SUMMARY', 'ALLOW', 'ACCOUNTANT');


INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'ACCOUNTING_GDN_DELETE', 'ACCOUNTING', 'Delete', 1, p.id, 'SALES_ORDERS'
FROM permission p
WHERE p.code = 'ACCOUNTING_GDN_LIST';

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'ACCOUNTING_GDN_SUMMARY', 'ACCOUNTING', 'Summary', 2, p.id, 'SALES_ORDERS'
FROM permission p
WHERE p.code = 'ACCOUNTING_GDN_LIST';

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'ACCOUNTING_GDN_CONVERT_TO_INVOICE', 'ACCOUNTING', 'Convert To SI', 3, p.id, 'SALES_ORDERS'
FROM permission p
WHERE p.code = 'ACCOUNTING_GDN_LIST';

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'SAVE_FILTER', 'ACCOUNTING', 'Save Filter', 4, p.id, 'SALES_ORDERS'
FROM permission p
WHERE p.code = 'ACCOUNTING_GDN_LIST';

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'RESET_FILTER', 'ACCOUNTING', 'Reset Filter', 5, p.id, 'SALES_ORDERS'
FROM permission p
WHERE p.code = 'ACCOUNTING_GDN_LIST';

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'ACCOUNTING_GDN_EXPORT', 'ACCOUNTING', 'Export', 6, p.id, 'SALES_ORDERS'
FROM permission p
WHERE p.code = 'ACCOUNTING_GDN_LIST';

INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('ACCOUNTING_GDN_EXPORT', 'ACCOUNTING');

INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('ACCOUNTING_GDN_EXPORT', 'ALLOW', 'ADMIN'),
       ('ACCOUNTING_GDN_EXPORT', 'ALLOW', 'DR'),
       ('ACCOUNTING_GDN_EXPORT', 'ALLOW', 'ACCOUNTANT');

