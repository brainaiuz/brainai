*********************************************
*********THIS IS NOT DATABASE PATCH !!! *****
*********************************************

create table logaudit_by_2013_12 (PRIMARY KEY (id), check(action_date between '2013-12-01 00:00:00' and '2013-12-31 23:59:59')) inherits (logaudit);
create table logaudit_by_2014_01 (PRIMARY KEY (id), check(action_date between '2014-01-01 00:00:00' and '2014-01-31 23:59:59')) inherits (logaudit);
create table logaudit_by_2014_02 (PRIMARY KEY (id), check(action_date between '2014-02-01 00:00:00' and '2014-02-30 23:59:59')) inherits (logaudit);
create table logaudit_by_2014_03 (PRIMARY KEY (id), check(action_date between '2014-03-01 00:00:00' and '2014-03-31 23:59:59')) inherits (logaudit);
create table logaudit_by_2014_04 (PRIMARY KEY (id), check(action_date between '2014-04-01 00:00:00' and '2014-04-30 23:59:59')) inherits (logaudit);
create table logaudit_by_2014_05 (PRIMARY KEY (id), check(action_date between '2014-05-01 00:00:00' and '2014-05-31 23:59:59')) inherits (logaudit);
create table logaudit_by_2014_06 (PRIMARY KEY (id), check(action_date between '2014-06-01 00:00:00' and '2014-06-30 23:59:59')) inherits (logaudit);
create table logaudit_by_2014_07 (PRIMARY KEY (id), check(action_date between '2014-07-01 00:00:00' and '2014-07-31 23:59:59')) inherits (logaudit);
create table logaudit_by_2014_08 (PRIMARY KEY (id), check(action_date between '2014-08-01 00:00:00' and '2014-08-31 23:59:59')) inherits (logaudit);
create table logaudit_by_2014_09 (PRIMARY KEY (id), check(action_date between '2014-09-01 00:00:00' and '2014-09-30 23:59:59')) inherits (logaudit);
create table logaudit_by_2014_10 (PRIMARY KEY (id), check(action_date between '2014-10-01 00:00:00' and '2014-10-31 23:59:59')) inherits (logaudit);
create table logaudit_by_2014_11 (PRIMARY KEY (id), check(action_date between '2014-11-01 00:00:00' and '2014-11-30 23:59:59')) inherits (logaudit);
create table logaudit_by_2014_12 (PRIMARY KEY (id), check(action_date between '2014-12-01 00:00:00' and '2014-12-31 23:59:59')) inherits (logaudit);

