INSERT INTO "anv".reference (code, deleted, isremovable, name, shared, sorder, parentid, isactive)
VALUES ('_WORKFLOW_MODULE_PURCHASE_INVOICE',
        FALSE,
        TRUE,
        'Purchase Invoice',
        TRUE,
        7,
        (SELECT id FROM "anv".reference WHERE code = '_WORKFLOW_MODULE'),
        TRUE);

INSERT INTO "0".reference (code, deleted, isremovable, name, shared, sorder, parentid, isactive)
VALUES ('_WORKFLOW_MODULE_PURCHASE_INVOICE',
        FALSE,
        TRUE,
        'Purchase Invoice',
        TRUE,
        7,
        (SELECT id FROM "0".reference WHERE code = '_WORKFLOW_MODULE'),
        TRUE);


INSERT INTO model (active, formid, title, viewname)
VALUES (TRUE, 'PURCHASE_INVOICE_FORM', 'Purchase Invoice Form', 'Purchase Invoice');

INSERT INTO modelfield (form_id, field_id, sorder, widget, source, usablebyworkflow, type, disableupdate, isWorkflowAttribute)
VALUES ('PURCHASE_INVOICE_FORM', 'SUPPLIER', 1, 'DropDown', 'ACCOUNTING@PURCHASE_ORDER_SUPPLIER', TRUE, 'Text', FALSE, FALSE),
       ('PURCHASE_INVOICE_FORM', 'PROJECT', 2, 'DropDown', 'ACCOUNTING@PURCHASE_ORDER_PROJECT', TRUE, 'Text', TRUE, FALSE),
       ('PURCHASE_INVOICE_FORM', 'START_DATE', 3, 'DatePicker', NULL, TRUE, 'Date', TRUE, FALSE),
       ('PURCHASE_INVOICE_FORM', 'DUE_DATE', 4, 'DatePicker', NULL, TRUE, 'Date', TRUE, FALSE),
       ('PURCHASE_INVOICE_FORM', 'REFERENCE', 5, 'TextBox', NULL, TRUE, 'Text', TRUE, FALSE),
       ('PURCHASE_INVOICE_FORM', 'NUMBER', 6, 'TextBox', NULL, TRUE, 'Text', TRUE, FALSE),
       ('PURCHASE_INVOICE_FORM', 'PO_NUMBER', 6, 'TextBox', NULL, TRUE, 'Text', TRUE, FALSE),
       ('PURCHASE_INVOICE_FORM', 'TOTAL_AMOUNT', 8, 'TextBox', NULL, TRUE, 'Number', TRUE, FALSE),
       ('PURCHASE_INVOICE_FORM', 'PAID_AMOUNT', 8, 'TextBox', NULL, TRUE, 'Number', TRUE, FALSE),
       ('PURCHASE_INVOICE_FORM', 'TOTAL_INVOICE_CURRENCY', 9, 'TextBox', NULL, TRUE, 'Number', TRUE, FALSE),
       ('PURCHASE_INVOICE_FORM', 'STATUS', 14, 'DropDown', 'ACCOUNTING@PURCHASE_INVOICE_STATUS', TRUE, 'Text', TRUE, FALSE),
       ('PURCHASE_INVOICE_FORM', 'PREV_APPROVER', 15, NULL, NULL, TRUE, NULL, TRUE, TRUE),
       ('PURCHASE_INVOICE_FORM', 'PREV_APPROVER_EMAIL', 16, NULL, NULL, TRUE, NULL, TRUE, TRUE),
       ('PURCHASE_INVOICE_FORM', 'PREV_APPROVER_STATUS', 17, NULL, NULL, TRUE, NULL, TRUE, TRUE),
       ('PURCHASE_INVOICE_FORM', 'CURRENT_APPROVER', 18, NULL, NULL, TRUE, NULL, TRUE, TRUE),
       ('PURCHASE_INVOICE_FORM', 'CURRENT_APPROVER_EMAIL', 19, NULL, NULL, TRUE, NULL, TRUE, TRUE),
       ('PURCHASE_INVOICE_FORM', 'CURRENT_APPROVER_STATUS', 20, NULL, NULL, TRUE, NULL, TRUE, TRUE),
       ('PURCHASE_INVOICE_FORM', 'NEXT_APPROVER', 21, NULL, NULL, TRUE, NULL, TRUE, TRUE),
       ('PURCHASE_INVOICE_FORM', 'NEXT_APPROVER_EMAIL', 22, NULL, NULL, TRUE, NULL, TRUE, TRUE),
       ('PURCHASE_INVOICE_FORM', 'NEXT_APPROVER_STATUS', 23, NULL, NULL, TRUE, NULL, TRUE, TRUE);


