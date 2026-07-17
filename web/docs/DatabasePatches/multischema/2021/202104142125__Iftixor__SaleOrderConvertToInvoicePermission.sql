
DELETE FROM permission WHERE code = 'CONVERT_SALE_ORDER_TO_SALE_INVOICE';
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
  VALUES ('CONVERT_SALE_ORDER_TO_SALE_INVOICE', 'ACCOUNTING', 'Convert to Sales Invoice', 9,
  (SELECT id FROM permission WHERE code = 'ACCOUNTING_SALES_ORDER_LIST'),'SALES_ORDERS') on conflict do nothing;

DELETE FROM "anv".permission_context WHERE permissioncode = 'CONVERT_SALE_ORDER_TO_SALE_INVOICE' AND contextcode = 'ACCOUNTING';
insert into "anv".permission_context (permissioncode, contextcode) values ('CONVERT_SALE_ORDER_TO_SALE_INVOICE', 'ACCOUNTING');

DELETE FROM "anv".rolepermission WHERE permissioncode = 'CONVERT_SALE_ORDER_TO_SALE_INVOICE';
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('CONVERT_SALE_ORDER_TO_SALE_INVOICE', 'ALLOW', 'ADMIN');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('CONVERT_SALE_ORDER_TO_SALE_INVOICE', 'ALLOW', 'DR');
INSERT INTO "anv".rolepermission (permissioncode, access, rolecode) VALUES ('CONVERT_SALE_ORDER_TO_SALE_INVOICE', 'ALLOW', 'ACCOUNTANT');

update permission set modulecode = 'SALES_QUOTES' where code='CONVERT_SALE_QUOTE_TO_SALE_INVOICE';