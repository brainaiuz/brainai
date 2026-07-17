DELETE
FROM permission
WHERE code IN (
               'ACCOUNTING_GOODS_RECEIVED_NOTE_SEE_OWN',
               'ACCOUNTING_GOODS_RECEIVED_NOTE_SEE_BY_LOCATION'
    );

DELETE
FROM "anv".permission_context
WHERE permissioncode IN (
                         'ACCOUNTING_GOODS_RECEIVED_NOTE_SEE_OWN',
                         'ACCOUNTING_GOODS_RECEIVED_NOTE_SEE_BY_LOCATION'
    );



-- Insert new permissions
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'ACCOUNTING_GOODS_RECEIVED_NOTE_SEE_OWN', 'ACCOUNTING', 'See Own', 5, p.id, 'PURCHASE_ORDERS'
FROM permission p WHERE p.code = 'ACCOUNTING_GRN_LIST';

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'ACCOUNTING_GOODS_RECEIVED_NOTE_SEE_BY_LOCATION', 'ACCOUNTING', 'See By Location', 6, p.id, 'PURCHASE_ORDERS'
FROM permission p WHERE p.code = 'ACCOUNTING_GRN_LIST';


-- Insert new permission contexts
INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('ACCOUNTING_GOODS_RECEIVED_NOTE_SEE_OWN', 'ACCOUNTING'),
       ('ACCOUNTING_GOODS_RECEIVED_NOTE_SEE_BY_LOCATION', 'ACCOUNTING');
