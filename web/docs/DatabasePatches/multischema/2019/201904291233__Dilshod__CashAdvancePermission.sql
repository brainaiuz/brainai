update permission set iscore=false,modulecode='PAYROLL',isadvancedmode=false where code in('PAYROLL_CASH_ADVANCE_LIST','PAYROLL_CASH_ADVANCE_ADD','PAYROLL_CASH_ADVANCE_VIEW','PAYROLL_CASH_ADVANCE_EDIT','PAYROLL_CASH_ADVANCE_DELETE','PAYROLL_CASH_ADVANCE_UPDATES');


delete from "0".rolepermission where permissioncode='HRMS_CASH_ADVANCE_LIST';
delete from "0".permission_context where permissioncode='HRMS_CASH_ADVANCE_LIST';

insert  into "0".permission_context(permissioncode,contextcode) values ('PAYROLL_CASH_ADVANCE_LIST','HRMS');
insert  into "0".permission_context(permissioncode,contextcode) values ('PAYROLL_CASH_ADVANCE_ADD','HRMS');
insert  into "0".permission_context(permissioncode,contextcode) values ('PAYROLL_CASH_ADVANCE_VIEW','HRMS');
insert  into "0".permission_context(permissioncode,contextcode) values ('PAYROLL_CASH_ADVANCE_EDIT','HRMS');
insert  into "0".permission_context(permissioncode,contextcode) values ('PAYROLL_CASH_ADVANCE_DELETE','HRMS');
insert  into "0".permission_context(permissioncode,contextcode) values ('PAYROLL_CASH_ADVANCE_UPDATES','HRMS');

delete from "anv".rolepermission where permissioncode='HRMS_CASH_ADVANCE_LIST';
delete from "anv".permission_context where permissioncode='HRMS_CASH_ADVANCE_LIST';

insert  into "anv".permission_context(permissioncode,contextcode) values ('PAYROLL_CASH_ADVANCE_LIST','HRMS');
insert  into "anv".permission_context(permissioncode,contextcode) values ('PAYROLL_CASH_ADVANCE_ADD','HRMS');
insert  into "anv".permission_context(permissioncode,contextcode) values ('PAYROLL_CASH_ADVANCE_VIEW','HRMS');
insert  into "anv".permission_context(permissioncode,contextcode) values ('PAYROLL_CASH_ADVANCE_EDIT','HRMS');
insert  into "anv".permission_context(permissioncode,contextcode) values ('PAYROLL_CASH_ADVANCE_DELETE','HRMS');
insert  into "anv".permission_context(permissioncode,contextcode) values ('PAYROLL_CASH_ADVANCE_UPDATES','HRMS');
