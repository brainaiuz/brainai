delete from "0".reference  where code = 'DRAFT' and parentid = (select id from "0".reference where code='MANUAL_JOURNAL_STATUS');
delete from "0".reference  where code = 'POST' and parentid = (select id from "0".reference where code='MANUAL_JOURNAL_STATUS');
delete from "0".reference  where code = 'REVERSED' and parentid = (select id from "0".reference where code='MANUAL_JOURNAL_STATUS');
delete from "0".reference  where code = 'APPROVED' and parentid = (select id from "0".reference where code='MANUAL_JOURNAL_STATUS');
delete from "0".reference  where code = 'REJECTED' and parentid = (select id from "0".reference where code='MANUAL_JOURNAL_STATUS');
delete from "0".reference  where code = 'SUBMITTED' and parentid = (select id from "0".reference where code='MANUAL_JOURNAL_STATUS');
delete from "0".reference  where code = 'MANUAL_JOURNAL_STATUS';

insert into "0".reference(code, deleted, isremovable, name, shared, sorder, isactive)
values('MANUAL_JOURNAL_STATUS', false, true, 'Manual Journal Status', true, 1, true);
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('DRAFT', false, true, 'Draft', true, 2, (select id from "0".reference where code='MANUAL_JOURNAL_STATUS'), true);
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('POST', false, true, 'Post', true, 3, (select id from "0".reference where code='MANUAL_JOURNAL_STATUS'), true);
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('REVERSED', false, true, 'Reversed', true, 4, (select id from "0".reference where code='MANUAL_JOURNAL_STATUS'), true);
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('APPROVED', false, true, 'Approved', true, 5, (select id from "0".reference where code='MANUAL_JOURNAL_STATUS'), true);
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('REJECTED', false, true, 'Rejected', true, 6, (select id from "0".reference where code='MANUAL_JOURNAL_STATUS'), true);
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('SUBMITTED', false, true, 'Submitted', true, 7, (select id from "0".reference where code='MANUAL_JOURNAL_STATUS'), true);

delete from "anv".reference  where code = 'DRAFT' and parentid = (select id from "anv".reference where code='MANUAL_JOURNAL_STATUS');
delete from "anv".reference  where code = 'POST' and parentid = (select id from "anv".reference where code='MANUAL_JOURNAL_STATUS');
delete from "anv".reference  where code = 'REVERSED' and parentid = (select id from "anv".reference where code='MANUAL_JOURNAL_STATUS');
delete from "anv".reference  where code = 'APPROVED' and parentid = (select id from "anv".reference where code='MANUAL_JOURNAL_STATUS');
delete from "anv".reference  where code = 'REJECTED' and parentid = (select id from "anv".reference where code='MANUAL_JOURNAL_STATUS');
delete from "anv".reference  where code = 'SUBMITTED' and parentid = (select id from "anv".reference where code='MANUAL_JOURNAL_STATUS');
delete from "anv".reference  where code = 'MANUAL_JOURNAL_STATUS';

insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, isactive)
values('MANUAL_JOURNAL_STATUS', false, true, 'Manual Journal Status', true, 1, true);
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('DRAFT', false, true, 'Draft', true, 2, (select id from "anv".reference where code='MANUAL_JOURNAL_STATUS'), true);
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('POST', false, true, 'Post', true, 3, (select id from "anv".reference where code='MANUAL_JOURNAL_STATUS'), true);
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('REVERSED', false, true, 'Reversed', true, 4, (select id from "anv".reference where code='MANUAL_JOURNAL_STATUS'), true);
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('APPROVED', false, true, 'Approved', true, 5, (select id from "anv".reference where code='MANUAL_JOURNAL_STATUS'), true);
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('REJECTED', false, true, 'Rejected', true, 6, (select id from "anv".reference where code='MANUAL_JOURNAL_STATUS'), true);
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('SUBMITTED', false, true, 'Submitted', true, 7, (select id from "anv".reference where code='MANUAL_JOURNAL_STATUS'), true);

delete from "anv".reference  where code = '_WORKFLOW_MODULE_MANUAL_JOURNAL' and parentid = (select id from "anv".reference where code='_WORKFLOW_MODULE');
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('_WORKFLOW_MODULE_MANUAL_JOURNAL', false, true, 'Manual Entry', true, 6, (select id from "anv".reference where code='_WORKFLOW_MODULE'), true);

delete from "0".reference  where code = '_WORKFLOW_MODULE_MANUAL_JOURNAL' and parentid = (select id from "0".reference where code='_WORKFLOW_MODULE');
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
values('_WORKFLOW_MODULE_MANUAL_JOURNAL', false, true, 'Manual Entry', true, 6, (select id from "0".reference where code='_WORKFLOW_MODULE'), true);

delete from model where formid = 'manualtransactions';
insert into model(active, formid, title, viewname) values(true, 'manualtransactions', 'Manual Entry Form', 'Manual Entry');

delete from modelfield where form_ID = 'manualtransactions';
insert into modelfield (form_ID,
                        field_ID,
                        sorder,
                        widget,
                        source,
                        usableByWorkflow,
                        type,
                        disableUpdate,
                        isWorkflowAttribute,
                        split,
                        mandatory,
                        systemMandatory,
                        hide,
                        isCustomField,
                        fullWidth,
                        isEntityField,
                        hideInCustomizeForm,
                        systemDisable)
