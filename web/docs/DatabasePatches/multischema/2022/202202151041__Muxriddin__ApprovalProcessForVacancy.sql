
delete  from "anv".form_property where form_id ='VACANCY_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('VACANCY_FORM',
        '[
  {
    "code": "contractPeriod",
    "title": "Contract Period",
    "aliasName": "CONTRACT_PERIOD",
    "changed": false,
    "required": false,
    "widget": "CONTRACT_PERIOD",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
   {
    "code": "VACANCY_ATTACHMENTS",
    "title": "Attachments",
    "aliasName": "VACANCY_ATTACHMENTS",
    "changed": false,
    "required": false,
    "widget": "WIDGET",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "VACANCY_NOTES",
    "title": "Notes",
    "aliasName": "VACANCY_NOTES",
    "changed": false,
    "required": false,
    "widget": "TextArea",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "vacancyNumberID",
    "title": "vacancy ID",
    "aliasName": "VACANCY_NUMBER",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "vacancyManager",
    "title": "Manager",
    "aliasName": "MANAGER",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "vacancyBackupManager",
    "title": "Backup Manager",
    "aliasName": "BACKUP_MANAGER",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "vacancyPosition",
    "title": "Position",
    "aliasName": "POSITION",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "vacancyLocation",
    "title": "Location",
    "aliasName": "LOCATION",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "vacancyJobTitle",
    "title": "Job Title",
    "aliasName": "JOB_TITLE",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "PROJECT",
    "title": "Project",
    "aliasName": "PROJECT",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "COUNTRY",
    "title": "Country",
    "aliasName": "COUNTRY",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "COUNTRYEMBASSY",
    "title": "Embassy Only",
    "aliasName": "COUNTRYEMBASSY",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "gender",
    "title": "Gender",
    "aliasName": "GENDER",
    "changed": false,
    "required": false,
    "widget": "WIDGET",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "proposedSalary",
    "title": "Proposed Salary",
    "aliasName": "PROPOSED_SALARY",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "jobRequirement",
    "title": "Job requirements",
    "aliasName": "JOB_REQUIREMENT",
    "changed": false,
    "required": false,
    "widget": "TextArea",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "vacancyType",
    "title": "Vacancy Type",
    "aliasName": "VACANCY_TYPE",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "religion",
    "title": "Religion",
    "aliasName": "RELIGION",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "vacancyDescription",
    "title": "Description",
    "aliasName": "DESCRIPTION",
    "changed": false,
    "required": false,
    "widget": "TextArea",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "vacancyStartDate",
    "title": "Start Date",
    "aliasName": "START_DATE",
    "changed": false,
    "required": true,
    "widget": "DatePicker",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "vacancyEndDate",
    "title": "End Date",
    "aliasName": "END_DATE",
    "changed": false,
    "required": true,
    "widget": "DatePicker",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "vacancyStatus",
    "title": "Status",
    "aliasName": "STATUS",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "vacancyPlaceCount",
    "title": "Vacant Place Count",
    "aliasName": "VACANCY_PLACE_COUNT",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "vacancyJobType",
    "title": "Job Type",
    "aliasName": "JOB_TYPE",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "vacancyJobFamily",
    "title": "Job Family",
    "aliasName": "JOB_FAMILY",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "vacancyResponsibilities",
    "title": "Responsibilities",
    "aliasName": "RESPONSIBILITIES",
    "changed": false,
    "required": false,
    "widget": "TextArea",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "vacancyRequiredDegree",
    "title": "Required Degree",
    "aliasName": "REQUIRED_DEGREE",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "department",
    "title": "Department",
    "aliasName": "DEPARTMENT",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "APPROVER",
    "title": "Approver",
    "aliasName": "APPROVER",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }
]');

delete from "anv".modelfield where form_id = 'VACANCY_FORM' and field_id = 'APPROVER';
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('VACANCY_FORM', 'APPROVER', true, false, 'COL_1', 'VACANCY_BASIC_INFORMATION', 8);

delete from "anv".reference where parentid=(select id from "anv".reference where code='VACANCY_APPROVAL_STATUS');
delete from "anv".reference  where code = 'VACANCY_APPROVAL_STATUS';
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, isactive)
							values('VACANCY_APPROVAL_STATUS', false, true, 'Vacancy Approval Status', true, 1, true);


delete from "anv".reference  where code = 'VACANCY_APPROVAL_STATUS_APPROVED';
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
							values('VACANCY_APPROVAL_STATUS_APPROVED', false, true, 'Approved', true, 2, (select id from "anv".reference where code='VACANCY_APPROVAL_STATUS'), true);


delete from "anv".reference  where code = 'VACANCY_APPROVAL_STATUS_REJECTED';
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
							values('VACANCY_APPROVAL_STATUS_REJECTED', false, true, 'Rejected', true, 3, (select id from "anv".reference where code='VACANCY_APPROVAL_STATUS'), true);


delete from "anv".reference  where code = 'VACANCY_APPROVAL_STATUS_SUBMITTED';
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
							values('VACANCY_APPROVAL_STATUS_SUBMITTED', false, true, 'Submitted', true, 4, (select id from "anv".reference where code='VACANCY_APPROVAL_STATUS'), true);


