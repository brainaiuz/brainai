insert into myupdatetype (code, description) values('ATTACHMENT', 'All attachment related updates');
insert into myupdatetype (code, description, parentid) values('ATTACHMENT_ADD', 'Records when user has uploaded attachment', (select id from myupdatetype mu where mu.code='ATTACHMENT'));
insert into myupdatetype (code, description, parentid) values('ATTACHMENT_DELETE', 'Records when user has deleted attachment', (select id from myupdatetype mu where mu.code='ATTACHMENT'));

