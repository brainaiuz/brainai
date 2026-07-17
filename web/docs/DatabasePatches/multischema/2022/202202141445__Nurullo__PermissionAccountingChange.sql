update permission
set parent= (select parent from permission where code = 'ACCOUNTING_STOCK_ADJUSTMENT')
where code = 'ACCOUNTING_STOCK_ADJUSTMENT_LIST';
delete
from "anv".rolepermission
where permissioncode = 'ACCOUNTING_STOCK_ADJUSTMENT';
delete
from "anv".permission_context
where permissioncode = 'ACCOUNTING_STOCK_ADJUSTMENT';
delete
from permission
where code = 'ACCOUNTING_STOCK_ADJUSTMENT';


update permission
set parent= (select parent from permission where code = 'ACCOUNTING_STOCK_TRANSFER')
where code = 'ACCOUNTING_STOCK_TRANSFER_LIST';
delete
from "anv".rolepermission
where permissioncode = 'ACCOUNTING_STOCK_TRANSFER';
delete
from "anv".permission_context
where permissioncode = 'ACCOUNTING_STOCK_TRANSFER';
DELETE
FROM PERMISSION
where code = 'ACCOUNTING_STOCK_TRANSFER';


delete
from "anv".rolepermission
where permissioncode = 'ACCOUNTING_PURCHASE_ORDER_MARK_AS_OPEN_BUTTON';
delete
from "anv".permission_context
where permissioncode = 'ACCOUNTING_PURCHASE_ORDER_MARK_AS_OPEN_BUTTON';
DELETE
FROM PERMISSION
where code = 'ACCOUNTING_PURCHASE_ORDER_MARK_AS_OPEN_BUTTON';



update permission
set parent=0
where code = 'ACCOUNTING_MAIN_MENU';



update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_ACCOUNTING_MENU';

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
where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_REQUEST_FOR_PURCHASE_LIST';
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
where code = 'ACCOUNTING_TRANSACTION_MENU';
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
where code = 'ACCOUNTING_REPORTS_MENU';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_WAREHOUSE_LIST';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_WAREHOUSE_MENU';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_RECURRING_INVOICE_LIST';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_RECURRING_BILL_LIST';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_STOCK_ADJUSTMENT_LIST';
update permission
set parent=(select id from permission where code = 'ACCOUNTING_MAIN_MENU')
where code = 'ACCOUNTING_STOCK_TRANSFER_LIST';


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

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'),
    name='Edit'
where code = 'ACCOUNTING_SALES_QUOTE_EDIT';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'),
    name='Delete'
where code = 'ACCOUNTING_SALES_QUOTE_DELETE';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'),
    name='Summary'
where code = 'ACCOUNTING_SALES_QUOTE_SUMMARY';

/* Children*/
update permission
set sorder= 5,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'),
    name='PDF'
where code = 'ACCOUNTING_SALES_QUOTE_PDF';

/* Children*/
update permission
set sorder= 6,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'),
    name='Approve Or Reject'
where code = 'ACCOUNTING_CAN_APPROVE_SALES_QUOTE';

/* Children*/
update permission
set sorder= 7,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'),
    name='Copy To SQ'
where code = 'ACCOUNTING_SALES_QUOTE_COPY';

/* Children*/
update permission
set sorder= 8,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'),
    name='Copy To SO'
where code = 'CONVERT_SALE_QUOTE_TO_SALE_ORDER';

/* Children*/
update permission
set sorder= 9,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'),
    name='Copy To SI'
where code = 'CONVERT_SALE_QUOTE_TO_SALE_INVOICE';

/* Children*/
update permission
set sorder= 10,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'),
    name='Copy to PO'
where code = 'CONVERT_SALE_QUOTE_TO_PURCHASE_ORDER';

/* Children*/
update permission
set sorder= 11,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'),
    name='Full list access'
where code = 'ACCOUNTING_SALES_QUOTE_FULL_LIST_ACCESS';

/* Children*/
update permission
set sorder= 12,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'),
    name='Full edit access'
where code = 'ACCOUNTING_SALES_QUOTE_FULL_EDIT_ACCESS';

/* Children*/
update permission
set sorder= 13,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'),
    name='See Own'
where code = 'SALES_QUOTE_SEE_OWN';

/* Children*/
update permission
set sorder= 14,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'),
    name='Client Approve'
where code = 'SALES_QUOTE_CLIENT_APPROVE';

/* Children*/
update permission
set sorder= 15,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'),
    name='Closed'
where code = 'ACCOUNTING_SALES_QUOTE_CLOSED';

/* Children*/
update permission
set sorder= 16,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'),
    name='Customize Column'
where code = 'ACCOUNTING_SALES_QUOTE_LIST_CUSTOMIZE';

/* Children*/
update permission
set sorder= 17,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'),
    name='Links'
where code = 'ACCOUNTING_SALES_QUOTE_LINKS';

/* Children*/
update permission
set sorder= 18,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'),
    name='Filter'
where code = 'ACCOUNTING_SALES_QUOTE_LIST_FILTER';

/* Children*/
update permission
set sorder= 19,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'),
    name='History & Notes'
where code = 'ACCOUNTING_SALES_QUOTE_HISTORY_NOTES';

/* Children*/
update permission
set sorder= 20,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'),
    name='Upload Files'
where code = 'ACCOUNTING_SALES_QUOTE_UPLOAD_FILES';

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

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='Edit'
where code = 'ACCOUNTING_SALES_ORDER_EDIT';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='Delete'
where code = 'ACCOUNTING_SALES_ORDER_DELETE';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='Summary'
where code = 'ACCOUNTING_SALES_ORDER_SUMMARY';

/* Children*/
update permission
set sorder= 5,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='Pick List'
where code = 'ACCOUNTING_SALES_ORDER_PICKLIST';

/* Children*/
update permission
set sorder= 6,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='Copy To PO'
where code = 'ACCOUNTING_SALES_ORDER_COPYTOPO';

/* Children*/
update permission
set sorder= 7,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='Copy to SQ'
where code = 'ACCOUNTING_SALES_ORDER_COPYTOSQ';

/* Children*/
update permission
set sorder= 8,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='Copy to SO'
where code = 'ACCOUNTING_SALES_ORDER_COPYTOSO';

/* Children*/
update permission
set sorder= 9,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='Convert to Project'
where code = 'ACCOUNTING_CONVERT_TO_PROJECT';

/* Children*/
update permission
set sorder= 10,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='Convert to Sales Invoice'
where code = 'CONVERT_SALE_ORDER_TO_SALE_INVOICE';

/* Children*/
update permission
set sorder= 11,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='Closed'
where code = 'ACCOUNTING_SALES_ORDER_CLOSED';

/* Children*/
update permission
set sorder= 12,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='PDF'
where code = 'ACCOUNTING_SALES_ORDER_PDF';

/* Children*/
update permission
set sorder= 13,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='GDN Delete'
where code = 'ACCOUNTING_GDN_DELETE';

/* Children*/
update permission
set sorder= 14,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='Full list access'
where code = 'ACCOUNTING_SALES_ORDER_FULL_LIST_ACCESS';

