--AWS_RFP_multi_approver_Faxriddin_For_public.sql shundan kiyin urilsin bu patch

delete from "anv".reference  where code = '_WORKFLOW_MODULE_REQUEST_FOR_PURCHASE';
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
							values('_WORKFLOW_MODULE_REQUEST_FOR_PURCHASE', false, true, 'Request for Purchase', true, (select max(sorder) from "anv".reference where parentid = (select id from "anv".reference where code = '_WORKFLOW_MODULE')), (select id from "anv".reference where code='_WORKFLOW_MODULE'), true);

DO $$
BEGIN
  IF EXISTS (SELECT id  FROM "anv".modelfield WHERE form_id = 'REQUEST_FOR_PURCHASE_FORM' LIMIT 1)
  THEN
    DELETE FROM "anv".modelfield WHERE form_ID = 'REQUEST_FOR_PURCHASE_FORM';
  END IF;
   INSERT INTO "anv".modelfield (form_ID, field_ID, sorder, widget, source, usableByWorkflow, type, disableUpdate, isWorkflowAttribute) VALUES
      ('REQUEST_FOR_PURCHASE_FORM', 'DUE_DATE', 2, NULL, NULL, TRUE, NULL, TRUE, TRUE),
      ('REQUEST_FOR_PURCHASE_FORM', 'MANAGER', 2, NULL, NULL, TRUE, NULL, TRUE, TRUE),
      ('REQUEST_FOR_PURCHASE_FORM', 'NUMBER', 2, NULL, NULL, TRUE, NULL, TRUE, TRUE),
      ('REQUEST_FOR_PURCHASE_FORM', 'PROJECT', 2, NULL, NULL, TRUE, NULL, TRUE, TRUE),
      ('REQUEST_FOR_PURCHASE_FORM', 'PREV_APPROVER', 2, NULL, NULL, TRUE, NULL, TRUE, TRUE),
      ('REQUEST_FOR_PURCHASE_FORM', 'PREV_APPROVER_EMAIL', 2, NULL, NULL, TRUE, NULL, TRUE, TRUE),
      ('REQUEST_FOR_PURCHASE_FORM', 'PREV_APPROVER_STATUS', 2, NULL, NULL, TRUE, NULL, TRUE, TRUE),
      ('REQUEST_FOR_PURCHASE_FORM', 'CURRENT_APPROVER', 2, NULL, NULL, TRUE, NULL, TRUE, TRUE),
      ('REQUEST_FOR_PURCHASE_FORM', 'CURRENT_APPROVER_EMAIL', 2, NULL, NULL, TRUE, NULL, TRUE, TRUE),
      ('REQUEST_FOR_PURCHASE_FORM', 'CURRENT_APPROVER_STATUS', 2, NULL, NULL, TRUE, NULL, TRUE, TRUE),
      ('REQUEST_FOR_PURCHASE_FORM', 'NEXT_APPROVER', 2, NULL, NULL, TRUE, NULL, TRUE, TRUE),
      ('REQUEST_FOR_PURCHASE_FORM', 'NEXT_APPROVER_EMAIL', 2, NULL, NULL, TRUE, NULL, TRUE, TRUE),
      ('REQUEST_FOR_PURCHASE_FORM', 'NEXT_APPROVER_STATUS', 2, NULL, NULL, TRUE, NULL, TRUE, TRUE);
END$$;

DROP FUNCTION IF EXISTS "anv".createRFPDefaultApprovers();
CREATE OR REPLACE FUNCTION "anv".createRFPDefaultApprovers()
  RETURNS INTEGER AS
$body$
DECLARE
  approverID          INTEGER;
  approved1WorkflowID INTEGER;
  rejected1WorkflowID INTEGER;
  rfp_approver_id     INTEGER;
  managerId           INTEGER;
  sq                  RECORD;

