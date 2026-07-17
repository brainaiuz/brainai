alter table if exists "anv".uploadminiosettings
    alter column filelink type varchar(400) using filelink::varchar(400);
alter table if exists "anv".uploadamazonsettings
    alter column filelink type varchar(400) using filelink::varchar(400);
