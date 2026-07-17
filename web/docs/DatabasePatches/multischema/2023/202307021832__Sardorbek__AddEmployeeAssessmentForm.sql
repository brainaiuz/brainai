insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('assessment', 'Assessment', 'Assessment', 'Assessments', 'AT', 'hrms', true);

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code = 'HRMS_MODULE' limit 1),
        (select id from "anv".property where objectName = 'assessment' limit 1),
        (select id from "anv".container where code = 'performanceAppraisals' limit 1), 18, 'hrms');

insert into "anv".model (formid, title, viewname, active)
values ('ASSESSMENT_FORM', 'assessment', 'assessment', true);


insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('ASSESSMENT_FORM', 'APPRAISIAL_FOR', false, false, 'COL_1', 'BASIC_INFORMATION', 0);

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('ASSESSMENT_FORM', 'COMPANY_NAME', false, false, 'COL_1', 'BASIC_INFORMATION', 1);

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('ASSESSMENT_FORM', 'DEPARTMENT_NAME', false, false, 'COL_1', 'BASIC_INFORMATION', 2);

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('ASSESSMENT_FORM', 'TEMPLATE_NAME', false, false, 'COL_1', 'BASIC_INFORMATION', 3);

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('ASSESSMENT_FORM', 'OVERALL_COMMENTS', false, false, 'COL_1', 'BASIC_INFORMATION', 4);

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('ASSESSMENT_FORM', 'OVERALL_RATE', false, false, 'COL_2', 'BASIC_INFORMATION', 0);

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('ASSESSMENT_FORM', 'EMPLOYEE_NAME', false, false, 'COL_2', 'BASIC_INFORMATION', 1);

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('ASSESSMENT_FORM', 'MANAGER_NAME', false, false, 'COL_2', 'BASIC_INFORMATION', 2);

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('ASSESSMENT_FORM', 'EMPLOYEE_COMPETENCIES', true, false, 'COL_1', 'EMPLOYEE_COMPETENCIES', 0);

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('ASSESSMENT_FORM', 'ASSIGNED_GOALS', true, false, 'COL_2', 'ASSIGNED_GOALS', 0);






