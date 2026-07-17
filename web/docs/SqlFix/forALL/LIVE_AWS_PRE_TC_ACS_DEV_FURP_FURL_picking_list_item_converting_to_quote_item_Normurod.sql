UPDATE "anv".quoteitem qi SET pickable = true, numberOfPacks = sq.numberOfPacks
FROM (SELECT qi.id, pli.numberOfPacks FROM "anv".picklistitem pli
  INNER JOIN "anv".picklist pl on pl.id = pli.picklist_id
  INNER JOIN "anv".quoteitem qi on qi.item_id = pli.item_id
  WHERE pl.salequote_id = qi.quote_id) sq WHERE qi.id = sq.id;