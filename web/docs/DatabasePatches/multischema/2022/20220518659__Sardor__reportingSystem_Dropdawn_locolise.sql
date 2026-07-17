-- Update codes in folders table
update "anv".folders
set code ='TAXRETURNSREPORTS'
where name = 'Tax Returns Reports';
update "anv".folders
set code ='DETAILREPORTS'
where name = 'Detail Reports';
update "anv".folders
set code ='FOCUSONSUPPLIERS'
where name = 'Focus on Suppliers';
update "anv".folders
set code ='FOCUSONCUSTOMERS'
where name = 'Focus on Customers';
update "anv".folders
set code ='FINANCIALSTATEMENTS'
where name = 'Financial Statements';
update "anv".folders
set code ='SALESREPORTS'
where name = 'Sales Reports';
update "anv".folders
set code ='OTHERREPORTSACCOUNTING'
where name = 'Other Reports (Accounting)';
update "anv".folders
set code ='OTHERREPORTSACCOUNTING'
where name = 'Other Reports (Accounting)';
update "anv".folders
set code ='CUSTOMREPORTS'
where name = 'Custom Reports';
update "anv".folders
set code ='OTHERREPORTSCRM'
where name = 'Other Reports (CRM)';
update "anv".folders
set code ='ACTIVITYREPORTS'
where name = 'Activity Reports';
update "anv".folders
set code ='OTHERREPORTS'
where name = 'Other Reports';
update "anv".folders
set code ='OTHERREPORTS'
where name = 'Other Reports';
update "anv".folders
set code ='CASEREPORTS'
where name = 'Case Reports';
update "anv".folders
set code ='OPPORTUNITYREPORTS'
where name = 'Opportunity Reports';
update "anv".folders
set code ='LEADREPORTS'
where name = 'Lead Reports';
update "anv".folders
set code ='RECRUITMENT'
where name = 'Recruitment';
update "anv".folders
set code ='LEAVEREQUESTS'
where name = 'Leave Requests';
update "anv".folders
set code ='EMPLOYEEDETAILS'
where name = 'Employee Details';
update "anv".folders
set code ='OTHERREPORTSHRMS'
where name = 'Other Reports (HRMS)';
update "anv".folders
set code ='EMPLOYEEINFORMATION'
where name = 'Employee information';
update "anv".folders
set code ='ATTENDANCYREPORTS'
where name = 'Attendancy Reports';
update "anv".folders
set code ='RECRUITMENTREPORTS'
where name = 'Recruitment Reports';
update "anv".folders
set code ='LEAVEREQUESTREPORTS'
where name = 'Leave Request Reports';
update "anv".folders
set code ='OTHERREPORTSPM'
where name = 'Other Reports (PM)';
update "anv".folders
set code ='TIMESHEETREPORTS'
where name = 'Timesheet Reports';
update "anv".folders
set code ='TASKREPORTS'
where name = 'Task Reports';
update "anv".folders
set code ='PROJECTREPORTS'
where name = 'Project Reports';
-- add items to reference table
-- folders name and folders code
INSERT INTO "anv".reference(code, name)
VALUES ('TAXRETURNSREPORTS', 'Tax Returns Reports');
INSERT INTO "anv".reference(code, name)
VALUES ('DETAILREPORTS', 'Detail Reports');
INSERT INTO "anv".reference(code, name)
VALUES ('FOCUSONSUPPLIERS', 'Focus on Suppliers');
INSERT INTO "anv".reference(code, name)
VALUES ('FOCUSONCUSTOMERS', 'Focus on Customers');
INSERT INTO "anv".reference(code, name)
VALUES ('FINANCIALSTATEMENTS', 'Financial Statements');
INSERT INTO "anv".reference(code, name)
VALUES ('SALESREPORTS', 'Sales Reports');
INSERT INTO "anv".reference(code, name)
VALUES ('OTHERREPORTSACCOUNTING', 'Other Reports (Accounting)');
INSERT INTO "anv".reference(code, name)
VALUES ('CUSTOMREPORTS', 'Custom Reports');
INSERT INTO "anv".reference(code, name)
VALUES ('OTHERREPORTSCRM', 'Other Reports (CRM)');
INSERT INTO "anv".reference(code, name)
VALUES ('ACTIVITYREPORTS', 'Activity Reports');
INSERT INTO "anv".reference(code, name)
VALUES ('OTHERREPORTS', 'Other Reports');
INSERT INTO "anv".reference(code, name)
VALUES ('CASEREPORTS', 'Case Reports');
INSERT INTO "anv".reference(code, name)
VALUES ('OPPORTUNITYREPORTS', 'Opportunity Reports');
INSERT INTO "anv".reference(code, name)
VALUES ('LEADREPORTS', 'Lead Reports');
INSERT INTO "anv".reference(code, name)
VALUES ('RECRUITMENT', 'Recruitment');
INSERT INTO "anv".reference(code, name)
VALUES ('LEAVEREQUESTS', 'Leave Requests');
INSERT INTO "anv".reference(code, name)
VALUES ('EMPLOYEEDETAILS', 'Employee Details');
INSERT INTO "anv".reference(code, name)
VALUES ('OTHERREPORTSHRMS', 'Other Reports (HRMS)');
INSERT INTO "anv".reference(code, name)
VALUES ('EMPLOYEEINFORMATION', 'Employee information');
INSERT INTO "anv".reference(code, name)
VALUES ('ATTENDANCYREPORTS', 'Attendancy Reports');
INSERT INTO "anv".reference(code, name)
VALUES ('RECRUITMENTREPORTS', 'Recruitment Reports');
INSERT INTO "anv".reference(code, name)
VALUES ('LEAVEREQUESTREPORTS', 'Leave Request Reports');
INSERT INTO "anv".reference(code, name)
VALUES ('OTHERREPORTSPM', 'Other Reports (PM)');
INSERT INTO "anv".reference(code, name)
VALUES ('TIMESHEETREPORTS', 'Timesheet Reports');
INSERT INTO "anv".reference(code, name)
VALUES ('TASKREPORTS', 'Task Reports');
INSERT INTO "anv".reference(code, name)
VALUES ('PROJECTREPORTS', 'Project Reports');
--reference_locale add english name and uzbek name
INSERT INTO "anv".reference_locale(english, uzbek)
VALUES ('Tax Returns Reports', 'Soliq deklaratsiyasi hisobotlari');
INSERT INTO "anv".reference_locale(english, uzbek)
VALUES ('Detail Reports', 'Tafsilot hisobotlari');
INSERT INTO "anv".reference_locale(english, uzbek)
VALUES ('Focus on Suppliers', 'Ta`monotchilarga e`tibor qaratish');
INSERT INTO "anv".reference_locale(english, uzbek)
VALUES ('Focus on Customers', 'Mijozlarga e`tibor qaratish');
INSERT INTO "anv".reference_locale(english, uzbek)
VALUES ('Financial Statements', 'Moliyaviy bayonnomalar');
INSERT INTO "anv".reference_locale(english, uzbek)
VALUES ('Sales Reports', 'Savdo hisobotlari');
INSERT INTO "anv".reference_locale(english, uzbek)
VALUES ('Other Reports (Accounting)', 'Boshqa hisobotlar (Buxgalteriya)');
INSERT INTO "anv".reference_locale(english, uzbek)
VALUES ('Other Reports (Accounting)', 'Boshqa hisobotlar (Buxgalteriya)');
INSERT INTO "anv".reference_locale(english, uzbek)
VALUES ('Custom Reports', 'Maxsus hisobotlar');
INSERT INTO "anv".reference_locale(english, uzbek)
VALUES ('Other Reports (CRM)', 'Boshqa hisobotlar (MMB)');
INSERT INTO "anv".reference_locale(english, uzbek)
VALUES ('Activity Reports', 'Faoliyat hisobotlari');
INSERT INTO "anv".reference_locale(english, uzbek)
VALUES ('Other Reports', 'Boshqa hisobotlar');
INSERT INTO "anv".reference_locale(english, uzbek)
VALUES ('Other Reports', 'Boshqa hisobotlar');
INSERT INTO "anv".reference_locale(english, uzbek)
VALUES ('Case Reports', 'Murojaat hisobotlari');
INSERT INTO "anv".reference_locale(english, uzbek)
VALUES ('Opportunity Reports', 'Imkoniyat hisobotlari');
INSERT INTO "anv".reference_locale(english, uzbek)
VALUES ('Lead Reports', 'Ehtimoliy mijoz hisobotlari');
INSERT INTO "anv".reference_locale(english, uzbek)
VALUES ('Recruitment', 'Ishga olish');
INSERT INTO "anv".reference_locale(english, uzbek)
VALUES ('Leave Requests', 'Izn so`rovlari');
INSERT INTO "anv".reference_locale(english, uzbek)
VALUES ('Employee Details', 'Xodim tafsilotlari');
INSERT INTO "anv".reference_locale(english, uzbek)
VALUES ('Other Reports (HRMS)', 'Boshqa hisobotlar (KB)');
INSERT INTO "anv".reference_locale(english, uzbek)
VALUES ('Employee information', 'Xodim haqida ma`lumot');
INSERT INTO "anv".reference_locale(english, uzbek)
VALUES ('Attendancy Reports', 'Davomat hisobotlari');
INSERT INTO "anv".reference_locale(english, uzbek)
VALUES ('Recruitment Reports', 'Ishga olish hisobotlari');
INSERT INTO "anv".reference_locale(english, uzbek)
VALUES ('Leave Request Reports', 'Izn so`rovi hisobotlari');
INSERT INTO "anv".reference_locale(english, uzbek)
VALUES ('Other Reports (PM)', 'Boshqa hisobotlar (LM)');
INSERT INTO "anv".reference_locale(english, uzbek)
VALUES ('Timesheet Reports', 'Tabel hisobotlari');
INSERT INTO "anv".reference_locale(english, uzbek)
VALUES ('Task Reports', 'Topshiriqlar hisoboti');
INSERT INTO "anv".reference_locale(english, uzbek)
VALUES ('Project Reports', 'Loyiha hisobotlari');
delete
from "anv".reference_locale english
WHERE id IN
      (SELECT id
       FROM (SELECT te.id,
                    ROW_NUMBER() OVER( PARTITION BY te.english,te.uzbek
        ORDER BY  te.id ) AS row_num
             from "anv".reference_locale te) t
       WHERE t.row_num > 1);


