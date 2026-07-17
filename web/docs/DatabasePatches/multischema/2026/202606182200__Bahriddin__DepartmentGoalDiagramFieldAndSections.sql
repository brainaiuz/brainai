INSERT INTO "anv".customformsection (custom, expanded, form_id, ispagination, label, section, sorder,
                                     customformlocalizationid)
VALUES (false, true, 'DEPARTMENT_GOAL_FORM', null, null, 'CHART_SECTION', 1, null);

update "anv".customformsection
set sorder =2
where form_id = 'DEPARTMENT_GOAL_FORM'
  and section = 'ASSIGNEES';



update "anv".customformsection
set sorder =3
where form_id = 'DEPARTMENT_GOAL_FORM'
  and section = 'ATTACHMENTS_TITLE';


insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder, hide)
values ('DEPARTMENT_GOAL_FORM', 'GOAL_CHART', false, 'COL_1', 'CHART_SECTION', 4, false);


DELETE
FROM permission
WHERE code ='HRMS_DEPARTMENT_GOAL_CHART_SETTINGS';

INSERT INTO permission (code, context, name, sorder, parent, modulecode)
SELECT 'HRMS_DEPARTMENT_GOAL_CHART_SETTINGS', 'HRMS', 'Chart Settings', 7, p.id, 'HRMS_MODULE'
FROM permission p
WHERE p.code = 'HRMS_DEPARTMENT_GOALS';

INSERT INTO "anv".permission_context (permissioncode, contextcode)
VALUES ('HRMS_DEPARTMENT_GOAL_CHART_SETTINGS', 'HRMS');


INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
VALUES ('HRMS_DEPARTMENT_GOAL_CHART_SETTINGS', 'ALLOW', 'ADMIN');
