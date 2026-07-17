delete from "0".model where formid='PERSONAL_GOAL_FORM';
insert into "0".model (formid, title, viewname, active) values('PERSONAL_GOAL_FORM', 'Personal Goal Form', 'PersonalGoal', true);

delete from "0".customformsection where form_id='PERSONAL_GOAL_FORM';
insert into "0".customformsection (form_id, section, sorder, expanded) values('PERSONAL_GOAL_FORM', 'GOAL_DETAILS', 0, true);
insert into "0".customformsection (form_id, section, sorder, expanded) values('PERSONAL_GOAL_FORM', 'ATTACHMENTS_TITLE', 1, false);
insert into "0".customformsection (form_id, section, sorder, expanded) values('PERSONAL_GOAL_FORM', 'NOTES', 2, false);
insert into "0".customformsection (form_id, section, sorder, expanded) values('PERSONAL_GOAL_FORM', 'LINKS2', 3, false);
insert into "0".customformsection (form_id, section, sorder, expanded) values('PERSONAL_GOAL_FORM', 'ADDITIONAL_INFORMATION', 4, false);

delete from "0".modelfield where form_id='PERSONAL_GOAL_FORM';
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_PERSONAL_ASSINESS', true, 'COL_1', 'GOAL_DETAILS', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_TITLE', true, 'COL_1', 'GOAL_DETAILS', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_START_DATE', true, 'COL_1', 'GOAL_DETAILS', 2);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_DESCRIPTION', false, 'COL_1', 'GOAL_DETAILS', 3);

insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_STATUS', true, 'COL_2', 'GOAL_DETAILS', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_TARGET', false, 'COL_2', 'GOAL_DETAILS', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_ACTUAL', false, 'COL_2', 'GOAL_DETAILS', 2);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_ACTION_STEPS', false, 'COL_2', 'GOAL_DETAILS', 3);

insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_VALIDITY_PERIOD', false, 'COL_3', 'GOAL_DETAILS', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_MEASUREMENT_UNIT', false, 'COL_3', 'GOAL_DETAILS', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_WEIGHT', false, 'COL_3', 'GOAL_DETAILS', 2);

insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'ATTACHMENTS', false, 'COL_1', 'ATTACHMENTS_TITLE', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'CRM_NOTE', false, 'COL_1', 'NOTES', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'LINKS', false, 'COL_1', 'LINKS2', 0);

delete from "0".model where formid='DEPARTMENT_GOAL_FORM';
insert into "0".model (formid, title, viewname, active) values('DEPARTMENT_GOAL_FORM', 'Department Goal Form', 'DepartmentGoal', true);
delete from "0".customformsection where form_id='DEPARTMENT_GOAL_FORM';
insert into "0".customformsection (form_id, section, sorder, expanded) values('DEPARTMENT_GOAL_FORM', 'GOAL_DETAILS', 0, true);
insert into "0".customformsection (form_id, section, sorder, expanded) values('DEPARTMENT_GOAL_FORM', 'ASSIGNEES', 1, false);
insert into "0".customformsection (form_id, section, sorder, expanded) values('DEPARTMENT_GOAL_FORM', 'ATTACHMENTS_TITLE', 2, false);
insert into "0".customformsection (form_id, section, sorder, expanded) values('DEPARTMENT_GOAL_FORM', 'NOTES', 3, false);
insert into "0".customformsection (form_id, section, sorder, expanded) values('DEPARTMENT_GOAL_FORM', 'LINKS2', 4, false);
insert into "0".customformsection (form_id, section, sorder, expanded) values('DEPARTMENT_GOAL_FORM', 'ADDITIONAL_INFORMATION', 5, false);

