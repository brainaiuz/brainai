update "anv".modelfield set columntype = 'COL_1', forder = 0 where field_id = 'ATTRIBUTES' and form_id = 'WORKFLOW_WEB_HOOK_FORM';
update "anv".modelfield set columntype = 'COL_2', forder = 0 where field_id = 'RESPONSED_VALUE' and form_id = 'WORKFLOW_WEB_HOOK_FORM';
update "anv".modelfield set columntype = 'COL_2', forder = 1 where field_id = 'RESPONSE_QUERY' and form_id = 'WORKFLOW_WEB_HOOK_FORM';


update "anv".modelfield set columntype = 'COL_1', forder = 0 where field_id = 'METHOD' and form_id = 'WORKFLOW_WEB_HOOK_FORM';
update "anv".modelfield set columntype = 'COL_1', forder = 1 where field_id = 'REQUEST_URL' and form_id = 'WORKFLOW_WEB_HOOK_FORM';
update "anv".modelfield set columntype = 'COL_1', forder = 2 where field_id = 'RAW_DATA_FORMAT' and form_id = 'WORKFLOW_WEB_HOOK_FORM';
update "anv".modelfield set columntype = 'COL_1', forder = 3 where field_id = 'HEADER' and form_id = 'WORKFLOW_WEB_HOOK_FORM';

update "anv".modelfield set columntype = 'COL_2', forder = 0 where field_id = 'BODY_TYPE' and form_id = 'WORKFLOW_WEB_HOOK_FORM';
update "anv".modelfield set columntype = 'COL_2', forder = 1 where field_id = 'FORM_DATA_PARAMS' and form_id = 'WORKFLOW_WEB_HOOK_FORM';
update "anv".modelfield set columntype = 'COL_2', forder = 2 where field_id = 'RAW_DATA_BOX' and form_id = 'WORKFLOW_WEB_HOOK_FORM';