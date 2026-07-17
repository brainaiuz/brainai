delete from "0".reference  where code = 'RFQ_SUBMITTED' and parentid = (select id from "0".reference where code='RFQ_STATUS');
delete from "0".reference  where code = 'RFQ_APPROVED' and parentid = (select id from "0".reference where code='RFQ_STATUS');
delete from "0".reference  where code = 'RFQ_DRAFT' and parentid = (select id from "0".reference where code='RFQ_STATUS');
delete from "0".reference  where code = 'RFQ_OPEN' and parentid = (select id from "0".reference where code='RFQ_STATUS');
delete from "0".reference  where code = 'RFQ_PARTIAL_CONVERTED' and parentid = (select id from "0".reference where code='RFQ_STATUS');
delete from "0".reference  where code = 'RFQ_CONVERTED' and parentid = (select id from "0".reference where code='RFQ_STATUS');
delete from "0".reference  where code = 'RFQ_DECLINED' and parentid = (select id from "0".reference where code='RFQ_STATUS');

delete from "0".reference  where code = 'SUBMITTED' and parentid = (select id from "0".reference where code='RFQ_STATUS');
delete from "0".reference  where code = 'APPROVED' and parentid = (select id from "0".reference where code='RFQ_STATUS');
delete from "0".reference  where code = 'DRAFT' and parentid = (select id from "0".reference where code='RFQ_STATUS');
delete from "0".reference  where code = 'OPEN' and parentid = (select id from "0".reference where code='RFQ_STATUS');
delete from "0".reference  where code = 'PARTIAL_CONVERTED' and parentid = (select id from "0".reference where code='RFQ_STATUS');
delete from "0".reference  where code = 'CONVERTED' and parentid = (select id from "0".reference where code='RFQ_STATUS');
delete from "0".reference  where code = 'DECLINED' and parentid = (select id from "0".reference where code='RFQ_STATUS');
delete from "0".reference  where code = 'RFQ_STATUS';

insert into "0".reference(code, deleted, isremovable, name, shared, sorder, isactive)
values('RFQ_STATUS', false, true, 'RFQ Status', true, 1, true);
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('SUBMITTED', false, true, 'Submitted', true, 2, (select id from "0".reference where code='RFQ_STATUS'), true);
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('APPROVED', false, true, 'Approved', true, 3, (select id from "0".reference where code='RFQ_STATUS'), true);
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('DRAFT', false, true, 'Draft', true, 4, (select id from "0".reference where code='RFQ_STATUS'), true);
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('OPEN', false, true, 'Open', true, 5, (select id from "0".reference where code='RFQ_STATUS'), true);
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('PARTIAL_CONVERTED', false, true, 'Partially Converted', true, 6, (select id from "0".reference where code='RFQ_STATUS'), true);
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('CONVERTED', false, true, 'Converted', true, 7, (select id from "0".reference where code='RFQ_STATUS'), true);
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('DECLINED', false, true, 'Rejected', true, 7, (select id from "0".reference where code='RFQ_STATUS'), true);

update "anv".approverhistory set status = null;
update "anv".approvers set status = null;
update "anv".rfq set overallstatus = null;
delete from "anv".reference  where code = 'RFQ_SUBMITTED' and parentid = (select id from "anv".reference where code='RFQ_STATUS');
delete from "anv".reference  where code = 'RFQ_APPROVED' and parentid = (select id from "anv".reference where code='RFQ_STATUS');
delete from "anv".reference  where code = 'RFQ_DRAFT' and parentid = (select id from "anv".reference where code='RFQ_STATUS');
delete from "anv".reference  where code = 'RFQ_OPEN' and parentid = (select id from "anv".reference where code='RFQ_STATUS');
delete from "anv".reference  where code = 'RFQ_PARTIAL_CONVERTED' and parentid = (select id from "anv".reference where code='RFQ_STATUS');
delete from "anv".reference  where code = 'RFQ_CONVERTED' and parentid = (select id from "anv".reference where code='RFQ_STATUS');
delete from "anv".reference  where code = 'RFQ_DECLINED' and parentid = (select id from "anv".reference where code='RFQ_STATUS');

delete from "anv".reference  where code = 'SUBMITTED' and parentid = (select id from "anv".reference where code='RFQ_STATUS');
delete from "anv".reference  where code = 'APPROVED' and parentid = (select id from "anv".reference where code='RFQ_STATUS');
delete from "anv".reference  where code = 'DRAFT' and parentid = (select id from "anv".reference where code='RFQ_STATUS');
delete from "anv".reference  where code = 'OPEN' and parentid = (select id from "anv".reference where code='RFQ_STATUS');
delete from "anv".reference  where code = 'PARTIAL_CONVERTED' and parentid = (select id from "anv".reference where code='RFQ_STATUS');
delete from "anv".reference  where code = 'CONVERTED' and parentid = (select id from "anv".reference where code='RFQ_STATUS');
delete from "anv".reference  where code = 'DECLINED' and parentid = (select id from "anv".reference where code='RFQ_STATUS');
delete from "anv".reference  where code = 'RFQ_STATUS';

insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, isactive)
values('RFQ_STATUS', false, true, 'RFQ Status', true, 1, true);
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('SUBMITTED', false, true, 'Submitted', true, 2, (select id from "anv".reference where code='RFQ_STATUS'), true);
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('APPROVED', false, true, 'Approved', true, 3, (select id from "anv".reference where code='RFQ_STATUS'), true);
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('DRAFT', false, true, 'Draft', true, 4, (select id from "anv".reference where code='RFQ_STATUS'), true);
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('OPEN', false, true, 'Open', true, 5, (select id from "anv".reference where code='RFQ_STATUS'), true);
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('PARTIAL_CONVERTED', false, true, 'Partially Converted', true, 6, (select id from "anv".reference where code='RFQ_STATUS'), true);
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('CONVERTED', false, true, 'Converted', true, 7, (select id from "anv".reference where code='RFQ_STATUS'), true);
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('DECLINED', false, true, 'Rejected', true, 7, (select id from "anv".reference where code='RFQ_STATUS'), true);

DROP function IF EXISTS "anv".createRFQDefaultApprovers();
CREATE OR replace function "anv".createRFQDefaultApprovers()
  returns INTEGER AS
$body$
DECLARE
  rfqId record;

BEGIN
  FOR rfqId IN (SELECT * FROM "anv".rfq WHERE deleted IS NOT TRUE order by id)
  LOOP
    UPDATE "anv".rfq a set overallstatus=(SELECT id FROM "anv".reference WHERE code = a.status AND parentId = (SELECT id FROM "anv".reference WHERE code='RFQ_STATUS')) where id = rfqId.id;
  END LOOP;
  return NULL;
END;
$body$
LANGUAGE plpgsql;
ALTER FUNCTION "anv".createRFQDefaultApprovers() OWNER TO wfmtest;
UPDATE company SET selectFunctioncolumn =(SELECT "anv".createRFQDefaultApprovers()) where id||'' = replace('"anv"', '"', '');
