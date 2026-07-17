

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('RENTAL_ORDER_FORM', 'APPROVERS', false, false, 'COL_3', 'INFORMATION', 3);

INSERT INTO "anv".reference(code, name, isactive, issystemreference, shared, deleted,parentid)
VALUES ('RENTAL_REJECTED', 'Rejected', true, true, true, false, (select id from "anv".reference where code = 'RENTAL_STATUS'));

INSERT INTO "anv".reference(code, name, isactive, issystemreference, shared, deleted,parentid)
VALUES ('RENTAL_SUBMITTED', 'Submitted', true, true, true, false, (select id from "anv".reference where code = 'RENTAL_STATUS'));

