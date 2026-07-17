-- Delete existing permissions
DELETE
FROM permission
WHERE code IN ('HRMS_PLACEMENT_LOG_CALL', 'HRMS_PLACEMENT_QUICK_ADD_LOG_CALL');

DELETE
FROM "anv".permission_context
WHERE permissioncode IN ('HRMS_PLACEMENT_LOG_CALL', 'HRMS_PLACEMENT_QUICK_ADD_LOG_CALL');

DELETE
FROM "anv".rolepermission
WHERE permissioncode IN ('HRMS_PLACEMENT_LOG_CALL', 'HRMS_PLACEMENT_QUICK_ADD_LOG_CALL');

-- Insert new permissions
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'HRMS_PLACEMENT_LOG_CALL', 'HRMS', 'Log a Call', 10, p.id, 'RECRUITMENT_SYSTEM'
FROM permission p
WHERE p.code = 'HRMS_PLACEMENT_LIST_VIEW';

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'HRMS_PLACEMENT_QUICK_ADD_LOG_CALL', 'HRMS', 'Quick Add Log a Call', 1, p.id, 'RECRUITMENT_SYSTEM'
FROM permission p
WHERE p.code = 'HRMS_PLACEMENT_LOG_CALL';

-- Insert new permission contexts
INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('HRMS_PLACEMENT_LOG_CALL', 'HRMS'),
       ('HRMS_PLACEMENT_QUICK_ADD_LOG_CALL', 'HRMS');

-- Insert new role permissions
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('HRMS_PLACEMENT_QUICK_ADD_LOG_CALL', 'ALLOW', 'ADMIN'),
       ('HRMS_PLACEMENT_LOG_CALL', 'ALLOW', 'ADMIN');

