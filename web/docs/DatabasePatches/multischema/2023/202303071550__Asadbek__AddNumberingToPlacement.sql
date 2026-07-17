update "anv".placement
set intNumber = 1,
    numberdata      = 'P0001'
where id = (select id from "anv".placement where deleted = false order by id limit 1);

DROP function if EXISTS "anv".insertPlacNumbers();
CREATE
OR replace function "anv".insertPlacNumbers()
    returns INTEGER AS
$body$
DECLARE
role record;
BEGIN

FOR role IN (SELECT * FROM "anv".placement where intnumber is null or numberdata is null order by id)
        loop
update "anv".placement
set intNumber = (select p.intNumber + 1
                 from "anv".placement p
                 where p.deleted = false
                   and p.intNumber is not null
                 order by p.intNumber desc
    limit 1)
  , numberdata = 'P' || (
select (case
    when p.intNumber + 1 < 10 then '000' || p.intNumber + 1
    when p.intNumber + 1 < 100 then '00' || p.intNumber + 1
    when p.intNumber + 1 < 1000 then '0' || p.intNumber + 1
    else '' || p.intNumber + 1 end)
from "anv".placement p
where p.deleted = false
  and p.intNumber is not null
order by p.intNumber desc
    limit 1)
where id = role.id;
END loop;
return NULL;
END;
$body$
LANGUAGE plpgsql;
ALTER function "anv".insertPlacNumbers() owner TO wfmtest;

UPDATE company
SET selectFunctioncolumn =(SELECT "anv".insertPlacNumbers())
WHERE id = (SELECT id FROM company LIMIT 1);