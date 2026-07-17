DELETE FROM permission WHERE code = 'ACCOUNTING_UNIT_MEASUREMENTS_ADD';
INSERT INTO permission (code, context, name, sorder, parent, modulecode, iscore)
  VALUES ('ACCOUNTING_UNIT_MEASUREMENTS_ADD', 'SETTINGS', 'Add', 1,
  (SELECT id FROM permission WHERE code = 'ACCOUNTING_UNIT_MEASUREMENTS_LIST'),'INVENTORY_MANAGEMENT', true );

DELETE FROM "0".permission_context WHERE permissioncode = 'ACCOUNTING_UNIT_MEASUREMENTS_ADD' AND contextcode = 'SETTINGS';
insert into "0".permission_context (permissioncode, contextcode) values ('ACCOUNTING_UNIT_MEASUREMENTS_ADD', 'SETTINGS');

DELETE FROM "0".rolepermission WHERE permissioncode = 'ACCOUNTING_UNIT_MEASUREMENTS_ADD';
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_UNIT_MEASUREMENTS_ADD', 'ALLOW', 'ADMIN');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_UNIT_MEASUREMENTS_ADD', 'ALLOW', 'DR');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_UNIT_MEASUREMENTS_ADD', 'ALLOW', 'ACCOUNTANT');


DELETE FROM "anv".permission_context WHERE permissioncode = 'ACCOUNTING_UNIT_MEASUREMENTS_ADD' AND contextcode = 'SETTINGS';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_UNIT_MEASUREMENTS_ADD', 'SETTINGS');

DELETE FROM "anv".rolepermission WHERE permissioncode = 'ACCOUNTING_UNIT_MEASUREMENTS_ADD';
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_UNIT_MEASUREMENTS_ADD', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_UNIT_MEASUREMENTS_ADD', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('ACCOUNTING_UNIT_MEASUREMENTS_ADD', 'ALLOW', 'ACCOUNTANT');