delete from "0".modelfield where form_id='DEPARTMENT_GOAL_FORM';
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_TITLE', true, 'COL_1', 'GOAL_DETAILS', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_PROORDEP', true, 'COL_1', 'GOAL_DETAILS', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_START_DATE', true, 'COL_1', 'GOAL_DETAILS', 2);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_DESCRIPTION', false, 'COL_1', 'GOAL_DETAILS', 3);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_STATUS', true, 'COL_2', 'GOAL_DETAILS', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_SCORE_CALCULATION', false, 'COL_2', 'GOAL_DETAILS', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'COMPANY_GOAL', false, 'COL_2', 'GOAL_DETAILS', 2);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_ACTION_STEPS', false, 'COL_2', 'GOAL_DETAILS', 3);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_VALIDITY_PERIOD', false, 'COL_3', 'GOAL_DETAILS', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_MEASUREMENT_UNIT', false, 'COL_3', 'GOAL_DETAILS', 1);

insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_ASSIGNEES', false, 'COL_1', 'ASSIGNEES', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_RESOLVER', false, 'COL_1', 'ASSIGNEES', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'ATTACHMENTS', false, 'COL_1', 'ATTACHMENTS_TITLE', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'CRM_NOTE', false, 'COL_1', 'NOTES', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'LINKS', false, 'COL_1', 'LINKS2', 0);

delete from "0".model where formid='PROJECT_GOAL_FORM';
insert into "0".model (formid, title, viewname, active) values('PROJECT_GOAL_FORM', 'Project Goal Form', 'ProjectGoal', true);
delete from "0".customformsection where form_id='PROJECT_GOAL_FORM';
insert into "0".customformsection (form_id, section, sorder, expanded) values('PROJECT_GOAL_FORM', 'GOAL_DETAILS', 0, true);
insert into "0".customformsection (form_id, section, sorder, expanded) values('PROJECT_GOAL_FORM', 'ASSIGNEES', 1, false);
insert into "0".customformsection (form_id, section, sorder, expanded) values('PROJECT_GOAL_FORM', 'ATTACHMENTS_TITLE', 2, false);
insert into "0".customformsection (form_id, section, sorder, expanded) values('PROJECT_GOAL_FORM', 'NOTES', 3, false);
insert into "0".customformsection (form_id, section, sorder, expanded) values('PROJECT_GOAL_FORM', 'LINKS2', 4, false);
insert into "0".customformsection (form_id, section, sorder, expanded) values('PROJECT_GOAL_FORM', 'ADDITIONAL_INFORMATION', 5, false);
delete from "0".modelfield where form_id='PROJECT_GOAL_FORM';
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_TITLE', true, 'COL_1', 'GOAL_DETAILS', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_PROORDEP', true, 'COL_1', 'GOAL_DETAILS', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_START_DATE', true, 'COL_1', 'GOAL_DETAILS', 2);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_DESCRIPTION', false, 'COL_1', 'GOAL_DETAILS', 3);

insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_STATUS', true, 'COL_2', 'GOAL_DETAILS', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_SCORE_CALCULATION', false, 'COL_2', 'GOAL_DETAILS', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'COMPANY_GOAL', false, 'COL_2', 'GOAL_DETAILS', 2);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_ACTION_STEPS', false, 'COL_2', 'GOAL_DETAILS', 3);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_VALIDITY_PERIOD', false, 'COL_3', 'GOAL_DETAILS', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_MEASUREMENT_UNIT', false, 'COL_3', 'GOAL_DETAILS', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_ASSIGNEES', false, 'COL_1', 'ASSIGNEES', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_RESOLVER', false, 'COL_1', 'ASSIGNEES', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'ATTACHMENTS', false, 'COL_1', 'ATTACHMENTS_TITLE', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'CRM_NOTE', false, 'COL_1', 'NOTES', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'LINKS', false, 'COL_1', 'LINKS2', 0);

