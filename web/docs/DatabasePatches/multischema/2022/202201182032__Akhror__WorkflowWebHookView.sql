delete from "anv".model where formid = 'WORKFLOW_WEB_HOOK_FORM';
insert into "anv".model(active, formid, title, viewname) values (true, 'WORKFLOW_WEB_HOOK_FORM', 'Web Hook form', 'WorkflowWebHooks');

delete from "anv".customformsection where form_id = 'WORKFLOW_WEB_HOOK_FORM';
insert into "anv".customformsection (form_id, section, sorder, expanded) values('WORKFLOW_WEB_HOOK_FORM', 'WEB_HOOK_DETAILS', 0, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('WORKFLOW_WEB_HOOK_FORM', 'WEB_HOOK_ATTRIBUTES', 1, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('WORKFLOW_WEB_HOOK_FORM', 'REQUEST_DETAILS', 2, true);

delete from "anv".modelfield where form_id = 'WORKFLOW_WEB_HOOK_FORM';
insert into "anv".modelfield(columntype, field_id, forder, form_id, fsection, mandatory,systemmandatory) values
                                ('COL_1', 'NAME', 0, 'WORKFLOW_WEB_HOOK_FORM', 'WEB_HOOK_DETAILS', true, true),
                                ('COL_2', 'DESCRIPTION', 0, 'WORKFLOW_WEB_HOOK_FORM', 'WEB_HOOK_DETAILS', false, false),
                                ('COL_1', 'ATTRIBUTES', 1, 'WORKFLOW_WEB_HOOK_FORM', 'WEB_HOOK_ATTRIBUTES', false, false),
                                ('COL_1', 'METHOD', 0, 'WORKFLOW_WEB_HOOK_FORM', 'REQUEST_DETAILS', true, true),
                                ('COL_1', 'REQUEST_URL', 1, 'WORKFLOW_WEB_HOOK_FORM', 'REQUEST_DETAILS', true, true),
                                ('COL_1', 'HEADER', 2, 'WORKFLOW_WEB_HOOK_FORM', 'REQUEST_DETAILS', true, true),
                                ('COL_1', 'BODY_TYPE', 3, 'WORKFLOW_WEB_HOOK_FORM', 'REQUEST_DETAILS', true, true),
                                ('COL_1', 'FORM_DATA_PARAMS', 4, 'WORKFLOW_WEB_HOOK_FORM', 'REQUEST_DETAILS', true, true),
                                ('COL_1', 'RAW_DATA_FORMAT', 5, 'WORKFLOW_WEB_HOOK_FORM', 'REQUEST_DETAILS', true, true),
                                ('COL_1', 'RAW_DATA_BOX', 6, 'WORKFLOW_WEB_HOOK_FORM', 'REQUEST_DETAILS', true, true);