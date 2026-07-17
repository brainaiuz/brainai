insert into "anv".reference(code, name)
values ('_FAI_PURCHASE_VAT', 'FAI PURCHASE VAT');

insert into "anv".reference (code, name, sorder, parentid, leavedays)
    values ('SA_PU_TSC0150', 'VAT Claim 15%', 103, (select id from "anv".reference where code = '_FAI_PURCHASE_VAT'), 15);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
    values ('SA_PU_S150', '15% - Standard Purchases', 38, (select id from "anv".reference where code = '_FAI_PURCHASE_VAT'), 15);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
    values ('SA_PU_IMP0150', 'Import VAT Claim 15%', 117, (select id from "anv".reference where code = '_FAI_PURCHASE_VAT'), 15);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
    values ('SA_PU_II15', '15% - Imported Purchases', 83, (select id from "anv".reference where code = '_FAI_PURCHASE_VAT'), 15);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
    values ('SA_PU_IG15', '15% - Paid GCC Import', 44, (select id from "anv".reference where code = '_FAI_PURCHASE_VAT'), 15);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
    values ('SA_PU_INTXXXX', 'Internal Purchases', 164, (select id from "anv".reference where code = '_FAI_PURCHASE_VAT'), 0);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
    values ('SA_PU_R050', '5% - RCM Service', 48, (select id from "anv".reference where code = '_FAI_PURCHASE_VAT'), 0);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
    values ('SA_PU_R150', '15% - RCM Service', 46, (select id from "anv".reference where code = '_FAI_PURCHASE_VAT'), 0);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
    values ('SA_PU_C150', '15% - RCM Goods', 100, (select id from "anv".reference where code = '_FAI_PURCHASE_VAT'), 0);

insert into "anv".reference (code, name, sorder, parentid, leavedays)
    values ('SA_PU_NAAXXXX', 'Purchases OS', 135, (select id from "anv".reference where code = '_FAI_PURCHASE_VAT'), 0);
