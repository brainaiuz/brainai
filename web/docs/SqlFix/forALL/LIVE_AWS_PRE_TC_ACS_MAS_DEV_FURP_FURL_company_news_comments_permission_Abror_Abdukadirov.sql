insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
values ('HRMS_COMPANY_NEWS_ADD_COMMENTS', 'HRMS', 'f', 'Add Comments', 10, (select id from permission where code = 'HRMS_COMPANY_NEWS'), 'HRMS_MODULE');

insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_COMPANY_NEWS_ADD_COMMENTS', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_COMPANY_NEWS_ADD_COMMENTS', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_COMPANY_NEWS_ADD_COMMENTS', 'HR', 'ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_COMPANY_NEWS_ADD_COMMENTS', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_COMPANY_NEWS_ADD_COMMENTS', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_COMPANY_NEWS_ADD_COMMENTS', 'HR', 'ALLOW');
