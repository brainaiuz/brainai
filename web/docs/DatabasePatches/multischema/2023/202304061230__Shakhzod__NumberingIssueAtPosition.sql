UPDATE "311555".position
SET intnumber = (SELECT COALESCE(MAX(SUBSTRING(numberdata, 4)::integer), 0)
                 FROM "311555".position
                 WHERE intnumber IS NULL
                 GROUP BY numberdata
                 ORDER BY numberdata DESC
    LIMIT 1
    )
WHERE isdeleted IS NOT TRUE
  AND (intnumber IS NULL)
  AND numberdata = (
    SELECT numberdata
    FROM "311555".position
    WHERE isdeleted IS NOT TRUE
    ORDER BY numberdata DESC
    LIMIT 1
    );