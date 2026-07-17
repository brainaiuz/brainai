
delete from myupdatetype where code='ACCOUNTING_STOCK_TRANSFER';
insert into myupdatetype (code, description) values('ACCOUNTING_STOCK_TRANSFER', 'All stock transfer note related updates');
insert into myupdatetype (code, description, parentid) values('ACCOUNTING_STOCK_TRANSFER_ADD', 'Records when user has add stock transfer note', (select id from myupdatetype mu where mu.code='ACCOUNTING_STOCK_TRANSFER'));
insert into myupdatetype (code, description, parentid) values('ACCOUNTING_STOCK_TRANSFER_EDIT', 'Records when user has edited stock transfer note', (select id from myupdatetype mu where mu.code='ACCOUNTING_STOCK_TRANSFER'));
insert into myupdatetype (code, description, parentid) values('ACCOUNTING_STOCK_TRANSFER_DELETE', 'Records when user has deleted stock transfer note', (select id from myupdatetype mu where mu.code='ACCOUNTING_STOCK_TRANSFER'));

insert into myupdatetype (code, description, parentid) values('ACCOUNTING_STOCK_TRANSFER_APPROVE', 'Records when user has approved stock transfer note', (select id from myupdatetype mu where mu.code='ACCOUNTING_STOCK_TRANSFER'));
insert into myupdatetype (code, description, parentid) values('ACCOUNTING_STOCK_TRANSFER_DECLINE', 'Records when user has rejected stock transfer note', (select id from myupdatetype mu where mu.code='ACCOUNTING_STOCK_TRANSFER'));
insert into myupdatetype (code, description, parentid) values('ACCOUNTING_STOCK_TRANSFER_SEND_TO_APPROVER', 'Records when user has sent stock transfer to approver', (select id from myupdatetype mu where mu.code='ACCOUNTING_STOCK_TRANSFER'));
insert into myupdatetype (code, description, parentid) values('ACCOUNTING_STOCK_TRANSFER_TRANSFERRED', 'Records when user transfers stock transfer', (select id from myupdatetype mu where mu.code='ACCOUNTING_STOCK_TRANSFER'));
