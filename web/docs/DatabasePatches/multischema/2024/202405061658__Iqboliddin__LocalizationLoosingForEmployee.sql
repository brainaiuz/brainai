insert into "anv".reference_locale(english, uzbek)
SELECT 'Not satisfied with the salary', 'Maoshidan qoniqmagan'
WHERE NOT EXISTS (
        SELECT 1 FROM "anv".reference_locale
        WHERE english = 'Not satisfied with the salary'
          and uzbek = 'Maoshidan qoniqmagan'
    );
update "anv".reference
set localeid = (select id
                from "anv".reference_locale
                where english = 'Not satisfied with the salary'
                  and uzbek = 'Maoshidan qoniqmagan')
where code = 'НЕ_УСТРАИВАЕТ_ЗАРАБОТНАЯ_ПЛАТА';