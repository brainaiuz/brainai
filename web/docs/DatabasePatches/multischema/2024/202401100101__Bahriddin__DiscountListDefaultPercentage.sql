
-- Need to set default discount ( by default it percentage ) ONE_OFF_FIXED_AMOUNT
UPDATE "anv".invoicingSettings
SET
    defDiscountSO = CASE WHEN defDiscountSO IS NULL THEN 0 ELSE defDiscountSO END,
    defDiscountSQ = CASE WHEN defDiscountSQ IS NULL THEN 0 ELSE defDiscountSQ END,
    defDiscountSI = CASE WHEN defDiscountSI IS NULL THEN 0 ELSE defDiscountSI END,
    defDiscountPO = CASE WHEN defDiscountPO IS NULL THEN 0 ELSE defDiscountPO END,
    defDiscountPI = CASE WHEN defDiscountPI IS NULL THEN 0 ELSE defDiscountPI END;