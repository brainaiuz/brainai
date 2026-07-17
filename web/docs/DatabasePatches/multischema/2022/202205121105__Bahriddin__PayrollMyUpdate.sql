insert into myupdatetype (code, description)
values ('ADDITIONAL_PAYMENT', 'All additional payment related updates');

insert into myupdatetype (code, description, parentid)
values ('ADDITIONAL_PAYMENT_ADD', 'Records when user has added additional payment', (select id from myupdatetype where code = 'ADDITIONAL_PAYMENT'));
insert into myupdatetype (code, description, parentid)
values ('ADDITIONAL_PAYMENT_EDIT', 'Records when user has edited additional payment', (select id from myupdatetype where code = 'ADDITIONAL_PAYMENT'));
insert into myupdatetype (code, description, parentid)
values ('ADDITIONAL_PAYMENT_DELETED', 'Records when user has deleted additional payment', (select id from myupdatetype where code = 'ADDITIONAL_PAYMENT'));
insert into myupdatetype (code, description, parentid)
values ('ADDITIONAL_PAYMENT_SUBMITTED_TO_MANAGER', 'Records when user has submited additional payment', (select id from myupdatetype where code = 'ADDITIONAL_PAYMENT'));
insert into myupdatetype (code, description, parentid)
values ('ADDITIONAL_PAYMENT_APPROVED', 'Records when user has approved additional payment', (select id from myupdatetype where code = 'ADDITIONAL_PAYMENT'));
insert into myupdatetype (code, description, parentid)
values ('ADDITIONAL_PAYMENT_REJECTED', 'Records when user has rejected additional payment', (select id from myupdatetype where code = 'ADDITIONAL_PAYMENT'));