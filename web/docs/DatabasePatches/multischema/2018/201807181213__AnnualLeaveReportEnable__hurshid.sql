delete from "0".genericsettings where key='ANNUAL_LEAVE_BALANCE_REPORT';
insert into "0".genericsettings (key, value) values('ANNUAL_LEAVE_BALANCE_REPORT', 'YES');
delete from "anv".genericsettings where key='ANNUAL_LEAVE_BALANCE_REPORT';
insert into "anv".genericsettings (key, value) values('ANNUAL_LEAVE_BALANCE_REPORT', 'YES');