delete from "anv".reference  where code = 'VACANCY_APPROVAL_STATUS_DRAFT';
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
							values('VACANCY_APPROVAL_STATUS_DRAFT', false, true, 'Draft', true, 5, (select id from "anv".reference where code='VACANCY_APPROVAL_STATUS'), true);



DROP function IF EXISTS "anv".createVacanyDefaultApprovers();
CREATE OR replace function "anv".createVacanyDefaultApprovers()
  returns INTEGER AS
$body$
DECLARE
  approverID          INTEGER;
  approved1WorkflowID INTEGER;
  rejected1WorkflowID INTEGER;
  vacancy_approver_id  INTEGER;
  er                  record;

BEGIN

IF EXISTS(SELECT id FROM "anv".approvers WHERE entitytype = 'VACANCY' AND entity_id IS NULL LIMIT 1)
  THEN
    DELETE FROM "anv".approver_roles WHERE approver_id IN (SELECT id FROM "anv".approvers WHERE entitytype = 'VACANCY');
    DELETE FROM "anv".approver_employees WHERE approver_id IN (SELECT id FROM "anv".approvers WHERE entitytype = 'VACANCY');
    UPDATE "anv".approvers set deleted =true WHERE entitytype = 'VACANCY';
  END IF;

  INSERT INTO "anv".approvers (approver_order, entitytype, is_default, onapprovedaction, onrejectedaction) VALUES (1, 'VACANCY', true, 1, 3) RETURNING id INTO approverID;
  INSERT INTO "anv".approver_roles (approver_id, role_id) (select approverID, r.id from "anv".role r where code in ('ADMIN', 'DR'));

  IF EXISTS(SELECT id FROM "anv".workflowrule WHERE module = '_WORKFLOW_MODULE_VACANCY' AND executioncriteria = '_WORKFLOW_ACTION_APPROVING' AND showinlist IS FALSE)
  THEN
    UPDATE "anv".workflowrule SET deleted = TRUE WHERE module = '_WORKFLOW_MODULE_VACANCY' AND executioncriteria = '_WORKFLOW_ACTION_APPROVING' AND showinlist IS FALSE;
    UPDATE "anv".workflow_alerts SET deleted = TRUE WHERE workflow IN (SELECT id FROM "anv".workflowrule WHERE module = '_WORKFLOW_MODULE_VACANCY' AND executioncriteria = '_WORKFLOW_ACTION_APPROVING' AND showinlist IS FALSE);
  END IF;

  INSERT INTO "anv".workflowrule (executioncriteria, module, showinlist) VALUES ('_WORKFLOW_ACTION_APPROVING', '_WORKFLOW_MODULE_VACANCY', false) RETURNING id into approved1WorkflowID;
  INSERT INTO "anv".workflowrule (executioncriteria, module, showinlist) VALUES ('_WORKFLOW_ACTION_APPROVING', '_WORKFLOW_MODULE_VACANCY', false) RETURNING id INTO rejected1WorkflowID;
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
  VALUES (false, null, '${email}', null, 'Declined your Vacancy', '', '', null, rejected1WorkflowID, 0, 'TRIGGER_TIME', 'MINUTES', false,
          '<p>Dear ${creator},</p>

         <p>Please be advised that ${current_approver} has declined your Vacancy on ${decline_date}.</p><br />
         You can review ${current_approver} ''s comments, and make the appropriate changes and resubmit it.<br /><br />
         Click <a href="${link}">here</a> to view and edit the Vacancy.<br /><br />
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
  VALUES (false, null, '${email}', null, 'Approved your Vacancy', '', '', null, approved1WorkflowID, 0, 'TRIGGER_TIME', 'MINUTES', false,
          '<p>Dear ${creator},</p>

         <p>Please be advised that ${current_approver} has approved your Vacancy, ${narration} on ${date}.</p><br /><br />
         Click <a href="${link}">here</a> to view the Vacancy.<br /><br />
         * If you are not able to click on the link, you can copy and paste the following address into your web browser: ${link}');

  FOR er IN (SELECT * FROM "anv".vacancy WHERE deleted IS NOT TRUE)
    LOOP
      INSERT INTO "anv".approvers(approver_order, entitytype, entity_id, is_default, onapprovedaction, onrejectedaction,workflow, rejected_workflow,exactapprover,status)
      VALUES (1, 'VACANCY', er.id, false, 1, 2,approved1WorkflowID, rejected1WorkflowID,er.manager_id,(SELECT id FROM "anv".reference WHERE id = er.status_id AND parentId = (SELECT id FROM "anv".reference WHERE code='VACANCY_APPROVAL_STATUS'))) RETURNING id INTO vacancy_approver_id;

      UPDATE "anv".vacancy a set currentapprover=vacancy_approver_id, prevapprover=vacancy_approver_id, overallstatus=(SELECT id FROM "anv".reference WHERE id = a.status_id AND parentId = (SELECT id FROM "anv".reference WHERE code='VACANCY_APPROVAL_STATUS')) where id = er.id;
    END LOOP;
  return NULL;
END;
$body$
  LANGUAGE plpgsql;
ALTER FUNCTION "anv".createVacanyDefaultApprovers() OWNER TO wfmtest;
UPDATE company SET selectFunctioncolumn =(SELECT "anv".createVacanyDefaultApprovers()) WHERE  id=(SELECT id FROM company LIMIT 1);
