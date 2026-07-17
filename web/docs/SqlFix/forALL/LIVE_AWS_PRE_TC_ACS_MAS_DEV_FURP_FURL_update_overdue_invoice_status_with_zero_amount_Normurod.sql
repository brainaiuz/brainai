UPDATE "anv".invoice SET status_id = (SELECT id FROM "anv".reference WHERE code = 'APPROVE')
 WHERE total = 0 and status_id = (SELECT id FROM "anv".reference WHERE code = 'OVER_DUE');