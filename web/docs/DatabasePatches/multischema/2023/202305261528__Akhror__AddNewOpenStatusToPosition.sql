insert into "anv".reference(code, isactive, name, sorder, parentid)
values ('POS_STATUS_OPEN', true, 'Open', 3, (select id from "anv".reference where code = 'POS_STATUS' limit 1) );