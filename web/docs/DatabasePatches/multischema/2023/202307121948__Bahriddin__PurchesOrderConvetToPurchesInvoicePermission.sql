-- Delete existing permissions
DELETE
FROM permission
WHERE code = 'ACCOUNTING_PORCHES_ORDER_CONVERT_TO_INVOICE';


DELETE
FROM "anv".permission_context
WHERE permissioncode = 'ACCOUNTING_PORCHES_ORDER_CONVERT_TO_INVOICE';


DELETE
FROM "anv".rolepermission
WHERE permissioncode = 'ACCOUNTING_PORCHES_ORDER_CONVERT_TO_INVOICE';


-- Insert new permissions
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'ACCOUNTING_PORCHES_ORDER_CONVERT_TO_INVOICE', 'ACCOUNTING', 'Convert', 19, p.id, 'PURCHASE_ORDERS'
FROM permission p
WHERE p.code = 'ACCOUNTING_PURCHASE_ORDER_LIST';

-- Insert new permission contexts
INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('ACCOUNTING_PORCHES_ORDER_CONVERT_TO_INVOICE', 'ACCOUNTING');


-- Insert new role permissions
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('ACCOUNTING_PORCHES_ORDER_CONVERT_TO_INVOICE', 'ALLOW', 'ADMIN');
