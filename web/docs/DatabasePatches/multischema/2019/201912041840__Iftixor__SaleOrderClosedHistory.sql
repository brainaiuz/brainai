
DELETE from myupdatetype WHERE code = 'SALES_ORDER_CLOSED';

insert into myupdatetype (code, description, parentid) values('SALES_ORDER_CLOSED', 'Records when user closed Sale Order', (select id from myupdatetype mu where mu.code='SALES_ORDER'));