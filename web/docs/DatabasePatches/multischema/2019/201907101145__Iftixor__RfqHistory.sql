

insert into myupdatetype (code, description) values ('ACCOUNTING_REQUEST_FOR_QUOTE', 'All Request for quote updates');
insert into myupdatetype (code, description, parentid) values ('ACCOUNTING_REQUEST_FOR_QUOTE_ADD',     'Records when user has added rfq',     (select id from myupdatetype where code='ACCOUNTING_REQUEST_FOR_QUOTE'));
insert into myupdatetype (code, description, parentid) values ('ACCOUNTING_REQUEST_FOR_QUOTE_EDIT',    'Records when user has edited rfq',    (select id from myupdatetype where code='ACCOUNTING_REQUEST_FOR_QUOTE'));
insert into myupdatetype (code, description, parentid) values ('ACCOUNTING_REQUEST_FOR_QUOTE_DELETE',  'Records when user has deleted rfq',   (select id from myupdatetype where code='ACCOUNTING_REQUEST_FOR_QUOTE'));
