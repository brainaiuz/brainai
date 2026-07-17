insert into permission (code, context, name,  parent, modulecode)
values ('VACANCY_LINKS', 'HRMS', 'Links',  (select id from permission where code = 'HRMS_VACANCY_LIST_VIEW'), 'RECRUITMENT_SYSTEM');

insert into "anv".permission_context (permissioncode, contextcode)
values ('VACANCY_LINKS', 'HRMS');


insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('VACANCY_LINKS', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('VACANCY_LINKS', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('VACANCY_LINKS', 'ALLOW', 'HR');