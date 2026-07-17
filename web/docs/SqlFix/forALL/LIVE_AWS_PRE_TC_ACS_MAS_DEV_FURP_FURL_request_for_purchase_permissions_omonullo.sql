-- ACCOUNTING_REQUEST_FOR_PURCHASE_LIST
-- ACCOUNTING_REQUEST_FOR_PURCHASE_ADD
-- ACCOUNTING_REQUEST_FOR_PURCHASE_EDIT
-- ACCOUNTING_REQUEST_FOR_PURCHASE_DELETE
-- ACCOUNTING_REQUEST_FOR_PURCHASE_CONVERT


-- ACCOUNTING_REQUEST_FOR_PURCHASE_LIST
DELETE FROM permission
WHERE code = 'ACCOUNTING_REQUEST_FOR_PURCHASE_LIST';
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
VALUES ('ACCOUNTING_REQUEST_FOR_PURCHASE_LIST', 'ACCOUNTING', 'Request for Purchase List', 8, (SELECT id
                                                                                               FROM permission
                                                                                               WHERE code = 'ACCOUNTING_ACCOUNTING_MENU'),
                                                                                               'REQUEST_FOR_PURCHASES');

DELETE FROM "anv".rolepermission
WHERE permissioncode = 'ACCOUNTING_REQUEST_FOR_PURCHASE_LIST';
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('ACCOUNTING_REQUEST_FOR_PURCHASE_LIST', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('ACCOUNTING_REQUEST_FOR_PURCHASE_LIST', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('ACCOUNTING_REQUEST_FOR_PURCHASE_LIST', 'ALLOW', 'ACCOUNTANT');

DELETE FROM "0".rolepermission
WHERE permissioncode = 'ACCOUNTING_REQUEST_FOR_PURCHASE_LIST';
INSERT INTO "0".rolepermission (permissioncode, access, rolecode)
VALUES ('ACCOUNTING_REQUEST_FOR_PURCHASE_LIST', 'ALLOW', 'ADMIN');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode)
VALUES ('ACCOUNTING_REQUEST_FOR_PURCHASE_LIST', 'ALLOW', 'DR');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode)
VALUES ('ACCOUNTING_REQUEST_FOR_PURCHASE_LIST', 'ALLOW', 'ACCOUNTANT');



-- ACCOUNTING_REQUEST_FOR_PURCHASE_ADD
delete from permission where code = 'ACCOUNTING_REQUEST_FOR_PURCHASE_ADD';
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
VALUES ('ACCOUNTING_REQUEST_FOR_PURCHASE_ADD', 'ACCOUNTING', 'Add Request for Purchase', 1, (SELECT id
                                                                                             FROM permission
                                                                                             WHERE code =
                                                                                                   'ACCOUNTING_REQUEST_FOR_PURCHASE_LIST'),
        'REQUEST_FOR_PURCHASES');

delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_REQUEST_FOR_PURCHASE_ADD';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_REQUEST_FOR_PURCHASE_ADD', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_REQUEST_FOR_PURCHASE_ADD', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_REQUEST_FOR_PURCHASE_ADD', 'ALLOW', 'ACCOUNTANT');

delete from "0".rolepermission where permissioncode = 'ACCOUNTING_REQUEST_FOR_PURCHASE_ADD';
insert into "0".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_REQUEST_FOR_PURCHASE_ADD', 'ALLOW', 'ADMIN');
insert into "0".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_REQUEST_FOR_PURCHASE_ADD', 'ALLOW', 'DR');
insert into "0".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_REQUEST_FOR_PURCHASE_ADD', 'ALLOW', 'ACCOUNTANT');



