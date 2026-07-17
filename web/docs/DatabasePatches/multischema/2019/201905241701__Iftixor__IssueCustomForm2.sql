
update  "0".model set viewname='FixedAsset' where formid='FIXED_ASSET_FORM';
update  "0".model set viewname='ProductServiceAdd' where formid='PRODUCT';
insert into "0".model (formid, title, viewname, active) values('ISSUE_FORM', 'Issue Form', 'Issues', true);

insert into "0".customformsection (form_id, section, sorder, expanded) values('ISSUE_FORM', 'DETAILS', 0, true);
insert into "0".customformsection (form_id, section, sorder, expanded) values('ISSUE_FORM', 'ASSIGNEE', 1, false);
insert into "0".customformsection (form_id, section, sorder, expanded) values('ISSUE_FORM', 'ATTACHMENTS_TITLE', 2, false);
insert into "0".customformsection (form_id, section, sorder, expanded) values('ISSUE_FORM', 'LINKS2', 3, false);
insert into "0".customformsection (form_id, section, sorder, expanded) values('ISSUE_FORM', 'NOTES_TITLE', 4, false);
insert into "0".customformsection (form_id, section, sorder, expanded) values('ISSUE_FORM', 'UPDATES', 5, false);
insert into "0".customformsection (form_id, section, sorder, expanded) values('ISSUE_FORM', 'ADDITIONAL_INFORMATION', 6, false);


insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'NAME', true, 'COL_1', 'DETAILS', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'DESCRIPTION', false, 'COL_1', 'DETAILS', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'REPORTED_BY', false, 'COL_1', 'DETAILS', 2);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'BILLABLE', false, 'COL_1', 'DETAILS', 3);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'PROJECT_FIELD', true, 'COL_2', 'DETAILS', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'PERIOD', false, 'COL_2', 'DETAILS', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'RESOLVER', false, 'COL_2', 'DETAILS', 2);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'ENABLE_TIME_SHEET', false, 'COL_2', 'DETAILS', 3);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'NUMBER', false, 'COL_3', 'DETAILS', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'PRIORITY', false, 'COL_3', 'DETAILS', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'STATUS', false, 'COL_3', 'DETAILS', 2);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'VISIBILITY', false, 'COL_3', 'DETAILS', 3);

insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'CREATED_BY', false, 'COL_1', 'UPDATES', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'CREATED_DATE', false, 'COL_1', 'UPDATES', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'UPDATED_BY', false, 'COL_2', 'UPDATES', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'UPDATED_DATE', false, 'COL_2', 'UPDATES', 1);

insert into "0".modelfield (form_id, field_id, mandatory,  fsection, forder) values('ISSUE_FORM', 'ASSIGNEES', true, 'ASSIGNEE', 0);
insert into "0".modelfield (form_id, field_id, mandatory,  fsection, forder) values('ISSUE_FORM', 'ATTACHMENTS', false, 'ATTACHMENTS_TITLE', 0);
insert into "0".modelfield (form_id, field_id, mandatory,  fsection, forder) values('ISSUE_FORM', 'LINKS', false, 'LINKS2', 0);
insert into "0".modelfield (form_id, field_id, mandatory,  fsection, forder) values('ISSUE_FORM', 'NOTES', false, 'NOTES_TITLE', 0);


update  "anv".model set viewname='FixedAsset' where formid='FIXED_ASSET_FORM';
update  "anv".model set viewname='ProductServiceAdd' where formid='PRODUCT';
insert into "anv".model (formid, title, viewname, active) values('ISSUE_FORM', 'Issue Form', 'Issues', true);

