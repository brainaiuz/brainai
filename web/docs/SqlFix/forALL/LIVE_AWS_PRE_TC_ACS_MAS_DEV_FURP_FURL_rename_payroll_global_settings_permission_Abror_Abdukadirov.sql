update permission set name = 'Payroll Global Settings List' where code = 'PAYROLL_PAYMENT_DEDUCATION_LIST'
                                                              and parent = (select id from permission where code = 'PAYROLL_SETTINGS');
