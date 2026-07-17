insert into myupdatetype (code, description)
values ('GROUP_PLACEMENT', 'All group placement related updates');
insert into myupdatetype (code, description, parentid)
values ('GROUP_PLACEMENT_ADD', 'Records when user has added group placement', (select id from myupdatetype where code = 'GROUP_PLACEMENT'));
insert into myupdatetype (code, description, parentid)
values ('GROUP_PLACEMENT_EDIT', 'Records when user has edited group placement', (select id from myupdatetype where code = 'GROUP_PLACEMENT'));
insert into myupdatetype (code, description, parentid)
values ('GROUP_PLACEMENT_DELETE', 'Records when user has deleted group placement', (select id from myupdatetype where code = 'GROUP_PLACEMENT'));
insert into myupdatetype (code, description, parentid)
values ('GROUP_PLACEMENT_SUBMITTED', 'Records when user has submited group placement', (select id from myupdatetype where code = 'GROUP_PLACEMENT'));
insert into myupdatetype (code, description, parentid)
values ('GROUP_PLACEMENT_APPROVED', 'Records when user has approved group placement', (select id from myupdatetype where code = 'GROUP_PLACEMENT'));
insert into myupdatetype (code, description, parentid)
values ('GROUP_PLACEMENT_REJECTED', 'Records when user has rejected group placement', (select id from myupdatetype where code = 'GROUP_PLACEMENT'));

