

DELETE from myupdatetype WHERE code = 'SALES_QUOTE_CLOSED';

insert into myupdatetype (code, description, parentid) values('SALES_QUOTE_CLOSED', 'Records when user closed Sale Quote', (select id from myupdatetype mu where mu.code='SALES_QUOTE'));