create index logaudit_by_2013_12_index ON logaudit_by_2013_12 (action_date);
create index logaudit_by_2014_01_index ON logaudit_by_2014_01 (action_date);
create index logaudit_by_2014_02_index ON logaudit_by_2014_02 (action_date);
create index logaudit_by_2014_03_index ON logaudit_by_2014_03 (action_date);
create index logaudit_by_2014_04_index ON logaudit_by_2014_04 (action_date);
create index logaudit_by_2014_05_index ON logaudit_by_2014_05 (action_date);
create index logaudit_by_2014_06_index ON logaudit_by_2014_06 (action_date);
create index logaudit_by_2014_07_index ON logaudit_by_2014_07 (action_date);
create index logaudit_by_2014_08_index ON logaudit_by_2014_08 (action_date);
create index logaudit_by_2014_09_index ON logaudit_by_2014_09 (action_date);
create index logaudit_by_2014_10_index ON logaudit_by_2014_10 (action_date);
create index logaudit_by_2014_11_index ON logaudit_by_2014_11 (action_date);
create index logaudit_by_2014_12_index ON logaudit_by_2014_12 (action_date);

 create or replace function logaudit_by_month_insert_trigger() returns trigger as $$ begin
 if(NEW.action_date between '2013-05-01 00:00:00' and '2013-05-31 23:59:59') THEN  INSERT INTO logaudit_by_2013_05 VALUES (NEW.*);
 elseif (NEW.action_date between '2013-06-01 00:00:00' and '2013-06-30 23:59:59') THEN  INSERT INTO logaudit_by_2013_06 VALUES (NEW.*);
 elseif (NEW.action_date between '2013-07-01 00:00:00' and '2013-07-31 23:59:59') THEN  INSERT INTO logaudit_by_2013_07 VALUES (NEW.*);
 elseif (NEW.action_date between '2013-08-01 00:00:00' and '2013-08-31 23:59:59') THEN  INSERT INTO logaudit_by_2013_08 VALUES (NEW.*);
 elseif (NEW.action_date between '2013-09-01 00:00:00' and '2013-09-30 23:59:59') THEN  INSERT INTO logaudit_by_2013_09 VALUES (NEW.*);
 elseif (NEW.action_date between '2013-10-01 00:00:00' and '2013-10-31 23:59:59') THEN  INSERT INTO logaudit_by_2013_10 VALUES (NEW.*);
 elseif (NEW.action_date between '2013-11-01 00:00:00' and '2013-11-30 23:59:59') THEN  INSERT INTO logaudit_by_2013_11 VALUES (NEW.*);
 elseif (NEW.action_date between '2013-12-01 00:00:00' and '2013-12-31 23:59:59') THEN  INSERT INTO logaudit_by_2013_12 VALUES (NEW.*);
 elseif (NEW.action_date between '2014-01-01 00:00:00' and '2014-01-31 23:59:59') THEN  INSERT INTO logaudit_by_2014_01 VALUES (NEW.*);
 elseif (NEW.action_date between '2014-02-01 00:00:00' and '2014-02-30 23:59:59') THEN  INSERT INTO logaudit_by_2014_02 VALUES (NEW.*);
 elseif (NEW.action_date between '2014-03-01 00:00:00' and '2014-03-31 23:59:59') THEN  INSERT INTO logaudit_by_2014_03 VALUES (NEW.*);
 elseif (NEW.action_date between '2014-04-01 00:00:00' and '2014-04-30 23:59:59') THEN  INSERT INTO logaudit_by_2014_04 VALUES (NEW.*);
 elseif (NEW.action_date between '2014-05-01 00:00:00' and '2014-05-31 23:59:59') THEN  INSERT INTO logaudit_by_2014_05 VALUES (NEW.*);
 elseif (NEW.action_date between '2014-06-01 00:00:00' and '2014-06-30 23:59:59') THEN  INSERT INTO logaudit_by_2014_06 VALUES (NEW.*);
 elseif (NEW.action_date between '2014-07-01 00:00:00' and '2014-07-31 23:59:59') THEN  INSERT INTO logaudit_by_2014_07 VALUES (NEW.*);
 elseif (NEW.action_date between '2014-08-01 00:00:00' and '2014-08-31 23:59:59') THEN  INSERT INTO logaudit_by_2014_08 VALUES (NEW.*);
 elseif (NEW.action_date between '2014-09-01 00:00:00' and '2014-09-30 23:59:59') THEN  INSERT INTO logaudit_by_2014_09 VALUES (NEW.*);
 elseif (NEW.action_date between '2014-10-01 00:00:00' and '2014-10-31 23:59:59') THEN  INSERT INTO logaudit_by_2014_10 VALUES (NEW.*);
 elseif (NEW.action_date between '2014-11-01 00:00:00' and '2014-11-30 23:59:59') THEN  INSERT INTO logaudit_by_2014_11 VALUES (NEW.*);
 elseif (NEW.action_date between '2014-12-01 00:00:00' and '2014-12-31 23:59:59') THEN  INSERT INTO logaudit_by_2014_12 VALUES (NEW.*);

 INSERT INTO logaudit VALUES(NEW.*); END iF; RETURN NULL;

 END; $$ LANGUAGE plpgsql;

 CREATE TRIGGER logaudit_by_month_trigger
	BEFORE INSERT ON logaudit
	FOR EACH ROW EXECUTE PROCEDURE logaudit_by_month_insert_trigger();


SET constraint_exclusion = ON;
