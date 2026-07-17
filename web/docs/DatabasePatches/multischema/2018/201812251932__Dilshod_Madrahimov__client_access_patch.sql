--Disable main menu permissions for CLIENT
DELETE from "anv".rolepermission where permissioncode in('HRMS_MAIN_MENU','SETTINGS_MAIN_MENU','REPORTING_MAIN_MENU','DOCUMENTS_MAIN_MENU','PAYROLL_MAIN_MENU','WORKSPACE_MAIN_MENU','LOGISTICS_MAIN_MENU') and rolecode = 'CLIENT';
DELETE from "anv".rolepermission where permissioncode = 'PM_ISSUE_LIST' and rolecode = 'CLIENT';

--- ACCOUNTING_MAIN_MENU
insert into "anv".rolepermission (permissioncode,rolecode,access)
  select 'ACCOUNTING_MAIN_MENU','CLIENT', 'ALLOW' from "anv".myuser
  where (select 1 from "anv".rolepermission where permissioncode='ACCOUNTING_MAIN_MENU' and rolecode='CLIENT' limit 1) is null limit 1;


--- PM_MAIN_MENU
insert into "anv".rolepermission (permissioncode,rolecode,access)
  select 'PM_MAIN_MENU','CLIENT', 'ALLOW' from "anv".myuser
  where (select 1 from "anv".rolepermission where permissioncode='PM_MAIN_MENU' and rolecode='CLIENT' limit 1) is null limit 1;


--- CRM_MAIN_MENU
insert into "anv".rolepermission (permissioncode,rolecode,access)
  select 'CRM_MAIN_MENU','CLIENT', 'ALLOW' from "anv".myuser
  where (select 1 from "anv".rolepermission where permissioncode='CRM_MAIN_MENU' and rolecode='CLIENT' limit 1) is null limit 1;


--- ACCOUNTING_SALES_QUOTE_LIST
insert into "anv".rolepermission (permissioncode,rolecode,access)
  select 'ACCOUNTING_SALES_QUOTE_LIST','CLIENT', 'ALLOW' from "anv".myuser
  where (select 1 from "anv".rolepermission where permissioncode='ACCOUNTING_SALES_QUOTE_LIST' and rolecode='CLIENT' limit 1) is null limit 1;


--- ACCOUNTING_SALES_INVOICE_LIST
insert into "anv".rolepermission (permissioncode,rolecode,access)
  select 'ACCOUNTING_SALES_INVOICE_LIST','CLIENT', 'ALLOW' from "anv".myuser
  where (select 1 from "anv".rolepermission where permissioncode='ACCOUNTING_SALES_INVOICE_LIST' and rolecode='CLIENT' limit 1) is null limit 1;

--- CRM_CASES_LIST
insert into "anv".rolepermission (permissioncode,rolecode,access)
  select 'CRM_CASES_LIST','CLIENT', 'ALLOW' from "anv".myuser
  where (select 1 from "anv".rolepermission where permissioncode='CRM_CASES_LIST' and rolecode='CLIENT' limit 1) is null limit 1;


--CUSTOMER_SERVICE_TAB
insert into "anv".rolepermission (permissioncode,rolecode,access)
  select 'CUSTOMER_SERVICE_TAB','CLIENT', 'ALLOW' from "anv".myuser
  where (select 1 from "anv".rolepermission where permissioncode='CUSTOMER_SERVICE_TAB' and rolecode='CLIENT' limit 1) is null limit 1;


---------- FOR ZERO SCHEMA --------------

DELETE from "0".rolepermission where permissioncode in('HRMS_MAIN_MENU','SETTINGS_MAIN_MENU','REPORTING_MAIN_MENU','DOCUMENTS_MAIN_MENU','PAYROLL_MAIN_MENU','WORKSPACE_MAIN_MENU','LOGISTICS_MAIN_MENU') and rolecode = 'CLIENT';
DELETE from "0".rolepermission where permissioncode = 'PM_ISSUE_LIST' and rolecode = 'CLIENT';

--- ACCOUNTING_MAIN_MENU
insert into "0".rolepermission (permissioncode,rolecode,access)
  select 'ACCOUNTING_MAIN_MENU','CLIENT', 'ALLOW' from "0".myuser
  where (select 1 from "0".rolepermission where permissioncode='ACCOUNTING_MAIN_MENU' and rolecode='CLIENT' limit 1) is null limit 1;


--- PM_MAIN_MENU
insert into "0".rolepermission (permissioncode,rolecode,access)
  select 'PM_MAIN_MENU','CLIENT', 'ALLOW' from "0".myuser
  where (select 1 from "0".rolepermission where permissioncode='PM_MAIN_MENU' and rolecode='CLIENT' limit 1) is null limit 1;


--- CRM_MAIN_MENU
insert into "0".rolepermission (permissioncode,rolecode,access)
  select 'CRM_MAIN_MENU','CLIENT', 'ALLOW' from "0".myuser
  where (select 1 from "0".rolepermission where permissioncode='CRM_MAIN_MENU' and rolecode='CLIENT' limit 1) is null limit 1;


--- ACCOUNTING_SALES_QUOTE_LIST
insert into "0".rolepermission (permissioncode,rolecode,access)
  select 'ACCOUNTING_SALES_QUOTE_LIST','CLIENT', 'ALLOW' from "0".myuser
  where (select 1 from "0".rolepermission where permissioncode='ACCOUNTING_SALES_QUOTE_LIST' and rolecode='CLIENT' limit 1) is null limit 1;


--- ACCOUNTING_SALES_INVOICE_LIST
insert into "0".rolepermission (permissioncode,rolecode,access)
  select 'ACCOUNTING_SALES_INVOICE_LIST','CLIENT', 'ALLOW' from "0".myuser
  where (select 1 from "0".rolepermission where permissioncode='ACCOUNTING_SALES_INVOICE_LIST' and rolecode='CLIENT' limit 1) is null limit 1;



--- CRM_CASES_LIST
insert into "0".rolepermission (permissioncode,rolecode,access)
  select 'CRM_CASES_LIST','CLIENT', 'ALLOW' from "0".myuser
  where (select 1 from "0".rolepermission where permissioncode='CRM_CASES_LIST' and rolecode='CLIENT' limit 1) is null limit 1;


--CUSTOMER_SERVICE_TAB
insert into "0".rolepermission (permissioncode,rolecode,access)
  select 'CUSTOMER_SERVICE_TAB','CLIENT', 'ALLOW' from "0".myuser
  where (select 1 from "0".rolepermission where permissioncode='CUSTOMER_SERVICE_TAB' and rolecode='CLIENT' limit 1) is null limit 1;


















