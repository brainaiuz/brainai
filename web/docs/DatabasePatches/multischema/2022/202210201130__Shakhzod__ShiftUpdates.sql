insert into myupdatetype (code, description)
values ('SHIFT', 'All backups employee related updates');
insert into myupdatetype (code, description, parentid)
values ('SHIFT_ADD', 'Records when user has added shift', (select id from myupdatetype where code = 'BACKUPS_EMPLOYEE'));
insert into myupdatetype (code, description, parentid)
values ('SHIFT_EDIT', 'Records when user has edited shift', (select id from myupdatetype where code = 'BACKUPS_EMPLOYEE'));
insert into myupdatetype (code, description, parentid)
values ('SHIFT_DELETE', 'Records when user has deleted shift', (select id from myupdatetype where code = 'BACKUPS_EMPLOYEE'));
insert into myupdatetype (code, description, parentid)
values ('SHIFT_SUBMITTED', 'Records when user has submited shift', (select id from myupdatetype where code = 'BACKUPS_EMPLOYEE'));
insert into myupdatetype (code, description, parentid)
values ('SHIFT_APPROVED', 'Records when user has approved shift', (select id from myupdatetype where code = 'BACKUPS_EMPLOYEE'));
insert into myupdatetype (code, description, parentid)
values ('SHIFT_REJECTED', 'Records when user has rejected shift', (select id from myupdatetype where code = 'BACKUPS_EMPLOYEE'));

