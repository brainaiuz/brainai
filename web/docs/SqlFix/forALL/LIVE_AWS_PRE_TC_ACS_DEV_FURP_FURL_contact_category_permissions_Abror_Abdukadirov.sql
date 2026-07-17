delete from permission where code = 'CRM_REMOVE_CATEGORY';
delete from "0".rolepermission where permissioncode = 'CRM_REMOVE_CATEGORY';
delete from "anv".rolepermission where permissioncode = 'CRM_REMOVE_CATEGORY';

delete from permission where code = 'CRM_SHARE_CATEGORY';
delete from "0".rolepermission where permissioncode = 'CRM_SHARE_CATEGORY';
delete from "anv".rolepermission where permissioncode = 'CRM_SHARE_CATEGORY';

delete from permission where code = 'CRM_EDIT_CATEGORY';
delete from "0".rolepermission where permissioncode = 'CRM_EDIT_CATEGORY';
delete from "anv".rolepermission where permissioncode = 'CRM_EDIT_CATEGORY';

insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode) values('CRM_EDIT_CATEGORY', 'CRM', false, 'CRM Edit Category', 9, (select id from permission where code ='CRM_CONTACTS_LIST'), false, 'CONTACT_MANAGEMENT');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('CRM_EDIT_CATEGORY', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('CRM_EDIT_CATEGORY', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('CRM_EDIT_CATEGORY', 'SALESMAN','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('CRM_EDIT_CATEGORY', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('CRM_EDIT_CATEGORY', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('CRM_EDIT_CATEGORY', 'SALESMAN','ALLOW');

insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode) values('CRM_SHARE_CATEGORY', 'CRM', false, 'CRM Share Category', 9, (select id from permission where code ='CRM_CONTACTS_LIST'), false, 'CONTACT_MANAGEMENT');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('CRM_SHARE_CATEGORY', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('CRM_SHARE_CATEGORY', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('CRM_SHARE_CATEGORY', 'SALESMAN','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('CRM_SHARE_CATEGORY', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('CRM_SHARE_CATEGORY', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('CRM_SHARE_CATEGORY', 'SALESMAN','ALLOW');

insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode) values('CRM_REMOVE_CATEGORY', 'CRM', false, 'CRM Remove Category', 9, (select id from permission where code ='CRM_CONTACTS_LIST'), false, 'CONTACT_MANAGEMENT');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('CRM_REMOVE_CATEGORY', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('CRM_REMOVE_CATEGORY', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('CRM_REMOVE_CATEGORY', 'SALESMAN','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('CRM_REMOVE_CATEGORY', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('CRM_REMOVE_CATEGORY', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('CRM_REMOVE_CATEGORY', 'SALESMAN','ALLOW');