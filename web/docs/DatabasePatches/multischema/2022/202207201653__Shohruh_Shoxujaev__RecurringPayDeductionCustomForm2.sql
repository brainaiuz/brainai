delete from "anv".mymodule where code = 'RECURRING_PAYMENT_DEDUCTION_MODULE';
insert into "anv".mymodule (code,name,section,active) values ('RECURRING_PAYMENT_DEDUCTION_MODULE','Recurring Payment/Deduction Management','payroll',true);

--Recurring Deduction

delete from "anv".model where formid = 'PAYROLL_RECURRING_DEDUCTION_FORM';
insert into "anv".model (active, formid, title, viewname)  values
    (true,  'PAYROLL_RECURRING_DEDUCTION_FORM',  'Recurring Deduction', 'RecurringPayDeductionList');

delete from "anv".customformsection where form_id = 'PAYROLL_RECURRING_DEDUCTION_FORM';
insert into "anv".customformsection
(form_id,section,sorder,expanded) values ('PAYROLL_RECURRING_DEDUCTION_FORM', 'MAIN', 0, true);


delete from "anv".modelfield where form_id = 'PAYROLL_RECURRING_DEDUCTION_FORM';
insert into "anv".modelfield
(form_id,fsection,section,fieldstyle,fullwidth,hide,columntype,mandatory,sectionstyle,widget,forder,field_id) values
                                                                                                                  ('PAYROLL_RECURRING_DEDUCTION_FORM',	'MAIN','MAIN','field',false,false,'COL_1',true,'','LOOKUP',0,'EMPLOYEE'),
                                                                                                                  ('PAYROLL_RECURRING_DEDUCTION_FORM',	'MAIN','MAIN','field',false,false,'COL_1',true,'','LOOKUP',1,'CATEGORY'),
                                                                                                                  ('PAYROLL_RECURRING_DEDUCTION_FORM',	'MAIN','MAIN','field',false,false,'COL_1',true,'','LOOKUP',2,'PAY_FROM'),
                                                                                                                  ('PAYROLL_RECURRING_DEDUCTION_FORM',	'MAIN','MAIN','field',false,false,'COL_1',true,'','LOOKUP',3,'PAY_TO_LIMIT'),
                                                                                                                  ('PAYROLL_RECURRING_DEDUCTION_FORM',	'MAIN','MAIN','field',false,false,'COL_2',true,'','LOOKUP',0,'PAYMENT_TERMS'),
                                                                                                                  ('PAYROLL_RECURRING_DEDUCTION_FORM',	'MAIN','MAIN','field',false,false,'COL_2',true,'','LOOKUP',1,'PAYMENT_AMOUNT'),
                                                                                                                  ('PAYROLL_RECURRING_DEDUCTION_FORM',	'MAIN','MAIN','field',false,false,'COL_2',true,'','LOOKUP',2,'APPROVER');
--Recurring Payment

delete from "anv".model where formid = 'PAYROLL_RECURRING_PAYMENT_FORM';
insert into "anv".model (active, formid, title, viewname)  values
    (true,  'PAYROLL_RECURRING_PAYMENT_FORM',  'Recurring Payment/Deduction', 'RecurringPayDeductionList');

delete from "anv".customformsection where form_id = 'PAYROLL_RECURRING_PAYMENT_FORM';
insert into "anv".customformsection
(form_id,section,sorder,expanded) values ('PAYROLL_RECURRING_PAYMENT_FORM', 'MAIN', 0, true);


delete from "anv".modelfield where form_id = 'PAYROLL_RECURRING_PAYMENT_FORM' ;
insert into "anv".modelfield
(form_id,fsection,section,fieldstyle,fullwidth,hide,columntype,mandatory,sectionstyle,widget,forder,field_id) values
                                                                                                                  ('PAYROLL_RECURRING_PAYMENT_FORM',	'MAIN','MAIN','field',false,false,'COL_1',true,'','LOOKUP',0,'EMPLOYEE'),
                                                                                                                  ('PAYROLL_RECURRING_PAYMENT_FORM',	'MAIN','MAIN','field',false,false,'COL_1',true,'','LOOKUP',1,'CATEGORY'),
                                                                                                                  ('PAYROLL_RECURRING_PAYMENT_FORM',	'MAIN','MAIN','field',false,false,'COL_1',true,'','LOOKUP',2,'PAY_FROM'),
                                                                                                                  ('PAYROLL_RECURRING_PAYMENT_FORM',	'MAIN','MAIN','field',false,false,'COL_2',true,'','LOOKUP',0,'PAYMENT_TERMS'),
                                                                                                                  ('PAYROLL_RECURRING_PAYMENT_FORM',	'MAIN','MAIN','field',false,false,'COL_2',true,'','LOOKUP',1,'PAYMENT_AMOUNT'),
                                                                                                                  ('PAYROLL_RECURRING_PAYMENT_FORM',	'MAIN','MAIN','field',false,false,'COL_2',true,'','LOOKUP',2,'APPROVER');


delete from "anv".container_item where moduleID = (select id from "anv".mymodule where code='PAYROLL' limit 1) and propertyID = (select id from "anv".property where objectName='recurringPayDeductionList' limit 1);

delete from "anv".property where objectName = 'recurringPayDeductionList';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('recurringPayDeductionList', 'Recurring Payment/Deduction', 'Recurring Payment/Deduction', 'Recurring Payment/Deductions', 'RPD', 'payroll', false) on conflict do nothing;

insert into "anv".container_item (isactive, modulecode, sorder, containerid, moduleid, propertyid)
values(true, 'payroll', 6, (select id from "anv".container where preparedview='payrollHome' limit 1),
       (select id from "anv".mymodule where code='PAYROLL' limit 1),
       (select id from "anv".property where objectname='recurringPayDeductionList' limit 1)) on conflict do nothing;