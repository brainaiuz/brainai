delete from "anv".model where formid = 'POSITION_FORM';
insert into "anv".model (formid, title, viewname, active) values('POSITION_FORM', 'Position Form', 'Positions', true);

delete from "anv".customformsection where form_id = 'POSITION_FORM';
insert into "anv".customformsection (form_id, section, sorder, expanded) values('POSITION_FORM', 'POSITION_DETAILS', 0, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('POSITION_FORM', 'POSITION_EMPLOYEES', 1, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('POSITION_FORM', 'JOB_INFORMATION', 2, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('POSITION_FORM', 'BENEFIT_ALLOWANCE_INFORMATION', 3, true);

delete from "anv".modelfield where form_id = 'POSITION_FORM';
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('POSITION_FORM', 'POSITION_CODE', false, 'COL_1', 'POSITION_DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('POSITION_FORM', 'POSITION_TITLE', true, 'COL_1', 'POSITION_DETAILS', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('POSITION_FORM', 'DESCRIPTION', false, 'COL_1', 'POSITION_DETAILS', 2);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('POSITION_FORM', 'STATUS', false, 'COL_1', 'POSITION_DETAILS', 3);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('POSITION_FORM', 'ESTIBLISHED', false , 'COL_2', 'POSITION_DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('POSITION_FORM', 'AVAILABLE', true, 'COL_2', 'POSITION_DETAILS', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('POSITION_FORM', 'END_DATE', false, 'COL_2', 'POSITION_DETAILS', 2);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('POSITION_FORM', 'EMPLOYEES', false, 'COL_1', 'POSITION_EMPLOYEES', 0);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('POSITION_FORM', 'DEPARTMENT', true, 'COL_1', 'JOB_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('POSITION_FORM', 'JOB_FAMILY_PANEL', false, 'COL_1', 'JOB_INFORMATION', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('POSITION_FORM', 'REG_TEMP', true, 'COL_1', 'JOB_INFORMATION', 2);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('POSITION_FORM', 'FULL_PART_TIME', true, 'COL_1', 'JOB_INFORMATION', 3);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('POSITION_FORM', 'WAGE_RATE', false, 'COL_1', 'JOB_INFORMATION', 4);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('POSITION_FORM', 'CLIENT_CHARGE_RATE', false, 'COL_1', 'JOB_INFORMATION', 5);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('POSITION_FORM', 'MIN_SALARY', false, 'COL_1', 'JOB_INFORMATION', 6);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('POSITION_FORM', 'MID_SALARY', false, 'COL_1', 'JOB_INFORMATION', 7);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('POSITION_FORM', 'MAX_SALARY', false, 'COL_1', 'JOB_INFORMATION', 8);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('POSITION_FORM', 'BENEFIT_ALLOWANCE_PANEL', false, 'COL_1', 'BENEFIT_ALLOWANCE_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('POSITION_FORM', 'APPLY_BENEFIT_ALL_EMPLOYEE', true, 'COL_1', 'BENEFIT_ALLOWANCE_INFORMATION', 1);

update "anv".modelfield  set hide = true where form_id = 'POSITION_FORM' and field_id not in ('POSITION_CODE', 'POSITION_TITLE', 'DESCRIPTION', 'EMPLOYEES');
update "anv".modelfield set columntype = 'COL_2' where form_id = 'POSITION_FORM' and field_id = 'DESCRIPTION';
update "anv".modelfield set columntype = 'COL_1' where form_id = 'POSITION_FORM' and field_id != 'DESCRIPTION';

update "anv".modelfield  set mandatory = false where form_id = 'POSITION_FORM' and field_id not in ('POSITION_CODE', 'POSITION_TITLE');
update "anv".modelfield  set systemmandatory = false where form_id = 'POSITION_FORM' and field_id not in ('POSITION_CODE', 'POSITION_TITLE');