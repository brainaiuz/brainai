
 delete from "anv".model where formid = 'MULTI_CASH_ADVANCE_FORM';
 insert into "anv".model (active, formid, title, viewname)  values
 (true,  'MULTI_CASH_ADVANCE_FORM',  'Multi Cash Advance', 'MultiCashAdvanceList');


 delete from "anv".customformsection where form_id = 'MULTI_CASH_ADVANCE_FORM';
 insert into "anv".customformsection
 (form_id,section,sorder,expanded) values
 ('MULTI_CASH_ADVANCE_FORM',	  'INFORMATION',   0, true ),
 ('MULTI_CASH_ADVANCE_FORM',	  'ITEMS',   1, true ),
 ('MULTI_CASH_ADVANCE_FORM',	  'ADDITIONAL_INFORMATION',   2, false );


delete from "anv".modelfield where form_id = 'MULTI_CASH_ADVANCE_FORM' ;
insert into "anv".modelfield
(form_id,fsection,section,fieldstyle,fullwidth,hide,columntype,mandatory,sectionstyle,widget,forder,field_id) values
('MULTI_CASH_ADVANCE_FORM',	'INFORMATION','INFORMATION','field',false,false,'COL_1',true,'','LOOKUP',0,'EMPLOYEE'),
('MULTI_CASH_ADVANCE_FORM',	'INFORMATION','INFORMATION','field',false,false,'COL_1',true,'','LOOKUP',1,'CATEGORY'),
('MULTI_CASH_ADVANCE_FORM',	'INFORMATION','INFORMATION','field',false,false,'COL_1',true,'','LOOKUP',2,'PAYMENT_METHOD'),
('MULTI_CASH_ADVANCE_FORM',	'INFORMATION','INFORMATION','field',false,false,'COL_2',true,'','LOOKUP',0,'REQUESTED_DATE'),
('MULTI_CASH_ADVANCE_FORM',	'INFORMATION','INFORMATION','field',false,false,'COL_2',true,'','LOOKUP',1,'PAYMENT_TYPE'),
('MULTI_CASH_ADVANCE_FORM',	'INFORMATION','INFORMATION','field',false,false,'COL_2',true,'','LOOKUP',2,'PAYMENT_TERMS'),
('MULTI_CASH_ADVANCE_FORM',	'INFORMATION','INFORMATION','field',false,false,'COL_3',true,'','LOOKUP',0,'NUMBER'),
('MULTI_CASH_ADVANCE_FORM',	'INFORMATION','INFORMATION','field',false,false,'COL_3',true,'','LOOKUP',1,'AMOUNT'),
('MULTI_CASH_ADVANCE_FORM',	'INFORMATION','INFORMATION','field',false,false,'COL_3',true,'','LOOKUP',2,'APPROVERS'),
('MULTI_CASH_ADVANCE_FORM',	  'ITEMS',      'ITEMS',    'field',true,false,'COL_1',false,'','UNKNOWN',0,'ITEMS'),
('MULTI_CASH_ADVANCE_FORM',	'INFORMATION','INFORMATION','field',false,false,'COL_1',true,'','LOOKUP',0,'DATE'),
('MULTI_CASH_ADVANCE_FORM',	'INFORMATION','INFORMATION','field',false,false,'COL_2',true,'','LOOKUP',0,'PAY_FROM'),
('MULTI_CASH_ADVANCE_FORM',	'INFORMATION','INFORMATION','field',false,false,'COL_3',true,'','LOOKUP',0,'CASH_ADVANCE_ACCOUNT');


delete from "anv".modelfield where form_id = 'PAYROLL_CASH_ADVANCE_FORM' and field_id='DATE';
insert into "anv".modelfield
(form_id,fsection,section,fieldstyle,fullwidth,hide,columntype,mandatory,sectionstyle,widget,forder,field_id) values
('PAYROLL_CASH_ADVANCE_FORM',	'DETAILS','DETAILS','field',false,false,'COL_1',true,'','LOOKUP', 20,'DATE');

delete from "anv".container_item where propertyID is null;

delete from "anv".container_item where moduleID = (select id from "anv".mymodule where code='PAYROLL' limit 1) and propertyID = (select id from "anv".property where objectName='multiCashadvanceList' limit 1);

delete from "anv".property where objectName = 'multiCashadvanceList';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('multiCashadvanceList', 'Multi Cash Advances', 'Multi Cash Advance', 'Multi Cash Advances', 'MCA', 'hrms,payroll', false) on conflict do nothing;

insert into "anv".container_item (isactive, modulecode, sorder, containerid, moduleid, propertyid)
values(true, 'payroll', 5, (select id from "anv".container where preparedview='payrollHome' limit 1),
       (select id from "anv".mymodule where code='PAYROLL' limit 1),
       (select id from "anv".property where objectname='multiCashadvanceList' limit 1)) on conflict do nothing;
