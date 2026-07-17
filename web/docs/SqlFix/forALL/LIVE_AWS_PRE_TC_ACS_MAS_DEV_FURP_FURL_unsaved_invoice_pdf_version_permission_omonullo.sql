delete from permission where code = 'ACCOUNTING_UNSAVED_SALES_INVOICE_PDF';
insert into permission (code, context, name, sorder, parent, modulecode)
    values ('ACCOUNTING_UNSAVED_SALES_INVOICE_PDF', 'ACCOUNTING', 'Unsaved Invoice Pdf Version', 7, (select id from permission where code= 'ACCOUNTING_SALES_INVOICE_LIST'), 'ACCOUNTING_MODULE');

delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_UNSAVED_SALES_INVOICE_PDF';
insert into "anv".rolepermission (permissioncode, access, rolecode)
  values ('ACCOUNTING_UNSAVED_SALES_INVOICE_PDF', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission (permissioncode, access, rolecode)
  values ('ACCOUNTING_UNSAVED_SALES_INVOICE_PDF', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode)
  values ('ACCOUNTING_UNSAVED_SALES_INVOICE_PDF', 'ALLOW', 'ACCOUNTANT');



delete from "0".rolepermission where permissioncode = 'ACCOUNTING_UNSAVED_SALES_INVOICE_PDF';
insert into "0".rolepermission (permissioncode, access, rolecode)
values ('ACCOUNTING_UNSAVED_SALES_INVOICE_PDF', 'ALLOW', 'ADMIN');
insert into "0".rolepermission (permissioncode, access, rolecode)
values ('ACCOUNTING_UNSAVED_SALES_INVOICE_PDF', 'ALLOW', 'DR');
insert into "0".rolepermission (permissioncode, access, rolecode)
values ('ACCOUNTING_UNSAVED_SALES_INVOICE_PDF', 'ALLOW', 'ACCOUNTANT');
