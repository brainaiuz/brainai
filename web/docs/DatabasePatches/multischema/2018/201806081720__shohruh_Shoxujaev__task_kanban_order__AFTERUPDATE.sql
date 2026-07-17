drop function if exists "anv".tasksetkanbanorder();
create or replace function "anv".tasksetkanbanorder()
  returns integer as
$body$
declare
  statusId integer;
  task record;
  sorderId integer;
  sorder bigint;
  taskCount integer;
  statusCount integer;
begin
    execute 'select count(r.id) from "anv".reference r where r.deleted = false ' ||
              ' and r.parentId = (select id from "anv".reference where code = ''_TASK_STATUS'')' into statusCount;
    if statusCount > 0
    then
        for statusId in execute('select r.id from "anv".reference r where r.deleted = false ' ||
                                ' and r.parentId = (select id from "anv".reference where code = ''_TASK_STATUS'')')
        loop
            sorder := 65535;
            sorderId := statusId;

            execute 'select count(l.id) from "anv".task l where l.deleted = false ' ||
                    ' and l.statusid = '||statusId||'' into taskCount;
            if taskCount > 0
            then
                for task in execute('select * from "anv".task l where l.deleted = false ' ||
                                    ' and l.statusid = '||statusId||' order by l.id DESC')
                loop
                    if (statusId <> sorderId)
                    then
                        sorderId := statusId;
                        sorder := 65535;
                    end if;
                    execute 'update "anv".task set kanban_order = $1 where id = $2' using sorder, task.id;
                    sorder := sorder + 65535 + 1;
                end loop;
            end if;
        end loop;
        sorder := 65535;
        for task in execute('select * from "anv".task l where ' ||
                                ' l.statusid is null and l.deleted = false order by l.id DESC')
          loop
              execute 'update "anv".task set kanban_order = $1 where id = $2' using sorder, task.id;
              sorder := sorder + 65535 + 1;
        end loop;
    end if;
    return null;
end;
$body$
language plpgsql;
alter function "anv".tasksetkanbanorder() owner to wfmtest;
update company set selectFunctioncolumn =(select "anv".tasksetkanbanorder()) where  id=(select id from company limit 1);