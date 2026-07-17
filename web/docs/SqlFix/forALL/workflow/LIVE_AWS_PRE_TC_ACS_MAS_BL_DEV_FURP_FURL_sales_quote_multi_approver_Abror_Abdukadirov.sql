INSERT INTO modelfield(form_ID, 				      field_ID, 	 						sorder, widget , 				source, 	usableByWorkflow, 	  type, 		disableUpdate, isWorkflowAttribute) VALUES
											('SALEQUOTE_FORM', 'PREV_APPROVER',					 2, 		 null, 	        null,         true, 								null,	    true,          true),
											('SALEQUOTE_FORM', 'PREV_APPROVER_EMAIL',		 2, 		 null, 	        null,         true, 								null,	    true,          true),
											('SALEQUOTE_FORM', 'PREV_APPROVER_STATUS',	 2, 		 null, 	        null,         true, 								null,	    true,          true),
											('SALEQUOTE_FORM', 'CURRENT_APPROVER',			 2, 		 null, 	        null,         true, 								null,	    true,          true),
											('SALEQUOTE_FORM', 'CURRENT_APPROVER_EMAIL', 2, 		 null, 	        null,         true, 								null,	    true,          true),
											('SALEQUOTE_FORM', 'CURRENT_APPROVER_STATUS',2, 		 null, 	        null,         true, 								null,	    true,          true),
											('SALEQUOTE_FORM', 'NEXT_APPROVER',					 2, 		 null, 	        null,         true, 								null,	    true,          true),
											('SALEQUOTE_FORM', 'NEXT_APPROVER_EMAIL',		 2, 		 null, 	        null,         true, 								null,	    true,          true),
											('SALEQUOTE_FORM', 'NEXT_APPROVER_STATUS',	 2, 		 null, 	        null,         true, 								null,	    true,          true);

DO $$
    BEGIN
        IF EXISTS (
            SELECT id FROM "anv".modelfield WHERE form_id = 'SALEQUOTE_FORM' LIMIT 1
        ) THEN
            INSERT INTO "anv".modelfield(form_ID, 				      field_ID, 	 						sorder, widget , 				source, usableByWorkflow, 	  type, 		disableUpdate, isWorkflowAttribute) VALUES
                                            ('SALEQUOTE_FORM', 'PREV_APPROVER',					 2, 		 null, 	        null,   true, 								null,	    true,          true),
                                            ('SALEQUOTE_FORM', 'PREV_APPROVER_EMAIL',		 2, 		 null, 	        null,   true, 								null,	    true,          true),
                                            ('SALEQUOTE_FORM', 'PREV_APPROVER_STATUS',	 2, 		 null, 	        null,   true, 								null,	    true,          true),
                                            ('SALEQUOTE_FORM', 'CURRENT_APPROVER',			 2, 		 null, 	        null,   true, 								null,	    true,          true),
                                            ('SALEQUOTE_FORM', 'CURRENT_APPROVER_EMAIL', 2, 		 null, 	        null,   true, 								null,	    true,          true),
                                            ('SALEQUOTE_FORM', 'CURRENT_APPROVER_STATUS',2, 		 null, 	        null,   true, 								null,	    true,          true),
                                            ('SALEQUOTE_FORM', 'NEXT_APPROVER',					 2, 		 null, 	        null,   true, 								null,	    true,          true),
                                            ('SALEQUOTE_FORM', 'NEXT_APPROVER_EMAIL',		 2, 		 null, 	        null,   true, 								null,	    true,          true),
                                            ('SALEQUOTE_FORM', 'NEXT_APPROVER_STATUS',	 2, 		 null, 	        null,   true, 								null,	    true,          true);
        END IF;
END$$;

DROP function IF EXISTS "anv".createSalesQuoteDefaultApprovers();
CREATE OR replace function "anv".createSalesQuoteDefaultApprovers()
  returns INTEGER AS
  $body$
