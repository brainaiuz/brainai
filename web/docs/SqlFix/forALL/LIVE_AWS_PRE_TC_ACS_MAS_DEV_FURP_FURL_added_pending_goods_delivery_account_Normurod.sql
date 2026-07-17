--- For Public schema ---
INSERT INTO accounttemplate(code, codestring, name, accounttypeid, key)
VALUES (1249, '1249', 'Pending goods delivered notes', (select id from accounttype where code = 'CURRENT_ASSET'), 1249);

--- For all schema ---
INSERT INTO "anv".account(accountcode, codestring, deleted,key, name, accounttypeid, currencyid)
VALUES('1249','1249',false, 1249, 'Pending goods delivered notes',
       (select id from accounttype where code = 'CURRENT_ASSET'),
       (select currency_id from "anv".financialsettings));