/* Children*/
update permission
set sorder= 15,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='Approve/Reject'
where code = 'ACCOUNTING_CAN_APPROVE_SALES_ORDER';

/* Children*/
update permission
set sorder= 16,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='Full edit access'
where code = 'ACCOUNTING_SALES_ORDER_FULL_EDIT_ACCESS';

/* Children*/
update permission
set sorder= 17,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='Go To Picklist, Set ready qty to ship'
where code = 'ACCOUNTING_SET_READY_QTY_TO_SHIP';

/* Children*/
update permission
set sorder= 18,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='Go To Picklist, Enable Shipping Button'
where code = 'ACCOUNTING_ENABLE_SHIPPING_BUTTON';


/* Children*/
update permission
set sorder= 19,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='GDN Convert To SI'
where code = 'ACCOUNTING_GDN_CONVERT_TO_INVOICE';

/* Children*/
update permission
set sorder= 20,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='GDN Save Filter'
where code = 'SAVE_FILTER';

/* Children*/
update permission
set sorder= 21,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='GDN Reset Filter'
where code = 'RESET_FILTER';

/* Children*/
update permission
set sorder= 22,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='See Own'
where code = 'SALES_ORDER_SEE_OWN';

/* Children*/
update permission
set sorder= 23,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='Customize Column'
where code = 'ACCOUNTING_SALES_ORDER_LIST_CUSTOMIZE';

/* Children*/
update permission
set sorder= 24,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='Approve & Email'
where code = 'SALES_ORDER_APPROVE_EMAIL_SEND';

/* Children*/
update permission
set sorder= 25,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='Links'
where code = 'ACCOUNTING_SALES_ORDER_LINKS';

/* Children*/
update permission
set sorder= 26,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='Filter'
where code = 'ACCOUNTING_SALES_ORDER_LIST_FILTER';

/* Children*/
update permission
set sorder= 27,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='History & Notes'
where code = 'ACCOUNTING_SALES_ORDER_HISTORY_NOTES';

/* Children*/
update permission
set sorder= 28,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='Packing List'
where code = 'ACCOUNTING_PACKING_LIST';

/* Children*/
update permission
set sorder= 29,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='Submit & Email'
where code = 'SALES_ORDER_SUBMIT_AND_EMAIL_SEND';

/* Children*/
update permission
set sorder= 30,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),
    name='Upload Files'
where code = 'ACCOUNTING_SALES_ORDER_UPLOAD_FILES';

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

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='Edit'
where code = 'ACCOUNTING_SALES_INVOICE_EDIT';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='Delete'
where code = 'ACCOUNTING_SALES_INVOICE_DELETE';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='Summary'
where code = 'ACCOUNTING_SALES_INVOICE_SUMMARY';

/* Children*/
update permission
set sorder= 5,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='PDF'
where code = 'ACCOUNTING_SALES_INVOICE_PDF';

/* Children*/
update permission
set sorder= 6,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='Void'
where code = 'ACCOUNTING_SALES_INVOICE_VOID';

/* Children*/
update permission
set sorder= 7,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='Credit Note Add'
where code = 'ACCOUNTING_SALES_CREDIT_NOTE_ADD';

/* Children*/
update permission
set sorder= 8,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='Timesheet Invoice Add'
where code = 'ACCOUNTING_TIMESHEET_INVOICE_ADD';

/* Children*/
update permission
set sorder= 9,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='Copy To SI'
where code = 'ACCOUNTING_SALES_INVOICE_COPY';

/* Children*/
update permission
set sorder= 10,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='Copy To PO'
where code = 'ACCOUNTING_SALES_INVOICE_COPYTOPO';

/* Children*/
update permission
set sorder= 11,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='Full list access'
where code = 'ACCOUNTING_SALES_INVOICE_FULL_LIST_ACCESS';

/* SubChildren*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='Paid Status Edit'
where code = 'ACCOUNTING_SALES_INVOICE_PAID_STATUS_EDIT';
/* Children*/
update permission
set sorder= 12,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='Full edit access'
where code = 'ACCOUNTING_SALES_INVOICE_FULL_EDIT_ACCESS';

/* Children*/
update permission
set sorder= 13,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='Note Edit'
where code = 'ACCOUNTING_SALES_CREDIT_NOTE_EDIT';

/* Children*/
update permission
set sorder= 14,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='Credit Note Full Edit Access'
where code = 'ACCOUNTING_SALES_CREDIT_NOTE_FULL_EDIT_ACCESS';

/* Children*/
update permission
set sorder= 15,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='Send email button'
where code = 'ACCOUNTING_SALES_INVOICE_SEND_EMAIL';

/* Children*/
update permission
set sorder= 16,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='Draft'
where code = 'ACCOUNTING_SI_AND_PI_DRAFT_BUTTON';

/* Children*/
update permission
set sorder= 17,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='Credit/Debit Note Approve'
where code = 'CREDIT_NOTE_APPROVE';

/* Children*/
update permission
set sorder= 18,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='Save & Approve'
where code = 'ACCOUNTING_SI_SAVE_APPROVE';

/* Children*/
update permission
set sorder= 19,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='Approve & Email'
where code = 'ACCOUNTING_SI_APPROVE_SENT';

/* Children*/
update permission
set sorder= 20,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='Credit/Debit Note Draft'
where code = 'CREDIT_NOTE_DRAFT';

/* Children*/
update permission
set sorder= 21,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='See Own'
where code = 'SALES_INVOICE_SEE_OWN';

/* Children*/
update permission
set sorder= 22,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='Approve/Reject'
where code = 'ACCOUNTING_CAN_APPROVE_SALES_INVOICE';

/* Children*/
update permission
set sorder= 23,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='Customize Column'
where code = 'ACCOUNTING_SALES_INVOICE_LIST_CUSTOMIZE';

/* Children*/
update permission
set sorder= 24,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='Filter'
where code = 'ACCOUNTING_SALES_INVOICE_LIST_FILTER';

/* Children*/
update permission
set sorder= 25,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='History & Notes'
where code = 'ACCOUNTING_SALES_INVOICE_HISTORY_NOTES';

/* Children*/
update permission
set sorder= 26,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='Full delete access'
where code = 'ACCOUNTING_SALES_INVOICE_FULL_DELETE_ACCESS';

/* Children*/
update permission
set sorder= 27,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='Upload Files'
where code = 'ACCOUNTING_SALES_INVOICE_UPLOAD_FILES';

/* Children*/
update permission
set sorder= 28,
    parent=(select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'),
    name='Client link clickable'
where code = 'CUSTOMER_CLICKABLE';

/* Parent*/
update permission
set sorder=6
where code = 'ACCOUNTING_RECURRING_INVOICE_LIST';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_RECURRING_INVOICE_LIST'),
    name='Add'
where code = 'ACCOUNTING_RECURRING_INVOICE_ADD';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_RECURRING_INVOICE_LIST'),
    name='Edit'
where code = 'ACCOUNTING_RECURRING_INVOICE_EDIT';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_RECURRING_INVOICE_LIST'),
    name='Delete'
where code = 'ACCOUNTING_RECURRING_INVOICE_DELETE';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_RECURRING_INVOICE_LIST'),
    name='Summary'
where code = 'ACCOUNTING_RECURRING_INVOICE_SUMMARY';

/* Children*/
update permission
set sorder= 5,
    parent=(select id from permission where code = 'ACCOUNTING_RECURRING_INVOICE_LIST'),
    name='Copy'
where code = 'ACCOUNTING_RECURRING_INVOICE_COPY';


/* Parent*/
update permission
set sorder=7
where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST'),
    name='Add'
where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_ADD';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST'),
    name='Edit'
