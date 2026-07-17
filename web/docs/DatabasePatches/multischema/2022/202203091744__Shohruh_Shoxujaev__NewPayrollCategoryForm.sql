delete from "0".model where formid='PAYROLL_CATEGORY_FORM';
delete from "anv".model where formid='PAYROLL_CATEGORY_FORM';
delete from "0".customformsection where form_id='PAYROLL_CATEGORY_FORM';
delete from "anv".customformsection where form_id='PAYROLL_CATEGORY_FORM';
delete from "0".modelfield where form_id='PAYROLL_CATEGORY_FORM';
delete from "anv".modelfield where form_id='PAYROLL_CATEGORY_FORM';

insert into "0".model(active, formid, title, viewname) values(true, 'PAYROLL_CATEGORY_FORM', 'Web form', 'WebForm');
insert into "anv".model(active, formid, title, viewname) values(true, 'PAYROLL_CATEGORY_FORM', 'Web form', 'WebForm');

INSERT INTO "0".customformsection (form_id, section, active, custom, sorder) VALUES ('PAYROLL_CATEGORY_FORM', 'CATEGORY', true, false, 0);
INSERT INTO "anv".customformsection (form_id, section, active, custom, sorder) VALUES ('PAYROLL_CATEGORY_FORM', 'CATEGORY', true, false, 0);

insert into "0".modelfield (form_id, fsection, columntype, mandatory, forder, field_id) values
        ('PAYROLL_CATEGORY_FORM', 'CATEGORY', 'COL_1', true, 0, 'TYPE'),
        ('PAYROLL_CATEGORY_FORM', 'CATEGORY', 'COL_1', true, 1, 'CODE'),
        ('PAYROLL_CATEGORY_FORM', 'CATEGORY', 'COL_1', true, 2, 'NAME'),
        ('PAYROLL_CATEGORY_FORM', 'CATEGORY', 'COL_2', false, 0, 'DEBIT_TO_ACCOUNT'),
        ('PAYROLL_CATEGORY_FORM', 'CATEGORY', 'COL_2', false, 1, 'CREDIT_TO_ACCOUNT'),
        ('PAYROLL_CATEGORY_FORM', 'CATEGORY', 'COL_2', false, 2, 'IS_CASH_ADVANCE');

insert into "anv".modelfield (form_id, fsection, columntype, mandatory, forder, field_id) values
        ('PAYROLL_CATEGORY_FORM', 'CATEGORY', 'COL_1', true, 0, 'TYPE'),
        ('PAYROLL_CATEGORY_FORM', 'CATEGORY', 'COL_1', true, 1, 'CODE'),
        ('PAYROLL_CATEGORY_FORM', 'CATEGORY', 'COL_1', true, 2, 'NAME'),
        ('PAYROLL_CATEGORY_FORM', 'CATEGORY', 'COL_2', false, 0, 'DEBIT_TO_ACCOUNT'),
        ('PAYROLL_CATEGORY_FORM', 'CATEGORY', 'COL_2', false, 1, 'CREDIT_TO_ACCOUNT'),
        ('PAYROLL_CATEGORY_FORM', 'CATEGORY', 'COL_2', false, 2, 'IS_CASH_ADVANCE');