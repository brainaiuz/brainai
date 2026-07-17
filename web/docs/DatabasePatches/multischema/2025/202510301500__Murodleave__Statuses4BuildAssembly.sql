
INSERT INTO "anv".reference(code, name, isactive, issystemreference, shared, deleted,parentid)
VALUES ('BUILD_ASSEMBLY_STATUS', 'Build Assembly status', true, true,true,false, null );

INSERT INTO "anv".reference(code, name, isactive, issystemreference, shared, deleted,parentid)
VALUES ('BUILD_ASSEMBLY_STATUS_APPROVED', 'Approved', true, true,true,false,(select id from "anv".reference where code = 'BUILD_ASSEMBLY_STATUS' limit 1));

INSERT INTO "anv".reference(code, name, isactive, issystemreference, shared, deleted,parentid)
VALUES ('BUILD_ASSEMBLY_STATUS_REJECTED', 'Rejected', true, true, true, false, (select id from "anv".reference where code = 'BUILD_ASSEMBLY_STATUS' limit 1));

INSERT INTO "anv".reference(code, name, isactive, issystemreference, shared, deleted,parentid)
VALUES ('BUILD_ASSEMBLY_STATUS_SUBMITTED', 'Submitted', true, true, true, false, (select id from "anv".reference where code = 'BUILD_ASSEMBLY_STATUS' limit 1));

INSERT INTO "anv".reference(code, name, isactive, issystemreference, shared, deleted,parentid)
VALUES ('BUILD_ASSEMBLY_STATUS_DRAFT', 'Draft', true, true, true, false, (select id from "anv".reference where code = 'BUILD_ASSEMBLY_STATUS' limit 1));
