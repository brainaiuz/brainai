

update permission set  sorder=1, parent=(select id from permission where code='CRM_MAIN_MENU'),name='Sales' where code='CRM_SALES_TAB';

update permission set  sorder=1, parent=(select id from permission where code='CRM_SALES_TAB'),name='Leads List' where code='CRM_LEADS_LIST';
update permission set  sorder=2, parent=(select id from permission where code='CRM_SALES_TAB'),name='Opportunities List' where code='CRM_OPPORTUNITIES_LIST';

update permission set  sorder=1, parent=(select id from permission where code='CRM_LEADS_LIST'),name='See All' where code='CRM_SEE_ALL_LEADS_LIST';
update permission set  sorder=2, parent=(select id from permission where code='CRM_LEADS_LIST'),name='Add' where code='ADD_NEW_LEAD';
update permission set  sorder=3, parent=(select id from permission where code='CRM_LEADS_LIST'),name='Edit' where code='CRM_LEAD_EDIT';
update permission set  sorder=4, parent=(select id from permission where code='CRM_LEADS_LIST'),name='Delete' where code='CRM_LEAD_DELETE';
update permission set  sorder=5, parent=(select id from permission where code='CRM_LEADS_LIST'),name='Change Status' where code='CRM_LEAD_STATUS';
update permission set  sorder=6, parent=(select id from permission where code='CRM_LEADS_LIST'),name='Send SMS' where code='ADD_LEAD_SMS';
update permission set  sorder=7, parent=(select id from permission where code='CRM_LEADS_LIST'),name='Lookup Actions' where code='CRM_LEAD_LOOKUP';
update permission set  sorder=8, parent=(select id from permission where code='CRM_LEADS_LIST'),name='Convert' where code='CRM_LEAD_CONVERT';
update permission set  sorder=9, parent=(select id from permission where code='CRM_LEADS_LIST'),name='Copy' where code='CRM_LEAD_COPY';
update permission set  sorder=10, parent=(select id from permission where code='CRM_LEADS_LIST'),name='Assignee Change' where code='CHANGE_LEADS_ASSIGNEE';
update permission set  sorder=11, parent=(select id from permission where code='CRM_LEADS_LIST'),name='Multi Assignee Change' where code='CHANGE_LEADS_MUlTI_ASSIGNEE';
update permission set  sorder=12, parent=(select id from permission where code='CRM_LEADS_LIST'),name='Campaign Change' where code='CHANGE_LEADS_CAMPAIGN';
update permission set  sorder=13, parent=(select id from permission where code='CRM_LEADS_LIST'),name='Import' where code='CRM_LEADS_IMPORT';
update permission set  sorder=14, parent=(select id from permission where code='CRM_LEADS_LIST'),name='Export' where code='CRM_LEADS_EXPORT';

