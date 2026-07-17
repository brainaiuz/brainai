insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
values ('CETIFICATE_OF_EMPLOYMENT_SHOW_DETAILS_INFORMATION', 'HRMS', 'f', 'Show Certificate Details', coalesce((select sorder from permission where code = 'CETIFICATE_OF_EMPLOYMENT_DELETE'), 0) + 1, (select id from permission where code='CETIFICATE_OF_EMPLOYMENT_LIST'), 'HRMS_MODULE');

insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CETIFICATE_OF_EMPLOYMENT_SHOW_DETAILS_INFORMATION', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CETIFICATE_OF_EMPLOYMENT_SHOW_DETAILS_INFORMATION', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CETIFICATE_OF_EMPLOYMENT_SHOW_DETAILS_INFORMATION', 'HR', 'ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values ('CETIFICATE_OF_EMPLOYMENT_SHOW_DETAILS_INFORMATION', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('CETIFICATE_OF_EMPLOYMENT_SHOW_DETAILS_INFORMATION', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('CETIFICATE_OF_EMPLOYMENT_SHOW_DETAILS_INFORMATION', 'HR', 'ALLOW');


insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
values ('CETIFICATE_OF_EMPLOYMENT_SEE_ALL_CERTIFICATE_LIST', 'HRMS', 'f', 'Show All Certificates', coalesce((select sorder from permission where code = 'CETIFICATE_OF_EMPLOYMENT_DELETE'), 0) + 1, (select id from permission where code='CETIFICATE_OF_EMPLOYMENT_LIST'), 'HRMS_MODULE');

insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CETIFICATE_OF_EMPLOYMENT_SEE_ALL_CERTIFICATE_LIST', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CETIFICATE_OF_EMPLOYMENT_SEE_ALL_CERTIFICATE_LIST', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CETIFICATE_OF_EMPLOYMENT_SEE_ALL_CERTIFICATE_LIST', 'HR', 'ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values ('CETIFICATE_OF_EMPLOYMENT_SEE_ALL_CERTIFICATE_LIST', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('CETIFICATE_OF_EMPLOYMENT_SEE_ALL_CERTIFICATE_LIST', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('CETIFICATE_OF_EMPLOYMENT_SEE_ALL_CERTIFICATE_LIST', 'HR', 'ALLOW');