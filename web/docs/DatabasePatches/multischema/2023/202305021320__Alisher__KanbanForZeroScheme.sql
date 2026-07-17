delete
from "0".kanbanitemsettings
where code = 'OPPORTUNITY_ITEM_SETTINGS';

insert into "0".kanbanitemsettings(name, code, settingsJSONData)
values ('Opportunity Kanban Item', 'OPPORTUNITY_ITEM_SETTINGS',
        '[{
"code":"OPPORTUNITY_NAME", "title":"Name", "localizationCode":"name", "selected":true, "relatedField":null,"mandatory":false, "changeable":true
},{
"code":"OPPORTUNITY_AMOUT", "title":"Amount", "localizationCode":"amount", "selected":true, "relatedField":null,"mandatory":false, "changeable":true
},{
"code":"OPPORTUNITY_ENTRY_PHOTO", "title":"Image", "localizationCode":"image", "selected":true, "relatedField":null,"mandatory":false, "changeable":false
},{
"code":"OPPORTUNITY_EMAIL", "title":"Email", "localizationCode":"email","selected":true, "relatedField":null,"mandatory":false, "changeable":false
},{
"code":"OPPORTUNITY_PHONE", "title":"Phone", "localizationCode":"phone","selected":true, "relatedField":null,"mandatory":false, "changeable":false
},{
"code":"OPPORTUNITY_ASSIGNE_NAME", "title":"Assigne Name", "localizationCode":"assignee","selected":true, "relatedField":null,"mandatory":false, "changeable":true
},{
"code":"OPPORTUNITY_INFO", "title":"Account", "localizationCode":"account","selected":true, "relatedField":null,"mandatory":false, "changeable":true
},{
"code":"OPPORTUNITY_ACTION", "title":"Action", "localizationCode":"action","selected":true, "relatedField":null,"mandatory":false, "changeable":false
},{
"code":"OPPORTUNITY_NOTE", "title":"Note", "localizationCode":"note","selected":true, "relatedField":null,"mandatory":false, "changeable":false
},{
"code":"OPPORTUNITY_CLOSEDATE", "title":"Close Date", "localizationCode":"closeDate","selected":true, "relatedField":null,"mandatory":false, "changeable":true
}]');



delete
from "0".kanbanitemsettings
where code = 'CANDIDATE_ITEM_SETTINGS';

insert into "0".kanbanitemsettings(name, code, settingsJSONData)
values ('Candidate Kanban Item', 'CANDIDATE_ITEM_SETTINGS',
        '[{
"code":"CANDIDATE_NAME", "title":"Name", "localizationCode":"name", "selected":true, "relatedField":null,"mandatory":false, "changeable":true
},{
"code":"CANDIDATE_PHONE", "title":"Phone", "localizationCode":"phone","selected":true, "relatedField":null,"mandatory":false, "changeable":false
},{
"code":"CANDIDATE_EMAIL", "title":"Email", "localizationCode":"email","selected":true, "relatedField":null,"mandatory":false, "changeable":false
},{
"code":"CANDIDATE_LEAD_ASSIGNEE", "title":"Lead Assignee Name", "localizationCode":"leadAssignee","selected":true, "relatedField":null,"mandatory":false, "changeable":true
},{
"code":"CANDIDATE_ACTION", "title":"Action", "localizationCode":"action","selected":true, "relatedField":null,"mandatory":false, "changeable":false
},{
"code":"CANDIDATE_NOTE", "title":"Note", "localizationCode":"note","selected":true, "relatedField":null,"mandatory":false, "changeable":false
}]');



delete
from "0".kanbanitemsettings
where code = 'CASE_ITEM_SETTINGS';

