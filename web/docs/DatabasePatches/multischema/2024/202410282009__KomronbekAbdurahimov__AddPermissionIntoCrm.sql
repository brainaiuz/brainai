DELETE FROM "anv".permission_context
WHERE permissioncode = 'SALES_QUOTE_CLIENT_APPROVE'
  AND contextcode = 'CRM';

INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('SALES_QUOTE_CLIENT_APPROVE', 'CRM');
