


DELETE from myupdatetype WHERE code = 'SALES_ORDER_CONVERT_FROM_SQ';

insert into myupdatetype (code, description, parentid) values('SALES_ORDER_CONVERT_FROM_SQ', 'Records when user closed Sale Order', (select id from myupdatetype mu where mu.code='SALES_ORDER'));