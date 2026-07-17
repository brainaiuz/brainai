delete from "anv".model where formid = 'SCHEDULE_INVOICE_FORM';
insert into "anv".model (formid, title, viewname, active) values('SCHEDULE_INVOICE_FORM', 'Schedule Invoice Form', null, true);

delete from "anv".customformsection where form_id = 'SCHEDULE_INVOICE_FORM';
insert into "anv".customformsection (form_id, section, sorder, expanded) values('SCHEDULE_INVOICE_FORM', 'SCHEDULE_INVOICE_DETAILS', 0, true);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('SCHEDULE_INVOICE_FORM', 'PERIOD', false, 'COL_1', 'SCHEDULE_INVOICE_DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('SCHEDULE_INVOICE_FORM', 'END', true, 'COL_2', 'SCHEDULE_INVOICE_DETAILS', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('SCHEDULE_INVOICE_FORM', 'CUSTOMER', true, 'COL_3', 'SCHEDULE_INVOICE_DETAILS', 2);
