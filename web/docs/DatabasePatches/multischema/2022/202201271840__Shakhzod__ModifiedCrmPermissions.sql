update permission
set parent=(select id from permission where code = 'CRM_MAIN_MENU')
where code = 'CRM_LEADS_LIST';
update permission
set parent=(select id from permission where code = 'CRM_MAIN_MENU')
where code = 'CRM_PRODUCT_LIST';
update permission
set parent=(select id from permission where code = 'CRM_MAIN_MENU')
where code = 'CRM_REQUEST_FOR_QUOTE_LIST';
update permission
set parent=(select id from permission where code = 'CRM_MAIN_MENU')
where code = 'CRM_SALES_INVOICE_LIST';
update permission
set parent=(select id from permission where code = 'CRM_MAIN_MENU')
where code = 'CRM_OPPORTUNITIES_LIST';
update permission
set parent=(select id from permission where code = 'CRM_MAIN_MENU')
where code = 'CRM_E_MAIL_MARKETING_TAB';
update permission
set parent=(select id from permission where code = 'CRM_MAIN_MENU')
where code = 'CRM_MESSAGE_CENTER';
update permission
set parent=(select id from permission where code = 'CRM_MAIN_MENU')
where code = 'CRM_CONTACTS_LIST';
update permission
set parent=(select id from permission where code = 'CRM_MAIN_MENU')
where code = 'CRM_PURCHASE_INVOICE_LIST';
update permission
set parent=(select id from permission where code = 'CRM_MAIN_MENU')
where code = 'CRM_ACTIVITIES_LIST';
update permission
set parent=(select id from permission where code = 'CRM_MAIN_MENU')
where code = 'CRM_ACCOUNTS_LIST';
update permission
set parent=(select id from permission where code = 'CRM_MAIN_MENU')
where code = 'CRM_CAMPAIGNS_LIST';
update permission
set parent=(select id from permission where code = 'CRM_MAIN_MENU')
where code = 'CRM_SALES_TAB';
update permission
set parent=(select id from permission where code = 'CRM_MAIN_MENU')
where code = 'CRM_WEB_FORMS_LIST';
update permission
set parent=(select id from permission where code = 'CRM_MAIN_MENU')
where code = 'CUSTOMER_SERVICE_TAB';
update permission
set parent=(select id from permission where code = 'CRM_MAIN_MENU')
where code = 'CRM_PURCHASE_ORDER_LIST';
update permission
set parent=(select id from permission where code = 'CRM_MAIN_MENU')
where code = 'CRM_SALES_QUOTE_LIST';
update permission
set parent=(select id from permission where code = 'CRM_MAIN_MENU')
where code = 'CRM_SALES_ORDER_LIST';
update permission
set parent=(select id from permission where code = 'CRM_MAIN_MENU')
where code = 'CRM_MAILING_LIST';

update permission
set sorder = 1
where code = 'CRM_SALES_TAB';
update permission
set sorder = 2
where code = 'CRM_LEADS_LIST';
update permission
set sorder = 3
where code = 'CRM_OPPORTUNITIES_LIST';
update permission
set sorder = 4
where code = 'CRM_ACCOUNTS_LIST';
update permission
set sorder = 5
where code = 'CRM_CONTACTS_LIST';
update permission
set sorder = 6
where code = 'CRM_ACTIVITIES_LIST';
update permission
set sorder = 7
where code = 'CRM_SALES_QUOTE_LIST';
update permission
set sorder = 8
where code = 'CRM_SALES_ORDER_LIST';
update permission
set sorder = 9
where code = 'CRM_SALES_INVOICE_LIST';
update permission
set sorder = 10
where code = 'CRM_PURCHASE_INVOICE_LIST';
update permission
set sorder = 11
where code = 'CRM_PURCHASE_ORDER_LIST';
update permission
set sorder = 12
where code = 'CRM_REQUEST_FOR_QUOTE_LIST';
update permission
set sorder = 13
where code = 'CRM_PRODUCT_LIST';
update permission
set sorder = 14
where code = 'CUSTOMER_SERVICE_TAB';
update permission
set sorder = 15
where code = 'CRM_CASES_LIST';
update permission
set sorder = 16
where code = 'CRM_TASKS_LIST';
update permission
set sorder = 17
where code = 'CRM_E_MAIL_MARKETING_TAB';
update permission
set sorder = 18
where code = 'CRM_MAILING_LIST';
update permission
set sorder = 19
where code = 'CRM_MESSAGE_CENTER';
update permission
set sorder = 20
where code = 'CRM_CAMPAIGNS_LIST';
update permission
set sorder = 21
where code = 'CRM_WEB_FORMS_LIST';



