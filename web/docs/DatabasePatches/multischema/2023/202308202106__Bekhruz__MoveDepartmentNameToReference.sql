delete
from "anv".reference
where code = 'DEPARTMENT_TITLES';
insert into "anv".reference (code, deleted, name, description, isremovable, issystemreference, shared, sorder,
                             parentid, iscustombutton, isactive)
values ('DEPARTMENT_TITLES', false, 'Department titles', 'Department titles', false, false, true, 0, null, false, true);

DROP function if EXISTS "anv".moveDepartmentNameToRef();
CREATE
OR replace function "anv".moveDepartmentNameToRef()
    returns INTEGER AS
$body$
DECLARE
role record;
BEGIN

FOR role IN (select DISTINCT ON (uzbek, russian, english, arabic) id,english from "anv".reference_locale
where id in (select localeId from "anv".team where isDeleted is false))
        loop



insert into  "anv".reference (parentId, code, name, deleted, isremovable, isactive, sorder, localeId)
values (
    (select id from "anv".reference where code = 'DEPARTMENT_TITLES' limit 1),
    UPPER((select name from "anv".team where localeId = role.id limit 1)),
    (select name from "anv".team where localeId = role.id limit 1),
    false, true, true,
    (select max(sorder) from "anv".reference where parentid = (select id from "anv".reference where code = 'DEPARTMENT_TITLES' limit 1)),
    role.id
);


END loop;
return NULL;
END;
$body$
LANGUAGE plpgsql;
ALTER function "anv".moveDepartmentNameToRef() owner TO wfmtest;

UPDATE company
SET selectFunctioncolumn =(SELECT "anv".moveDepartmentNameToRef())
WHERE id = (SELECT id FROM company LIMIT 1);