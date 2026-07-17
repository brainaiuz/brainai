UPDATE "anv".account SET name = 'Output VAT' WHERE key = 2202 AND name = 'VAT Liability';

UPDATE "anv".transactionitem ti SET accountid = (SELECT id FROM "anv".account WHERE key = 1248 and deleted is not true limit 1)
FROM (
    SELECT ti.id AS ti_id, a.id AS account_id FROM "anv".transactionitem ti
    JOIN "anv".account a ON a.id = ti.accountid
    JOIN "anv".transaction t ON t.id = ti.transactionid
    LEFT JOIN "anv".invoice inv ON inv.id = t.invoiceid AND inv.type = 'PAYABLE'
    LEFT JOIN "anv".spendreceivemoney bt ON bt.id = t.banktransferid AND (bt.transferType = 1 AND bt.transferType = 3)
    LEFT JOIN "anv".expenseReport er ON er.id = t.expenseReportid
    LEFT jOIN "anv".fixedasset ft ON ft.id = t.fixedassetid
    WHERE t.deleted IS NOT TRUE AND a.key = 2202 AND (inv.id IS NOT NULL
    OR bt.id IS NOT NULL
    OR er.id IS NOT NULL
    OR ft.id IS NOT NULL)) t
WHERE ti.id = t.ti_id;