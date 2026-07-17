-- In order to make correct calculation and give a client more comfort, quoteitem qty and convertedqty incereased to 10 from 5
ALTER TABLE "anv".quoteItem
    ALTER COLUMN qty TYPE numeric(25, 10);

ALTER TABLE "anv".quoteItem
    ALTER COLUMN convertedqty TYPE numeric(25, 10);