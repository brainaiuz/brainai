DELETE FROM "0".genericsettings WHERE key = 'ENABLE_TO_SHOW_SAMPLE_DATA';
DELETE FROM "anv".genericsettings WHERE key = 'ENABLE_TO_SHOW_SAMPLE_DATA';

INSERT INTO "0".genericsettings(key, value) VALUES ('ENABLE_TO_SHOW_SAMPLE_DATA', 'YES');
INSERT INTO "anv".genericsettings(key, value) VALUES ('ENABLE_TO_SHOW_SAMPLE_DATA', 'YES');
