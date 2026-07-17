DROP function IF EXISTS "anv".createPurchaseOrderDefaultApprovers();
CREATE OR replace function "anv".createPurchaseOrderDefaultApprovers()
  returns INTEGER AS
$body$
DECLARE
  approverID          INTEGER;
  approved1WorkflowID INTEGER;
  rejected1WorkflowID INTEGER;
  manual_approver_id  INTEGER;
  er                  record;

BEGIN
  INSERT INTO "anv".approvers (approver_order, entitytype, is_default, onapprovedaction, onrejectedaction)
  VALUES (1, 'TYPE_PURCHASE_ORDER', true, 1, 3) RETURNING id INTO approverID;

  INSERT INTO "anv".approver_roles (approver_id, role_id) (select approverID, r.id from "anv".role r where code in ('ADMIN', 'DR', 'ACCOUNTANT'));
  INSERT INTO "anv".workflowrule (executioncriteria, module, showinlist) VALUES ('_WORKFLOW_ACTION_APPROVING', '_WORKFLOW_MODULE_PURCHASEORDER', false) RETURNING id into approved1WorkflowID;
  INSERT INTO "anv".workflowrule (executioncriteria, module, showinlist) VALUES ('_WORKFLOW_ACTION_APPROVING', '_WORKFLOW_MODULE_PURCHASEORDER', false) RETURNING id INTO rejected1WorkflowID;
  UPDATE "anv".approvers set workflow = approved1WorkflowID, rejected_workflow = rejected1WorkflowID WHERE id = approverID;
  INSERT INTO "anv".workflow_alerts (deleted,
                                     extrakeyvalues,
                                     recepient,
                                     replyto,
                                     subject,
                                     tobcc,
                                     ccemails,
                                     emailtemplateid,
                                     workflow,
                                     workflowactionstarttimeunit,
                                     workflowactionstarttime,
                                     workflowactionstarttimegranularity,
                                     isworkflowactiontimebased,
                                     content)
  VALUES (false, null, '${email}', null, 'Declined your Purchase Order', '', '', null, rejected1WorkflowID, 0, 'TRIGGER_TIME', 'MINUTES', false,
          '<p>Dear ${creator},</p>

         <p>Please be advised that ${current_approver} has declined your Purchase Order on ${decline_date}.</p><br />
         You can review ${current_approver} ''s comments, and make the appropriate changes and resubmit it.<br /><br />
         Click <a href="${link}">here</a> to view and edit the Purchase Order.<br /><br />
         * If you are not able to click on the link, you can copy and paste the following address into your web browser: ${link}');
  INSERT INTO "anv".workflow_alerts (deleted,
                                     extrakeyvalues,
                                     recepient,
                                     replyto,
                                     subject,
                                     tobcc,
                                     ccemails,
                                     emailtemplateid,
                                     workflow,
                                     workflowactionstarttimeunit,
                                     workflowactionstarttime,
                                     workflowactionstarttimegranularity,
                                     isworkflowactiontimebased,
                                     content)
  VALUES (false, null, '${email}', null, 'Approved your Purchase Order', '', '', null, approved1WorkflowID, 0, 'TRIGGER_TIME', 'MINUTES', false,
          '<p>Dear ${creator},</p>

         <p>Please be advised that ${current_approver} has approved your Purchase Order, ${narration} on ${date}.</p><br /><br />
         Click <a href="${link}">here</a> to view the Purchase Order.<br /><br />
         * If you are not able to click on the link, you can copy and paste the following address into your web browser: ${link}');

  FOR er IN (SELECT po.*,q.* FROM "anv".purchaseorder po left join "anv".quote q on po.id= q.id WHERE q.deleted IS NOT TRUE)
    LOOP
      INSERT INTO "anv".approvers(approver_order, entitytype, entity_id, is_default, onapprovedaction, onrejectedaction,workflow, rejected_workflow,exactapprover,status)
      VALUES (1, 'TYPE_PURCHASE_ORDER', er.id, false, 1, 2,approved1WorkflowID, rejected1WorkflowID,er.creator_Id,(SELECT id FROM "anv".reference WHERE id = er.status_id AND parentId = (SELECT id FROM "anv".reference WHERE code='INVOICE_STATUS'))) RETURNING id INTO manual_approver_id;

      UPDATE "anv".quote a set currentapprover=manual_approver_id, prevapprover=manual_approver_id, overallstatus=(SELECT id FROM "anv".reference WHERE id = a.status_id AND parentId = (SELECT id FROM "anv".reference WHERE code='INVOICE_STATUS')) where id = er.id;
    END LOOP;
  return NULL;
END;
$body$
  LANGUAGE plpgsql;
ALTER FUNCTION "anv".createPurchaseOrderDefaultApprovers() OWNER TO wfmtest;
UPDATE company SET selectFunctioncolumn =(SELECT "anv".createPurchaseOrderDefaultApprovers()) WHERE  id=(SELECT id FROM company LIMIT 1);