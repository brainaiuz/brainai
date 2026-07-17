
DELETE from myupdatetype WHERE code = 'PURCHASE_ORDER_CLOSED';

insert into myupdatetype (code, description, parentid) values('PURCHASE_ORDER_CLOSED', 'Records when user closed Purchase Order', (select id from myupdatetype mu where mu.code='PURCHASE_ORDER'));