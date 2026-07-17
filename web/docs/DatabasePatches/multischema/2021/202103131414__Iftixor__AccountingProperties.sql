
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('trashBin', 'Trash Bin', 'Trash Bin', 'Trash Bins', 'TB', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('STOCK_ADJUSTMENT', 'Stock Adjustment', 'Stock Adjustment', 'Stock Adjustments', 'SA', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('consignment', 'Consignment', 'Consignment', 'Consignments', 'CON', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('CASH_RECEIPT', 'Cash Receipt', 'Cash Receipt', 'Cash Receipts', 'CR', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('CASH_PAYMENT', 'Cash Payment', 'Cash Payment', 'Cash Payments', 'CP', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('RECEIVE_MONEY', 'Bank Receipt', 'Bank Receipt', 'Bank Receipts', 'BR', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('SPEND_MONEY', 'Bank Payment', 'Bank Payment', 'Bank Payments', 'BP', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive, iscustom)
values ('customerPrepayment', 'Customer Prepayment', 'Customer Prepayment', 'Customer Prepayments', 'CP', 'accounting', false, false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive, iscustom)
values ('supplierPrepayment', 'Supplier Prepayment', 'Supplier Prepayment', 'Supplier Prepayments', 'SP', 'accounting', false, false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('checkList', 'Write Check', 'Write Check', 'Write Checks', 'WCH', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('manualtransactions', 'Manual Entry', 'Manual Entry', 'Manual Entries', 'ME', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('BATCH_RECEIVE_PAYMENT', 'Receive Payment', 'Receive Payment', 'Receive Payments', 'RP', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive, iscustom)
values ('payBillsList', 'Pay Invoices', 'Pay Invoice', 'Pay Invoices', 'PI', 'accounting', false, false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive, iscustom)
values ('newprofitLoss', 'Profit and Loss', 'Profit and Loss', 'Profit and Loss', 'PL', 'accounting', false, false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive, iscustom)
values ('balanceSheet', 'Balance Sheet', 'Balance Sheet', 'Balance Sheet', 'BSh', 'accounting', false, false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive, iscustom)
values ('trialBalance', 'Trial Balance', 'Trial Balance', 'Trial Balances', 'TB', 'accounting', false, false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('cashFlowStatement', 'Cash Flow', 'Cash Flow', 'Cash Flow', 'CF', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('arAgingSummary', 'Aged Receivable', 'Aged Receivable', 'Aged Receivables', 'AR', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('apAgingSummary', 'Aged Payable', 'Aged Payable', 'Aged Payables', 'AP', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('journalReport', 'Journal Report', 'Journal Report', 'Journal Reports', 'JR', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('transactionsByPeriod', 'Account Transaction', 'Account Transaction', 'Account Transactions', 'AT', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('stockValuation', 'Stock Valuation', 'Stock Valuation', 'Stock Valuations', 'SV', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('vatReturnsSaudiOrUae', 'VAT Report(Saudi Company or UAE Company)', 'VAT Report', 'VAT Reports', 'VR', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('oldGccVatReturn', 'Old VAT Return', 'Old VAT Return', 'Old VAT Return', 'OVR', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('vatReturn', 'VAT Return', 'VAT Return', 'VAT Return', 'VR', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('vatReturns', 'VAT Report', 'VAT Report', 'VAT Reports', 'VR', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('budgetsheetView', 'Budget Manager', 'Budget Manager', 'Budget Managers', 'BM', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('warehouseList', 'Warehouse', 'Warehouse', 'Warehouses', 'W', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('STOCK_TRANSFER', 'Stock Transfer', 'Stock Transfer', 'Stock Transfers', 'ST', 'accounting', false) on conflict do nothing;