insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_CERTIFICATES_ADD_FOR_OTHERS', 'HRMS', 'Add For Others', 7,
        (select id from permission where code = 'CETIFICATE_OF_EMPLOYMENT_LIST'),'HRMS_MODULE');

insert into "0".permission_context (permissioncode, contextcode) values ('HRMS_CERTIFICATES_ADD_FOR_OTHERS', 'HRMS');
insert into "0".rolepermission (permissioncode, access, rolecode) values ('HRMS_CERTIFICATES_ADD_FOR_OTHERS', 'ALLOW', 'HR');
insert into "0".rolepermission (permissioncode, access, rolecode) values ('HRMS_CERTIFICATES_ADD_FOR_OTHERS', 'ALLOW', 'DR');

insert into "anv".permission_context (permissioncode, contextcode) values ('HRMS_CERTIFICATES_ADD_FOR_OTHERS', 'HRMS');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('HRMS_CERTIFICATES_ADD_FOR_OTHERS', 'ALLOW', 'HR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('HRMS_CERTIFICATES_ADD_FOR_OTHERS', 'ALLOW', 'DR');

insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_CERTIFICATES_PDF', 'HRMS', 'Print PDF', 8,
        (select id from permission where code = 'CETIFICATE_OF_EMPLOYMENT_LIST'),'HRMS_MODULE');

insert into "0".permission_context (permissioncode, contextcode) values ('HRMS_CERTIFICATES_PDF', 'HRMS');
insert into "0".rolepermission (permissioncode, access, rolecode) values ('HRMS_CERTIFICATES_PDF', 'ALLOW', 'HR');
insert into "0".rolepermission (permissioncode, access, rolecode) values ('HRMS_CERTIFICATES_PDF', 'ALLOW', 'DR');

insert into "anv".permission_context (permissioncode, contextcode) values ('HRMS_CERTIFICATES_PDF', 'HRMS');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('HRMS_CERTIFICATES_PDF', 'ALLOW', 'HR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('HRMS_CERTIFICATES_PDF', 'ALLOW', 'DR');