--Account transactionda currency fieldni korsatish uchun
delete from "anv".genericsettings where key='ENABLE_CURRENCY_FIELD';
delete from "anv".genericsettings where key='ENABLE_CURRENCY_FIELD_IN_ACCOUNT_TRANSACTIONS';
insert into "anv".genericsettings (key,value) values ('ENABLE_CURRENCY_FIELD_IN_ACCOUNT_TRANSACTIONS',
(select case when (select value from "anv".genericSettings where key = 'ENABLE_CURRENCY_FIELD') is null then 'NO' when
(select value from "anv".genericSettings where key = 'ENABLE_CURRENCY_FIELD')='YES' then 'YES' else 'NO' end));