update permission set  sorder=1, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='See All' where code='CRM_SEE_ALL_OPPORTUNITIES_LIST';
update permission set  sorder=2, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Add' where code='CRM_ADD_NEW_OPPORTUNITIES';
update permission set  sorder=3, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Edit' where code='CRM_EDIT_OPPORTUNITIES';
update permission set  sorder=4, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Copy' where code='CRM_COPY_OPPORTUNITIES';
update permission set  sorder=5, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Delete' where code='CRM_REMOVE_OPPORTUNITIES';
update permission set  sorder=6, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Add Note' where code='CRM_ADD_OPPORTUNITY_NOTE';
update permission set  sorder=7, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Change Stage' where code='CRM_OPPORTUNITY_CHANGE_STAGE';
update permission set  sorder=8, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Change Campaign' where code='CHANGE_OPPORTUNITIES_CAMPAIGN';
update permission set  sorder=9, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Convert to SQ' where code='CONVERT_OPPORTUNITY_TO_SQ';
update permission set  sorder=10, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Convert to SO' where code='CONVERT_OPPORTUNITY_TO_SO';
update permission set  sorder=11, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Convert to RFQ' where code='CONVERT_OPPORTUNITY_TO_RFQ';
update permission set  sorder=12, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Convert to Project' where code='CONVERT_OPPORTUNITY_TO_PROJECT';
update permission set  sorder=13, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Import' where code='CRM_OPPORTUNITIES_IMPORT_LIST';
update permission set  sorder=14, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Export' where code='CRM_OPPORTUNITIES_EXPORT_LIST';
update permission set  sorder=15, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Expense Claim' where code='CRM_OPPORTUNITIES_EXPENSE_CLAIM_LIST';
update permission set  sorder=16, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='RFQ' where code='CRM_OPPORTUNITIES_RFQ_LIST';
update permission set  sorder=3, parent=(select id from permission where code='CRM_SALES_TAB'),name='Accounts List' where code='CRM_ACCOUNTS_LIST';
update permission set  sorder=1, parent=(select id from permission where code='CRM_ACCOUNTS_LIST'),name='See All' where code='CRM_SEE_ALL_ACCOUNTS_LIST';
update permission set  sorder=2, parent=(select id from permission where code='CRM_ACCOUNTS_LIST'),name='Add Account' where code='CRM_ACCOUNT_ADD';
update permission set  sorder=3, parent=(select id from permission where code='CRM_ACCOUNTS_LIST'),name='Add Supplier' where code='CRM_SUPPLIER_ADD';
update permission set  sorder=4, parent=(select id from permission where code='CRM_ACCOUNTS_LIST'),name='Add Client' where code='CRM_CLIENT_ADD';
update permission set  sorder=5, parent=(select id from permission where code='CRM_ACCOUNTS_LIST'),name='Edit' where code='CRM_ACCOUNTS_EDIT';
update permission set  sorder=6, parent=(select id from permission where code='CRM_ACCOUNTS_LIST'),name='Copy' where code='CRM_ACCOUNTS_COPY';
update permission set  sorder=7, parent=(select id from permission where code='CRM_ACCOUNTS_LIST'),name='Delete' where code='CRM_ACCOUNTS_DELETE';
update permission set  sorder=8, parent=(select id from permission where code='CRM_ACCOUNTS_LIST'),name='Convert' where code='CRM_ACCOUNTS_CONVERT';
update permission set  sorder=9, parent=(select id from permission where code='CRM_ACCOUNTS_LIST'),name='Detect Duplicates' where code='CRM_ACCOUNTS_DETECT_DUBLICATES';
update permission set  sorder=10, parent=(select id from permission where code='CRM_ACCOUNTS_LIST'),name='Merge' where code='CRM_ACCOUNTS_MERGE';
update permission set  sorder=11, parent=(select id from permission where code='CRM_ACCOUNTS_LIST'),name='Import' where code='CRM_ACCOUNTS_IMPORT';
update permission set  sorder=12, parent=(select id from permission where code='CRM_ACCOUNTS_LIST'),name='Export' where code='CRM_ACCOUNTS_EXPORT';


update permission set  sorder=1, parent=(select id from permission where code='CRM_ACCOUNTS_EDIT'),name='Owner Edit' where code='CRM_ACCOUNT_OWNER_EDIT';
update permission set  sorder=1, parent=(select id from permission where code='CRM_CLIENT_ADD'),name='Number Edit' where code='CRM_ACCOUNT_NUMBER_EDIT';
update permission set  sorder=4, parent=(select id from permission where code='CRM_SALES_TAB'),name='Contacts List' where code='CRM_CONTACTS_LIST';
update permission set  sorder=5, parent=(select id from permission where code='CRM_SALES_TAB'),name='Campaign List' where code='CRM_CAMPAIGNS_LIST';
update permission set  sorder=6, parent=(select id from permission where code='CRM_SALES_TAB'),name='Activities List' where code='CRM_ACTIVITIES_LIST';
update permission set  sorder=7, parent=(select id from permission where code='CRM_SALES_TAB'),name='Calendar' where code='CRM_Calendar';
update permission set  sorder=8, parent=(select id from permission where code='CRM_SALES_TAB') where code='CRM_TASKS_LIST';


