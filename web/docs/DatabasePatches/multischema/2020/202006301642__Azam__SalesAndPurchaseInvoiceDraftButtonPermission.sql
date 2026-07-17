--delete ACCOUNTING_SI_AND_PI_DRAFT
DELETE FROM permission WHERE code = 'ACCOUNTING_SI_AND_PI_DRAFT';
DELETE FROM "anv".permission_context WHERE permissioncode = 'ACCOUNTING_SI_AND_PI_DRAFT' AND contextcode = 'ACCOUNTING';
DELETE FROM "anv".permission_context WHERE permissioncode = 'ACCOUNTING_SI_AND_PI_DRAFT' AND contextcode = 'LOGISTICS';
DELETE FROM "anv".rolepermission WHERE permissioncode = 'ACCOUNTING_SI_AND_PI_DRAFT';

--add new permission ACCOUNTING_SI_AND_PI_DRAFT_BUTTON
DELETE FROM permission WHERE code = 'ACCOUNTING_SI_AND_PI_DRAFT_BUTTON';
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
VALUES ('ACCOUNTING_SI_AND_PI_DRAFT_BUTTON', 'ACCOUNTING', 'Sales/Purchase Invoice Draft Button', 18,
        (select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'), 'SALES_INVOICING');

UPDATE permission set sorder=19 where code='CREDIT_NOTE_APPROVE' and context='ACCOUNTING';
UPDATE permission set sorder=20 where code='SALES_INVOICE_SEE_OWN' and context='ACCOUNTING';
UPDATE permission set sorder=21 where code='ACCOUNTING_CAN_APPROVE_SALES_INVOICE' and context='ACCOUNTING';

DELETE FROM "anv".permission_context WHERE permissioncode = 'ACCOUNTING_SI_AND_PI_DRAFT_BUTTON' AND contextcode = 'ACCOUNTING';
INSERT INTO "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_SI_AND_PI_DRAFT_BUTTON', 'ACCOUNTING');

--LOGISTICS

DELETE FROM "anv".permission_context WHERE permissioncode = 'ACCOUNTING_SI_AND_PI_DRAFT_BUTTON' AND contextcode = 'LOGISTICS';
INSERT INTO "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_SI_AND_PI_DRAFT_BUTTON', 'LOGISTICS');

DELETE FROM "anv".rolepermission WHERE permissioncode = 'ACCOUNTING_SI_AND_PI_DRAFT_BUTTON';
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('ACCOUNTING_SI_AND_PI_DRAFT_BUTTON', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('ACCOUNTING_SI_AND_PI_DRAFT_BUTTON', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('ACCOUNTING_SI_AND_PI_DRAFT_BUTTON', 'ALLOW', 'ACCOUNTANT');