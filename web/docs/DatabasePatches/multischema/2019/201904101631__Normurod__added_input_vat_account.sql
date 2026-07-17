--- For Public schema ---
INSERT INTO accounttemplate(code, codestring, name, accounttypeid, key)
VALUES (1248, '1248', 'Input VAT', (select id from accounttype where code = 'CURRENT_ASSET'), 1248);

--- For all schema ---
INSERT INTO "anv".account(accountcode, codestring, deleted,key, name, accounttypeid, currencyid)
VALUES('1248','1248',false, 1248, 'Input VAT',
       (select id from accounttype where code = 'CURRENT_ASSET'),
       (select currency_id from "anv".financialsettings));