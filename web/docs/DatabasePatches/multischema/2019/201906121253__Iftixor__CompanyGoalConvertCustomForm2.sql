delete from "0".model where formid='COMPANY_GOAL_FORM';
insert into "0".model (formid, title, viewname, active) values('COMPANY_GOAL_FORM', 'Company Goal Form', 'CompanyGoal', true);

delete from "0".customformsection where form_id='COMPANY_GOAL_FORM';
insert into "0".customformsection (form_id, section, sorder, expanded) values('COMPANY_GOAL_FORM', 'GOAL_DETAILS', 0, true);
insert into "0".customformsection (form_id, section, sorder, expanded) values('COMPANY_GOAL_FORM', 'ATTACHMENTS_TITLE', 1, false);
insert into "0".customformsection (form_id, section, sorder, expanded) values('COMPANY_GOAL_FORM', 'NOTES', 2, false);
insert into "0".customformsection (form_id, section, sorder, expanded) values('COMPANY_GOAL_FORM', 'LINKS2', 3, false);
insert into "0".customformsection (form_id, section, sorder, expanded) values('COMPANY_GOAL_FORM', 'ADDITIONAL_INFORMATION', 4, false);

delete from "0".modelfield where form_id='COMPANY_GOAL_FORM';
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('COMPANY_GOAL_FORM', 'GOAL_TITLE', true, 'COL_1', 'GOAL_DETAILS', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('COMPANY_GOAL_FORM', 'GOAL_DESCRIPTION', false, 'COL_1', 'GOAL_DETAILS', 1);

insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('COMPANY_GOAL_FORM', 'GOAL_START_DATE', true, 'COL_2', 'GOAL_DETAILS', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('COMPANY_GOAL_FORM', 'GOAL_OUTCOME', false, 'COL_2', 'GOAL_DETAILS', 1);

insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('COMPANY_GOAL_FORM', 'GOAL_STATUS', true, 'COL_3', 'GOAL_DETAILS', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('COMPANY_GOAL_FORM', 'GOAL_VALIDITY_PERIOD', false, 'COL_3', 'GOAL_DETAILS', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('COMPANY_GOAL_FORM', 'ATTACHMENTS', false, 'COL_1', 'ATTACHMENTS_TITLE', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('COMPANY_GOAL_FORM', 'CRM_NOTE', false, 'COL_1', 'NOTES', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('COMPANY_GOAL_FORM', 'LINKS', false, 'COL_1', 'LINKS2', 0);

delete from "anv".model where formid='COMPANY_GOAL_FORM';
insert into "anv".model (formid, title, viewname, active) values('COMPANY_GOAL_FORM', 'Company Goal Form', 'CompanyGoal', true);
delete from "anv".customformsection where form_id='COMPANY_GOAL_FORM';
insert into "anv".customformsection (form_id, section, sorder, expanded) values('COMPANY_GOAL_FORM', 'GOAL_DETAILS', 0, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('COMPANY_GOAL_FORM', 'ATTACHMENTS_TITLE', 1, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('COMPANY_GOAL_FORM', 'NOTES', 2, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('COMPANY_GOAL_FORM', 'LINKS2', 3, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('COMPANY_GOAL_FORM', 'ADDITIONAL_INFORMATION', 4, false);

delete from "anv".modelfield where form_id='COMPANY_GOAL_FORM';
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('COMPANY_GOAL_FORM', 'GOAL_TITLE', true, 'COL_1', 'GOAL_DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('COMPANY_GOAL_FORM', 'GOAL_DESCRIPTION', false, 'COL_1', 'GOAL_DETAILS', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('COMPANY_GOAL_FORM', 'GOAL_START_DATE', true, 'COL_2', 'GOAL_DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('COMPANY_GOAL_FORM', 'GOAL_OUTCOME', false, 'COL_2', 'GOAL_DETAILS', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('COMPANY_GOAL_FORM', 'GOAL_STATUS', true, 'COL_3', 'GOAL_DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('COMPANY_GOAL_FORM', 'GOAL_VALIDITY_PERIOD', false, 'COL_3', 'GOAL_DETAILS', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('COMPANY_GOAL_FORM', 'ATTACHMENTS', false, 'COL_1', 'ATTACHMENTS_TITLE', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('COMPANY_GOAL_FORM', 'CRM_NOTE', false, 'COL_1', 'NOTES', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('COMPANY_GOAL_FORM', 'LINKS', false, 'COL_1', 'LINKS2', 0);


delete from "0_template".model where formid='COMPANY_GOAL_FORM';
insert into "0_template".model (formid, title, viewname, active) values('COMPANY_GOAL_FORM', 'Company Goal Form', 'CompanyGoal', true);

delete from "0_template".customformsection where form_id='COMPANY_GOAL_FORM';
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('COMPANY_GOAL_FORM', 'GOAL_DETAILS', 0, true);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('COMPANY_GOAL_FORM', 'ATTACHMENTS_TITLE', 1, false);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('COMPANY_GOAL_FORM', 'NOTES', 2, false);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('COMPANY_GOAL_FORM', 'LINKS2', 3, false);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('COMPANY_GOAL_FORM', 'ADDITIONAL_INFORMATION', 4, false);

delete from "0_template".modelfield where form_id='COMPANY_GOAL_FORM';
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('COMPANY_GOAL_FORM', 'GOAL_TITLE', true, 'COL_1', 'GOAL_DETAILS', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('COMPANY_GOAL_FORM', 'GOAL_DESCRIPTION', false, 'COL_1', 'GOAL_DETAILS', 1);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('COMPANY_GOAL_FORM', 'GOAL_START_DATE', true, 'COL_2', 'GOAL_DETAILS', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('COMPANY_GOAL_FORM', 'GOAL_OUTCOME', false, 'COL_2', 'GOAL_DETAILS', 1);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('COMPANY_GOAL_FORM', 'GOAL_STATUS', true, 'COL_3', 'GOAL_DETAILS', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('COMPANY_GOAL_FORM', 'GOAL_VALIDITY_PERIOD', false, 'COL_3', 'GOAL_DETAILS', 1);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('COMPANY_GOAL_FORM', 'ATTACHMENTS', false, 'COL_1', 'ATTACHMENTS_TITLE', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('COMPANY_GOAL_FORM', 'CRM_NOTE', false, 'COL_1', 'NOTES', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('COMPANY_GOAL_FORM', 'LINKS', false, 'COL_1', 'LINKS2', 0);