-- ACCOUNTING_REQUEST_FOR_PURCHASE_EDIT
delete from permission where code = 'ACCOUNTING_REQUEST_FOR_PURCHASE_EDIT';
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
VALUES ('ACCOUNTING_REQUEST_FOR_PURCHASE_EDIT', 'ACCOUNTING', 'Edit Request for Purchase', 1, (SELECT id
                                                                                             FROM permission
                                                                                             WHERE code =
                                                                                                   'ACCOUNTING_REQUEST_FOR_PURCHASE_LIST'),
        'REQUEST_FOR_PURCHASES');

delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_REQUEST_FOR_PURCHASE_EDIT';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_REQUEST_FOR_PURCHASE_EDIT', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_REQUEST_FOR_PURCHASE_EDIT', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_REQUEST_FOR_PURCHASE_EDIT', 'ALLOW', 'ACCOUNTANT');

delete from "0".rolepermission where permissioncode = 'ACCOUNTING_REQUEST_FOR_PURCHASE_EDIT';
insert into "0".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_REQUEST_FOR_PURCHASE_EDIT', 'ALLOW', 'ADMIN');
insert into "0".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_REQUEST_FOR_PURCHASE_EDIT', 'ALLOW', 'DR');
insert into "0".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_REQUEST_FOR_PURCHASE_EDIT', 'ALLOW', 'ACCOUNTANT');




-- ACCOUNTING_REQUEST_FOR_PURCHASE_DELETE
delete from permission where code = 'ACCOUNTING_REQUEST_FOR_PURCHASE_DELETE';
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
VALUES ('ACCOUNTING_REQUEST_FOR_PURCHASE_DELETE', 'ACCOUNTING', 'Delete Request for Purchase', 1, (SELECT id
                                                                                               FROM permission
                                                                                               WHERE code =
                                                                                                     'ACCOUNTING_REQUEST_FOR_PURCHASE_LIST'),
        'REQUEST_FOR_PURCHASES');

delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_REQUEST_FOR_PURCHASE_DELETE';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_REQUEST_FOR_PURCHASE_DELETE', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_REQUEST_FOR_PURCHASE_DELETE', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_REQUEST_FOR_PURCHASE_DELETE', 'ALLOW', 'ACCOUNTANT');

delete from "0".rolepermission where permissioncode = 'ACCOUNTING_REQUEST_FOR_PURCHASE_DELETE';
insert into "0".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_REQUEST_FOR_PURCHASE_DELETE', 'ALLOW', 'ADMIN');
insert into "0".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_REQUEST_FOR_PURCHASE_DELETE', 'ALLOW', 'DR');
insert into "0".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_REQUEST_FOR_PURCHASE_DELETE', 'ALLOW', 'ACCOUNTANT');



-- ACCOUNTING_REQUEST_FOR_PURCHASE_CONVERT
delete from permission where code = 'ACCOUNTING_REQUEST_FOR_PURCHASE_CONVERT';
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
VALUES ('ACCOUNTING_REQUEST_FOR_PURCHASE_CONVERT', 'ACCOUNTING', 'Convert RFP to PO', 1, (SELECT id
                                                                                                   FROM permission
                                                                                                   WHERE code =
                                                                                                         'ACCOUNTING_REQUEST_FOR_PURCHASE_LIST'),
        'REQUEST_FOR_PURCHASES');

delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_REQUEST_FOR_PURCHASE_CONVERT';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_REQUEST_FOR_PURCHASE_CONVERT', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_REQUEST_FOR_PURCHASE_CONVERT', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_REQUEST_FOR_PURCHASE_CONVERT', 'ALLOW', 'ACCOUNTANT');

delete from "0".rolepermission where permissioncode = 'ACCOUNTING_REQUEST_FOR_PURCHASE_CONVERT';
insert into "0".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_REQUEST_FOR_PURCHASE_CONVERT', 'ALLOW', 'ADMIN');
insert into "0".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_REQUEST_FOR_PURCHASE_CONVERT', 'ALLOW', 'DR');
insert into "0".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_REQUEST_FOR_PURCHASE_CONVERT', 'ALLOW', 'ACCOUNTANT');