insert into "0".kanbanitemsettings(name, code, settingsJSONData)
values ('Case Kanban Item', 'CASE_ITEM_SETTINGS',
        '[{
"code":"CASE_NUMBER", "title":"Number", "localizationCode":"number", "selected":true, "relatedField":null,"mandatory":false, "changeable":true
},{
"code":"CASE_SUBJECT", "title":"Subject", "localizationCode":"subject", "selected":true, "relatedField":null,"mandatory":false, "changeable":true
},{
"code":"CASE_REPORTER", "title":"Reporter", "localizationCode":"reporter","selected":true, "relatedField":null,"mandatory":false, "changeable":true
},{
"code":"CASE_PHONE", "title":"Phone", "localizationCode":"phone","selected":true, "relatedField":null,"mandatory":false, "changeable":false
},{
"code":"CASE_EMAIL", "title":"Email", "localizationCode":"email","selected":true, "relatedField":null,"mandatory":false, "changeable":false
},{
"code":"CASE_ASSIGNE_NAME", "title":"Assignee Name", "localizationCode":"assignee","selected":true, "relatedField":null,"mandatory":false, "changeable":true
},{
"code":"CASE_ACTION", "title":"Action", "localizationCode":"action","selected":true, "relatedField":null,"mandatory":false, "changeable":false
},{
"code":"CASE_NOTE", "title":"Note", "localizationCode":"note","selected":true, "relatedField":null,"mandatory":false, "changeable":false
}]');



delete
from "0".kanbanitemsettings
where code = 'LEAD_ITEM_SETTINGS';

insert into "0".kanbanitemsettings(name, code, settingsJSONData)
values ('Lead Kanban Item', 'LEAD_ITEM_SETTINGS',
        '[{
"code":"LEAD_NAME", "title":"Name", "localizationCode":"name", "selected":true, "relatedField":null,"mandatory":false, "changeable":true
},{
"code":"LEAD_ENTRY_PHOTO", "title":"Photo", "localizationCode":"image", "selected":true, "relatedField":null,"mandatory":false, "changeable":false
},{
"code":"LEAD_INFO", "title":"Account", "localizationCode":"account", "selected":true, "relatedField":null,"mandatory":false, "changeable":true
},{
"code":"LEAD_PHONE", "title":"Phone", "localizationCode":"phone","selected":true, "relatedField":null,"mandatory":false, "changeable":false
},{
"code":"LEAD_EMAIL", "title":"Email", "localizationCode":"email","selected":true, "relatedField":null,"mandatory":false, "changeable":false
},{
"code":"LEAD_ASSIGNE_NAME", "title":"Assignee Name", "localizationCode":"assignee","selected":true, "relatedField":null,"mandatory":false, "changeable":true
},{
"code":"LEAD_ACTION", "title":"Action", "localizationCode":"action","selected":true, "relatedField":null,"mandatory":false, "changeable":false
},{
"code":"LEAD_NOTE", "title":"Note", "localizationCode":"note","selected":true, "relatedField":null,"mandatory":false, "changeable":false
}]');

delete
from "0".kanbanitemsettings
where code = 'TASK_ITEM_SETTINGS';

insert into "0".kanbanitemsettings(name, code, settingsJSONData)
values ('Task Kanban Item', 'TASK_ITEM_SETTINGS',
        '[{
"code":"TASK_NAME", "title":"Name", "localizationCode":"name", "selected":true, "relatedField":null,"mandatory":false, "changeable":true
},{
"code":"TASK_CODE", "title":"Code", "localizationCode":"codeOnly", "selected":true, "relatedField":null,"mandatory":false, "changeable":true
},{
"code":"TASK_START_DATE", "title":"Start Date", "localizationCode":"startDate", "selected":true, "relatedField":null,"mandatory":false, "changeable":true
},{
"code":"TASK_END_DATE", "title":"End Date", "localizationCode":"endDate", "selected":true, "relatedField":null,"mandatory":false, "changeable":true
},{
"code":"TASK_ASSIGNEE_EMPLOYEE", "title":"Assignee Employee", "localizationCode":"assignee", "selected":true, "relatedField":null,"mandatory":false, "changeable":true
},{
"code":"TASK_PROJECTNAME", "title":"Project Name", "localizationCode":"projectName", "selected":true, "relatedField":null,"mandatory":false, "changeable":true
},{
"code":"TASK_ACTION", "title":"Task Action", "localizationCode":"action", "selected":true, "relatedField":null,"mandatory":false, "changeable":false
}]');



