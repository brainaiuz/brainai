DELETE
FROM permission
WHERE code = 'CRM_OPPORTUNITY_EMAIL_COMPOSE';

INSERT INTO permission
(code,
 context,
 name,
 sorder,
 parent,
 modulecode)
VALUES ('CRM_OPPORTUNITY_EMAIL_COMPOSE',
        'CRM',
        'Compose',
        5,
        (SELECT id
         FROM permission
         WHERE code = 'CRM_MESSAGE_CENTER'),
        'CRM_MODULE');

DELETE
FROM "anv".permission_context
WHERE permissioncode = 'CRM_OPPORTUNITY_EMAIL_COMPOSE';

INSERT INTO "anv".permission_context
(permissioncode,
 contextcode)
VALUES ('CRM_OPPORTUNITY_EMAIL_COMPOSE',
        'CRM');

DELETE
FROM "anv".rolepermission
WHERE permissioncode = 'CRM_OPPORTUNITY_EMAIL_COMPOSE';

INSERT INTO "anv".rolepermission
(permissioncode,
 access,
 rolecode)
VALUES ('CRM_OPPORTUNITY_EMAIL_COMPOSE', 'ALLOW', 'ADMIN'),
       ('CRM_OPPORTUNITY_EMAIL_COMPOSE', 'ALLOW', 'DR'),
       ('CRM_OPPORTUNITY_EMAIL_COMPOSE', 'ALLOW', 'SALESMAN'),
       ('CRM_OPPORTUNITY_EMAIL_COMPOSE', 'ALLOW', 'SALESPERSON');