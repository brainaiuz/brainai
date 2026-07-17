delete from "anv".account where key = 6499;
insert into "anv".account(accountcode, codestring, deleted, key, name, currencyid, accounttypeid) values ('6499', '6499', false, 6499, 'Cargo Expense Account', (select currency_id from "anv".financialsettings), (select id from accounttype where code = 'DIRECT_EXPENSES'));

delete from accounttemplate where key = 6499;
insert into accounttemplate(code, codestring, key, name, accounttypeid) values (6499, '6499', 6499, 'Cargo Expense Account', (select id from accounttype where code = 'DIRECT_EXPENSES'));