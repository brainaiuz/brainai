insert into "anv".model (formid, title, viewname, active) values('CHART_OF_ACCOUNT_FORM', 'Chart of Account Form', 'Account', true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('CHART_OF_ACCOUNT_FORM', 'GENERAL_INFORMATION', 0, true);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('CHART_OF_ACCOUNT_FORM', 'CHART_ACCOUNT_TYPE', true, 'COL_1', 'GENERAL_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('CHART_OF_ACCOUNT_FORM', 'CHART_ACCOUNT_CODE', false, 'COL_2', 'GENERAL_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('CHART_OF_ACCOUNT_FORM', 'CHART_ACCOUNT_NAME', false, 'COL_3', 'GENERAL_INFORMATION', 0);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('CHART_OF_ACCOUNT_FORM', 'CHART_ACCOUNT_PARENT', false, 'COL_1', 'GENERAL_INFORMATION', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('CHART_OF_ACCOUNT_FORM', 'SHOW_IN_EXPENCE', false, 'COL_2', 'GENERAL_INFORMATION', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('CHART_OF_ACCOUNT_FORM', 'CHART_ACCOUNT_DESCRIPTION', false, 'COL_3', 'GENERAL_INFORMATION', 1);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('CHART_OF_ACCOUNT_FORM', 'ENABLE_PAYMENTS', false, 'COL_1', 'GENERAL_INFORMATION', 2);
