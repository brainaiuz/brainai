
delete from "anv".account where accountcode='2021';
insert into "anv".account(accountcode, codestring, deleted, key, name, currencyid, accounttypeid) values ('2021', '2021', false, 2021, 'Unearned Revenue', (select currency_id from "anv".financialsettings), (select id from accounttype where code = 'CURRENT_LIABILITY'));

delete from accounttemplate where code=2021;
insert into accounttemplate(code, codestring, key, name, accounttypeid) values (2021, '2021', 2021, 'Unearned Revenue', (select id from accounttype where code = 'CURRENT_LIABILITY'));

delete from "anv".account where accountcode='1011';
insert into "anv".account(accountcode, codestring, deleted, key, name, currencyid, accounttypeid) values ('1011', '1011', false, 1011, 'Prepaid Expenses', (select currency_id from "anv".financialsettings), (select id from accounttype where code = 'CURRENT_ASSET'));

delete from accounttemplate where code=1011;
insert into accounttemplate(code, codestring, key, name, accounttypeid) values (1011, '1011', 1011, 'Prepaid Expenses', (select id from accounttype where code = 'CURRENT_ASSET'));