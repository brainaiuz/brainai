
--sales order
DELETE FROM permission WHERE code='SALES_ORDER_SUBMIT_AND_EMAIL_SEND';
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
VALUES ('SALES_ORDER_SUBMIT_AND_EMAIL_SEND', 'ACCOUNTING', 'Submit & Email', 52, (SELECT id FROM permission WHERE code = 'ACCOUNTING_SALES_ORDER_LIST'), 'SALES_ORDERS');

DELETE FROM "anv".permission_context WHERE permissioncode = 'SALES_ORDER_SUBMIT_AND_EMAIL_SEND' AND contextcode = 'ACCOUNTING';
INSERT INTO "anv".permission_context (permissioncode, contextcode) values ('SALES_ORDER_SUBMIT_AND_EMAIL_SEND', 'ACCOUNTING');

--delete
DELETE FROM "anv".rolepermission WHERE permissioncode = 'SALES_ORDER_SUBMIT_AND_EMAIL_SEND';

--function insert
DROP function if EXISTS "anv".insertAccountingSubmitAndEmailPermissions();
CREATE
OR replace function "anv".insertAccountingSubmitAndEmailPermissions()
    returns INTEGER AS
$body$
DECLARE
role record;
BEGIN

FOR role IN (SELECT * FROM "anv".role where code in ('ADMIN', 'DR', 'ACCOUNTANT', 'SALESMAN', 'SALESPERSON') order by id)
        loop
            insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('SALES_ORDER_SUBMIT_AND_EMAIL_SEND', 'ALLOW', role.code);
        END loop;
return NULL;
END;
$body$
LANGUAGE plpgsql;
ALTER function "anv".insertAccountingSubmitAndEmailPermissions() owner TO wfmtest;


UPDATE company
SET selectFunctioncolumn =(SELECT "anv".insertAccountingSubmitAndEmailPermissions())
WHERE id = (SELECT id FROM company LIMIT 1);