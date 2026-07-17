insert into permission (code, context, name, sorder, parent, modulecode)
select 'CRM_SEE_OWN_ACTIVITY_EVENT', 'CRM', 'See Own', 2,
        (select id from permission where code = 'CRM_ACTIVITIES_LIST'),'ACTIVITIES'
where NOT EXISTS (SELECT id from permission where code='CRM_SEE_OWN_ACTIVITY_EVENT');

insert into "anv".permission_context (permissioncode, contextcode)
 select 'CRM_SEE_OWN_ACTIVITY_EVENT', 'CRM' where NOT EXISTS (select permissioncode from "anv".permission_context where permissioncode = 'CRM_SEE_OWN_ACTIVITY_EVENT');

insert into "anv".rolepermission (permissioncode, access, rolecode)
 select 'CRM_SEE_OWN_ACTIVITY_EVENT', 'ALLOW', 'DR' where NOT EXISTS (select id from "anv".rolepermission where permissioncode = 'CRM_SEE_OWN_ACTIVITY_EVENT');
insert into "anv".rolepermission (permissioncode, access, rolecode)
select 'CRM_SEE_OWN_ACTIVITY_EVENT', 'ALLOW', 'SALESMAN' where NOT EXISTS (select id from "anv".rolepermission where permissioncode = 'CRM_SEE_OWN_ACTIVITY_EVENT');