DELETE FROM permission WHERE code = 'SAVE_FILTER';
INSERT INTO permission (code, context, name, sorder, parent, modulecode, iscore)
  VALUES ('SAVE_FILTER', 'ACCOUNTING', 'GDN Save Filter', 33,
  (SELECT id FROM permission WHERE code = 'ACCOUNTING_SALES_ORDER_LIST'),'CORE', true );

DELETE FROM "0".permission_context WHERE permissioncode = 'SAVE_FILTER' AND contextcode = 'ACCOUNTING';
insert into "0".permission_context (permissioncode, contextcode) values ('SAVE_FILTER', 'ACCOUNTING');

DELETE FROM "0".rolepermission WHERE permissioncode = 'SAVE_FILTER';
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('SAVE_FILTER', 'ALLOW', 'ADMIN');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('SAVE_FILTER', 'ALLOW', 'DR');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('SAVE_FILTER', 'ALLOW', 'ACCOUNTANT');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('SAVE_FILTER', 'ALLOW', 'MEM');


DELETE FROM "anv".permission_context WHERE permissioncode = 'SAVE_FILTER' AND contextcode = 'ACCOUNTING';
insert into "anv".permission_context (permissioncode, contextcode) values ('SAVE_FILTER', 'ACCOUNTING');

DELETE FROM "anv".rolepermission WHERE permissioncode = 'SAVE_FILTER';
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('SAVE_FILTER', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('SAVE_FILTER', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('SAVE_FILTER', 'ALLOW', 'ACCOUNTANT');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('SAVE_FILTER', 'ALLOW', 'MEM');


DELETE FROM permission WHERE code = 'RESET_FILTER';
INSERT INTO permission (code, context, name, sorder, parent, modulecode, iscore)
  VALUES ('RESET_FILTER', 'ACCOUNTING', 'GDN Reset Filter', 34,
  (SELECT id FROM permission WHERE code = 'ACCOUNTING_SALES_ORDER_LIST'),'CORE', true );

DELETE FROM "0".permission_context WHERE permissioncode = 'RESET_FILTER' AND contextcode = 'ACCOUNTING';
insert into "0".permission_context (permissioncode, contextcode) values ('RESET_FILTER', 'ACCOUNTING');

DELETE FROM "0".rolepermission WHERE permissioncode = 'RESET_FILTER';
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('RESET_FILTER', 'ALLOW', 'ADMIN');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('RESET_FILTER', 'ALLOW', 'DR');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('RESET_FILTER', 'ALLOW', 'ACCOUNTANT');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('RESET_FILTER', 'ALLOW', 'MEM');


DELETE FROM "anv".permission_context WHERE permissioncode = 'RESET_FILTER' AND contextcode = 'ACCOUNTING';
insert into "anv".permission_context (permissioncode, contextcode) values ('RESET_FILTER', 'ACCOUNTING');

DELETE FROM "anv".rolepermission WHERE permissioncode = 'RESET_FILTER';
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('RESET_FILTER', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('RESET_FILTER', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('RESET_FILTER', 'ALLOW', 'ACCOUNTANT');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('RESET_FILTER', 'ALLOW', 'MEM');


update permission set sorder=35  where code='SALES_ORDER_SEE_OWN';


