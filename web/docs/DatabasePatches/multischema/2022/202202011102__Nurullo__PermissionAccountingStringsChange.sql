delete
from permission
where code = 'LOGISTICS_PURCHASE_ORDER_GOODS_RECEIVED_NOTE';
delete
from permission
where code = 'LOGISTICS_PURCHASE_ORDER_COPY_PI';
delete
from permission
where code = 'LOGISTICS_PURCHASE_ORDER_COPY_SQ';


update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_SALES_QUOTE_LIST';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_SALES_ORDER_LIST';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_SALES_INVOICE_LIST';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_PURCHASE_ORDER_LIST';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_FIXED_ASSET_LIST';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_EXPENSE_REPORT_LIST';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_CUSTOMER_LIST';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_SUPPLIER_LIST';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_PRODUCT_LIST';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_INVENTORY_LIST';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_BANK_ACCOUNT_LIST';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_BANK_ACCOUNT_SPEND';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_BANK_ACCOUNT_RECEIVE';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_CONTACT_LIST';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_PREPAYMENT_LIST';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_SUPPLIER_CREDIT_LIST';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_MANUAL_JOURNAL_LIST';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_RECEIVE_PAYMENT_LIST';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_PAY_BILL_LIST';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_WAREHOUSE_LIST';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_WAREHOUSE_MENU';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_STOCK_ADJUSTMENT';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_STOCK_TRANSFER';


//ACCOUNTING_MAIN_MENU

    /* Main parent*/
update permission
set sorder=1
where code = 'ACCOUNTING_MAIN_MENU';
/* Sub parent */
update permission
set sorder=2
where code = 'ACCOUNTING_ACCOUNTING_MENU';
/* Parent*/
update permission
set sorder=3
where code = 'ACCOUNTING_SALES_QUOTE_LIST';
/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'),
    name='Add'
where code = 'ACCOUNTING_SALES_QUOTE_ADD';
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'),
    name='Edit'
where code = 'ACCOUNTING_SALES_QUOTE_EDIT';
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'),
    name='Delete'
where code = 'ACCOUNTING_SALES_QUOTE_DELETE';
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'),
    name='Summary'
where code = 'ACCOUNTING_SALES_QUOTE_SUMMARY';
/* Parent*/
update permission
set sorder=4
where code = 'ACCOUNTING_SALES_ORDER_LIST';
/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='Add'
where code = 'ACCOUNTING_SALES_ORDER_ADD';
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='Edit'
where code = 'ACCOUNTING_SALES_ORDER_EDIT';
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='Delete'
where code = 'ACCOUNTING_SALES_ORDER_DELETE';
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='Summary'
where code = 'ACCOUNTING_SALES_ORDER_SUMMARY';
/* Parent*/
update permission
set sorder=5
where code = 'ACCOUNTING_SALES_INVOICE_LIST';
/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='Add'
where code = 'ACCOUNTING_SALES_INVOICE_ADD';
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='Edit'
where code = 'ACCOUNTING_SALES_INVOICE_EDIT';
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='Delete'
where code = 'ACCOUNTING_SALES_INVOICE_DELETE';
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='Summary'
where code = 'ACCOUNTING_SALES_INVOICE_SUMMARY';
/* Parent*/
update permission
set sorder=6
where code = 'ACCOUNTING_RECURRING_INVOICE_LIST';
/* Parent*/
update permission
set sorder=7
where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST';
/* Parent*/
update permission
set sorder=8
where code = 'ACCOUNTING_PURCHASE_ORDER_LIST';
/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),
    name='Add'
where code = 'ACCOUNTING_PURCHASE_ORDER_ADD';
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),
    name='Edit'
where code = 'ACCOUNTING_PURCHASE_ORDER_EDIT';
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),
    name='Delete'
where code = 'ACCOUNTING_PURCHASE_ORDER_DELETE';
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),
    name='Summary'
where code = 'ACCOUNTING_PURCHASE_ORDER_SUMMARY';
/* Parent*/
update permission
set sorder=9
where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST';
/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'),
    name='Add'
where code = 'ACCOUNTING_PURCHASE_INVOICE_ADD';
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'),
    name='Edit'
where code = 'ACCOUNTING_PURCHASE_INVOICE_EDIT';
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'),
    name='Delete'
where code = 'ACCOUNTING_PURCHASE_INVOICE_DELETE';
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'),
    name='Summary'
where code = 'ACCOUNTING_PURCHASE_INVOICE_SUMMARY';
/* Parent*/
update permission
set sorder=10
where code = 'ACCOUNTING_RECURRING_BILL_LIST';
/* Parent*/
update permission
set sorder=11
where code = 'ACCOUNTING_FIXED_ASSET_LIST';
/* Parent*/
update permission
set sorder=12
where code = 'ACCOUNTING_EXPENSE_REPORT_LIST';
/* Parent*/
update permission
set sorder=13
where code = 'ACCOUNTING_CUSTOMER_LIST';
/* Parent*/
update permission
set sorder=14
where code = 'ACCOUNTING_SUPPLIER_LIST';
/* Parent*/
update permission
set sorder=15
where code = 'ACCOUNTING_PRODUCT_LIST';
/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'),
    name='Add'
