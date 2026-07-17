--Disable main menu permissions for SUPPLIER
DELETE from "anv".rolepermission where permissioncode in('HRMS_MAIN_MENU','SETTINGS_MAIN_MENU','REPORTING_MAIN_MENU','DOCUMENTS_MAIN_MENU','PAYROLL_MAIN_MENU','WORKSPACE_MAIN_MENU','LOGISTICS_MAIN_MENU') and rolecode = 'SUPPLIER';
DELETE from "anv".rolepermission where permissioncode = 'PM_ISSUE_LIST' and rolecode = 'SUPPLIER';

--- ACCOUNTING_MAIN_MENU
insert into "anv".rolepermission (permissioncode,rolecode,access)
  select 'ACCOUNTING_MAIN_MENU','SUPPLIER', 'ALLOW' from "anv".myuser
  where (select 1 from "anv".rolepermission where permissioncode='ACCOUNTING_MAIN_MENU' and rolecode='SUPPLIER' limit 1) is null limit 1;

--- PM_MAIN_MENU
insert into "anv".rolepermission (permissioncode,rolecode,access)
  select 'PM_MAIN_MENU','SUPPLIER', 'ALLOW' from "anv".myuser
  where (select 1 from "anv".rolepermission where permissioncode='PM_MAIN_MENU' and rolecode='SUPPLIER' limit 1) is null limit 1;


--- CRM_MAIN_MENU
insert into "anv".rolepermission (permissioncode,rolecode,access)
  select 'CRM_MAIN_MENU','SUPPLIER', 'ALLOW' from "anv".myuser
  where (select 1 from "anv".rolepermission where permissioncode='CRM_MAIN_MENU' and rolecode='SUPPLIER' limit 1) is null limit 1;


--- ACCOUNTING_SALES_ORDER_LIST
insert into "anv".rolepermission (permissioncode,rolecode,access)
  select 'ACCOUNTING_SALES_ORDER_LIST','SUPPLIER', 'ALLOW' from "anv".myuser
  where (select 1 from "anv".rolepermission where permissioncode='ACCOUNTING_SALES_ORDER_LIST' and rolecode='SUPPLIER' limit 1) is null limit 1;


--- ACCOUNTING_PURCHASE_ORDER_LIST
insert into "anv".rolepermission (permissioncode,rolecode,access)
  select 'ACCOUNTING_PURCHASE_ORDER_LIST','SUPPLIER', 'ALLOW' from "anv".myuser
  where (select 1 from "anv".rolepermission where permissioncode='ACCOUNTING_PURCHASE_ORDER_LIST' and rolecode='SUPPLIER' limit 1) is null limit 1;

--- ACCOUNTING_PURCHASE_INVOICE_LIST
insert into "anv".rolepermission (permissioncode,rolecode,access)
  select 'ACCOUNTING_PURCHASE_INVOICE_LIST','SUPPLIER', 'ALLOW' from "anv".myuser
  where (select 1 from "anv".rolepermission where permissioncode='ACCOUNTING_PURCHASE_INVOICE_LIST' and rolecode='SUPPLIER' limit 1) is null limit 1;

--- CRM_CASES_LIST
insert into "anv".rolepermission (permissioncode,rolecode,access)
  select 'CRM_CASES_LIST','SUPPLIER', 'ALLOW' from "anv".myuser
  where (select 1 from "anv".rolepermission where permissioncode='CRM_CASES_LIST' and rolecode='SUPPLIER' limit 1) is null limit 1;


insert into "anv".rolepermission (permissioncode,rolecode,access)
  select 'CUSTOMER_SERVICE_TAB','SUPPLIER', 'ALLOW' from "anv".myuser
  where (select 1 from "anv".rolepermission where permissioncode='CUSTOMER_SERVICE_TAB' and rolecode='SUPPLIER' limit 1) is null limit 1;

---------- FOR ZERO SCHEMA --------
DELETE from "0".rolepermission where permissioncode in('HRMS_MAIN_MENU','SETTINGS_MAIN_MENU','REPORTING_MAIN_MENU','DOCUMENTS_MAIN_MENU','PAYROLL_MAIN_MENU','WORKSPACE_MAIN_MENU','LOGISTICS_MAIN_MENU') and rolecode = 'SUPPLIER';
DELETE from "0".rolepermission where permissioncode = 'PM_ISSUE_LIST' and rolecode = 'SUPPLIER';

insert into "0".rolepermission (permissioncode,rolecode,access)
  select 'ACCOUNTING_MAIN_MENU','SUPPLIER', 'ALLOW' from "0".myuser
  where (select 1 from "0".rolepermission where permissioncode='ACCOUNTING_MAIN_MENU' and rolecode='SUPPLIER' limit 1) is null limit 1;

--- PM_MAIN_MENU
insert into "0".rolepermission (permissioncode,rolecode,access)
  select 'PM_MAIN_MENU','SUPPLIER', 'ALLOW' from "0".myuser
  where (select 1 from "0".rolepermission where permissioncode='PM_MAIN_MENU' and rolecode='SUPPLIER' limit 1) is null limit 1;


--- CRM_MAIN_MENU
insert into "0".rolepermission (permissioncode,rolecode,access)
  select 'CRM_MAIN_MENU','SUPPLIER', 'ALLOW' from "0".myuser
  where (select 1 from "0".rolepermission where permissioncode='CRM_MAIN_MENU' and rolecode='SUPPLIER' limit 1) is null limit 1;


--- ACCOUNTING_SALES_ORDER_LIST
insert into "0".rolepermission (permissioncode,rolecode,access)
  select 'ACCOUNTING_SALES_ORDER_LIST','SUPPLIER', 'ALLOW' from "0".myuser
  where (select 1 from "0".rolepermission where permissioncode='ACCOUNTING_SALES_ORDER_LIST' and rolecode='SUPPLIER' limit 1) is null limit 1;


--- ACCOUNTING_PURCHASE_ORDER_LIST
insert into "0".rolepermission (permissioncode,rolecode,access)
  select 'ACCOUNTING_PURCHASE_ORDER_LIST','SUPPLIER', 'ALLOW' from "0".myuser
  where (select 1 from "0".rolepermission where permissioncode='ACCOUNTING_PURCHASE_ORDER_LIST' and rolecode='SUPPLIER' limit 1) is null limit 1;

--- ACCOUNTING_PURCHASE_INVOICE_LIST
insert into "0".rolepermission (permissioncode,rolecode,access)
  select 'ACCOUNTING_PURCHASE_INVOICE_LIST','SUPPLIER', 'ALLOW' from "0".myuser
  where (select 1 from "0".rolepermission where permissioncode='ACCOUNTING_PURCHASE_INVOICE_LIST' and rolecode='SUPPLIER' limit 1) is null limit 1;


--- CRM_CASES_LIST
insert into "0".rolepermission (permissioncode,rolecode,access)
  select 'CRM_CASES_LIST','SUPPLIER', 'ALLOW' from "0".myuser
  where (select 1 from "0".rolepermission where permissioncode='CRM_CASES_LIST' and rolecode='SUPPLIER' limit 1) is null limit 1;

---CUSTOMER_SERVICE_TAB
insert into "0".rolepermission (permissioncode,rolecode,access)
  select 'CUSTOMER_SERVICE_TAB','SUPPLIER', 'ALLOW' from "0".myuser
  where (select 1 from "0".rolepermission where permissioncode='CUSTOMER_SERVICE_TAB' and rolecode='SUPPLIER' limit 1) is null limit 1;









