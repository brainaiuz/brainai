insert into permission (code, context, name, sorder, parent, modulecode) values('BILL_OF_MATERIALS', 'PM', 'Bill Of Materials', 27, (select id from permission where code ='PM_PROJECT_LIST'), 'PM');
insert into permission (code, context, name, sorder, parent, modulecode) values('BILL_OF_MATERIAL_CREATE_PROJECT_PLAN', 'PM', 'Create project plan', 1, (select id from permission where code ='BILL_OF_MATERIALS'), 'PM');
insert into permission (code, context, name, sorder, parent, modulecode) values('BILL_OF_MATERIAL_SUBMIT_TO_MANAGER', 'PM', 'Submit to manager', 2, (select id from permission where code ='BILL_OF_MATERIALS'), 'PM');
insert into permission (code, context, name, sorder, parent, modulecode) values('BILL_OF_MATERIAL_APPROVE_REJECT', 'PM', 'Approve/Reject', 3, (select id from permission where code ='BILL_OF_MATERIALS'), 'PM');
insert into permission (code, context, name, sorder, parent, modulecode) values('BILL_OF_MATERIAL_REQUEST_MATERIALS', 'PM', 'Request materials', 4, (select id from permission where code ='BILL_OF_MATERIALS'), 'PM');

insert into "0".rolepermission (permissioncode, rolecode, access) values('BILL_OF_MATERIALS', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('BILL_OF_MATERIALS', 'DR','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('BILL_OF_MATERIAL_CREATE_PROJECT_PLAN', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('BILL_OF_MATERIAL_CREATE_PROJECT_PLAN', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('BILL_OF_MATERIAL_CREATE_PROJECT_PLAN', 'PM','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('BILL_OF_MATERIAL_SUBMIT_TO_MANAGER', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('BILL_OF_MATERIAL_SUBMIT_TO_MANAGER', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('BILL_OF_MATERIAL_SUBMIT_TO_MANAGER', 'PM','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('BILL_OF_MATERIAL_APPROVE_REJECT', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('BILL_OF_MATERIAL_APPROVE_REJECT', 'DR','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('BILL_OF_MATERIAL_REQUEST_MATERIALS', 'ADMIN','ALLOW');

insert into "0".permission_context(permissioncode, contextcode) values('BILL_OF_MATERIALS', 'PM');
insert into "0".permission_context(permissioncode, contextcode) values('BILL_OF_MATERIAL_CREATE_PROJECT_PLAN', 'PM');
insert into "0".permission_context(permissioncode, contextcode) values('BILL_OF_MATERIAL_SUBMIT_TO_MANAGER', 'PM');
insert into "0".permission_context(permissioncode, contextcode) values('BILL_OF_MATERIAL_APPROVE_REJECT', 'PM');
insert into "0".permission_context(permissioncode, contextcode) values('BILL_OF_MATERIAL_REQUEST_MATERIALS', 'PM');






insert into "anv".rolepermission (permissioncode, rolecode, access) values('BILL_OF_MATERIALS', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('BILL_OF_MATERIALS', 'DR','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('BILL_OF_MATERIAL_CREATE_PROJECT_PLAN', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('BILL_OF_MATERIAL_CREATE_PROJECT_PLAN', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('BILL_OF_MATERIAL_CREATE_PROJECT_PLAN', 'PM','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('BILL_OF_MATERIAL_SUBMIT_TO_MANAGER', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('BILL_OF_MATERIAL_SUBMIT_TO_MANAGER', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('BILL_OF_MATERIAL_SUBMIT_TO_MANAGER', 'PM','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('BILL_OF_MATERIAL_APPROVE_REJECT', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('BILL_OF_MATERIAL_APPROVE_REJECT', 'DR','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('BILL_OF_MATERIAL_REQUEST_MATERIALS', 'ADMIN','ALLOW');

insert into "anv".permission_context(permissioncode, contextcode) values('BILL_OF_MATERIALS', 'PM');
insert into "anv".permission_context(permissioncode, contextcode) values('BILL_OF_MATERIAL_CREATE_PROJECT_PLAN', 'PM');
insert into "anv".permission_context(permissioncode, contextcode) values('BILL_OF_MATERIAL_SUBMIT_TO_MANAGER', 'PM');
insert into "anv".permission_context(permissioncode, contextcode) values('BILL_OF_MATERIAL_APPROVE_REJECT', 'PM');
insert into "anv".permission_context(permissioncode, contextcode) values('BILL_OF_MATERIAL_REQUEST_MATERIALS', 'PM');






