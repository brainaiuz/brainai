delete
from "anv".kanbanitemsettings
where code = 'CASE_ITEM_SETTINGS';

insert into "anv".kanbanitemsettings(name, code, settingsJSONData)
values ('Case Kanban Item', 'CASE_ITEM_SETTINGS',
        '[{
"code":"CASE_NUMBER", "title":"Number", "localizationCode":"number", "selected":true, "relatedField":null,"mandatory":false, "changeable":false
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
where code = 'CASE_ITEM_SETTINGS';

insert into "0".kanbanitemsettings(name, code, settingsJSONData)
values ('Case Kanban Item', 'CASE_ITEM_SETTINGS',
        '[{
"code":"CASE_NUMBER", "title":"Number", "localizationCode":"number", "selected":true, "relatedField":null,"mandatory":false, "changeable":false
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

