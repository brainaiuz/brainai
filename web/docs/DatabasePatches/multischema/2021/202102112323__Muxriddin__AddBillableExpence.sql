
delete from "anv".model where formid = 'BILLABLE_EXPENSE_FORM';
insert into "anv".model (active, formid, title, viewname)  values
(true,  'BILLABLE_EXPENSE_FORM',  'Billable Expence', null);

delete from "anv".customformsection where form_id = 'BILLABLE_EXPENSE_FORM';
insert into "anv".customformsection
(form_id,section,sorder,expanded) values
('BILLABLE_EXPENSE_FORM',	  'BILLABLE_FORM_TITLE',   0, true ),
('BILLABLE_EXPENSE_FORM',	  'ITEMS_TABLE',   1, true );

delete from "anv".modelfield where form_id = 'BILLABLE_EXPENSE_FORM';
insert into "anv".modelfield
(form_id,                      fsection,                   section,                  fieldstyle, fullwidth, hide,    columntype,         mandatory,   sectionstyle,     widget,         forder,     field_id) values
('BILLABLE_EXPENSE_FORM',	  'BILLABLE_FORM_TITLE',      'BILLABLE_FORM_TITLE',    'field',    false,      false,   'COL_1',	         false,         '',             'DropDown',       1,	    'MARKUP_TYPE'),
('BILLABLE_EXPENSE_FORM',	  'BILLABLE_FORM_TITLE',      'BILLABLE_FORM_TITLE',    'field',    false,      false,   'COL_2',	         false,         '',             'TextBox',        1,   	    'MARKUP_AMOUNT'),
('BILLABLE_EXPENSE_FORM',	  'BILLABLE_FORM_TITLE',      'BILLABLE_FORM_TITLE',    'field',    false,      false,   'COL_3',	         false,         '',             'LOOKUP',         1,   	    'ACCOUNT_LIST'),
('BILLABLE_EXPENSE_FORM',	  'BILLABLE_FORM_TITLE',      'BILLABLE_FORM_TITLE',    'field',    false,      false,   'COL_1',	         false,         '',             'LOOKUP',         2,	    'EX_RATE'),
('BILLABLE_EXPENSE_FORM',	  'ITEMS_TABLE',              'ITEMS_TABLE',            'field',    true ,      false,   'COL_1',	         false,         '',             'UNKNOWN',        1,	    'ITEMS_TABLE');