--  Updating localeid in reference from reference_locale
UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Tax Returns Reports' and uzbek = 'Soliq deklaratsiyasi hisobotlari')
WHERE code = 'TAXRETURNSREPORTS';

UPDATE "anv".reference
SET localeid=(select id from "anv".reference_locale WHERE english = 'Detail Reports' and uzbek = 'Tafsilot hisobotlari')
WHERE code = 'DETAILREPORTS';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Focus on Suppliers' and uzbek = 'Ta`monotchilarga e`tibor qaratish')
WHERE code = 'FOCUSONSUPPLIERS';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Focus on Customers' and uzbek = 'Mijozlarga e`tibor qaratish')
WHERE code = 'FOCUSONCUSTOMERS';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Financial Statements' and uzbek = 'Moliyaviy bayonnomalar')
WHERE code = 'FINANCIALSTATEMENTS';

UPDATE "anv".reference
SET localeid=(select id from "anv".reference_locale WHERE english = 'Sales Reports' and uzbek = 'Savdo hisobotlari')
WHERE code = 'SALESREPORTS';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Other Reports (Accounting)'
                and uzbek = 'Boshqa hisobotlar (Buxgalteriya)')
WHERE code = 'OTHERREPORTSACCOUNTING';


UPDATE "anv".reference
SET localeid=(select id from "anv".reference_locale WHERE english = 'Custom Reports' and uzbek = 'Maxsus hisobotlar')
WHERE code = 'CUSTOMREPORTS';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Other Reports (CRM)' and uzbek = 'Boshqa hisobotlar (MMB)')
WHERE code = 'OTHERREPORTSCRM';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Activity Reports' and uzbek = 'Faoliyat hisobotlari')
WHERE code = 'ACTIVITYREPORTS';

UPDATE "anv".reference
SET localeid=(select id from "anv".reference_locale WHERE english = 'Other Reports' and uzbek = 'Boshqa hisobotlar')
WHERE code = 'OTHERREPORTS';


UPDATE "anv".reference
SET localeid=(select id from "anv".reference_locale WHERE english = 'Case Reports' and uzbek = 'Murojaat hisobotlari')
WHERE code = 'CASEREPORTS';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Opportunity Reports' and uzbek = 'Imkoniyat hisobotlari')
WHERE code = 'OPPORTUNITYREPORTS';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Lead Reports' and uzbek = 'Ehtimoliy mijoz hisobotlari')
WHERE code = 'LEADREPORTS';

UPDATE "anv".reference
SET localeid=(select id from "anv".reference_locale WHERE english = 'Recruitment' and uzbek = 'Ishga olish')
WHERE code = 'RECRUITMENT';

UPDATE "anv".reference
SET localeid=(select id from "anv".reference_locale WHERE english = 'Leave Requests' and uzbek = 'Izn so`rovlari')
WHERE code = 'LEAVEREQUESTS';

