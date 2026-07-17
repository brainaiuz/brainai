delete from "0".reference  where code = 'RFQ_SUBMITTED' and parentid = (select id from "0".reference where code='RFQ_STATUS');
delete from "0".reference  where code = 'RFQ_APPROVED' and parentid = (select id from "0".reference where code='RFQ_STATUS');
delete from "0".reference  where code = 'RFQ_DRAFT' and parentid = (select id from "0".reference where code='RFQ_STATUS');
delete from "0".reference  where code = 'RFQ_OPEN' and parentid = (select id from "0".reference where code='RFQ_STATUS');
delete from "0".reference  where code = 'RFQ_PARTIAL_CONVERTED' and parentid = (select id from "0".reference where code='RFQ_STATUS');
delete from "0".reference  where code = 'RFQ_CONVERTED' and parentid = (select id from "0".reference where code='RFQ_STATUS');
delete from "0".reference  where code = 'RFQ_DECLINED' and parentid = (select id from "0".reference where code='RFQ_STATUS');
delete from "0".reference  where code = 'RFQ_STATUS';

insert into "0".reference(code, deleted, isremovable, name, shared, sorder, isactive)
values('RFQ_STATUS', false, true, 'RFQ Status', true, 1, true);
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('RFQ_SUBMITTED', false, true, 'Submitted', true, 2, (select id from "0".reference where code='RFQ_STATUS'), true);
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('RFQ_APPROVED', false, true, 'Approved', true, 3, (select id from "0".reference where code='RFQ_STATUS'), true);
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('RFQ_DRAFT', false, true, 'Draft', true, 4, (select id from "0".reference where code='RFQ_STATUS'), true);
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('RFQ_OPEN', false, true, 'Open', true, 5, (select id from "0".reference where code='RFQ_STATUS'), true);
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('RFQ_PARTIAL_CONVERTED', false, true, 'Partially Converted', true, 6, (select id from "0".reference where code='RFQ_STATUS'), true);
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('RFQ_CONVERTED', false, true, 'Converted', true, 7, (select id from "0".reference where code='RFQ_STATUS'), true);
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('RFQ_DECLINED', false, true, 'Rejected', true, 7, (select id from "0".reference where code='RFQ_STATUS'), true);

delete from "anv".reference  where code = 'RFQ_SUBMITTED' and parentid = (select id from "anv".reference where code='RFQ_STATUS');
delete from "anv".reference  where code = 'RFQ_APPROVED' and parentid = (select id from "anv".reference where code='RFQ_STATUS');
delete from "anv".reference  where code = 'RFQ_DRAFT' and parentid = (select id from "anv".reference where code='RFQ_STATUS');
delete from "anv".reference  where code = 'RFQ_OPEN' and parentid = (select id from "anv".reference where code='RFQ_STATUS');
delete from "anv".reference  where code = 'RFQ_PARTIAL_CONVERTED' and parentid = (select id from "anv".reference where code='RFQ_STATUS');
delete from "anv".reference  where code = 'RFQ_CONVERTED' and parentid = (select id from "anv".reference where code='RFQ_STATUS');
delete from "anv".reference  where code = 'RFQ_DECLINED' and parentid = (select id from "anv".reference where code='RFQ_STATUS');
delete from "anv".reference  where code = 'RFQ_STATUS';

insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, isactive)
values('RFQ_STATUS', false, true, 'RFQ Status', true, 1, true);
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('RFQ_SUBMITTED', false, true, 'Submitted', true, 2, (select id from "anv".reference where code='RFQ_STATUS'), true);
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('RFQ_APPROVED', false, true, 'Approved', true, 3, (select id from "anv".reference where code='RFQ_STATUS'), true);
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('RFQ_DRAFT', false, true, 'Draft', true, 4, (select id from "anv".reference where code='RFQ_STATUS'), true);
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('RFQ_OPEN', false, true, 'Open', true, 5, (select id from "anv".reference where code='RFQ_STATUS'), true);
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('RFQ_PARTIAL_CONVERTED', false, true, 'Partially Converted', true, 6, (select id from "anv".reference where code='RFQ_STATUS'), true);
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('RFQ_CONVERTED', false, true, 'Converted', true, 7, (select id from "anv".reference where code='RFQ_STATUS'), true);
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('RFQ_DECLINED', false, true, 'Rejected', true, 7, (select id from "anv".reference where code='RFQ_STATUS'), true);

