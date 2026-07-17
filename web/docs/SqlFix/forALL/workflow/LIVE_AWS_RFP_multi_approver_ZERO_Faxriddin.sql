--AWS_RFP_multi_approver_Faxriddin_For_public.sql shundan kiyin urilsin bu patch!!!

delete from "0".reference  where code = '_WORKFLOW_MODULE_REQUEST_FOR_PURCHASE';
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
							values('_WORKFLOW_MODULE_REQUEST_FOR_PURCHASE', false, true, 'Request For Purchase', true, (select max(sorder) from "0".reference where parentid = (select id from "0".reference where code = '_WORKFLOW_MODULE')), (select id from "0".reference where code='_WORKFLOW_MODULE'), true);


DO $$
BEGIN
  IF EXISTS (SELECT id  FROM "0".modelfield WHERE form_id = 'REQUEST_FOR_PURCHASE_FORM' LIMIT 1)
  THEN
    DELETE FROM "0".modelfield WHERE form_ID = 'REQUEST_FOR_PURCHASE_FORM';
  END IF;
   INSERT INTO "0".modelfield (form_ID, field_ID, sorder, widget, source, usableByWorkflow, type, disableUpdate, isWorkflowAttribute) VALUES
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

DROP FUNCTION IF EXISTS "0".createRFPDefaultApprovers();
CREATE OR replace function "0".createRFPDefaultApprovers()
  returns INTEGER AS
  $body$
DECLARE
    approverID INTEGER;
    approved1WorkflowID INTEGER;
    rejected1WorkflowID INTEGER;
    sq record;
    BEGIN
    IF EXISTS(SELECT id FROM "0".approvers WHERE entitytype = 'REQUEST_FOR_PURCHASE' LIMIT 1)
        THEN
          DELETE FROM "0".approver_roles WHERE approver_id IN (SELECT id FROM "0".approvers WHERE entitytype = 'REQUEST_FOR_PURCHASE');
          DELETE FROM "0".approver_employees WHERE approver_id IN (SELECT id FROM "0".approvers WHERE entitytype = 'REQUEST_FOR_PURCHASE');
          UPDATE "0".approvers SET deleted=true WHERE entitytype = 'REQUEST_FOR_PURCHASE';
    END IF;
        insert into "0".approvers(approver_order, entitytype, is_default, onapprovedaction, onrejectedaction) values (1, 'REQUEST_FOR_PURCHASE', true, 1, 3) RETURNING id INTO approverID;
        insert into "0".approver_roles(approver_id, role_id) (select approverID, r.id from "0".role r where code in ('ADMIN','DR','ACCOUNTANT'));
    IF EXISTS(SELECT id FROM "0".workflowrule WHERE module = '_WORKFLOW_MODULE_REQUEST_FOR_PURCHASE' AND executioncriteria = '_WORKFLOW_ACTION_APPROVING' AND showinlist IS FALSE)
        THEN
          UPDATE "0".workflowrule SET deleted = TRUE WHERE module = '_WORKFLOW_MODULE_REQUEST_FOR_PURCHASE' AND executioncriteria = '_WORKFLOW_ACTION_APPROVING' AND showinlist IS FALSE;
          UPDATE "0".workflow_alerts SET deleted = TRUE WHERE workflow IN (SELECT id FROM "0".workflowrule WHERE module = '_WORKFLOW_MODULE_REQUEST_FOR_PURCHASE' AND executioncriteria = '_WORKFLOW_ACTION_APPROVING' AND showinlist IS FALSE);
    END IF;
        insert into "0".workflowrule(executioncriteria, module,showinlist,status) values('_WORKFLOW_ACTION_APPROVING', '_WORKFLOW_MODULE_REQUEST_FOR_PURCHASE', false,'_WORKFLOW_STATUS_ACTIVE') RETURNING id into approved1WorkflowID;
        insert into "0".workflowrule(executioncriteria, module,showinlist,status) values('_WORKFLOW_ACTION_APPROVING', '_WORKFLOW_MODULE_REQUEST_FOR_PURCHASE', false,'_WORKFLOW_STATUS_ACTIVE') RETURNING id INTO rejected1WorkflowID;
        INSERT INTO "0".workflow_alerts (deleted, extrakeyvalues, recepient, replyto, subject, tobcc, ccemails, emailtemplateid, workflow, workflowactionstarttimeunit, workflowactionstarttime, workflowactionstarttimegranularity, isworkflowactiontimebased, content) VALUES (false, null, '${email}', null, 'Request For Purchase ${number} has been rejected by ${current_approver}', '', '', null,rejected1WorkflowID, 0, 'TRIGGER_TIME', 'MINUTES', false,
        '<p>Dear ${first_name} ${last_name},</p>
        <p>Please be advised that ${current_approver} has rejected the Request For Purchase Number ${number} on ${updated_date}.</p>
        You can view this request for purchase by clicking <a href="${rfp_link}">here</a><br />
        (or copy/paste and go to following link in your web browser:<br />
        ${rfp_link}
        <p>&nbsp;</p>'
        );
        INSERT INTO "0".workflow_alerts (deleted, extrakeyvalues, recepient, replyto, subject, tobcc, ccemails, emailtemplateid, workflow, workflowactionstarttimeunit, workflowactionstarttime, workflowactionstarttimegranularity, isworkflowactiontimebased, content) VALUES (false, null, '${email}', null, 'Request For Purchase ${number} has been approved by ${current_approver}', '', '', null,approved1WorkflowID, 0, 'TRIGGER_TIME', 'MINUTES', false, 
        '<p>Dear ${first_name} ${last_name},</p>
        <p>Please be advised that ${current_approver} has approved the Request For Purchase Number ${number} on ${updated_date}.<br />
        <br />
        You can view this Request For Purchase and convert it to request for purchase by clicking <a href="${rfp_link}">here</a><br />
        (or copy/paste and go to following link in your web browser:<br />
        ${rfp_link}</p>'
        );
        update "0".approvers set workflow=approved1WorkflowID, rejected_workflow=rejected1WorkflowID where id=approverID;
    return NULL;
    END;
$body$
LANGUAGE plpgsql;

ALTER function "0".createRFPDefaultApprovers() owner TO wfmtest;

SELECT "0".createRFPDefaultApprovers();