
update company set selectFunctioncolumn =(select setval('"anv".property_id_seq', (select max(id) from "anv".property))) where id=(select id from company limit 1);