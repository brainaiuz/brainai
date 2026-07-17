update "anv".account set name = 'VAT Payable' where key = 2202 and name ='Output VAT';

insert into "anv".account(accountcode, codestring, deleted, key, name, currencyid, accounttypeid) values ('2398', '2398', false, 2398, 'Output VAT', (select currency_id from "anv".financialsettings), (select id from accounttype where code = 'CURRENT_LIABILITY'));

insert into accounttemplate(code, codestring, key, name, accounttypeid) values (2398, '2398', 2398, 'Output VAT', (select id from accounttype where code = 'CURRENT_LIABILITY'));
