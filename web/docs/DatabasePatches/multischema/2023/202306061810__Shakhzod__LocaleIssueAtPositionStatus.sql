insert into "anv".reference_locale (english, russian, arabic, uzbek)
values ('Open', 'Открытый', 'Open', 'Ochiq');
insert into "anv".reference_locale (english, russian, arabic, uzbek)
values ('Active', 'Активный', 'Active', 'Faol');

update "anv".reference
set localeid = (select id from "anv".reference_locale where english = 'Open')
where code = 'POS_STATUS_OPEN';
update "anv".reference
set localeid =(select id from "anv".reference_locale where english = 'Active')
where code = 'POS_STATUS_ACTIVE';