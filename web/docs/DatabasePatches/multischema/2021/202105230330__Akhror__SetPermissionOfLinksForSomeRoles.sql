DROP function if EXISTS "anv".insertAccountingLinkPermissions();
CREATE OR replace function "anv".insertAccountingLinkPermissions()
    returns INTEGER AS
$body$
DECLARE  role record;
BEGIN

    FOR role IN (SELECT * FROM "anv".role where code in ('DR', 'SALESMAN', 'ACCOUNTANT', 'ADMIN', 'SALESPERSON') order by id)
        loop
            insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_SALES_QUOTE_LINKS', 'ALLOW', role.code);
            insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_SALES_ORDER_LINKS', 'ALLOW', role.code);
            insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_PURCHASE_ORDER_LINKS', 'ALLOW', role.code);
        END loop;
    return NULL;
END;
$body$
    LANGUAGE plpgsql;
ALTER function "anv".insertAccountingLinkPermissions() owner TO wfmtest;



UPDATE company SET selectFunctioncolumn =(SELECT "anv".insertAccountingLinkPermissions()) WHERE  id=(SELECT id FROM company LIMIT 1);