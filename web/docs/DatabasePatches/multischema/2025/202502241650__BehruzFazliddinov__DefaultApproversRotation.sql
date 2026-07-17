DROP FUNCTION IF EXISTS "0".createRFPDefaultApprovers();
CREATE OR replace function "0".createRFPDefaultApprovers()
    returns INTEGER AS
$body$
DECLARE
    approverID INTEGER;
    sq record;
BEGIN
    IF EXISTS(SELECT id FROM "0".approvers WHERE entitytype = 'ROTATION' LIMIT 1)
    THEN
        DELETE FROM "0".approver_roles WHERE approver_id IN (SELECT id FROM "0".approvers WHERE entitytype = 'ROTATION');
        DELETE FROM "0".approver_employees WHERE approver_id IN (SELECT id FROM "0".approvers WHERE entitytype = 'ROTATION');
        UPDATE "0".approvers SET deleted=true WHERE entitytype = 'ROTATION';
    END IF;
    insert into "0".approvers(approver_order, entitytype, is_default, onapprovedaction, onrejectedaction) values (1, 'ROTATION', true, 5, 6) RETURNING id INTO approverID;
    insert into "0".approver_roles(approver_id, role_id) (select approverID, r.id from "0".role r where code in ('ADMIN','DR','HR'));
    return NULL;
END;
$body$
    LANGUAGE plpgsql;

ALTER function "0".createRFPDefaultApprovers() owner TO wfmtest;

SELECT "0".createRFPDefaultApprovers();




DROP FUNCTION IF EXISTS "anv".createRFPDefaultApprovers();
CREATE OR replace function "anv".createRFPDefaultApprovers()
    returns INTEGER AS
$body$
DECLARE
    approverID INTEGER;
    sq record;
BEGIN
    IF EXISTS(SELECT id FROM "anv".approvers WHERE entitytype = 'ROTATION' LIMIT 1)
    THEN
        DELETE FROM "anv".approver_roles WHERE approver_id IN (SELECT id FROM "anv".approvers WHERE entitytype = 'ROTATION');
        DELETE FROM "anv".approver_employees WHERE approver_id IN (SELECT id FROM "anv".approvers WHERE entitytype = 'ROTATION');
        UPDATE "anv".approvers SET deleted=true WHERE entitytype = 'ROTATION';
    END IF;
    insert into "anv".approvers(approver_order, entitytype, is_default, onapprovedaction, onrejectedaction) values (1, 'ROTATION', true, 5, 6) RETURNING id INTO approverID;
    insert into "anv".approver_roles(approver_id, role_id) (select approverID, r.id from "anv".role r where code in ('ADMIN','DR','HR'));
    return NULL;
END;
$body$
    LANGUAGE plpgsql;

ALTER function "anv".createRFPDefaultApprovers() owner TO wfmtest;

SELECT "anv".createRFPDefaultApprovers();