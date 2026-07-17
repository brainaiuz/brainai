


delete from "anv".reference  where code = 'SUBMITTED' and parentid = (select id from "anv".reference where code='STOCK_ADJUSTMENT_STATUS');
delete from "anv".reference  where code = 'APPROVED' and parentid = (select id from "anv".reference where code='STOCK_ADJUSTMENT_STATUS');
delete from "anv".reference  where code = 'DECLINED' and parentid = (select id from "anv".reference where code='STOCK_ADJUSTMENT_STATUS');
delete from "anv".reference  where code = 'DRAFT' and parentid = (select id from "anv".reference where code='STOCK_ADJUSTMENT_STATUS');
delete from "anv".reference  where code = 'STOCK_ADJUSTMENT_STATUS';

insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, isactive)
values('STOCK_ADJUSTMENT_STATUS', false, true, 'Stock Adjustment Status', true, 1, true);

insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('SUBMITTED', false, true, 'Submitted', true, 2, (select id from "anv".reference where code='STOCK_ADJUSTMENT_STATUS'), true);

insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('APPROVED', false, true, 'Approved', true, 3, (select id from "anv".reference where code='STOCK_ADJUSTMENT_STATUS'), true);

insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('DECLINED', false, true, 'Rejected', true, 4, (select id from "anv".reference where code='STOCK_ADJUSTMENT_STATUS'), true);

insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('DRAFT', false, true, 'Draft', true, 5, (select id from "anv".reference where code='STOCK_ADJUSTMENT_STATUS'), true);


delete from "anv".reference  where code = '_WORKFLOW_MODULE_STOCK_ADJUSTMENT' and parentid = (select id from "anv".reference where code='_WORKFLOW_MODULE');
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('_WORKFLOW_MODULE_STOCK_ADJUSTMENT', false, true, 'Stock Adjustment', true, 6, (select id from "anv".reference where code='_WORKFLOW_MODULE'), true);

delete from model where formid = 'STOCKADJUSTMENTS_FORM';
insert into model(active, formid, title, viewname) values(true, 'STOCKADJUSTMENTS_FORM', 'Stock Adjustment Form', 'Stock Adjustment');

delete from modelfield where form_ID = 'STOCKADJUSTMENTS_FORM';
insert into modelfield (form_ID, field_ID, sorder, widget, source, usableByWorkflow, type, disableUpdate, isWorkflowAttribute,
                        split, mandatory, systemMandatory, hide, isCustomField, fullWidth, isEntityField, hideInCustomizeForm, systemDisable)
values ('STOCKADJUSTMENTS_FORM', 'DATE', 1, 'DatePicker', null, true, 'Date', true, false, false, false, false, false, false, false, false, false, false),

       ('STOCKADJUSTMENTS_FORM', 'STATUS', 2, 'DropDown', 'REFERENCE@STOCK_ADJUSTMENT_STATUS', true, 'Text', true, false, false, false, false, false, false, false, false, false, false),

       ('STOCKADJUSTMENTS_FORM', 'PREV_APPROVER', 3, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('STOCKADJUSTMENTS_FORM', 'PREV_APPROVER_EMAIL', 4, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('STOCKADJUSTMENTS_FORM', 'PREV_APPROVER_STATUS', 5, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('STOCKADJUSTMENTS_FORM', 'CURRENT_APPROVER', 6, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('STOCKADJUSTMENTS_FORM', 'CURRENT_APPROVER_EMAIL', 7, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('STOCKADJUSTMENTS_FORM', 'CURRENT_APPROVER_STATUS', 8, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('STOCKADJUSTMENTS_FORM', 'NEXT_APPROVER', 9, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('STOCKADJUSTMENTS_FORM', 'NEXT_APPROVER_EMAIL', 10, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('STOCKADJUSTMENTS_FORM', 'NEXT_APPROVER_STATUS', 11, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false);


DROP function IF EXISTS "anv".createStockAdjustmentDefaultApprovers();
CREATE OR replace function "anv".createStockAdjustmentDefaultApprovers()
  returns INTEGER AS
$body$
DECLARE
  stock_adjustment record;

BEGIN
  FOR stock_adjustment IN (SELECT * FROM "anv".stock_adjustment WHERE deleted IS NOT TRUE)
  LOOP
    UPDATE "anv".stock_adjustment a set overallstatus=(SELECT id FROM "anv".reference WHERE code = 'APPROVED' AND parentId = (SELECT id FROM "anv".reference WHERE code='STOCK_ADJUSTMENT_STATUS')) where id = stock_adjustment.id;
  END LOOP;
  return NULL;
END;
$body$
LANGUAGE plpgsql;
ALTER FUNCTION "anv".createStockAdjustmentDefaultApprovers() OWNER TO wfmtest;
UPDATE company SET selectFunctioncolumn =(SELECT "anv".createStockAdjustmentDefaultApprovers()) where id||'' = replace('"anv"', '"', '');
