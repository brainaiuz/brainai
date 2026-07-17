
insert into permission(code, context, name, sorder, parent, modulecode) values ('CRM_COPY_ACTIVITY','CRM','Copy',10,(select id from permission where code='CRM_ACTIVITIES_LIST'),'CORE');

---Zero
insert into "0".rolepermission(permissioncode,access,rolecode) values ('CRM_COPY_ACTIVITY','ALLOW','ADMIN');
insert into "0".rolepermission(permissioncode,access,rolecode) values ('CRM_COPY_ACTIVITY','ALLOW','DR');
insert into "0".rolepermission(permissioncode,access,rolecode) values ('CRM_COPY_ACTIVITY','ALLOW','ACCOUNTANT');

delete from "0".permission_context where permissioncode = 'CRM_COPY_ACTIVITY' and contextcode='CRM';
insert into "0".permission_context(permissioncode,contextcode) values ('CRM_COPY_ACTIVITY','CRM');


--All

insert into "anv".rolepermission(permissioncode,access,rolecode) values ('CRM_COPY_ACTIVITY','ALLOW','ADMIN');
insert into "anv".rolepermission(permissioncode,access,rolecode) values ('CRM_COPY_ACTIVITY','ALLOW','DR');
insert into "anv".rolepermission(permissioncode,access,rolecode) values ('CRM_COPY_ACTIVITY','ALLOW','ACCOUNTANT');

delete from "anv".permission_context where permissioncode = 'CRM_COPY_ACTIVITY' and contextcode='CRM';
insert into "anv".permission_context(permissioncode,contextcode) values ('CRM_COPY_ACTIVITY','CRM');