where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_EDIT';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST'),
    name='Delete'
where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_DELETE';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST'),
    name='Summary'
where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_SUMMARY';

/* Children*/
update permission
set sorder= 5,
    parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST'),
    name='Copy'
where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_COPY';

/* Children*/
update permission
set sorder= 6,
    parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST'),
    name='Convert'
where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_CONVERT';

/* Children*/
update permission
set sorder= 7,
    parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST'),
    name='Item Cost Editable'
where code = 'REQUEST_FOR_QUOTE_CELL_EDITABLE';

/* Children*/
update permission
set sorder= 8,
    parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST'),
    name='Full List Access Purchase List'
where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST_FULL_ACCESS';

/* Children*/
update permission
set sorder= 9,
    parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST'),
    name='See Own'
where code = 'RFQ_SEE_OWN';

/* Children*/
update permission
set sorder= 10,
    parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST'),
    name='New Event'
where code = 'RFQ_ADD_NEW_ACTIVITY_EVENT';

/* Children*/
update permission
set sorder= 11,
    parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST'),
    name='New Log a Call'
where code = 'RFQ_ADD_NEW_ACTIVITY_LOG_A_CALL';

/* Children*/
update permission
set sorder= 12,
    parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST'),
    name='Send Email'
where code = 'RFQ_SEND_EMAIL';

/* Children*/
update permission
set sorder= 13,
    parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_QUOTE_LIST'),
    name='Approve'
where code = 'RFQ_APPROVE';

/* Parent*/
update permission
set sorder=8
where code = 'ACCOUNTING_REQUEST_FOR_PURCHASE_LIST';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_PURCHASE_LIST'),
    name='Add'
where code = 'ACCOUNTING_REQUEST_FOR_PURCHASE_ADD';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_PURCHASE_LIST'),
    name='Edit'
where code = 'ACCOUNTING_REQUEST_FOR_PURCHASE_EDIT';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_PURCHASE_LIST'),
    name='Delete'
where code = 'ACCOUNTING_REQUEST_FOR_PURCHASE_DELETE';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_PURCHASE_LIST'),
    name='Convert to PO'
where code = 'ACCOUNTING_REQUEST_FOR_PURCHASE_CONVERT';

/* Children*/
update permission
set sorder= 5,
    parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_PURCHASE_LIST'),
    name='Full List Access'
where code = 'ACCOUNTING_REQUEST_FOR_PURCHASE_LIST_FULL_ACCESS';

/* Children*/
update permission
set sorder= 6,
    parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_PURCHASE_LIST'),
    name='See Own'
where code = 'RFP_SEE_OWN';

/* Children*/
update permission
set sorder= 7,
    parent=(select id from permission where code = 'ACCOUNTING_REQUEST_FOR_PURCHASE_LIST'),
    name='Approve/Reject'
where code = 'ACCOUNTING_REQUEST_FOR_PURCHASE_APPROVE';

/* Parent*/
update permission
set sorder=9
where code = 'ACCOUNTING_PURCHASE_ORDER_LIST';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),
    name='Add'
where code = 'ACCOUNTING_PURCHASE_ORDER_ADD';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),
    name='Edit'
where code = 'ACCOUNTING_PURCHASE_ORDER_EDIT';
/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),
    name='Delete'
where code = 'ACCOUNTING_PURCHASE_ORDER_DELETE';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),
    name='Summary'
where code = 'ACCOUNTING_PURCHASE_ORDER_SUMMARY';

/* Children*/
update permission
set sorder= 5,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),
    name='PDF'
where code = 'ACCOUNTING_PURCHASE_ORDER_PDF';

/* Children*/
update permission
set sorder= 6,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),
    name='Copy To PO'
where code = 'ACCOUNTING_PURCHASE_ORDER_COPY';

/* Children*/
update permission
set sorder= 7,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),
    name='Copy to SQ'
where code = 'ACCOUNTING_PURCHASE_ORDER_COPY_SQ';

/* Children*/
update permission
set sorder= 8,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),
    name='Copy to PI'
where code = 'ACCOUNTING_PURCHASE_ORDER_COPY_PI';

/* Children*/
update permission
set sorder= 9,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),
    name='Approve and Send'
where code = 'ACCOUNTING_PURCHASE_APPROVE_AND_SEND_BUTTON';

/* Children*/
update permission
set sorder= 10,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),
    name='Save and Approve'
where code = 'ACCOUNTING_PURCHASE_SAVE_AND_APPROVE_BUTTON';

/* Children*/
update permission
set sorder= 11,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),
    name='Closed'
where code = 'ACCOUNTING_PURCHASE_ORDER_CLOSED';

/* Children*/
update permission
set sorder= 12,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),
    name='Purchase Order Receive'
where code = 'ACCOUNTING_PURCHASE_ORDER_RECEIVE';

/* Children*/
update permission
set sorder= 13,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),
    name='Can Approve Purchase Order'
where code = 'ACCOUNTING_CAN_APPROVE_PURCHASE_ORDER';

/* Children*/
update permission
set sorder= 14,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),
    name='Full list access'
where code = 'ACCOUNTING_PURCHASE_ORDER_FULL_LIST_ACCESS';

/* Children*/
update permission
set sorder= 15,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),
    name='Full edit access'
where code = 'ACCOUNTING_PURCHASE_ORDER_FULL_EDIT_ACCESS';

/* Children*/
update permission
set sorder= 16,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),
    name='Goods Received Notes'
where code = 'ACCOUNTING_PURCHASE_ORDER_GOODS_RECEIVED_NOTE';

/* Children*/
update permission
set sorder= 17,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),
    name='Mark As Open'
where code = 'ACCOUNTING_PURCHASE_ORDER_MARK_AS_OPEN';

/* Children*/
update permission
set sorder= 18,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),
    name='GRN Delete'
where code = 'ACCOUNTING_GRN_DELETE';

/* Children*/
update permission
set sorder= 19,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),
    name='GRN Convert To Invoice'
where code = 'ACCOUNTING_GRN_CONVERT_TO_INVOICE';

/* Children*/
update permission
set sorder= 20,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),
    name='See Own'
where code = 'PURCHASE_ORDER_SEE_OWN';

/* Children*/
update permission
set sorder= 21,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),
    name='Customize Column'
where code = 'ACCOUNTING_PURCHASE_ORDER_LIST_CUSTOMIZE';

/* Children*/
update permission
set sorder= 22,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),
    name='Links'
where code = 'ACCOUNTING_PURCHASE_ORDER_LINKS';

/* Children*/
update permission
set sorder= 23,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),
    name='History & Notes'
