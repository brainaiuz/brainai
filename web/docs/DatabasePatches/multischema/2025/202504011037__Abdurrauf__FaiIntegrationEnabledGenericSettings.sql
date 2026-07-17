delete
from "0".genericsettings
where key = 'FAI_INTEGRATION_ENABLED';
delete
from "anv".genericsettings
where key = 'FAI_INTEGRATION_ENABLED';

insert into "0".genericsettings(key, value)
values ('FAI_INTEGRATION_ENABLED', 'NO');
insert into "anv".genericsettings(key, value)
values ('FAI_INTEGRATION_ENABLED', 'NO');

do $$
    begin
        if exists (select 1 from pg_namespace where nspname = '59981') then
            execute 'update "59981".genericsettings set value = ''YES'' where key = ''FAI_INTEGRATION_ENABLED'';';
        end if;
    end $$;
do $$
    begin
        if exists (select 1 from pg_namespace where nspname = '64999') then
execute 'update "64999".genericsettings set value = ''YES'' where key = ''FAI_INTEGRATION_ENABLED'';';
        end if;
    end $$;
do $$
    begin
        if exists (select 1 from pg_namespace where nspname = '300525') then
            execute 'update "300525".genericsettings set value = ''YES'' where key = ''FAI_INTEGRATION_ENABLED'';';
        end if;
    end $$;
do $$
    begin
        if exists (select 1 from pg_namespace where nspname = '307306') then
            execute 'update "307306".genericsettings set value = ''YES'' where key = ''FAI_INTEGRATION_ENABLED'';';
        end if;
    end $$;
do $$
    begin
        if exists (select 1 from pg_namespace where nspname = '309062') then
            execute 'update "309062".genericsettings set value = ''YES'' where key = ''FAI_INTEGRATION_ENABLED'';';
        end if;
    end $$;
do $$
    begin
        if exists (select 1 from pg_namespace where nspname = '311628') then
            execute 'update "311628".genericsettings set value = ''YES'' where key = ''FAI_INTEGRATION_ENABLED'';';
        end if;
    end $$;
do $$
    begin
        if exists (select 1 from pg_namespace where nspname = '312391') then
            execute 'update "312391".genericsettings set value = ''YES'' where key = ''FAI_INTEGRATION_ENABLED'';';
        end if;
    end $$;
do $$
    begin
        if exists (select 1 from pg_namespace where nspname = '313046') then
            execute 'update "313046".genericsettings set value = ''YES'' where key = ''FAI_INTEGRATION_ENABLED'';';
        end if;
    end $$;
do $$
    begin
        if exists (select 1 from pg_namespace where nspname = '317653') then
            execute 'update "317653".genericsettings set value = ''YES'' where key = ''FAI_INTEGRATION_ENABLED'';';
        end if;
    end $$;
do $$
    begin
        if exists (select 1 from pg_namespace where nspname = '325292') then
            execute 'update "325292".genericsettings set value = ''YES'' where key = ''FAI_INTEGRATION_ENABLED'';';
        end if;
    end $$;
