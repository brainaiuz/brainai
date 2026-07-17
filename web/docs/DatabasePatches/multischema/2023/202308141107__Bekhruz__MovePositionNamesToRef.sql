delete
from "anv".reference
where code = 'POSITION_TITLES';
insert into "anv".reference (code, deleted, name, description, isremovable, issystemreference, shared, sorder,
                             parentid, iscustombutton, isactive)
values ('POSITION_TITLES', false, 'Position titles', 'Position titles', false, false, true, 0, null, false, true);

DROP function if EXISTS "anv".movePositionNameToRef();
CREATE
OR replace function "anv".movePositionNameToRef()
    returns INTEGER AS
$body$
DECLARE
role record;
BEGIN

FOR role IN (select DISTINCT ON (uzbek, russian, english, arabic) id,english from "anv".reference_locale
where id in (select localeId from "anv".position where isdeleted is false))
        loop



insert into  "anv".reference (parentId, code, name, deleted, isremovable, isactive, sorder, localeId)
values (
    (select id from "anv".reference where code = 'POSITION_TITLES' limit 1),
    UPPER((select name from "anv".position where localeId = role.id limit 1)),
    (select name from "anv".position where localeId = role.id limit 1),
    false, true, true,
    (select max(sorder) from "anv".reference where parentid = (select id from "anv".reference where code = 'POSITION_TITLES' limit 1)),
    role.id
);


END loop;
return NULL;
END;
$body$
LANGUAGE plpgsql;
ALTER function "anv".movePositionNameToRef() owner TO wfmtest;

UPDATE company
SET selectFunctioncolumn =(SELECT "anv".movePositionNameToRef())
WHERE id = (SELECT id FROM company LIMIT 1);