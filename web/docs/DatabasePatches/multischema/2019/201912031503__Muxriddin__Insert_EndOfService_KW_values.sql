
update company set selectFunctioncolumn =(select setval('"0".eos_settings_id_seq', (select max(id) from "0".eos_settings))) where id=(select id from company limit 1);
insert into "0".eos_settings (countrycode,payfrom,includebenefitpayments,includeleaveallowances,fromallallowances,fromlastpayment,usemonthpayment)
values('KW',0,false,false,false,false,false);

update company set selectFunctioncolumn =(select setval('"anv".eos_settings_id_seq', (select max(id) from "anv".eos_settings))) where id=(select id from company limit 1);
insert into "anv".eos_settings (countrycode,payfrom,includebenefitpayments,includeleaveallowances,fromallallowances,fromlastpayment,usemonthpayment)
values('KW',0,false,false,false,false,false);

update company set selectFunctioncolumn =(select setval('"0".eos_rules_id_seq', (select max(id) from "0".eos_rules))) where id=(select id from company limit 1);
insert into "0".eos_rules(days,reasoncode,rule,rulecode,settings_id,ruletype,months) values(0,'EMPLOYEE_RESIGNATION','Less than 1 year','0<x<1', (select id from "0".eos_settings where countrycode='KW'),0,0);
insert into "0".eos_rules(days,reasoncode,rule,rulecode,settings_id,ruletype,months) values(7,'EMPLOYEE_RESIGNATION','1 to 3 years','1<=x<3',(select id from "0".eos_settings where countrycode='KW'),0,0);
insert into "0".eos_rules(days,reasoncode,rule,rulecode,settings_id,ruletype,months) values(14,'EMPLOYEE_RESIGNATION','3 to 5 years','3<=x<5',(select id from "0".eos_settings where countrycode='KW'),0,0);
insert into "0".eos_rules(days,reasoncode,rule,rulecode,settings_id,ruletype,months) values(21,'EMPLOYEE_RESIGNATION','More than 5 years','x>5',(select id from "0".eos_settings where countrycode='KW'),0,0);
insert into "0".eos_rules(days,reasoncode,rule,rulecode,settings_id,ruletype,months) values(21,'CONTRACT_TERMINATION','Less than 3 years','x<=3',(select id from "0".eos_settings where countrycode='KW'),0,0);
insert into "0".eos_rules(days,reasoncode,rule,rulecode,settings_id,ruletype,months) values(30,'CONTRACT_TERMINATION','More than 3 years','x>3',(select id from "0".eos_settings where countrycode='KW'),0,0);

update company set selectFunctioncolumn =(select setval('"anv".eos_rules_id_seq', (select max(id) from "anv".eos_rules))) where id=(select id from company limit 1);
insert into "anv".eos_rules(days,reasoncode,rule,rulecode,settings_id,ruletype,months) values(0,'EMPLOYEE_RESIGNATION','Less than 1 year','0<x<1', (select id from "anv".eos_settings where countrycode='KW'),0,0);
insert into "anv".eos_rules(days,reasoncode,rule,rulecode,settings_id,ruletype,months) values(7,'EMPLOYEE_RESIGNATION','1 to 3 years','1<=x<3',(select id from "anv".eos_settings where countrycode='KW'),0,0);
insert into "anv".eos_rules(days,reasoncode,rule,rulecode,settings_id,ruletype,months) values(14,'EMPLOYEE_RESIGNATION','3 to 5 years','3<=x<5',(select id from "anv".eos_settings where countrycode='KW'),0,0);
insert into "anv".eos_rules(days,reasoncode,rule,rulecode,settings_id,ruletype,months) values(21,'EMPLOYEE_RESIGNATION','More than 5 years','x>5',(select id from "anv".eos_settings where countrycode='KW'),0,0);
insert into "anv".eos_rules(days,reasoncode,rule,rulecode,settings_id,ruletype,months) values(21,'CONTRACT_TERMINATION','Less than 3 years','x<=3',(select id from "anv".eos_settings where countrycode='KW'),0,0);
insert into "anv".eos_rules(days,reasoncode,rule,rulecode,settings_id,ruletype,months) values(30,'CONTRACT_TERMINATION','More than 3 years','x>3',(select id from "anv".eos_settings where countrycode='KW'),0,0);