--  my update type for backups employee

delete
from myupdatetype
where code = 'BACKUPS_EMPLOYEE_ADD';
delete
from myupdatetype
where code = 'BACKUPS_EMPLOYEE_EDIT';
delete
from myupdatetype
where code = 'BACKUPS_EMPLOYEE_DELETE';
delete
from myupdatetype
where code = 'BACKUPS_EMPLOYEE_SUBMITTED_TO_MANAGER';
delete
from myupdatetype
where code = 'BACKUPS_EMPLOYEE_APPROVED';
delete
from myupdatetype
where code = 'BACKUPS_EMPLOYEE_REJECTED';
delete
from myupdatetype
where code = 'BACKUPS_EMPLOYEE';



insert into myupdatetype (code, description)
values ('BACKUPS_EMPLOYEE', 'All backups employee related updates');
insert into myupdatetype (code, description, parentid)
values ('BACKUPS_EMPLOYEE_ADD', 'Records when user has added backups employee', (select id from myupdatetype where code = 'BACKUPS_EMPLOYEE'));
insert into myupdatetype (code, description, parentid)
values ('BACKUPS_EMPLOYEE_EDIT', 'Records when user has edited backups employee', (select id from myupdatetype where code = 'BACKUPS_EMPLOYEE'));
insert into myupdatetype (code, description, parentid)
values ('BACKUPS_EMPLOYEE_DELETE', 'Records when user has deleted backups employee', (select id from myupdatetype where code = 'BACKUPS_EMPLOYEE'));
insert into myupdatetype (code, description, parentid)
values ('BACKUPS_EMPLOYEE_SUBMITTED_TO_MANAGER', 'Records when user has submited backups employee', (select id from myupdatetype where code = 'BACKUPS_EMPLOYEE'));
insert into myupdatetype (code, description, parentid)
values ('BACKUPS_EMPLOYEE_APPROVED', 'Records when user has approved backups employee', (select id from myupdatetype where code = 'BACKUPS_EMPLOYEE'));
insert into myupdatetype (code, description, parentid)
values ('BACKUPS_EMPLOYEE_REJECTED', 'Records when user has rejected backups employee', (select id from myupdatetype where code = 'BACKUPS_EMPLOYEE'));