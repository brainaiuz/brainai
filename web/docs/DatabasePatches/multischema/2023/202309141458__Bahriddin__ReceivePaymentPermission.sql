-- Delete existing permissions, contexts, and role permissions
DELETE
FROM permission
WHERE code IN ('ACCOUNTING_PAY_BILL_SUMMARY', 'ACCOUNTING_RECEIVE_PAYMENT_SUMMARY');
DELETE
FROM "anv".permission_context
WHERE permissioncode IN ('ACCOUNTING_PAY_BILL_SUMMARY', 'ACCOUNTING_RECEIVE_PAYMENT_SUMMARY');
DELETE
FROM "anv".rolepermission
WHERE permissioncode IN ('ACCOUNTING_PAY_BILL_SUMMARY', 'ACCOUNTING_RECEIVE_PAYMENT_SUMMARY');

-- Insert new permissions
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'ACCOUNTING_PAY_BILL_SUMMARY', 'ACCOUNTING', 'Pay Bill Summary', 2, p.id, 'PURCHASE_INVOICING'
FROM permission p
WHERE p.code = 'ACCOUNTING_PAY_BILL_LIST';

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'ACCOUNTING_RECEIVE_PAYMENT_SUMMARY', 'ACCOUNTING', 'Receive Bill Summary', 2, p.id, 'ACCOUNTING_MODULE'
FROM permission p
WHERE p.code = 'ACCOUNTING_RECEIVE_PAYMENT_LIST';

-- Insert new permission contexts
INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('ACCOUNTING_PAY_BILL_SUMMARY', 'ACCOUNTING'),
       ('ACCOUNTING_RECEIVE_PAYMENT_SUMMARY', 'ACCOUNTING');

-- Insert new role permissions
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
SELECT 'ACCOUNTING_RECEIVE_PAYMENT_SUMMARY', access, rolecode
FROM "anv".rolepermission
WHERE permissioncode = 'ACCOUNTING_RECEIVE_PAYMENT';

INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
SELECT 'ACCOUNTING_PAY_BILL_SUMMARY', access, rolecode
FROM "anv".rolepermission
WHERE permissioncode = 'ACCOUNTING_PAY_BILL';
