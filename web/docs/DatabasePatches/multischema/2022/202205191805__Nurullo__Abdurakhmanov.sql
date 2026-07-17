insert into permission(code, context, parent, name, modulecode) values('ACCOUNTING_BANK_ACCOUNT_SPEND_PDF', 'ACCOUNTING',
    (SELECT id from permission where code='ACCOUNTING_BANK_ACCOUNT_SPEND'), 'PDF', 'ACCOUNTING_MODULE')

insert into "anv".permission_context (permissioncode,contextcode) values( 'ACCOUNTING_BANK_ACCOUNT_SPEND_PDF' , 'ACCOUNTING' )