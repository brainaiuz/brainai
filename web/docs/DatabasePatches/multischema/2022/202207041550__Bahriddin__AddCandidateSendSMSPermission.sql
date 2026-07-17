DELETE
FROM permission
WHERE code = 'HRMS_CONDIDATE_SMS_SEND';

INSERT INTO permission
(code,
 context,
 name,
 sorder,
 parent,
 modulecode)
VALUES ('HRMS_CONDIDATE_SMS_SEND',
        'HRMS',
        'Send SMS',
        8,
        (SELECT id
         FROM permission
         WHERE code = 'HRMS_CANDIDATE_LIST_VIEW'),
        'RECRUITMENT_SYSTEM');

DELETE
FROM "anv".permission_context
WHERE permissioncode = 'HRMS_CONDIDATE_SMS_SEND';

INSERT INTO "anv".permission_context
(permissioncode,
 contextcode)
VALUES ('HRMS_CONDIDATE_SMS_SEND',
        'HRMS');

DELETE
FROM "anv".rolepermission
WHERE permissioncode = 'HRMS_CONDIDATE_SMS_SEND';

INSERT INTO "anv".rolepermission
(permissioncode,
 access,
 rolecode)
VALUES ('HRMS_CONDIDATE_SMS_SEND',
        'ALLOW',
        'ADMIN'),
       ('HRMS_CONDIDATE_SMS_SEND',
        'ALLOW',
        'HR');
