insert into permission (code, context, name, parent, modulecode)
values ('SETTINGS_SEND_ADS_FORM', 'SETTINGS', 'Referral Form',
        (select id from permission where code = 'SETTINGS_COMPANY_SETTINGS'), 'CORE');
insert into "anv".permission_context (permissioncode, contextcode)
values ('SETTINGS_SEND_ADS_FORM', 'SETTINGS');

drop function if exists set_permission_allroles();
create
or replace function set_permission_allroles()
returns void as $$
 	declare
fold text;
begin
for fold in (select DISTINCT rolecode from "anv".rolepermission)
		loop
			insert into "anv".rolepermission (permissioncode,access, rolecode) values ('SETTINGS_SEND_ADS_FORM', 'ALLOW', fold);
end loop;
end;
$$
language plpgsql;

select set_permission_allroles();