insert into myupdatetype (code, description)
values ('ROTATION', 'All rotation related updates');
insert into myupdatetype (code, description, parentid)
values ('ROTATION_ADD', 'Records when user has added rotation', (select id from myupdatetype where code = 'ROTATION'));
insert into myupdatetype (code, description, parentid)
values ('ROTATION_EDIT', 'Records when user has edited rotation', (select id from myupdatetype where code = 'ROTATION'));
insert into myupdatetype (code, description, parentid)
values ('ROTATION_DELETE', 'Records when user has deleted rotation', (select id from myupdatetype where code = 'ROTATION'));
insert into myupdatetype (code, description, parentid)
values ('ROTATION_SUBMITTED', 'Records when user has submited rotation', (select id from myupdatetype where code = 'ROTATION'));
insert into myupdatetype (code, description, parentid)
values ('ROTATION_APPROVED', 'Records when user has approved rotation', (select id from myupdatetype where code = 'ROTATION'));
insert into myupdatetype (code, description, parentid)
values ('ROTATION_REJECTED', 'Records when user has rejected rotation', (select id from myupdatetype where code = 'ROTATION'));

