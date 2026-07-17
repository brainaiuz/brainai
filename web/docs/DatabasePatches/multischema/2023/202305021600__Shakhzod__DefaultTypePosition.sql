update "anv".position
set type = (select id from "anv".reference where code = 'TYPE_INTERNAL')
where isdeleted is not true
  and type is null;