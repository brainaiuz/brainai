

update "anv".reference set name='1',sorder=1,changed=true where code='FULL_TIME' and parentid = (select id from "anv".reference where code='_EMPLOYMENT_MODE');
update "anv".reference set name='0.5',sorder=3,changed=true where code='PART_TIME' and parentid = (select id from "anv".reference where code='_EMPLOYMENT_MODE');

delete from "anv".reference where code='075_TIME' and parentid = (select id from "anv".reference where code='_EMPLOYMENT_MODE');
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive, changed)
values('075_TIME', false, true, '0.75', true, 2, (select id from "anv".reference where code='_EMPLOYMENT_MODE'), true, true );

delete from "anv".reference where code='QUARTER_TIME' and parentid = (select id from "anv".reference where code='_EMPLOYMENT_MODE');
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive, changed)
values('QUARTER_TIME', false, true, '0.25', true, 4, (select id from "anv".reference where code='_EMPLOYMENT_MODE'), true, true);