update permission set  sorder=1, parent=(select id from permission where code='CRM_ACTIVITIES_LIST'),name='See All' where code='CRM_SEE_ALL_ACTIVITIES_LIST';
update permission set  sorder=2, parent=(select id from permission where code='CRM_ACTIVITIES_LIST'),name='Add' where code='CRM_ADD_NEW_ACTIVITY_EVENT';
update permission set  sorder=3, parent=(select id from permission where code='CRM_ACTIVITIES_LIST'),name='Edit' where code='CRM_EDIT_ACTIVITY';
update permission set  sorder=4, parent=(select id from permission where code='CRM_ACTIVITIES_LIST'),name='Delete' where code='CRM_REMOVE_ACTIVITY';
update permission set  sorder=5, parent=(select id from permission where code='CRM_ACTIVITIES_LIST'),name='Add Log a Call' where code='CRM_ADD_NEW_ACTIVITY_LOG_A_CALL';
update permission set  sorder=6, parent=(select id from permission where code='CRM_ACTIVITIES_LIST'),name='Summary Log a Call' where code='CRM_ACTIVITIES_LOG_CALL_VIEW';
update permission set  sorder=7, parent=(select id from permission where code='CRM_ACTIVITIES_LIST'),name='Attachments' where code='CRM_ACTIVITY_SEE_ATTACHMENTS';
update permission set  sorder=8, parent=(select id from permission where code='CRM_ACTIVITIES_LIST'),name='All Attachments' where code='CRM_ACTIVITY_SEE_ALL_ATTACHMENTS';
update permission set  sorder=1, parent=(select id from permission where code='CRM_CAMPAIGNS_LIST'),name='Add' where code='CRM_ADD_NEW_CAMPAIGN';
update permission set  sorder=2, parent=(select id from permission where code='CRM_CAMPAIGNS_LIST'),name='Edit' where code='CRM_EDIT_CAMPAIGN';
update permission set  sorder=3, parent=(select id from permission where code='CRM_CAMPAIGNS_LIST'),name='Delete' where code='CRM_REMOVE_CAMPAIGN';
update permission set  sorder=4, parent=(select id from permission where code='CRM_CAMPAIGNS_LIST'),name='Export' where code='CRM_CAMPAIGNS_EXPORT';

update permission set  sorder=1, parent=(select id from permission where code='CRM_CONTACTS_LIST'),name='See All' where code='CRM_SEE_ALL_CONTACT_LIST';
update permission set  sorder=2, parent=(select id from permission where code='CRM_CONTACTS_LIST'),name='Add' where code='CRM_ADD_NEW_CONTACT';
update permission set  sorder=3, parent=(select id from permission where code='CRM_CONTACTS_LIST'),name='Edit' where code='CRM_EDIT_CONTACT';
update permission set  sorder=4, parent=(select id from permission where code='CRM_CONTACTS_LIST'),name='Delete' where code='CRM_REMOVE_CONTACT';
update permission set  sorder=5, parent=(select id from permission where code='CRM_CONTACTS_LIST'),name='Send SMS' where code='ADD_CONTACT_SMS';
update permission set  sorder=6, parent=(select id from permission where code='CRM_CONTACTS_LIST'),name='Look Up' where code='CRM_CONTACT_LOOK_UP';
update permission set  sorder=7, parent=(select id from permission where code='CRM_CONTACTS_LIST'),name='Detect Duplicates' where code='CRM_CONTACTS_DETECT_DUBLICATES';
update permission set  sorder=8, parent=(select id from permission where code='CRM_CONTACTS_LIST'),name='Merge' where code='CRM_CONTACTS_MERGE';
update permission set  sorder=10, parent=(select id from permission where code='CRM_CONTACTS_LIST'),name='Google' where code='CRM_GOOGLE_CONTACTS';
update permission set  sorder=12, parent=(select id from permission where code='CRM_CONTACTS_LIST'),name='Import' where code='CRM_CONTACTS_IMPORT';
update permission set  sorder=13, parent=(select id from permission where code='CRM_CONTACTS_LIST'),name='Export' where code='CRM_CONTACTS_EXPORT';
update permission set  sorder=14, parent=(select id from permission where code='CRM_CONTACTS_LIST'),name='Category Move' where code='CRM_CONTACT_CATEGORY_MOVE';
update permission set  sorder=15, parent=(select id from permission where code='CRM_CONTACTS_LIST'),name='Category Copy' where code='CRM_CONTACT_CATEGORY_COPY';

