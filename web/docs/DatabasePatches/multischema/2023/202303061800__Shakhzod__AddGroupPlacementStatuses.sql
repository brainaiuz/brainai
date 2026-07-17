insert into "anv".reference (code, deleted, name, description, isremovable, issystemreference, shared, sorder,
                             parentid, iscustombutton, isactive)
values ('GROUP_PLACEMENT_STATUS', false, 'Group Placement', 'Group Placement', false, false, true, 0, null, false, true);



insert into "anv".reference (code, deleted, name, description, isremovable, issystemreference, shared, sorder,
                             parentid, iscustombutton, isactive)
values ('GROUP_PLACEMENT_REJECTED', false, 'Group Placement Rejected', 'Group Placement', false, false, true, 1,
        (select r.id from "anv".reference r where r.code = 'GROUP_PLACEMENT_STATUS' order by r.id desc limit 1), false, true),
('GROUP_PLACEMENT_SUBMITTED', false, 'Group Placement Submitted', 'Group Placement Submitted', false, false, true, 2,
(select r.id from "anv".reference r where r.code = 'GROUP_PLACEMENT_STATUS' order by r.id desc limit 1), false, true),
('GROUP_PLACEMENT_APPROVED', false, 'Group Placement Approved', 'Group Placement Approved', false, false, true, 3,
(select r.id from "anv".reference r where r.code = 'GROUP_PLACEMENT_STATUS' order by r.id desc limit 1), false, true),
('GROUP_PLACEMENT_DRAFT', false, 'Group Placement Draft', 'Group Placement Draft', false, false, true, 4,
(select r.id from "anv".reference r where r.code = 'GROUP_PLACEMENT_STATUS' order by r.id desc limit 1), false, true);