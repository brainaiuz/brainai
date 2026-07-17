-- Not satisfied with the salary
WITH ref AS (
    SELECT id, localeid FROM "anv".reference WHERE code = 'НЕ_УСТРАИВАЕТ_ЗАРАБОТНАЯ_ПЛАТА' AND changed = false
), new_locale AS (
    INSERT INTO "anv".reference_locale (english, russian, uzbek, arabic)
    SELECT 'Not satisfied with the salary', 'Не устраивает заработная плата', 'Maoshidan norozi', 'غير راضٍ عن الراتب'
    WHERE EXISTS (SELECT 1 FROM ref WHERE localeid IS NULL)
    RETURNING id
), fill_existing AS (
    UPDATE "anv".reference_locale rl
    SET english = COALESCE(rl.english, 'Not satisfied with the salary'),
        russian = COALESCE(rl.russian, 'Не устраивает заработная плата'),
        uzbek   = COALESCE(rl.uzbek, 'Maoshidan norozi'),
        arabic  = COALESCE(rl.arabic, 'غير راضٍ عن الراتب')
    FROM ref
    WHERE rl.id = ref.localeid
)
UPDATE "anv".reference SET localeid = (SELECT id FROM new_locale) WHERE id IN (SELECT id FROM ref WHERE localeid IS NULL);

-- Conflict with the manager
WITH ref AS (
    SELECT id, localeid FROM "anv".reference WHERE code = 'КОНФОИКТ_С_РУКОВОДИТЕЛЕМ' AND changed = false
), new_locale AS (
    INSERT INTO "anv".reference_locale (english, russian, uzbek, arabic)
    SELECT 'Conflict with the manager', 'Конфликт с руководителем', 'Rahbar bilan ziddiyat', 'خلاف مع المدير'
    WHERE EXISTS (SELECT 1 FROM ref WHERE localeid IS NULL)
    RETURNING id
), fill_existing AS (
    UPDATE "anv".reference_locale rl
    SET english = COALESCE(rl.english, 'Conflict with the manager'),
        russian = COALESCE(rl.russian, 'Конфликт с руководителем'),
        uzbek   = COALESCE(rl.uzbek, 'Rahbar bilan ziddiyat'),
        arabic  = COALESCE(rl.arabic, 'خلاف مع المدير')
    FROM ref
    WHERE rl.id = ref.localeid
)
UPDATE "anv".reference SET localeid = (SELECT id FROM new_locale) WHERE id IN (SELECT id FROM ref WHERE localeid IS NULL);

-- Cannot find common ground within the team
WITH ref AS (
    SELECT id, localeid FROM "anv".reference WHERE code = 'НЕ_МОГУ_НАЙТИ_ОБЩИЙ_ЯЗЫК_В_КОМАНДЕ' AND changed = false
), new_locale AS (
    INSERT INTO "anv".reference_locale (english, russian, uzbek, arabic)
    SELECT 'Cannot find common ground within the team', 'Не могу найти общий язык в команде', 'Jamoada umumiy til topisha olmayapman', 'عدم التفاهم مع الفريق'
    WHERE EXISTS (SELECT 1 FROM ref WHERE localeid IS NULL)
    RETURNING id
), fill_existing AS (
    UPDATE "anv".reference_locale rl
    SET english = COALESCE(rl.english, 'Cannot find common ground within the team'),
        russian = COALESCE(rl.russian, 'Не могу найти общий язык в команде'),
        uzbek   = COALESCE(rl.uzbek, 'Jamoada umumiy til topisha olmayapman'),
        arabic  = COALESCE(rl.arabic, 'عدم التفاهم مع الفريق')
    FROM ref
    WHERE rl.id = ref.localeid
)
UPDATE "anv".reference SET localeid = (SELECT id FROM new_locale) WHERE id IN (SELECT id FROM ref WHERE localeid IS NULL);

-- Lack of career growth
WITH ref AS (
    SELECT id, localeid FROM "anv".reference WHERE code = 'НЕТ_КАРЬЕРНОГО_РОСТА' AND changed = false
), new_locale AS (
    INSERT INTO "anv".reference_locale (english, russian, uzbek, arabic)
    SELECT 'Lack of career growth', 'Нет карьерного роста', 'Karyerada o''sish yo''q', 'عدم وجود نمو وظيفي'
    WHERE EXISTS (SELECT 1 FROM ref WHERE localeid IS NULL)
    RETURNING id
), fill_existing AS (
    UPDATE "anv".reference_locale rl
    SET english = COALESCE(rl.english, 'Lack of career growth'),
        russian = COALESCE(rl.russian, 'Нет карьерного роста'),
        uzbek   = COALESCE(rl.uzbek, 'Karyerada o''sish yo''q'),
        arabic  = COALESCE(rl.arabic, 'عدم وجود نمو وظيفي')
    FROM ref
    WHERE rl.id = ref.localeid
)
UPDATE "anv".reference SET localeid = (SELECT id FROM new_locale) WHERE id IN (SELECT id FROM ref WHERE localeid IS NULL);

