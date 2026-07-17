DELETE FROM permission WHERE code = 'CONVERT_OPPORTUNITY_TO_PO';
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
VALUES ('CONVERT_OPPORTUNITY_TO_PO', 'CRM', 'Convert to PO', 14,
        (select id from permission where code = 'CRM_OPPORTUNITIES_LIST'), 'PURCHASE_ORDERS');

UPDATE permission set sorder=15 where code='CRM_OPPORTUNITIES_IMPORT_LIST' and context='CRM';
UPDATE permission set sorder=16 where code='CRM_OPPORTUNITIES_EXPORT_LIST' and context='CRM';
UPDATE permission set sorder=17 where code='CRM_OPPORTUNITIES_EXPENSE_CLAIM_LIST' and context='CRM';
UPDATE permission set sorder=18 where code='CRM_OPPORTUNITIES_RFQ_LIST' and context='CRM';
UPDATE permission set sorder=19 where code='OPPORTUNITY_SEE_OWN' and context='CRM';

DELETE FROM "anv".permission_context WHERE permissioncode = 'CONVERT_OPPORTUNITY_TO_PO' AND contextcode = 'CRM';
INSERT INTO "anv".permission_context (permissioncode, contextcode) values ('CONVERT_OPPORTUNITY_TO_PO', 'CRM');

DELETE FROM "anv".rolepermission WHERE permissioncode = 'CONVERT_OPPORTUNITY_TO_PO';
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('CONVERT_OPPORTUNITY_TO_PO', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('CONVERT_OPPORTUNITY_TO_PO', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('CONVERT_OPPORTUNITY_TO_PO', 'ALLOW', 'SALESMAN');