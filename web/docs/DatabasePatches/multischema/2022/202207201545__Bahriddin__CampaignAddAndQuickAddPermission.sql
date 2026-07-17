DELETE
FROM permission
WHERE code = 'CRM_CAMPAIGN_QUICK_ADD';

INSERT INTO permission
(code,
 context,
 name,
 sorder,
 parent,
 modulecode)
VALUES ('CRM_CAMPAIGN_QUICK_ADD',
        'CRM',
        'Quick Add',
        2,
        (SELECT id
         FROM permission
         WHERE code = 'CRM_CAMPAIGNS_LIST'),
        'CRM_MODULE');

DELETE
FROM "anv".permission_context
WHERE permissioncode = 'CRM_CAMPAIGN_QUICK_ADD';

INSERT INTO "anv".permission_context
(permissioncode,
 contextcode)
VALUES ('CRM_CAMPAIGN_QUICK_ADD',
        'CRM');

DELETE
FROM "anv".rolepermission
WHERE permissioncode = 'CRM_CAMPAIGN_QUICK_ADD';

INSERT INTO "anv".rolepermission
(permissioncode,
 access,
 rolecode)
VALUES ('CRM_CAMPAIGN_QUICK_ADD', 'ALLOW', 'ADMIN');
