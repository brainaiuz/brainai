insert into myupdatetype(code, description, parentid)
values ('CREDIT_NOTE_WEBHOOK_RESPONSE_ADD', 'Records when credit note webhook response added',
        (select id from myupdatetype where code = 'SALES_INVOICE')),
       ('DEBIT_NOTE_WEBHOOK_RESPONSE_ADD', 'Records when debit note webhook response added',
        (select id from myupdatetype where code = 'PURCHASE_INVOICE'));