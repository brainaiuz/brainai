delete from "anv".pdfreference where code='STOCK_ADJUSTMENT';

SELECT setval('"anv".pdfreference_id_seq', max(id)) FROM "anv".pdfreference;

insert into "anv".pdfreference (code,name) values ('STOCK_ADJUSTMENT', 'Stock Adjustment');

