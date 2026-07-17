INSERT INTO "anv".reference_locale (arabic, english, russian, uzbek)
VALUES ('استقال', 'Resigned', 'Уволенный', 'Istefoga chiqqan');

UPDATE "anv".leave_reason
SET localeid = (SELECT "id" FROM "65159".reference_locale where "english" = 'Resigned' and "russian" = 'Уволенный')
WHERE "code" = 'LR_TYPE_RESIGNED';