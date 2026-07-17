insert into "anv".modelfield(columntype, field_id, forder, form_id, fsection, mandatory, systemmandatory)
select 'COL_1',
       'SAVE_INTEGRATION_ID',
       2,
       'WORKFLOW_WEB_HOOK_FORM',
       'WEB_HOOK_DETAILS',
       false,
       false where not exists (select id from "anv".modelfield where form_id = 'WORKFLOW_WEB_HOOK_FORM' and field_id = 'SAVE_INTEGRATION_ID');