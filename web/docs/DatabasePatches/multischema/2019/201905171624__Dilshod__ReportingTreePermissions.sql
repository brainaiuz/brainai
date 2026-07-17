
---Set report main menu as parent to permission categories
update "0".reportingpermission set parent =(select id from "0".reportingpermission where code='REPORTING_MAIN_MENU') where code in(
'REPORTING_TEMPLATE_CATEGORY_ACCOUNTING',
'REPORTING_TEMPLATE_CATEGORY_CRM',
'REPORTING_TEMPLATE_CATEGORY_PM',
'REPORTING_TEMPLATE_CATEGORY_HRMS',
'REPORTING_TEMPLATE_CATEGORY_PAYROLL',
'REPORTING_TEMPLATE_CATEGORY_CUSTOM',
'REPORTING_TEMPLATE_CATEGORY_SYSTEM');

update "0".reportingpermission set sorder =0,name ='Accounts Category' where code = 'REPORTING_TEMPLATE_CATEGORY_ACCOUNTING';
update "0".reportingpermission set sorder =1,name ='Sales Category' where code = 'REPORTING_TEMPLATE_CATEGORY_CRM';
update "0".reportingpermission set sorder =2,name ='Humans Category' where code = 'REPORTING_TEMPLATE_CATEGORY_HRMS';
update "0".reportingpermission set sorder =3,name ='Projects Category' where code = 'REPORTING_TEMPLATE_CATEGORY_PM';
update "0".reportingpermission set sorder =4,name ='Payroll Category' where code = 'REPORTING_TEMPLATE_CATEGORY_PAYROLL';
update "0".reportingpermission set sorder =5,name ='Custom Category' where code = 'REPORTING_TEMPLATE_CATEGORY_CUSTOM';
update "0".reportingpermission set sorder =6,name ='System Category' where code = 'REPORTING_TEMPLATE_CATEGORY_SYSTEM';



---Set report main menu as parent to permission categories

update "anv".reportingpermission set parent =(select id from "anv".reportingpermission where code='REPORTING_MAIN_MENU') where code in(
'REPORTING_TEMPLATE_CATEGORY_ACCOUNTING',
'REPORTING_TEMPLATE_CATEGORY_CRM',
'REPORTING_TEMPLATE_CATEGORY_PM',
'REPORTING_TEMPLATE_CATEGORY_HRMS',
'REPORTING_TEMPLATE_CATEGORY_PAYROLL',
'REPORTING_TEMPLATE_CATEGORY_CUSTOM',
'REPORTING_TEMPLATE_CATEGORY_SYSTEM');

update "anv".reportingpermission set sorder =0,name ='Accounts Category' where code = 'REPORTING_TEMPLATE_CATEGORY_ACCOUNTING';
update "anv".reportingpermission set sorder =1,name ='Sales Category' where code = 'REPORTING_TEMPLATE_CATEGORY_CRM';
update "anv".reportingpermission set sorder =2,name ='Humans Category' where code = 'REPORTING_TEMPLATE_CATEGORY_HRMS';
update "anv".reportingpermission set sorder =3,name ='Projects Category' where code = 'REPORTING_TEMPLATE_CATEGORY_PM';
update "anv".reportingpermission set sorder =4,name ='Payroll Category' where code = 'REPORTING_TEMPLATE_CATEGORY_PAYROLL';
update "anv".reportingpermission set sorder =5,name ='Custom Category' where code = 'REPORTING_TEMPLATE_CATEGORY_CUSTOM';
update "anv".reportingpermission set sorder =6,name ='System Category' where code = 'REPORTING_TEMPLATE_CATEGORY_SYSTEM';



------- Update default template name. If default template name doest not contain Default, add Default like Account Transactions (Default)

---ACCOUNTING
update reporttemplate  set name = name||' (Default)' where (iscustom is null or iscustom = false) and name not ilike ('%Default%') and code in('ACCOUNT TRANSACTIONS',
                                                                                                                                               'CHARTOFACCOUNTSDEFAULT',
                                                                                                                                               'CONSIGNMENTDEFAULT',
                                                                                                                                               'CUSTOMER CENTER DEFAULT',
                                                                                                                                               'CUSTOMER INVOICE REPORT',
                                                                                                                                               'EXPENSE REPORTS',
                                                                                                                                               'FIXED ASSET DEFAULT',
                                                                                                                                               'PRODUCTS DEFAULT',
                                                                                                                                               'PRODUCTUSEDDETAILSBYFAXRIDDIN',
                                                                                                                                               'PURCHASE INVOICE DEFAULT',
                                                                                                                                               'PURCHASE ORDER DEFAULT',
                                                                                                                                               'SALES BY ITEM',
                                                                                                                                               'SALES INVOICE DEFAULT',
                                                                                                                                               'SALES QUOTES DEFAULT',
                                                                                                                                               'STOCKTRANSFERREPORT',
                                                                                                                                               'STOCK_VALUATION_REPORT',
                                                                                                                                               'SUPPLIER CENTER DEFAULT',
                                                                                                                                               'SUPPLIER INVOICE REPORT',
                                                                                                                                               'INVOICES AMOUNT');

