
insert into permission (code, context, name, sorder, parent, modulecode) values('BILL_OF_MATERIAL_EDIT', 'PM', 'Edit', 5, (select id from permission where code ='BILL_OF_MATERIALS'), 'PM');

insert into "0".rolepermission (permissioncode, rolecode, access) values('BILL_OF_MATERIAL_EDIT', 'ADMIN','ALLOW');
insert into "0".permission_context(permissioncode, contextcode) values('BILL_OF_MATERIAL_EDIT', 'PM');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('BILL_OF_MATERIAL_EDIT', 'ADMIN','ALLOW');
insert into "anv".permission_context(permissioncode, contextcode) values('BILL_OF_MATERIAL_EDIT', 'PM');
