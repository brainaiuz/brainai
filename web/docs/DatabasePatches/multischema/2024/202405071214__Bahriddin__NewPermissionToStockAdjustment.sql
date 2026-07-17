-- Delete unused permissions
DELETE
FROM permission
WHERE code IN (
               'ACCOUNTING_STOCK_ADJUSTMENT_SEE_OWN',
               'ACCOUNTING_STOCK_ADJUSTMENT_SEE_BY_LOCATION'
    );

DELETE
FROM "anv".permission_context
WHERE permissioncode IN (
                         'ACCOUNTING_STOCK_ADJUSTMENT_SEE_OWN',
                         'ACCOUNTING_STOCK_ADJUSTMENT_SEE_BY_LOCATION'
    );



-- Insert new permissions
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'ACCOUNTING_STOCK_ADJUSTMENT_SEE_OWN', 'ACCOUNTING', 'See Own', 4, p.id, 'INVENTORY_MANAGEMENT'
FROM permission p WHERE p.code = 'ACCOUNTING_STOCK_ADJUSTMENT_LIST';

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'ACCOUNTING_STOCK_ADJUSTMENT_SEE_BY_LOCATION', 'ACCOUNTING', 'See By Location', 5, p.id, 'INVENTORY_MANAGEMENT'
FROM permission p WHERE p.code = 'ACCOUNTING_STOCK_ADJUSTMENT_LIST';


-- Insert new permission contexts
INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('ACCOUNTING_STOCK_ADJUSTMENT_SEE_OWN', 'ACCOUNTING'),
       ('ACCOUNTING_STOCK_ADJUSTMENT_SEE_BY_LOCATION', 'ACCOUNTING');
