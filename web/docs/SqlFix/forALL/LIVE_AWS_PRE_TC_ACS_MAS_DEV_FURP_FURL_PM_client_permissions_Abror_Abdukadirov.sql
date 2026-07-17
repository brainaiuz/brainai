insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_SALES_QUOTE_LIST','PM','Sales Quote List',1,'false',(select id from permission where code='PM_MAIN_MENU'),'false','SALES_QUOTES');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_LIST', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_LIST', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_LIST', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_LIST', 'ACCOUNTANT', 'ALLOW');

insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_SALES_QUOTE_ADD','PM','Sales Quote Add',2,'false',(select id from permission where code='PM_SALES_QUOTE_LIST'),'false','SALES_QUOTES');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_ADD', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_ADD', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_ADD', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_ADD', 'ACCOUNTANT', 'ALLOW');

insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_SALES_QUOTE_EDIT','PM','Sales Quote Edit',3,'false',(select id from permission where code='PM_SALES_QUOTE_LIST'),'false','SALES_QUOTES');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_EDIT', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_EDIT', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_EDIT', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_EDIT', 'ACCOUNTANT', 'ALLOW');

insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_SALES_QUOTE_DELETE','PM','Sales Quote Delete',4,'false',(select id from permission where code='PM_SALES_QUOTE_LIST'),'false','SALES_QUOTES');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_DELETE', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_DELETE', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_DELETE', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_DELETE', 'ACCOUNTANT', 'ALLOW');
insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_SALES_QUOTE_SUMMARY','PM','Sales Quote Summary',5,'false',(select id from permission where code='PM_SALES_QUOTE_LIST'),'false','SALES_QUOTES');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_SUMMARY', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_SUMMARY', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_SUMMARY', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_SUMMARY', 'ACCOUNTANT', 'ALLOW');

insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_SALES_QUOTE_PDF','PM','Sales Quote PDF',6,'false',(select id from permission where code='PM_SALES_QUOTE_LIST'),'false','SALES_QUOTES');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_PDF', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_PDF', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_PDF', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_PDF', 'ACCOUNTANT', 'ALLOW');

insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_SALES_INVOICE_LIST','PM','Sales Invoice List',1,'false',(select id from permission where code='PM_MAIN_MENU'),'false','SALES_INVOICING');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_LIST', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_LIST', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_LIST', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_LIST', 'ACCOUNTANT', 'ALLOW');

insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_SALES_INVOICE_ADD','PM','Sales Invoice Add',2,'false',(select id from permission where code='PM_SALES_INVOICE_LIST'),'false','SALES_INVOICING');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_ADD', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_ADD', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_ADD', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_ADD', 'ACCOUNTANT', 'ALLOW');

insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_SALES_INVOICE_EDIT','PM','Sales Invoice Edit',3,'false',(select id from permission where code='PM_SALES_INVOICE_LIST'),'false','SALES_INVOICING');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_EDIT', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_EDIT', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_EDIT', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_EDIT', 'ACCOUNTANT', 'ALLOW');

insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_SALES_INVOICE_DELETE','PM','Sales Invoice Delete',4,'false',(select id from permission where code='PM_SALES_INVOICE_LIST'),'false','SALES_INVOICING');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_DELETE', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_DELETE', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_DELETE', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_DELETE', 'ACCOUNTANT', 'ALLOW');

insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_SALES_INVOICE_SUMMARY','PM','Sales Invoice Summary',5,'false',(select id from permission where code='PM_SALES_INVOICE_LIST'),'false','SALES_INVOICING');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_SUMMARY', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_SUMMARY', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_SUMMARY', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_SUMMARY', 'ACCOUNTANT', 'ALLOW');

insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_SALES_INVOICE_PDF','PM','Sales Invoice Pdf',6,'false',(select id from permission where code='PM_SALES_INVOICE_LIST'),'false','SALES_INVOICING');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_PDF', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_PDF', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_PDF', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_PDF', 'ACCOUNTANT', 'ALLOW');

insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_SALES_ORDER_LIST','PM','Sales Order List',1,'false',(select id from permission where code='PM_MAIN_MENU'),'false','SALES_ORDERS');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_LIST', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_LIST', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_LIST', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_LIST', 'ACCOUNTANT', 'ALLOW');

insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_SALES_ORDER_ADD','PM','Sales Order Add',2,'false',(select id from permission where code='PM_SALES_ORDER_LIST'),'false','SALES_ORDERS');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_ADD', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_ADD', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_ADD', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_ADD', 'ACCOUNTANT', 'ALLOW');


insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_SALES_ORDER_EDIT','PM','Sales Order Edit',3,'false',(select id from permission where code='PM_SALES_ORDER_LIST'),'false','SALES_ORDERS');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_EDIT', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_EDIT', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_EDIT', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_EDIT', 'ACCOUNTANT', 'ALLOW');


insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_SALES_ORDER_DELETE','PM','Sales Order Delete',4,'false',(select id from permission where code='PM_SALES_ORDER_LIST'),'false','SALES_ORDERS');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_DELETE', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_DELETE', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_DELETE', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_DELETE', 'ACCOUNTANT', 'ALLOW');
insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_SALES_ORDER_SUMMARY','PM','Sales Order Summary',5,'false',(select id from permission where code='PM_SALES_ORDER_LIST'),'false','SALES_ORDERS');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_SUMMARY', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_SUMMARY', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_SUMMARY', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_SUMMARY', 'ACCOUNTANT', 'ALLOW');
insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_SALES_ORDER_PDF','PM','Sales Order Pdf',6,'false',(select id from permission where code='PM_SALES_ORDER_LIST'),'false','SALES_ORDERS');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_PDF', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_PDF', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_PDF', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_PDF', 'ACCOUNTANT', 'ALLOW');

insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_OPPORTUNITY_LIST','PM','Opportunity List',1,'false',(select id from permission where code='PM_MAIN_MENU'),'false','OPPORTUNITY_TRACKING');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_LIST', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_LIST', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_LIST', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_LIST', 'ACCOUNTANT', 'ALLOW');
insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_OPPORTUNITY_ADD','PM','Opportunity Add',2,'false',(select id from permission where code='PM_OPPORTUNITY_LIST'),'false','OPPORTUNITY_TRACKING');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_ADD', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_ADD', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_ADD', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_ADD', 'ACCOUNTANT', 'ALLOW');

insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_OPPORTUNITY_EDIT','PM','Opportunity Edit',3,'false',(select id from permission where code='PM_OPPORTUNITY_LIST'),'false','OPPORTUNITY_TRACKING');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_EDIT', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_EDIT', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_EDIT', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_EDIT', 'ACCOUNTANT', 'ALLOW');

insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_OPPORTUNITY_DELETE','PM','Opportunity Delete',4,'false',(select id from permission where code='PM_OPPORTUNITY_LIST'),'false','OPPORTUNITY_TRACKING');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_DELETE', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_DELETE', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_DELETE', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_DELETE', 'ACCOUNTANT', 'ALLOW');

insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_OPPORTUNITY_SUMMARY','PM','Opportunity Summary',5,'false',(select id from permission where code='PM_OPPORTUNITY_LIST'),'false','OPPORTUNITY_TRACKING');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_SUMMARY', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_SUMMARY', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_SUMMARY', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_SUMMARY', 'ACCOUNTANT', 'ALLOW');

insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_CONTACT_LIST','PM','Contact List',1,'false',(select id from permission where code='PM_MAIN_MENU'),'false','CONTACT_MANAGEMENT');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_LIST', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_LIST', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_LIST', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_LIST', 'ACCOUNTANT', 'ALLOW');

insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_CONTACT_ADD','PM','Contact Add',2,'false',(select id from permission where code='PM_CONTACT_LIST'),'false','CONTACT_MANAGEMENT');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_ADD', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_ADD', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_ADD', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_ADD', 'ACCOUNTANT', 'ALLOW');
insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_CONTACT_EDIT','PM','Contact Edit',3,'false',(select id from permission where code='PM_CONTACT_LIST'),'false','CONTACT_MANAGEMENT');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_EDIT', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_EDIT', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_EDIT', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_EDIT', 'ACCOUNTANT', 'ALLOW');
insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_CONTACT_DELETE','PM','Contact Delete',4,'false',(select id from permission where code='PM_CONTACT_LIST'),'false','CONTACT_MANAGEMENT');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_DELETE', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_DELETE', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_DELETE', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_DELETE', 'ACCOUNTANT', 'ALLOW');
insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_CONTACT_SUMMARY','PM','Contact Summary',5,'false',(select id from permission where code='PM_CONTACT_LIST'),'false','CONTACT_MANAGEMENT');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_SUMMARY', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_SUMMARY', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_SUMMARY', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_SUMMARY', 'ACCOUNTANT', 'ALLOW');

insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_EVENT_LIST','PM','Event List',1,'false',(select id from permission where code='PM_MAIN_MENU'),'false','ACTIVITIES');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_LIST', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_LIST', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_LIST', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_LIST', 'ACCOUNTANT', 'ALLOW');
insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_EVENT_ADD','PM','Event Add',2,'false',(select id from permission where code='PM_EVENT_LIST'),'false','ACTIVITIES');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_ADD', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_ADD', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_ADD', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_ADD', 'ACCOUNTANT', 'ALLOW');
insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_EVENT_EDIT','PM','Event Edit',3,'false',(select id from permission where code='PM_EVENT_LIST'),'false','ACTIVITIES');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_EDIT', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_EDIT', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_EDIT', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_EDIT', 'ACCOUNTANT', 'ALLOW');
insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_EVENT_DELETE','PM','Event Delete',4,'false',(select id from permission where code='PM_EVENT_LIST'),'false','ACTIVITIES');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_DELETE', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_DELETE', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_DELETE', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_DELETE', 'ACCOUNTANT', 'ALLOW');
insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_EVENT_SUMMARY','PM','Event Summary',5,'false',(select id from permission where code='PM_EVENT_LIST'),'false','ACTIVITIES');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_SUMMARY', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_SUMMARY', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_SUMMARY', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_SUMMARY', 'ACCOUNTANT', 'ALLOW');
insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_CASE_LIST','PM','Case List',1,'false',(select id from permission where code='PM_MAIN_MENU'),'false','CASE_MANAGEMENT');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CASE_LIST', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CASE_LIST', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CASE_LIST', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CASE_LIST', 'ACCOUNTANT', 'ALLOW');

insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_CASE_ADD','PM','Case Add',2,'false',(select id from permission where code='PM_CASE_LIST'),'false','CASE_MANAGEMENT');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CASE_ADD', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CASE_ADD', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CASE_ADD', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CASE_ADD', 'ACCOUNTANT', 'ALLOW');
insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_CASE_EDIT','PM','Case Edit',3,'false',(select id from permission where code='PM_CASE_LIST'),'false','CASE_MANAGEMENT');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CASE_EDIT', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CASE_EDIT', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CASE_EDIT', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CASE_EDIT', 'ACCOUNTANT', 'ALLOW');

insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_CASE_DELETE','PM','Case Delete',4,'false',(select id from permission where code='PM_CASE_LIST'),'false','CASE_MANAGEMENT');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CASE_DELETE', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CASE_DELETE', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CASE_DELETE', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CASE_DELETE', 'ACCOUNTANT', 'ALLOW');
insert into permission (code,context,name,sorder,ismainmenu,parent,iscore,modulecode) values
('PM_CASE_SUMMARY','PM','Case Summary',5,'false',(select id from permission where code='PM_CASE_LIST'),'false','CASE_MANAGEMENT');

insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CASE_SUMMARY', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CASE_SUMMARY', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CASE_SUMMARY', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_CASE_SUMMARY', 'ACCOUNTANT', 'ALLOW');



