insert into "anv".reference (code, deleted, name, description, isremovable, issystemreference, shared, sorder,
                             parentid, iscustombutton, isactive)
values ('POSITION_TYPE', false, 'TYPE', 'TYPE', false, false, true, 0, null, false, true);



insert into "anv".reference (code, deleted, name, description, isremovable, issystemreference, shared, sorder,
                             parentid, iscustombutton, isactive)
values ('TYPE_INTERNAL', false, 'INTERNAL', 'INTERNAL', false, false, true, 1,
        (select r.id from "anv".reference r where r.code = 'POSITION_TYPE' order by r.id desc limit 1), false, true),
('TYPE_EXTERNAL', false, 'EXTERNAL', 'EXTERNAL', false, false, true, 2,
(select r.id from "anv".reference r where r.code = 'POSITION_TYPE' order by r.id desc limit 1), false, true);


insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder, hide)
values ('POSITION_FORM', 'TYPE', false, 'COL_3', 'BASIC_INFORMATION', 0, true);