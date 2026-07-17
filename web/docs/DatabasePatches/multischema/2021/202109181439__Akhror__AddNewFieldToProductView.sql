delete from "anv".modelfield where form_id = 'PRODUCT' and field_id = 'SOLD_TO_CUSTOMERS';

INSERT INTO "anv".modelfield (field_id, form_id, type, columntype, forder, fsection)
VALUES ('SOLD_TO_CUSTOMERS', 'PRODUCT', 'text', 'COL_2', 2, 'FINANCIAL_INFORMATION');