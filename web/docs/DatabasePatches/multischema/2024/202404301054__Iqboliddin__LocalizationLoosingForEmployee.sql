insert into "anv".reference_locale(english, uzbek)
SELECT 'Conflict with the manager', 'Rahbar bilan ziddiyat'
WHERE NOT EXISTS (
        SELECT 1 FROM "anv".reference_locale
        WHERE english = 'Conflict with the manager'
          and uzbek = 'Rahbar bilan ziddiyat'
    );
update "anv".reference
set localeid = (select id
                from "anv".reference_locale
                where english = 'Conflict with the manager'
                  and uzbek = 'Rahbar bilan ziddiyat')
where code = 'КОНФОИКТ_С_РУКОВОДИТЕЛЕМ';


insert into "anv".reference_locale(english, uzbek)
SELECT 'Cannot find common ground within the team', 'Jamoada umumiy til topisha olmayapman'
WHERE NOT EXISTS (
        SELECT 1 FROM "anv".reference_locale
        WHERE english = 'Cannot find common ground within the team'
          and uzbek = 'Jamoada umumiy til topisha olmayapman'
    );
update "anv".reference
set localeid = (select id
                from "anv".reference_locale
                where english = 'Cannot find common ground within the team'
                  and uzbek = 'Jamoada umumiy til topisha olmayapman')
where code = 'НЕ_МОГУ_НАЙТИ_ОБЩИЙ_ЯЗЫК_В_КОМАНДЕ';


insert into "anv".reference_locale(english, uzbek)
SELECT 'Lack of career growth', 'Karyerada o''sish yo''q'
WHERE NOT EXISTS (
        SELECT 1 FROM "anv".reference_locale
        WHERE english = 'Lack of career growth'
          and uzbek = 'Karyerada o''sish yo''q'
    );
update "anv".reference
set localeid = (select id
                from "anv".reference_locale
                where english = 'Lack of career growth'
                  and uzbek = 'Karyerada o''sish yo''q')
where code = 'НЕТ_КАРЬЕРНОГО_РОСТА';


insert into "anv".reference_locale(english, uzbek)
SELECT 'Was offered a more interesting job', 'Qiziqroq ish taklif qilindi'
WHERE NOT EXISTS (
        SELECT 1 FROM "anv".reference_locale
        WHERE english = 'Was offered a more interesting job'
          and uzbek = 'Qiziqroq ish taklif qilindi'
    );
update "anv".reference
set localeid = (select id
                from "anv".reference_locale
                where english = 'Was offered a more interesting job'
                  and uzbek = 'Qiziqroq ish taklif qilindi')
where code = 'ПРЕДЛОЖИЛИ_РАБОТУ_ИНТЕРЕСНЕЕ';


insert into "anv".reference_locale(english, uzbek)
SELECT 'Due to family circumstances', 'Oilaviy sabablarga ko''ra'
WHERE NOT EXISTS (
        SELECT 1 FROM "anv".reference_locale
        WHERE english = 'Due to family circumstances'
          and uzbek = 'Oilaviy sabablarga ko''ra'
    );
update "anv".reference
set localeid = (select id
                from "anv".reference_locale
                where english = 'Due to family circumstances'
                  and uzbek = 'Oilaviy sabablarga ko''ra')
where code = 'ПО_СЕМЕЙНЫМ_ОБСТОЯТЕЛЬСТВАМ';


insert into "anv".reference_locale(english, uzbek)
SELECT 'Do not see myself in this position', 'O''zimni bu vazifada ko''ra olmayapman'
WHERE NOT EXISTS (
        SELECT 1 FROM "anv".reference_locale
        WHERE english = 'Do not see myself in this position'
          and uzbek = 'O''zimni bu vazifada ko''ra olmayapman'
    );
update "anv".reference
set localeid = (select id
                from "anv".reference_locale
                where english = 'Do not see myself in this position'
                  and uzbek = 'O''zimni bu vazifada ko''ra olmayapman')
where code = 'НЕ_ВИЖУ_СЕБЯ_НА_ЭТОЙ_ДОЛЖНОСТИ';