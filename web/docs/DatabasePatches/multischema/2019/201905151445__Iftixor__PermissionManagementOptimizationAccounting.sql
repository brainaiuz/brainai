
update permission set sorder=2, parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU'), name='Accounting' where code='ACCOUNTING_ACCOUNTING_MENU';
update permission set sorder=3, parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU'), name='Transactions' where code='ACCOUNTING_TRANSACTION_MENU';

update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_ACCOUNTING_MENU'), name='Sales Quote List' where code='ACCOUNTING_SALES_QUOTE_LIST';
update permission set sorder=2, parent=(select id from permission where code = 'ACCOUNTING_ACCOUNTING_MENU'), name='Sales Order List' where code='ACCOUNTING_SALES_ORDER_LIST';
update permission set sorder=3, parent=(select id from permission where code = 'ACCOUNTING_ACCOUNTING_MENU'), name='Sales Invoice List' where code='ACCOUNTING_SALES_INVOICE_LIST';
update permission set sorder=4, parent=(select id from permission where code = 'ACCOUNTING_ACCOUNTING_MENU'), name='Recurring Invoice List' where code='ACCOUNTING_RECURRING_INVOICE_LIST';
update permission set sorder=5, parent=(select id from permission where code = 'ACCOUNTING_ACCOUNTING_MENU'), name='Request For Quote List' where code='ACCOUNTING_REQUEST_FOR_QUOTE_LIST';
update permission set sorder=6, parent=(select id from permission where code = 'ACCOUNTING_ACCOUNTING_MENU'), name='Request For Purchase List' where code='ACCOUNTING_REQUEST_FOR_PURCHASE_LIST';

update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'), name='Add' where code='ACCOUNTING_SALES_QUOTE_ADD';
update permission set sorder=2, parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'), name='Edit' where code='ACCOUNTING_SALES_QUOTE_EDIT';
update permission set sorder=3, parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'), name='Delete' where code='ACCOUNTING_SALES_QUOTE_DELETE';
update permission set sorder=4, parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'), name='Summary' where code='ACCOUNTING_SALES_QUOTE_SUMMARY';
update permission set sorder=5, parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'), name='Copy' where code='ACCOUNTING_SALES_QUOTE_COPY';
update permission set sorder=6, parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'), name='Approve/Reject' where code='ACCOUNTING_CAN_APPROVE_SALES_QUOTE';
update permission set sorder=7, parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'), name='PDF' where code='ACCOUNTING_SALES_QUOTE_PDF';
update permission set sorder=8, parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'), name='Convert to Order' where code='CONVERT_SALE_QUOTE_TO_SALE_ORDER';
update permission set sorder=9, parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'), name='Convert to Invoice' where code='CONVERT_SALE_QUOTE_TO_SALE_INVOICE';
update permission set sorder=10, parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'), name='Full List Access' where code='ACCOUNTING_SALES_QUOTE_FULL_LIST_ACCESS';
update permission set sorder=11, parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'), name='Full Edit Access' where code='ACCOUNTING_SALES_QUOTE_FULL_EDIT_ACCESS';

update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'), name='Add' where code='ACCOUNTING_SALES_INVOICE_ADD';
update permission set sorder=2, parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'), name='Edit' where code='ACCOUNTING_SALES_INVOICE_EDIT';
update permission set sorder=3, parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'), name='Delete' where code='ACCOUNTING_SALES_INVOICE_DELETE';
update permission set sorder=4, parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'), name='Void' where code='ACCOUNTING_SALES_INVOICE_VOID';
update permission set sorder=5, parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'), name='Summary' where code='ACCOUNTING_SALES_INVOICE_SUMMARY';
update permission set sorder=6, parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'), name='PDF' where code='ACCOUNTING_SALES_INVOICE_PDF';
update permission set sorder=7, parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'), name='Unsaved invoice PDF' where code='ACCOUNTING_UNSAVED_SALES_INVOICE_PDF';
update permission set sorder=8, parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'), name='Copy' where code='ACCOUNTING_SALES_INVOICE_COPY';
update permission set sorder=9, parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'), name='Copy to PO' where code='ACCOUNTING_SALES_INVOICE_COPYTOPO';
update permission set sorder=10, parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'), name='Full List Access' where code='ACCOUNTING_SALES_INVOICE_FULL_LIST_ACCESS';
update permission set sorder=11, parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'), name='Full Edit Access' where code='ACCOUNTING_SALES_INVOICE_FULL_EDIT_ACCESS';
update permission set sorder=12, parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'), name='Save&Approve Button' where code='ACCOUNTING_SI_SAVE_APPROVE';
update permission set sorder=13, parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'), name='Approve&Sent Email Button' where code='ACCOUNTING_SI_APPROVE_SENT';
update permission set sorder=14, parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'), name='Add Credit Note' where code='ACCOUNTING_SALES_CREDIT_NOTE_ADD';
update permission set sorder=15, parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'), name='Credit Note Edit' where code='ACCOUNTING_SALES_CREDIT_NOTE_EDIT';
update permission set sorder=16, parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'), name='Credit Note Full Edit Access' where code='ACCOUNTING_SALES_CREDIT_NOTE_FULL_EDIT_ACCESS';
update permission set sorder=17, parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'), name='Credit/Debit Note Draft' where code='CREDIT_NOTE_DRAFT';
update permission set sorder=18, parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'), name='Credit/Debit Note Approve' where code='CREDIT_NOTE_APPROVE';
update permission set sorder=19, parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'), name='Receive Payment' where code='ACCOUNTING_RECEIVE_PAYMENT';

