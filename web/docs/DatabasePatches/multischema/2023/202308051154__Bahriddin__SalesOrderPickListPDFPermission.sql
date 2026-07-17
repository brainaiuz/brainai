-- Delete existing permissions
DELETE
FROM permission
WHERE code = 'ACCOUNTING_SALES_ORDER_PICK_LIST_PDF';


DELETE
FROM "anv".permission_context
WHERE permissioncode = 'ACCOUNTING_SALES_ORDER_PICK_LIST_PDF';


DELETE
FROM "anv".rolepermission
WHERE permissioncode = 'ACCOUNTING_SALES_ORDER_PICK_LIST_PDF';


-- Insert new permissions
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'ACCOUNTING_SALES_ORDER_PICK_LIST_PDF', 'ACCOUNTING', 'PDF Picklist', 13, p.id, 'SALES_ORDERS'
FROM permission p
WHERE p.code = 'ACCOUNTING_SALES_ORDER_LIST';

-- Insert new permission contexts
INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('ACCOUNTING_SALES_ORDER_PICK_LIST_PDF', 'ACCOUNTING');


-- Insert new role permissions
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('ACCOUNTING_SALES_ORDER_PICK_LIST_PDF', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('ACCOUNTING_SALES_ORDER_PICK_LIST_PDF', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('ACCOUNTING_SALES_ORDER_PICK_LIST_PDF', 'ALLOW', 'ACCOUNTANT');