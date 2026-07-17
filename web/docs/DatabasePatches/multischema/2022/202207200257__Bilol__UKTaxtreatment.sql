insert into "anv".reference (code, name, isremovable, issystemreference) values ('_UK_TAX_TREATMENTS', 'UK Tax Treatments', false, true);

insert into "anv".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('UNITED_KINGDOM', 'United Kingdom',          (select id from "anv".reference r where r.code = '_UK_TAX_TREATMENTS'), 1, false, true);
insert into "anv".reference (code, name, parentid, sorder, isremovable, issystemreference) values ('OVERSEAS', 'Overseas',          (select id from "anv".reference r where r.code = '_UK_TAX_TREATMENTS'), 2, false, true);
