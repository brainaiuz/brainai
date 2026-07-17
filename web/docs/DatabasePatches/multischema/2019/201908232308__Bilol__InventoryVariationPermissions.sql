delete from permission WHERE code in('INVENTORY_VARIATION_ADD','INVENTORY_VARIATION_DELETE');

delete from "0".permission_context where permissioncode in('INVENTORY_VARIATION_ADD','INVENTORY_VARIATION_DELETE');
delete from "anv".permission_context where permissioncode in('INVENTORY_VARIATION_ADD','INVENTORY_VARIATION_DELETE');

delete from "0".rolepermission where permissioncode in('INVENTORY_VARIATION_ADD','INVENTORY_VARIATION_DELETE');
delete from "anv".rolepermission where permissioncode in('INVENTORY_VARIATION_ADD','INVENTORY_VARIATION_DELETE');


insert into permission (code, context, name, sorder, parent, modulecode)
values('INVENTORY_VARIATION_ADD', 'ACCOUNTING', 'Inventory Variation Add', 6, (select id from permission WHERE code='ACCOUNTING_INVENTORY_LIST'), 'PRODUCT_INVENTORY_ITEMS');

insert into permission (code, context, name, sorder, parent, modulecode)
values('INVENTORY_VARIATION_DELETE', 'ACCOUNTING', 'Inventory Variation Delete', 7, (select id from permission WHERE code='ACCOUNTING_INVENTORY_LIST'), 'PRODUCT_INVENTORY_ITEMS');



insert into "0".permission_context (permissioncode, contextcode) values('INVENTORY_VARIATION_ADD', 'ACCOUNTING');
insert into "0".permission_context (permissioncode, contextcode) values('INVENTORY_VARIATION_DELETE', 'ACCOUNTING');

insert into "anv".permission_context (permissioncode, contextcode) values('INVENTORY_VARIATION_ADD', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values('INVENTORY_VARIATION_DELETE', 'ACCOUNTING');

insert into "0".rolepermission (permissioncode, rolecode, access) values('INVENTORY_VARIATION_ADD', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('INVENTORY_VARIATION_ADD', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('INVENTORY_VARIATION_ADD', 'ACCOUNTANT','ALLOW');


insert into "0".rolepermission (permissioncode, rolecode, access) values('INVENTORY_VARIATION_DELETE', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('INVENTORY_VARIATION_DELETE', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('INVENTORY_VARIATION_DELETE', 'ACCOUNTANT','ALLOW');


insert into "anv".rolepermission (permissioncode, rolecode, access) values('INVENTORY_VARIATION_ADD', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('INVENTORY_VARIATION_ADD', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('INVENTORY_VARIATION_ADD', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('INVENTORY_VARIATION_DELETE', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('INVENTORY_VARIATION_DELETE', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('INVENTORY_VARIATION_DELETE', 'ACCOUNTANT','ALLOW');
