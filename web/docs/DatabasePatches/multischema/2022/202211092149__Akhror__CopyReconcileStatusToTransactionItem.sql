DROP function if EXISTS "anv".copyReconcileStatus();
CREATE OR replace function "anv".copyReconcileStatus()
    returns INTEGER AS
$body$
DECLARE  role record;
BEGIN

    FOR role IN (SELECT * FROM "anv".transaction  order by id)
        loop
            update "anv".transactionItem set reconcileStatus = role.reconcileStatus where transactionid = role.id;
        END loop;
    return NULL;
END;
$body$
    LANGUAGE plpgsql;
ALTER function "anv".copyReconcileStatus() owner TO wfmtest;



UPDATE company SET selectFunctioncolumn =(SELECT "anv".copyReconcileStatus()) WHERE  id=(SELECT id FROM company LIMIT 1);