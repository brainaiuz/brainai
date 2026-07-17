UPDATE "anv".uploadamazonsettings
SET expireDate = CASE
                     WHEN expireDate > '2025-01-01' THEN '2024-01-25 00:00:00'
                     ELSE expireDate
    END;