BEGIN
  IF EXISTS(SELECT id FROM "anv".approvers WHERE entitytype = 'REQUEST_FOR_PURCHASE' AND entity_id IS NULL LIMIT 1)
  THEN
    DELETE FROM "anv".approver_roles WHERE approver_id IN (SELECT id FROM "anv".approvers WHERE entitytype = 'REQUEST_FOR_PURCHASE');
    DELETE FROM "anv".approver_employees WHERE approver_id IN (SELECT id FROM "anv".approvers WHERE entitytype = 'REQUEST_FOR_PURCHASE');
    UPDATE "anv".approvers set deleted =true WHERE entitytype = 'REQUEST_FOR_PURCHASE';
  END IF;

  INSERT INTO "anv".approvers (approver_order, entitytype, is_default, onapprovedaction, onrejectedaction) VALUES (1, 'REQUEST_FOR_PURCHASE', TRUE, 1, 3) RETURNING id INTO approverID;
  INSERT INTO "anv".approver_roles (approver_id, role_id) (SELECT  approverID, r.id FROM "anv".role r WHERE code IN ('ADMIN', 'DR', 'ACCOUNTANT'));

  IF EXISTS(SELECT id FROM "anv".workflowrule WHERE module = '_WORKFLOW_MODULE_REQUEST_FOR_PURCHASE' AND executioncriteria = '_WORKFLOW_ACTION_APPROVING' AND showinlist IS FALSE)
  THEN
    UPDATE "anv".workflowrule SET deleted = TRUE WHERE module = '_WORKFLOW_MODULE_REQUEST_FOR_PURCHASE' AND executioncriteria = '_WORKFLOW_ACTION_APPROVING' AND showinlist IS FALSE;
    UPDATE "anv".workflow_alerts SET deleted = TRUE WHERE workflow IN (SELECT id FROM "anv".workflowrule WHERE module = '_WORKFLOW_MODULE_REQUEST_FOR_PURCHASE' AND executioncriteria = '_WORKFLOW_ACTION_APPROVING' AND showinlist IS FALSE);
  END IF;

  INSERT INTO "anv".workflowrule (name, executioncriteria, module, showinlist,status) VALUES ('Request For Purchase approve', '_WORKFLOW_ACTION_APPROVING', '_WORKFLOW_MODULE_REQUEST_FOR_PURCHASE', FALSE,'_WORKFLOW_STATUS_ACTIVE') RETURNING id INTO approved1WorkflowID;
  INSERT INTO "anv".workflowrule (name, executioncriteria, module, showinlist,status) VALUES ('Request For Purchase reject', '_WORKFLOW_ACTION_APPROVING', '_WORKFLOW_MODULE_REQUEST_FOR_PURCHASE', FALSE,'_WORKFLOW_STATUS_ACTIVE') RETURNING id INTO rejected1WorkflowID;
  INSERT INTO "anv".workflow_alerts (deleted, extrakeyvalues, recepient, replyto, subject, tobcc, ccemails, emailtemplateid, workflow, workflowactionstarttimeunit, workflowactionstarttime, workflowactionstarttimegranularity, isworkflowactiontimebased, content)
  VALUES (FALSE, NULL, '${email}', NULL, 'Request For Purchase ${number} has been rejected by ${current_approver}', '', '', NULL, rejected1WorkflowID, 0, 'TRIGGER_TIME', 'MINUTES', FALSE,
          '<p>Dear ${first_name} ${last_name},</p>
          <p>Please be advised that ${current_approver} has rejected the Request For Purchase Number ${number} on ${updated_date}.</p>
          You can view this Request For Purchase by clicking <a href="${rfp_link}">here</a><br />
          (or copy/paste and go to following link in your web browser:<br />
          ${rfp_link}
          <p>&nbsp;</p>'
  );
  INSERT INTO "anv".workflow_alerts (deleted, extrakeyvalues, recepient, replyto, subject, tobcc, ccemails, emailtemplateid, workflow, workflowactionstarttimeunit, workflowactionstarttime, workflowactionstarttimegranularity, isworkflowactiontimebased, content)
  VALUES (FALSE, NULL, '${email}', NULL, 'Request For Purchase ${number} has been approved by ${current_approver}', '', '', NULL, approved1WorkflowID, 0, 'TRIGGER_TIME', 'MINUTES', FALSE,
          '<p>Dear ${first_name} ${last_name},</p>
          <p>Please be advised that ${current_approver} has approved the Request For Purchase Number ${number} on ${updated_date}.<br />
          <br />
          You can view this Request For Purchase and convert it to Request For Purchase by clicking <a href="${rfp_link}">here</a><br />
          (or copy/paste and go to following link in your web browser:<br />
          ${rfp_link}</p>'
  );
  UPDATE "anv".approvers SET workflow = approved1WorkflowID, rejected_workflow = rejected1WorkflowID WHERE id = approverID;

  FOR sq IN (SELECT q.id, q.creatorid, q.statusid, q.managerid FROM "anv".rfp q WHERE q.deleted IS NOT TRUE)
  LOOP
    managerid = sq.managerId;
    IF managerid > 0
    THEN
      INSERT INTO "anv".approvers (approver_order, entitytype, entity_id, is_default, onapprovedaction, onrejectedaction, workflow, rejected_workflow, exactapprover, status)
      VALUES (1, 'REQUEST_FOR_PURCHASE', sq.id, FALSE, 1, 2, approved1WorkflowID, rejected1WorkflowID, sq.managerid, sq.statusid) RETURNING id INTO rfp_approver_id;
    ELSE
      INSERT INTO "anv".approvers (approver_order, entitytype, entity_id, is_default, onapprovedaction, onrejectedaction, workflow, rejected_workflow, exactapprover, status)
      VALUES (1, 'REQUEST_FOR_PURCHASE', sq.id, FALSE, 1, 2, approved1WorkflowID, rejected1WorkflowID, sq.creatorid, sq.statusid) RETURNING id INTO rfp_approver_id;
    END IF;
    UPDATE "anv".rfp q SET currentapprover = rfp_approver_id, prevapprover = rfp_approver_id, overallstatus = q.statusid WHERE id = sq.id;
  END LOOP;
  RETURN NULL;
END;
$body$
LANGUAGE plpgsql;

ALTER FUNCTION "anv".createRFPDefaultApprovers() OWNER TO wfmtest;

UPDATE company SET selectFunctioncolumn =(SELECT "anv".createRFPDefaultApprovers()) where id=anv;