UPDATE "anv".transaction_locking
SET lockdate = (SELECT blockBeforeDate FROM "anv".financialsettings),
    lock_modules = COALESCE(lock_modules, '{}'::jsonb) || '{
        "sales": {
            "module": "sales",
            "status": "locked",
            "description": null
        },
        "banking": {
            "module": "banking",
            "status": "locked",
            "description": null
        },
        "purchases": {
            "module": "purchases",
            "status": "locked",
            "description": null
        }
    }'::jsonb
WHERE lockdate IS NULL
   OR lockdate = '1970-01-01 00:00:00';


INSERT INTO "anv".transaction_locking (lockdate, lock_modules)
SELECT (SELECT blockBeforeDate FROM "anv".financialsettings),
       '{
           "sales": {
               "module": "sales",
               "status": "locked",
               "description": null
           },
           "banking": {
               "module": "banking",
               "status": "locked",
               "description": null
           },
           "purchases": {
               "module": "purchases",
               "status": "locked",
               "description": null
           }
       }'::jsonb
WHERE NOT EXISTS (SELECT 1 FROM "anv".transaction_locking);
