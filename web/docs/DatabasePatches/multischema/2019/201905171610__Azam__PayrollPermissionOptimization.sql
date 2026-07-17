update permission set sorder=1, parent=(select id from permission where code='PAYROLL_MAIN_MENU') where code='PAYROLL_MAIN_CONTENT';

    update permission set sorder=1, parent=(select id from permission where code='PAYROLL_MAIN_CONTENT') where code='PAYROLL_EMPLOYEES_LIST';
      update permission set  sorder=1, name='Add', parent=(select id from permission where code='PAYROLL_EMPLOYEES_LIST') where code='PAYROLL_EMPLOYEE_ADD';
      update permission set  sorder=2, name='View', parent=(select id from permission where code='PAYROLL_EMPLOYEES_LIST') where code='PAYROLL_EMPLOYEES_PAYROLL_SETTINGS';
      update permission set  sorder=3, name='Full Access', parent=(select id from permission where code='PAYROLL_EMPLOYEES_LIST') where code='PAYROLL_EMPLOYEES_FULL_ACCESS';
      update permission set  sorder=4, parent=(select id from permission where code='PAYROLL_EMPLOYEES_LIST') where code='PAYROLL_EMPLOYEE_BASIC_SALARY';
      update permission set  sorder=5, parent=(select id from permission where code='PAYROLL_EMPLOYEES_LIST') where code='PAYROLL_EMPLOYEES_PAYSLIPS';
      update permission set  sorder=6, name='Approval', parent=(select id from permission where code='PAYROLL_EMPLOYEES_LIST') where code='PAYROLL_EMPLOYEE_APPROVAL';

    update permission set sorder=2, name='Single Payruns', parent=(select id from permission where code='PAYROLL_MAIN_CONTENT') where code='PAYROLL_PAYSLIP_LIST';
      update permission set  sorder=1, name='View', parent=(select id from permission where code='PAYROLL_PAYSLIP_LIST') where code='PAYROLL_PAYSLIP_VIEW';
      update permission set  sorder=2, name='Add', parent=(select id from permission where code='PAYROLL_PAYSLIP_LIST') where code='PAYROLL_PAYSLIP_ADD';
      update permission set  sorder=3, name='Edit', parent=(select id from permission where code='PAYROLL_PAYSLIP_LIST') where code='PAYROLL_PAYSLIP_EDIT';
      update permission set  sorder=4, name='Delete', parent=(select id from permission where code='PAYROLL_PAYSLIP_LIST') where code='PAYROLL_PAYSLIP_DELETE';
      update permission set  sorder=5, name='Update', parent=(select id from permission where code='PAYROLL_PAYSLIP_LIST') where code='PAYROLL_PAYSLIP_UPDATE';
      update permission set  sorder=6, name='Full Access', parent=(select id from permission where code='PAYROLL_PAYSLIP_LIST') where code='PAYROLL_SINGLE_PAYRUN_FULL_ACCESS';
      update permission set  sorder=7, parent=(select id from permission where code='PAYROLL_PAYSLIP_LIST') where code='PAYROLL_PAYSLIP_EDITABLE';
      update permission set  sorder=8, parent=(select id from permission where code='PAYROLL_PAYSLIP_LIST') where code='PAYROLL_PAYSLIP_PDF';

    update permission set sorder=3, name='Group Payruns', parent=(select id from permission where code='PAYROLL_MAIN_CONTENT') where code='PAYROLL_GROUP_PAYRUN_LIST';
      update permission set sorder=1, name='Add', parent=(select id from permission where code='PAYROLL_GROUP_PAYRUN_LIST') where code='PAYROLL_GROUP_PAYRUN_ADD';
      update permission set sorder=2, name='View', parent=(select id from permission where code='PAYROLL_GROUP_PAYRUN_LIST') where code='PAYROLL_GROUP_PAYRUN_VIEW';
      update permission set sorder=3, name='Edit', parent=(select id from permission where code='PAYROLL_GROUP_PAYRUN_LIST') where code='PAYROLL_GROUP_PAYRUN_EDIT';
      update permission set sorder=4, name='Full Access', parent=(select id from permission where code='PAYROLL_GROUP_PAYRUN_LIST') where code='PAYROLL_GROUP_PAYRUN_FULL_ACCESS';
      update permission set sorder=5, name='Approve Payslip/Cash Advance', parent=(select id from permission where code='PAYROLL_GROUP_PAYRUN_LIST') where code='PAYROLL_CAN_APPROVE_PAYSLIP';
      update permission set sorder=6, name='Update', parent=(select id from permission where code='PAYROLL_GROUP_PAYRUN_LIST') where code='PAYROLL_GROUP_PAYRUN_UPDATES';
      update permission set sorder=7, parent=(select id from permission where code='PAYROLL_GROUP_PAYRUN_LIST') where code='PAYROLL_GROUP_PAYRUN_LIST_EXCEL';

    update permission set sorder=4, name='Cash Advance/Loan', parent=(select id from permission where code='PAYROLL_MAIN_CONTENT') where code='PAYROLL_CASH_ADVANCE_LIST';
      update permission set sorder=1, name='View', parent=(select id from permission where code='PAYROLL_CASH_ADVANCE_LIST') where code='PAYROLL_CASH_ADVANCE_VIEW';
        update permission set sorder=1, name='Payment Delete', parent=(select id from permission where code='PAYROLL_CASH_ADVANCE_VIEW') where code='CASH_ADVANCE_PAYMENT_DELETE';
      update permission set sorder=2, name='Add', parent=(select id from permission where code='PAYROLL_CASH_ADVANCE_LIST') where code='PAYROLL_CASH_ADVANCE_ADD';
      update permission set sorder=3, name='Edit', parent=(select id from permission where code='PAYROLL_CASH_ADVANCE_LIST') where code='PAYROLL_CASH_ADVANCE_EDIT';
      update permission set sorder=4, name='Delete', parent=(select id from permission where code='PAYROLL_CASH_ADVANCE_LIST') where code='PAYROLL_CASH_ADVANCE_DELETE';
      update permission set sorder=5, name='Update', parent=(select id from permission where code='PAYROLL_CASH_ADVANCE_LIST') where code='PAYROLL_CASH_ADVANCE_UPDATES';
      update permission set sorder=6, name='Post', parent=(select id from permission where code='PAYROLL_CASH_ADVANCE_LIST') where code='PAYROLL_POST_TRANSACTION';
      update permission set sorder=7, name='Full Access', parent=(select id from permission where code='PAYROLL_CASH_ADVANCE_LIST') where code='PAYROLL_CASH_ADVANCE_FULL_ACCESS';

    update permission set sorder=5, name='Additional Payment', parent=(select id from permission where code='PAYROLL_MAIN_CONTENT') where code='PAYROLL_ADDITIONAL_PAYMENT_LIST';
      update permission set sorder=1, name='View', parent=(select id from permission where code='PAYROLL_ADDITIONAL_PAYMENT_LIST') where code='PAYROLL_ADDITIONAL_PAYMENT_VIEW';
      update permission set sorder=2, name='Deduction Add', parent=(select id from permission where code='PAYROLL_ADDITIONAL_PAYMENT_LIST') where code='PAYROLL_ADDITIONAL_DEDUCTION_ADD';
      update permission set sorder=3, name='Add', parent=(select id from permission where code='PAYROLL_ADDITIONAL_PAYMENT_LIST') where code='PAYROLL_ADDITIONAL_PAYMENT_ADD';
      update permission set sorder=4, name='Edit', parent=(select id from permission where code='PAYROLL_ADDITIONAL_PAYMENT_LIST') where code='PAYROLL_ADDITIONAL_PAYMENT_EDIT';
      update permission set sorder=5, name='Delete', parent=(select id from permission where code='PAYROLL_ADDITIONAL_PAYMENT_LIST') where code='PAYROLL_ADDITIONAL_PAYMENT_DELETE';
      update permission set sorder=6, name='PDF', parent=(select id from permission where code='PAYROLL_ADDITIONAL_PAYMENT_LIST') where code='PAYROLL_ADDITIONAL_PAYMENT_PDF';

    update permission set sorder=6, name='End Of Service Gratuity', parent=(select id from permission where code='PAYROLL_MAIN_CONTENT') where code='END_OF_SERVICE_GRATUITY_LIST';
      update permission set sorder=1, name='Add', parent=(select id from permission where code='END_OF_SERVICE_GRATUITY_LIST') where code='END_OF_SERVICE_GRATUITY_ADD';
      update permission set sorder=2, name='Edit', parent=(select id from permission where code='END_OF_SERVICE_GRATUITY_LIST') where code='END_OF_SERVICE_GRATUITY_EDIT';
      update permission set sorder=3, name='Delete', parent=(select id from permission where code='END_OF_SERVICE_GRATUITY_LIST') where code='END_OF_SERVICE_GRATUITY_DELETE';

update permission set sorder=2, parent=(select id from permission where code='PAYROLL_MAIN_MENU') where code='PAYROLL_REPORTS';
  update permission set sorder=1, parent=(select id from permission where code='PAYROLL_REPORTS') where code='PAYROLL_WPS_REPORT';
  update permission set sorder=2, parent=(select id from permission where code='PAYROLL_REPORTS') where code='PAYROLL_END_OF_SERVICE_REPORT';
  update permission set sorder=3, parent=(select id from permission where code='PAYROLL_REPORTS') where code='PAYROLL_PENSION_CONTRIBUTION_REPORT';