update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'), name='Add' where code='ACCOUNTING_SALES_ORDER_ADD';
update permission set sorder=2, parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'), name='Edit' where code='ACCOUNTING_SALES_ORDER_EDIT';
update permission set sorder=3, parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'), name='Delete' where code='ACCOUNTING_SALES_ORDER_DELETE';
update permission set sorder=4, parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'), name='Summary' where code='ACCOUNTING_SALES_ORDER_SUMMARY';
update permission set sorder=5, parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'), name='Pick List' where code='ACCOUNTING_SALES_ORDER_PICKLIST';
update permission set sorder=6, parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'), name='Copy to PO' where code='ACCOUNTING_SALES_ORDER_COPYTOPO';
update permission set sorder=7, parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'), name='PDF' where code='ACCOUNTING_SALES_ORDER_PDF';
update permission set sorder=8, parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'), name='Convert to Project' where code='ACCOUNTING_CONVERT_TO_PROJECT';
update permission set sorder=9, parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'), name='Delete GDN' where code='ACCOUNTING_GDN_DELETE';
update permission set sorder=10, parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'), name='Full List Access' where code='ACCOUNTING_SALES_ORDER_FULL_LIST_ACCESS';
update permission set sorder=11, parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'), name='Full Edit Access' where code='ACCOUNTING_SALES_ORDER_FULL_EDIT_ACCESS';

update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_PURCHASE_LIST'), name='Add' where code='ACCOUNTING_REQUEST_FOR_PURCHASE_ADD';
update permission set sorder=2, parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_PURCHASE_LIST'), name='Edit' where code='ACCOUNTING_REQUEST_FOR_PURCHASE_EDIT';
update permission set sorder=3, parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_PURCHASE_LIST'), name='Delete' where code='ACCOUNTING_REQUEST_FOR_PURCHASE_DELETE';
update permission set sorder=4, parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_PURCHASE_LIST'), name='Convert to PO' where code='ACCOUNTING_REQUEST_FOR_PURCHASE_CONVERT';
update permission set sorder=5, parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_PURCHASE_LIST'), name='Full List Access' where code='ACCOUNTING_REQUEST_FOR_PURCHASE_LIST_FULL_ACCESS';

update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST'), name='Add' where code='ACCOUNTING_REQUEST_FOR_QUOTE_ADD';
update permission set sorder=2, parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST'), name='Edit' where code='ACCOUNTING_REQUEST_FOR_QUOTE_EDIT';
update permission set sorder=3, parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST'), name='Delete' where code='ACCOUNTING_REQUEST_FOR_QUOTE_DELETE';
update permission set sorder=4, parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST'), name='Summary' where code='ACCOUNTING_REQUEST_FOR_QUOTE_SUMMARY';
update permission set sorder=5, parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST'), name='Copy' where code='ACCOUNTING_REQUEST_FOR_QUOTE_COPY';
update permission set sorder=6, parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST'), name='Convert' where code='ACCOUNTING_REQUEST_FOR_QUOTE_CONVERT';
update permission set sorder=7, parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST'), name='Send Quote' where code='SEND_RFQ_QUOTE_NOTE';
update permission set sorder=8, parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST'), name='Item Cost Editable' where code='REQUEST_FOR_QUOTE_CELL_EDITABLE';
update permission set sorder=9, parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST'), name='Full List Access Purchase List' where code='ACCOUNTING_REQUEST_FOR_QUOTE_LIST_FULL_ACCESS';

update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_RECURRING_INVOICE_LIST'), name='Add' where code='ACCOUNTING_RECURRING_INVOICE_ADD';
update permission set sorder=2, parent=(select id from permission where code = 'ACCOUNTING_RECURRING_INVOICE_LIST'), name='Edit' where code='ACCOUNTING_RECURRING_INVOICE_EDIT';
update permission set sorder=3, parent=(select id from permission where code = 'ACCOUNTING_RECURRING_INVOICE_LIST'), name='Delete' where code='ACCOUNTING_RECURRING_INVOICE_DELETE';
update permission set sorder=4, parent=(select id from permission where code = 'ACCOUNTING_RECURRING_INVOICE_LIST'), name='Summary' where code='ACCOUNTING_RECURRING_INVOICE_SUMMARY';
update permission set sorder=5, parent=(select id from permission where code = 'ACCOUNTING_RECURRING_INVOICE_LIST'), name='Copy' where code='ACCOUNTING_RECURRING_INVOICE_COPY';

