
delete from myupdatetype where code='ACCOUNTING_STOCK_ADJUSTMENT';
insert into myupdatetype (code, description) values('ACCOUNTING_STOCK_ADJUSTMENT', 'All stock adjustment note related updates');
insert into myupdatetype (code, description, parentid) values('ACCOUNTING_STOCK_ADJUSTMENT_ADD', 'Records when user has add stock adjustment note', (select id from myupdatetype mu where mu.code='ACCOUNTING_STOCK_ADJUSTMENT'));
insert into myupdatetype (code, description, parentid) values('ACCOUNTING_STOCK_ADJUSTMENT_EDIT', 'Records when user has edited stock adjustment note', (select id from myupdatetype mu where mu.code='ACCOUNTING_STOCK_ADJUSTMENT'));
insert into myupdatetype (code, description, parentid) values('ACCOUNTING_STOCK_ADJUSTMENT_DELETE', 'Records when user has deleted stock adjustment note', (select id from myupdatetype mu where mu.code='ACCOUNTING_STOCK_ADJUSTMENT'));

insert into myupdatetype (code, description, parentid) values('ACCOUNTING_STOCK_ADJUSTMENT_APPROVE', 'Records when user has approved stock adjustment note', (select id from myupdatetype mu where mu.code='ACCOUNTING_STOCK_ADJUSTMENT'));
insert into myupdatetype (code, description, parentid) values('ACCOUNTING_STOCK_ADJUSTMENT_DECLINE', 'Records when user has rejected stock adjustment note', (select id from myupdatetype mu where mu.code='ACCOUNTING_STOCK_ADJUSTMENT'));
insert into myupdatetype (code, description, parentid) values('ACCOUNTING_STOCK_ADJUSTMENT_SEND_TO_APPROVER', 'Records when user has sent stock adjustment to approver', (select id from myupdatetype mu where mu.code='ACCOUNTING_STOCK_ADJUSTMENT'));