---CRM
update reporttemplate  set name = name||' (Default)' where (iscustom is null or iscustom = false) and name not ilike ('%Default%') and code in('CRM ACCOUNTS',
                                                                                                                                               'CRM ACTIVITIES',
                                                                                                                                               'CRM CAMPAIGN',
                                                                                                                                               'CRM CONTACTS',
                                                                                                                                               'CRM LEADS',
                                                                                                                                               'CRM OPPORTUNITY CUSTOM');


---PM
update reporttemplate  set name = name||' (Default)' where (iscustom is null or iscustom = false) and name not ilike ('%Default%') and code in('PROJECT LIST',
                                                                                                                                               'TASK LIST',
                                                                                                                                               'TIMESHEETREPORT');


---HRMS
update reporttemplate  set name = name||' (Default)' where (iscustom is null or iscustom = false) and name not ilike ('%Default%') and code in('COMPETENCIES',
                                                                                                                                               'EMPLOYEEPAYROLLINFORMATION',
                                                                                                                                               'EMPLOYEE PROFILE CUSTOM',
                                                                                                                                               'GOALS',
                                                                                                                                               'WEEKLY LEAVE REPORT',
                                                                                                                                               'PAYSLIPSBYPROJECT');

---PM
update reporttemplate  set name = name||' (Default)' where (iscustom is null or iscustom = false) and name not ilike ('%Default%') and code in('EMPLOYEEADDITIONALPAYMENTS');




--- Rename Old module names to new names
UPDATE reporttemplate SET name = REPLACE(name, 'HRMS','Humans');
UPDATE reporttemplate SET name = REPLACE(name, 'Hrms','Humans');
UPDATE reporttemplate SET name = REPLACE(name, 'hrms','Humans');

UPDATE reporttemplate SET name = REPLACE(name, 'ACCOUNTING','Accounts');
UPDATE reporttemplate SET name = REPLACE(name, 'Accounting','Accounts');
UPDATE reporttemplate SET name = REPLACE(name, 'accounting','Accounts');
UPDATE reporttemplate SET name = REPLACE(name, 'Accounts & Finance','Accounts');

UPDATE reporttemplate SET name = REPLACE(name, 'CRM','Sales');
UPDATE reporttemplate SET name = REPLACE(name, 'crm','Sales');
UPDATE reporttemplate SET name = REPLACE(name, 'Crm','Sales');


UPDATE reporttemplate SET name = REPLACE(name, 'PM','Projects');
UPDATE reporttemplate SET name = REPLACE(name, 'pm','Projects');
UPDATE reporttemplate SET name = REPLACE(name, 'Project Management','Projects');
UPDATE reporttemplate SET name = REPLACE(name, 'project management','Projects');



----- After changing report templates name, change those templates permissions name also

update "anv".reportingpermission p set name = (
       select rt.name from reporttemplate rt
            inner join "anv".reportingpermission rp on 'REPORTING_TEMPLATE_'||rt.code=rp.code or 'REPORTING_TEMPLATE_'||rt.code||'_'||replace('"anv"','"','') = rp.code
            where p.code = rp.code
     ) where p.code in(select rp.code from reporttemplate rt join "anv".reportingpermission rp on 'REPORTING_TEMPLATE_'||rt.code=rp.code or 'REPORTING_TEMPLATE_'||rt.code||'_'||replace('"anv"','"','') = rp.code);



update "0".reportingpermission p set name = (
       select rt.name from reporttemplate rt
            inner join "0".reportingpermission rp on 'REPORTING_TEMPLATE_'||rt.code=rp.code or 'REPORTING_TEMPLATE_'||rt.code||'_'||replace('"0"','"','') = rp.code
            where p.code = rp.code
     ) where p.code in(select rp.code from reporttemplate rt join "0".reportingpermission rp on 'REPORTING_TEMPLATE_'||rt.code=rp.code or 'REPORTING_TEMPLATE_'||rt.code||'_'||replace('"0"','"','') = rp.code);

