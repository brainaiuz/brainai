
--sales quote
DELETE FROM permission WHERE code='ACCOUNTING_SALES_QUOTE_UPLOAD_FILES';
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
VALUES ('ACCOUNTING_SALES_QUOTE_UPLOAD_FILES', 'ACCOUNTING', 'Upload Files', 52, (SELECT id FROM permission WHERE code = 'ACCOUNTING_SALES_QUOTE_LIST'), 'SALES_QUOTES');

DELETE FROM "anv".permission_context WHERE permissioncode = 'ACCOUNTING_SALES_QUOTE_UPLOAD_FILES' AND contextcode = 'ACCOUNTING';
INSERT INTO "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_SALES_QUOTE_UPLOAD_FILES', 'ACCOUNTING');

--sales order
DELETE FROM permission WHERE code='ACCOUNTING_SALES_ORDER_UPLOAD_FILES';
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
VALUES ('ACCOUNTING_SALES_ORDER_UPLOAD_FILES', 'ACCOUNTING', 'Upload Files', 52, (SELECT id FROM permission WHERE code = 'ACCOUNTING_SALES_ORDER_LIST'), 'SALES_ORDERS');

DELETE FROM "anv".permission_context WHERE permissioncode = 'ACCOUNTING_SALES_ORDER_UPLOAD_FILES' AND contextcode = 'ACCOUNTING';
INSERT INTO "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_SALES_ORDER_UPLOAD_FILES', 'ACCOUNTING');

--sales invoice
DELETE FROM permission WHERE code='ACCOUNTING_SALES_INVOICE_UPLOAD_FILES';
INSERT INTO permission (code, context, name, sorder, parent, modulecode)
VALUES ('ACCOUNTING_SALES_INVOICE_UPLOAD_FILES', 'ACCOUNTING', 'Upload Files', 52, (SELECT id FROM permission WHERE code = 'ACCOUNTING_SALES_INVOICE_LIST'), 'SALES_INVOICING');

DELETE FROM "anv".permission_context WHERE permissioncode = 'ACCOUNTING_SALES_INVOICE_UPLOAD_FILES' AND contextcode = 'ACCOUNTING';
INSERT INTO "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_SALES_INVOICE_UPLOAD_FILES', 'ACCOUNTING');


--delete
DELETE FROM "anv".rolepermission WHERE permissioncode = 'ACCOUNTING_SALES_QUOTE_UPLOAD_FILES';
DELETE FROM "anv".rolepermission WHERE permissioncode = 'ACCOUNTING_SALES_ORDER_UPLOAD_FILES';
DELETE FROM "anv".rolepermission WHERE permissioncode = 'ACCOUNTING_SALES_INVOICE_UPLOAD_FILES';

--function insert
DROP function if EXISTS "anv".insertAccountingUploadFilesPermissions();
CREATE
OR replace function "anv".insertAccountingUploadFilesPermissions()
    returns INTEGER AS
$body$
DECLARE
role record;
BEGIN

FOR role IN (SELECT * FROM "anv".role where code in ('ADMIN', 'DR', 'ACCOUNTANT') order by id)
        loop
            insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_SALES_QUOTE_UPLOAD_FILES', 'ALLOW', role.code);
            insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_SALES_ORDER_UPLOAD_FILES', 'ALLOW', role.code);
            insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_SALES_INVOICE_UPLOAD_FILES', 'ALLOW', role.code);
        END loop;
return NULL;
END;
$body$
LANGUAGE plpgsql;
ALTER function "anv".insertAccountingUploadFilesPermissions() owner TO wfmtest;


UPDATE company
SET selectFunctioncolumn =(SELECT "anv".insertAccountingUploadFilesPermissions())
WHERE id = (SELECT id FROM company LIMIT 1);