DECLARE
    approverID INTEGER;
    approved1WorkflowID INTEGER;
    rejected1WorkflowID INTEGER;
    quote_approver_id INTEGER;
    managerId INTEGER;
    sq record;

    BEGIN
        INSERT INTO "anv".approvers(approver_order, entitytype, is_default, onapprovedaction, onrejectedaction) VALUES (1, 'salequote', true, 1, 3) RETURNING id INTO approverID;
        INSERT INTO "anv".approver_roles(approver_id, role_id) (select approverID, r.id from "anv".role r where code in ('ADMIN','DR','ACCOUNTANT'));
        INSERT INTO "anv".workflowrule(executioncriteria, module,showinlist) VALUES('_WORKFLOW_ACTION_APPROVING', '_WORKFLOW_MODULE_SALEQUOTE', false) RETURNING id into approved1WorkflowID;
        INSERT INTO "anv".workflowrule(executioncriteria, module,showinlist) VALUES('_WORKFLOW_ACTION_APPROVING', '_WORKFLOW_MODULE_SALEQUOTE', false) RETURNING id INTO rejected1WorkflowID;
        UPDATE "anv".approvers set workflow=approved1WorkflowID, rejected_workflow=rejected1WorkflowID WHERE id=approverID;
        INSERT INTO "anv".workflow_alerts (deleted, extrakeyvalues, recepient, replyto, subject, tobcc, ccemails, emailtemplateid, workflow, workflowactionstarttimeunit, workflowactionstarttime, workflowactionstarttimegranularity, isworkflowactiontimebased, content) VALUES (false, null, '${email}', null, 'Sales quote ${number} has been rejected by ${current_approver}', '', '', null,rejected1WorkflowID, 0, 'TRIGGER_TIME', 'MINUTES', false, '<p>Dear ${firstname} ${lastname},</p>

<p>Please be advised that ${current_approver} has rejected the Quote Number ${number} on ${updated_date}.</p>
You can view this quote by clicking <a href="${sale_quote_link}">here</a><br />
(or copy/paste and go to following link in your web browser:<br />
${sale_quote_link}
<p>&nbsp;</p>'
);
        INSERT INTO "anv".workflow_alerts (deleted, extrakeyvalues, recepient, replyto, subject, tobcc, ccemails, emailtemplateid, workflow, workflowactionstarttimeunit, workflowactionstarttime, workflowactionstarttimegranularity, isworkflowactiontimebased, content) VALUES (false, null, '${email}', null, 'Sales quote ${number} has been approved by ${current_approver}', '', '', null,approved1WorkflowID, 0, 'TRIGGER_TIME', 'MINUTES', false, '<p>Dear ${firstname} ${lastname},</p>

<p>Please be advised that ${current_approver} has approved the Quote Number ${number} on ${updated_date}.<br />
<br />
You can view this quote and convert it to Sales Invoice by clicking <a href="${sale_quote_link}">here</a><br />
(or copy/paste and go to following link in your web browser:<br />
${sale_quote_link}</p>'
);
        FOR sq IN (SELECT isq.id, q.creator_id, q.status_id, isq.managerId FROM "anv".salequote isq
                            left join "anv".quote q on q.id = isq.id
                            where q.deleted = false
                            and (isq.isSalesOrder = false or isq.isSalesOrder is null)
                  )
        LOOP
          managerId = (SELECT q.managerId FROM "anv".salequote q where q.id = sq.id);
          IF managerId > 0
          THEN
            INSERT INTO "anv".approvers(approver_order, entitytype, entity_id, is_default, onapprovedaction, onrejectedaction,workflow, rejected_workflow,exactapprover,status)
                  values (1, 'salequote', sq.id, false, 1, 2,approved1WorkflowID, rejected1WorkflowID,sq.managerId,sq.status_id) RETURNING id INTO quote_approver_id;
          ELSE
            INSERT INTO "anv".approvers(approver_order, entitytype, entity_id, is_default, onapprovedaction, onrejectedaction,workflow, rejected_workflow,exactapprover,status)
                  values (1, 'salequote', sq.id, false, 1, 2,approved1WorkflowID, rejected1WorkflowID,sq.creator_id,sq.status_id) RETURNING id INTO quote_approver_id;
          END IF;
          UPDATE "anv".quote q SET currentapprover=quote_approver_id, prevapprover=quote_approver_id, overallstatus=q.status_id WHERE id = sq.id;
        END LOOP;
    return NULL;
    END;
$body$
LANGUAGE plpgsql;
ALTER function "anv".createSalesQuoteDefaultApprovers() owner TO wfmtest;
UPDATE company SET isdeleted = FALSE WHERE isdeleted IS NULL AND (select "anv".createSalesQuoteDefaultApprovers()) IS NOT NULL;