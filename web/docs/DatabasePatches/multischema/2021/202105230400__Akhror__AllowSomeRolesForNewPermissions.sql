DROP function if EXISTS "anv".insertAccountingHistoryNotesPermissions();
CREATE OR replace function "anv".insertAccountingHistoryNotesPermissions()
    returns INTEGER AS
$body$
DECLARE  role record;
BEGIN

    FOR role IN (SELECT * FROM "anv".role where code in ('DR', 'ADMIN', 'ACCOUNTANT') order by id)
        loop
            insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_SALES_QUOTE_HISTORY_NOTES', 'ALLOW', role.code);
            insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_SALES_ORDER_HISTORY_NOTES', 'ALLOW', role.code);
            insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_SALES_INVOICE_HISTORY_NOTES', 'ALLOW', role.code);
            insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_PURCHASE_ORDER_HISTORY_NOTES', 'ALLOW', role.code);
            insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_PURCHASE_INVOICE_HISTORY_NOTES', 'ALLOW', role.code);
            insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_EXPENSE_REPORT_HISTORY_NOTES', 'ALLOW', role.code);
        END loop;
    return NULL;
END;
$body$
    LANGUAGE plpgsql;
ALTER function "anv".insertAccountingHistoryNotesPermissions() owner TO wfmtest;



UPDATE company SET selectFunctioncolumn =(SELECT "anv".insertAccountingHistoryNotesPermissions()) WHERE  id=(SELECT id FROM company LIMIT 1);


DROP function if EXISTS "anv".insertAccountingListingCustomizePermissions();
CREATE OR replace function "anv".insertAccountingListingCustomizePermissions()
    returns INTEGER AS
$body$
DECLARE  role record;
BEGIN

    FOR role IN (SELECT * FROM "anv".role where code in ('DR', 'ADMIN', 'ACCOUNTANT', 'SALESMAN', 'SALESPERSON') order by id)
        loop
            insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_SALES_QUOTE_LIST_CUSTOMIZE', 'ALLOW', role.code);
            insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_SALES_ORDER_LIST_CUSTOMIZE', 'ALLOW', role.code);
            insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_SALES_INVOICE_LIST_CUSTOMIZE', 'ALLOW', role.code);
            insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_PURCHASE_ORDER_LIST_CUSTOMIZE', 'ALLOW', role.code);
            insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_PURCHASE_INVOICE_LIST_CUSTOMIZE', 'ALLOW', role.code);
            insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_EXPENSE_REPORT_LIST_CUSTOMIZE', 'ALLOW', role.code);
        END loop;
    return NULL;
END;
$body$
    LANGUAGE plpgsql;
ALTER function "anv".insertAccountingListingCustomizePermissions() owner TO wfmtest;



UPDATE company SET selectFunctioncolumn =(SELECT "anv".insertAccountingListingCustomizePermissions()) WHERE  id=(SELECT id FROM company LIMIT 1);


DROP function if EXISTS "anv".insertAccountingSalesOrderApproveSendPermissions();
CREATE OR replace function "anv".insertAccountingSalesOrderApproveSendPermissions()
    returns INTEGER AS
$body$
DECLARE  role record;
BEGIN

    FOR role IN (SELECT * FROM "anv".role where code in ('DR', 'ADMIN', 'ACCOUNTANT', 'SALESMAN', 'SALESPERSON') order by id)
        loop
            insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('SALES_ORDER_APPROVE_EMAIL_SEND', 'ALLOW', role.code);
        END loop;
    return NULL;
END;
$body$
    LANGUAGE plpgsql;
ALTER function "anv".insertAccountingSalesOrderApproveSendPermissions() owner TO wfmtest;



UPDATE company SET selectFunctioncolumn =(SELECT "anv".insertAccountingSalesOrderApproveSendPermissions()) WHERE  id=(SELECT id FROM company LIMIT 1);