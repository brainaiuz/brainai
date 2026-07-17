

DELETE from myupdatetype WHERE code ='MANUAL_JOURNAL_APPLIED';

insert into myupdatetype (code, description) values('MANUAL_JOURNAL_APPLIED', 'Records when user applieds manual journal');
insert into myupdatetype (code, description, parentid) values('MANUAL_JOURNAL_APPLIED_RECEIVABLE_PAYABLE', 'Records when user applieds manual journal', (select id from myupdatetype mu where mu.code='MANUAL_JOURNAL_APPLIED'));

