insert into "anv".reference_locale(russian, uzbek,english) values('Открытый', 'Ochiq','Open');
update "anv".reference set localeid = (select id from "anv".reference_locale where russian = 'Открытый' and uzbek = 'Ochiq')
where code = 'POS_STATUS_OPEN';