update permission set  sorder=2, parent=(select id from permission where code='CRM_MAIN_MENU'), name ='Customer Service' where code='CUSTOMER_SERVICE_TAB';
update permission set  sorder=3, parent=(select id from permission where code='CRM_MAIN_MENU'), name ='Marketing' where code='CRM_E_MAIL_MARKETING_TAB';


update permission set  sorder=1, parent=(select id from permission where code='CUSTOMER_SERVICE_TAB'),name ='Cases List' where code='CRM_CASES_LIST';
update permission set  sorder=2, parent=(select id from permission where code='CUSTOMER_SERVICE_TAB'),name ='Solutions List' where code='CRM_SOLUTIONS_LIST';


update permission set  sorder=1, parent=(select id from permission where code='CRM_SOLUTIONS_LIST'),name ='Add' where code='ADD_NEW_SOLUTION';
update permission set  sorder=2, parent=(select id from permission where code='CRM_SOLUTIONS_LIST'),name ='Edit' where code='CRM_EDIT_SOLUTION';
update permission set  sorder=3, parent=(select id from permission where code='CRM_SOLUTIONS_LIST'),name ='Delete' where code='CRM_REMOVE_SOLUTION';
update permission set  sorder=4, parent=(select id from permission where code='CRM_SOLUTIONS_LIST'),name ='Export' where code='CRM_SOLUTIONS_EXPORT';

update permission set  sorder=1, parent=(select id from permission where code='CRM_CASES_LIST'),name ='See All' where code='CRM_SEE_ALL_CASES_LIST';
update permission set  sorder=2, parent=(select id from permission where code='CRM_CASES_LIST'),name ='Add' where code='ADD_NEW_CASE';
update permission set  sorder=3, parent=(select id from permission where code='CRM_CASES_LIST'),name ='Edit' where code='CRM_EDIT_CASE';
update permission set  sorder=4, parent=(select id from permission where code='CRM_CASES_LIST'),name ='Delete' where code='CRM_REMOVE_CASE';
update permission set  sorder=5, parent=(select id from permission where code='CRM_CASES_LIST'),name ='Copy' where code='CRM_COPY_CASE';
update permission set  sorder=6, parent=(select id from permission where code='CRM_CASES_LIST'),name ='Change Status' where code='CRM_CHANGE_STATUS_CASE';
update permission set  sorder=7, parent=(select id from permission where code='CRM_CASES_LIST'),name ='Change Priority' where code='CRM_CHANGE_PRIORITY_CASE';
update permission set  sorder=8, parent=(select id from permission where code='CRM_CASES_LIST'),name ='Change Assignee' where code='CRM_CHANGE_ASSIGNEE_CASE';
update permission set  sorder=9, parent=(select id from permission where code='CRM_CASES_LIST'),name ='Close' where code='CRM_CLOSE_CASE';
update permission set  sorder=10, parent=(select id from permission where code='CRM_CASES_LIST'),name ='Convert' where code='CRM_CONVERT_TO_CASE';
update permission set  sorder=11, parent=(select id from permission where code='CRM_CASES_LIST'),name ='Export' where code='CRM_CASES_EXPORT';
update permission set  sorder=1, parent=(select id from permission where code='CRM_E_MAIL_MARKETING_TAB'),name ='Mailing List' where code='CRM_MAILING_LIST';
update permission set  sorder=2, parent=(select id from permission where code='CRM_E_MAIL_MARKETING_TAB'),name ='Draft Messages' where code='CRM_DRAFT_MESSAGES_LIST';
update permission set  sorder=3, parent=(select id from permission where code='CRM_E_MAIL_MARKETING_TAB'),name ='Scheduled Messages' where code='CRM_QUEUED_MESSAGES_LIST';
update permission set  sorder=4, parent=(select id from permission where code='CRM_E_MAIL_MARKETING_TAB'),name ='Sent Messages' where code='CRM_SENT_MESSAGES_LIST';
update permission set  sorder=5, parent=(select id from permission where code='CRM_E_MAIL_MARKETING_TAB'),name ='Message Center' where code='CRM_MESSAGE_CENTER';
update permission set  sorder=6, parent=(select id from permission where code='CRM_E_MAIL_MARKETING_TAB'),name ='Web Forms List' where code='CRM_WEB_FORMS_LIST';

