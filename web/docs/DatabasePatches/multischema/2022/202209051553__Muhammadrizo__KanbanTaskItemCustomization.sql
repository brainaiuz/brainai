delete
from "anv".kanbanitemsettings
where code = 'TASK_ITEM_SETTINGS';

insert into "anv".kanbanitemsettings(name, code, settingsJSONData)
values ('Task Kanban Item', 'TASK_ITEM_SETTINGS',
        '[{
"code":"TASK_NAME", "title":"Name", "localizationCode":"name", "selected":true, "relatedField":null,"mandatory":true, "changeable":false
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