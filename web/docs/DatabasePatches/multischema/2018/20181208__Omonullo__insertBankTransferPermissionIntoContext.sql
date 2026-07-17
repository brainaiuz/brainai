insert into "anv".permission_context (permissioncode, contextcode)
select 'ACCOUNTING_BANK_ACCOUNT_RECEIVE_ADD', 'ACCOUNTING'
from permission p
where not exists(select permissioncode
                 from "anv".permission_context
                 where permissioncode = 'ACCOUNTING_BANK_ACCOUNT_RECEIVE_ADD'
                   and contextcode = 'ACCOUNTING') and p.code = 'ACCOUNTING_BANK_ACCOUNT_RECEIVE_ADD';
insert into "anv".permission_context (permissioncode, contextcode)
select 'ACCOUNTING_BANK_ACCOUNT_SPEND_ADD', 'ACCOUNTING'
from permission p
where not exists(select permissioncode
                 from "anv".permission_context
                 where permissioncode = 'ACCOUNTING_BANK_ACCOUNT_SPEND_ADD'
                   and contextcode = 'ACCOUNTING') and p.code = 'ACCOUNTING_BANK_ACCOUNT_SPEND_ADD';
insert into "anv".permission_context (permissioncode, contextcode)
select 'ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_ADD', 'ACCOUNTING'
from permission p
where not exists(select permissioncode
                 from "anv".permission_context
                 where permissioncode = 'ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_ADD'
                   and contextcode = 'ACCOUNTING') and p.code = 'ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_ADD';
insert into "anv".permission_context (permissioncode, contextcode)
select 'ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_ADD', 'ACCOUNTING'
from permission p
where not exists(select permissioncode
                 from "anv".permission_context
                 where permissioncode = 'ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_ADD'
                   and contextcode = 'ACCOUNTING') and p.code = 'ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_ADD';



insert into "0".permission_context (permissioncode, contextcode)
select 'ACCOUNTING_BANK_ACCOUNT_RECEIVE_ADD', 'ACCOUNTING'
from permission p
where not exists(select permissioncode
                 from "0".permission_context
                 where permissioncode = 'ACCOUNTING_BANK_ACCOUNT_RECEIVE_ADD'
                   and contextcode = 'ACCOUNTING') and p.code = 'ACCOUNTING_BANK_ACCOUNT_RECEIVE_ADD';
insert into "0".permission_context (permissioncode, contextcode)
select 'ACCOUNTING_BANK_ACCOUNT_SPEND_ADD', 'ACCOUNTING'
from permission p
where not exists(select permissioncode
                 from "0".permission_context
                 where permissioncode = 'ACCOUNTING_BANK_ACCOUNT_SPEND_ADD'
                   and contextcode = 'ACCOUNTING') and p.code = 'ACCOUNTING_BANK_ACCOUNT_SPEND_ADD';
insert into "0".permission_context (permissioncode, contextcode)
select 'ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_ADD', 'ACCOUNTING'
from permission p
where not exists(select permissioncode
                 from "0".permission_context
                 where permissioncode = 'ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_ADD'
                   and contextcode = 'ACCOUNTING') and p.code = 'ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_ADD';
insert into "0".permission_context (permissioncode, contextcode)
select 'ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_ADD', 'ACCOUNTING'
from permission p
where not exists(select permissioncode
                 from "0".permission_context
                 where permissioncode = 'ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_ADD'
                   and contextcode = 'ACCOUNTING') and p.code = 'ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_ADD';