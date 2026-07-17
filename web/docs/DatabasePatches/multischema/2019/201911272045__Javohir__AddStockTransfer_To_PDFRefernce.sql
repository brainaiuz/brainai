update company set selectFunctioncolumn =(select setval('"anv".pdfreference_id_seq', (select max(id) from "anv".pdfreference))) where id=(select id from company limit 1);

delete from "0".pdfreference where code = 'STOCK_TRANSFER';
delete from "anv".pdfreference where code = 'STOCK_TRANSFER';

insert into "0".pdfreference (code, name) values ('STOCK_TRANSFER', 'Stock Transfer');
insert into "anv".pdfreference (code, name) values ('STOCK_TRANSFER', 'Stock Transfer');