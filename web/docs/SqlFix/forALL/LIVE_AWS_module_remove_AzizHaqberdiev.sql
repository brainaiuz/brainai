delete from "anv".genericSettings where key = 'MULTIWAREHOUSE_ENABLED';
INSERT INTO "anv".genericSettings(key,value) values('MULTIWAREHOUSE_ENABLED', (select case when (select code from "anv".myModule where code = 'MULTIPLE_WAREHOUSE') is not null then 'YES' else 'NO' end));
delete from "anv".myModule where code = 'MULTIPLE_WAREHOUSE';