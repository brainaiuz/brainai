-- For zero schema
DELETE FROM "0".reference
WHERE code = 'LEAVE_REQUEST' AND parentid = (SELECT id
                                             FROM "0".reference
                                             WHERE code = '_REQUEST_TYPE');
DELETE FROM "0".reference
WHERE code = 'BENEFIT_REQUEST' AND parentid = (SELECT id
                                               FROM "0".reference
                                               WHERE code = '_REQUEST_TYPE');
DELETE FROM "0".reference
WHERE code = 'CASH_ADVANCED' AND parentid = (SELECT id
                                             FROM "0".reference
                                             WHERE code = '_REQUEST_TYPE');
DELETE FROM "0".reference
WHERE code = 'EXPENSES_CLAIM' AND parentid = (SELECT id
                                              FROM "0".reference
                                              WHERE code = '_REQUEST_TYPE');
DELETE FROM "0".reference
WHERE code = 'OTHER_REQUEST' AND parentid = (SELECT id
                                             FROM "0".reference
                                             WHERE code = '_REQUEST_TYPE');

DELETE FROM "0".reference
WHERE code = '_REQUEST_TYPE';

INSERT INTO "0".reference (code, name) VALUES ('_REQUEST_TYPE', 'Request Type');

INSERT INTO "0".reference (code, name, parentid, sorder) VALUES ('LEAVE_REQUEST', 'Leave Request', (SELECT id
                                                                                                    FROM "0".reference
                                                                                                    WHERE code =
                                                                                                          '_REQUEST_TYPE'),
                                                                 0);
INSERT INTO "0".reference (code, name, parentid, sorder) VALUES ('BENEFIT_REQUEST', 'Benefit Request', (SELECT id
                                                                                                        FROM
                                                                                                          "0".reference
                                                                                                        WHERE code =
                                                                                                              '_REQUEST_TYPE'),
                                                                 1);
INSERT INTO "0".reference (code, name, parentid, sorder) VALUES ('CASH_ADVANCED', 'Cash Advance', (SELECT id
                                                                                                   FROM "0".reference
                                                                                                   WHERE code =
                                                                                                         '_REQUEST_TYPE'),
                                                                 2);
INSERT INTO "0".reference (code, name, parentid, sorder) VALUES ('EXPENSES_CLAIM', 'Expense Claim', (SELECT id
                                                                                                     FROM "0".reference
                                                                                                     WHERE code =
                                                                                                           '_REQUEST_TYPE'),
                                                                 3);
INSERT INTO "0".reference (code, name, parentid, sorder) VALUES ('OTHER_REQUEST', 'Other Request', (SELECT id
                                                                                                    FROM "0".reference
                                                                                                    WHERE code =
                                                                                                          '_REQUEST_TYPE'),
                                                                 4);


-- For All Schema

DELETE FROM "anv".reference
WHERE code = 'LEAVE_REQUEST' AND parentid = (SELECT id
                                             FROM "anv".reference
                                             WHERE code = '_REQUEST_TYPE');
DELETE FROM "anv".reference
WHERE code = 'BENEFIT_REQUEST' AND parentid = (SELECT id
                                               FROM "anv".reference
                                               WHERE code = '_REQUEST_TYPE');
DELETE FROM "anv".reference
WHERE code = 'CASH_ADVANCED' AND parentid = (SELECT id
                                             FROM "anv".reference
                                             WHERE code = '_REQUEST_TYPE');
DELETE FROM "anv".reference
WHERE code = 'EXPENSES_CLAIM' AND parentid = (SELECT id
                                              FROM "anv".reference
                                              WHERE code = '_REQUEST_TYPE');
DELETE FROM "anv".reference
WHERE code = 'OTHER_REQUEST' AND parentid = (SELECT id
                                             FROM "anv".reference
                                             WHERE code = '_REQUEST_TYPE');

DELETE FROM "anv".reference
WHERE code = '_REQUEST_TYPE';


INSERT INTO "anv".reference (code, name) VALUES ('_REQUEST_TYPE', 'Request Type');

INSERT INTO "anv".reference (code, name, parentid, sorder) VALUES ('LEAVE_REQUEST', 'Leave Request', (SELECT id
                                                                                                      FROM
                                                                                                        "anv".reference
                                                                                                      WHERE code =
                                                                                                            '_REQUEST_TYPE'),
                                                                   0);
INSERT INTO "anv".reference (code, name, parentid, sorder) VALUES ('BENEFIT_REQUEST', 'Benefit Request', (SELECT id
                                                                                                          FROM
                                                                                                            "anv".reference
                                                                                                          WHERE code =
                                                                                                                '_REQUEST_TYPE'),
                                                                   1);
INSERT INTO "anv".reference (code, name, parentid, sorder) VALUES ('CASH_ADVANCED', 'Cash Advance', (SELECT id
                                                                                                     FROM
                                                                                                       "anv".reference
                                                                                                     WHERE code =
                                                                                                           '_REQUEST_TYPE'),
                                                                   2);
INSERT INTO "anv".reference (code, name, parentid, sorder) VALUES ('EXPENSES_CLAIM', 'Expense Claim', (SELECT id
                                                                                                       FROM
                                                                                                         "anv".reference
                                                                                                       WHERE code =
                                                                                                             '_REQUEST_TYPE'),
                                                                   3);
INSERT INTO "anv".reference (code, name, parentid, sorder) VALUES ('OTHER_REQUEST', 'Other Request', (SELECT id
                                                                                                      FROM
                                                                                                        "anv".reference
                                                                                                      WHERE code =
                                                                                                            '_REQUEST_TYPE'),
                                                                   4);





