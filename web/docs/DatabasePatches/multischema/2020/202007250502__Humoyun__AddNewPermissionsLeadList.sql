
update permission set  sorder=1, parent=(select id from permission where code='CRM_LEADS_LIST'),name='See All' where code='CRM_SEE_ALL_LEADS_LIST';
update permission set  sorder=2, parent=(select id from permission where code='CRM_LEADS_LIST'),name='Add' where code='ADD_NEW_LEAD';
update permission set  sorder=5, parent=(select id from permission where code='CRM_LEADS_LIST'),name='Edit' where code='CRM_LEAD_EDIT';
update permission set  sorder=6, parent=(select id from permission where code='CRM_LEADS_LIST'),name='Delete' where code='CRM_LEAD_DELETE';
update permission set  sorder=7, parent=(select id from permission where code='CRM_LEADS_LIST'),name='Change Status' where code='CRM_LEAD_STATUS';
update permission set  sorder=8, parent=(select id from permission where code='CRM_LEADS_LIST'),name='Send SMS' where code='ADD_LEAD_SMS';
update permission set  sorder=9, parent=(select id from permission where code='CRM_LEADS_LIST'),name='Lookup Actions' where code='CRM_LEAD_LOOKUP';
update permission set  sorder=10, parent=(select id from permission where code='CRM_LEADS_LIST'),name='Convert' where code='CRM_LEAD_CONVERT';
update permission set  sorder=11, parent=(select id from permission where code='CRM_LEADS_LIST'),name='Copy' where code='CRM_LEAD_COPY';
update permission set  sorder=12, parent=(select id from permission where code='CRM_LEADS_LIST'),name='Assignee Change' where code='CHANGE_LEADS_ASSIGNEE';
update permission set  sorder=13, parent=(select id from permission where code='CRM_LEADS_LIST'),name='Multi Assignee Change' where code='CHANGE_LEADS_MUlTI_ASSIGNEE';
update permission set  sorder=14, parent=(select id from permission where code='CRM_LEADS_LIST'),name='Campaign Change' where code='CHANGE_LEADS_CAMPAIGN';
update permission set  sorder=15, parent=(select id from permission where code='CRM_LEADS_LIST'),name='Import' where code='CRM_LEADS_IMPORT';
update permission set  sorder=16, parent=(select id from permission where code='CRM_LEADS_LIST'),name='Export' where code='CRM_LEADS_EXPORT';
update permission set  sorder=17, parent=(select id from permission where code='CRM_LEADS_LIST'),name='Assignee/Owner for Lead/Contact' where code='CRM_LEAD_CONTACT_ASSIGNEE';
update permission set  sorder=18, parent=(select id from permission where code='CRM_LEADS_LIST'),name='List view' where code='CRM_LEAD_LIST_VIEW';

delete from permission where code='CRM_QUICK_ADD_NEW_LEAD';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('CRM_QUICK_ADD_NEW_LEAD', 'CRM', 'Quick Add', 3,
        (select id from permission where code = 'CRM_LEADS_LIST'),'CRM_MODULE');

delete from "anv".permission_context where permissioncode = 'CRM_QUICK_ADD_NEW_LEAD';
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_QUICK_ADD_NEW_LEAD', 'CRM');

delete from "anv".rolepermission where permissioncode = 'CRM_QUICK_ADD_NEW_LEAD';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_QUICK_ADD_NEW_LEAD', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_QUICK_ADD_NEW_LEAD', 'ALLOW', 'SALESMAN');



delete from permission where code='CRM_MULTIPLE_ADD_NEW_LEADS';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('CRM_MULTIPLE_ADD_NEW_LEADS', 'CRM', 'Multiple Add', 4,
        (select id from permission where code = 'CRM_LEADS_LIST'),'CRM_MODULE');

delete from "anv".permission_context where permissioncode = 'CRM_MULTIPLE_ADD_NEW_LEADS';
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_MULTIPLE_ADD_NEW_LEADS', 'CRM');

delete from "anv".rolepermission where permissioncode = 'CRM_MULTIPLE_ADD_NEW_LEADS';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_MULTIPLE_ADD_NEW_LEADS', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_MULTIPLE_ADD_NEW_LEADS', 'ALLOW', 'SALESMAN');