where code = 'ACCOUNTING_PRODUCT_ADD';
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'),
    name='Add'
where code = 'ACCOUNTING_PRODUCT_EDIT';
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'),
    name='Add'
where code = 'ACCOUNTING_PRODUCT_DELETE';
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'),
    name='Add'
where code = 'ACCOUNTING_PRODUCT_SUMMARY';
update permission
set sorder= 5,
    parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'),
    name='Add'
where code = 'ACCOUNTING_PRODUCT_QUICK_ADD';
/* Parent*/
update permission
set sorder=16
where code = 'ACCOUNTING_INVENTORY_LIST';
/* Parent*/
update permission
set sorder=17
where code = 'ACCOUNTING_BANK_ACCOUNT_LIST';
/* Parent*/
update permission
set sorder=18
where code = 'ACCOUNTING_BANK_ACCOUNT_SPEND';
/* Parent*/
update permission
set sorder=19
where code = 'ACCOUNTING_BANK_ACCOUNT_RECEIVE';
/* Parent*/
update permission
set sorder=20
where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT';
/* Parent*/
update permission
set sorder= 21
where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT';
/* Children*/
update permission
set sorder= 5,
    parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT'),
    name='Consignment List'
where code = 'ACCOUNTING_CONSIGNMENT_LIST_VIEW';
/* Parent*/
update permission
set sorder= 22
where code = 'ACCOUNTING_CONTACT_LIST';
/* Children*/
update permission
set sorder=4,
    parent=(select id from permission where code = 'ACCOUNTING_CONTACT_LIST'),
    name='Events'
where code = 'ACCOUNTING_EVENT_LIST';
update permission
set sorder=5,
    parent=(select id from permission where code = 'ACCOUNTING_CONTACT_LIST'),
    name='Cases'
where code = 'ACCOUNTING_CASE_LIST';
update permission
set sorder=6,
    parent=(select id from permission where code = 'ACCOUNTING_CONTACT_LIST'),
    name='Tasks'
where code = 'ACCOUNTING_TASK_LIST';
update permission
set sorder=7,
    parent=(select id from permission where code = 'ACCOUNTING_CONTACT_LIST'),
    name='Issues'
where code = 'ACCOUNTING_ISSUE_LIST';
update permission
set sorder=8,
    parent=(select id from permission where code = 'ACCOUNTING_CONTACT_LIST'),
    name='Skip Department Validation'
where code = 'SKIP_DEPARTMENT_ITEM_VALIDATION';
/* Parent*/
update permission
set sorder= 23
where code = 'ACCOUNTING_TRANSACTION_MENU';
/* Parent*/
update permission
set sorder= 24
where code = 'ACCOUNTING_PREPAYMENT_LIST';
/* Parent*/
update permission
set sorder= 25
where code = 'ACCOUNTING_SUPPLIER_CREDIT_LIST';
/* Children*/
update permission
set sorder= 10,
    parent=(select id from permission where code = 'ACCOUNTING_SUPPLIER_CREDIT_LIST'),
    name='Check List'
where code = 'ACCOUNTING_CHECK_LIST';
/* Parent*/
update permission
set sorder=26
where code = 'ACCOUNTING_MANUAL_JOURNAL_LIST';
/* Parent*/
update permission
set sorder=27
where code = 'ACCOUNTING_RECEIVE_PAYMENT_LIST';
/* Parent*/
update permission
set sorder=28
where code = 'ACCOUNTING_PAY_BILL_LIST';
/* Parent*/
update permission
set sorder=29
where code = 'ACCOUNTING_REPORTS_MENU';
/* Parent*/
update permission
set sorder=30
where code = 'ACCOUNTING_WAREHOUSE_LIST';
/* Parent*/
update permission
set sorder= 31
where code = 'ACCOUNTING_WAREHOUSE_MENU';
/* Children*/
update permission
set sorder=1,
    parent=(select id from permission where code = 'ACCOUNTING_WAREHOUSE_MENU'),
    name='Warehouse Owner'
where code = 'WAREHOUSE_OWNER';
update permission
set sorder=2,
    parent=(select id from permission where code = 'ACCOUNTING_WAREHOUSE_MENU'),
    name='Full List Access'
where code = 'ACCOUNTING_WAREHOUSE_FULL_LIST_ACCESS';
update permission
set sorder=3,
    parent=(select id from permission where code = 'ACCOUNTING_WAREHOUSE_MENU'),
    name='Full List Access'
where code = 'WAREHOUSE_SEE_OWN';
/* Parent*/
update permission
set sorder= 32
where code = 'ACCOUNTING_STOCK_ADJUSTMENT';
/* Parent*/
update permission
set sorder= 33
where code = 'ACCOUNTING_STOCK_TRANSFER';






