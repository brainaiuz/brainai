UPDATE "anv".CompanyPayrollSettings
SET value = (
    SELECT id
    FROM "anv".reference
    WHERE code = 'OTHERS_INDUSTRY'
    LIMIT 1
)
WHERE key = 'INDUSTRY_ID'
  AND value IS NOT NULL;