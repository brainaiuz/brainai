
update "anv".reference set deleted = true where code = 'EMPLOYEE_PAYSLIP_CATEGORY' and parentid = (select id from "anv".reference where code = 'ET_PAYROLL_MODULE');
update "0".reference set deleted = true where code = 'EMPLOYEE_PAYSLIP_CATEGORY' and parentid = (select id from "0".reference where code = 'ET_PAYROLL_MODULE');