-----For all

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_LIST', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_LIST', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_LIST', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_LIST', 'ACCOUNTANT', 'ALLOW');


insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_ADD', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_ADD', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_ADD', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_ADD', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_EDIT', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_EDIT', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_EDIT', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_EDIT', 'ACCOUNTANT', 'ALLOW');


insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_DELETE', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_DELETE', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_DELETE', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_DELETE', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_SUMMARY', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_SUMMARY', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_SUMMARY', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_SUMMARY', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_PDF', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_PDF', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_PDF', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_QUOTE_PDF', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_LIST', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_LIST', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_LIST', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_LIST', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_ADD', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_ADD', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_ADD', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_ADD', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_EDIT', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_EDIT', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_EDIT', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_EDIT', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_DELETE', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_DELETE', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_DELETE', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_DELETE', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_SUMMARY', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_SUMMARY', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_SUMMARY', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_SUMMARY', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_PDF', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_PDF', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_PDF', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_INVOICE_PDF', 'ACCOUNTANT', 'ALLOW');


insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_LIST', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_LIST', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_LIST', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_LIST', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_ADD', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_ADD', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_ADD', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_ADD', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_EDIT', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_EDIT', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_EDIT', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_EDIT', 'ACCOUNTANT', 'ALLOW');


insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_DELETE', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_DELETE', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_DELETE', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_DELETE', 'ACCOUNTANT', 'ALLOW');


insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_SUMMARY', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_SUMMARY', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_SUMMARY', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_SUMMARY', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_PDF', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_PDF', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_PDF', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SALES_ORDER_PDF', 'ACCOUNTANT', 'ALLOW');


insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_LIST', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_LIST', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_LIST', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_LIST', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_ADD', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_ADD', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_ADD', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_ADD', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_EDIT', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_EDIT', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_EDIT', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_EDIT', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_DELETE', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_DELETE', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_DELETE', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_DELETE', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_SUMMARY', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_SUMMARY', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_SUMMARY', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_OPPORTUNITY_SUMMARY', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_LIST', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_LIST', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_LIST', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_LIST', 'ACCOUNTANT', 'ALLOW');


insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_ADD', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_ADD', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_ADD', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_ADD', 'ACCOUNTANT', 'ALLOW');


insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_EDIT', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_EDIT', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_EDIT', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_EDIT', 'ACCOUNTANT', 'ALLOW');


insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_DELETE', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_DELETE', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_DELETE', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_DELETE', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_SUMMARY', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_SUMMARY', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_SUMMARY', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CONTACT_SUMMARY', 'ACCOUNTANT', 'ALLOW');


insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_LIST', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_LIST', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_LIST', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_LIST', 'ACCOUNTANT', 'ALLOW');


insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_ADD', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_ADD', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_ADD', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_ADD', 'ACCOUNTANT', 'ALLOW');


insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_EDIT', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_EDIT', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_EDIT', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_EDIT', 'ACCOUNTANT', 'ALLOW');


insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_DELETE', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_DELETE', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_DELETE', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_DELETE', 'ACCOUNTANT', 'ALLOW');


insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_SUMMARY', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_SUMMARY', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_SUMMARY', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_EVENT_SUMMARY', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CASE_LIST', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CASE_LIST', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CASE_LIST', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CASE_LIST', 'ACCOUNTANT', 'ALLOW');


insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CASE_ADD', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CASE_ADD', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CASE_ADD', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CASE_ADD', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CASE_EDIT', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CASE_EDIT', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CASE_EDIT', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CASE_EDIT', 'ACCOUNTANT', 'ALLOW');


insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CASE_DELETE', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CASE_DELETE', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CASE_DELETE', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CASE_DELETE', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CASE_SUMMARY', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CASE_SUMMARY', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CASE_SUMMARY', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_CASE_SUMMARY', 'ACCOUNTANT', 'ALLOW');