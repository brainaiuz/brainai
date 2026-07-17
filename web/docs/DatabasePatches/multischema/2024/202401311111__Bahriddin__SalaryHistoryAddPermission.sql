-- Delete existing permissions to avoid duplicates.
DELETE
FROM permission
WHERE code = 'ADD_SALARY_HISTORY';

DELETE
FROM "anv".permission_context
WHERE permissioncode = 'ADD_SALARY_HISTORY';


DELETE
FROM "anv".rolepermission
WHERE permissioncode = 'ADD_SALARY_HISTORY';


-- Insert new permissions & please make sure you are setting correct sorder & parent!
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'ADD_SALARY_HISTORY', 'HRMS', 'Salary History Add', 42, p.id, 'HRMS_MODULE'
FROM permission p
WHERE p.code = 'HRMS_EMPLOYEES';

-- Insert new permission contexts, it is for reloading permissions by   section.
INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('ADD_SALARY_HISTORY', 'HRMS');

INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('ADD_SALARY_HISTORY', 'PAYROLL');


-- Insert new role permissions ALLOW or DENY
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('ADD_SALARY_HISTORY', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('ADD_SALARY_HISTORY', 'ALLOW', 'PM');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('ADD_SALARY_HISTORY', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('ADD_SALARY_HISTORY', 'ALLOW', 'HR');

