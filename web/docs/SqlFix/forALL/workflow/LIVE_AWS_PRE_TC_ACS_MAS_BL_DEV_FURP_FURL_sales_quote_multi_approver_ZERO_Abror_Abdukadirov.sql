insert into "0".modelfield(form_ID, 				      field_ID, 	 						sorder, widget , 				source, 													    usableByWorkflow, 	  type, 		disableUpdate, isWorkflowAttribute) values
											('SALEQUOTE_FORM', 'PREV_APPROVER',					 2, 		 null, 	        null,                                 true, 								null,	    true,          true),
											('SALEQUOTE_FORM', 'PREV_APPROVER_EMAIL',		 2, 		 null, 	        null,                                 true, 								null,	    true,          true),
											('SALEQUOTE_FORM', 'PREV_APPROVER_STATUS',	 2, 		 null, 	        null,                                 true, 								null,	    true,          true),
											('SALEQUOTE_FORM', 'CURRENT_APPROVER',			 2, 		 null, 	        null,                                 true, 								null,	    true,          true),
											('SALEQUOTE_FORM', 'CURRENT_APPROVER_EMAIL', 2, 		 null, 	        null,                                 true, 								null,	    true,          true),
											('SALEQUOTE_FORM', 'CURRENT_APPROVER_STATUS',2, 		 null, 	        null,                                 true, 								null,	    true,          true),
											('SALEQUOTE_FORM', 'NEXT_APPROVER',					 2, 		 null, 	        null,                                 true, 								null,	    true,          true),
											('SALEQUOTE_FORM', 'NEXT_APPROVER_EMAIL',		 2, 		 null, 	        null,                                 true, 								null,	    true,          true),
											('SALEQUOTE_FORM', 'NEXT_APPROVER_STATUS',	 2, 		 null, 	        null,                                 true, 								null,	    true,          true);

DROP function IF EXISTS "0".createSalesQuoteDefaultApprovers();
CREATE OR replace function "0".createSalesQuoteDefaultApprovers()
  returns INTEGER AS
  $body$
DECLARE
    approverID INTEGER;
    approved1WorkflowID INTEGER;
    rejected1WorkflowID INTEGER;
    sq record;
    BEGIN
        insert into "0".approvers(approver_order, entitytype, is_default, onapprovedaction, onrejectedaction) values (1, 'salequote', true, 1, 3) RETURNING id INTO approverID;
        insert into "0".approver_roles(approver_id, role_id) (select approverID, r.id from "0".role r where code in ('ADMIN','DR','ACCOUNTANT'));
        insert into "0".workflowrule(executioncriteria, module,showinlist) values('_WORKFLOW_ACTION_APPROVING', '_WORKFLOW_MODULE_SALEQUOTE', false) RETURNING id into approved1WorkflowID;
        insert into "0".workflowrule(executioncriteria, module,showinlist) values('_WORKFLOW_ACTION_APPROVING', '_WORKFLOW_MODULE_SALEQUOTE', false) RETURNING id INTO rejected1WorkflowID;
        update "0".approvers set workflow=approved1WorkflowID, rejected_workflow=rejected1WorkflowID where id=approverID;
        INSERT INTO "0".workflow_alerts (deleted, extrakeyvalues, recepient, replyto, subject, tobcc, ccemails, emailtemplateid, workflow, workflowactionstarttimeunit, workflowactionstarttime, workflowactionstarttimegranularity, isworkflowactiontimebased, content) VALUES (false, null, '${email}', null, 'Sales quote ${number} has been rejected by ${current_approver}', '', '', null,rejected1WorkflowID, 0, 'TRIGGER_TIME', 'MINUTES', false, '<p>Dear ${firstname} ${lastname},</p>

<p>Please be advised that ${current_approver} has rejected the Quote Number ${number} on ${updated_date}.</p>
You can view this quote by clicking <a href="${sale_quote_link}">here</a><br />
(or copy/paste and go to following link in your web browser:<br />
${sale_quote_link}
<p>&nbsp;</p>'
);
        INSERT INTO "0".workflow_alerts (deleted, extrakeyvalues, recepient, replyto, subject, tobcc, ccemails, emailtemplateid, workflow, workflowactionstarttimeunit, workflowactionstarttime, workflowactionstarttimegranularity, isworkflowactiontimebased, content) VALUES (false, null, '${email}', null, 'Sales quote ${number} has been approved by ${current_approver}', '', '', null,approved1WorkflowID, 0, 'TRIGGER_TIME', 'MINUTES', false, '<p>Dear ${firstname} ${lastname},</p>

<p>Please be advised that ${current_approver} has approved the Quote Number ${number} on ${updated_date}.<br />
<br />
You can view this quote and convert it to Sales Invoice by clicking <a href="${sale_quote_link}">here</a><br />
(or copy/paste and go to following link in your web browser:<br />
${sale_quote_link}</p>'
);                 
    return NULL;
    END;
$body$
LANGUAGE plpgsql;
ALTER function "0".createSalesQuoteDefaultApprovers() owner TO wfmtest;
UPDATE company SET isdeleted = FALSE WHERE isdeleted IS NULL AND (select "0".createSalesQuoteDefaultApprovers()) IS NOT NULL;