update permission
set sorder=1,
    parent=(select id from permission where code = 'CRM_LEADS_LIST'),
    name='See All'
where code = 'CRM_SEE_ALL_LEADS_LIST';
update permission
set sorder=2,
    parent=(select id from permission where code = 'CRM_LEADS_LIST'),
    name='Add'
where code = 'ADD_NEW_LEAD';
update permission
set sorder=3,
    parent=(select id from permission where code = 'CRM_LEADS_LIST'),
    name='Edit'
where code = 'CRM_LEAD_EDIT';
update permission
set sorder=4,
    parent=(select id from permission where code = 'CRM_LEADS_LIST'),
    name='Delete'
where code = 'CRM_LEAD_DELETE';

update permission
set sorder=1,
    parent=(select id from permission where code = 'CRM_OPPORTUNITIES_LIST'),
    name='See All'
where code = 'CRM_SEE_ALL_OPPORTUNITIES_LIST';
update permission
set sorder=2,
    parent=(select id from permission where code = 'CRM_OPPORTUNITIES_LIST'),
    name='Add'
where code = 'CRM_ADD_NEW_OPPORTUNITIES';
update permission
set sorder=3,
    parent=(select id from permission where code = 'CRM_OPPORTUNITIES_LIST'),
    name='Edit'
where code = 'CRM_EDIT_OPPORTUNITIES';
update permission
set sorder=4,
    parent=(select id from permission where code = 'CRM_OPPORTUNITIES_LIST'),
    name='Delete'
where code = 'CRM_REMOVE_OPPORTUNITIES';
update permission
set sorder=5,
    parent=(select id from permission where code = 'CRM_OPPORTUNITIES_LIST'),
    name='Customize Form'
where code = 'CUSTOM_FORM_2_CUSTOMIZE_FORM';

update permission
set sorder=1,
    parent=(select id from permission where code = 'CRM_ACCOUNTS_LIST'),
    name='See All'
where code = 'CRM_SEE_ALL_ACCOUNTS_LIST';
update permission
set sorder=2,
    parent=(select id from permission where code = 'CRM_ACCOUNTS_LIST'),
    name='Add'
where code = 'CRM_ACCOUNT_ADD';
update permission
set sorder=3,
    parent=(select id from permission where code = 'CRM_ACCOUNTS_LIST'),
    name='Edit'
where code = 'CRM_ACCOUNTS_EDIT';
update permission
set sorder=4,
    parent=(select id from permission where code = 'CRM_ACCOUNTS_LIST'),
    name='Delete'
where code = 'CRM_ACCOUNTS_DELETE';

update permission
set sorder=1,
    parent=(select id from permission where code = 'CRM_CONTACTS_LIST'),
    name='See All'
where code = 'CRM_SEE_ALL_CONTACT_LIST';
update permission
set sorder=2,
    parent=(select id from permission where code = 'CRM_CONTACTS_LIST'),
    name='Add'
where code = 'CRM_ADD_NEW_CONTACT';
update permission
set sorder=3,
    parent=(select id from permission where code = 'CRM_CONTACTS_LIST'),
    name='Edit'
where code = 'CRM_EDIT_CONTACT';
update permission
set sorder=4,
    parent=(select id from permission where code = 'CRM_CONTACTS_LIST'),
    name='Delete'
where code = 'CRM_REMOVE_CONTACT';

update permission
set sorder=1,
    parent=(select id from permission where code = 'CRM_ACTIVITIES_LIST'),
    name='See All'
where code = 'CRM_SEE_ALL_ACTIVITIES_LIST';
update permission
set sorder=2,
    parent=(select id from permission where code = 'CRM_ACTIVITIES_LIST'),
    name='Add'
where code = 'CRM_ADD_NEW_ACTIVITY_EVENT';
update permission
set sorder=3,
    parent=(select id from permission where code = 'CRM_ACTIVITIES_LIST'),
    name='Edit'
where code = 'CRM_EDIT_ACTIVITY';
update permission
set sorder=4,
    parent=(select id from permission where code = 'CRM_ACTIVITIES_LIST'),
    name='Delete'
where code = 'CRM_REMOVE_ACTIVITY';


update permission
set sorder=1,
    parent=(select id from permission where code = 'CRM_SALES_QUOTE_LIST'),
    name='Add'
