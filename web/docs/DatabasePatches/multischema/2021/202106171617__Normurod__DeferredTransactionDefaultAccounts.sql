insert into "anv".account(accountcode, codestring, deleted, key, name, currencyid, accounttypeid) values ('2150', '2150', false, 2150, 'Deferred Revenue', (select currency_id from "anv".financialsettings), (select id from accounttype where code = 'CURRENT_LIABILITY'));
insert into accounttemplate(code, codestring, key, name, accounttypeid) values (2150, '2150', 2150, 'Deferred Revenue', (select id from accounttype where code = 'CURRENT_LIABILITY'));

insert into "anv".account(accountcode, codestring, deleted, key, name, currencyid, accounttypeid) values ('1180', '1180', false, 1180, 'Deferred Expense', (select currency_id from "anv".financialsettings), (select id from accounttype where code = 'CURRENT_ASSET'));
insert into accounttemplate(code, codestring, key, name, accounttypeid) values (1180, '1180', 1180, 'Deferred Expense', (select id from accounttype where code = 'CURRENT_ASSET'));
