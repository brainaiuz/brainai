-- Delete existing permissions
DELETE
FROM permission
WHERE code = 'HRMS_SHIFT_APPROVE';

DELETE
FROM "anv".permission_context
WHERE permissioncode = 'HRMS_SHIFT_APPROVE';

DELETE
FROM "anv".rolepermission
WHERE permissioncode = 'HRMS_SHIFT_APPROVE';

-- Insert new permissions
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'HRMS_SHIFT_APPROVE',
       'HRMS',
       'Approve',
       (SELECT MAX(sorder) + 1 FROM permission WHERE parent = p.id),
       p.id,
       'RECRUITMENT_SYSTEM'
FROM permission p
WHERE p.code = 'HRMS_SHIFT';

-- Insert new permission contexts
INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('HRMS_SHIFT_APPROVE', 'HRMS');

-- Insert new role permissions
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('HRMS_SHIFT_APPROVE', 'ALLOW', 'ADMIN');
