UPDATE "anv".stock_adjustment
SET type = 'STOCK_ADJUSTMENT'
WHERE type IS NULL
  AND (stocktransfer IS NULL OR stocktransfer = false);