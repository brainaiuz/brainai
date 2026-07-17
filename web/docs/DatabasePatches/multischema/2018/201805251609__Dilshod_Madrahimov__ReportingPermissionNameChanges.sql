update permission set name='Accounts' where lower(name) like lower('%Accounting & Finance%') and modulecode='REPORTING_SYSTEM';
update permission set name='Humans' where lower(name) like lower('%Hrms%') and modulecode='REPORTING_SYSTEM';
update permission set name='Sales' where lower(name) like lower('%Crm%') and modulecode='REPORTING_SYSTEM';
update permission set name='Projects' where lower(name) like lower('%Project Management%') and modulecode='REPORTING_SYSTEM';