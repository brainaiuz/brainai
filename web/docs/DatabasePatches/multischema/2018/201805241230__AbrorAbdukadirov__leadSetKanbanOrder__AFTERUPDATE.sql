drop function if exists "anv".leadsetkanbanorder();
create or replace function "anv".leadsetkanbanorder()
  returns integer as
$body$
declare
  statusId integer;
  lead record;
  sorderId integer;
  sorder bigint;
  leadCount integer;
  statusCount integer;
begin
  execute 'select count(r.id) from "anv".reference r where r.deleted = false ' ||
              ' and r.parentId = (select count(id) from "anv".reference where code = ''_LEAD_STATUS'')' into statusCount;
  if statusCount > 0
  then
      for statusId in execute('select r.id from "anv".reference r where r.deleted = false ' ||
                              ' and r.parentId = (select id from "anv".reference where code = ''_LEAD_STATUS'')')
      loop
          sorder := 65535;
          sorderId := statusId;

          execute 'select count(l.id) from "anv".crmContact l where l.contactType = 5 and l.deleted = false ' ||
                  ' and l.status = '||statusId||'' into leadCount;
          if leadCount > 0
          then
              for lead in execute('select * from "anv".crmContact l where l.contactType = 5 and l.deleted = false ' ||
                                  ' and l.status = '||statusId||' order by l.creationdate')
              loop
                  if (statusId <> sorderId)
                  then
                      sorderId := statusId;
                      sorder := 65535;
                  end if;
                  execute 'update "anv".crmContact set kanban_order = $1 where id = $2' using sorder, lead.id;
                  sorder := sorder + 65535 + 1;
              end loop;
          end if;
      end loop;
      sorder := 65535;
      for lead in execute('select * from "anv".crmContact l where l.contactType = 5 ' ||
                              ' and l.status is null and l.deleted = false order by l.creationdate')
      loop
          execute 'update "anv".crmContact set kanban_order = $1 where id = $2' using sorder, lead.id;
          sorder := sorder + 65535 + 1;
      end loop;
  end if;
  return null;
end;
$body$
language plpgsql;
alter function "anv".leadsetkanbanorder() owner to wfmtest;
update company set selectFunctioncolumn =(select "anv".leadsetkanbanorder()) where  id=(select id from company limit 1);