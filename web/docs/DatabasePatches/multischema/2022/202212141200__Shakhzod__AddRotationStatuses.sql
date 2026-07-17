insert into "anv".reference (code, deleted, name, description, isremovable, issystemreference, shared, sorder,
                             parentid, iscustombutton, isactive)
values ('ROTATION_STATUS', false, 'Rotation', 'Rotation', false, false, true, 0, null, false, true);



insert into "anv".reference (code, deleted, name, description, isremovable, issystemreference, shared, sorder,
                             parentid, iscustombutton, isactive)
values ('ROTATION_REJECTED', false, 'Rotation Rejected', 'Rotation Rejected', false, false, true, 1,
        (select r.id from "anv".reference r where r.code = 'ROTATION_STATUS' order by r.id desc limit 1), false, true),
('ROTATION_SUBMITTED', false, 'Rotation Submitted', 'Rotation Submitted', false, false, true, 2,
(select r.id from "anv".reference r where r.code = 'ROTATION_STATUS' order by r.id desc limit 1), false, true),
('ROTATION_APPROVED', false, 'Rotation Approved', 'Rotation Approved', false, false, true, 3,
(select r.id from "anv".reference r where r.code = 'ROTATION_STATUS' order by r.id desc limit 1), false, true),
('ROTATION_DRAFT', false, 'Rotation Draft', 'Rotation Draft', false, false, true, 4,
(select r.id from "anv".reference r where r.code = 'ROTATION_STATUS' order by r.id desc limit 1), false, true);