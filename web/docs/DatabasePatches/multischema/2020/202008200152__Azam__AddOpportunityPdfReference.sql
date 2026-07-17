update company set selectFunctioncolumn =(select setval('"anv".pdfreference_id_seq', (select max(id) from "anv".pdfreference))) where id=(select id from company limit 1);

delete from "anv".pdfreference where code = 'OPPORTUNITY';
insert into "anv".pdfreference (code, name) values ('OPPORTUNITY', 'Opportunity');