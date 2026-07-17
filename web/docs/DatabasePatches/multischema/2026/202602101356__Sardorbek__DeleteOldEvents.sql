DELETE
FROM "anv".businessevent
WHERE time < (NOW() - INTERVAL '3 hour');