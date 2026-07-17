insert into "anv".account(accountcode, codestring, deleted, key, name, currencyid, accounttypeid) values ('1212', '1212', false, 1212, 'Sales Discount', (select currency_id from "anv".financialsettings), (select id from accounttype where code = 'SALES'));
insert into accounttemplate(code, codestring, key, name, accounttypeid) values (1212, '1212', 1212, 'Sales Discount', (select id from accounttype where code = 'SALES'));

insert into "anv".account(accountcode, codestring, deleted, key, name, currencyid, accounttypeid) values ('2121', '2121', false, 2121, 'Purchase Discount', (select currency_id from "anv".financialsettings), (select id from accounttype where code = 'COST_OF_SALES'));
insert into accounttemplate(code, codestring, key, name, accounttypeid) values (2121, '2121', 2121, 'Purchase Discount', (select id from accounttype where code = 'COST_OF_SALES'));
