--Leave Request number uchun urilmasa numbering settings null pointer tawedi
insert into recurrencejob (name) values ('Restart Leave Request number');


update "anv".pmnumberingsettings set leaveRequestLastIntNumber = (select s.intNumber from "anv".SickRequest s where s.intNumber is not null order by s.intNumber desc limit 1);