update permission set  sorder=1, parent=(select id from permission where code='CRM_WEB_FORMS_LIST'),name ='See All' where code='CRM_SEE_ALL_WEB_FORMS_LIST';
update permission set  sorder=2, parent=(select id from permission where code='CRM_WEB_FORMS_LIST'),name ='Add' where code='CRM_ADD_NEW_WEB_FORM';
update permission set  sorder=3, parent=(select id from permission where code='CRM_WEB_FORMS_LIST'),name ='Edit' where code='CRM_EDIT_WEB_FORM';
update permission set  sorder=4, parent=(select id from permission where code='CRM_WEB_FORMS_LIST'),name ='Delete' where code='CRM_REMOVE_WEB_FORM';

update permission set  sorder=1, parent=(select id from permission where code='CRM_MESSAGE_CENTER'),name ='Add' where code='CRM_ADD_NEW_MESSAGE';
update permission set  sorder=2, parent=(select id from permission where code='CRM_MESSAGE_CENTER'),name ='Edit' where code='CRM_MESSAGE_EDIT';
update permission set  sorder=3, parent=(select id from permission where code='CRM_MESSAGE_CENTER'),name ='Delete' where code='CRM_MESSAGE_REMOVE';
update permission set  sorder=4, parent=(select id from permission where code='CRM_MESSAGE_CENTER'),name ='Export' where code='CRM_MESSAGES_EXPORT';

update permission set  sorder=1, parent=(select id from permission where code='CRM_SENT_MESSAGES_LIST'),name ='Track Messages' where code='CRM_TRACK_MESSAGES_IN_SENT_MESSAGE';
update permission set  sorder=2, parent=(select id from permission where code='CRM_SENT_MESSAGES_LIST'),name ='Update Statistics' where code='CRM_UPDATE_STATISTIC_IN_SENT_MESSAGE';

update permission set  sorder=1, parent=(select id from permission where code='CRM_MAILING_LIST'),name ='See All' where code='CRM_SEE_ALL_MAILLIST_LIST';
update permission set  sorder=2, parent=(select id from permission where code='CRM_MAILING_LIST'),name ='Add' where code='CRM_ADD_NEW_MAILING_LIST';
update permission set  sorder=3, parent=(select id from permission where code='CRM_MAILING_LIST'),name ='Edit' where code='CRM_EDIT_MAILING_LIST';
update permission set  sorder=4, parent=(select id from permission where code='CRM_MAILING_LIST'),name ='Delete' where code='CRM_REMOVE_MAILING_LIST';
update permission set  sorder=5, parent=(select id from permission where code='CRM_MAILING_LIST'),name ='Members List' where code='CRM_MAIL_LIST_MEMBERS';

update permission set  sorder=9, parent=(select id from permission where code='CRM_SALES_TAB') where code='CRM_SALES_ORDER_LIST';
update permission set  sorder=10, parent=(select id from permission where code='CRM_SALES_TAB') where code='CRM_SALES_QUOTE_LIST';
update permission set  sorder=11, parent=(select id from permission where code='CRM_SALES_TAB') where code='CRM_SALES_INVOICE_LIST';
update permission set  sorder=12, parent=(select id from permission where code='CRM_SALES_TAB') where code='CRM_PURCHASE_ORDER_LIST';
update permission set  sorder=13, parent=(select id from permission where code='CRM_SALES_TAB') where code='CRM_PURCHASE_INVOICE_LIST';

delete from permission where code='CRM_VIEW_LEAD_CHANGE_LOG';
delete from permission where code='CRM_WELCOME_PAGE';