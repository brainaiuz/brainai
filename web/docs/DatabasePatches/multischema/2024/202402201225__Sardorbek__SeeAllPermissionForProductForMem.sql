INSERT INTO "anv".rolepermission (permissioncode, access, rolecode)
SELECT 'PRODUCT_SEE_ALL', 'ALLOW', code
FROM "anv".role
WHERE active = true
  AND (isdeleted IS NULL OR isdeleted IS NOT TRUE);