UPDATE "anv".reference
SET localeid=(select id from "anv".reference_locale WHERE english = 'Employee Details' and uzbek = 'Xodim tafsilotlari')
WHERE code = 'EMPLOYEEDETAILS';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Other Reports (HRMS)' and uzbek = 'Boshqa hisobotlar (KB)')
WHERE code = 'OTHERREPORTSHRMS';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Employee information' and uzbek = 'Xodim haqida ma`lumot')
WHERE code = 'EMPLOYEEINFORMATION';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Attendancy Reports' and uzbek = 'Davomat hisobotlari')
WHERE code = 'ATTENDANCYREPORTS';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Recruitment Reports' and uzbek = 'Ishga olish hisobotlari')
WHERE code = 'RECRUITMENTREPORTS';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Leave Request Reports' and uzbek = 'Izn so`rovi hisobotlari')
WHERE code = 'LEAVEREQUESTREPORTS';

UPDATE "anv".reference
SET localeid=(select id
              from "anv".reference_locale
              WHERE english = 'Other Reports (PM)' and uzbek = 'Boshqa hisobotlar (LM)')
WHERE code = 'OTHERREPORTSPM';

UPDATE "anv".reference
SET localeid=(select id from "anv".reference_locale WHERE english = 'Timesheet Reports' and uzbek = 'Tabel hisobotlari')
WHERE code = 'TIMESHEETREPORTS';

UPDATE "anv".reference
SET localeid=(select id from "anv".reference_locale WHERE english = 'Task Reports' and uzbek = 'Topshiriqlar hisoboti')
WHERE code = 'TASKREPORTS';

UPDATE "anv".reference
SET localeid=(select id from "anv".reference_locale WHERE english = 'Project Reports' and uzbek = 'Loyiha hisobotlari')
WHERE code = 'PROJECTREPORTS';

