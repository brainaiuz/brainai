update permission set sorder = 1, parent=(select id from permission where code='ACCOUNTING_EXPENSE_REPORT_LIST') where code='ACCOUNTING_EXPENSE_REPORT_ADD';
update permission set sorder = 2, parent=(select id from permission where code='ACCOUNTING_EXPENSE_REPORT_LIST') where code='ACCOUNTING_EXPENSE_REPORT_EDIT';
update permission set sorder = 3, parent=(select id from permission where code='ACCOUNTING_EXPENSE_REPORT_LIST') where code='ACCOUNTING_EXPENSE_REPORT_DELETE';
update permission set sorder = 4, parent=(select id from permission where code='ACCOUNTING_EXPENSE_REPORT_LIST') where code='ACCOUNTING_EXPENSE_REPORT_SUMMARY';
update permission set sorder = 5, parent=(select id from permission where code='ACCOUNTING_EXPENSE_REPORT_LIST') where code='ACCOUNTING_EXPENSE_REPORT_VOID';
update permission set sorder = 6, parent=(select id from permission where code='ACCOUNTING_EXPENSE_REPORT_LIST') where code='ACCOUNTING_EXPENSE_CLAIM_ADD_CATEGORY';
update permission set sorder = 7, parent=(select id from permission where code='ACCOUNTING_EXPENSE_REPORT_LIST') where code='ACCOUNTING_EXPENSE_REPORT_ADD_TO_STAFF';
update permission set sorder = 8, parent=(select id from permission where code='ACCOUNTING_EXPENSE_REPORT_LIST') where code='ACCOUNTING_EXPENSE_FULL_LIST_ACCESS';