where code = 'ACCOUNTING_PURCHASE_ORDER_HISTORY_NOTES';
/* Parent*/
update permission
set sorder=10
where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST';
/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'),
    name='Add'
where code = 'ACCOUNTING_PURCHASE_INVOICE_ADD';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'),
    name='Edit'
where code = 'ACCOUNTING_PURCHASE_INVOICE_EDIT';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'),
    name='Delete'
where code = 'ACCOUNTING_PURCHASE_INVOICE_DELETE';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'),
    name='Void'
where code = 'ACCOUNTING_PURCHASE_INVOICE_VOID';

/* Children*/
update permission
set sorder= 5,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'),
    name='Summary'
where code = 'ACCOUNTING_PURCHASE_INVOICE_SUMMARY';

/* Children*/
update permission
set sorder= 6,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'),
    name='PDF'
where code = 'ACCOUNTING_PURCHASE_INVOICE_PDF';

/* Children*/
update permission
set sorder= 7,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'),
    name='COPY'
where code = 'ACCOUNTING_PURCHASE_INVOICE_COPY';

/* Children*/
update permission
set sorder= 8,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'),
    name='Full List Access'
where code = 'ACCOUNTING_PURCHASE_INVOICE_FULL_LIST_ACCESS';

/* Children*/
update permission
set sorder= 9,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'),
    name='Full Edit Access'
where code = 'ACCOUNTING_PURCHASE_INVOICE_FULL_EDIT_ACCESS';

/* Children*/
update permission
set sorder= 10,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'),
    name='Add Credit Note'
where code = 'ACCOUNTING_PURCHASE_CREDIT_NOTE_ADD';

/* Children*/
update permission
set sorder= 11,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'),
    name='Edit Credit Note'
where code = 'ACCOUNTING_PURCHASE_CREDIT_NOTE_EDIT';

/* Children*/
update permission
set sorder= 12,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'),
    name='See Own'
where code = 'PURCHASE_INVOICE_SEE_OWN';

/* Children*/
update permission
set sorder= 13,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'),
    name='Credit Note Full Edit Access'
where code = 'ACCOUNTING_PURCHASE_CREDIT_NOTE_FULL_EDIT_ACCESS';

/* Children*/
update permission
set sorder= 14,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'),
    name='Approve Invoice'
where code = 'ACCOUNTING_PURCHASE_INVOICE_APPROVE';

/* Children*/
update permission
set sorder= 15,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'),
    name='Customize Column'
where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST_CUSTOMIZE';

/* Children*/
update permission
set sorder= 16,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'),
    name='Paid Status Edit'
where code = 'ACCOUNTING_PURCHASE_INVOICE_PAID_STATUS_EDIT';

