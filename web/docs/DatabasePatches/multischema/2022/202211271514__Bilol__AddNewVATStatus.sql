insert into "anv".reference(code, name, deleted, issystemreference, shared, parentid)
values ('OPEN', 'Open', false, true, true, (select id from "anv".reference where code = '_VAT_RETURN_STATUS'));
