

delete from permission where code='ACCOUNTING_SALES_INVOICE_SEND_EMAIL';
insert into permission (code,                 context,     ismainmenu,   name,                sorder,  parent,                                                                iscore, modulecode,          isadvancedmode)
values ('ACCOUNTING_SALES_INVOICE_SEND_EMAIL','ACCOUNTING',false,        'Send email button', 14,     (select id from permission where code='ACCOUNTING_SALES_INVOICE_LIST'), false,  'ACCOUNTING_MODULE', false );