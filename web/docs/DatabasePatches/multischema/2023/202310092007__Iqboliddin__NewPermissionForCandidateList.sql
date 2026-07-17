-- Delete existing permissions
DELETE
FROM permission
WHERE code = 'HRMS_SHOW_LOCATION_CANDIDATES';

DELETE
FROM "anv".permission_context
WHERE permissioncode = 'HRMS_SHOW_LOCATION_CANDIDATES';

DELETE
FROM "anv".rolepermission
WHERE permissioncode = 'HRMS_SHOW_LOCATION_CANDIDATES';


-- Insert new permissions
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'HRMS_SHOW_LOCATION_CANDIDATES',
       'HRMS',
       'Show Location Candidates',
       (SELECT MAX(sorder) + 1 FROM permission WHERE parent = p.id),
       p.id,
       'RECRUITMENT_SYSTEM'
FROM permission p
WHERE p.code = 'HRMS_CANDIDATE_LIST_VIEW';

-- Insert new permission contexts
INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('HRMS_SHOW_LOCATION_CANDIDATES', 'HRMS');

-- Insert new role permissions
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('HRMS_SHOW_LOCATION_CANDIDATES', 'ALLOW', 'ADMIN');
