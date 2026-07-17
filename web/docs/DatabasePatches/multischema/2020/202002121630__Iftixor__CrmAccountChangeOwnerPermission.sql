DELETE FROM permission WHERE code = 'CRM_ACCOUNTS_CHANGE_OWNER';
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
  VALUES ('CRM_ACCOUNTS_CHANGE_OWNER', 'CRM', 'Change owner', 13,
  (SELECT id FROM permission WHERE code = 'CRM_ACCOUNTS_LIST'),'CRM_MODULE');

DELETE FROM "0".permission_context WHERE permissioncode = 'CRM_ACCOUNTS_CHANGE_OWNER' AND contextcode = 'CRM';
insert into "0".permission_context (permissioncode, contextcode) values ('CRM_ACCOUNTS_CHANGE_OWNER', 'CRM');

DELETE FROM "0".rolepermission WHERE permissioncode = 'CRM_ACCOUNTS_CHANGE_OWNER';
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('CRM_ACCOUNTS_CHANGE_OWNER', 'ALLOW', 'ADMIN');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('CRM_ACCOUNTS_CHANGE_OWNER', 'ALLOW', 'DR');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('CRM_ACCOUNTS_CHANGE_OWNER', 'ALLOW', 'ACCOUNTANT');


DELETE FROM "anv".permission_context WHERE permissioncode = 'CRM_ACCOUNTS_CHANGE_OWNER' AND contextcode = 'CRM';
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_ACCOUNTS_CHANGE_OWNER', 'CRM');

DELETE FROM "anv".rolepermission WHERE permissioncode = 'CRM_ACCOUNTS_CHANGE_OWNER';
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('CRM_ACCOUNTS_CHANGE_OWNER', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('CRM_ACCOUNTS_CHANGE_OWNER', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('CRM_ACCOUNTS_CHANGE_OWNER', 'ALLOW', 'ACCOUNTANT');

