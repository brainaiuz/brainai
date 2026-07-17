insert into myupdatetype (code, description) values('COMPANY_SETTINGS', 'All company settings related updates');
insert into myupdatetype (code, description, parentid) values('COMPANY_SETTINGS_EDIT', 'Records when user has added company settings', (select id from myupdatetype mu where mu.code='COMPANY_SETTINGS'));
