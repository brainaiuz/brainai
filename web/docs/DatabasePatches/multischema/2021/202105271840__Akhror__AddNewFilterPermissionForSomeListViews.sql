delete from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST_FILTER';
delete from permission where code = 'ACCOUNTING_SALES_ORDER_LIST_FILTER';
delete from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST_FILTER';
insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('ACCOUNTING_SALES_QUOTE_LIST_FILTER', 'ACCOUNTING', false, 'Filter', 50,
        (select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'), true, 'SALES_QUOTES'),
        ('ACCOUNTING_SALES_ORDER_LIST_FILTER', 'ACCOUNTING', false, 'Filter', 50,
        (select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'), true, 'SALES_ORDERS'),
        ('ACCOUNTING_SALES_INVOICE_LIST_FILTER', 'ACCOUNTING', false, 'Filter', 50,
        (select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'), true, 'SALES_INVOICING');


delete from "anv".permission_context where permissioncode = 'ACCOUNTING_SALES_QUOTE_LIST_FILTER';
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_SALES_ORDER_LIST_FILTER';
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_SALES_INVOICE_LIST_FILTER';
insert into "anv".permission_context(permissioncode, contextcode)
values ('ACCOUNTING_SALES_QUOTE_LIST_FILTER', 'ACCOUNTING'),
('ACCOUNTING_SALES_ORDER_LIST_FILTER', 'ACCOUNTING'),
('ACCOUNTING_SALES_INVOICE_LIST_FILTER', 'ACCOUNTING');



DROP function if EXISTS "anv".insertListingFilter();
CREATE OR replace function "anv".insertListingFilter()
    returns INTEGER AS
$body$
DECLARE  role record;
BEGIN

    FOR role IN (SELECT * FROM "anv".role  order by id)
        loop
            insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_SALES_QUOTE_LIST_FILTER', 'ALLOW', role.code);
            insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_SALES_ORDER_LIST_FILTER', 'ALLOW', role.code);
            insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_SALES_INVOICE_LIST_FILTER', 'ALLOW', role.code);
        END loop;
    return NULL;
END;
$body$
    LANGUAGE plpgsql;
ALTER function "anv".insertListingFilter() owner TO wfmtest;



UPDATE company SET selectFunctioncolumn =(SELECT "anv".insertListingFilter()) WHERE  id=(SELECT id FROM company LIMIT 1);
