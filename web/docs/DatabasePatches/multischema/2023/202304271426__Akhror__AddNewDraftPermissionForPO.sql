insert into permission(code, context, name, sorder, parent, modulecode)
values ('PO_DRAFT', 'ACCOUNTING', 'Draft', (select max(sorder)
                                            from permission
                                            where parent =
                                                  (select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST')),
        (select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'), 'PURCHASE_ORDERS');
insert into "anv".permission_context(permissioncode, contextcode)
values ('PO_DRAFT', 'ACCOUNTING');

insert into "anv".rolepermission(permissioncode, access, rolecode)
values ('PO_DRAFT', 'ALLOW', 'ACCOUNTANT');
insert into "anv".rolepermission(permissioncode, access, rolecode)
values ('PO_DRAFT', 'ALLOW', 'ADMIN');