delete from "anv".reference  where code = '_WORKFLOW_MODULE_REQUEST_FOR_QUOTE' and parentid = (select id from "anv".reference where code='_WORKFLOW_MODULE');
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('_WORKFLOW_MODULE_REQUEST_FOR_QUOTE', false, true, 'RFQ', true, 6, (select id from "anv".reference where code='_WORKFLOW_MODULE'), true);

delete from "0".reference  where code = '_WORKFLOW_MODULE_REQUEST_FOR_QUOTE' and parentid = (select id from "0".reference where code='_WORKFLOW_MODULE');
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('_WORKFLOW_MODULE_REQUEST_FOR_QUOTE', false, true, 'RFQ', true, 6, (select id from "0".reference where code='_WORKFLOW_MODULE'), true);

delete from model where formid = 'REQUEST_FOR_QUOTE_FORM';
insert into model(active, formid, title, viewname) values(true, 'REQUEST_FOR_QUOTE_FORM', 'Request For Quote Form', 'Request For Quote');

delete from modelfield where form_ID = 'REQUEST_FOR_QUOTE_FORM';
insert into modelfield (form_ID, field_ID, sorder, widget, source, usableByWorkflow, type, disableUpdate, isWorkflowAttribute,
                        split, mandatory, systemMandatory, hide, isCustomField, fullWidth, isEntityField, hideInCustomizeForm, systemDisable)
values ('REQUEST_FOR_QUOTE_FORM', 'DATE', 1, 'DatePicker', null, true, 'Date', true, false, false, false, false, false, false, false, false, false, false),
       ('REQUEST_FOR_QUOTE_FORM', 'DUE_DATE', 2, 'DatePicker', null, true, 'Date', false, false, false, false, false, false, false, false, false, false, false),
       ('REQUEST_FOR_QUOTE_FORM', 'NUMBER', 3, 'TextBox', null, true, 'Text', false, false, false, false, false, false, false, false, false, false, false),
       ('REQUEST_FOR_QUOTE_FORM', 'SQ_NUMBER', 4, 'TextBox', null, true, 'Text', false, false, false, false, false, false, false, false, false, false, false),
       ('REQUEST_FOR_QUOTE_FORM', 'STATUS', 6, 'DropDown', 'REFERENCE@RFQ_STATUS', true, 'Text', true, false, false, false, false, false, false, false, false, false, false),
       ('REQUEST_FOR_QUOTE_FORM', 'CUSTOMER', 7, 'DropDown', 'ACCOUNTING@RFQ_CUSTOMER', true, 'Text', true, false, false, false, false, false, false, false, false, false, false),
       ('REQUEST_FOR_QUOTE_FORM', 'PROJECT', 8, 'DropDown', 'ACCOUNTING@RFQ_PROJECT', true, 'Text', true, false, false, false, false, false, false, false, false, false, false),
       ('REQUEST_FOR_QUOTE_FORM', 'PREV_APPROVER', 2, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('REQUEST_FOR_QUOTE_FORM', 'PREV_APPROVER_EMAIL', 2, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('REQUEST_FOR_QUOTE_FORM', 'PREV_APPROVER_STATUS', 2, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('REQUEST_FOR_QUOTE_FORM', 'CURRENT_APPROVER', 2, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('REQUEST_FOR_QUOTE_FORM', 'CURRENT_APPROVER_EMAIL', 2, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('REQUEST_FOR_QUOTE_FORM', 'CURRENT_APPROVER_STATUS', 2, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('REQUEST_FOR_QUOTE_FORM', 'NEXT_APPROVER', 2, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('REQUEST_FOR_QUOTE_FORM', 'NEXT_APPROVER_EMAIL', 2, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('REQUEST_FOR_QUOTE_FORM', 'NEXT_APPROVER_STATUS', 2, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false);

DROP function IF EXISTS "anv".createRFQDefaultApprovers();
CREATE OR replace function "anv".createRFQDefaultApprovers()
  returns INTEGER AS
$body$
DECLARE
  rfq record;

BEGIN
  FOR rfq IN (SELECT * FROM "anv".rfq WHERE deleted IS NOT TRUE)
  LOOP
    UPDATE "anv".rfq a set overallstatus=(SELECT id FROM "anv".reference WHERE code = a.status AND parentId = (SELECT id FROM "anv".reference WHERE code='RFQ_STATUS')) where id = rfq.id;
  END LOOP;
  return NULL;
END;
$body$
LANGUAGE plpgsql;
ALTER FUNCTION "anv".createRFQDefaultApprovers() OWNER TO wfmtest;
UPDATE company SET selectFunctioncolumn =(SELECT "anv".createRFQDefaultApprovers()) where id||'' = replace('"anv"', '"', '');
