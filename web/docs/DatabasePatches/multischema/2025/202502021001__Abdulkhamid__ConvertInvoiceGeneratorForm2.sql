delete from "anv".model where formid = 'INVOICE_GENERATOR_FORM';
insert into "anv".model (formid, title, viewname, active) values('INVOICE_GENERATOR_FORM', 'Invoice Generator Form', null, true);

delete from "anv".customformsection where form_id = 'INVOICE_GENERATOR_FORM';
insert into "anv".customformsection (form_id, section, sorder, expanded) values('INVOICE_GENERATOR_FORM', 'INVOICE_GENERATOR_DETAILS', 0, true);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('INVOICE_GENERATOR_FORM', 'PERIOD', false, 'COL_1', 'INVOICE_GENERATOR_DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('INVOICE_GENERATOR_FORM', 'END', true, 'COL_2', 'INVOICE_GENERATOR_DETAILS', 1);
