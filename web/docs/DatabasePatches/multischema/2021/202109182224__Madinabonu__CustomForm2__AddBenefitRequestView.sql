
-- //****** EdsModel
delete from "anv".model where formid='BENEFIT_REQUEST_FORM';
insert into "anv".model (formid, title, viewname, active) values('BENEFIT_REQUEST_FORM', 'Benefit Request List', 'BenefitRequestList', true);


-- //****** EdsCustomFormSection
delete from "anv".customformsection where form_id='BENEFIT_REQUEST_FORM';
insert into "anv".customformsection (form_id, section, sorder, expanded) values('BENEFIT_REQUEST_FORM', 'INFORMATION', 0, true);


-- //****** EdsModelField
delete from "anv".modelfield where form_id='BENEFIT_REQUEST_FORM';
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder, hide) values('BENEFIT_REQUEST_FORM', 'REQUESTER', true, 'COL_1', 'INFORMATION', 0, false);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder, hide) values('BENEFIT_REQUEST_FORM', 'DATE_PERIOD', true, 'COL_1', 'INFORMATION', 1, false);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder, hide) values('BENEFIT_REQUEST_FORM', 'DESCRIPTION', false, 'COL_1', 'INFORMATION', 2, false);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder, hide) values('BENEFIT_REQUEST_FORM', 'LEFT_QUANTITY', true, 'COL_2', 'INFORMATION', 3, true);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder, hide) values('BENEFIT_REQUEST_FORM', 'APPROVER', true, 'COL_2', 'INFORMATION', 0, false);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder, hide) values('BENEFIT_REQUEST_FORM', 'BENEFIT_TYPE', true, 'COL_2', 'INFORMATION', 1,false);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder, hide) values('BENEFIT_REQUEST_FORM', 'REQUESTED_QUANTITY', true, 'COL_2', 'INFORMATION', 2, false);


