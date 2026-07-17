-- Delete unused permissions
DELETE
FROM permission
WHERE code IN (
               'WAREHOUSE_SEE_OWN',
               'ACCOUNTING_WAREHOUSE_FULL_LIST_ACCESS'
    );

DELETE
FROM "anv".permission_context
WHERE permissioncode IN (
                         'WAREHOUSE_SEE_OWN',
                         'ACCOUNTING_WAREHOUSE_FULL_LIST_ACCESS'
    );

DELETE
FROM "anv".rolepermission
WHERE permissioncode IN (
                         'WAREHOUSE_SEE_OWN',
                         'ACCOUNTING_WAREHOUSE_FULL_LIST_ACCESS'
    );


-- Delete existing permissions
DELETE
FROM permission
WHERE code IN (
               'ACCOUNTING_WAREHOUSES_ADD',
               'ACCOUNTING_WAREHOUSES_EDIT',
               'ACCOUNTING_WAREHOUSES_EXPORT',
               'ACCOUNTING_WAREHOUSES_SUMMARY',
               'ACCOUNTING_WAREHOUSES_SEE_ALL',
               'ACCOUNTING_WAREHOUSES_SEE_OWN'
    );

DELETE
FROM "anv".permission_context
WHERE permissioncode IN (
                         'ACCOUNTING_WAREHOUSES_ADD',
                         'ACCOUNTING_WAREHOUSES_EDIT',
                         'ACCOUNTING_WAREHOUSES_EXPORT',
                         'ACCOUNTING_WAREHOUSES_SUMMARY',
                         'ACCOUNTING_WAREHOUSES_SEE_ALL',
                         'ACCOUNTING_WAREHOUSES_SEE_OWN'
    );

DELETE
FROM "anv".rolepermission
WHERE permissioncode IN (
                         'ACCOUNTING_WAREHOUSES_ADD',
                         'ACCOUNTING_WAREHOUSES_EDIT',
                         'ACCOUNTING_WAREHOUSES_EXPORT',
                         'ACCOUNTING_WAREHOUSES_SUMMARY',
                         'ACCOUNTING_WAREHOUSES_SEE_ALL',
                         'ACCOUNTING_WAREHOUSES_SEE_OWN'
    );

-- Insert new permissions
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'ACCOUNTING_WAREHOUSES_ADD', 'ACCOUNTING', 'Add', 2, p.id, 'INVENTORY_MANAGEMENT'
FROM permission p
WHERE p.code = 'ACCOUNTING_WAREHOUSE_MENU';

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'ACCOUNTING_WAREHOUSES_EDIT', 'ACCOUNTING', 'Edit', 3, p.id, 'INVENTORY_MANAGEMENT'
FROM permission p
WHERE p.code = 'ACCOUNTING_WAREHOUSE_MENU';

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'ACCOUNTING_WAREHOUSES_SEE_ALL', 'ACCOUNTING', 'See All', 6, p.id, 'INVENTORY_MANAGEMENT'
FROM permission p
WHERE p.code = 'ACCOUNTING_WAREHOUSE_MENU';

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'ACCOUNTING_WAREHOUSES_SEE_OWN', 'ACCOUNTING', 'See Own', 7, p.id, 'INVENTORY_MANAGEMENT'
FROM permission p
WHERE p.code = 'ACCOUNTING_WAREHOUSE_MENU';

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'ACCOUNTING_WAREHOUSES_SUMMARY', 'ACCOUNTING', 'Summary', 4, p.id, 'INVENTORY_MANAGEMENT'
FROM permission p
WHERE p.code = 'ACCOUNTING_WAREHOUSE_MENU';

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'ACCOUNTING_WAREHOUSES_EXPORT', 'ACCOUNTING', 'Export', 5, p.id, 'INVENTORY_MANAGEMENT'
FROM permission p
WHERE p.code = 'ACCOUNTING_WAREHOUSE_MENU';

-- Insert new permission contexts
INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('ACCOUNTING_WAREHOUSES_ADD', 'ACCOUNTING'),
       ('ACCOUNTING_WAREHOUSES_EDIT', 'ACCOUNTING'),
       ('ACCOUNTING_WAREHOUSES_SEE_ALL', 'ACCOUNTING'),
       ('ACCOUNTING_WAREHOUSES_SUMMARY', 'ACCOUNTING'),
       ('ACCOUNTING_WAREHOUSES_SEE_OWN', 'ACCOUNTING'),
       ('ACCOUNTING_WAREHOUSES_EXPORT', 'ACCOUNTING');

-- Insert new role permissions
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('ACCOUNTING_WAREHOUSES_ADD', 'ALLOW', 'ADMIN'),
       ('ACCOUNTING_WAREHOUSES_EDIT', 'ALLOW', 'ADMIN'),
       ('ACCOUNTING_WAREHOUSES_SEE_ALL', 'ALLOW', 'ADMIN'),
       ('ACCOUNTING_WAREHOUSES_SUMMARY', 'ALLOW', 'ADMIN'),
       ('ACCOUNTING_WAREHOUSES_SEE_OWN', 'ALLOW', 'ADMIN'),
       ('ACCOUNTING_WAREHOUSES_EXPORT', 'ALLOW', 'ADMIN');