update permission set sorder=7, parent=(select id from permission where code = 'ACCOUNTING_ACCOUNTING_MENU'), name='Purchase Order List' where code='ACCOUNTING_PURCHASE_ORDER_LIST';
update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'), name='Add' where code='ACCOUNTING_PURCHASE_ORDER_ADD';
update permission set sorder=2, parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'), name='Edit' where code='ACCOUNTING_PURCHASE_ORDER_EDIT';
update permission set sorder=3, parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'), name='Delete' where code='ACCOUNTING_PURCHASE_ORDER_DELETE';
update permission set sorder=4, parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'), name='Summary' where code='ACCOUNTING_PURCHASE_ORDER_SUMMARY';
update permission set sorder=5, parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'), name='Copy' where code='ACCOUNTING_PURCHASE_ORDER_COPY';
update permission set sorder=6, parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'), name='PDF' where code='ACCOUNTING_PURCHASE_ORDER_PDF';
update permission set sorder=7, parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'), name='Approve' where code='ACCOUNTING_CAN_APPROVE_PURCHASE_ORDER';
update permission set sorder=8, parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'), name='Full List Access' where code='ACCOUNTING_PURCHASE_ORDER_FULL_LIST_ACCESS';
update permission set sorder=9, parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'), name='Full Edit Access' where code='ACCOUNTING_PURCHASE_ORDER_FULL_EDIT_ACCESS';
update permission set sorder=11, parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'), name='Mark as Open Button' where code='ACCOUNTING_PURCHASE_ORDER_MARK_AS_OPEN_BUTTON';
update permission set sorder=12, parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'), name='Approve and Send Button' where code='ACCOUNTING_PURCHASE_APPROVE_AND_SEND_BUTTON';
update permission set sorder=13, parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'), name='Save and Approve Button' where code='ACCOUNTING_PURCHASE_SAVE_AND_APPROVE_BUTTON';
update permission set sorder=14, parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'), name='Receive' where code='ACCOUNTING_PURCHASE_ORDER_RECEIVE';
update permission set sorder=8, parent=(select id from permission where code = 'ACCOUNTING_ACCOUNTING_MENU'), name='Purchase Invoice List' where code='ACCOUNTING_PURCHASE_INVOICE_LIST';
update permission set sorder=9, parent=(select id from permission where code = 'ACCOUNTING_ACCOUNTING_MENU'), name='Recurring Bills' where code='ACCOUNTING_RECURRING_BILL_LIST';
update permission set sorder=10, parent=(select id from permission where code = 'ACCOUNTING_ACCOUNTING_MENU'), name='Fixed Asset List' where code='ACCOUNTING_FIXED_ASSET_LIST';

update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_FIXED_ASSET_LIST'), name='Add' where code='ACCOUNTING_FIXED_ASSET_ADD';
update permission set sorder=2, parent=(select id from permission where code = 'ACCOUNTING_FIXED_ASSET_LIST'), name='Edit' where code='ACCOUNTING_FIXED_ASSET_EDIT';
update permission set sorder=3, parent=(select id from permission where code = 'ACCOUNTING_FIXED_ASSET_LIST'), name='Delete' where code='ACCOUNTING_FIXED_ASSET_DELETE';
update permission set sorder=4, parent=(select id from permission where code = 'ACCOUNTING_FIXED_ASSET_LIST'), name='Update Depreciation' where code='ACCOUNTING_FIXED_ASSET_DEPRECIATION_UPDATE';

update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_RECURRING_BILL_LIST'), name='Add' where code='ACCOUNTING_RECURRING_BILL_ADD';
update permission set sorder=2, parent=(select id from permission where code = 'ACCOUNTING_RECURRING_BILL_LIST'), name='Edit' where code='ACCOUNTING_RECURRING_BILL_EDIT';
update permission set sorder=3, parent=(select id from permission where code = 'ACCOUNTING_RECURRING_BILL_LIST'), name='Summary' where code='ACCOUNTING_RECURRING_BILL_SUMMARY';
update permission set sorder=4, parent=(select id from permission where code = 'ACCOUNTING_RECURRING_BILL_LIST'), name='Delete' where code='ACCOUNTING_RECURRING_BILL_DELETE';
update permission set sorder=5, parent=(select id from permission where code = 'ACCOUNTING_RECURRING_BILL_LIST'), name='Copy' where code='ACCOUNTING_RECURRING_BILL_COPY';