/* Children*/
update permission
set sorder= 17,
    parent=(select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'),
    name='History & Notes'
where code = 'ACCOUNTING_PURCHASE_INVOICE_HISTORY_NOTES';

/* Parent*/
update permission
set sorder=11
where code = 'ACCOUNTING_RECURRING_BILL_LIST';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_RECURRING_BILL_LIST'),
    name='Add'
where code = 'ACCOUNTING_RECURRING_BILL_ADD';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_RECURRING_BILL_LIST'),
    name='Edit'
where code = 'ACCOUNTING_RECURRING_BILL_EDIT';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_RECURRING_BILL_LIST'),
    name='Delete'
where code = 'ACCOUNTING_RECURRING_BILL_DELETE';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_RECURRING_BILL_LIST'),
    name='Summary   '
where code = 'ACCOUNTING_RECURRING_BILL_SUMMARY';

/* Children*/
update permission
set sorder= 5,
    parent=(select id from permission where code = 'ACCOUNTING_RECURRING_BILL_LIST'),
    name='Copy'
where code = 'ACCOUNTING_RECURRING_BILL_COPY';

/* Parent*/
update permission
set sorder=12
where code = 'ACCOUNTING_FIXED_ASSET_LIST';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_FIXED_ASSET_LIST'),
    name='Add'
where code = 'ACCOUNTING_FIXED_ASSET_ADD';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_FIXED_ASSET_LIST'),
    name='Edit'
where code = 'ACCOUNTING_FIXED_ASSET_EDIT';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_FIXED_ASSET_LIST'),
    name='Delete   '
where code = 'ACCOUNTING_FIXED_ASSET_DELETE';

/* Children*/
update permission
set sorder= 5,
    parent=(select id from permission where code = 'ACCOUNTING_FIXED_ASSET_LIST'),
    name='Update Depreciation'
where code = 'ACCOUNTING_FIXED_ASSET_DEPRECIATION_UPDATE';

/* Parent*/
update permission
set sorder=13
where code = 'ACCOUNTING_EXPENSE_REPORT_LIST';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'),
    name='Company Expense List'
where code = 'ACCOUNTING_COMPANY_EXPENSE_LIST';

/* SubChildren*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_COMPANY_EXPENSE_LIST'),
    name='Add'
where code = 'ACCOUNTING_COMPANY_EXPENSE_ADD';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'),
    name='Add'
where code = 'ACCOUNTING_EXPENSE_REPORT_ADD';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'),
    name='Edit'
where code = 'ACCOUNTING_EXPENSE_REPORT_EDIT';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'),
    name='Delete'
where code = 'ACCOUNTING_EXPENSE_REPORT_DELETE';

/* Children*/
update permission
set sorder= 5,
    parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'),
    name='Add Payment'
where code = 'ACCOUNTING_EXPENSE_ADD_PAYMENT';

/* Children*/
update permission
set sorder= 6,
    parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'),
    name='Copy'
where code = 'ACCOUNTING_EXPENSE_REPORT_COPY';

/* Children*/
update permission
set sorder= 7,
    parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'),
    name='Approve'
where code = 'ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM';

/* Children*/
update permission
set sorder= 8,
    parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'),
    name='Draft'
where code = 'ACCOUNTING_EXPENSE_REPORT_DRAFT';

/* Children*/
update permission
set sorder= 9,
    parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'),
    name='Void'
where code = 'ACCOUNTING_EXPENSE_REPORT_VOID';

/* Children*/
update permission
set sorder= 10,
    parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'),
    name='Add/View Full Access'
where code = 'EXPENSE_ADD_VIEW_FULL_ACCESS';

/* Children*/
update permission
set sorder= 11,
    parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'),
    name='Add to Staff'
where code = 'ACCOUNTING_EXPENSE_REPORT_ADD_TO_STAFF';

/* Children*/
update permission
set sorder= 12,
    parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'),
    name='Full List Access'
where code = 'ACCOUNTING_EXPENSE_FULL_LIST_ACCESS';

/* Children*/
update permission
set sorder= 13,
    parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'),
    name='Add Category'
where code = 'ACCOUNTING_EXPENSE_CLAIM_ADD_CATEGORY';

/* Children*/
update permission
set sorder= 14,
    parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'),
    name='Accountant Approval'
where code = 'ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM_DOUBLE_APPROVE';

/* Children*/
update permission
set sorder= 15,
    parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'),
    name='Import from CSV'
where code = 'SHOW_IMPORT_EXPENCE';

/* Children*/
update permission
set sorder= 16,
    parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'),
    name='See Own'
where code = 'EXPENSE_SEE_OWN';

/* Children*/
update permission
set sorder= 17,
    parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'),
    name='Customize Column'
where code = 'ACCOUNTING_EXPENSE_REPORT_LIST_CUSTOMIZE';

/* Children*/
update permission
set sorder= 18,
    parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'),
    name='History & Notes'
where code = 'ACCOUNTING_EXPENSE_REPORT_HISTORY_NOTES';

/* Children*/
update permission
set sorder= 19,
    parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'),
    name='Full edit access'
where code = 'ACCOUNTING_EXPENSE_REPORT_FULL_EDIT_ACCESS';

/* Children*/
update permission
set sorder= 20,
    parent=(select id from permission where code = 'ACCOUNTING_EXPENSE_REPORT_LIST'),
    name='Full delete access'
where code = 'ACCOUNTING_EXPENSE_REPORT_FULL_DELETE_ACCESS';

/* Parent*/
update permission
set sorder=14
where code = 'ACCOUNTING_CUSTOMER_LIST';
/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_CUSTOMER_LIST'),
    name='Add'
where code = 'ACCOUNTING_CUSTOMER_ADD';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_CUSTOMER_LIST'),
    name='Edit'
where code = 'ACCOUNTING_CUSTOMER_EDIT';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_CUSTOMER_LIST'),
    name='Delete'
where code = 'ACCOUNTING_CUSTOMER_DELETE';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_CUSTOMER_LIST'),
    name='Summary'
where code = 'ACCOUNTING_CUSTOMER_SUMMARY';

/* Children*/
update permission
set sorder= 5,
    parent=(select id from permission where code = 'ACCOUNTING_CUSTOMER_LIST'),
    name='Quick Add'
where code = 'ACCOUNTING_CUSTOMER_QUICK_ADD';

/* Children*/
update permission
set sorder= 6,
    parent=(select id from permission where code = 'ACCOUNTING_CUSTOMER_LIST'),
    name='Update Credit Limit'
where code = 'UPDATE_CUSTOMER_CREDIT_LIMIT';

/* Children*/
update permission
set sorder= 7,
    parent=(select id from permission where code = 'ACCOUNTING_CUSTOMER_LIST'),
    name='Import'
where code = 'ACCOUNTING_CUSTOMER_IMPORT';

/* Children*/
update permission
set sorder= 8,
    parent=(select id from permission where code = 'ACCOUNTING_CUSTOMER_LIST'),
    name='Export'
where code = 'ACCOUNTING_CUSTOMER_EXPORT';

/* Children*/
update permission
set sorder= 9,
    parent=(select id from permission where code = 'ACCOUNTING_CUSTOMER_LIST'),
    name='See All'
where code = 'ACCOUNTING_SEE_ALL_CUSTOMERS_LIST';

/* Parent*/
update permission
set sorder= 15
where code = 'ACCOUNTING_CONTACT_LIST';

/* Children*/
update permission
set sorder=1,
    parent=(select id from permission where code = 'ACCOUNTING_CONTACT_LIST'),
    name='Add'
where code = 'ACCOUNTING_CONTACT_ADD';

/* Children*/
update permission
set sorder=2,
    parent=(select id from permission where code = 'ACCOUNTING_CONTACT_LIST'),
    name='Edit'
where code = 'ACCOUNTING_CONTACT_EDIT';

/* Children*/
update permission
set sorder=3,
    parent=(select id from permission where code = 'ACCOUNTING_CONTACT_LIST'),
    name='Delete'
where code = 'ACCOUNTING_CONTACT_DELETE';

/* Children*/
update permission
set sorder=4,
    parent=(select id from permission where code = 'ACCOUNTING_CONTACT_LIST'),
    name='Events'
where code = 'ACCOUNTING_EVENT_LIST';
/* Children*/
update permission
set sorder=5,
    parent=(select id from permission where code = 'ACCOUNTING_CONTACT_LIST'),
    name='Cases'
where code = 'ACCOUNTING_CASE_LIST';

/* Children*/
update permission
set sorder=6,
    parent=(select id from permission where code = 'ACCOUNTING_CONTACT_LIST'),
    name='Tasks'
where code = 'ACCOUNTING_TASK_LIST';

/* Children*/
update permission
set sorder=7,
    parent=(select id from permission where code = 'ACCOUNTING_CONTACT_LIST'),
    name='Issues'
where code = 'ACCOUNTING_ISSUE_LIST';

/* Children*/
update permission
set sorder=8,
    parent=(select id from permission where code = 'ACCOUNTING_CONTACT_LIST'),
    name='Skip Department Validation'
where code = 'SKIP_DEPARTMENT_ITEM_VALIDATION';


/* Parent*/
update permission
set sorder=16
where code = 'ACCOUNTING_SUPPLIER_LIST';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_SUPPLIER_LIST'),
    name='Add'
where code = 'ACCOUNTING_SUPPLIER_ADD';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_SUPPLIER_LIST'),
    name='Edit'
where code = 'ACCOUNTING_SUPPLIER_EDIT';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_SUPPLIER_LIST'),
    name='Delete'
where code = 'ACCOUNTING_SUPPLIER_DELETE';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_SUPPLIER_LIST'),
    name='Summary'
where code = 'ACCOUNTING_SUPPLIER_SUMMARY';

/* Children*/
update permission
set sorder= 5,
    parent=(select id from permission where code = 'ACCOUNTING_SUPPLIER_LIST'),
    name='Quick Add'
where code = 'ACCOUNTING_SUPPLIER_QUICK_ADD';

/* Children*/
update permission
set sorder= 6,
    parent=(select id from permission where code = 'ACCOUNTING_SUPPLIER_LIST'),
    name='Export'
where code = 'ACCOUNTING_SUPPLIER_EXPORT';

/* Children*/
update permission
set sorder= 7,
    parent=(select id from permission where code = 'ACCOUNTING_SUPPLIER_LIST'),
    name='Full List Access'
where code = 'ACCOUNTING_SUPPLIER_FULL_LIST_ACCESS';

/* Parent*/
update permission
set sorder=17
where code = 'ACCOUNTING_PRODUCT_LIST';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'),
    name='Add'
where code = 'ACCOUNTING_PRODUCT_ADD'
  and CONTEXT = 'ACCOUNTING';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'),
    name='Edit'
where code = 'ACCOUNTING_PRODUCT_EDIT'
  and CONTEXT = 'ACCOUNTING';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'),
    name='Delete'
where code = 'ACCOUNTING_PRODUCT_DELETE'
  and CONTEXT = 'ACCOUNTING';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'),
    name='Summary'
where code = 'ACCOUNTING_PRODUCT_SUMMARY'
  and CONTEXT = 'ACCOUNTING';

/* Children*/
update permission
set sorder= 5,
    parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'),
    name='Quick Add'
where code = 'ACCOUNTING_PRODUCT_QUICK_ADD'
  and CONTEXT = 'ACCOUNTING';

/* Children*/
update permission
set sorder= 6,
    parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'),
    name='Variation Add'
where code = 'ACCOUNTING_VARIATION_ADD'
  and CONTEXT = 'ACCOUNTING';

/* Children*/
update permission
set sorder= 7,
    parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'),
    name='Variation Delete'
where code = 'ACCOUNTING_VARIATION_DELETE'
  and CONTEXT = 'ACCOUNTING';

/* Children*/
update permission
set sorder= 8,
    parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'),
    name='Inventory'
where code = 'ACCOUNTING_INVENTORY_LIST'
  and context = 'ACCOUNTING';

/* Sub Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_INVENTORY_LIST' and context = 'ACCOUNTING'),
    name='Add'
where code = 'ACCOUNTING_INVENTORY_ADD';

/* Sub Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_INVENTORY_LIST' and context = 'ACCOUNTING'),
    name='Edit'
where code = 'ACCOUNTING_INVENTORY_EDIT';

/* Sub Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_INVENTORY_LIST' and context = 'ACCOUNTING'),
    name='Delete'
where code = 'ACCOUNTING_INVENTORY_DELETE';

/* Sub Children*/
update permission
set sorder= 5,
    parent=(select id from permission where code = 'ACCOUNTING_INVENTORY_LIST' and context = 'ACCOUNTING'),
    name='Summary'
where code = 'ACCOUNTING_INVENTORY_SUMMARY';

/* Sub Children*/
update permission
set sorder= 6,
    parent=(select id from permission where code = 'ACCOUNTING_INVENTORY_LIST' and context = 'ACCOUNTING'),
    name='Inventory Variation Add'
where code = 'INVENTORY_VARIATION_ADD';

/* Sub Children*/
update permission
set sorder= 7,
    parent=(select id from permission where code = 'ACCOUNTING_INVENTORY_LIST' and context = 'ACCOUNTING'),
    name='Inventory Variation Delete'
where code = 'INVENTORY_VARIATION_DELETE';

/* Children*/
update permission
set sorder= 9,
    parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST' and context = 'ACCOUNTING'),
    name='Build Assembly'
where code = 'ACCOUNTING_BUILD_ASSEMBLY';

/* Children*/
update permission
set sorder= 10,
    parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'),
    name='Cost'
where code = 'ACCOUNTING_PRODUCT_COST'
  and CONTEXT = 'ACCOUNTING';

/* Children*/
update permission
set sorder= 11,
    parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'),
    name='History List'
where code = 'ACCOUNTING_PRODUCT_HISTORY_LIST'
  and CONTEXT = 'ACCOUNTING';

/* Children*/
update permission
set sorder= 12,
    parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'),
    name='Upload Files'
where code = 'ACCOUNTING_PRODUCT_UPLOAD_FILES'
  and CONTEXT = 'ACCOUNTING';

/* Children*/
update permission
set sorder= 13,
    parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'),
    name='Selling Price'
where code = 'ACCOUNTING_PRODUCT_SELLING'
  and CONTEXT = 'ACCOUNTING';

/* Children*/
update permission
set sorder= 14,
    parent=(select id from permission where code = 'ACCOUNTING_PRODUCT_LIST'),
    name='Avarage Cost'
where code = 'ACCOUNTING_PRODUCT_AVARAGE_COST'
  and CONTEXT = 'ACCOUNTING';

/* Parent*/
update permission
set sorder=18
where code = 'ACCOUNTING_BANK_ACCOUNT_LIST';
/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_LIST'),
    name='Add'
where code = 'ACCOUNTING_BANK_ACCOUNT_ADD';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_LIST'),
    name='Edit'
where code = 'ACCOUNTING_BANK_ACCOUNT_EDIT';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_LIST'),
    name='Delete'
where code = 'ACCOUNTING_BANK_ACCOUNT_DELETE';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_LIST'),
    name='Summary'
where code = 'ACCOUNTING_BANK_ACCOUNT_SUMMARY';

/* Children*/
update permission
set sorder= 5,
    parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_LIST'),
    name='Assignee List Value'
where code = 'ACCOUNTING_BANKACCOUNT_ASSIGNEE_LIST_VALUE';

/* Children*/
update permission
set sorder= 6,
    parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_LIST'),
    name='Reconcilation Report'
where code = 'ACCOUNTING_BANK_ACCOUNT_RECONCILATION_REPORT';

/* Children*/
update permission
set sorder= 7,
    parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_LIST'),
    name='Transfer Money'
where code = 'ACCOUNTING_BANK_ACCOUNT_TRANSFER';

/* Children*/
update permission
set sorder= 8,
    parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_LIST'),
    name='Import Transactions'
where code = 'ACCOUNTING_BANK_ACCOUNT_TRANSACTION_IMPORT';

/* Children*/
update permission
set sorder= 9,
    parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_LIST'),
    name='Account Transactions'
where code = 'ACCOUNTING_BANK_ACCOUNT_TRANSACTIONS';

/* Children*/
update permission
set sorder= 10,
    parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_LIST'),
    name='Bank Statements'
where code = 'ACCOUNTING_BANK_STATEMENT';

/* Parent*/
update permission
set sorder= 19
where code = 'ACCOUNTING_TRANSACTION_MENU';

/* Parent*/
update permission
set sorder=20,
    name='Bank Receipts'
where code = 'ACCOUNTING_BANK_ACCOUNT_RECEIVE';
/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_RECEIVE'),
    name='Add'
where code = 'ACCOUNTING_BANK_ACCOUNT_RECEIVE_ADD';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_RECEIVE'),
    name='Edit'
where code = 'ACCOUNTING_BANK_ACCOUNT_RECEIVE_EDIT';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_RECEIVE'),
    name='Delete'
where code = 'ACCOUNTING_BANK_ACCOUNT_RECEIVE_DELETE';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_RECEIVE'),
    name='Summary'
where code = 'ACCOUNTING_BANK_ACCOUNT_RECEIVE_SUMMARY';

/* Parent*/
update permission
set sorder=21,
    name='Bank payments'
where code = 'ACCOUNTING_BANK_ACCOUNT_SPEND';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_SPEND'),
    name='Add'
where code = 'ACCOUNTING_BANK_ACCOUNT_SPEND_ADD';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_SPEND'),
    name='Edit'
where code = 'ACCOUNTING_BANK_ACCOUNT_SPEND_EDIT';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_SPEND'),
    name='Delete'
where code = 'ACCOUNTING_BANK_ACCOUNT_SPEND_DELETE';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_SPEND'),
    name='Summary'
where code = 'ACCOUNTING_BANK_ACCOUNT_SPEND_SUMMARY';

/* Parent*/
update permission
set sorder=22
where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT'),
    name='Add'
where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_ADD';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT'),
    name='Edit'
