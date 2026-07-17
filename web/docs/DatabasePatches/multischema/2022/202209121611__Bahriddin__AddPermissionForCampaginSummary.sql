UPDATE permission
set sorder = 5
where code = 'CRM_CAMPAIGNS_EXPORT';


DELETE
FROM permission
WHERE code = 'CRM_CAMPAIGNS_SUMMARY';

INSERT INTO permission
(code,
 context,
 name,
 sorder,
 parent,
 modulecode)
VALUES ('CRM_CAMPAIGNS_SUMMARY',
        'CRM',
        'Summary',
        4,
        (SELECT id
         FROM permission
         WHERE code = 'CRM_CAMPAIGNS_LIST'),
        'CRM_MODULE');

DELETE
FROM "anv".permission_context
WHERE permissioncode = 'CRM_CAMPAIGNS_SUMMARY';

INSERT INTO "anv".permission_context
(permissioncode,
 contextcode)
VALUES ('CRM_CAMPAIGNS_SUMMARY',
        'CRM');

DELETE
FROM "anv".rolepermission
WHERE permissioncode = 'CRM_CAMPAIGNS_SUMMARY';

INSERT INTO "anv".rolepermission
(permissioncode,
 access,
 rolecode)
VALUES ('CRM_CAMPAIGNS_SUMMARY', 'ALLOW', 'ADMIN'),
       ('CRM_CAMPAIGNS_SUMMARY', 'ALLOW', 'DR'),
       ('CRM_CAMPAIGNS_SUMMARY', 'ALLOW', 'SALESMAN'),
       ('CRM_CAMPAIGNS_SUMMARY', 'ALLOW', 'SALESPERSON');
	   
	   