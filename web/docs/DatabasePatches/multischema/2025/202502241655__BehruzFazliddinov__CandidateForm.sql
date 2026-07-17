delete
from "0".reference_locale
where english = '_ASADBEK_BEGINNER';
delete
from "0".reference_locale
where english = '_ASADBEK_ELEMENTARY';
delete
from "0".reference_locale
where english = '_ASADBEK_INTERMEDIATE';
delete
from "0".reference_locale
where english = '_ASADBEK_UPPER_INTERMEDIATE';
delete
from "0".reference_locale
where english = '_ASADBEK_ADVANCED';
delete
from "0".reference_locale
where english = '_ASADBEK_PROFICIENT';


insert into "0".reference_locale(arabic, english, russian, uzbek)
values ('ابتدائي', '_ASADBEK_BEGINNER', 'Начальный', 'Boshlang''ich');
insert into "0".reference_locale(arabic, english, russian, uzbek)
values ('ابتدائي', '_ASADBEK_ELEMENTARY', 'Элементарный', 'Elementar');
insert into "0".reference_locale(arabic, english, russian, uzbek)
values ('متوسط', '_ASADBEK_INTERMEDIATE', 'Средний', 'O''rta');
insert into "0".reference_locale(arabic, english, russian, uzbek)
values ('متقدم باعتدال', '_ASADBEK_UPPER_INTERMEDIATE', 'Cредее продвинутый', 'Yuqori o''rta');
insert into "0".reference_locale(arabic, english, russian, uzbek)
values ('متقدم', '_ASADBEK_ADVANCED', 'Продвинутый', 'Yuqori');
insert into "0".reference_locale(arabic, english, russian, uzbek)
values ('بارع', '_ASADBEK_PROFICIENT', 'В Совершенстве', 'Mukammal');


update "0".reference r
set localeid =
        (select rl.id
         from "0".reference_locale rl
                  join "0".reference r2 on rl.english = '_ASADBEK_' || r2.code
         where r.id = r2.id)
where r.parentid = (select id from "0".reference where code = '_LANGUAGE_LEVELS');


update "0".reference_locale
set english = 'A1-Beginner'
where english = '_ASADBEK_BEGINNER';
update "0".reference_locale
set english = 'A2-Elementary'
where english = '_ASADBEK_ELEMENTARY';
update "0".reference_locale
set english = 'B1-Intermediate'
where english = '_ASADBEK_INTERMEDIATE';
update "0".reference_locale
set english = 'B2-Upper Intermediate'
where english = '_ASADBEK_UPPER_INTERMEDIATE';
update "0".reference_locale
set english = 'C1-Advanced'
where english = '_ASADBEK_ADVANCED';
update "0".reference_locale
set english = 'C2-Proficient'
where english = '_ASADBEK_PROFICIENT';



delete
from "anv".reference_locale
where english = '_ASADBEK_BEGINNER';
delete
from "anv".reference_locale
where english = '_ASADBEK_ELEMENTARY';
delete
from "anv".reference_locale
where english = '_ASADBEK_INTERMEDIATE';
delete
from "anv".reference_locale
where english = '_ASADBEK_UPPER_INTERMEDIATE';
delete
from "anv".reference_locale
where english = '_ASADBEK_ADVANCED';
delete
from "anv".reference_locale
where english = '_ASADBEK_PROFICIENT';


insert into "anv".reference_locale(arabic, english, russian, uzbek)
values ('ابتدائي', '_ASADBEK_BEGINNER', 'Начальный', 'Boshlang''ich');
insert into "anv".reference_locale(arabic, english, russian, uzbek)
values ('ابتدائي', '_ASADBEK_ELEMENTARY', 'Элементарный', 'Elementar');
insert into "anv".reference_locale(arabic, english, russian, uzbek)
values ('متوسط', '_ASADBEK_INTERMEDIATE', 'Средний', 'O''rta');
insert into "anv".reference_locale(arabic, english, russian, uzbek)
values ('متقدم باعتدال', '_ASADBEK_UPPER_INTERMEDIATE', 'Cредее продвинутый', 'Yuqori o''rta');
insert into "anv".reference_locale(arabic, english, russian, uzbek)
values ('متقدم', '_ASADBEK_ADVANCED', 'Продвинутый', 'Yuqori');
insert into "anv".reference_locale(arabic, english, russian, uzbek)
values ('بارع', '_ASADBEK_PROFICIENT', 'В Совершенстве', 'Mukammal');


update "anv".reference r
set localeid =
        (select rl.id
         from "anv".reference_locale rl
                  join "anv".reference r2 on rl.english = '_ASADBEK_' || r2.code
         where r.id = r2.id)
where r.parentid = (select id from "anv".reference where code = '_LANGUAGE_LEVELS');


update "anv".reference_locale
set english = 'A1-Beginner'
where english = '_ASADBEK_BEGINNER';
update "anv".reference_locale
set english = 'A2-Elementary'
where english = '_ASADBEK_ELEMENTARY';
update "anv".reference_locale
set english = 'B1-Intermediate'
where english = '_ASADBEK_INTERMEDIATE';
update "anv".reference_locale
set english = 'B2-Upper Intermediate'
where english = '_ASADBEK_UPPER_INTERMEDIATE';
update "anv".reference_locale
set english = 'C1-Advanced'
where english = '_ASADBEK_ADVANCED';
update "anv".reference_locale
set english = 'C2-Proficient'
where english = '_ASADBEK_PROFICIENT';




