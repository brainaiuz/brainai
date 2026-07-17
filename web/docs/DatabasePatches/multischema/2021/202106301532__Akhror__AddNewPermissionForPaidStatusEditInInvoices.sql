delete from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_PAID_STATUS_EDIT';
insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('ACCOUNTING_PURCHASE_INVOICE_PAID_STATUS_EDIT', 'ACCOUNTING', false, 'Paid Status Edit', 50,
        (select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'), true, 'PURCHASE_INVOICING');

delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PURCHASE_INVOICE_PAID_STATUS_EDIT';
insert into "anv".permission_context(permissioncode, contextcode)
values ('ACCOUNTING_PURCHASE_INVOICE_PAID_STATUS_EDIT', 'ACCOUNTING');


DROP function if EXISTS "anv".insertPurchaseInvoicePaidStatusEdit();
CREATE OR replace function "anv".insertPurchaseInvoicePaidStatusEdit()
    returns INTEGER AS
$body$
DECLARE
    rolePermission record;
BEGIN

    FOR rolePermission IN (SELECT *
                 FROM "anv".rolepermission
                 where access = 'ALLOW'
                   and permissioncode = 'ACCOUNTING_PURCHASE_INVOICE_FULL_EDIT_ACCESS'
                 order by id)
        loop
            insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_PURCHASE_INVOICE_PAID_STATUS_EDIT', 'ALLOW', rolePermission.rolecode);
        END loop;
    return NULL;
END;
$body$
    LANGUAGE plpgsql;
ALTER function "anv".insertPurchaseInvoicePaidStatusEdit() owner TO wfmtest;



UPDATE company
SET selectFunctioncolumn =(SELECT "anv".insertPurchaseInvoicePaidStatusEdit())
WHERE id = (SELECT id FROM company LIMIT 1);

delete from permission where code = 'ACCOUNTING_SALES_INVOICE_PAID_STATUS_EDIT';
insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('ACCOUNTING_SALES_INVOICE_PAID_STATUS_EDIT', 'ACCOUNTING', false, 'Paid Status Edit', 50,
        (select id from permission where code = 'ACCOUNTING_SALES_INVOICE_FULL_LIST_ACCESS'), true, 'SALES_INVOICING');

delete from "anv".permission_context where permissioncode = 'ACCOUNTING_SALES_INVOICE_PAID_STATUS_EDIT';
insert into "anv".permission_context(permissioncode, contextcode)
values ('ACCOUNTING_SALES_INVOICE_PAID_STATUS_EDIT', 'ACCOUNTING');


DROP function if EXISTS "anv".insertSaleInvoicePaidStatusEdit();
CREATE OR replace function "anv".insertSaleInvoicePaidStatusEdit()
    returns INTEGER AS
$body$
DECLARE
    rolePermission record;
BEGIN

    FOR rolePermission IN (SELECT *
                 FROM "anv".rolepermission
                 where access = 'ALLOW'
                   and permissioncode = 'ACCOUNTING_SALES_INVOICE_FULL_EDIT_ACCESS'
                 order by id)
        loop
            insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_SALES_INVOICE_PAID_STATUS_EDIT', 'ALLOW', rolePermission.rolecode);
        END loop;
    return NULL;
END;
$body$
    LANGUAGE plpgsql;
ALTER function "anv".insertSaleInvoicePaidStatusEdit() owner TO wfmtest;



UPDATE company
SET selectFunctioncolumn =(SELECT "anv".insertSaleInvoicePaidStatusEdit())
WHERE id = (SELECT id FROM company LIMIT 1);