--AWS_RFP_multi_approver_Faxriddin_For_public.sql shundan kiyin urilsin bu patch!!!

DROP FUNCTION IF EXISTS "anv".createRFPDefaultApprovers();
CREATE OR REPLACE FUNCTION "anv".createRFPDefaultApprovers()
  RETURNS INTEGER AS
$body$
DECLARE
  approverID          INTEGER;
  createdWorkflowID   INTEGER;
  rfp_approver_id     INTEGER;
  managerId           INTEGER;
  sq   RECORD;

BEGIN

  IF EXISTS(SELECT id FROM "anv".workflowrule WHERE module = '_WORKFLOW_MODULE_REQUEST_FOR_PURCHASE' AND executioncriteria = '_WORKFLOW_EXECUTION_CRITERIA_CREATE' AND showinlist IS true)
  THEN
    UPDATE "anv".workflowrule SET deleted = TRUE  WHERE module = '_WORKFLOW_MODULE_REQUEST_FOR_PURCHASE' AND executioncriteria = '_WORKFLOW_EXECUTION_CRITERIA_CREATE' AND showinlist IS true;
    UPDATE "anv".workflow_alerts SET deleted = TRUE  WHERE workflow IN (SELECT id  FROM "anv".workflowrule  WHERE module = '_WORKFLOW_MODULE_REQUEST_FOR_PURCHASE' AND executioncriteria = '_WORKFLOW_EXECUTION_CRITERIA_CREATE' AND showinlist IS true);
  END IF;

  INSERT INTO "anv".workflowrule (name, executioncriteria, module, showinlist,status) VALUES ('Request For Purchase create', '_WORKFLOW_EXECUTION_CRITERIA_CREATE', '_WORKFLOW_MODULE_REQUEST_FOR_PURCHASE', true,'_WORKFLOW_STATUS_ACTIVE')   RETURNING id INTO createdWorkflowID;
  INSERT INTO "anv".workflow_alerts (deleted, extrakeyvalues, recepient, replyto, subject, tobcc, ccemails, emailtemplateid, workflow, workflowactionstarttimeunit, workflowactionstarttime, workflowactionstarttimegranularity, isworkflowactiontimebased, content)
  VALUES (FALSE, NULL, '${email}', NULL, 'Request For Purchase ${number} has added by  ${first_name} ${last_name}', '', '', NULL, createdWorkflowID, 0, 'TRIGGER_TIME', 'MINUTES', FALSE,
   '<p>Dear ${current_approver},</p>
          <p>Please be advised that ${first_name} ${last_name} has added the Request For Purchase Number ${number} .</p>
          You can view this Request For Purchase by clicking <a href="${rfp_link}">here</a><br />
          (or copy/paste and go to following link in your web browser:<br />
          ${rfp_link}
          <p>&nbsp;</p>'
  );

  RETURN NULL;
END;
$body$
LANGUAGE plpgsql;

ALTER FUNCTION "anv".createRFPDefaultApprovers() OWNER TO wfmtest;

UPDATE company SET selectFunctioncolumn =(SELECT "anv".createRFPDefaultApprovers()) where id=anv;