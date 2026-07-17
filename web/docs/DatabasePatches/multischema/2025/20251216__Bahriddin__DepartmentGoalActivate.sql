-- listing sectiondi active qilish
UPDATE "anv".container_item ci
SET isactive = true
FROM "anv".property p
WHERE ci.propertyid = p.id
  AND p.objectname = 'departmentgoal';

-- kerakmas sectionlarni hide qilish
update "anv".customformsection
set active = false
where form_id = 'DEPARTMENT_GOAL_FORM'
  and section in ('LINKS2', 'ADDITIONAL_INFORMATION', 'NOTES', 'ATTACHMENTS_TITLE');

-- kerakmas fieldlarni hide qilish
update "anv".modelfield
set hide = true
where form_id = 'DEPARTMENT_GOAL_FORM'
  and field_id in ('ATTACHMENTS', 'GOAL_ACTION_STEPS', 'LINKS', 'CRM_NOTE', 'GOAL_MEASUREMENT_UNIT', 'COMPANY_GOAL');

-- default expanded qilish
update "anv".customformsection
set expanded = true
where form_id = 'DEPARTMENT_GOAL_FORM'
  and section = 'ASSIGNEES';

-- delete no need fields
delete
from "anv".modelfield
where form_id = 'DEPARTMENT_GOAL_FORM'
  and field_id in ('GOAL_SCORE_CALCULATION', 'GOAL_VALIDITY_PERIOD');

-- standarting playouts for all companies
update "anv".modelfield
set columntype ='COL_2',
    forder=1
where form_id = 'DEPARTMENT_GOAL_FORM'
  and field_id = 'GOAL_START_DATE';

update "anv".modelfield
set columntype ='COL_1',
    forder=0
where form_id = 'DEPARTMENT_GOAL_FORM'
  and field_id = 'GOAL_RESOLVER';

update "anv".modelfield
set columntype ='COL_1',
    forder=2
where form_id = 'DEPARTMENT_GOAL_FORM'
  and field_id = 'GOAL_PROORDEP';

update "anv".modelfield
set columntype ='COL_1',
    forder=0
where form_id = 'DEPARTMENT_GOAL_FORM'
  and field_id = 'GOAL_TITLE';

update "anv".modelfield
set columntype ='COL_3',
    forder=0
where form_id = 'DEPARTMENT_GOAL_FORM'
  and field_id = 'GOAL_DESCRIPTION';

update "anv".modelfield
set columntype ='COL_1',
    forder=1
where form_id = 'DEPARTMENT_GOAL_FORM'
  and field_id = 'GOAL_ASSIGNEES';