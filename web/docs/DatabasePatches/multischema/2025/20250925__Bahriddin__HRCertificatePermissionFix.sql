DELETE
FROM permission
WHERE code in ('CETIFICATE_OF_EMPLOYMENT_EDIT', 'CETIFICATE_OF_EMPLOYMENT_DELETE', 'CETIFICATE_OF_EMPLOYMENT_PDF');

DELETE
FROM "anv".permission_context
WHERE permissioncode in
      ('CETIFICATE_OF_EMPLOYMENT_EDIT', 'CETIFICATE_OF_EMPLOYMENT_DELETE', 'CETIFICATE_OF_EMPLOYMENT_PDF');

DELETE
FROM "anv".rolepermission
WHERE permissioncode in
      ('CETIFICATE_OF_EMPLOYMENT_EDIT', 'CETIFICATE_OF_EMPLOYMENT_DELETE', 'CETIFICATE_OF_EMPLOYMENT_PDF');


INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'CETIFICATE_OF_EMPLOYMENT_EDIT', 'HRMS', 'Edit', 3, p.id, 'HRMS_MODULE'
FROM permission p
WHERE p.code = 'CETIFICATE_OF_EMPLOYMENT_LIST';


INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'CETIFICATE_OF_EMPLOYMENT_DELETE', 'HRMS', 'Delete', 4, p.id, 'HRMS_MODULE'
FROM permission p
WHERE p.code = 'CETIFICATE_OF_EMPLOYMENT_LIST';

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'CETIFICATE_OF_EMPLOYMENT_PDF', 'HRMS', 'PDF', 5, p.id, 'HRMS_MODULE'
FROM permission p
WHERE p.code = 'CETIFICATE_OF_EMPLOYMENT_LIST';



INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('CETIFICATE_OF_EMPLOYMENT_EDIT', 'HRMS');

INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('CETIFICATE_OF_EMPLOYMENT_DELETE', 'HRMS');


INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('CETIFICATE_OF_EMPLOYMENT_PDF', 'HRMS');



INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('CETIFICATE_OF_EMPLOYMENT_EDIT', 'ALLOW', 'ADMIN');

INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('CETIFICATE_OF_EMPLOYMENT_DELETE', 'ALLOW', 'ADMIN');

INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('CETIFICATE_OF_EMPLOYMENT_PDF', 'ALLOW', 'ADMIN');