delete from "0".model where formid='BUSINESS_GOAL_FORM';
insert into "0".model (formid, title, viewname, active) values('BUSINESS_GOAL_FORM', 'Businees Goal Form', 'BusinessGoal', true);
delete from "0".customformsection where form_id='BUSINESS_GOAL_FORM';
insert into "0".customformsection (form_id, section, sorder, expanded) values('BUSINESS_GOAL_FORM', 'GOAL_DETAILS', 0, true);
insert into "0".customformsection (form_id, section, sorder, expanded) values('BUSINESS_GOAL_FORM', 'ASSIGNEES', 1, false);
insert into "0".customformsection (form_id, section, sorder, expanded) values('BUSINESS_GOAL_FORM', 'ATTACHMENTS_TITLE', 2, false);
insert into "0".customformsection (form_id, section, sorder, expanded) values('BUSINESS_GOAL_FORM', 'NOTES', 3, false);
insert into "0".customformsection (form_id, section, sorder, expanded) values('BUSINESS_GOAL_FORM', 'LINKS2', 4, false);
insert into "0".customformsection (form_id, section, sorder, expanded) values('BUSINESS_GOAL_FORM', 'ADDITIONAL_INFORMATION', 5, false);
delete from "0".modelfield where form_id='BUSINESS_GOAL_FORM';
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'GOAL_TITLE', true, 'COL_1', 'GOAL_DETAILS', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'GOAL_START_DATE', true, 'COL_1', 'GOAL_DETAILS', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'GOAL_DESCRIPTION', false, 'COL_1', 'GOAL_DETAILS', 2);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'GOAL_STATUS', true, 'COL_2', 'GOAL_DETAILS', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'COMPANY_GOAL', false, 'COL_2', 'GOAL_DETAILS', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'GOAL_ACTION_STEPS', false, 'COL_2', 'GOAL_DETAILS', 2);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'GOAL_VALIDITY_PERIOD', false, 'COL_3', 'GOAL_DETAILS', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'GOAL_MEASUREMENT_UNIT', false, 'COL_3', 'GOAL_DETAILS', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'GOAL_SCORE_CALCULATION', false, 'COL_3', 'GOAL_DETAILS', 2);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'GOAL_ASSIGNEES', false, 'COL_1', 'ASSIGNEES', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'GOAL_RESOLVER', false, 'COL_1', 'ASSIGNEES', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'ATTACHMENTS', false, 'COL_1', 'ATTACHMENTS_TITLE', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'CRM_NOTE', false, 'COL_1', 'NOTES', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'LINKS', false, 'COL_1', 'LINKS2', 0);

delete from "anv".model where formid='PERSONAL_GOAL_FORM';
insert into "anv".model (formid, title, viewname, active) values('PERSONAL_GOAL_FORM', 'Personal Goal Form', 'PersonalGoal', true);

delete from "anv".customformsection where form_id='PERSONAL_GOAL_FORM';
insert into "anv".customformsection (form_id, section, sorder, expanded) values('PERSONAL_GOAL_FORM', 'GOAL_DETAILS', 0, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('PERSONAL_GOAL_FORM', 'ATTACHMENTS_TITLE', 1, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('PERSONAL_GOAL_FORM', 'NOTES', 2, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('PERSONAL_GOAL_FORM', 'LINKS2', 3, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('PERSONAL_GOAL_FORM', 'ADDITIONAL_INFORMATION', 4, false);

delete from "anv".modelfield where form_id='PERSONAL_GOAL_FORM';
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_PERSONAL_ASSINESS', true, 'COL_1', 'GOAL_DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_TITLE', true, 'COL_1', 'GOAL_DETAILS', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_START_DATE', true, 'COL_1', 'GOAL_DETAILS', 2);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_DESCRIPTION', false, 'COL_1', 'GOAL_DETAILS', 3);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_STATUS', true, 'COL_2', 'GOAL_DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_TARGET', false, 'COL_2', 'GOAL_DETAILS', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_ACTUAL', false, 'COL_2', 'GOAL_DETAILS', 2);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_ACTION_STEPS', false, 'COL_2', 'GOAL_DETAILS', 3);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_VALIDITY_PERIOD', false, 'COL_3', 'GOAL_DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_MEASUREMENT_UNIT', false, 'COL_3', 'GOAL_DETAILS', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_WEIGHT', false, 'COL_3', 'GOAL_DETAILS', 2);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'ATTACHMENTS', false, 'COL_1', 'ATTACHMENTS_TITLE', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'CRM_NOTE', false, 'COL_1', 'NOTES', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'LINKS', false, 'COL_1', 'LINKS2', 0);

