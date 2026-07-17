update "anv".property set modulecode='hrms,payroll' where objectname='employee' and modulecode='hrms';
update "anv".property set modulecode='hrms,payroll' where objectname='benefit_requests' and modulecode='hrms';

insert into "anv".property (isactive, defaultname, iscustom, modulecode, objectname, plural, shortcut, singular)
values(false, 'WPS Report', false, 'payroll', 'wpsReport', 'WPS Reports', 'WPSR', 'WPS Report');

insert into "anv".property (isactive, defaultname, iscustom, modulecode, objectname, plural, shortcut, singular)
values(false, 'End Of Service Report', false, 'payroll', 'endOfServiceReport', 'End Of Service Reports', 'EOSR', 'End Of Service Report');

insert into "anv".property (isactive, defaultname, iscustom, modulecode, objectname, plural, shortcut, singular)
values(false, 'Pension Contribution Report', false, 'payroll', 'pensionContributionReport', 'Pension Contribution Reports', 'PCR', 'Pension Contribution Report');

insert into "anv".property (isactive, defaultname, iscustom, modulecode, objectname, plural, shortcut, singular)
values(false, 'Cash Advance Report', false, 'payroll', 'cashAdvanceReport', 'Cash Advance Reports', 'CAR', 'Cash Advance Report');

insert into "anv".property (isactive, defaultname, iscustom, modulecode, objectname, plural, shortcut, singular)
values(false, 'Salary Report', false, 'payroll', 'salaryReport', 'Salary Reports', 'SR', 'Salary Report');

insert into "anv".property (isactive, defaultname, iscustom, modulecode, objectname, plural, shortcut, singular)
values(false, 'Employee Template List', false, 'payroll', 'employeeTemplateList', 'Employee Template Lists', 'ETL', 'Employee Template List');

insert into "anv".property (isactive, defaultname, iscustom, modulecode, objectname, plural, shortcut, singular)
values(false, 'End of Service Gratuity List', false, 'payroll', 'endOfServiceGratuity', 'End of Service Gratuity List', 'ESGL', 'End of Service Gratuity List');

insert into "anv".container (changed, code, iscustom, defaultname, modulecode, preparedview, sorder)
                        values(false, 'payroll', false, 'payroll', 'payroll', 'payrollHome', 1);
insert into "anv".container (changed, code, iscustom, defaultname, modulecode, preparedview, sorder)
                        values(false, 'payrollReports', false, 'reports', 'payroll', 'payrollReportsHome', 2);
insert into "anv".container (changed, code, iscustom, defaultname, modulecode, preparedview, sorder)
                        values(false, 'myPayroll', false, 'myPayroll', 'payroll', 'myPayrollHome', 3);



insert into "anv".container_item (isactive, modulecode, sorder, containerid, moduleid, propertyid)
values(true, 'payroll', 4, (select id from "anv".container where preparedview='payrollHome' limit 1),
       (select id from "anv".mymodule where code='PAYROLL' limit 1),
       (select id from "anv".property where objectname='cashadvanceList' limit 1)) on conflict do nothing;

insert into "anv".container_item (isactive, modulecode, sorder, containerid, moduleid, propertyid)
values(true, 'payroll', 2, (select id from "anv".container where preparedview='payrollHome' limit 1),
       (select id from "anv".mymodule where code='PAYROLL' limit 1),
       (select id from "anv".property where objectname='singlePayrunList' limit 1)) on conflict do nothing;

insert into "anv".container_item (isactive, modulecode, sorder, containerid, moduleid, propertyid)
values(true, 'payroll', 3, (select id from "anv".container where preparedview='payrollHome' limit 1),
       (select id from "anv".mymodule where code='PAYROLL' limit 1),
       (select id from "anv".property where objectname='payslipTableList' limit 1)) on conflict do nothing;

insert into "anv".container_item (isactive, modulecode, sorder, containerid, moduleid, propertyid)
values(true, 'payroll', 6, (select id from "anv".container where preparedview='payrollHome' limit 1),
       (select id from "anv".mymodule where code='PAYROLL' limit 1),
       (select id from "anv".property where objectname='additionalpaymentList' limit 1)) on conflict do nothing;

insert into "anv".container_item (isactive, modulecode, sorder, containerid, moduleid, propertyid)
values(true, 'payroll', 5, (select id from "anv".container where preparedview='payrollHome' limit 1),
       (select id from "anv".mymodule where code='PAYROLL' limit 1),
       (select id from "anv".property where objectname='additionalpaymentItemList' limit 1)) on conflict do nothing;

