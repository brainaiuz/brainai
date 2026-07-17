
-----------contact note
insert into myupdatetype (code, description) values('CONTACT_NOTE', 'All contact note related updates');
insert into myupdatetype (code, description, parentid) values('CONTACT_NOTE_ADD', 'Records when user has add contact note', (select id from myupdatetype mu where mu.code='CONTACT_NOTE'));
insert into myupdatetype (code, description, parentid) values('CONTACT_NOTE_EDIT', 'Records when user has edited contact note', (select id from myupdatetype mu where mu.code='CONTACT_NOTE'));
insert into myupdatetype (code, description, parentid) values('CONTACT_NOTE_DELETE', 'Records when user has deleted contact note', (select id from myupdatetype mu where mu.code='CONTACT_NOTE'));
-----------lead note
insert into myupdatetype (code, description) values('LEAD_NOTE', 'All lead note related updates');
insert into myupdatetype (code, description, parentid) values('LEAD_NOTE_ADD', 'Records when user has add lead note', (select id from myupdatetype mu where mu.code='LEAD_NOTE'));
insert into myupdatetype (code, description, parentid) values('LEAD_NOTE_EDIT', 'Records when user has edited lead note', (select id from myupdatetype mu where mu.code='LEAD_NOTE'));
insert into myupdatetype (code, description, parentid) values('LEAD_NOTE_DELETE', 'Records when user has deleted lead note', (select id from myupdatetype mu where mu.code='LEAD_NOTE'));