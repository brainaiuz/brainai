delete
from "anv".reference
where code = 'OVERTIME';
insert into "anv".reference (code, deleted, name, description, isremovable, issystemreference, shared, sorder,
                             parentid, iscustombutton, isactive)
values ('OVERTIME_STATUS', false, 'Overtime', 'Overtime', false, false, true, 0, null, false, true);



insert into "anv".reference (code, deleted, name, description, isremovable, issystemreference, shared, sorder,
                             parentid, iscustombutton, isactive)
values ('OVERTIME_REJECTED', false, 'Overtime Rejected', 'Overtime Rejected', false, false, true, 1,
        (select r.id from "anv".reference r where r.code = 'OVERTIME_STATUS' order by r.id desc limit 1), false, true),
('OVERTIME_SUBMITTED', false, 'Overtime Submitted', 'Overtime Submitted', false, false, true, 2,
(select r.id from "anv".reference r where r.code = 'OVERTIME_STATUS' order by r.id desc limit 1), false, true),
('OVERTIME_APPROVED', false, 'Overtime Approved', 'Overtime Approved', false, false, true, 3,
(select r.id from "anv".reference r where r.code = 'OVERTIME_STATUS' order by r.id desc limit 1), false, true),
('OVERTIME_DRAFT', false, 'Overtime Draft', 'Overtime Draft', false, false, true, 4,
(select r.id from "anv".reference r where r.code = 'OVERTIME_STATUS' order by r.id desc limit 1), false, true);