where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_EDIT';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT'),
    name='Delete'
where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_DELETE';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT'),
    name='Summary'
where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_SUMMARY';

/* Parent*/
update permission
set sorder= 23
where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT'),
    name='Add'
where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_ADD';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT'),
    name='Edit'
where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_EDIT';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT'),
    name='Delete'
where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_DELETE';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT'),
    name='Summary'
where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_SUMMARY';

/* Children*/
update permission
set sorder= 5,
    parent=(select id from permission where code = 'ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT'),
    name='Consignment List'
where code = 'ACCOUNTING_CONSIGNMENT_LIST_VIEW';


/* Parent*/
update permission
set sorder= 24
where code = 'ACCOUNTING_PREPAYMENT_LIST';

/* Children*/
update permission
set sorder=1,
    parent=(select id from permission where code = 'ACCOUNTING_PREPAYMENT_LIST'),
    name='Add'
where code = 'ACCOUNTING_PREPAYMENT_ADD';

/* Children*/
update permission
set sorder=2,
    parent=(select id from permission where code = 'ACCOUNTING_PREPAYMENT_LIST'),
    name='Edit'
where code = 'ACCOUNTING_PREPAYMENT_EDIT';
/* Children*/
update permission
set sorder=3,
    parent=(select id from permission where code = 'ACCOUNTING_PREPAYMENT_LIST'),
    name='Delete'
