insert into permission (code, context, name, parent, modulecode)
values ('SEND_INVOICE_TO_ZATCA', 'ACCOUNTING', 'Send To Zatca',
        (select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'), 'SALES_INVOICING');

insert into "anv".permission_context (permissioncode, contextcode)
values ('SEND_INVOICE_TO_ZATCA', 'ACCOUNTING');

insert into "anv".rolepermission (permissioncode, access, rolecode)
  values ('SEND_INVOICE_TO_ZATCA', 'ALLOW', 'ADMIN');
  
  insert into "anv".rolepermission (permissioncode, access, rolecode)
  values ('SEND_INVOICE_TO_ZATCA', 'ALLOW', 'DR');
  
    insert into "anv".rolepermission (permissioncode, access, rolecode)
  values ('SEND_INVOICE_TO_ZATCA', 'ALLOW', 'ACCOUNTANT');