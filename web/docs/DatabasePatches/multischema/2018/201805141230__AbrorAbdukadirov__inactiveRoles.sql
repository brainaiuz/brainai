drop function if exists inactive_roles();
create or replace function inactive_roles()
  returns integer as $body$
declare
  companyid text;
begin
  if (select catalog_name from information_schema.information_schema_catalog_name) = 'multischemafree'
  then
    for companyid in (select schema_name from information_schema.schemata where schema_owner = 'wfmtest')
    loop
      begin
        if exists (select nspname from pg_namespace where companyid = nspname order by nspname)
              and (select id from company where id = cast(companyid as int8)) is null
        then
          execute 'update "'||companyid||'".role set active = false where code in ($1, $2, $3)' using 'CALENDAR_EDITOR', 'CALENDAR_VIEWER', 'TIMESHEET_EDITOR';
        end if;
        exception when others then
      end;
    end loop;
  end if;
  return null;
end;
$body$
language plpgsql;
alter function inactive_roles() owner to postgres;
update company set selectFunctioncolumn =(select inactive_roles()) where  id=(select id from company limit 1);
drop function if exists inactive_roles();