where code = 'CRM_SALES_QUOTE_ADD';
update permission
set sorder=2,
    parent=(select id from permission where code = 'CRM_SALES_QUOTE_LIST'),
    name='Edit'
where code = 'CRM_SALES_QUOTE_EDIT';
update permission
set sorder=3,
    parent=(select id from permission where code = 'CRM_SALES_QUOTE_LIST'),
    name='Delete'
where code = 'CRM_SALES_QUOTE_DELETE';


update permission
set sorder=1,
    parent=(select id from permission where code = 'CRM_SALES_ORDER_LIST'),
    name='Add'
where code = 'CRM_SALES_ORDER_ADD';
update permission
set sorder=2,
    parent=(select id from permission where code = 'CRM_SALES_ORDER_LIST'),
    name='Edit'
where code = 'CRM_SALES_ORDER_EDIT';
update permission
set sorder=3,
    parent=(select id from permission where code = 'CRM_SALES_ORDER_LIST'),
    name='Delete'
where code = 'CRM_SALES_ORDER_DELETE';

update permission
set sorder=1,
    parent=(select id from permission where code = 'CRM_SALES_INVOICE_LIST'),
    name='Add'
where code = 'CRM_SALES_INVOICE_ADD';
update permission
set sorder=2,
    parent=(select id from permission where code = 'CRM_SALES_INVOICE_LIST'),
    name='Edit'
where code = 'CRM_SALES_INVOICE_EDIT';
update permission
set sorder=3,
    parent=(select id from permission where code = 'CRM_SALES_INVOICE_LIST'),
    name='Delete'
where code = 'CRM_SALES_INVOICE_DELETE';


update permission
set sorder=1,
    parent=(select id from permission where code = 'CRM_PURCHASE_INVOICE_LIST'),
    name='Add'
where code = 'CRM_PURCHASE_INVOICE_ADD';
update permission
set sorder=2,
    parent=(select id from permission where code = 'CRM_PURCHASE_INVOICE_LIST'),
    name='Edit'
where code = 'CRM_PURCHASE_INVOICE_EDIT';
update permission
set sorder=3,
    parent=(select id from permission where code = 'CRM_PURCHASE_INVOICE_LIST'),
    name='Delete'
where code = 'CRM_PURCHASE_INVOICE_DELETE';


update permission
set sorder=1,
    parent=(select id from permission where code = 'CRM_PURCHASE_ORDER_LIST'),
    name='Add'
where code = 'CRM_PURCHASE_ORDER_ADD';
update permission
set sorder=2,
    parent=(select id from permission where code = 'CRM_PURCHASE_ORDER_LIST'),
    name='Edit'
where code = 'CRM_PURCHASE_ORDER_EDIT';
update permission
set sorder=3,
    parent=(select id from permission where code = 'CRM_PURCHASE_ORDER_LIST'),
    name='Delete'
where code = 'CRM_PURCHASE_ORDER_DELETE';


update permission
set sorder=1,
    parent=(select id from permission where code = 'CRM_REQUEST_FOR_QUOTE_LIST'),
    name='Add'
where code = 'CRM_REQUEST_FOR_QUOTE_ADD';
update permission
set sorder=2,
    parent=(select id from permission where code = 'CRM_REQUEST_FOR_QUOTE_LIST'),
    name='Edit'
where code = 'CRM_REQUEST_FOR_QUOTE_EDIT';
update permission
set sorder=3,
    parent=(select id from permission where code = 'CRM_REQUEST_FOR_QUOTE_LIST'),
    name='Delete'
where code = 'CRM_REQUEST_FOR_QUOTE_DELETE';


update permission
set sorder=1,
    parent=(select id from permission where code = 'CRM_PRODUCT_LIST'),
    name='Add'
where code = 'ACCOUNTING_PRODUCT_ADD';
update permission
set sorder=2,
    parent=(select id from permission where code = 'CRM_PRODUCT_LIST'),
    name='Edit'
where code = 'ACCOUNTING_PRODUCT_EDIT';
update permission
set sorder=3,
    parent=(select id from permission where code = 'CRM_PRODUCT_LIST'),
    name='Delete'
where code = 'ACCOUNTING_PRODUCT_DELETE';

update permission
set sorder=1,
    parent=(select id from permission where code = 'CRM_CASES_LIST'),
    name  ='See All'
where code = 'CRM_SEE_ALL_CASES_LIST';
update permission
set sorder=2,
    parent=(select id from permission where code = 'CRM_CASES_LIST'),
    name  ='Add'