update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'), name='Add' where code='ACCOUNTING_PURCHASE_INVOICE_ADD';
update permission set sorder=2, parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'), name='Edit' where code='ACCOUNTING_PURCHASE_INVOICE_EDIT';
update permission set sorder=3, parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'), name='Delete' where code='ACCOUNTING_PURCHASE_INVOICE_DELETE';
update permission set sorder=4, parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'), name='Void' where code='ACCOUNTING_PURCHASE_INVOICE_VOID';
update permission set sorder=5, parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'), name='Summary' where code='ACCOUNTING_PURCHASE_INVOICE_SUMMARY';
update permission set sorder=6, parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'), name='PDF' where code='ACCOUNTING_PURCHASE_INVOICE_PDF';
update permission set sorder=7, parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'), name='Copy' where code='ACCOUNTING_PURCHASE_INVOICE_COPY';
update permission set sorder=8, parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'), name='Full Edit Access' where code='ACCOUNTING_PURCHASE_INVOICE_FULL_EDIT_ACCESS';
update permission set sorder=9, parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'), name='Add Credit Note' where code='ACCOUNTING_PURCHASE_CREDIT_NOTE_ADD';
update permission set sorder=10, parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'), name='Edit Credit Note' where code='ACCOUNTING_PURCHASE_CREDIT_NOTE_EDIT';
update permission set sorder=11, parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'), name='Credit Note Full Edit Access' where code='ACCOUNTING_PURCHASE_CREDIT_NOTE_FULL_EDIT_ACCESS';
update permission set sorder=11, parent=(select id from permission where code = 'ACCOUNTING_ACCOUNTING_MENU'), name='Expense Claim List' where code='ACCOUNTING_EXPENSE_REPORT_LIST';

update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), name='Company Expense List' where code='ACCOUNTING_COMPANY_EXPENSE_LIST';
update permission set sorder=2, parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), name='Add' where code='ACCOUNTING_EXPENSE_REPORT_ADD';
update permission set sorder=3, parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), name='Edit' where code='ACCOUNTING_EXPENSE_REPORT_EDIT';
update permission set sorder=4, parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), name='Delete' where code='ACCOUNTING_EXPENSE_REPORT_DELETE';
update permission set sorder=6, parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), name='Approve' where code='ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM';
update permission set sorder=7, parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), name='Void' where code='ACCOUNTING_EXPENSE_REPORT_VOID';
update permission set sorder=8, parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), name='Add/View Full Access' where code='EXPENSE_ADD_VIEW_FULL_ACCESS';
update permission set sorder=9, parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), name='Add to Staff' where code='ACCOUNTING_EXPENSE_REPORT_ADD_TO_STAFF';
update permission set sorder=10, parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), name='Full List Access' where code='ACCOUNTING_EXPENSE_FULL_LIST_ACCESS';
update permission set sorder=11, parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), name='Add Category' where code='ACCOUNTING_EXPENSE_CLAIM_ADD_CATEGORY';
update permission set sorder=12, parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), name='Accountant Approval' where code='ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM_DOUBLE_APPROVE';
update permission set sorder=13, parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'), name='Import from CSV' where code='SHOW_IMPORT_EXPENCE';

update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_COMPANY_EXPENSE_LIST'), name='Add' where code='ACCOUNTING_COMPANY_EXPENSE_ADD';
update permission set sorder=12, parent=(select id from permission where code = 'ACCOUNTING_ACCOUNTING_MENU'), name='Customer Center' where code='ACCOUNTING_CUSTOMER_LIST';

update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_CUSTOMER_LIST'), name='Add' where code='ACCOUNTING_CUSTOMER_ADD';
update permission set sorder=2, parent=(select id from permission where code = 'ACCOUNTING_CUSTOMER_LIST'), name='Edit' where code='ACCOUNTING_CUSTOMER_EDIT';
update permission set sorder=3, parent=(select id from permission where code = 'ACCOUNTING_CUSTOMER_LIST'), name='Delete' where code='ACCOUNTING_CUSTOMER_DELETE';
update permission set sorder=4, parent=(select id from permission where code = 'ACCOUNTING_CUSTOMER_LIST'), name='Summary' where code='ACCOUNTING_CUSTOMER_SUMMARY';
update permission set sorder=5, parent=(select id from permission where code = 'ACCOUNTING_CUSTOMER_LIST'), name='Import' where code='ACCOUNTING_CUSTOMER_IMPORT';
update permission set sorder=6, parent=(select id from permission where code = 'ACCOUNTING_CUSTOMER_LIST'), name='Export' where code='ACCOUNTING_CUSTOMER_EXPORT';
update permission set sorder=8, parent=(select id from permission where code = 'ACCOUNTING_CUSTOMER_LIST'), name='See All' where code='ACCOUNTING_SEE_ALL_CUSTOMERS_LIST';
update permission set sorder=9, parent=(select id from permission where code = 'ACCOUNTING_CUSTOMER_LIST'), name='Update Credit Limit' where code='UPDATE_CUSTOMER_CREDIT_LIMIT';

