update "anv".transactionitem ti1
SET department_id = (SELECT ti2.department_id
                     FROM "anv".transactionitem ti2
                       JOIN "anv".account a2 ON (a2.id = ti2.accountid)
                       JOIN (SELECT t2.id
                             FROM "anv".transaction t2
                               JOIN "anv".invoice i2 ON i2.id = t2.invoiceid
                             WHERE i2.type = 'RECEIVABLE'
                                   AND t2.dtype IN ('EdsInvoiceTransaction')) trans2 ON trans2.id = ti1.transactionid
                     WHERE ti2.department_id IS NOT NULL
                           AND a2.accountcode NOT IN ('100', '2100')
                     LIMIT 1)
from "anv".account a1
WHERE transactionid IN (SELECT t12.id
                        FROM "anv".transaction t12
                          JOIN "anv".invoice i12 ON i12.id = t12.invoiceid
                        WHERE i12.type = 'RECEIVABLE'
                              AND t12.dtype IN ('EdsInvoiceTransaction'))
      AND department_id IS NULL
    and a1.id = ti1.accountid
      AND a1.accountcode NOT IN ('100', '2100')
and exists(select id from "anv".genericsettings gs where gs.key = 'ACCOUNTING_DEPARTMENT_RELATION_ENABLED' and gs.value = 'YES');