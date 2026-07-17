DROP FUNCTION IF EXISTS "0".createDefaultTaskWorkflow();
CREATE OR REPLACE FUNCTION "0".createDefaultTaskWorkflow()
  RETURNS INTEGER AS
$body$
DECLARE
  addWorkflowID INTEGER;

BEGIN

  if exists (select id from "0".companyEmailNotificationSettings where category = 'CATEGORY_PM' and notificationname ='TASK_ADD_NOTIFICATION' limit 1)
  then
    insert into "0".workflowrule (name, executioncriteria, module, active) values ('Default Task add', '_WORKFLOW_EXECUTION_CRITERIA_CREATE', '_WORKFLOW_MODULE_TASK', false) returning id into addWorkflowID;
  else
    insert into "0".workflowrule (name, executioncriteria, module, active) values ('Default Task add', '_WORKFLOW_EXECUTION_CRITERIA_CREATE', '_WORKFLOW_MODULE_TASK', false)  returning id into addWorkflowID;
  end if;

  INSERT INTO "0".workflow_alerts (deleted, extrakeyvalues, recepient, replyto, subject, tobcc, ccemails, emailtemplateid, workflow, workflowactionstarttimeunit, workflowactionstarttime, workflowactionstarttimegranularity, isworkflowactiontimebased, content)
  VALUES (FALSE, NULL, '${project_manager_email}', NULL, 'Add Task', '', '', NULL, addWorkflowID, 0, 'TRIGGER_TIME', 'MINUTES', FALSE,
          '<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
<p>Dear ${project_manager},</p>

<p>Please be advised that ${creator_name} added a new task on ${created_date}.</p>

<table width="705" border="0" cellspacing="0" cellpadding="0" bgcolor="#ffffff">
<tr>
<td width="368" valign="top">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td width="368" valign="top">
                <table cellpadding="0" cellspacing="0" border="0" bgcolor="#ffffff" width="100%"
                       style="vertical-align:top;">
                    <tr>
                        <td height="20" bgcolor="#1c5b94" valign="middle" style="color:rgb(255,255,255);
                    font-size:12px;font-weight:bolder;padding-left:5px;text-align:left;font-family:Verdana,Arial,Helvetica,sans-serif;"
                            colspan="2">
                            Task Summary
                        </td>
                    </tr>
                    <tr>
                        <td height="17" bgcolor="#dce5ea" width="132" valign="top" style="color:rgb(0,0,0);
                    font-size:11px; padding-left:5px;text-align:left;font-weight:bold;font-family:Verdana,sans-serif;">
                            Task name:
                        </td>
                        <td width="236" valign="top" bgcolor="#dce5ea" style="color:rgb(0,0,0);
                    font-size:11px;padding-left:1px;text-align:left;font-weight:normal;font-family:Verdana,sans-serif;">
                            ${name}</td>
                    </tr>
                    <tr>
                        <td height="17" bgcolor="#dce5ea" width="132" valign="top" style="color:rgb(0,0,0);
                    font-size:11px; padding-left:5px;text-align:left;font-weight:bold;font-family:Verdana,sans-serif;">
                            Task Number:
                        </td>
                        <td width="236" valign="top" bgcolor="#dce5ea" style="color:rgb(0,0,0);
                    font-size:11px;padding-left:1px;text-align:left;font-weight:normal;font-family:Verdana,sans-serif;">
                            ${number}</td>
                    </tr>
                    <tr>
                        <td height="17" bgcolor="#dce5ea" width="132" valign="top" style="color:rgb(0,0,0);
                    font-size:11px; padding-left:5px;text-align:left;font-weight:bold;font-family:Verdana,sans-serif;">
                            Description:
                        </td>
                        <td width="236" valign="top" bgcolor="#dce5ea" style="color:rgb(0,0,0);
                    font-size:11px;padding-left:1px;text-align:left;font-weight:normal;font-family:Verdana,sans-serif;">
                            ${description}</td>
                    </tr>
                    <tr>
                        <td height="17" bgcolor="#dce5ea" width="132" valign="top" style="color:rgb(0,0,0);
                    font-size:11px; padding-left:5px;text-align:left;font-weight:bold;font-family:Verdana,sans-serif;">
                            Priority:
                        </td>
                        <td width="236" valign="top" bgcolor="#dce5ea" style="color:rgb(0,0,0);
                    font-size:11px;padding-left:1px;text-align:left;font-weight:normal;font-family:Verdana,sans-serif;">
                            ${priority}</td>
                    </tr>
                    <tr>
                        <td height="17" bgcolor="#dce5ea" width="132" valign="top" style="color:rgb(0,0,0);
                    font-size:11px; padding-left:5px;text-align:left;font-weight:bold;font-family:Verdana,sans-serif;">
                            Status:
                        </td>
                        <td width="236" valign="top" bgcolor="#dce5ea" style="color:rgb(0,0,0);
                    font-size:11px;padding-left:1px;text-align:left;font-weight:normal;font-family:Verdana,sans-serif;">
                            ${status}</td>
                    </tr>
                    <tr>
                        <td height="17" bgcolor="#dce5ea" width="132" valign="top" style="color:rgb(0,0,0);
                    font-size:11px; padding-left:5px;text-align:left;font-weight:bold;font-family:Verdana,sans-serif;">
                            % completed:
                        </td>
                        <td width="236" valign="top" bgcolor="#dce5ea" style="color:rgb(0,0,0);
                    font-size:11px;padding-left:1px;text-align:left;font-weight:normal;font-family:Verdana,sans-serif;">
                            ${completed}</td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr><td height="19" bgcolor="#ffffff" valign="top" colspan="2">&nbsp;</td></tr>
        <tr>
            <td width="368" valign="top">
                <table cellpadding="0" cellspacing="0" border="0" bgcolor="#ffffff" width="100%">
                    <tr>
                        <td height="20" bgcolor="#1c5b94" valign="middle" style="color:rgb(255,255,255);
                    font-size:12px;font-weight:bolder;padding-left:5px;text-align:left;font-family:Verdana,Arial,Helvetica,sans-serif;"
                            colspan="2">
                            Additional Details
                        </td>
                    </tr>
                    <tr>
                        <td height="17" bgcolor="#dce5ea" width="117" valign="top" style="color:rgb(0,0,0);
                    font-size:11px; padding-left:5px;text-align:left;font-weight:bold;font-family:Verdana,sans-serif;">
                            Creator:
                        </td>
                        <td width="211" valign="top" bgcolor="#dce5ea" style="color:rgb(0,0,0);
                    font-size:11px;padding-left:1px;text-align:left;font-weight:normal;font-family:Verdana,sans-serif;">
                            ${creator_name}</td>
                    </tr>
                    <tr>
                        <td height="17" bgcolor="#dce5ea" width="117" valign="top" style="color:rgb(0,0,0);
                    font-size:11px; padding-left:5px;text-align:left;font-weight:bold;font-family:Verdana,sans-serif;">
                            Project:
                        </td>
                        <td width="211" valign="top" bgcolor="#dce5ea" style="color:rgb(0,0,0);
                    font-size:11px;padding-left:1px;text-align:left;font-weight:normal;font-family:Verdana,sans-serif;">
                            ${project}</td>
                    </tr>
                    <tr>
                        <td height="17" bgcolor="#dce5ea" width="117" valign="top" style="color:rgb(0,0,0);
                    font-size:11px; padding-left:5px;text-align:left;font-weight:bold;font-family:Verdana,sans-serif;">
                            Client:
                        </td>
                        <td width="211" valign="top" bgcolor="#dce5ea" style="color:rgb(0,0,0);
                    font-size:11px;padding-left:1px;text-align:left;font-weight:normal;font-family:Verdana,sans-serif;">
                            ${client_name}</td>
                    </tr>
                    <tr>
                        <td height="17" bgcolor="#dce5ea" width="117" valign="top" style="color:rgb(0,0,0);
                    font-size:11px; padding-left:5px;text-align:left;font-weight:bold;font-family:Verdana,sans-serif;">
                            Assignee(s):
                        </td>
                        <td width="211" valign="top" bgcolor="#dce5ea" style="color:rgb(0,0,0);
                    font-size:11px;padding-left:1px;text-align:left;font-weight:normal;font-family:Verdana,sans-serif;">
                            ${assignees}</td>
                    </tr>
                    <tr>
                        <td height="9"></td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td width="368" valign="top">
                <table cellpadding="0" cellspacing="0" border="0" bgcolor="#ffffff" width="100%">
                    <td height="19" bgcolor="#ffffff" valign="top" colspan="2">&nbsp;</td>
                    <tr>
                        <td height="20" bgcolor="#1c5b94" valign="middle" style="color:rgb(255,255,255);
                    font-size:12px;font-weight:bolder;padding-left:5px;text-align:left;font-family:Verdana,Arial,Helvetica,sans-serif;"
                            colspan="2">
                            Dates
                        </td>
                    </tr>
                    <tr>
                        <td height="17" bgcolor="#dce5ea" width="117" valign="top" style="color:rgb(0,0,0);
                    font-size:11px; padding-left:5px;text-align:left;font-weight:bold;font-family:Verdana,sans-serif;">
                            Start Date:
                        </td>
                        <td width="211" valign="top" bgcolor="#dce5ea" style="color:rgb(0,0,0);
                    font-size:11px;padding-left:1px;text-align:left;font-weight:normal;font-family:Verdana,sans-serif;">
                            ${start_date}</td>
                    </tr>
                    <tr>
                        <td height="17" bgcolor="#dce5ea" width="117" valign="top" style="color:rgb(0,0,0);
                    font-size:11px; padding-left:5px;text-align:left;font-weight:bold;font-family:Verdana,sans-serif;">
                            Due Date:
                        </td>
                        <td width="211" valign="top" bgcolor="#dce5ea" style="color:rgb(0,0,0);
                    font-size:11px;padding-left:1px;text-align:left;font-weight:normal;font-family:Verdana,sans-serif;">
                            ${due_date}</td>
                    </tr>
                    <tr>
                        <td height="17" bgcolor="#dce5ea" width="117" valign="top" style="color:rgb(0,0,0);
                    font-size:11px; padding-left:5px;text-align:left;font-weight:bold;font-family:Verdana,sans-serif;">
                            Est. Time:
                        </td>
                        <td width="211" valign="top" bgcolor="#dce5ea" style="color:rgb(0,0,0);
                    font-size:11px;padding-left:1px;text-align:left;font-weight:normal;font-family:Verdana,sans-serif;">
                            ${estimated_time}</td>
                    </tr>
                    <tr>
                        <td height="9"></td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</td>
</tr>
</table>

<p>Click <a href="${link}" title="View task" target="_blank">here</a>
to view the task</p><p>* If you are not able to click on the link, you can instead copy and paste the following address in to your web browser: </p>
<p><span style="color: #0033FF">${link}</span></p>
<p></p>
</body>
</html>
');
  RETURN NULL;
END;
$body$
LANGUAGE plpgsql;

ALTER FUNCTION "0".createDefaultTaskWorkflow() OWNER TO wfmtest;

UPDATE company SET selectFunctioncolumn =(SELECT "0".createDefaultTaskWorkflow()) where  id=(select id from company limit 1);
