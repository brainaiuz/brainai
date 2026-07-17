
update permission set  sorder=5, parent=(select id from permission where code='CRM_CONTACTS_LIST'),name='Edit' where code='CRM_EDIT_CONTACT';
update permission set  sorder=6, parent=(select id from permission where code='CRM_CONTACTS_LIST'),name='Delete' where code='CRM_REMOVE_CONTACT';
update permission set  sorder=7, parent=(select id from permission where code='CRM_CONTACTS_LIST'),name='Send SMS' where code='ADD_CONTACT_SMS';
update permission set  sorder=8, parent=(select id from permission where code='CRM_CONTACTS_LIST'),name='Look Up' where code='CRM_CONTACT_LOOK_UP';
update permission set  sorder=9, parent=(select id from permission where code='CRM_CONTACTS_LIST'),name='Detect Duplicatesd' where code='CRM_CONTACTS_DETECT_DUBLICATES';
update permission set  sorder=10, parent=(select id from permission where code='CRM_CONTACTS_LIST'),name='Merge' where code='CRM_CONTACTS_MERGE';
update permission set  sorder=11, parent=(select id from permission where code='CRM_CONTACTS_LIST'),name='Google' where code='CRM_GOOGLE_CONTACTS';
update permission set  sorder=12, parent=(select id from permission where code='CRM_CONTACTS_LIST'),name='Import' where code='CRM_CONTACTS_IMPORT';
update permission set  sorder=13, parent=(select id from permission where code='CRM_CONTACTS_LIST'),name='Export' where code='CRM_CONTACTS_EXPORT';
update permission set  sorder=14, parent=(select id from permission where code='CRM_CONTACTS_LIST'),name='Sales change campaign' where code='CRM_CHANGE_CAMPAIGN';
update permission set  sorder=15, parent=(select id from permission where code='CRM_CONTACTS_LIST'),name='Category Move' where code='CRM_CONTACT_CATEGORY_MOVE';
update permission set  sorder=16, parent=(select id from permission where code='CRM_CONTACTS_LIST'),name='Category Copy' where code='CRM_CONTACT_CATEGORY_COPY';


delete from permission where code='CRM_QUICK_ADD_NEW_CONTACTS';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('CRM_QUICK_ADD_NEW_CONTACTS', 'CRM', 'Quick Add', 3,
        (select id from permission where code = 'CRM_CONTACTS_LIST'),'CONTACT_MANAGEMENT');

delete from "anv".permission_context where permissioncode = 'CRM_QUICK_ADD_NEW_CONTACTS';
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_QUICK_ADD_NEW_CONTACTS', 'CRM');

delete from "anv".rolepermission where permissioncode = 'CRM_QUICK_ADD_NEW_CONTACTS';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_QUICK_ADD_NEW_CONTACTS', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_QUICK_ADD_NEW_CONTACTS', 'ALLOW', 'SALESMAN');

delete from permission where code='CRM_MULTI_ADD_NEW_CONTACTS';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('CRM_MULTI_ADD_NEW_CONTACTS', 'CRM', 'Multi Add', 4,
        (select id from permission where code = 'CRM_CONTACTS_LIST'),'CONTACT_MANAGEMENT');

delete from "anv".permission_context where permissioncode = 'CRM_MULTI_ADD_NEW_CONTACTS';
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_MULTI_ADD_NEW_CONTACTS', 'CRM');

delete from "anv".rolepermission where permissioncode = 'CRM_MULTI_ADD_NEW_CONTACTS';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_MULTI_ADD_NEW_CONTACTS', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CRM_MULTI_ADD_NEW_CONTACTS', 'ALLOW', 'SALESMAN');


update permission set  sorder=1, parent=(select id from permission where code='CRM_CAMPAIGNS_LIST'),name='Add' where code='CRM_ADD_NEW_CAMPAIGN';
update permission set  sorder=2, parent=(select id from permission where code='CRM_CAMPAIGNS_LIST'),name='Edit' where code='CRM_EDIT_CAMPAIGN';
update permission set  sorder=3, parent=(select id from permission where code='CRM_CAMPAIGNS_LIST'),name='Delete' where code='CRM_REMOVE_CAMPAIGN';
update permission set  sorder=4, parent=(select id from permission where code='CRM_CAMPAIGNS_LIST'),name='Export' where code='CRM_CAMPAIGNS_EXPORT';

update permission set  sorder=1, parent=(select id from permission where code='CRM_ACTIVITIES_LIST'),name='See All' where code='CRM_SEE_ALL_ACTIVITIES_LIST';
update permission set  sorder=2, parent=(select id from permission where code='CRM_ACTIVITIES_LIST'),name='Add Event' where code='CRM_ADD_NEW_ACTIVITY_EVENT';
update permission set  sorder=3, parent=(select id from permission where code='CRM_ACTIVITIES_LIST'),name='Edit' where code='CRM_EDIT_ACTIVITY';
update permission set  sorder=4, parent=(select id from permission where code='CRM_ACTIVITIES_LIST'),name='Delete' where code='CRM_REMOVE_ACTIVITY';
update permission set  sorder=5, parent=(select id from permission where code='CRM_ACTIVITIES_LIST'),name='Add Call' where code='CRM_ADD_NEW_CAMPAIGN';
update permission set  sorder=6, parent=(select id from permission where code='CRM_ACTIVITIES_LIST'),name='View Call' where code='CRM_ADD_NEW_CAMPAIGN';

