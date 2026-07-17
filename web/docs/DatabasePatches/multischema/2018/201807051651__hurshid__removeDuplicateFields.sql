

delete from "anv".modelfield where id in (select max(id) from "anv".modelfield group by field_id, form_id having count(id)>1);
delete from "anv".modelfield where id in (select max(id) from "anv".modelfield group by field_id, form_id having count(id)>1);