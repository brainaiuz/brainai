DELETE from myupdatetype WHERE code in ('CUSTOM_FORM_ITEM','CUSTOM_FORM_ITEM_ADD','CUSTOM_FORM_ITEM_EDIT','CUSTOM_FORM_ITEM_DELETE');

insert into myupdatetype (code, description) values('CUSTOM_FORM_ITEM', 'All custom form item related updates');
insert into myupdatetype (code, description, parentid) values('CUSTOM_FORM_ITEM_ADD', 'Records when user has add custom form item', (select id from myupdatetype mu where mu.code='CUSTOM_FORM_ITEM'));
insert into myupdatetype (code, description, parentid) values('CUSTOM_FORM_ITEM_EDIT', 'Records when user has edited custom form item', (select id from myupdatetype mu where mu.code='CUSTOM_FORM_ITEM'));
insert into myupdatetype (code, description, parentid) values('CUSTOM_FORM_ITEM_DELETE', 'Records when user has deleted custom form item', (select id from myupdatetype mu where mu.code='CUSTOM_FORM_ITEM'));