update permission set sorder=13, parent=(select id from permission where code = 'ACCOUNTING_ACCOUNTING_MENU'), name='Supplier Center' where code='ACCOUNTING_SUPPLIER_LIST';
update permission set sorder=14, parent=(select id from permission where code = 'ACCOUNTING_ACCOUNTING_MENU'), name='Products/Services List' where code='ACCOUNTING_PRODUCT_LIST';
update permission set sorder=15, parent=(select id from permission where code = 'ACCOUNTING_ACCOUNTING_MENU'), name='Inventory Items' where code='ACCOUNTING_INVENTORY_LIST';
update permission set sorder=16, parent=(select id from permission where code = 'ACCOUNTING_ACCOUNTING_MENU'), name='Bank Accounts' where code='ACCOUNTING_BANK_ACCOUNT_LIST';


update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_LIST'), name='Add' where code='ACCOUNTING_BANK_ACCOUNT_ADD';
update permission set sorder=2, parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_LIST'), name='Edit' where code='ACCOUNTING_BANK_ACCOUNT_EDIT';
update permission set sorder=3, parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_LIST'), name='Delete' where code='ACCOUNTING_BANK_ACCOUNT_DELETE';
update permission set sorder=8, parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_LIST') where code='ACCOUNTING_BANK_ACCOUNT_TRANSFER';
update permission set sorder=9, parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_LIST') where code='ACCOUNTING_BANK_ACCOUNT_TRANSACTION_IMPORT';
update permission set sorder=10, parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_LIST') where code='ACCOUNTING_BANK_ACCOUNT_TRANSACTIONS';
update permission set sorder=11, parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_LIST') where code='ACCOUNTING_BANK_STATEMENT';


update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'), name='Add' where code='ACCOUNTING_PRODUCT_ADD';
update permission set sorder=2, parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'), name='Edit' where code='ACCOUNTING_PRODUCT_EDIT';
update permission set sorder=3, parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'), name='Delete' where code='ACCOUNTING_PRODUCT_DELETE';
update permission set sorder=4, parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'), name='Summary' where code='ACCOUNTING_PRODUCT_SUMMARY';
update permission set sorder=5, parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'), name='Cost' where code='ACCOUNTING_PRODUCT_COST';
update permission set sorder=6, parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'), name='Hide Price' where code='HIDE_PRODUCT_PRICE';
update permission set sorder=7, parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'), name='Build Assembly' where code='ACCOUNTING_BUILD_ASSEMBLY';

update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_SUPPLIER_LIST'), name='Add' where code='ACCOUNTING_SUPPLIER_ADD';
update permission set sorder=2, parent=(select id from permission where code = 'ACCOUNTING_SUPPLIER_LIST'), name='Edit' where code='ACCOUNTING_SUPPLIER_EDIT';
update permission set sorder=3, parent=(select id from permission where code = 'ACCOUNTING_SUPPLIER_LIST'), name='Delete' where code='ACCOUNTING_SUPPLIER_DELETE';
update permission set sorder=4, parent=(select id from permission where code = 'ACCOUNTING_SUPPLIER_LIST'), name='Summary' where code='ACCOUNTING_SUPPLIER_SUMMARY';
update permission set sorder=6, parent=(select id from permission where code = 'ACCOUNTING_SUPPLIER_LIST'), name='Export' where code='ACCOUNTING_SUPPLIER_EXPORT';
update permission set sorder=7, parent=(select id from permission where code = 'ACCOUNTING_SUPPLIER_LIST'), name='Full List Access' where code='ACCOUNTING_SUPPLIER_FULL_LIST_ACCESS';

update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_TRANSACTION_MENU') where code='ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT';

update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT'),name='Add' where code='ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_ADD';
update permission set sorder=2, parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT'),name='Edit' where code='ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_EDIT';
update permission set sorder=3, parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT'),name='Delete' where code='ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_DELETE';
update permission set sorder=4, parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT'),name='Summary' where code='ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_SUMMARY';

update permission set sorder=2, parent=(select id from permission where code = 'ACCOUNTING_TRANSACTION_MENU'), name='Cash Payments' where code='ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT';
update permission set sorder=3, parent=(select id from permission where code = 'ACCOUNTING_TRANSACTION_MENU'),name='Bank Receipts' where code='ACCOUNTING_BANK_ACCOUNT_RECEIVE';
update permission set sorder=4, parent=(select id from permission where code = 'ACCOUNTING_TRANSACTION_MENU'), name='Bank Payments' where code='ACCOUNTING_BANK_ACCOUNT_SPEND';

