DELETE FROM permission WHERE code = 'CONVERT_SALE_QUOTE_TO_SALE_INVOICE';
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
  VALUES ('CONVERT_SALE_QUOTE_TO_SALE_INVOICE', 'ACCOUNTING', 'Convert to Sales Invoice', 13,
  (SELECT id FROM permission WHERE code = 'ACCOUNTING_SALES_QUOTE_LIST'),'ACCOUNTING_MODULE') on conflict do nothing;

DELETE FROM "anv".permission_context WHERE permissioncode = 'CONVERT_SALE_QUOTE_TO_SALE_INVOICE' AND contextcode = 'ACCOUNTING';
insert into "anv".permission_context (permissioncode, contextcode) values ('CONVERT_SALE_QUOTE_TO_SALE_INVOICE', 'ACCOUNTING');

DELETE FROM "anv".rolepermission WHERE permissioncode = 'CONVERT_SALE_QUOTE_TO_SALE_INVOICE';
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('CONVERT_SALE_QUOTE_TO_SALE_INVOICE', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('CONVERT_SALE_QUOTE_TO_SALE_INVOICE', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('CONVERT_SALE_QUOTE_TO_SALE_INVOICE', 'ALLOW', 'ACCOUNTANT');

DELETE FROM "0".permission_context WHERE permissioncode = 'CONVERT_SALE_QUOTE_TO_SALE_INVOICE' AND contextcode = 'ACCOUNTING';
insert into "0".permission_context (permissioncode, contextcode) values ('CONVERT_SALE_QUOTE_TO_SALE_INVOICE', 'ACCOUNTING');

DELETE FROM "0".rolepermission WHERE permissioncode = 'CONVERT_SALE_QUOTE_TO_SALE_INVOICE';
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('CONVERT_SALE_QUOTE_TO_SALE_INVOICE', 'ALLOW', 'ADMIN');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('CONVERT_SALE_QUOTE_TO_SALE_INVOICE', 'ALLOW', 'DR');
INSERT INTO "0".rolepermission (permissioncode, access, rolecode) VALUES ('CONVERT_SALE_QUOTE_TO_SALE_INVOICE', 'ALLOW', 'ACCOUNTANT');