DELETE
FROM permission
WHERE code IN (
               'ACCOUNTING_PRODUCT_LIST_FILTER',
               'ACCOUNTING_PRODUCT_LIST_CUSTOMIZE',
               'ACCOUNTING_PRODUCT_LIST_ITEM_COPY'
    );

DELETE
FROM "anv".permission_context
WHERE permissioncode IN (
                         'ACCOUNTING_PRODUCT_LIST_FILTER',
                         'ACCOUNTING_PRODUCT_LIST_CUSTOMIZE',
                         'ACCOUNTING_PRODUCT_LIST_ITEM_COPY'
    );


INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'ACCOUNTING_PRODUCT_LIST_FILTER',
       'ACCOUNTING',
       'Filter',
       (select max(sorder) + 2 from permission where id = p.id),
       p.id,
       'PRODUCTS_SERVICES'
FROM permission p
WHERE p.code = 'ACCOUNTING_PRODUCT_LIST';

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'ACCOUNTING_PRODUCT_LIST_CUSTOMIZE',
       'ACCOUNTING',
       'Customize Column',
       (select max(sorder) + 3 from permission where id = p.id),
       p.id,
       'PRODUCTS_SERVICES'
FROM permission p
WHERE p.code = 'ACCOUNTING_PRODUCT_LIST';

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'ACCOUNTING_PRODUCT_LIST_ITEM_COPY',
       'ACCOUNTING',
       'Copy',
       (select max(sorder) + 4 from permission where id = p.id),
       p.id,
       'PRODUCTS_SERVICES'
FROM permission p
WHERE p.code = 'ACCOUNTING_PRODUCT_LIST';


INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('ACCOUNTING_PRODUCT_LIST_FILTER', 'ACCOUNTING'),
       ('ACCOUNTING_PRODUCT_LIST_CUSTOMIZE', 'ACCOUNTING'),
       ('ACCOUNTING_PRODUCT_LIST_ITEM_COPY', 'ACCOUNTING');

INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('ACCOUNTING_PRODUCT_LIST_FILTER', 'ALLOW', 'ADMIN'),
       ('ACCOUNTING_PRODUCT_LIST_CUSTOMIZE', 'ALLOW', 'DR'),
       ('ACCOUNTING_PRODUCT_LIST_ITEM_COPY', 'ALLOW', 'PM');