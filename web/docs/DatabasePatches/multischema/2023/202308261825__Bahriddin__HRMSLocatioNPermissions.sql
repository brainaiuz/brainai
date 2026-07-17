UPDATE permission
set sorder=7
where code = 'HRMS_COUNTRY_SETTINGS_LIST';

DELETE
FROM permission
WHERE code IN (
               'HRMS_SUMMARY_LOCATION',
               'HRMS_SEE_ALL_LOCATION',
               'HRMS_SEE_OWN_LOCATION',
               'HRMS_OWNERS_FIELD_LOCATION'
    );

DELETE
FROM "anv".permission_context
WHERE permissioncode IN (
                         'HRMS_SUMMARY_LOCATION',
                         'HRMS_SEE_ALL_LOCATION',
                         'HRMS_SEE_OWN_LOCATION',
                         'HRMS_OWNERS_FIELD_LOCATION'
    );

DELETE
FROM "anv".rolepermission
WHERE permissioncode IN (
                         'HRMS_SUMMARY_LOCATION',
                         'HRMS_SEE_ALL_LOCATION',
                         'HRMS_SEE_OWN_LOCATION',
                         'HRMS_OWNERS_FIELD_LOCATION'
    );

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'HRMS_SUMMARY_LOCATION', 'SETTINGS', 'Summary', 3, p.id, 'HRMS_MODULE'
FROM permission p
WHERE p.code = 'HRMS_LOCATION';

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'HRMS_SEE_ALL_LOCATION', 'SETTINGS', 'See All', 4, p.id, 'HRMS_MODULE'
FROM permission p
WHERE p.code = 'HRMS_LOCATION';

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'HRMS_SEE_OWN_LOCATION', 'SETTINGS', 'See Own', 5, p.id, 'HRMS_MODULE'
FROM permission p
WHERE p.code = 'HRMS_LOCATION';

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'HRMS_OWNERS_FIELD_LOCATION', 'SETTINGS', 'Owners', 6, p.id, 'HRMS_MODULE'
FROM permission p
WHERE p.code = 'HRMS_LOCATION';


INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('HRMS_SUMMARY_LOCATION', 'SETTINGS'),
       ('HRMS_SEE_ALL_LOCATION', 'SETTINGS'),
       ('HRMS_SEE_OWN_LOCATION', 'SETTINGS'),
       ('HRMS_OWNERS_FIELD_LOCATION', 'SETTINGS');


INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('HRMS_SUMMARY_LOCATION', 'ALLOW', 'DR'),
       ('HRMS_SEE_ALL_LOCATION', 'ALLOW', 'DR'),
       ('HRMS_SEE_OWN_LOCATION', 'ALLOW', 'DR'),
       ('HRMS_OWNERS_FIELD_LOCATION', 'ALLOW', 'DR');

INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('HRMS_SUMMARY_LOCATION', 'ALLOW', 'HR'),
       ('HRMS_SEE_ALL_LOCATION', 'ALLOW', 'HR'),
       ('HRMS_SEE_OWN_LOCATION', 'ALLOW', 'HR'),
       ('HRMS_OWNERS_FIELD_LOCATION', 'ALLOW', 'HR');

INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('HRMS_SUMMARY_LOCATION', 'ALLOW', 'ADMIN'),
       ('HRMS_SEE_ALL_LOCATION', 'ALLOW', 'ADMIN'),
       ('HRMS_SEE_OWN_LOCATION', 'ALLOW', 'ADMIN'),
       ('HRMS_OWNERS_FIELD_LOCATION', 'ALLOW', 'ADMIN');
