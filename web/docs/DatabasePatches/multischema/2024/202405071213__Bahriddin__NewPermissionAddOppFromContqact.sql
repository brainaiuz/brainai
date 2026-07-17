-- Delete existing permissions to avoid duplicates.
DELETE
FROM permission
WHERE code = 'CRM_ADD_OPPORTUNITY_FROM_CONTACT';

DELETE
FROM "anv".permission_context
WHERE permissioncode = 'CRM_ADD_OPPORTUNITY_FROM_CONTACT';


DELETE
FROM "anv".rolepermission
WHERE permissioncode = 'CRM_ADD_OPPORTUNITY_FROM_CONTACT';


-- Insert new permissions & please make sure you are setting correct sorder & parent!
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'CRM_ADD_OPPORTUNITY_FROM_CONTACT', 'CRM', 'Add Opportunity From Contact', 27, p.id, 'OPPORTUNITY_TRACKING'
FROM permission p
WHERE p.code = 'CRM_OPPORTUNITIES_LIST';

-- Insert new permission contexts, it is for reloading permissions by   section.
INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('CRM_ADD_OPPORTUNITY_FROM_CONTACT', 'CRM');


-- Insert new role permissions ALLOW or DENY
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('CRM_ADD_OPPORTUNITY_FROM_CONTACT', 'ALLOW', 'ADMIN');

