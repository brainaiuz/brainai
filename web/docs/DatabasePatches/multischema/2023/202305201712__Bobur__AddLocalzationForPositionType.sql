WITH inserted_data AS (
INSERT
INTO "anv".reference_locale (russian, english, uzbek, arabic)
VALUES ('Штатное ', 'Place Count', 'Shtat', 'Place Count')
    RETURNING id
    )
UPDATE "anv".Reference rc
SET localeid = inserted_data.id FROM inserted_data
WHERE rc.code = 'TYPE_INTERNAL'
  AND rc.parentid = (
    SELECT rp.id
    FROM "anv".Reference rp
    WHERE rp.code = 'POSITION_TYPE'
    ORDER BY rp.id DESC
    LIMIT 1
    );

WITH inserted_data AS (
INSERT
INTO "anv".reference_locale (russian, english, uzbek, arabic)
VALUES ('Внештатное', 'Freelance', 'Shtatdan tashqari', 'Freelance')
    RETURNING id
    )
UPDATE "anv".Reference rc
SET localeid = inserted_data.id FROM inserted_data
WHERE rc.code = 'TYPE_EXTERNAL'
  AND rc.parentid = (
    SELECT rp.id
    FROM "anv".Reference rp
    WHERE rp.code = 'POSITION_TYPE'
    ORDER BY rp.id DESC
    LIMIT 1
    );
