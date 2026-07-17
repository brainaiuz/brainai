update company set selectFunctioncolumn =(select setval('"anv".pdfreference_id_seq', (select max(id) from "anv".pdfreference))) where id=(select id from company limit 1);

delete from "0".pdfreference where code = 'VACANCY';
delete from "anv".pdfreference where code = 'VACANCY';

insert into "0".pdfreference (code, name) values ('VACANCY', 'Vacancy');
insert into "anv".pdfreference (code, name) values ('VACANCY', 'Vacancy');