update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_SPEND'), name='Add' where code='ACCOUNTING_BANK_ACCOUNT_SPEND_ADD';
update permission set sorder=2, parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_SPEND'), name='Edit' where code='ACCOUNTING_BANK_ACCOUNT_SPEND_EDIT';
update permission set sorder=3, parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_SPEND'), name='Delete' where code='ACCOUNTING_BANK_ACCOUNT_SPEND_DELETE';
update permission set sorder=4, parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_SPEND'), name='Summary' where code='ACCOUNTING_BANK_ACCOUNT_SPEND_SUMMARY';

update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_RECEIVE'), name='Add' where code='ACCOUNTING_BANK_ACCOUNT_RECEIVE_ADD';
update permission set sorder=2, parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_RECEIVE'), name='Edit' where code='ACCOUNTING_BANK_ACCOUNT_RECEIVE_EDIT';
update permission set sorder=3, parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_RECEIVE'), name='Delete' where code='ACCOUNTING_BANK_ACCOUNT_RECEIVE_DELETE';
update permission set sorder=4, parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_RECEIVE'), name='Summary' where code='ACCOUNTING_BANK_ACCOUNT_RECEIVE_SUMMARY';

update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT'), name='Add' where code='ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_ADD';
update permission set sorder=2, parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT'), name='Edit' where code='ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_EDIT';
update permission set sorder=3, parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT'), name='Delete' where code='ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_DELETE';
update permission set sorder=4, parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT'), name='Summary' where code='ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_SUMMARY';
update permission set sorder=5, parent=(select id from permission where code = 'ACCOUNTING_TRANSACTION_MENU'), name='Customer Prepayment' where code='ACCOUNTING_PREPAYMENT_LIST';
update permission set sorder=6, parent=(select id from permission where code = 'ACCOUNTING_TRANSACTION_MENU'), name='Supplier Prepayment' where code='ACCOUNTING_SUPPLIER_CREDIT_LIST';
update permission set sorder=7, parent=(select id from permission where code = 'ACCOUNTING_TRANSACTION_MENU'), name='Check List' where code='ACCOUNTING_CHECK_LIST';
update permission set sorder=8, parent=(select id from permission where code = 'ACCOUNTING_TRANSACTION_MENU'), name='Manual Entries' where code='ACCOUNTING_MANUAL_JOURNAL_LIST';
update permission set sorder=9, parent=(select id from permission where code = 'ACCOUNTING_TRANSACTION_MENU'), name='Receive Payments' where code='ACCOUNTING_RECEIVE_PAYMENT_LIST';
update permission set sorder=10, parent=(select id from permission where code = 'ACCOUNTING_TRANSACTION_MENU'), name='Pay Invoices' where code='ACCOUNTING_PAY_BILL_LIST';

update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_PAY_BILL_LIST'), name='Add' where code='ACCOUNTING_PAY_BILL';

update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_RECEIVE_PAYMENT_LIST'), name='Add' where code='ACCOUNTING_RECEIVE_PAYMENT';
update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_MANUAL_JOURNAL_LIST'), name='Add' where code='ACCOUNTING_MANUAL_JOURNAL_ADD';
update permission set sorder=2, parent=(select id from permission where code = 'ACCOUNTING_MANUAL_JOURNAL_LIST'), name='Edit' where code='ACCOUNTING_MANUAL_JOURNAL_EDIT';
update permission set sorder=3, parent=(select id from permission where code = 'ACCOUNTING_MANUAL_JOURNAL_LIST'), name='Delete' where code='ACCOUNTING_MANUAL_JOURNAL_DELETE';
update permission set sorder=4, parent=(select id from permission where code = 'ACCOUNTING_MANUAL_JOURNAL_LIST'), name='Void' where code='ACCOUNTING_MANUAL_JOURNAL_VOID';
update permission set sorder=5, parent=(select id from permission where code = 'ACCOUNTING_MANUAL_JOURNAL_LIST'), name='Summary' where code='ACCOUNTING_MANUAL_JOURNAL_SUMMARY';

