
insert into myupdatetype (code, description)
values ('HRMS_PLACEMENT', 'All placement related updates');

insert into myupdatetype (code, description, parentid)
values ('HRMS_PLACEMENT_ADD', 'Records when user has added placement', (select id from myupdatetype where code = 'HRMS_PLACEMENT'));
insert into myupdatetype (code, description, parentid)
values ('HRMS_PLACEMENT_EDIT', 'Records when user has edited placement', (select id from myupdatetype where code = 'HRMS_PLACEMENT'));
insert into myupdatetype (code, description, parentid)
values ('HRMS_PLACEMENT_DELETE', 'Records when user has deleted placement', (select id from myupdatetype where code = 'HRMS_PLACEMENT'));
insert into myupdatetype (code, description, parentid)
values ('HRMS_PLACEMENT_SEND_TO_APPROVER', 'Records when user has submited placement', (select id from myupdatetype where code = 'HRMS_PLACEMENT'));
insert into myupdatetype (code, description, parentid)
values ('HRMS_PLACEMENT_APPROVE', 'Records when user has approved placement', (select id from myupdatetype where code = 'HRMS_PLACEMENT'));
insert into myupdatetype (code, description, parentid)
values ('HRMS_PLACEMENT_DECLINE', 'Records when user has rejected placement', (select id from myupdatetype where code = 'HRMS_PLACEMENT'));