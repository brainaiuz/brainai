INSERT INTO permission (code, context, name, sorder, parent, modulecode)
VALUES ('CRM_ACCOUNTS_CONTACT_ACCESS', 'CRM', 'Contact access', 14,
        (SELECT id FROM permission WHERE code = 'CRM_ACCOUNTS_LIST'),'CRM_MODULE');

insert into "0".permission_context (permissioncode, contextcode) values ('CRM_ACCOUNTS_CONTACT_ACCESS', 'CRM');
insert into "0".permission_context (permissioncode, contextcode) values ('CRM_ACCOUNTS_CONTACT_ACCESS', 'ACCOUNTING');
insert into "0".permission_context (permissioncode, contextcode) values ('CRM_ACCOUNTS_CONTACT_ACCESS', 'PM');

INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('CRM_ACCOUNTS_CONTACT_ACCESS', 'ALLOW', 'ADMIN');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('CRM_ACCOUNTS_CONTACT_ACCESS', 'ALLOW', 'DR');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('CRM_ACCOUNTS_CONTACT_ACCESS', 'ALLOW', 'HR');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('CRM_ACCOUNTS_CONTACT_ACCESS', 'ALLOW', 'SALESMAN');

insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_ACCOUNTS_CONTACT_ACCESS', 'CRM');
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_ACCOUNTS_CONTACT_ACCESS', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_ACCOUNTS_CONTACT_ACCESS', 'PM');

INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('CRM_ACCOUNTS_CONTACT_ACCESS', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('CRM_ACCOUNTS_CONTACT_ACCESS', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('CRM_ACCOUNTS_CONTACT_ACCESS', 'ALLOW', 'HR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('CRM_ACCOUNTS_CONTACT_ACCESS', 'ALLOW', 'SALESMAN');