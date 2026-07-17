INSERT INTO "anv".reference (code, deleted, isremovable, name, shared, sorder, parentid, isactive)
VALUES ('_WORKFLOW_MODULE_SALEORDER',
        FALSE,
        TRUE,
        'Sales Order',
        TRUE,
        6,
        (SELECT id FROM "anv".reference WHERE code = '_WORKFLOW_MODULE'),
        TRUE);

INSERT INTO "0".reference (code, deleted, isremovable, name, shared, sorder, parentid, isactive)
VALUES ('_WORKFLOW_MODULE_SALEORDER',
        FALSE,
        TRUE,
        'Sales Order',
        TRUE,
        6,
        (SELECT id FROM "0".reference WHERE code = '_WORKFLOW_MODULE'),
        TRUE);


INSERT INTO model (active, formid, title, viewname)
VALUES (TRUE, 'SALEORDER_FORM', 'Sales Order Form', 'Sales Order');

INSERT INTO modelfield (form_id,
                        field_id,
                        sorder,
                        widget,
                        source,
                        usablebyworkflow,
                        type,
                        disableupdate,
                        isworkflowattribute)
VALUES ('SALEORDER_FORM', 'CUSTOMER', 1, 'DropDown', 'ACCOUNTING@SALE_QUOTE_CUSTOMER', TRUE, 'Text', TRUE, FALSE),
       ('SALEORDER_FORM', 'PROJECT', 2, 'DropDown', 'ACCOUNTING@SALE_QUOTE_PROJECT', TRUE, 'Text', TRUE, FALSE),
       ('SALEORDER_FORM', 'INVOICE_DATE', 3, 'DatePicker', NULL, TRUE, 'Date', TRUE, FALSE),
       ('SALEORDER_FORM', 'DUE_DATE', 4, 'DatePicker', NULL, TRUE, 'Date', TRUE, FALSE),
       ('SALEORDER_FORM', 'REFERENCE', 6, 'TextBox', NULL, TRUE, 'Text', TRUE, FALSE),
       ('SALEORDER_FORM', 'PO_NUMBER', 7, 'TextBox', NULL, TRUE, 'Text', TRUE, FALSE),
       ('SALEORDER_FORM', 'SUB_TOTAL', 9, 'TextBox', NULL, TRUE, 'Number', TRUE, FALSE),
       ('SALEORDER_FORM', 'TOTAL', 10, 'TextBox', NULL, TRUE, 'Number', TRUE, FALSE),
       ('SALEORDER_FORM', 'TOTAL_INVOICE_CURRENCY', 11, 'TextBox', NULL, TRUE, 'Number', TRUE, FALSE),
       ('SALEORDER_FORM', 'TOTAL_DISCOUNT', 12, 'TextBox', NULL, TRUE, 'Number', TRUE, FALSE),
       ('SALEORDER_FORM', 'STATUS', 13, 'DropDown', 'ACCOUNTING@SALE_QUOTE_STATUS', TRUE, 'Text', TRUE, FALSE),
       ('SALEORDER_FORM', 'PREV_APPROVER', 2, NULL, NULL, TRUE, NULL, TRUE, TRUE),
       ('SALEORDER_FORM', 'PREV_APPROVER_EMAIL', 2, NULL, NULL, TRUE, NULL, TRUE, TRUE),
       ('SALEORDER_FORM', 'PREV_APPROVER_STATUS', 2, NULL, NULL, TRUE, NULL, TRUE, TRUE),
       ('SALEORDER_FORM', 'CURRENT_APPROVER', 2, NULL, NULL, TRUE, NULL, TRUE, TRUE),
       ('SALEORDER_FORM', 'CURRENT_APPROVER_EMAIL', 2, NULL, NULL, TRUE, NULL, TRUE, TRUE),
       ('SALEORDER_FORM', 'CURRENT_APPROVER_STATUS', 2, NULL, NULL, TRUE, NULL, TRUE, TRUE),
       ('SALEORDER_FORM', 'NEXT_APPROVER', 2, NULL, NULL, TRUE, NULL, TRUE, TRUE),
       ('SALEORDER_FORM', 'NEXT_APPROVER_EMAIL', 2, NULL, NULL, TRUE, NULL, TRUE, TRUE),
       ('SALEORDER_FORM', 'NEXT_APPROVER_STATUS', 2, NULL, NULL, TRUE, NULL, TRUE, TRUE);

