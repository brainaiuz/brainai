-- Delete unused permissions
DELETE
FROM permission
WHERE code = 'ACCOUNTING_STOCK_TRANSFER_SEE_OWN_WAREHOUSE_OWNER';

DELETE
FROM "anv".permission_context
WHERE permissioncode = 'ACCOUNTING_STOCK_TRANSFER_SEE_OWN_WAREHOUSE_OWNER';

DELETE
FROM "anv".rolepermission
WHERE permissioncode = 'ACCOUNTING_STOCK_TRANSFER_SEE_OWN_WAREHOUSE_OWNER';

-- Delete existing permissions
DELETE
FROM permission
WHERE code = 'ACCOUNTING_WAREHOUSES_DELETE';


DELETE
FROM "anv".permission_context
WHERE permissioncode = 'ACCOUNTING_WAREHOUSES_DELETE';


DELETE
FROM "anv".rolepermission
WHERE permissioncode = 'ACCOUNTING_WAREHOUSES_DELETE';


-- Insert new permissions
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'ACCOUNTING_WAREHOUSES_DELETE', 'ACCOUNTING', 'Delete', 3, p.id, 'INVENTORY_MANAGEMENT'
FROM permission p
WHERE p.code = 'ACCOUNTING_WAREHOUSE_MENU';

-- Insert new permission contexts
INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('ACCOUNTING_WAREHOUSES_DELETE', 'ACCOUNTING');

-- Insert new role permissions
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('ACCOUNTING_WAREHOUSES_DELETE', 'ALLOW', 'ADMIN');

-- Update permission order
UPDATE permission
SET sorder =1
WHERE code = 'ACCOUNTING_WAREHOUSES_ADD';
UPDATE permission
SET sorder =2
WHERE code = 'ACCOUNTING_WAREHOUSES_EDIT';
UPDATE permission
SET sorder =4
WHERE code = 'ACCOUNTING_WAREHOUSES_SUMMARY';
UPDATE permission
SET sorder =5
WHERE code = 'ACCOUNTING_WAREHOUSES_EXPORT';
UPDATE permission
SET sorder =6
WHERE code = 'ACCOUNTING_WAREHOUSES_SEE_ALL';
UPDATE permission
SET sorder =7
WHERE code = 'ACCOUNTING_WAREHOUSES_SEE_OWN';
UPDATE permission
SET sorder =8
WHERE code = 'WAREHOUSE_OWNER';
