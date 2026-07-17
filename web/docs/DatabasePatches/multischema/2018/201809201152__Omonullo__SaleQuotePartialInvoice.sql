insert into "0".reference (code, name, deleted, isRemovable, isSystemReference, shared, isCustomButton, isActive, attendanceLR, autoApprove, hasProrata, parentid)
    select 'PARTIAL_INVOICED', 'Partially Invoiced', false, false, true, true, false, true, false, false, false, (select id from "0".reference where code='INVOICE_STATUS')
    from "0".reference where not exists(select id from "0".reference where code = 'PARTIAL_INVOICED') limit 1;

insert into "anv".reference (code, name, deleted, isRemovable, isSystemReference, shared, isCustomButton, isActive, attendanceLR, autoApprove, hasProrata, parentid)
    select 'PARTIAL_INVOICED', 'Partially Invoiced', false, false, true, true, false, true, false, false, false, (select id from "anv".reference where code='INVOICE_STATUS')
    from "anv".reference where not exists(select id from "anv".reference where code = 'PARTIAL_INVOICED') limit 1;