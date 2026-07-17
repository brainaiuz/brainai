delete
from "anv".modelfield
where form_id = 'WORKFLOW_WEB_HOOK_FORM'
  and field_id = 'TEMPLATE';

insert into "anv".modelfield(form_id, field_id, columntype, fsection, forder)
values ('WORKFLOW_WEB_HOOK_FORM', 'TEMPLATE', 'COL_1', 'WEB_HOOK_DETAILS', 6);