insert into "anv".container_item (isactive, modulecode, sorder, containerid, moduleid, propertyid)
values(true, 'payroll', 1, (select id from "anv".container where preparedview='payrollHome' limit 1),
       (select id from "anv".mymodule where code='PAYROLL' limit 1),
       (select id from "anv".property where objectname='employee' limit 1)) on conflict do nothing;

insert into "anv".container_item (isactive, modulecode, sorder, containerid, moduleid, propertyid)
values(true, 'payroll', 7, (select id from "anv".container where preparedview='payrollHome' limit 1),
       (select id from "anv".mymodule where code='PAYROLL' limit 1),
       (select id from "anv".property where objectname='benefit_requests' limit 1)) on conflict do nothing;

insert into "anv".container_item (isactive, modulecode, sorder, containerid, moduleid, propertyid)
values(true, 'payroll', 8, (select id from "anv".container where preparedview='payrollHome' limit 1),
       (select id from "anv".mymodule where code='PAYROLL' limit 1),
       (select id from "anv".property where objectname='employeeTemplateList' limit 1)) on conflict do nothing;

insert into "anv".container_item (isactive, modulecode, sorder, containerid, moduleid, propertyid)
values(true, 'payroll', 9, (select id from "anv".container where preparedview='payrollHome' limit 1),
       (select id from "anv".mymodule where code='PAYROLL' limit 1),
       (select id from "anv".property where objectname='endOfServiceGratuity' limit 1)) on conflict do nothing;


insert into "anv".container_item (isactive, modulecode, sorder, containerid, moduleid, propertyid)
values(true, 'payroll', 1, (select id from "anv".container where preparedview='payrollReportsHome' limit 1),
       (select id from "anv".mymodule where code='PAYROLL' limit 1),
       (select id from "anv".property where objectname='cashAdvanceReport' limit 1)) on conflict do nothing;

insert into "anv".container_item (isactive, modulecode, sorder, containerid, moduleid, propertyid)
values(true, 'payroll', 2, (select id from "anv".container where preparedview='payrollReportsHome' limit 1),
       (select id from "anv".mymodule where code='PAYROLL' limit 1),
       (select id from "anv".property where objectname='salaryReport' limit 1)) on conflict do nothing;

insert into "anv".container_item (isactive, modulecode, sorder, containerid, moduleid, propertyid)
values(true, 'payroll', 3, (select id from "anv".container where preparedview='payrollReportsHome' limit 1),
       (select id from "anv".mymodule where code='PAYROLL' limit 1),
       (select id from "anv".property where objectname='wpsReport' limit 1)) on conflict do nothing;

insert into "anv".container_item (isactive, modulecode, sorder, containerid, moduleid, propertyid)
values(true, 'payroll', 4, (select id from "anv".container where preparedview='payrollReportsHome' limit 1),
       (select id from "anv".mymodule where code='PAYROLL' limit 1),
       (select id from "anv".property where objectname='endOfServiceReport' limit 1)) on conflict do nothing;

insert into "anv".container_item (isactive, modulecode, sorder, containerid, moduleid, propertyid)
values(true, 'payroll', 5, (select id from "anv".container where preparedview='payrollReportsHome' limit 1),
       (select id from "anv".mymodule where code='PAYROLL' limit 1),
       (select id from "anv".property where objectname='pensionContributionReport' limit 1)) on conflict do nothing;

insert into "anv".container_item (isactive, modulecode, sorder, containerid, moduleid, propertyid)
values(true, 'payroll', 1, (select id from "anv".container where preparedview='myPayrollHome' limit 1),
       (select id from "anv".mymodule where code='PAYROLL' limit 1),
       (select id from "anv".property where objectname='singlePayrunList' limit 1)) on conflict do nothing;

insert into "anv".container_item (isactive, modulecode, sorder, containerid, moduleid, propertyid)
values(true, 'payroll', 2, (select id from "anv".container where preparedview='myPayrollHome' limit 1),
       (select id from "anv".mymodule where code='PAYROLL' limit 1),
       (select id from "anv".property where objectname='cashadvanceList' limit 1)) on conflict do nothing;

insert into "anv".container_item (isactive, modulecode, sorder, containerid, moduleid, propertyid)
values(true, 'payroll', 2, (select id from "anv".container where preparedview='myPayrollHome' limit 1),
       (select id from "anv".mymodule where code='PAYROLL' limit 1),
       (select id from "anv".property where objectname='additionalpaymentItemList' limit 1)) on conflict do nothing;