
--accounting sq so draft
delete from permission where code='ACCOUNTING_SO_AND_SQ_DRAFT';
insert into permission (code, context, name, sorder, parent, modulecode) values ('ACCOUNTING_SO_AND_SQ_DRAFT', 'ACCOUNTING', 'Draft',
                                                                                 (SELECT max(sorder) + 1 from permission WHERE code = 'ACCOUNTING_SALES_ORDER_LIST'),
                                                                                 (select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'), 'SALES_ORDERS');

delete from "anv".permission_context where permissioncode = 'ACCOUNTING_SO_AND_SQ_DRAFT';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_SO_AND_SQ_DRAFT', 'ACCOUNTING');

delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_SO_AND_SQ_DRAFT';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_SO_AND_SQ_DRAFT', 'ALLOW', 'ADMIN'),
                                                                           ('ACCOUNTING_SO_AND_SQ_DRAFT', 'ALLOW', 'ACCOUNTANT');