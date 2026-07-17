

update permission set modulecode='PURCHASE_INVOICING' where code='ACCOUNTING_PURCHASE_INVOICE_PDF';
update permission set modulecode='PURCHASE_INVOICING' where code='ACCOUNTING_PURCHASE_INVOICE_FULL_EDIT_ACCESS';
update permission set modulecode='PURCHASE_INVOICING' where code='ACCOUNTING_PURCHASE_CREDIT_NOTE_EDIT';
update permission set modulecode='PURCHASE_INVOICING' where code='ACCOUNTING_PURCHASE_CREDIT_NOTE_FULL_EDIT_ACCESS';


update permission set modulecode='SALES_QUOTES' where code='ACCOUNTING_SALES_QUOTE_FULL_LIST_ACCESS';
update permission set modulecode='SALES_QUOTES' where code='ACCOUNTING_SALES_QUOTE_FULL_EDIT_ACCESS';
update permission set modulecode='SALES_QUOTES' where code='CONVERT_SALE_QUOTE_TO_SALE_INVOICE';
update permission set modulecode='SALES_QUOTES' where code='ACCOUNTING_CAN_APPROVE_SALES_QUOTE';

update permission set modulecode='EXPENSE_REPORTING' where code='ACCOUNTING_EXPENSE_REPORT_ADD';
update permission set modulecode='EXPENSE_REPORTING' where code='ACCOUNTING_EXPENSE_REPORT_EDIT';
update permission set modulecode='EXPENSE_REPORTING' where code='ACCOUNTING_EXPENSE_REPORT_DELETE';
update permission set modulecode='EXPENSE_REPORTING' where code='ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM';
update permission set modulecode='EXPENSE_REPORTING' where code='ACCOUNTING_EXPENSE_REPORT_VOID';
update permission set modulecode='EXPENSE_REPORTING' where code='ACCOUNTING_EXPENSE_REPORT_ADD_TO_STAFF';
update permission set modulecode='EXPENSE_REPORTING' where code='ACCOUNTING_EXPENSE_FULL_LIST_ACCESS';
update permission set modulecode='EXPENSE_REPORTING' where code='ACCOUNTING_EXPENSE_CLAIM_ADD_CATEGORY';
update permission set modulecode='EXPENSE_REPORTING' where code='ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM_DOUBLE_APPROVE';
update permission set modulecode='EXPENSE_REPORTING' where code='ACCOUNTING_COMPANY_EXPENSE_ADD';

update permission set modulecode='REQUEST_FOR_PURCHASES' where code='ACCOUNTING_REQUEST_FOR_PURCHASE_APPROVE';
update permission set modulecode='REQUEST_FOR_PURCHASES' where code='ACCOUNTING_REQUEST_FOR_PURCHASE_CONVERT';
update permission set modulecode='REQUEST_FOR_PURCHASES' where code='ACCOUNTING_REQUEST_FOR_PURCHASE_ADD';
update permission set modulecode='REQUEST_FOR_PURCHASES' where code='ACCOUNTING_REQUEST_FOR_PURCHASE_EDIT';
update permission set modulecode='REQUEST_FOR_PURCHASES' where code='ACCOUNTING_REQUEST_FOR_PURCHASE_DELETE';

update permission set modulecode='SALES_ORDERS' where code='SAVE_FILTER';
update permission set modulecode='SALES_ORDERS' where code='RESET_FILTER';

update permission set modulecode='SALES_INVOICING' where code='ACCOUNTING_SALES_INVOICE_SEND_EMAIL';
update permission set modulecode='SALES_INVOICING' where code='ACCOUNTING_SALES_CREDIT_NOTE_FULL_EDIT_ACCESS';
update permission set modulecode='SALES_INVOICING' where code='ACCOUNTING_SALES_INVOICE_PDF';
update permission set modulecode='SALES_INVOICING' where code='ACCOUNTING_UNSAVED_SALES_INVOICE_PDF';
update permission set modulecode='SALES_INVOICING' where code='ACCOUNTING_SALES_CREDIT_NOTE_EDIT';
update permission set modulecode='SALES_INVOICING' where code='ACCOUNTING_SALES_INVOICE_COPYTOPO';
update permission set modulecode='SALES_INVOICING' where code='ACCOUNTING_SI_SAVE_APPROVE';
update permission set modulecode='SALES_INVOICING' where code='ACCOUNTING_SI_APPROVE_SENT';

update permission set modulecode='PRODUCTS_SERVICES' where code='HIDE_PRODUCT_PRICE';
update permission set modulecode='PRODUCTS_SERVICES' where code='ACCOUNTING_PRODUCT_COST';

update permission set modulecode='BANK_ACCOUNTS' where code='ACCOUNTING_BANK_ACCOUNT_TRANSFER';
update permission set modulecode='BANK_ACCOUNTS' where code='ACCOUNTING_BANK_ACCOUNT_TRANSACTION_IMPORT';
update permission set modulecode='BANK_ACCOUNTS' where code='ACCOUNTING_BANK_ACCOUNT_TRANSACTIONS';
update permission set modulecode='BANK_ACCOUNTS' where code='ACCOUNTING_BANK_STATEMENT';

update permission set modulecode='ACCOUNTING_CUSTOMER_CENTER' where code='UPDATE_CUSTOMER_CREDIT_LIMIT';
update permission set modulecode='ACCOUNTING_CUSTOMER_CENTER' where code='ACCOUNTING_CUSTOMER_IMPORT';
update permission set modulecode='ACCOUNTING_CUSTOMER_CENTER' where code='ACCOUNTING_CUSTOMER_EXPORT';
update permission set modulecode='ACCOUNTING_CUSTOMER_CENTER' where code='ACCOUNTING_SEE_ALL_CUSTOMERS_LIST';


update permission set modulecode='INVENTORY_MANAGEMENT' where code='ACCOUNTING_STOCK_TRANSFER_LIST';
update permission set modulecode='INVENTORY_MANAGEMENT' where code='ACCOUNTING_STOCK_TRANSFER_ADD';
update permission set modulecode='INVENTORY_MANAGEMENT' where code='ACCOUNTING_STOCK_TRANSFER_BUTTON';
update permission set modulecode='INVENTORY_MANAGEMENT' where code='ACCOUNTING_STOCK_TRANSFER_SEE_OWN';
update permission set modulecode='INVENTORY_MANAGEMENT' where code='ACCOUNTING_STOCK_TRANSFER_SUMMARY';
update permission set modulecode='INVENTORY_MANAGEMENT' where code='ACCOUNTING_STOCK_TRANSFER_DELETE';