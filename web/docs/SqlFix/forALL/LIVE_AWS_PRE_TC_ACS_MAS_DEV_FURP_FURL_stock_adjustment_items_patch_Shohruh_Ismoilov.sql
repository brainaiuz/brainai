UPDATE "anv".adjustment_item SET newqty = greatest(0.0, qty), usedqty = -least(0.0, qty);
UPDATE "anv".adjustment_item SET qty = currentqty + newqty - usedqty WHERE currentqty NOTNULL;