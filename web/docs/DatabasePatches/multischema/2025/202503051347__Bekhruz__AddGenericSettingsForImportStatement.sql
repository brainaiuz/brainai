DELETE FROM "0".genericsettings WHERE key = 'IMPORT_STATEMT_ENABLED';
DELETE FROM "anv".genericsettings WHERE key = 'IMPORT_STATEMT_ENABLED';

INSERT INTO "0".genericsettings(key, value) VALUES ('IMPORT_STATEMT_ENABLED', 'NO');
INSERT INTO "anv".genericsettings(key, value) VALUES ('IMPORT_STATEMT_ENABLED', 'NO');
