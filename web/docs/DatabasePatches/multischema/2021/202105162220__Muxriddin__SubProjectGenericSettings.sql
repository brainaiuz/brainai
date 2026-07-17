
delete from "anv".genericsettings where key ='ENABLE_SUB_PROJECT';
insert into "anv".genericsettings(key,value) values ('ENABLE_SUB_PROJECT', (select case when (select issetupsubproject from companysettings where id = (select companysettingsid from company where id=cast(replace('"anv"', '"', '') as int))) is true then 'YES' else 'NO' end));

