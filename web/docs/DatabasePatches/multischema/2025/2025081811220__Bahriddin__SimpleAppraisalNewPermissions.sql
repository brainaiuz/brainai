delete
from "anv".container_item
where propertyid = (select id
                    from "anv".property
                    where objectname = 'simpleAppraisal'
                      and defaultname = 'Simple Appraisals'
                    limit 1);

delete
from "anv".property
where objectname = 'simpleAppraisal'
  and defaultname = 'Simple Appraisals';

update "anv".property
set defaultname='Simple Appraisals',
    plural='Simple Appraisals',
    singular='Simple Appraisal'
where objectname = 'appraisalsArchive';


-- new permissions
DELETE
FROM permission
WHERE code in ('HRMS_APPRAISALS_SEE_OWN', 'HRMS_APPRAISALS_SEE_ALL', 'HRMS_ADD_COMPETENCY_FROM_TEMPLATE',
               'HRMS_ADD_VALIDITY_PERIOD', 'HRMS_NEW_EMPLOYEE_360_APPRAISALS',
               'HRMS_APPRAISALS_ARCHIVE');

DELETE
FROM "anv".permission_context
WHERE permissioncode in ('HRMS_APPRAISALS_SEE_OWN', 'HRMS_APPRAISALS_SEE_ALL', 'HRMS_ADD_COMPETENCY_FROM_TEMPLATE',
                         'HRMS_ADD_VALIDITY_PERIOD', 'HRMS_NEW_EMPLOYEE_360_APPRAISALS',
                         'HRMS_APPRAISALS_ARCHIVE');

DELETE
FROM "anv".rolepermission
WHERE permissioncode in ('HRMS_APPRAISALS_SEE_OWN', 'HRMS_APPRAISALS_SEE_ALL', 'HRMS_ADD_COMPETENCY_FROM_TEMPLATE',
                         'HRMS_ADD_VALIDITY_PERIOD', 'HRMS_APPRAISALS_REVIEWER', 'HRMS_NEW_EMPLOYEE_360_APPRAISALS',
                         'HRMS_APPRAISALS_ARCHIVE');


INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'HRMS_APPRAISALS_SEE_OWN', 'HRMS', 'See Own', 2, p.id, 'HRMS_MODULE'
FROM permission p
WHERE p.code = 'HRMS_SIMPLE_APPRAISALS';


INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'HRMS_APPRAISALS_SEE_ALL', 'HRMS', 'See All', 3, p.id, 'HRMS_MODULE'
FROM permission p
WHERE p.code = 'HRMS_SIMPLE_APPRAISALS';



INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('HRMS_APPRAISALS_SEE_OWN', 'HRMS');

INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('HRMS_APPRAISALS_SEE_ALL', 'HRMS');


INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('HRMS_APPRAISALS_SEE_ALL', 'ALLOW', 'ADMIN');

INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('HRMS_APPRAISALS_SEE_OWN', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('HRMS_APPRAISALS_SEE_OWN', 'ALLOW', 'PM');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('HRMS_APPRAISALS_SEE_OWN', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('HRMS_APPRAISALS_SEE_OWN', 'ALLOW', 'HR');


