

DELETE from myupdatetype WHERE code in ('BANK_TRANSFER_APPLIED','BANK_TRANSFER_APPLIED_PAYABLE','BANK_TRANSFER_APPLIED_RECEIVABLE');

insert into myupdatetype (code, description) values('BANK_TRANSFER_APPLIED', 'Records when user applieds bank transfer');
insert into myupdatetype (code, description, parentid) values('BANK_TRANSFER_APPLIED_PAYABLE', 'Records when user applieds bank transfer', (select id from myupdatetype mu where mu.code='BANK_TRANSFER_APPLIED'));
insert into myupdatetype (code, description, parentid) values('BANK_TRANSFER_APPLIED_RECEIVABLE', 'Records when user applieds bank transfer', (select id from myupdatetype mu where mu.code='BANK_TRANSFER_APPLIED'));
