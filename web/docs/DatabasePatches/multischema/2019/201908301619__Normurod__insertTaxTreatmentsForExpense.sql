insert into "0".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('NON_VAT', 'Non Vat',          (select id from "0".reference r where r.code = '_TAX_TREATMENT'), 6, false, true);
insert into "0".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('OUT_OF_SCOPE', 'Out of Scope',    (select id from "0".reference r where r.code = '_TAX_TREATMENT'), 7, false, true);

update "0".reference set sorder = 8 where code = 'VAT_REGISTERED_DESIGNATED_ZONE';
update "0".reference set sorder = 9 where code = 'NON_VAT_REGISTERED_DESIGNATED_ZONE';

insert into "anv".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('NON_VAT', 'Non Vat',          (select id from "anv".reference r where r.code = '_TAX_TREATMENT'), 6, false, true);
insert into "anv".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('OUT_OF_SCOPE', 'Out of Scope',    (select id from "anv".reference r where r.code = '_TAX_TREATMENT'), 7, false, true);
update "anv".reference set sorder = 8 where code = 'VAT_REGISTERED_DESIGNATED_ZONE';
update "anv".reference set sorder = 9 where code = 'NON_VAT_REGISTERED_DESIGNATED_ZONE';