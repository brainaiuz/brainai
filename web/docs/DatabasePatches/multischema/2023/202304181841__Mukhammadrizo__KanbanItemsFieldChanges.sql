update "anv".kanbanitemsettings
set settingsjsondata = '[{
"code":"LEAD_NAME", "title":"Name", "localizationCode":"name", "selected":true, "relatedField":null,"mandatory":false, "changeable":true
},{
"code":"LEAD_ENTRY_PHOTO", "title":"Photo", "localizationCode":"image", "selected":true, "relatedField":null,"mandatory":false, "changeable":true
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
}]' where code = 'LEAD_ITEM_SETTINGS';

update "anv".kanbanitemsettings
set settingsjsondata = '[{
"code":"CANDIDATE_NAME", "title":"Name", "localizationCode":"name", "selected":true, "relatedField":null,"mandatory":false, "changeable":true
},{
"code":"CANDIDATE_PHONE", "title":"Phone", "localizationCode":"phone","selected":true, "relatedField":null,"mandatory":false, "changeable":false
},{
"code":"CANDIDATE_EMAIL", "title":"Email", "localizationCode":"email","selected":true, "relatedField":null,"mandatory":false, "changeable":false
},{
"code":"CANDIDATE_LEAD_ASSIGNEE", "title":"Lead Assignee Name", "localizationCode":"leadAssignee","selected":true, "relatedField":null,"mandatory":false, "changeable":true
},{
"code":"CANDIDATE_LEAD_NAME", "title":"Account", "localizationCode":"lead","selected":true, "relatedField":null,"mandatory":false, "changeable":true
},{
"code":"CANDIDATE_ACTION", "title":"Action", "localizationCode":"action","selected":true, "relatedField":null,"mandatory":false, "changeable":false
},{
"code":"CANDIDATE_NOTE", "title":"Note", "localizationCode":"note","selected":true, "relatedField":null,"mandatory":false, "changeable":false
}]' where code = 'CANDIDATE_ITEM_SETTINGS';