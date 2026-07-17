--Custom Employee Lookup fieldlarda Employee Fullname chiqishi uchun
delete from "300205".genericsettings where key = 'FULLNAME_FOR_CUSTOM_EMPLOYEE_LOOKUP';
insert into "300205".genericsettings (key, value) VALUES ('FULLNAME_FOR_CUSTOM_EMPLOYEE_LOOKUP', 'YES');