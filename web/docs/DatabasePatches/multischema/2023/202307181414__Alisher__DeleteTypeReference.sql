update "anv".position set type = (select id from "anv".reference where code = 'TYPE_INTERNAL' and localeid is not null limit 1)
where type in (select id from "anv".reference where code = 'TYPE_INTERNAL' and localeid is null);

delete from "anv".reference where code = 'TYPE_INTERNAL' and localeid is null;

