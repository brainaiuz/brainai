DROP function if EXISTS "anv".insertStocktransferDraft();
CREATE OR replace function "anv".insertStocktransferDraft()
    returns INTEGER AS
$body$
DECLARE  role record;
BEGIN

    FOR role IN (SELECT * FROM "anv".role  order by id)
        loop
            insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('STOCK_TRANSFER_DRAFT_ADD', 'ALLOW', role.code);
        END loop;
    return NULL;
END;
$body$
    LANGUAGE plpgsql;
ALTER function "anv".insertStocktransferDraft() owner TO wfmtest;



UPDATE company SET selectFunctioncolumn =(SELECT "anv".insertStocktransferDraft()) WHERE  id=(SELECT id FROM company LIMIT 1);