delete from "anv".model where formid='DEPARTMENT_GOAL_FORM';
insert into "anv".model (formid, title, viewname, active) values('DEPARTMENT_GOAL_FORM', 'Department Goal Form', 'DepartmentGoal', true);
delete from "anv".customformsection where form_id='DEPARTMENT_GOAL_FORM';
insert into "anv".customformsection (form_id, section, sorder, expanded) values('DEPARTMENT_GOAL_FORM', 'GOAL_DETAILS', 0, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('DEPARTMENT_GOAL_FORM', 'ASSIGNEES', 1, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('DEPARTMENT_GOAL_FORM', 'ATTACHMENTS_TITLE', 2, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('DEPARTMENT_GOAL_FORM', 'NOTES', 3, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('DEPARTMENT_GOAL_FORM', 'LINKS2', 4, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('DEPARTMENT_GOAL_FORM', 'ADDITIONAL_INFORMATION', 5, false);

delete from "anv".modelfield where form_id='DEPARTMENT_GOAL_FORM';
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_TITLE', true, 'COL_1', 'GOAL_DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_PROORDEP', true, 'COL_1', 'GOAL_DETAILS', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_START_DATE', true, 'COL_1', 'GOAL_DETAILS', 2);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_DESCRIPTION', false, 'COL_1', 'GOAL_DETAILS', 3);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_STATUS', true, 'COL_2', 'GOAL_DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_SCORE_CALCULATION', false, 'COL_2', 'GOAL_DETAILS', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'COMPANY_GOAL', false, 'COL_2', 'GOAL_DETAILS', 2);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_ACTION_STEPS', false, 'COL_2', 'GOAL_DETAILS', 3);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_VALIDITY_PERIOD', false, 'COL_3', 'GOAL_DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_MEASUREMENT_UNIT', false, 'COL_3', 'GOAL_DETAILS', 1);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_ASSIGNEES', false, 'COL_1', 'ASSIGNEES', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_RESOLVER', false, 'COL_1', 'ASSIGNEES', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'ATTACHMENTS', false, 'COL_1', 'ATTACHMENTS_TITLE', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'CRM_NOTE', false, 'COL_1', 'NOTES', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'LINKS', false, 'COL_1', 'LINKS2', 0);

delete from "anv".model where formid='PROJECT_GOAL_FORM';
insert into "anv".model (formid, title, viewname, active) values('PROJECT_GOAL_FORM', 'Project Goal Form', 'ProjectGoal', true);
delete from "anv".customformsection where form_id='PROJECT_GOAL_FORM';
insert into "anv".customformsection (form_id, section, sorder, expanded) values('PROJECT_GOAL_FORM', 'GOAL_DETAILS', 0, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('PROJECT_GOAL_FORM', 'ASSIGNEES', 1, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('PROJECT_GOAL_FORM', 'ATTACHMENTS_TITLE', 2, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('PROJECT_GOAL_FORM', 'NOTES', 3, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('PROJECT_GOAL_FORM', 'LINKS2', 4, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('PROJECT_GOAL_FORM', 'ADDITIONAL_INFORMATION', 5, false);
delete from "anv".modelfield where form_id='PROJECT_GOAL_FORM';
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_TITLE', true, 'COL_1', 'GOAL_DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_PROORDEP', true, 'COL_1', 'GOAL_DETAILS', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_START_DATE', true, 'COL_1', 'GOAL_DETAILS', 2);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_DESCRIPTION', false, 'COL_1', 'GOAL_DETAILS', 3);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_STATUS', true, 'COL_2', 'GOAL_DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_SCORE_CALCULATION', false, 'COL_2', 'GOAL_DETAILS', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'COMPANY_GOAL', false, 'COL_2', 'GOAL_DETAILS', 2);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_ACTION_STEPS', false, 'COL_2', 'GOAL_DETAILS', 3);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_VALIDITY_PERIOD', false, 'COL_3', 'GOAL_DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_MEASUREMENT_UNIT', false, 'COL_3', 'GOAL_DETAILS', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_ASSIGNEES', false, 'COL_1', 'ASSIGNEES', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_RESOLVER', false, 'COL_1', 'ASSIGNEES', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'ATTACHMENTS', false, 'COL_1', 'ATTACHMENTS_TITLE', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'CRM_NOTE', false, 'COL_1', 'NOTES', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'LINKS', false, 'COL_1', 'LINKS2', 0);

delete from "anv".model where formid='BUSINESS_GOAL_FORM';
insert into "anv".model (formid, title, viewname, active) values('BUSINESS_GOAL_FORM', 'Businees Goal Form', 'BusinessGoal', true);
delete from "anv".customformsection where form_id='BUSINESS_GOAL_FORM';
insert into "anv".customformsection (form_id, section, sorder, expanded) values('BUSINESS_GOAL_FORM', 'GOAL_DETAILS', 0, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('BUSINESS_GOAL_FORM', 'ASSIGNEES', 1, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('BUSINESS_GOAL_FORM', 'ATTACHMENTS_TITLE', 2, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('BUSINESS_GOAL_FORM', 'NOTES', 3, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('BUSINESS_GOAL_FORM', 'LINKS2', 4, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('BUSINESS_GOAL_FORM', 'ADDITIONAL_INFORMATION', 5, false);
delete from "anv".modelfield where form_id='BUSINESS_GOAL_FORM';
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'GOAL_TITLE', true, 'COL_1', 'GOAL_DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'GOAL_START_DATE', true, 'COL_1', 'GOAL_DETAILS', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'GOAL_DESCRIPTION', false, 'COL_1', 'GOAL_DETAILS', 2);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'GOAL_STATUS', true, 'COL_2', 'GOAL_DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'COMPANY_GOAL', false, 'COL_2', 'GOAL_DETAILS', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'GOAL_ACTION_STEPS', false, 'COL_2', 'GOAL_DETAILS', 2);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'GOAL_VALIDITY_PERIOD', false, 'COL_3', 'GOAL_DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'GOAL_MEASUREMENT_UNIT', false, 'COL_3', 'GOAL_DETAILS', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'GOAL_SCORE_CALCULATION', false, 'COL_3', 'GOAL_DETAILS', 2);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'GOAL_ASSIGNEES', false, 'COL_1', 'ASSIGNEES', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'GOAL_RESOLVER', false, 'COL_1', 'ASSIGNEES', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'ATTACHMENTS', false, 'COL_1', 'ATTACHMENTS_TITLE', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'CRM_NOTE', false, 'COL_1', 'NOTES', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'LINKS', false, 'COL_1', 'LINKS2', 0);


delete from "0_template".model where formid='PERSONAL_GOAL_FORM';
insert into "0_template".model (formid, title, viewname, active) values('PERSONAL_GOAL_FORM', 'Personal Goal Form', 'PersonalGoal', true);

delete from "0_template".customformsection where form_id='PERSONAL_GOAL_FORM';
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('PERSONAL_GOAL_FORM', 'GOAL_DETAILS', 0, true);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('PERSONAL_GOAL_FORM', 'ATTACHMENTS_TITLE', 1, false);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('PERSONAL_GOAL_FORM', 'NOTES', 2, false);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('PERSONAL_GOAL_FORM', 'LINKS2', 3, false);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('PERSONAL_GOAL_FORM', 'ADDITIONAL_INFORMATION', 4, false);

delete from "0_template".modelfield where form_id='PERSONAL_GOAL_FORM';
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_PERSONAL_ASSINESS', true, 'COL_1', 'GOAL_DETAILS', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_TITLE', true, 'COL_1', 'GOAL_DETAILS', 1);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_START_DATE', true, 'COL_1', 'GOAL_DETAILS', 2);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_DESCRIPTION', false, 'COL_1', 'GOAL_DETAILS', 3);

insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_STATUS', true, 'COL_2', 'GOAL_DETAILS', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_TARGET', false, 'COL_2', 'GOAL_DETAILS', 1);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_ACTUAL', false, 'COL_2', 'GOAL_DETAILS', 2);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_ACTION_STEPS', false, 'COL_2', 'GOAL_DETAILS', 3);

insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_VALIDITY_PERIOD', false, 'COL_3', 'GOAL_DETAILS', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_MEASUREMENT_UNIT', false, 'COL_3', 'GOAL_DETAILS', 1);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'GOAL_WEIGHT', false, 'COL_3', 'GOAL_DETAILS', 2);

insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'ATTACHMENTS', false, 'COL_1', 'ATTACHMENTS_TITLE', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'CRM_NOTE', false, 'COL_1', 'NOTES', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PERSONAL_GOAL_FORM', 'LINKS', false, 'COL_1', 'LINKS2', 0);

delete from "0_template".model where formid='DEPARTMENT_GOAL_FORM';
insert into "0_template".model (formid, title, viewname, active) values('DEPARTMENT_GOAL_FORM', 'Department Goal Form', 'DepartmentGoal', true);
delete from "0_template".customformsection where form_id='DEPARTMENT_GOAL_FORM';
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('DEPARTMENT_GOAL_FORM', 'GOAL_DETAILS', 0, true);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('DEPARTMENT_GOAL_FORM', 'ASSIGNEES', 1, false);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('DEPARTMENT_GOAL_FORM', 'ATTACHMENTS_TITLE', 2, false);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('DEPARTMENT_GOAL_FORM', 'NOTES', 3, false);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('DEPARTMENT_GOAL_FORM', 'LINKS2', 4, false);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('DEPARTMENT_GOAL_FORM', 'ADDITIONAL_INFORMATION', 5, false);

delete from "0_template".modelfield where form_id='DEPARTMENT_GOAL_FORM';
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_TITLE', true, 'COL_1', 'GOAL_DETAILS', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_PROORDEP', true, 'COL_1', 'GOAL_DETAILS', 1);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_START_DATE', true, 'COL_1', 'GOAL_DETAILS', 2);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_DESCRIPTION', false, 'COL_1', 'GOAL_DETAILS', 3);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_STATUS', true, 'COL_2', 'GOAL_DETAILS', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_SCORE_CALCULATION', false, 'COL_2', 'GOAL_DETAILS', 1);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'COMPANY_GOAL', false, 'COL_2', 'GOAL_DETAILS', 2);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_ACTION_STEPS', false, 'COL_2', 'GOAL_DETAILS', 3);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_VALIDITY_PERIOD', false, 'COL_3', 'GOAL_DETAILS', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_MEASUREMENT_UNIT', false, 'COL_3', 'GOAL_DETAILS', 1);

insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_ASSIGNEES', false, 'COL_1', 'ASSIGNEES', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'GOAL_RESOLVER', false, 'COL_1', 'ASSIGNEES', 1);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'ATTACHMENTS', false, 'COL_1', 'ATTACHMENTS_TITLE', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'CRM_NOTE', false, 'COL_1', 'NOTES', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_GOAL_FORM', 'LINKS', false, 'COL_1', 'LINKS2', 0);

delete from "0_template".model where formid='PROJECT_GOAL_FORM';
insert into "0_template".model (formid, title, viewname, active) values('PROJECT_GOAL_FORM', 'Project Goal Form', 'ProjectGoal', true);
delete from "0_template".customformsection where form_id='PROJECT_GOAL_FORM';
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('PROJECT_GOAL_FORM', 'GOAL_DETAILS', 0, true);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('PROJECT_GOAL_FORM', 'ASSIGNEES', 1, false);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('PROJECT_GOAL_FORM', 'ATTACHMENTS_TITLE', 2, false);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('PROJECT_GOAL_FORM', 'NOTES', 3, false);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('PROJECT_GOAL_FORM', 'LINKS2', 4, false);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('PROJECT_GOAL_FORM', 'ADDITIONAL_INFORMATION', 5, false);
delete from "0_template".modelfield where form_id='PROJECT_GOAL_FORM';
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_TITLE', true, 'COL_1', 'GOAL_DETAILS', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_PROORDEP', true, 'COL_1', 'GOAL_DETAILS', 1);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_START_DATE', true, 'COL_1', 'GOAL_DETAILS', 2);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_DESCRIPTION', false, 'COL_1', 'GOAL_DETAILS', 3);

insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_STATUS', true, 'COL_2', 'GOAL_DETAILS', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_SCORE_CALCULATION', false, 'COL_2', 'GOAL_DETAILS', 1);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'COMPANY_GOAL', false, 'COL_2', 'GOAL_DETAILS', 2);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_ACTION_STEPS', false, 'COL_2', 'GOAL_DETAILS', 3);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_VALIDITY_PERIOD', false, 'COL_3', 'GOAL_DETAILS', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_MEASUREMENT_UNIT', false, 'COL_3', 'GOAL_DETAILS', 1);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_ASSIGNEES', false, 'COL_1', 'ASSIGNEES', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'GOAL_RESOLVER', false, 'COL_1', 'ASSIGNEES', 1);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'ATTACHMENTS', false, 'COL_1', 'ATTACHMENTS_TITLE', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'CRM_NOTE', false, 'COL_1', 'NOTES', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('PROJECT_GOAL_FORM', 'LINKS', false, 'COL_1', 'LINKS2', 0);

delete from "0_template".model where formid='BUSINESS_GOAL_FORM';
insert into "0_template".model (formid, title, viewname, active) values('BUSINESS_GOAL_FORM', 'Businees Goal Form', 'BusinessGoal', true);
delete from "0_template".customformsection where form_id='BUSINESS_GOAL_FORM';
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('BUSINESS_GOAL_FORM', 'GOAL_DETAILS', 0, true);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('BUSINESS_GOAL_FORM', 'ASSIGNEES', 1, false);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('BUSINESS_GOAL_FORM', 'ATTACHMENTS_TITLE', 2, false);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('BUSINESS_GOAL_FORM', 'NOTES', 3, false);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('BUSINESS_GOAL_FORM', 'LINKS2', 4, false);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('BUSINESS_GOAL_FORM', 'ADDITIONAL_INFORMATION', 5, false);
delete from "0_template".modelfield where form_id='BUSINESS_GOAL_FORM';
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'GOAL_TITLE', true, 'COL_1', 'GOAL_DETAILS', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'GOAL_START_DATE', true, 'COL_1', 'GOAL_DETAILS', 1);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'GOAL_DESCRIPTION', false, 'COL_1', 'GOAL_DETAILS', 2);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'GOAL_STATUS', true, 'COL_2', 'GOAL_DETAILS', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'COMPANY_GOAL', false, 'COL_2', 'GOAL_DETAILS', 1);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'GOAL_ACTION_STEPS', false, 'COL_2', 'GOAL_DETAILS', 2);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'GOAL_VALIDITY_PERIOD', false, 'COL_3', 'GOAL_DETAILS', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'GOAL_MEASUREMENT_UNIT', false, 'COL_3', 'GOAL_DETAILS', 1);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'GOAL_SCORE_CALCULATION', false, 'COL_3', 'GOAL_DETAILS', 2);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'GOAL_ASSIGNEES', false, 'COL_1', 'ASSIGNEES', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'GOAL_RESOLVER', false, 'COL_1', 'ASSIGNEES', 1);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'ATTACHMENTS', false, 'COL_1', 'ATTACHMENTS_TITLE', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'CRM_NOTE', false, 'COL_1', 'NOTES', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUSINESS_GOAL_FORM', 'LINKS', false, 'COL_1', 'LINKS2', 0);