update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_PREPAYMENT_LIST'), name='Add' where code='ACCOUNTING_PREPAYMENT_ADD';
update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_SUPPLIER_CREDIT_LIST'), name='Add' where code='ACCOUNTING_SUPPLIER_CREDIT_ADD';
update permission set sorder=4, parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU'), name='Statements' where code='ACCOUNTING_REPORTS_MENU';
update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_REPORTS_MENU'), name='Profit and Loss' where code='ACCOUNTING_PROFIT_AND_LOSS';
update permission set sorder=2, parent=(select id from permission where code = 'ACCOUNTING_REPORTS_MENU'), name='Balance Sheet' where code='ACCOUNTING_BALANCE_SHEET';
update permission set sorder=3, parent=(select id from permission where code = 'ACCOUNTING_REPORTS_MENU'), name='Trial Balance' where code='ACCOUNTING_TRIAL_BALANCE';
update permission set sorder=4, parent=(select id from permission where code = 'ACCOUNTING_REPORTS_MENU'), name='Cash Flow' where code='ACCOUNTING_CASH_FLOW';
update permission set sorder=5, parent=(select id from permission where code = 'ACCOUNTING_REPORTS_MENU'), name='Journal Report' where code='ACCOUNTING_JOURNAL_REPORT';
insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode) VALUES ('ACCOUNTING_AGING_SUMMARY_RECEIVABLE', 'ACCOUNTING', false, 'Aging Summary Receivable', 6 , (select id from permission where code = 'ACCOUNTING_REPORTS_MENU'), 'ACCOUNTING_MODULE');
insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode) VALUES ('ACCOUNTING_AGING_SUMMARY_PAYABLE', 'ACCOUNTING', false, 'Aging Summary Payable', 7 , (select id from permission where code = 'ACCOUNTING_REPORTS_MENU'), 'ACCOUNTING_MODULE');
update permission set sorder=8, parent=(select id from permission where code = 'ACCOUNTING_REPORTS_MENU'), name='Account Transactions' where code='ACCOUNTING_ACCOUNT_TRANSACTIONS';
update permission set sorder=9, parent=(select id from permission where code = 'ACCOUNTING_REPORTS_MENU'), name='Stock Valuation' where code='ACCOUNTING_STOCK_VALUATION';
update permission set sorder=10, parent=(select id from permission where code = 'ACCOUNTING_REPORTS_MENU'), name='Vat Return List' where code='ACCOUNTING_VAT_RETURNS_LIST';
update permission set sorder=11, parent=(select id from permission where code = 'ACCOUNTING_REPORTS_MENU'), name='Vat Return' where code='ACCOUNTING_VAT_RETURN';
update permission set sorder=12, parent=(select id from permission where code = 'ACCOUNTING_REPORTS_MENU') where code='ACCOUNTING_BUDGET_SHEET';

update permission set sorder=5, parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU'), name='Warehouse' where code='ACCOUNTING_WAREHOUSE_MENU';
update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_WAREHOUSE_MENU'), name='Warehouses' where code='ACCOUNTING_WAREHOUSE_LIST';
update permission set sorder=2, parent=(select id from permission where code = 'ACCOUNTING_WAREHOUSE_MENU'), name='Stock Adjustment' where code='ACCOUNTING_STOCK_ADJUSTMENT';
update permission set sorder=3, parent=(select id from permission where code = 'ACCOUNTING_WAREHOUSE_MENU'), name='Stock Transfer' where code='ACCOUNTING_STOCK_TRANSFER';

update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_STOCK_ADJUSTMENT'), name='Stock Adjustment List' where code='ACCOUNTING_STOCK_ADJUSTMENT_LIST';
update permission set sorder=2, parent=(select id from permission where code = 'ACCOUNTING_STOCK_ADJUSTMENT_LIST'), name='Add' where code='ACCOUNTING_STOCK_ADJUSTMENT_ADD';
update permission set sorder=3, parent=(select id from permission where code = 'ACCOUNTING_STOCK_ADJUSTMENT_LIST'), name='Edit' where code='ACCOUNTING_STOCK_ADJUSTMENT_EDIT';
update permission set sorder=4, parent=(select id from permission where code = 'ACCOUNTING_STOCK_ADJUSTMENT_LIST'), name='Delete' where code='ACCOUNTING_STOCK_ADJUSTMENT_DELETE';

