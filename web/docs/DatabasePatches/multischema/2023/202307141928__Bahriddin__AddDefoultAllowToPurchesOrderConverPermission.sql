-- Delete existing permissions
DELETE
FROM "anv".rolepermission
WHERE permissioncode = 'ACCOUNTING_PORCHES_ORDER_CONVERT_TO_INVOICE';


-- Insert new role permissions
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('ACCOUNTING_PORCHES_ORDER_CONVERT_TO_INVOICE', 'ALLOW', 'ACCOUNTANT');

INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('ACCOUNTING_PORCHES_ORDER_CONVERT_TO_INVOICE', 'ALLOW', 'ADMIN');
