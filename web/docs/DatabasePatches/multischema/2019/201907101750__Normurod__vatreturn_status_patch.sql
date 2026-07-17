insert into "0".reference(code, name, deleted, issystemreference, shared) values('_VAT_RETURN_STATUS', 'VAT return status', false, true, true);

insert into "0".reference(code, name, deleted, issystemreference, shared, parentid) values
('FILED', 'Filed', false, true, true, (select id from "0".reference where code = '_VAT_RETURN_STATUS')),
('UNFILED', 'Unfiled', false, true, true, (select id from "0".reference where code = '_VAT_RETURN_STATUS'));

insert into "anv".reference(code, name, deleted, issystemreference, shared) values('_VAT_RETURN_STATUS', 'VAT return status', false, true, true);

insert into "anv".reference(code, name, deleted, issystemreference, shared, parentid) values
('FILED', 'Filed', false, true, true, (select id from "anv".reference where code = '_VAT_RETURN_STATUS')),
('UNFILED', 'Unfiled', false, true, true, (select id from "anv".reference where code = '_VAT_RETURN_STATUS'));