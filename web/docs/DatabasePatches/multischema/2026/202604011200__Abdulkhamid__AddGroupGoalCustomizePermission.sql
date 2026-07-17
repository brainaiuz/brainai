INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'HRMS_GROUP_GOAL_CUSTOMIZE_BUTTON', 'HRMS', 'Customize', 5, p.id, 'GOAL_MANAGEMENT'
FROM permission p
WHERE p.code = 'HRMS_GROUP_PERSONAL_GOALS'
  AND NOT EXISTS (
      SELECT 1
      FROM permission
      WHERE code = 'HRMS_GROUP_GOAL_CUSTOMIZE_BUTTON'
  );

INSERT INTO "anv".permission_context (permissioncode, contextcode)
SELECT 'HRMS_GROUP_GOAL_CUSTOMIZE_BUTTON', 'HRMS'
WHERE NOT EXISTS (
    SELECT 1
    FROM "anv".permission_context
    WHERE permissioncode = 'HRMS_GROUP_GOAL_CUSTOMIZE_BUTTON'
      AND contextcode = 'HRMS'
);

INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
SELECT 'HRMS_GROUP_GOAL_CUSTOMIZE_BUTTON', 'ALLOW', 'MEM'
WHERE NOT EXISTS (
    SELECT 1
    FROM "anv".rolepermission
    WHERE permissioncode = 'HRMS_GROUP_GOAL_CUSTOMIZE_BUTTON'
      AND access = 'ALLOW'
      AND rolecode = 'MEM'
);
