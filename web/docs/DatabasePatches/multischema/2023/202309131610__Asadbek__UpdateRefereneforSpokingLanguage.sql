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
values ('ابتدائي', '_ASADBEK_BEGINNER', 'A1-Начальный', 'A1-Boshlang''ich');
insert into "anv".reference_locale(arabic, english, russian, uzbek)
values ('ابتدائي', '_ASADBEK_ELEMENTARY', 'A2-Элементарный', 'A2-Elementar');
insert into "anv".reference_locale(arabic, english, russian, uzbek)
values ('متوسط', '_ASADBEK_INTERMEDIATE', 'B1-Средний', 'B1-O''rta');
insert into "anv".reference_locale(arabic, english, russian, uzbek)
values ('متقدم باعتدال', '_ASADBEK_UPPER_INTERMEDIATE', 'B2-Cредее продвинутый', 'B2-Yuqori o''rta');
insert into "anv".reference_locale(arabic, english, russian, uzbek)
values ('متقدم', '_ASADBEK_ADVANCED', 'C1-Продвинутый', 'C1-Yuqori');
insert into "anv".reference_locale(arabic, english, russian, uzbek)
values ('بارع', '_ASADBEK_PROFICIENT', 'C2-В Совершенстве', 'C2-Mukammal');


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