insert into "anv".customformsection (form_id, section, sorder, expanded) values('ISSUE_FORM', 'DETAILS', 0, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('ISSUE_FORM', 'ASSIGNEE', 1, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('ISSUE_FORM', 'ATTACHMENTS_TITLE', 2, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('ISSUE_FORM', 'LINKS2', 3, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('ISSUE_FORM', 'NOTES_TITLE', 4, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('ISSUE_FORM', 'UPDATES', 5, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('ISSUE_FORM', 'ADDITIONAL_INFORMATION', 6, false);


insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'NAME', true, 'COL_1', 'DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'DESCRIPTION', false, 'COL_1', 'DETAILS', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'REPORTED_BY', false, 'COL_1', 'DETAILS', 2);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'BILLABLE', false, 'COL_1', 'DETAILS', 3);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'PROJECT_FIELD', true, 'COL_2', 'DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'PERIOD', false, 'COL_2', 'DETAILS', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'RESOLVER', false, 'COL_2', 'DETAILS', 2);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'ENABLE_TIME_SHEET', false, 'COL_2', 'DETAILS', 3);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'NUMBER', false, 'COL_3', 'DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'PRIORITY', false, 'COL_3', 'DETAILS', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'STATUS', false, 'COL_3', 'DETAILS', 2);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'VISIBILITY', false, 'COL_3', 'DETAILS', 3);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'CREATED_BY', false, 'COL_1', 'UPDATES', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'CREATED_DATE', false, 'COL_1', 'UPDATES', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'UPDATED_BY', false, 'COL_2', 'UPDATES', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'UPDATED_DATE', false, 'COL_2', 'UPDATES', 1);

insert into "anv".modelfield (form_id, field_id, mandatory,  fsection, forder) values('ISSUE_FORM', 'ASSIGNEES', true, 'ASSIGNEE', 0);
insert into "anv".modelfield (form_id, field_id, mandatory,  fsection, forder) values('ISSUE_FORM', 'ATTACHMENTS', false, 'ATTACHMENTS_TITLE', 0);
insert into "anv".modelfield (form_id, field_id, mandatory,  fsection, forder) values('ISSUE_FORM', 'LINKS', false, 'LINKS2', 0);
insert into "anv".modelfield (form_id, field_id, mandatory,  fsection, forder) values('ISSUE_FORM', 'NOTES', false, 'NOTES_TITLE', 0);

update  "0_template".model set viewname='FixedAsset' where formid='FIXED_ASSET_FORM';
update  "0_template".model set viewname='ProductServiceAdd' where formid='PRODUCT';
insert into "0_template".model (formid, title, viewname, active) values('ISSUE_FORM', 'Issue Form', 'Issues', true);

insert into "0_template".customformsection (form_id, section, sorder, expanded) values('ISSUE_FORM', 'DETAILS', 0, true);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('ISSUE_FORM', 'ASSIGNEE', 1, false);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('ISSUE_FORM', 'ATTACHMENTS_TITLE', 2, false);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('ISSUE_FORM', 'LINKS2', 3, false);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('ISSUE_FORM', 'NOTES_TITLE', 4, false);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('ISSUE_FORM', 'UPDATES', 5, false);
insert into "0_template".customformsection (form_id, section, sorder, expanded) values('ISSUE_FORM', 'ADDITIONAL_INFORMATION', 6, false);


insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'NAME', true, 'COL_1', 'DETAILS', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'DESCRIPTION', false, 'COL_1', 'DETAILS', 1);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'REPORTED_BY', false, 'COL_1', 'DETAILS', 2);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'BILLABLE', false, 'COL_1', 'DETAILS', 3);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'PROJECT_FIELD', true, 'COL_2', 'DETAILS', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'PERIOD', false, 'COL_2', 'DETAILS', 1);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'RESOLVER', false, 'COL_2', 'DETAILS', 2);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'ENABLE_TIME_SHEET', false, 'COL_2', 'DETAILS', 3);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'NUMBER', false, 'COL_3', 'DETAILS', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'PRIORITY', false, 'COL_3', 'DETAILS', 1);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'STATUS', false, 'COL_3', 'DETAILS', 2);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'VISIBILITY', false, 'COL_3', 'DETAILS', 3);

insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'CREATED_BY', false, 'COL_1', 'UPDATES', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'CREATED_DATE', false, 'COL_1', 'UPDATES', 1);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'UPDATED_BY', false, 'COL_2', 'UPDATES', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ISSUE_FORM', 'UPDATED_DATE', false, 'COL_2', 'UPDATES', 1);

insert into "0_template".modelfield (form_id, field_id, mandatory,  fsection, forder) values('ISSUE_FORM', 'ASSIGNEES', true, 'ASSIGNEE', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory,  fsection, forder) values('ISSUE_FORM', 'ATTACHMENTS', false, 'ATTACHMENTS_TITLE', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory,  fsection, forder) values('ISSUE_FORM', 'LINKS', false, 'LINKS2', 0);
insert into "0_template".modelfield (form_id, field_id, mandatory,  fsection, forder) values('ISSUE_FORM', 'NOTES', false, 'NOTES_TITLE', 0);