where code = 'ADD_NEW_CASE';
update permission
set sorder=3,
    parent=(select id from permission where code = 'CRM_CASES_LIST'),
    name  ='Edit'
where code = 'CRM_EDIT_CASE';
update permission
set sorder=4,
    parent=(select id from permission where code = 'CRM_CASES_LIST'),
    name  ='Delete'
where code = 'CRM_REMOVE_CASE';


update permission
set sorder=1,
    parent=(select id from permission where code = 'CRM_TASKS_LIST'),
    name  ='Add'
where code = 'CRM_TASKS_ADD';
update permission
set sorder=2,
    parent=(select id from permission where code = 'CRM_TASKS_LIST'),
    name  ='Edit'
where code = 'CRM_TASKS_EDIT';
update permission
set sorder=3,
    parent=(select id from permission where code = 'CRM_TASKS_LIST'),
    name  ='Delete'
where code = 'CRM_TASKS_REMOVE';


update permission
set sorder=1,
    parent=(select id from permission where code = 'CRM_MAILING_LIST'),
    name  ='Add'
where code = 'CRM_ADD_NEW_MAILING_LIST';
update permission
set sorder=2,
    parent=(select id from permission where code = 'CRM_MAILING_LIST'),
    name  ='Edit'
where code = 'CRM_EDIT_MAILING_LIST';
update permission
set sorder=3,
    parent=(select id from permission where code = 'CRM_MAILING_LIST'),
    name  ='Delete'
where code = 'CRM_REMOVE_MAILING_LIST';


update permission
set sorder=1,
    parent=(select id from permission where code = 'CRM_MESSAGE_CENTER'),
    name  ='Add'
where code = 'CRM_ADD_NEW_MESSAGE';
update permission
set sorder=2,
    parent=(select id from permission where code = 'CRM_MESSAGE_CENTER'),
    name  ='Edit'
where code = 'CRM_MESSAGE_EDIT';
update permission
set sorder=3,
    parent=(select id from permission where code = 'CRM_MESSAGE_CENTER'),
    name  ='Delete'
where code = 'CRM_MESSAGE_REMOVE';

update permission
set sorder=1,
    parent=(select id from permission where code = 'CRM_CAMPAIGNS_LIST'),
    name='Add'
where code = 'CRM_ADD_NEW_CAMPAIGN';
update permission
set sorder=2,
    parent=(select id from permission where code = 'CRM_CAMPAIGNS_LIST'),
    name='Edit'
where code = 'CRM_EDIT_CAMPAIGN';
update permission
set sorder=3,
    parent=(select id from permission where code = 'CRM_CAMPAIGNS_LIST'),
    name='Delete'
where code = 'CRM_REMOVE_CAMPAIGN';

update permission
set sorder=1,
    parent=(select id from permission where code = 'CRM_WEB_FORMS_LIST'),
    name  ='See All'
where code = 'CRM_SEE_ALL_WEB_FORMS_LIST';
update permission
set sorder=2,
    parent=(select id from permission where code = 'CRM_WEB_FORMS_LIST'),
    name  ='Add'
where code = 'CRM_ADD_NEW_WEB_FORM';
update permission
set sorder=3,
    parent=(select id from permission where code = 'CRM_WEB_FORMS_LIST'),
    name  ='Edit'
where code = 'CRM_EDIT_WEB_FORM';
update permission
set sorder=4,
    parent=(select id from permission where code = 'CRM_WEB_FORMS_LIST'),
    name  ='Delete'
where code = 'CRM_REMOVE_WEB_FORM';


update permission
set name = 'Sales Quote'
where code = 'CRM_SALES_QUOTE_LIST';
update permission
set name = 'Sales Order'
where code = 'CRM_SALES_ORDER_LIST';
update permission
set name = 'Sales Invoice'
where code = 'CRM_SALES_INVOICE_LIST';
update permission
set name = 'Purchase Order'
where code = 'CRM_PURCHASE_ORDER_LIST';
update permission
set name = 'Purchase Invoice'
where code = 'CRM_PURCHASE_INVOICE_LIST';
update permission
set name = 'Products/Services'
where code = 'CRM_PRODUCT_LIST';
update permission
set name = 'Request for Quote'
where code = 'CRM_REQUEST_FOR_QUOTE_LIST';
update permission
set name = 'Cases'
where code = 'CRM_CASES_LIST';
update permission
set name = 'Members'
where code = 'CRM_MAIL_LIST_MEMBERS';






