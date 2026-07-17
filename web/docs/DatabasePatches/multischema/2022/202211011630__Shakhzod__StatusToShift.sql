insert into "anv".reference (code, deleted, name, description, isremovable, issystemreference, shared, sorder,
                             parentid, iscustombutton, isactive)
values ('SHIFT_SUBMITTED', false, 'Shift Submitted', 'Shift Submitted', false, false, true, 1,
        (select r.id from "anv".reference r where r.code = 'SHIFT_STATUS' order by r.id desc limit 1), false, true );