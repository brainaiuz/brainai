update permission
set parent=(select id from permission where code = 'PAYROLL_MAIN_MENU')
where code = 'PAYROLL_MAIN_CONTENT';
update permission
set parent=(select id from permission where code = 'PAYROLL_MAIN_MENU')
where code = 'PAYROLL_EMPLOYEES_LIST';
update permission
set parent=(select id from permission where code = 'PAYROLL_MAIN_MENU')
where code = 'PAYROLL_PAYSLIP_LIST';
update permission
set parent=(select id from permission where code = 'PAYROLL_MAIN_MENU')
where code = 'PAYROLL_GROUP_PAYRUN_LIST';
update permission
set parent=(select id from permission where code = 'PAYROLL_MAIN_MENU')
where code = 'PAYROLL_CASH_ADVANCE_LIST';
update permission
set parent=(select id from permission where code = 'PAYROLL_MAIN_MENU')
where code = 'PAYROLL_ADDITIONAL_PAYMENT_ITEM_LIST';
update permission
set parent=(select id from permission where code = 'PAYROLL_MAIN_MENU')
where code = 'PAYROLL_ADDITIONAL_PAYMENT_LIST';
update permission
set parent=(select id from permission where code = 'PAYROLL_MAIN_MENU')
where code = 'END_OF_SERVICE_GRATUITY_LIST';
update permission
set parent=(select id from permission where code = 'PAYROLL_MAIN_MENU')
where code = 'PAYROLL_REPORTS';
update permission
set parent=(select id from permission where code = 'PAYROLL_MAIN_MENU')
where code = 'PAYROLL_PENDING_CHANGES';

update permission
set sorder = 1
where code = 'PAYROLL_MAIN_CONTENT';
update permission
set sorder = 2
where code = 'PAYROLL_EMPLOYEES_LIST';
update permission
set sorder = 3
where code = 'PAYROLL_PAYSLIP_LIST';
update permission
set sorder = 4
where code = 'PAYROLL_GROUP_PAYRUN_LIST';
update permission
set sorder = 5
where code = 'PAYROLL_CASH_ADVANCE_LIST';
update permission
set sorder = 6
where code = 'PAYROLL_ADDITIONAL_PAYMENT_ITEM_LIST';
update permission
set sorder = 7
where code = 'PAYROLL_ADDITIONAL_PAYMENT_LIST';
update permission
set sorder = 8
where code = 'END_OF_SERVICE_GRATUITY_LIST';
update permission
set sorder = 9
where code = 'PAYROLL_PENDING_CHANGES';
update permission
set sorder = 10
where code = 'PAYROLL_REPORTS';

update permission
set sorder=7,
    parent=(select id from permission where code = 'PAYROLL_CASH_ADVANCE_LIST'),
    name='Payment Delete'
where code = 'CASH_ADVANCE_PAYMENT_DELETE';

update permission
set sorder=7,
    parent=(select id from permission where code = 'PAYROLL_EMPLOYEES_LIST'),
    name='Payment Delete'
where code = 'PAYROLL_SETTINGS_EMPLOYER_CONTRIBUTION';
