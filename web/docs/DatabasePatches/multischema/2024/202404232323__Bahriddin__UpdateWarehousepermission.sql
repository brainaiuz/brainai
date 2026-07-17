INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
SELECT 'ACCOUNTING_WAREHOUSES_SEE_ALL', 'ALLOW', 'MEM'
WHERE NOT EXISTS (SELECT 1
                  FROM "anv".rolepermission
                  WHERE permissioncode = 'ACCOUNTING_WAREHOUSES_SEE_ALL'
                    AND rolecode = 'MEM');