
ALTER TABLE "0".reportingpermission DROP CONSTRAINT  IF EXISTS reportingpermission_codeunique;
ALTER TABLE "0".reportingpermission ADD CONSTRAINT reportingpermission_codeunique UNIQUE (code);

ALTER TABLE "anv".reportingpermission DROP CONSTRAINT  IF EXISTS reportingpermission_codeunique;
ALTER TABLE "anv".reportingpermission ADD CONSTRAINT reportingpermission_codeunique UNIQUE (code);