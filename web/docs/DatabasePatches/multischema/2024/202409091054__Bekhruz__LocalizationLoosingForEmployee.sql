insert into "anv".reference_locale(english, uzbek)
SELECT 'Not satisfied with the salary', 'Maoshidan norozi'
WHERE NOT EXISTS (
    SELECT 1 FROM "anv".reference_locale
    WHERE english = 'Not satisfied with the salary'
      and uzbek = 'Maoshidan norozi'
);
update "anv".reference
set localeid = (select id
                from "anv".reference_locale
                where english = 'Not satisfied with the salary'
                  and uzbek = 'Maoshidan norozi')
where code = 'НЕ_УСТРАИВАЕТ_ЗАРАБОТНАЯ_ПЛАТА';


insert into "0".reference_locale(english, uzbek)
SELECT 'Not satisfied with the salary', 'Maoshidan norozi'
WHERE NOT EXISTS (
    SELECT 1 FROM "0".reference_locale
    WHERE english = 'Not satisfied with the salary'
      and uzbek = 'Maoshidan norozi'
);
update "0".reference
set localeid = (select id
                from "0".reference_locale
                where english = 'Not satisfied with the salary'
                  and uzbek = 'Maoshidan norozi')
where code = 'НЕ_УСТРАИВАЕТ_ЗАРАБОТНАЯ_ПЛАТА';



