-- Delete existing permissions
DELETE
FROM permission
WHERE code = 'HRMS_DRAFT_VACANCY';


DELETE
FROM "anv".permission_context
WHERE permissioncode = 'HRMS_DRAFT_VACANCY';


DELETE
FROM "anv".rolepermission
WHERE permissioncode = 'HRMS_DRAFT_VACANCY';


-- Insert new permissions
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'HRMS_DRAFT_VACANCY', 'HRMS', 'Draft', 3, p.id, 'RECRUITMENT_SYSTEM'
FROM permission p
WHERE p.code = 'HRMS_VACANCY_LIST_VIEW';

-- Insert new permission contexts
INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('HRMS_DRAFT_VACANCY', 'HRMS');


-- Insert new role permissions
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('HRMS_DRAFT_VACANCY', 'ALLOW', 'ADMIN');
