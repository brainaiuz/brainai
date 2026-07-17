drop function if exists "anv".opportunitysetkanbanorder();
create or replace function "anv".opportunitysetkanbanorder()
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
              ' and r.parentId = (select id from "anv".reference where code = ''_OPPORTUNITY_STAGE'')' into statusCount;
    if statusCount > 0
    then
        for statusId in execute('select r.id from "anv".reference r where r.deleted = false ' ||
                                ' and r.parentId = (select id from "anv".reference where code = ''_OPPORTUNITY_STAGE'')')
        loop
            sorder := 65535;
            sorderId := statusId;

            execute 'select count(l.id) from "anv".opportunity l where l.deleted = false ' ||
                    ' and l.stage = '||statusId||'' into leadCount;
            if leadCount > 0
            then
                for lead in execute('select * from "anv".opportunity l where l.deleted = false ' ||
                                    ' and l.stage = '||statusId||' order by l.id DESC')
                loop
                    if (statusId <> sorderId)
                    then
                        sorderId := statusId;
                        sorder := 65535;
                    end if;
                    execute 'update "anv".opportunity set kanban_order = $1 where id = $2' using sorder, lead.id;
                    sorder := sorder + 65535 + 1;
              end loop;
            end if;
        end loop;
        sorder := 65535;
        for lead in execute('select * from "anv".opportunity l where ' ||
                                ' l.stage is null and l.deleted = false order by l.id DESC')
        loop
            execute 'update "anv".opportunity set kanban_order = $1 where id = $2' using sorder, lead.id;
            sorder := sorder + 65535 + 1;
        end loop;
    end if;
    return null;
end;
$body$
language plpgsql;
alter function "anv".opportunitysetkanbanorder() owner to wfmtest;
update company set selectFunctioncolumn =(select "anv".opportunitysetkanbanorder()) where  id=(select id from company limit 1);