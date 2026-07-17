delete
from "anv".modelfield
where form_id = 'WORKFLOW_WEB_HOOK_FORM'
  and field_id = 'TABLE_FIELD_NAME';

insert into "anv".modelfield(columntype, field_id, forder, form_id, fsection, mandatory, systemmandatory)
values ('COL_1', 'TABLE_FIELD_NAME', 0, 'WORKFLOW_WEB_HOOK_FORM', 'RESPONSE_ATTRIBUTES', true, false);