where code = 'ACCOUNTING_PREPAYMENT_DELETE';

/* Children*/
update permission
set sorder=4,
    parent=(select id from permission where code = 'ACCOUNTING_PREPAYMENT_LIST'),
    name='Summary'
where code = 'ACCOUNTING_PREPAYMENT_SUMMARY';

/* Children*/
update permission
set sorder=5,
    parent=(select id from permission where code = 'ACCOUNTING_PREPAYMENT_LIST'),
    name='Void'
where code = 'ACCOUNTING_PREPAYMENT_VOID';

/* Children*/
update permission
set sorder=6,
    parent=(select id from permission where code = 'ACCOUNTING_PREPAYMENT_LIST'),
    name='PDF'
where code = 'ACCOUNTING_PREPAYMENT_PDF';

/* Children*/
update permission
set sorder=7,
    parent=(select id from permission where code = 'ACCOUNTING_PREPAYMENT_LIST'),
    name='Customer Refund Add'
where code = 'CUSTOMER_PREPAYMENT_REFUND_ADD';

/* Children*/
update permission
set sorder=8,
    parent=(select id from permission where code = 'ACCOUNTING_PREPAYMENT_LIST'),
    name='Customer Refund Summary'
where code = 'CUSTOMER_PREPAYMENT_REFUND_VIEW';

/* Children*/
update permission
set sorder=9,
    parent=(select id from permission where code = 'ACCOUNTING_PREPAYMENT_LIST'),
    name='Customer Refund Delete'
where code = 'CUSTOMER_PREPAYMENT_REFUND_DELETE';


/* Parent*/
update permission
set sorder= 25
where code = 'ACCOUNTING_SUPPLIER_CREDIT_LIST';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_SUPPLIER_CREDIT_LIST'),
    name='Add'
where code = 'ACCOUNTING_SUPPLIER_CREDIT_ADD';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_SUPPLIER_CREDIT_LIST'),
    name='Edit'
where code = 'ACCOUNTING_SUPPLIER_CREDIT_EDIT';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_SUPPLIER_CREDIT_LIST'),
    name='Delete'
where code = 'ACCOUNTING_SUPPLIER_CREDIT_DELETE';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_SUPPLIER_CREDIT_LIST'),
    name='Summary'
where code = 'ACCOUNTING_SUPPLIER_CREDIT_SUMMARY';

/* Children*/
update permission
set sorder= 5,
    parent=(select id from permission where code = 'ACCOUNTING_SUPPLIER_CREDIT_LIST'),
    name='Void'
where code = 'ACCOUNTING_SUPPLIER_CREDIT_VOID';

/* Children*/
update permission
set sorder= 6,
    parent=(select id from permission where code = 'ACCOUNTING_SUPPLIER_CREDIT_LIST'),
    name='PDF'
where code = 'ACCOUNTING_SUPPLIER_CREDIT_PDF';

/* Children*/
update permission
set sorder= 7,
    parent=(select id from permission where code = 'ACCOUNTING_SUPPLIER_CREDIT_LIST'),
    name='Supplier Refund Add'
where code = 'SUPPLIER_PREPAYMENT_REFUND_ADD';

/* Children*/
update permission
set sorder= 8,
    parent=(select id from permission where code = 'ACCOUNTING_SUPPLIER_CREDIT_LIST'),
    name='Supplier Refund Summary'
where code = 'SUPPLIER_PREPAYMENT_REFUND_VIEW';

/* Children*/
update permission
set sorder= 9,
    parent=(select id from permission where code = 'ACCOUNTING_SUPPLIER_CREDIT_LIST'),
    name='Supplier Refund Delete'
where code = 'SUPPLIER_PREPAYMENT_REFUND_DELETE';

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
/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_MANUAL_JOURNAL_LIST'),
    name='Add'
where code = 'ACCOUNTING_MANUAL_JOURNAL_ADD';
/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_MANUAL_JOURNAL_LIST'),
    name='Edit'
where code = 'ACCOUNTING_MANUAL_JOURNAL_EDIT';
/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_MANUAL_JOURNAL_LIST'),
    name='Delete'
where code = 'ACCOUNTING_MANUAL_JOURNAL_DELETE';
/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_MANUAL_JOURNAL_LIST'),
    name='Summary'
where code = 'ACCOUNTING_MANUAL_JOURNAL_SUMMARY';

/* Children*/
update permission
set sorder= 5,
    parent=(select id from permission where code = 'ACCOUNTING_MANUAL_JOURNAL_LIST'),
    name='Void'
where code = 'ACCOUNTING_MANUAL_JOURNAL_VOID';

/* Parent*/
update permission
set sorder=27
where code = 'ACCOUNTING_RECEIVE_PAYMENT_LIST';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_RECEIVE_PAYMENT_LIST'),
    name='Add'
where code = 'ACCOUNTING_RECEIVE_PAYMENT';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_RECEIVE_PAYMENT_LIST'),
    name='Edit'
where code = 'RECEIVE_PAYMENT_EDIT';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_RECEIVE_PAYMENT_LIST'),
    name='Delete'
where code = 'RECEIVE_PAYMENT_DELETE';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_RECEIVE_PAYMENT_LIST'),
    name='Summary'
where code = 'RECEIVE_PAYMENT_SUMMARY';

/* Children*/
update permission
set sorder= 5,
    parent=(select id from permission where code = 'ACCOUNTING_RECEIVE_PAYMENT_LIST'),
    name='PDF'
where code = 'RECEIVE_PAYMENT_PDF';

/* Children*/
update permission
set sorder= 6,
    parent=(select id from permission where code = 'ACCOUNTING_RECEIVE_PAYMENT_LIST'),
    name='Void'
where code = 'ACCOUNTING_MANUAL_JOURNAL_VOID';

/* Children*/
update permission
set sorder= 7,
    parent=(select id from permission where code = 'ACCOUNTING_RECEIVE_PAYMENT_LIST'),
    name='See All'
where code = 'ACCOUNTING_RECEIVE_PAYMENT_FULL_LIST_ACCESS';

/* Children*/
update permission
set sorder= 8,
    parent=(select id from permission where code = 'ACCOUNTING_RECEIVE_PAYMENT_LIST'),
    name='See Own'
where code = 'ACCOUNTING_RECEIVE_PAYMENT_SEE_OWN';


/* Parent*/
update permission
set sorder=28
where code = 'ACCOUNTING_PAY_BILL_LIST';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_PAY_BILL_LIST'),
    name='Add'
