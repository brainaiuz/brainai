

INSERT INTO "anv".reference(code, name, isactive, issystemreference, shared, deleted,parentid)
VALUES ('BUILD_ASSEMBLY_STATUS_UNBUILD', 'Unbuilt', true, true, true, false, (select id from "anv".reference where code = 'BUILD_ASSEMBLY_STATUS' limit 1));
