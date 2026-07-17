
DELETE from myupdatetype WHERE code in ('EVENT','EVENT_ADD','EVENT_EDIT','EVENT_DELETE');

insert into myupdatetype (code, description) values ('EVENT', 'All activities related updates');
insert into myupdatetype (code, description, parentid) values ('EVENT_ADD',     'Records when user has added activity',     (select id from myupdatetype where code='EVENT'));
insert into myupdatetype (code, description, parentid) values ('EVENT_EDIT',    'Records when user has edited activity',    (select id from myupdatetype where code='EVENT'));
insert into myupdatetype (code, description, parentid) values ('EVENT_DELETE',  'Records when user has deleted activity',   (select id from myupdatetype where code='EVENT'));