values ('manualtransactions', 'DATE', 1, 'DatePicker', null, true, 'Date', false, false, false, false, false, false, false, false, false, false, false),
       ('manualtransactions', 'NARRATION', 2, 'TextBox', null, true, 'Text', false, false, false, false, false, false, false, false, false, false, false),
       ('manualtransactions', 'STATUS', 2, 'DropDown', 'REFERENCE@MANUAL_JOURNAL_STATUS', true, 'Text', true, false, false, false, false, false, false, false, false, false, false),
       ('manualtransactions', 'NUMBER', 2, 'TextBox', null, true, 'Text', true, false, false, false, false, false, false, false, false, false, false),
       ('manualtransactions', 'REFERENCE', 2, 'TextBox', null, true, 'Text', false, false, false, false, false, false, false, false, false, false, false),
       ('manualtransactions', 'MEMORIZED_TRANSACTION', 2, 'DropDown', 'ACCOUNTING@MEMORIZED_TRANSACTION', true, 'Text', true, false, false, false, false, false, false, false, false, false, false),
       ('manualtransactions', 'PREV_APPROVER', 2, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('manualtransactions', 'PREV_APPROVER_EMAIL', 2, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('manualtransactions', 'PREV_APPROVER_STATUS', 2, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('manualtransactions', 'CURRENT_APPROVER', 2, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('manualtransactions', 'CURRENT_APPROVER_EMAIL', 2, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('manualtransactions', 'CURRENT_APPROVER_STATUS', 2, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('manualtransactions', 'NEXT_APPROVER', 2, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('manualtransactions', 'NEXT_APPROVER_EMAIL', 2, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false),
       ('manualtransactions', 'NEXT_APPROVER_STATUS', 2, null, null, true, null, true, true, false, false, false, false, false, false, false, false, false);

DROP function IF EXISTS "anv".createManualEntryDefaultApprovers();
CREATE OR replace function "anv".createManualEntryDefaultApprovers()
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
  VALUES (1, 'manualjournal', true, 1, 3) RETURNING id INTO approverID;

  INSERT INTO "anv".approver_roles (approver_id, role_id) (select approverID, r.id from "anv".role r where code in ('ADMIN', 'DR', 'ACCOUNTANT'));
  INSERT INTO "anv".workflowrule (executioncriteria, module, showinlist) VALUES ('_WORKFLOW_ACTION_APPROVING', '_WORKFLOW_MODULE_MANUAL_JOURNAL', false) RETURNING id into approved1WorkflowID;
  INSERT INTO "anv".workflowrule (executioncriteria, module, showinlist) VALUES ('_WORKFLOW_ACTION_APPROVING', '_WORKFLOW_MODULE_MANUAL_JOURNAL', false) RETURNING id INTO rejected1WorkflowID;
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
  VALUES (false, null, '${email}', null, 'Declined your Manual Entry', '', '', null, rejected1WorkflowID, 0, 'TRIGGER_TIME', 'MINUTES', false,
          '<p>Dear ${creator},</p>

         <p>Please be advised that ${current_approver} has declined your Manual Entry on ${decline_date}.</p><br />
         You can review ${current_approver} ''s comments, and make the appropriate changes and resubmit it.<br /><br />
         Click <a href="${link}">here</a> to view and edit the Manual Entry.<br /><br />
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
  VALUES (false, null, '${email}', null, 'Approved your Manual Entry', '', '', null, approved1WorkflowID, 0, 'TRIGGER_TIME', 'MINUTES', false,
          '<p>Dear ${creator},</p>

         <p>Please be advised that ${current_approver} has approved your Manual Entry, ${narration} on ${date}.</p><br /><br />
         Click <a href="${link}">here</a> to view the Manual Entry.<br /><br />
         * If you are not able to click on the link, you can copy and paste the following address into your web browser: ${link}');

  FOR er IN (SELECT * FROM "anv".manualjournal WHERE deleted IS NOT TRUE)
  LOOP
    INSERT INTO "anv".approvers(approver_order, entitytype, entity_id, is_default, onapprovedaction, onrejectedaction,workflow, rejected_workflow,exactapprover,status)
    VALUES (1, 'manualjournal', er.id, false, 1, 2,approved1WorkflowID, rejected1WorkflowID,er.creatorId,(SELECT id FROM "anv".reference WHERE code = er.status AND parentId = (SELECT id FROM "anv".reference WHERE code='MANUAL_JOURNAL_STATUS'))) RETURNING id INTO manual_approver_id;

    UPDATE "anv".manualjournal a set currentapprover=manual_approver_id, prevapprover=manual_approver_id, overallstatus=(SELECT id FROM "anv".reference WHERE code = a.status AND parentId = (SELECT id FROM "anv".reference WHERE code='MANUAL_JOURNAL_STATUS')) where id = er.id;
  END LOOP;
  return NULL;
END;
$body$
LANGUAGE plpgsql;
ALTER FUNCTION "anv".createManualEntryDefaultApprovers() OWNER TO wfmtest;
UPDATE company SET selectFunctioncolumn =(SELECT "anv".createManualEntryDefaultApprovers()) WHERE  id=(SELECT id FROM company LIMIT 1);
