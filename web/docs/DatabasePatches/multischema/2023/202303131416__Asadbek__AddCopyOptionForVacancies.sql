delete from permission where code = 'HRMS_VACANCY_COPY';
insert into permission(code,context,name,sorder,parent,modulecode) values
					  ('HRMS_VACANCY_COPY','HRMS','Copy',12,(select id from permission where code = 'HRMS_VACANCY_LIST_VIEW'),'RECRUITMENT_SYSTEM');

delete from "anv".permission_context where permissioncode = 'HRMS_VACANCY_COPY';
insert into "anv".permission_context (permissioncode, contextcode) values ('HRMS_VACANCY_COPY', 'HRMS');

delete from "anv".rolepermission where permissioncode = 'HRMS_VACANCY_COPY';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('HRMS_VACANCY_COPY', 'ALLOW', 'HR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('HRMS_VACANCY_COPY', 'ALLOW', 'DLOFPR');