where code = 'ACCOUNTING_PAY_BILL';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_PAY_BILL_LIST'),
    name='Edit'
where code = 'PAY_INVOICE_EDIT';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_PAY_BILL_LIST'),
    name='Delete'
where code = 'PAY_INVOICE_DELETE';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_PAY_BILL_LIST'),
    name='Summary'
where code = 'PAY_INVOICE_SUMMARY';

/* Children*/
update permission
set sorder= 5,
    parent=(select id from permission where code = 'ACCOUNTING_PAY_BILL_LIST'),
    name='Void'
where code = 'PAY_INVOICE_VOID';

/* Children*/
update permission
set sorder= 6,
    parent=(select id from permission where code = 'ACCOUNTING_PAY_BILL_LIST'),
    name='PDF'
where code = 'PAY_INVOICE_PDF';

/* Children*/
update permission
set sorder= 7,
    parent=(select id from permission where code = 'ACCOUNTING_PAY_BILL_LIST'),
    name='See All'
where code = 'ACCOUNTING_PAY_BILL_FULL_LIST_ACCESS';

/* Children*/
update permission
set sorder= 8,
    parent=(select id from permission where code = 'ACCOUNTING_PAY_BILL_LIST'),
    name='See Own'
where code = 'ACCOUNTING_PAY_BILL_SEE_OWN';


/* Parent*/
update permission
set sorder=29
where code = 'ACCOUNTING_REPORTS_MENU';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_REPORTS_MENU'),
    name='Profit and Loss'
where code = 'ACCOUNTING_PROFIT_AND_LOSS';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_REPORTS_MENU'),
    name='Balance Sheet'
where code = 'ACCOUNTING_BALANCE_SHEET';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_REPORTS_MENU'),
    name='Trial Balance'
where code = 'ACCOUNTING_TRIAL_BALANCE';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_REPORTS_MENU'),
    name='Cash Flow'
where code = 'ACCOUNTING_CASH_FLOW';

/* Children*/
update permission
set sorder= 5,
    parent=(select id from permission where code = 'ACCOUNTING_REPORTS_MENU'),
    name='Journal Report'
where code = 'ACCOUNTING_JOURNAL_REPORT';

/* Children*/
update permission
set sorder= 6,
    parent=(select id from permission where code = 'ACCOUNTING_REPORTS_MENU'),
    name='Vat Return'
where code = 'ACCOUNTING_VAT_RETURN';

/* Children*/
update permission
set sorder= 7,
    parent=(select id from permission where code = 'ACCOUNTING_REPORTS_MENU'),
    name='Budget Sheet'
where code = 'ACCOUNTING_BUDGET_SHEET';

/* Children*/
update permission
set sorder= 8,
    parent=(select id from permission where code = 'ACCOUNTING_REPORTS_MENU'),
    name='Account Transactions'
where code = 'ACCOUNTING_ACCOUNT_TRANSACTIONS';

/* Children*/
update permission
set sorder= 9,
    parent=(select id from permission where code = 'ACCOUNTING_REPORTS_MENU'),
    name='Stock Valuation'
where code = 'ACCOUNTING_STOCK_VALUATION';

/* Children*/
update permission
set sorder= 10,
    parent=(select id from permission where code = 'ACCOUNTING_REPORTS_MENU'),
    name='Vat Return List'
where code = 'ACCOUNTING_VAT_RETURNS_LIST';

/* Children*/
update permission
set sorder= 11,
    parent=(select id from permission where code = 'ACCOUNTING_REPORTS_MENU'),
    name='Aging Summary Receivable'
where code = 'ACCOUNTING_AGING_SUMMARY_RECEIVABLE';

/* Children*/
update permission
set sorder= 12,
    parent=(select id from permission where code = 'ACCOUNTING_REPORTS_MENU'),
    name='Aging Summary Payable'
where code = 'ACCOUNTING_AGING_SUMMARY_PAYABLE';

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
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_WAREHOUSE_MENU'),
    name='Warehouse Owner'
where code = 'WAREHOUSE_OWNER';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_WAREHOUSE_MENU'),
    name='Full List Access'
where code = 'ACCOUNTING_WAREHOUSE_FULL_LIST_ACCESS';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_WAREHOUSE_MENU'),
    name='See Own'
where code = 'WAREHOUSE_SEE_OWN';


/* Parent*/
update permission
set sorder= 32
where code = 'ACCOUNTING_STOCK_ADJUSTMENT_LIST';

/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_STOCK_ADJUSTMENT_LIST'),
    name='Add'
where code = 'ACCOUNTING_STOCK_ADJUSTMENT_ADD';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_STOCK_ADJUSTMENT_LIST'),
    name='Edit'
where code = 'ACCOUNTING_STOCK_ADJUSTMENT_EDIT';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_STOCK_ADJUSTMENT_LIST'),
    name='Delete'
where code = 'ACCOUNTING_STOCK_ADJUSTMENT_DELETE';


/* Parent*/
update permission
set sorder= 33
where code = 'ACCOUNTING_STOCK_TRANSFER_LIST';


/* Children*/
update permission
set sorder= 1,
    parent=(select id from permission where code = 'ACCOUNTING_STOCK_TRANSFER_LIST'),
    name='Add'
where code = 'ACCOUNTING_STOCK_TRANSFER_ADD';

/* Children*/
update permission
set sorder= 2,
    parent=(select id from permission where code = 'ACCOUNTING_STOCK_TRANSFER_LIST'),
    name='Edit'
where code = 'ACCOUNTING_STOCK_TRANSFER_EDIT';

/* Children*/
update permission
set sorder= 3,
    parent=(select id from permission where code = 'ACCOUNTING_STOCK_TRANSFER_LIST'),
    name='Delete'
where code = 'ACCOUNTING_STOCK_TRANSFER_DELETE';

/* Children*/
update permission
set sorder= 4,
    parent=(select id from permission where code = 'ACCOUNTING_STOCK_TRANSFER_LIST'),
    name='Summary'
where code = 'ACCOUNTING_STOCK_TRANSFER_SUMMARY';

/* Children*/
update permission
set sorder= 5,
    parent=(select id from permission where code = 'ACCOUNTING_STOCK_TRANSFER_LIST'),
    name='Transfer Button'
where code = 'ACCOUNTING_STOCK_TRANSFER_BUTTON';


/* Children*/
update permission
set sorder= 6,
    parent=(select id from permission where code = 'ACCOUNTING_STOCK_TRANSFER_LIST'),
    name='See Own'
where code = 'ACCOUNTING_STOCK_TRANSFER_SEE_OWN';

/* Children*/
update permission
set sorder= 7,
    parent=(select id from permission where code = 'ACCOUNTING_STOCK_TRANSFER_LIST'),
    name='See Own (Warehouse Owner)'
where code = 'ACCOUNTING_STOCK_TRANSFER_SEE_OWN_WAREHOUSE_OWNER';

/* Children*/
update permission
set sorder= 8,
    parent=(select id from permission where code = 'ACCOUNTING_STOCK_TRANSFER_LIST'),
    name='Add Draft'
where code = 'STOCK_TRANSFER_DRAFT_ADD';