
DELETE FROM permission WHERE code='CRM_OPPORTUNITY_PDF';
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
VALUES ('CRM_OPPORTUNITY_PDF', 'CRM', 'PDF', 20, (SELECT id FROM permission WHERE code = 'CRM_OPPORTUNITIES_LIST'), 'CRM_MODULE');

DELETE FROM "anv".permission_context WHERE permissioncode = 'CRM_OPPORTUNITY_PDF' AND contextcode = 'CRM';
INSERT INTO "anv".permission_context (permissioncode, contextcode) values ('CRM_OPPORTUNITY_PDF', 'CRM');

DELETE FROM "anv".rolepermission WHERE permissioncode = 'CRM_OPPORTUNITY_PDF';
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('CRM_OPPORTUNITY_PDF', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('CRM_OPPORTUNITY_PDF', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('CRM_OPPORTUNITY_PDF', 'ALLOW', 'SALESMAN');