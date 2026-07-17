-- Delete existing permissions
DELETE
FROM permission
WHERE code = 'ACCOUNTING_PURCHASE_ORDER_GRN_EXPORT';


DELETE
FROM "anv".permission_context
WHERE permissioncode = 'ACCOUNTING_PURCHASE_ORDER_GRN_EXPORT';


DELETE
FROM "anv".rolepermission
WHERE permissioncode = 'ACCOUNTING_PURCHASE_ORDER_GRN_EXPORT';


-- Insert new permissions
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'ACCOUNTING_PURCHASE_ORDER_GRN_EXPORT', 'ACCOUNTING', 'GRN Export', 6, p.id, 'PURCHASE_ORDERS'
FROM permission p
WHERE p.code = 'ACCOUNTING_PURCHASE_ORDER_LIST';

-- Insert new permission contexts
INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('ACCOUNTING_PURCHASE_ORDER_GRN_EXPORT', 'ACCOUNTING');


-- Insert new role permissions
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('ACCOUNTING_PURCHASE_ORDER_GRN_EXPORT', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('ACCOUNTING_PURCHASE_ORDER_GRN_EXPORT', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('ACCOUNTING_PURCHASE_ORDER_GRN_EXPORT', 'ALLOW', 'ACCOUNTANT');