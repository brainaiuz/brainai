---SALES_QUOTE_SEE_OWN
delete from "0".rolepermission where permissioncode='SALES_QUOTE_SEE_OWN';
delete from "0".permission_context where permissioncode='SALES_QUOTE_SEE_OWN';

insert into "0".permission_context (permissioncode,contextcode) values ('SALES_QUOTE_SEE_OWN','ACCOUNTING');
insert into "0".permission_context (permissioncode,contextcode) values ('SALES_QUOTE_SEE_OWN','CRM');

insert into "0".rolepermission (permissioncode,access,rolecode) values ('SALES_QUOTE_SEE_OWN','ALLOW','ADMIN');



delete from "0".rolepermission where permissioncode='SALES_ORDER_SEE_OWN';
delete from "0".permission_context where permissioncode='SALES_ORDER_SEE_OWN';

insert into "0".permission_context (permissioncode,contextcode) values ('SALES_ORDER_SEE_OWN','ACCOUNTING');
insert into "0".permission_context (permissioncode,contextcode) values ('SALES_ORDER_SEE_OWN','CRM');

insert into "0".rolepermission (permissioncode,access,rolecode) values ('SALES_ORDER_SEE_OWN','ALLOW','ADMIN');




delete from "0".rolepermission where permissioncode = 'SALES_INVOICE_SEE_OWN';
delete from "0".permission_context where permissioncode='SALES_INVOICE_SEE_OWN';

insert into "0".permission_context (permissioncode,contextcode) values ('SALES_INVOICE_SEE_OWN','ACCOUNTING');
insert into "0".permission_context (permissioncode,contextcode) values ('SALES_INVOICE_SEE_OWN','CRM');

insert into "0".rolepermission (permissioncode,access,rolecode) values ('SALES_INVOICE_SEE_OWN','ALLOW','ADMIN');



delete from "0".rolepermission where permissioncode = 'RFQ_SEE_OWN';
delete from "0".permission_context where permissioncode='RFQ_SEE_OWN';

insert into "0".permission_context (permissioncode,contextcode) values ('RFQ_SEE_OWN','ACCOUNTING');
insert into "0".permission_context (permissioncode,contextcode) values ('RFQ_SEE_OWN','CRM');

insert into "0".rolepermission (permissioncode,access,rolecode) values ('RFQ_SEE_OWN','ALLOW','ADMIN');


delete from "0".rolepermission where permissioncode = 'RFP_SEE_OWN';
delete from "0".permission_context where permissioncode='RFP_SEE_OWN';

insert into "0".permission_context (permissioncode,contextcode) values ('RFP_SEE_OWN','ACCOUNTING');
insert into "0".permission_context (permissioncode,contextcode) values ('RFP_SEE_OWN','CRM');

insert into "0".rolepermission (permissioncode,access,rolecode) values ('RFP_SEE_OWN','ALLOW','ADMIN');



delete from "0".rolepermission where permissioncode = 'PURCHASE_ORDER_SEE_OWN';
delete from "0".permission_context where permissioncode='PURCHASE_ORDER_SEE_OWN';

insert into "0".permission_context (permissioncode,contextcode) values ('PURCHASE_ORDER_SEE_OWN','ACCOUNTING');
insert into "0".permission_context (permissioncode,contextcode) values ('PURCHASE_ORDER_SEE_OWN','CRM');

insert into "0".rolepermission (permissioncode,access,rolecode) values ('PURCHASE_ORDER_SEE_OWN','ALLOW','ADMIN');




delete from "0".rolepermission where permissioncode = 'PURCHASE_INVOICE_SEE_OWN';
delete from "0".permission_context where permissioncode='PURCHASE_INVOICE_SEE_OWN';

insert into "0".permission_context (permissioncode,contextcode) values ('PURCHASE_INVOICE_SEE_OWN','ACCOUNTING');
insert into "0".permission_context (permissioncode,contextcode) values ('PURCHASE_INVOICE_SEE_OWN','CRM');

insert into "0".rolepermission (permissioncode,access,rolecode) values ('PURCHASE_INVOICE_SEE_OWN','ALLOW','ADMIN');




delete from "0".rolepermission where permissioncode = 'EXPENSE_SEE_OWN';
delete from "0".permission_context where permissioncode='EXPENSE_SEE_OWN';

insert into "0".permission_context (permissioncode,contextcode) values ('EXPENSE_SEE_OWN','ACCOUNTING');
insert into "0".permission_context (permissioncode,contextcode) values ('EXPENSE_SEE_OWN','CRM');

insert into "0".rolepermission (permissioncode,access,rolecode) values ('EXPENSE_SEE_OWN','ALLOW','ADMIN');
