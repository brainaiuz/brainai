DELETE
FROM permission
WHERE code IN (
               'HRMS_PERSONAL_GOAL_CUSTOMIZE_BUTTON',
               'HRMS_DEPARTMENT_GOAL_CUSTOMIZE_BUTTON',
               'HRMS_PROJECT_GOAL_CUSTOMIZE_BUTTON',
               'HRMS_BUSINESS_GOAL_CUSTOMIZE_BUTTON',
               'HRMS_COMPANY_GOAL_CUSTOMIZE_BUTTON'
    );

DELETE
FROM "anv".permission_context
WHERE permissioncode IN (
                         'HRMS_PERSONAL_GOAL_CUSTOMIZE_BUTTON',
                         'HRMS_DEPARTMENT_GOAL_CUSTOMIZE_BUTTON',
                         'HRMS_PROJECT_GOAL_CUSTOMIZE_BUTTON',
                         'HRMS_BUSINESS_GOAL_CUSTOMIZE_BUTTON',
                         'HRMS_COMPANY_GOAL_CUSTOMIZE_BUTTON'
    );

DELETE
FROM "anv".rolepermission
WHERE permissioncode IN (
                         'HRMS_PERSONAL_GOAL_CUSTOMIZE_BUTTON',
                         'HRMS_DEPARTMENT_GOAL_CUSTOMIZE_BUTTON',
                         'HRMS_PROJECT_GOAL_CUSTOMIZE_BUTTON',
                         'HRMS_BUSINESS_GOAL_CUSTOMIZE_BUTTON',
                         'HRMS_COMPANY_GOAL_CUSTOMIZE_BUTTON'
    );


-- Insert new permissions & please make sure you are setting correct sorder!
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'HRMS_PERSONAL_GOAL_CUSTOMIZE_BUTTON', 'HRMS', 'Customize', 5, p.id, 'GOAL_MANAGEMENT'
FROM permission p
WHERE p.code = 'HRMS_PERSONAL_GOALS';


INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'HRMS_DEPARTMENT_GOAL_CUSTOMIZE_BUTTON', 'HRMS', 'Customize', 5, p.id, 'GOAL_MANAGEMENT'
FROM permission p
WHERE p.code = 'HRMS_DEPARTMENT_GOALS';

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'HRMS_PROJECT_GOAL_CUSTOMIZE_BUTTON', 'HRMS', 'Customize', 5, p.id, 'GOAL_MANAGEMENT'
FROM permission p
WHERE p.code = 'HRMS_PROJECT_GOALS';

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'HRMS_BUSINESS_GOAL_CUSTOMIZE_BUTTON', 'HRMS', 'Customize', 5, p.id, 'GOAL_MANAGEMENT'
FROM permission p
WHERE p.code = 'HRMS_BUSINESS_GOALS';

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'HRMS_COMPANY_GOAL_CUSTOMIZE_BUTTON', 'HRMS', 'Customize', 5, p.id, 'GOAL_MANAGEMENT'
FROM permission p
WHERE p.code = 'HRMS_COMPANY_GOALS';

-- Insert new permission contexts
INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('HRMS_PERSONAL_GOAL_CUSTOMIZE_BUTTON', 'HRMS'),
       ('HRMS_DEPARTMENT_GOAL_CUSTOMIZE_BUTTON', 'HRMS'),
       ('HRMS_PROJECT_GOAL_CUSTOMIZE_BUTTON', 'HRMS'),
       ('HRMS_BUSINESS_GOAL_CUSTOMIZE_BUTTON', 'HRMS'),
       ('HRMS_COMPANY_GOAL_CUSTOMIZE_BUTTON', 'HRMS');

-- Insert new role permissions ALLOW or DENY
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('HRMS_PERSONAL_GOAL_CUSTOMIZE_BUTTON', 'ALLOW', 'MEM'),
       ('HRMS_DEPARTMENT_GOAL_CUSTOMIZE_BUTTON', 'ALLOW', 'MEM'),
       ('HRMS_PROJECT_GOAL_CUSTOMIZE_BUTTON', 'ALLOW', 'MEM'),
       ('HRMS_BUSINESS_GOAL_CUSTOMIZE_BUTTON', 'ALLOW', 'MEM'),
       ('HRMS_COMPANY_GOAL_CUSTOMIZE_BUTTON', 'ALLOW', 'MEM');
