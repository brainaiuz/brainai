insert into permission (code, context, name, parent, modulecode)
values ('COPY_PI_TO_SI', 'ACCOUNTING', 'Copy To Sale Invoice',
        (select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'), 'SALES_INVOICING');

insert into "anv".permission_context (permissioncode, contextcode)
values ('COPY_PI_TO_SI', 'ACCOUNTING');

insert into "anv".rolepermission (permissioncode, access, rolecode)
  values ('COPY_PI_TO_SI', 'ALLOW', 'ADMIN');

  insert into "anv".rolepermission (permissioncode, access, rolecode)
  values ('COPY_PI_TO_SI', 'ALLOW', 'DR');

    insert into "anv".rolepermission (permissioncode, access, rolecode)
  values ('COPY_PI_TO_SI', 'ALLOW', 'ACCOUNTANT');