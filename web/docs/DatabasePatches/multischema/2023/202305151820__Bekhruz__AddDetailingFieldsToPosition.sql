insert into "anv".customformsection (form_id, section, sorder, expanded)
values ('POSITION_FORM', 'DETAILED_INFORMATION', 2, true);

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('POSITION_FORM', 'positionResponsibilities', false, false, 'COL_1', 'DETAILED_INFORMATION', 0);

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('POSITION_FORM', 'positionDescription', false, false, 'COL_2', 'DETAILED_INFORMATION', 0);

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('POSITION_FORM', 'jobRequirement', false, false, 'COL_3', 'DETAILED_INFORMATION', 0);

