delete from "0".model where formid = 'GROUP_GOAL_FORM';
insert into "0".model (formid, title, viewname, active) values ('GROUP_GOAL_FORM', 'Group Goal Form', 'GroupPersonalGoal', true);

delete from "0".customformsection where form_id = 'GROUP_GOAL_FORM';
insert into "0".customformsection (form_id, section, sorder, expanded) values ('GROUP_GOAL_FORM', 'GOAL_DETAILS', 0, true);
insert into "0".customformsection (form_id, section, sorder, expanded) values ('GROUP_GOAL_FORM', 'ASSIGNED_GOALS', 1, true);

delete from "0".modelfield where form_id = 'GROUP_GOAL_FORM';
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values ('GROUP_GOAL_FORM', 'EMPLOYEE', true, 'COL_1', 'GOAL_DETAILS', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values ('GROUP_GOAL_FORM', 'GOAL_START_DATE', true, 'COL_1', 'GOAL_DETAILS', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values ('GROUP_GOAL_FORM', 'APPROVERS', true, 'COL_2', 'GOAL_DETAILS', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values ('GROUP_GOAL_FORM', 'GOAL_TO_DATE', true, 'COL_2', 'GOAL_DETAILS', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values ('GROUP_GOAL_FORM', 'GOAL_VALIDITY_PERIOD', false, 'COL_3', 'GOAL_DETAILS', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values ('GROUP_GOAL_FORM', 'ASSIGNED_GOALS', false, 'COL_1', 'ASSIGNED_GOALS', 0);

delete from "anv".model where formid = 'GROUP_GOAL_FORM';
insert into "anv".model (formid, title, viewname, active) values ('GROUP_GOAL_FORM', 'Group Goal Form', 'GroupPersonalGoal', true);

delete from "anv".customformsection where form_id = 'GROUP_GOAL_FORM';
insert into "anv".customformsection (form_id, section, sorder, expanded) values ('GROUP_GOAL_FORM', 'GOAL_DETAILS', 0, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values ('GROUP_GOAL_FORM', 'ASSIGNED_GOALS', 1, true);

delete from "anv".modelfield where form_id = 'GROUP_GOAL_FORM';
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values ('GROUP_GOAL_FORM', 'EMPLOYEE', true, 'COL_1', 'GOAL_DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values ('GROUP_GOAL_FORM', 'GOAL_START_DATE', true, 'COL_1', 'GOAL_DETAILS', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values ('GROUP_GOAL_FORM', 'APPROVERS', true, 'COL_2', 'GOAL_DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values ('GROUP_GOAL_FORM', 'GOAL_TO_DATE', true, 'COL_2', 'GOAL_DETAILS', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values ('GROUP_GOAL_FORM', 'GOAL_VALIDITY_PERIOD', false, 'COL_3', 'GOAL_DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values ('GROUP_GOAL_FORM', 'ASSIGNED_GOALS', false, 'COL_1', 'ASSIGNED_GOALS', 0);