update permission set  sorder=1, parent=(select id from permission where code='CRM_TASKS_LIST'),name='See All' where code='CRM_SEE_ALL_TASKS_LIST';
update permission set  sorder=2, parent=(select id from permission where code='CRM_TASKS_LIST'),name='Add' where code='CRM_TASKS_ADD';
update permission set  sorder=3, parent=(select id from permission where code='CRM_TASKS_LIST'),name='Multi Add' where code='CRM_TASKS_ADD_MULTI';
update permission set  sorder=4, parent=(select id from permission where code='CRM_TASKS_LIST'),name='Edit' where code='CRM_TASKS_EDIT';
update permission set  sorder=5, parent=(select id from permission where code='CRM_TASKS_LIST'),name='Delete' where code='CRM_TASKS_REMOVE';
update permission set  sorder=6, parent=(select id from permission where code='CRM_TASKS_LIST'),name='More Button' where code='CRM_TASK_LIST_MORE_BUTTON';
update permission set  sorder=7, parent=(select id from permission where code='CRM_TASKS_LIST'),name='Notes' where code='CRM_TASKS_NOTES';
update permission set  sorder=8, parent=(select id from permission where code='CRM_TASKS_LIST'),name='Timer' where code='CRM_TASKS_TIMER';
update permission set  sorder=9, parent=(select id from permission where code='CRM_TASKS_LIST'),name='Documents' where code='CRM_TASKS_DOCUMENTS';
update permission set  sorder=10, parent=(select id from permission where code='CRM_TASKS_LIST'),name='Comments' where code='CRM_TASKS_COMMENTS';
update permission set  sorder=11, parent=(select id from permission where code='CRM_TASKS_LIST'),name='Issue' where code='CRM_TASKS_ISSUE';
update permission set  sorder=12, parent=(select id from permission where code='CRM_TASKS_LIST'),name='PDF' where code='CRM_TASKS_PDF_EXCEL_EXPORT';

update permission set  sorder=1, parent=(select id from permission where code='CRM_ACCOUNTS_LIST'),name='See All' where code='CRM_SEE_ALL_ACCOUNTS_LIST';
update permission set  sorder=2, parent=(select id from permission where code='CRM_ACCOUNTS_LIST'),name='Add' where code='CRM_ACCOUNT_ADD';
update permission set  sorder=3, parent=(select id from permission where code='CRM_ACCOUNTS_LIST'),name='Quick Add' where code='CRM_ACCOUNT_QUICK_ADD';
update permission set  sorder=4, parent=(select id from permission where code='CRM_ACCOUNTS_LIST'),name='Edit' where code='CRM_ACCOUNTS_EDIT';
update permission set  sorder=5, parent=(select id from permission where code='CRM_ACCOUNTS_LIST'),name='Delete' where code='CRM_ACCOUNTS_DELETE';
update permission set  sorder=6, parent=(select id from permission where code='CRM_ACCOUNTS_LIST'),name='Number Edit' where code='CRM_ACCOUNT_NUMBER_EDIT';
update permission set  sorder=7, parent=(select id from permission where code='CRM_ACCOUNTS_LIST'),name='Owner Edit' where code='CRM_ACCOUNT_OWNER_EDIT';
update permission set  sorder=8, parent=(select id from permission where code='CRM_ACCOUNTS_LIST'),name='Copy' where code='CRM_ACCOUNTS_COPY';
update permission set  sorder=9, parent=(select id from permission where code='CRM_ACCOUNTS_LIST'),name='Convert' where code='CRM_ACCOUNTS_CONVERT';
update permission set  sorder=10, parent=(select id from permission where code='CRM_ACCOUNTS_LIST'),name='Detect Duplicates' where code='CRM_ACCOUNTS_DETECT_DUBLICATES';
update permission set  sorder=11, parent=(select id from permission where code='CRM_ACCOUNTS_LIST'),name='Merge' where code='CRM_ACCOUNTS_MERGE';
update permission set  sorder=12, parent=(select id from permission where code='CRM_ACCOUNTS_LIST'),name='Export' where code='CRM_ACCOUNTS_EXPORT';
update permission set  sorder=13, parent=(select id from permission where code='CRM_ACCOUNTS_LIST'),name='Import' where code='CRM_ACCOUNTS_IMPORT';
update permission set  sorder=14, parent=(select id from permission where code='CRM_ACCOUNTS_LIST'),name='Change owner' where code='CRM_ACCOUNTS_CHANGE_OWNER';
update permission set  sorder=15, parent=(select id from permission where code='CRM_ACCOUNTS_LIST'),name='Contact access' where code='CRM_ACCOUNTS_CONTACT_ACCESS';