-- Was offered a more interesting job
WITH ref AS (
    SELECT id, localeid FROM "anv".reference WHERE code = 'ПРЕДЛОЖИЛИ_РАБОТУ_ИНТЕРЕСНЕЕ' AND changed = false
), new_locale AS (
    INSERT INTO "anv".reference_locale (english, russian, uzbek, arabic)
    SELECT 'Was offered a more interesting job', 'Предложили работу интереснее', 'Qiziqroq ish taklif qilindi', 'تم عرض وظيفة أكثر إثارة للاهتمام'
    WHERE EXISTS (SELECT 1 FROM ref WHERE localeid IS NULL)
    RETURNING id
), fill_existing AS (
    UPDATE "anv".reference_locale rl
    SET english = COALESCE(rl.english, 'Was offered a more interesting job'),
        russian = COALESCE(rl.russian, 'Предложили работу интереснее'),
        uzbek   = COALESCE(rl.uzbek, 'Qiziqroq ish taklif qilindi'),
        arabic  = COALESCE(rl.arabic, 'تم عرض وظيفة أكثر إثارة للاهتمام')
    FROM ref
    WHERE rl.id = ref.localeid
)
UPDATE "anv".reference SET localeid = (SELECT id FROM new_locale) WHERE id IN (SELECT id FROM ref WHERE localeid IS NULL);

-- Due to family circumstances
WITH ref AS (
    SELECT id, localeid FROM "anv".reference WHERE code = 'ПО_СЕМЕЙНЫМ_ОБСТОЯТЕЛЬСТВАМ' AND changed = false
), new_locale AS (
    INSERT INTO "anv".reference_locale (english, russian, uzbek, arabic)
    SELECT 'Due to family circumstances', 'По семейным обстоятельствам', 'Oilaviy sabablarga ko''ra', 'لظروف عائلية'
    WHERE EXISTS (SELECT 1 FROM ref WHERE localeid IS NULL)
    RETURNING id
), fill_existing AS (
    UPDATE "anv".reference_locale rl
    SET english = COALESCE(rl.english, 'Due to family circumstances'),
        russian = COALESCE(rl.russian, 'По семейным обстоятельствам'),
        uzbek   = COALESCE(rl.uzbek, 'Oilaviy sabablarga ko''ra'),
        arabic  = COALESCE(rl.arabic, 'لظروف عائلية')
    FROM ref
    WHERE rl.id = ref.localeid
)
UPDATE "anv".reference SET localeid = (SELECT id FROM new_locale) WHERE id IN (SELECT id FROM ref WHERE localeid IS NULL);

-- Do not see myself in this position
WITH ref AS (
    SELECT id, localeid FROM "anv".reference WHERE code = 'НЕ_ВИЖУ_СЕБЯ_НА_ЭТОЙ_ДОЛЖНОСТИ' AND changed = false
), new_locale AS (
    INSERT INTO "anv".reference_locale (english, russian, uzbek, arabic)
    SELECT 'Do not see myself in this position', 'Не вижу себя на этой должности', 'O''zimni bu vazifada ko''ra olmayapman', 'لا أرى نفسي في هذا المنصب'
    WHERE EXISTS (SELECT 1 FROM ref WHERE localeid IS NULL)
    RETURNING id
), fill_existing AS (
    UPDATE "anv".reference_locale rl
    SET english = COALESCE(rl.english, 'Do not see myself in this position'),
        russian = COALESCE(rl.russian, 'Не вижу себя на этой должности'),
        uzbek   = COALESCE(rl.uzbek, 'O''zimni bu vazifada ko''ra olmayapman'),
        arabic  = COALESCE(rl.arabic, 'لا أرى نفسي في هذا المنصب')
    FROM ref
    WHERE rl.id = ref.localeid
)
UPDATE "anv".reference SET localeid = (SELECT id FROM new_locale) WHERE id IN (SELECT id FROM ref WHERE localeid IS NULL);

-- Other
WITH ref AS (
    SELECT id, localeid FROM "anv".reference WHERE code = 'ДРУГОЕ' AND changed = false
), new_locale AS (
    INSERT INTO "anv".reference_locale (english, russian, uzbek, arabic)
    SELECT 'Other', 'Другое', 'Boshqa', 'أخرى'
    WHERE EXISTS (SELECT 1 FROM ref WHERE localeid IS NULL)
    RETURNING id
), fill_existing AS (
    UPDATE "anv".reference_locale rl
    SET english = COALESCE(rl.english, 'Other'),
        russian = COALESCE(rl.russian, 'Другое'),
        uzbek   = COALESCE(rl.uzbek, 'Boshqa'),
        arabic  = COALESCE(rl.arabic, 'أخرى')
    FROM ref
    WHERE rl.id = ref.localeid
)
UPDATE "anv".reference SET localeid = (SELECT id FROM new_locale) WHERE id IN (SELECT id FROM ref WHERE localeid IS NULL);