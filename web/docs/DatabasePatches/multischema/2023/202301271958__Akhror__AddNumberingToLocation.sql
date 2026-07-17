insert into "anv".modelfield(form_id, field_id, columntype, forder, fsection)
values ('LOCATION_FORM', 'CODE', 'COL_1', 0, 'GENERAL_DETAILS');

update "anv".location
set intNumber = 1,
    code      = 'LOC0001'
where id = (select id from "anv".location where deleted = false order by id limit 1);

DROP function if EXISTS "anv".insertLocationNumbers();
CREATE
OR replace function "anv".insertLocationNumbers()
    returns INTEGER AS
$body$
DECLARE
role record;
BEGIN

FOR role IN (SELECT * FROM "anv".location where intnumber is null or code is null order by id)
        loop
update "anv".location
set intNumber = (select l.intNumber + 1
                 from "anv".location l
                 where l.deleted = false
                   and l.intNumber is not null
                 order by l.intNumber desc
    limit 1)
  , code = 'LOC' || (
select (case
    when l.intNumber + 1 < 10 then '000' || l.intNumber + 1
    when l.intNumber + 1 < 100 then '00' || l.intNumber + 1
    when l.intNumber + 1 < 1000 then '0' || l.intNumber + 1
    else '' || l.intNumber + 1 end)
from "anv".location l
where l.deleted = false
  and l.intNumber is not null
order by l.intNumber desc
    limit 1)
where id = role.id;
END loop;
return NULL;
END;
$body$
LANGUAGE plpgsql;
ALTER function "anv".insertLocationNumbers() owner TO wfmtest;

UPDATE company
SET selectFunctioncolumn =(SELECT "anv".insertLocationNumbers())
WHERE id = (SELECT id FROM company LIMIT 1);