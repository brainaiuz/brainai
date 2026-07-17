delete from "0".reference  where code = 'SUBMITTED' and parentid = (select id from "0".reference where code='STOCK_TRANSFER_STATUS');
delete from "0".reference  where code = 'APPROVED' and parentid = (select id from "0".reference where code='STOCK_TRANSFER_STATUS');
delete from "0".reference  where code = 'DECLINED' and parentid = (select id from "0".reference where code='STOCK_TRANSFER_STATUS');
delete from "0".reference  where code = 'DRAFT' and parentid = (select id from "0".reference where code='STOCK_TRANSFER_STATUS');
delete from "0".reference  where code = 'TRANSFERRED' and parentid = (select id from "0".reference where code='STOCK_TRANSFER_STATUS');
delete from "0".reference  where code = 'STOCK_TRANSFER_STATUS';

insert into "0".reference(code, deleted, isremovable, name, shared, sorder, isactive)
values('STOCK_TRANSFER_STATUS', false, true, 'Stock Transfer Status', true, 1, true);

insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('SUBMITTED', false, true, 'Submitted', true, 2, (select id from "0".reference where code='STOCK_TRANSFER_STATUS'), true);

insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('APPROVED', false, true, 'Approved', true, 3, (select id from "0".reference where code='STOCK_TRANSFER_STATUS'), true);

insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('DECLINED', false, true, 'Rejected', true, 4, (select id from "0".reference where code='STOCK_TRANSFER_STATUS'), true);

insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('DRAFT', false, true, 'Draft', true, 5, (select id from "0".reference where code='STOCK_TRANSFER_STATUS'), true);

insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('TRANSFERRED', false, true, 'Transferred', true, 6, (select id from "0".reference where code='STOCK_TRANSFER_STATUS'), true);



delete from "anv".reference  where code = 'SUBMITTED' and parentid = (select id from "anv".reference where code='STOCK_TRANSFER_STATUS');
delete from "anv".reference  where code = 'APPROVED' and parentid = (select id from "anv".reference where code='STOCK_TRANSFER_STATUS');
delete from "anv".reference  where code = 'DECLINED' and parentid = (select id from "anv".reference where code='STOCK_TRANSFER_STATUS');
delete from "anv".reference  where code = 'DRAFT' and parentid = (select id from "anv".reference where code='STOCK_TRANSFER_STATUS');
delete from "anv".reference  where code = 'TRANSFERRED' and parentid = (select id from "anv".reference where code='STOCK_TRANSFER_STATUS');
delete from "anv".reference  where code = 'STOCK_TRANSFER_STATUS';

insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, isactive)
values('STOCK_TRANSFER_STATUS', false, true, 'Stock Transfer Status', true, 1, true);

insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('SUBMITTED', false, true, 'Submitted', true, 2, (select id from "anv".reference where code='STOCK_TRANSFER_STATUS'), true);

insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('APPROVED', false, true, 'Approved', true, 3, (select id from "anv".reference where code='STOCK_TRANSFER_STATUS'), true);

insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('DECLINED', false, true, 'Rejected', true, 4, (select id from "anv".reference where code='STOCK_TRANSFER_STATUS'), true);

insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('DRAFT', false, true, 'Draft', true, 5, (select id from "anv".reference where code='STOCK_TRANSFER_STATUS'), true);

insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('TRANSFERRED', false, true, 'Transferred', true, 6, (select id from "anv".reference where code='STOCK_TRANSFER_STATUS'), true);


delete from "anv".reference  where code = '_WORKFLOW_MODULE_STOCK_TRANSFER' and parentid = (select id from "anv".reference where code='_WORKFLOW_MODULE');
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('_WORKFLOW_MODULE_STOCK_TRANSFER', false, true, 'Stock Transfer', true, 6, (select id from "anv".reference where code='_WORKFLOW_MODULE'), true);

delete from "0".reference  where code = '_WORKFLOW_MODULE_REQUEST_FOR_QUOTE' and parentid = (select id from "0".reference where code='_WORKFLOW_MODULE');
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('_WORKFLOW_MODULE_REQUEST_FOR_QUOTE', false, true, 'Stock Transfer', true, 6, (select id from "0".reference where code='_WORKFLOW_MODULE'), true);

delete from model where formid = 'STOCK_TRANSFER_FORM';
insert into model(active, formid, title, viewname) values(true, 'STOCK_TRANSFER_FORM', 'Stock Transfer Form', 'Stock Transfer');

delete from modelfield where form_ID = 'STOCK_TRANSFER_FORM';
insert into modelfield (form_ID, field_ID, sorder, widget, source, usableByWorkflow, type, disableUpdate, isWorkflowAttribute,
                        split, mandatory, systemMandatory, hide, isCustomField, fullWidth, isEntityField, hideInCustomizeForm, systemDisable)
values ('STOCK_TRANSFER_FORM', 'DATE', 1, 'DatePicker', null, true, 'Date', true, false, false, false, false, false, false, false, false, false, false),

       ('STOCK_TRANSFER_FORM', 'STATUS', 2, 'DropDown', 'REFERENCE@STOCK_TRANSFER_STATUS', true, 'Text', true, false, false, false, false, false, false, false, false, false, false),

       ('STOCK_TRANSFER_FORM', 'PREV_APPROVER', 2, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('STOCK_TRANSFER_FORM', 'PREV_APPROVER_EMAIL', 2, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('STOCK_TRANSFER_FORM', 'PREV_APPROVER_STATUS', 2, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('STOCK_TRANSFER_FORM', 'CURRENT_APPROVER', 2, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('STOCK_TRANSFER_FORM', 'CURRENT_APPROVER_EMAIL', 2, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('STOCK_TRANSFER_FORM', 'CURRENT_APPROVER_STATUS', 2, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('STOCK_TRANSFER_FORM', 'NEXT_APPROVER', 2, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('STOCK_TRANSFER_FORM', 'NEXT_APPROVER_EMAIL', 2, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('STOCK_TRANSFER_FORM', 'NEXT_APPROVER_STATUS', 2, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false);


DROP function IF EXISTS "anv".createStockTransferDefaultApprovers();
CREATE OR replace function "anv".createStockTransferDefaultApprovers()
  returns INTEGER AS
$body$
DECLARE
  stockTransfer record;

BEGIN
  FOR stockTransfer IN (SELECT * FROM "anv".stockTransfer WHERE deleted IS NOT TRUE)
  LOOP
    UPDATE "anv".stockTransfer a set overallstatus=(SELECT id FROM "anv".reference WHERE code = 'TRANSFERRED' AND parentId = (SELECT id FROM "anv".reference WHERE code='STOCK_TRANSFER_STATUS')) where id = stockTransfer.id;
  END LOOP;
  return NULL;
END;
$body$
LANGUAGE plpgsql;
ALTER FUNCTION "anv".createStockTransferDefaultApprovers() OWNER TO wfmtest;
UPDATE company SET selectFunctioncolumn =(SELECT "anv".createStockTransferDefaultApprovers()) where id||'' = replace('"anv"', '"', '');