update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_STOCK_TRANSFER'), name='Stock Transfer List' where code='ACCOUNTING_STOCK_TRANSFER_LIST';
update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_STOCK_TRANSFER_LIST'), name='Add' where code='ACCOUNTING_STOCK_TRANSFER_ADD';
update permission set sorder=2, parent=(select id from permission where code = 'ACCOUNTING_STOCK_TRANSFER_LIST'), name='Edit' where code='ACCOUNTING_STOCK_TRANSFER_EDIT';
update permission set sorder=3, parent=(select id from permission where code = 'ACCOUNTING_STOCK_TRANSFER_LIST'), name='Summary' where code='ACCOUNTING_STOCK_TRANSFER_SUMMARY';
update permission set sorder=4, parent=(select id from permission where code = 'ACCOUNTING_STOCK_TRANSFER_LIST'), name='Delete' where code='ACCOUNTING_STOCK_TRANSFER_DELETE';
update permission set  name='Customer Name Clickable' where code='CUSTOMER_CLICKABLE';
update permission set sorder=17, parent=(select id from permission where code = 'ACCOUNTING_ACCOUNTING_MENU'), name='Consignment List' where code='ACCOUNTING_CONSIGNMENT_LIST_VIEW';
update permission set sorder=18, parent=(select id from permission where code = 'ACCOUNTING_ACCOUNTING_MENU'), name='Contact List' where code='ACCOUNTING_CONTACT_LIST';
update permission set sorder=19, parent=(select id from permission where code = 'ACCOUNTING_ACCOUNTING_MENU'), name='Event List' where code='ACCOUNTING_EVENT_LIST';
update permission set sorder=20, parent=(select id from permission where code = 'ACCOUNTING_ACCOUNTING_MENU'), name='Case List' where code='ACCOUNTING_CASE_LIST';
update permission set sorder=21, parent=(select id from permission where code = 'ACCOUNTING_ACCOUNTING_MENU'), name='Task List' where code='ACCOUNTING_TASK_LIST';
update permission set sorder=22, parent=(select id from permission where code = 'ACCOUNTING_ACCOUNTING_MENU'), name='Issue List' where code='ACCOUNTING_ISSUE_LIST';
update permission set sorder=23, parent=(select id from permission where code = 'ACCOUNTING_ACCOUNTING_MENU'), name='Skip Department Validation' where code='SKIP_DEPARTMENT_ITEM_VALIDATION';
update permission set sorder=1, parent=(select id from permission where code = 'ACCOUNTING_CONTACT_LIST'), name='Add' where code='ACCOUNTING_CONTACT_ADD';
update permission set sorder=2, parent=(select id from permission where code = 'ACCOUNTING_CONTACT_LIST'), name='Edit' where code='ACCOUNTING_CONTACT_EDIT';
update permission set sorder=3, parent=(select id from permission where code = 'ACCOUNTING_CONTACT_LIST'), name='Delete' where code='ACCOUNTING_CONTACT_DELETE';
update permission set sorder=2, parent=(select id from permission where code = 'ACCOUNTING_PAY_BILL_LIST'), name='Edit' where code='PAY_INVOICE_EDIT';
update permission set sorder=3, parent=(select id from permission where code = 'ACCOUNTING_PAY_BILL_LIST'), name='Delete' where code='PAY_INVOICE_DELETE';

update permission set sorder=2, parent=(select id from permission where code = 'ACCOUNTING_RECEIVE_PAYMENT_LIST'), name='Edit' where code='RECEIVE_PAYMENT_EDIT';
update permission set sorder=3, parent=(select id from permission where code = 'ACCOUNTING_RECEIVE_PAYMENT_LIST'), name='Delete' where code='RECEIVE_PAYMENT_DELETE';


delete from permission where code='ACCOUNTING_FRANCHISE_LIST';
delete from permission where code='ACCOUNTING_FRANCHISE_ADD';
delete from permission where code='ACCOUNTING_FRANCHISE_EDIT';
delete from permission where code='ACCOUNTING_FRANCHISE_DELETE';
delete from permission where code='ACCOUNTING_OPPORTUNITY_LIST';
delete from permission where code='ACCOUNTING_CLIENT_NOTE_LIST';
delete from permission where code='ACCOUNTING_SUPPLIER_NOTE_LIST';
delete from permission where code='ACCOUNTING_STOREFRONT_MENU';
delete from permission where code='ACCOUNTING_RESERVATION_LIST';
delete from permission where code='ACCOUNTING_RESERVATION_ADD';
delete from permission where code='ACCOUNTING_RESERVATION_EDIT';
delete from permission where code='ACCOUNTING_PURCHASE_ORDER_IGNORE_MANAGER_APPROVAL';
delete from permission where code='ACCOUNTING_EXPENSE_REPORT_SUMMARY';
delete from permission where code='ACCOUNTING_ACCOUNTANT_APPROVAL_EXPENCE_CLAIM';
delete from permission where code='ACCOUNTING_COMPANY_EXPENSE_EDIT';
delete from permission where code='ACCOUNTING_COMPANY_EXPENSE_DELETE';
delete from permission where code='ACCOUNTING_COMPANY_EXPENSE_VOID';
delete from permission where code='ACCOUNTING_COMPANY_EXPENSE_LIST_FULL_ACCESS';
delete from permission where code='ACCOUNTING_CUSTOMER_PROJECT_BALANCE';
delete from permission where code='ACCOUNTING_PRODUCT_GROUP_ADD';
delete from permission where code='ACCOUNTING_VARIATION_ADD';
delete from permission where code='ACCOUNTING_VARIATION_DELETE';
delete from permission where code='ACCOUNTING_SUPPLIER_IMPORT';
delete from permission where code='ACCOUNTING_WELCOME_PAGE';
delete from permission where code='MANAGE_KEEP_EX_RATE';
delete from permission where code='KEEP_EX_RATE';
delete from permission where code='ACCOUNTING_CUSTOM_FIELD_EDIT';
delete from permission where code='ACCOUNTING_TAX_RATE_DELETE';
delete from permission where code='ACCOUNTING_TAX_RATE_ADD';
delete from permission where code='ACCOUNTING_TAX_RATE_SUMMARY';
delete from permission where code='ACCOUNTING_TAX_RATE_EDIT';