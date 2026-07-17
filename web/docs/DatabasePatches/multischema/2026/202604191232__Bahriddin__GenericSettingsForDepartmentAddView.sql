-- Remove old setting if it exists
DELETE FROM "anv".genericsettings
WHERE key = 'REMOVE_LOCATION_DEPARTMENT_VALIDATION_DEPARTMENT_ADD_VIEW';

-- Add new setting only if it does not exist
INSERT INTO "anv".genericsettings(key, value)
SELECT 'LOCATION_DEPARTMENT_VALIDATION_DEPARTMENT_ADD_VIEW', 'YES'
WHERE NOT EXISTS (
    SELECT 1
    FROM "anv".genericsettings
    WHERE key = 'LOCATION_DEPARTMENT_VALIDATION_DEPARTMENT_ADD_VIEW'
);