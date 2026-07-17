-- In order to calculate correctly, we need to change qty type to numeric(25, 10) from 20, 5
ALTER TABLE "anv".invoiceitem
    ALTER COLUMN qty TYPE numeric(25, 10);