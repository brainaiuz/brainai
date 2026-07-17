insert into "anv".reference (code, deleted, name, description, isremovable, issystemreference, shared, sorder,
                             parentid, iscustombutton, isactive)
values ('SHIFT_STATUS', false, 'Shift', 'Shift', false, false, true, 0, null, false, true);



insert into "anv".reference (code, deleted, name, description, isremovable, issystemreference, shared, sorder,
                             parentid, iscustombutton, isactive)
values ('SHIFT_REJECTED', false, 'Shift Rejected', 'Shift Rejected', false, false, true, 1,
        (select r.id from "anv".reference r where r.code = 'SHIFT_STATUS' order by r.id desc limit 1), false, true),
('SHIFT_SUBMITTED', false, 'Shift Submitted', 'Shift Submitted', false, false, true, 2,
(select r.id from "anv".reference r where r.code = 'OVERTIME_STATUS' order by r.id desc limit 1), false, true),
('SHIFT_APPROVED', false, 'Shift Approved', 'Shift Approved', false, false, true, 3,
(select r.id from "anv".reference r where r.code = 'SHIFT_STATUS' order by r.id desc limit 1), false, true),
('SHIFT_DRAFT', false, 'Shift Draft', 'Shift Draft', false, false, true, 4,
(select r.id from "anv".reference r where r.code = 'SHIFT_STATUS' order by r.id desc limit 1), false, true);