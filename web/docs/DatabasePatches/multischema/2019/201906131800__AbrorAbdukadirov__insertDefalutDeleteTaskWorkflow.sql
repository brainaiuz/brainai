DROP FUNCTION IF EXISTS "anv".createDefaultDeleteTaskWorkflow();
CREATE OR REPLACE FUNCTION "anv".createDefaultDeleteTaskWorkflow()
  RETURNS INTEGER AS
$body$
DECLARE
  workflowID INTEGER;

BEGIN

  if exists (select id from "anv".companyEmailNotificationSettings where category = 'CATEGORY_PM' and notificationname ='TASK_DELETE_NOTIFICATION' limit 1)
  then
    insert into "anv".workflowrule (name, executioncriteria, module, active) values ('Default Task delete', '_WORKFLOW_EXECUTION_CRITERIA_REMOVE', '_WORKFLOW_MODULE_TASK', false) returning id into workflowID;
  else
    insert into "anv".workflowrule (name, executioncriteria, module, active) values ('Default Task delete', '_WORKFLOW_EXECUTION_CRITERIA_REMOVE', '_WORKFLOW_MODULE_TASK', false)  returning id into workflowID;
  end if;

  INSERT INTO "anv".workflow_alerts (deleted, extrakeyvalues, recepient, replyto, subject, tobcc, ccemails, emailtemplateid, workflow, workflowactionstarttimeunit, workflowactionstarttime, workflowactionstarttimegranularity, isworkflowactiontimebased, content)
  VALUES (FALSE, NULL, '${assignees_emails}', NULL, 'Delete Task', '', '', NULL, workflowID, 0, 'TRIGGER_TIME', 'MINUTES', FALSE,
          '<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head><body>
<p>Please be informed that ${modifier_name} deleted ${name} task on ${updated_date}.</p>

<table width="705" border="0" cellspacing="0" cellpadding="0" bgcolor="#ffffff">
  <tr>
    <td width="368" valign="top">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="368" valign="top">
            <table cellpadding="0" cellspacing="0" border="0" bgcolor="#ffffff" width="100%" style="vertical-align:top;">
                <tr>
                    <td height="20" bgcolor="#1c5b94" valign="middle" style="color:rgb(255,255,255);
                    font-size:12px;font-weight:bolder;padding-left:5px;text-align:left;font-family:Verdana,Arial,Helvetica,sans-serif;" colspan="2">
                        Task Summary</td>
                </tr>
                <tr>
                    <td height="17" bgcolor="#dce5ea" width="132" valign="top" style="color:rgb(0,0,0);
                    font-size:11px; padding-left:5px;text-align:left;font-weight:bold;font-family:Verdana,sans-serif;">
                        Task name:</td>
                    <td width="236" valign="top" bgcolor="#dce5ea" style="color:rgb(0,0,0);
                    font-size:11px;padding-left:1px;text-align:left;font-weight:normal;font-family:Verdana,sans-serif;">
                         ${name}</td>
                </tr>

                <tr>
                    <td height="17" bgcolor="#dce5ea" width="132" valign="top" style="color:rgb(0,0,0);
                    font-size:11px; padding-left:5px;text-align:left;font-weight:bold;font-family:Verdana,sans-serif;">
                        Task number:</td>
                        <td width="236" valign="top" bgcolor="#dce5ea" style="color:rgb(0,0,0);
                            font-size:11px;padding-left:1px;text-align:left;font-weight:normal;font-family:Verdana,sans-serif;">
                                ${number}</td>
                            </tr>
                <tr>
                    <td height="17" bgcolor="#dce5ea" width="132" valign="top" style="color:rgb(0,0,0);
                    font-size:11px; padding-left:5px;text-align:left;font-weight:bold;font-family:Verdana,sans-serif;">
                        Description:</td>
                    <td width="236" valign="top" bgcolor="#dce5ea" style="color:rgb(0,0,0);
                    font-size:11px;padding-left:1px;text-align:left;font-weight:normal;font-family:Verdana,sans-serif;">
                         ${description}</td>
                </tr>
                <tr>
                    <td height="17" bgcolor="#dce5ea" width="132" valign="top" style="color:rgb(0,0,0);
                    font-size:11px; padding-left:5px;text-align:left;font-weight:bold;font-family:Verdana,sans-serif;">
                        Priority:</td>
                    <td width="236" valign="top" bgcolor="#dce5ea" style="color:rgb(0,0,0);
                    font-size:11px;padding-left:1px;text-align:left;font-weight:normal;font-family:Verdana,sans-serif;">
                         ${priority}</td>
                </tr>
                <tr>
                    <td height="17" bgcolor="#dce5ea" width="132" valign="top" style="color:rgb(0,0,0);
                    font-size:11px; padding-left:5px;text-align:left;font-weight:bold;font-family:Verdana,sans-serif;">
                        Status:</td>
                    <td width="236" valign="top" bgcolor="#dce5ea" style="color:rgb(0,0,0);
                    font-size:11px;padding-left:1px;text-align:left;font-weight:normal;font-family:Verdana,sans-serif;">
                         ${status}</td>
                </tr>
                <tr>
                    <td height="17" bgcolor="#dce5ea" width="132" valign="top" style="color:rgb(0,0,0);
                    font-size:11px; padding-left:5px;text-align:left;font-weight:bold;font-family:Verdana,sans-serif;">
                        % completed:</td>
                    <td width="236" valign="top" bgcolor="#dce5ea" style="color:rgb(0,0,0);
                    font-size:11px;padding-left:1px;text-align:left;font-weight:normal;font-family:Verdana,sans-serif;">
                         ${completed}</td>
                </tr>
            </table>
        </td>
      </tr>
    </table>
    </td>
    <td width="9">&nbsp;</td>
    <td width="328" valign="top">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="70" width="328" valign="top">
            <table cellpadding="0" cellspacing="0" border="0" bgcolor="#ffffff" width="100%">
                <tr>
                    <td height="20" bgcolor="#1c5b94" valign="middle" style="color:rgb(255,255,255);
                    font-size:12px;font-weight:bolder;padding-left:5px;text-align:left;font-family:Verdana,Arial,Helvetica,sans-serif;" colspan="2">
                        Additional Details</td>
                </tr>
                <tr>
                    <td height="17" bgcolor="#dce5ea" width="117" valign="top" style="color:rgb(0,0,0);
                    font-size:11px; padding-left:5px;text-align:left;font-weight:bold;font-family:Verdana,sans-serif;">
                        Creator:</td>
                    <td width="211" valign="top" bgcolor="#dce5ea" style="color:rgb(0,0,0);
                    font-size:11px;padding-left:1px;text-align:left;font-weight:normal;font-family:Verdana,sans-serif;">
                         ${creator_name}</td>
                </tr>
                <tr>
                    <td height="17" bgcolor="#dce5ea" width="117" valign="top" style="color:rgb(0,0,0);
                    font-size:11px; padding-left:5px;text-align:left;font-weight:bold;font-family:Verdana,sans-serif;">
                        Project:</td>
                    <td width="211" valign="top" bgcolor="#dce5ea" style="color:rgb(0,0,0);
                    font-size:11px;padding-left:1px;text-align:left;font-weight:normal;font-family:Verdana,sans-serif;">
                         ${project}</td>
                </tr>
                <tr>
                    <td height="17" bgcolor="#dce5ea" width="117" valign="top" style="color:rgb(0,0,0);
                    font-size:11px; padding-left:5px;text-align:left;font-weight:bold;font-family:Verdana,sans-serif;">
                        Client:</td>
                    <td width="211" valign="top" bgcolor="#dce5ea" style="color:rgb(0,0,0);
                    font-size:11px;padding-left:1px;text-align:left;font-weight:normal;font-family:Verdana,sans-serif;">
                         ${project_client}</td>
                </tr>
                <tr>
                    <td height="17" bgcolor="#dce5ea" width="117" valign="top" style="color:rgb(0,0,0);
                    font-size:11px; padding-left:5px;text-align:left;font-weight:bold;font-family:Verdana,sans-serif;">
                        Assignee(s):</td>
                    <td width="211" valign="top" bgcolor="#dce5ea" style="color:rgb(0,0,0);
                    font-size:11px;padding-left:1px;text-align:left;font-weight:normal;font-family:Verdana,sans-serif;">
                         ${assignees}</td>
                </tr>
                <tr><td height="9"></td></tr>
            </table>
        </td>
      </tr>
      <tr>
          <td height="89" width="328" valign="top">
            <table cellpadding="0" cellspacing="0" border="0" bgcolor="#ffffff" width="100%">
                <td height="19" bgcolor="#ffffff" valign="top" colspan="2">&nbsp;</td>
                <tr>
                    <td height="20" bgcolor="#1c5b94" valign="middle" style="color:rgb(255,255,255);
                    font-size:12px;font-weight:bolder;padding-left:5px;text-align:left;font-family:Verdana,Arial,Helvetica,sans-serif;" colspan="2">
                        Dates</td>
                </tr>
                <tr>
                    <td height="17" bgcolor="#dce5ea" width="117" valign="top" style="color:rgb(0,0,0);
                    font-size:11px; padding-left:5px;text-align:left;font-weight:bold;font-family:Verdana,sans-serif;">
                        Start Date:</td>
                    <td width="211" valign="top" bgcolor="#dce5ea" style="color:rgb(0,0,0);
                    font-size:11px;padding-left:1px;text-align:left;font-weight:normal;font-family:Verdana,sans-serif;">
                         ${start_date}</td>
                </tr>
                <tr>
                    <td height="17" bgcolor="#dce5ea" width="117" valign="top" style="color:rgb(0,0,0);
                    font-size:11px; padding-left:5px;text-align:left;font-weight:bold;font-family:Verdana,sans-serif;">
                        Due Date:</td>
                    <td width="211" valign="top" bgcolor="#dce5ea" style="color:rgb(0,0,0);
                    font-size:11px;padding-left:1px;text-align:left;font-weight:normal;font-family:Verdana,sans-serif;">
                         ${due_date}</td>
                </tr>
                <tr>
                    <td height="17" bgcolor="#dce5ea" width="117" valign="top" style="color:rgb(0,0,0);
                    font-size:11px; padding-left:5px;text-align:left;font-weight:bold;font-family:Verdana,sans-serif;">
                        Est. Time:</td>
                    <td width="211" valign="top" bgcolor="#dce5ea" style="color:rgb(0,0,0);
                    font-size:11px;padding-left:1px;text-align:left;font-weight:normal;font-family:Verdana,sans-serif;">
                         ${estimated_time}</td>
                </tr>
                <tr><td height="9"></td></tr>
            </table>
        </td>
      </tr>
    </table>
    </td>
  </tr>
</table>

</body>
</html>
');
  RETURN NULL;
END;
$body$
LANGUAGE plpgsql;

ALTER FUNCTION "anv".createDefaultDeleteTaskWorkflow() OWNER TO wfmtest;

UPDATE company SET selectFunctioncolumn =(SELECT "anv".createDefaultDeleteTaskWorkflow()) where id||'' = replace('"anv"', '"', '');
