--EdsBankTransaction
--Bank opening balance transaction
UPDATE "anv".transaction t set currencyid = b.currencyid, exchangeRate = b.exchangeRate FROM (select b.id, b.exchangeRate, a.currencyid from "anv".bankAccount b INNER JOIN "anv".account a on a.id = b.accountid) b WHERE t.bankaccountid IS NOT NULL AND t.bankaccountid = b.id;

--EdsBankTransferTransaction
--Bank Receive/Spend Cash Receive/Spend
UPDATE "anv".transaction t set currencyid = bt.currencyid, exchangeRate = bt.exchangeRate FROM "anv".spendreceivemoney bt where t.banktransferid is not null and t.banktransferid = bt.id;

--EdsCashAdvanceTransaction
UPDATE "anv".transaction t set currencyid = ca.currency_id, exchangeRate = ca.exchangeRate FROM "anv".cashAdvance ca where t.cashadvance_id is not null and t.cashadvance_id = ca.id;

--EdsCusSuppPaymentTransaction
UPDATE "anv".transaction t set currencyid = cp.currencyID, exchangeRate = cp.exchangeRate FROM "anv".customerPayment cp where t.customerSupplierPaymentID is not null and t.customerSupplierPaymentID = cp.id;

--EdsExpensePaymentTransaction
UPDATE "anv".expensePayments ep set currencyid = er.currencyID FROM "anv".expenseReport er WHERE ep.expenseReportId = er.id;
UPDATE "anv".transaction t set currencyid = jt.currencyID, exchangeRate = jt.exchangeRate FROM "anv".expensePayments jt where t.expensePaymentId is not null and t.expensePaymentId = jt.id;


--EdsExpenseTransaction
UPDATE "anv".transaction t set currencyid = jt.currencyID, exchangeRate = jt.exchageRate FROM "anv".expenseReport jt where t.expenseReportid is not null and t.expenseReportid = jt.id;

--EdsGoodsReceivedTransaction
UPDATE "anv".transaction t set currencyid = jt.currency_id, exchangeRate = jt.exchangeRate FROM "anv".quote jt where t.purchaseorder_id is not null and t.purchaseorder_id = jt.id and jt.type = 'PAYABLE';

--EdsInvoicePaymentTransaction
UPDATE "anv".transaction t set currencyid = jt.currencyid, exchangeRate = jt.exchangeRate FROM "anv".invoicePayments jt where t.invoicePaymentId is not null and t.invoicePaymentId = jt.id;

--EdsInvoiceTransaction
UPDATE "anv".transaction t set currencyid = jt.currency_id, exchangeRate = jt.exchangeRate FROM "anv".invoice jt where t.invoiceid is not null and t.invoiceid = jt.id;

--EdsManualTransaction
UPDATE "anv".transaction t set currencyid = jt.currencyid, exchangeRate = jt.exchangeRate FROM "anv".manualjournal jt where t.manualjournalid is not null and t.manualjournalid = jt.id;

--EdsPayslipTableTransaction
UPDATE "anv".transaction t set currencyid = jt.currency_id, exchangeRate = jt.exchangeRate FROM "anv".payslipTable jt where t.payslip_table_id is not null and t.payslip_table_id = jt.id;

--EdsSinglePayrunTransaction
UPDATE "anv".transaction t set currencyid = jt.currency_id, exchangeRate = jt.exchangeRate FROM "anv".payslipTableItem jt where t.payrun_id is not null and t.payrun_id = jt.id;






