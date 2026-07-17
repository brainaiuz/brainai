update reportTemplateCategory set code = 'CUSTOM' where name = 'Custom';
update reportTemplateCategory set code = 'PRODUCT' where name = 'Sale Invoice Product';
update reportTemplateCategory set code = 'HRMS' where name in ('HRMS', 'Hrms');
update reportTemplateCategory set code = 'CRM' where name = 'CRM';
update reportTemplateCategory set code = 'ACCOUNTING' where name = 'Accounting & Finance';
update reportTemplateCategory set code = 'PAYROLL' where name = 'Payroll';
update reportTemplateCategory set code = 'PM' where name = 'Project Management';