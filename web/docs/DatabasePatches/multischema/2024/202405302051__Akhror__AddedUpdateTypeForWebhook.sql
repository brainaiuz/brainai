insert into myupdatetype(code, description, parentid)
values ('SALES_INVOICE_WEBHOOK_RESPONSE_ADD', 'Records when sales invoice webhook response added',
        (select id from myupdatetype where code = 'SALES_INVOICE')),
       ('SALES_QUOTE_WEBHOOK_RESPONSE_ADD', 'Records when sales quote webhook response added',
        (select id from myupdatetype where code = 'SALES_QUOTE')),
       ('EXPENSE_REPORT_WEBHOOK_RESPONSE_ADD', 'Records when expense report webhook response added',
        (select id from myupdatetype where code = 'EXPENSE_REPORT')),
       ('PURCHASE_INVOICE_WEBHOOK_RESPONSE_ADD', 'Records when purchase invoice webhook response added',
        (select id from myupdatetype where code = 'PURCHASE_INVOICE')),
       ('PURCHASE_ORDER_WEBHOOK_RESPONSE_ADD', 'Records when purchase order webhook response added',
        (select id from myupdatetype where code = 'PURCHASE_ORDER'));