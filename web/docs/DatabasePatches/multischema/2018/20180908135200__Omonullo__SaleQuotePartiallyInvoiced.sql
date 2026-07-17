insert into "0".reference (code, name, deleted, isRemovable, isSystemReference, shared, isCustomButton, isActive, attendanceLR, autoApprove, hasProrata, parentid)
values('PARTIAL_INVOICED', 'Partially Invoiced', false, false, true, true, false, true, false, false, false,
(select id from "0".reference where code='INVOICE_STATUS'));