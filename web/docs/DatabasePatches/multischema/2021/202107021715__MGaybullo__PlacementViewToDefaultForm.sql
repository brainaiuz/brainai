
delete from "anv".customformsection where form_id='PLACEMENT_FORM';
insert into "anv".customformsection (form_id, section, sorder, expanded) values('PLACEMENT_FORM', 'PLACEMENT_BASIC_INFORMATION', 0, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('PLACEMENT_FORM', 'PLACEMENT_FILES', 1, true);


delete from "anv".modelfield where form_id='PLACEMENT_FORM';
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('PLACEMENT_FORM', 'placementcandidate', true, false, 'COL_1', 'PLACEMENT_BASIC_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('PLACEMENT_FORM', 'placementDateOffer', true, false, 'COL_1', 'PLACEMENT_BASIC_INFORMATION', 1);


insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('PLACEMENT_FORM', 'placementDepartment', true, false, 'COL_2', 'PLACEMENT_BASIC_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('PLACEMENT_FORM', 'placementPosition', false, false, 'COL_2', 'PLACEMENT_BASIC_INFORMATION', 1);


insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('PLACEMENT_FORM', 'placementLocation', false, false, 'COL_3', 'PLACEMENT_BASIC_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('PLACEMENT_FORM', 'placementVacancies', false, false, 'COL_3', 'PLACEMENT_BASIC_INFORMATION', 1);


insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('PLACEMENT_FORM', 'PLACEMENT_NOTES', false, false, 'COL_1', 'PLACEMENT_FILES', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('PLACEMENT_FORM', 'PLACEMENT_FILES', false, false, 'COL_2', 'PLACEMENT_FILES', 0);



insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('PLACEMENT_FORM', 'placementProject', false, true, 'COL_1', 'PLACEMENT_BASIC_INFORMATION', 1);


