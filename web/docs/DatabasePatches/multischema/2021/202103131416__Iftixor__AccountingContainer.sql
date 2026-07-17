update company set selectFunctioncolumn =(select setval('"anv".container_id_seq', (select max(id) from "anv".container))) where id=(select id from company limit 1);
update company set selectFunctioncolumn =(select setval('"anv".container_item_id_seq', (select max(id) from "anv".container_item))) where id=(select id from company limit 1);

delete from "anv".container_item where propertyID is null;
delete from "anv".container_item where moduleCode = 'accounting';
delete from "anv".container where moduleCode ='accounting';

insert into "anv".container(code, defaultName, moduleCode, sorder, changed, preparedView)
values ('accounting', 'accounting', 'accounting', 1, false, 'accountingHomepage');

insert into "anv".container(code, defaultName, moduleCode, sorder, changed, preparedView)
values ('transaction', 'transactions', 'accounting', 2, false, 'cashreceipt');

insert into "anv".container(code, defaultName, moduleCode, sorder, changed, preparedView)
values ('report', 'statements', 'accounting', 3, false, 'newprofitLoss');

insert into "anv".container(code, defaultName, moduleCode, sorder, changed, preparedView)
values ('warehouse', 'warehouse', 'accounting', 4, false, 'warehouseList');



insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='SALES_QUOTES' limit 1), (select id from "anv".property where objectName='salequote' limit 1), (select id from "anv".container where code='accounting' limit 1), 1, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='SALES_ORDERS' limit 1), (select id from "anv".property where objectName='saleorder' limit 1), (select id from "anv".container where code='accounting' limit 1), 2, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='SALES_INVOICING' limit 1), (select id from "anv".property where objectName='saleinvoice' limit 1), (select id from "anv".container where code='accounting' limit 1), 3, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='RECCURING_INVOICES' limit 1), (select id from "anv".property where objectName='recurringinvoice' limit 1), (select id from "anv".container where code='accounting' limit 1), 4, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='REQUEST_FOR_QUOTES' limit 1), (select id from "anv".property where objectName='requestforquote' limit 1), (select id from "anv".container where code='accounting' limit 1), 5, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='REQUEST_FOR_PURCHASES' limit 1), (select id from "anv".property where objectName='requestforpurchase' limit 1), (select id from "anv".container where code='accounting' limit 1), 6, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='PURCHASE_ORDERS' limit 1), (select id from "anv".property where objectName='purchaseorder' limit 1), (select id from "anv".container where code='accounting' limit 1), 7, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='PURCHASE_INVOICING' limit 1), (select id from "anv".property where objectName='purchaseinvoice' limit 1), (select id from "anv".container where code='accounting' limit 1), 8, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='RECURRING_BILLS' limit 1), (select id from "anv".property where objectName='recurringbill' limit 1), (select id from "anv".container where code='accounting' limit 1), 9, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='FIXED_ASSESTS' limit 1), (select id from "anv".property where objectName='fixedassets' limit 1), (select id from "anv".container where code='accounting' limit 1), 10, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='EXPENSE_REPORTING' limit 1), (select id from "anv".property where objectName='EXPENSES_CLAIM' limit 1), (select id from "anv".container where code='accounting' limit 1), 11, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='ACCOUNTING_CUSTOMER_CENTER' limit 1), (select id from "anv".property where objectName='clientList' limit 1), (select id from "anv".container where code='accounting' limit 1), 12, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='SUPPLIER_CENTER' limit 1), (select id from "anv".property where objectName='supplierList' limit 1), (select id from "anv".container where code='accounting' limit 1), 13, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='PRODUCTS_SERVICES' limit 1), (select id from "anv".property where objectName='productsOrServices' limit 1), (select id from "anv".container where code='accounting' limit 1), 14, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='PRODUCT_INVENTORY_ITEMS' limit 1), (select id from "anv".property where objectName='inventoryitems' limit 1), (select id from "anv".container where code='accounting' limit 1), 15, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='BANK_ACCOUNTS' limit 1), (select id from "anv".property where objectName='bankaccount' limit 1), (select id from "anv".container where code='accounting' limit 1), 16, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='INVENTORY_MANAGEMENT' limit 1), (select id from "anv".property where objectName='STOCK_ADJUSTMENT' limit 1), (select id from "anv".container where code='accounting' limit 1), 18, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='CONSIGNMENTS' limit 1), (select id from "anv".property where objectName='consignment' limit 1), (select id from "anv".container where code='accounting' limit 1), 19, 'accounting');


insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='ACCOUNTING_MODULE' limit 1), (select id from "anv".property where objectName='CASH_RECEIPT' limit 1), (select id from "anv".container where code='transaction' limit 1), 1, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='ACCOUNTING_MODULE' limit 1), (select id from "anv".property where objectName='CASH_PAYMENT' limit 1), (select id from "anv".container where code='transaction' limit 1), 2, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='ACCOUNTING_MODULE' limit 1), (select id from "anv".property where objectName='RECEIVE_MONEY' limit 1), (select id from "anv".container where code='transaction' limit 1), 3, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='ACCOUNTING_MODULE' limit 1), (select id from "anv".property where objectName='SPEND_MONEY' limit 1), (select id from "anv".container where code='transaction' limit 1), 4, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='ACCOUNTING_MODULE' limit 1), (select id from "anv".property where objectName='customerPrepayment' limit 1), (select id from "anv".container where code='transaction' limit 1), 5, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='ACCOUNTING_MODULE' limit 1), (select id from "anv".property where objectName='supplierPrepayment' limit 1), (select id from "anv".container where code='transaction' limit 1), 6, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='CHECKS' limit 1), (select id from "anv".property where objectName='checkList' limit 1), (select id from "anv".container where code='transaction' limit 1), 7, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='MANUAL_TRANSACTIONS' limit 1), (select id from "anv".property where objectName='manualtransactions' limit 1), (select id from "anv".container where code='transaction' limit 1), 9, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='ACCOUNTING_MODULE' limit 1), (select id from "anv".property where objectName='BATCH_RECEIVE_PAYMENT' limit 1), (select id from "anv".container where code='transaction' limit 1), 10, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='ACCOUNTING_MODULE' limit 1), (select id from "anv".property where objectName='payBillsList' limit 1), (select id from "anv".container where code='transaction' limit 1), 11, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='PURCHASE_ORDERS' limit 1), (select id from "anv".property where objectName='goodsreceivednotes' limit 1), (select id from "anv".container where code='transaction' limit 1), 12, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='SALES_ORDERS' limit 1), (select id from "anv".property where objectName='goodsdeliverednotes' limit 1), (select id from "anv".container where code='transaction' limit 1), 13, 'accounting');


insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='ACCOUNTING_MODULE' limit 1), (select id from "anv".property where objectName='newprofitLoss' limit 1), (select id from "anv".container where code='report' limit 1), 1, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='ACCOUNTING_MODULE' limit 1), (select id from "anv".property where objectName='balanceSheet' limit 1), (select id from "anv".container where code='report' limit 1), 2, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='ACCOUNTING_MODULE' limit 1), (select id from "anv".property where objectName='trialBalance' limit 1), (select id from "anv".container where code='report' limit 1), 3, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='ACCOUNTING_MODULE' limit 1), (select id from "anv".property where objectName='cashFlowStatement' limit 1), (select id from "anv".container where code='report' limit 1), 4, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='ACCOUNTING_MODULE' limit 1), (select id from "anv".property where objectName='arAgingSummary' limit 1), (select id from "anv".container where code='report' limit 1), 5, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='ACCOUNTING_MODULE' limit 1), (select id from "anv".property where objectName='apAgingSummary' limit 1), (select id from "anv".container where code='report' limit 1), 6, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='ACCOUNTING_MODULE' limit 1), (select id from "anv".property where objectName='journalReport' limit 1), (select id from "anv".container where code='report' limit 1), 7, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='ACCOUNTING_MODULE' limit 1), (select id from "anv".property where objectName='transactionsByPeriod' limit 1), (select id from "anv".container where code='report' limit 1), 8, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='ACCOUNTING_MODULE' limit 1), (select id from "anv".property where objectName='stockValuation' limit 1), (select id from "anv".container where code='report' limit 1), 9, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='ACCOUNTING_MODULE' limit 1), (select id from "anv".property where objectName='vatReturns' limit 1), (select id from "anv".container where code='report' limit 1), 11, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='ACCOUNTING_MODULE' limit 1), (select id from "anv".property where objectName='vatReturnsSaudiOrUae' limit 1), (select id from "anv".container where code='report' limit 1), 12, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='ACCOUNTING_MODULE' limit 1), (select id from "anv".property where objectName='vatReturn' limit 1), (select id from "anv".container where code='report' limit 1), 13, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='ACCOUNTING_MODULE' limit 1), (select id from "anv".property where objectName='vatReturns' limit 1), (select id from "anv".container where code='report' limit 1), 14, 'accounting');



insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='INVENTORY_MANAGEMENT' limit 1), (select id from "anv".property where objectName='warehouseList' limit 1), (select id from "anv".container where code='warehouse' limit 1), 1, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='INVENTORY_MANAGEMENT' limit 1), (select id from "anv".property where objectName='STOCK_ADJUSTMENT' limit 1), (select id from "anv".container where code='warehouse' limit 1), 2, 'accounting');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='INVENTORY_MANAGEMENT' limit 1), (select id from "anv".property where objectName='STOCK_TRANSFER' limit 1), (select id from "anv".container where code='warehouse' limit 1), 3, 'accounting');
