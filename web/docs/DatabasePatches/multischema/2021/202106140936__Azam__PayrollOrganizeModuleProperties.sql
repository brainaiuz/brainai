
--single payrun
delete from "anv".property where objectName = 'singlePayrunList';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive, iscustom)
values ('singlePayrunList', 'Single Payruns', 'Single Payrun', 'Single Payruns', 'S', 'payroll', false, false);


--group payrun
delete from "anv".property where objectName = 'payslipTableList';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive, iscustom)
values ('payslipTableList', 'Group Payruns', 'Group Payrun', 'Group Payruns', 'G', 'payroll', false, false);


--cash advance
update "anv".property set moduleCode='hrms,payroll' where objectName = 'cashadvanceList';


--additional payment
delete from "anv".property where objectName = 'additionalpaymentList';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive, iscustom)
values ('additionalpaymentList', 'Additional Payments', 'Additional Payment', 'Additional Payments', 'A', 'payroll', false, false);

--single payment
delete from "anv".property where objectName = 'additionalpaymentItemList';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive, iscustom)
values ('additionalpaymentItemList', 'Single Payments', 'Single Payment', 'Single Payments', 'S', 'payroll', false, false);
