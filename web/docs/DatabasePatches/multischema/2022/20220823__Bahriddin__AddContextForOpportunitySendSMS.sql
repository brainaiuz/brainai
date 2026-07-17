DELETE
FROM "anv".permission_context
WHERE permissioncode = 'CRM_OPPORTUNITY_SEND_SMS';

INSERT INTO "anv".permission_context
(permissioncode,
 contextcode)
VALUES ('CRM_OPPORTUNITY_SEND_SMS',
        'CRM');
