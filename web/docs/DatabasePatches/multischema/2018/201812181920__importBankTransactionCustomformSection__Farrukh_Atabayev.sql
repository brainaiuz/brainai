delete from "0".customformsection where form_id='IMPORT_BANK_TRANSACTION_FORM';
INSERT INTO "0".customformsection (active, custom, form_id, section, sorder) VALUES (true, false, 'IMPORT_BANK_TRANSACTION_FORM', 'REQUIRED_INFORMATIONS', 0);
INSERT INTO "0".customformsection (active, custom, form_id, section, sorder) VALUES (true, false, 'IMPORT_BANK_TRANSACTION_FORM', 'OPTIONAL_INFORMATIONS', 1);

delete from "anv".customformsection where form_id='IMPORT_BANK_TRANSACTION_FORM';
INSERT INTO "anv".customformsection (active, custom, form_id, section, sorder) VALUES (true, false, 'IMPORT_BANK_TRANSACTION_FORM', 'REQUIRED_INFORMATIONS', 0);
INSERT INTO "anv".customformsection (active, custom, form_id, section, sorder) VALUES (true, false, 'IMPORT_BANK_TRANSACTION_FORM', 'OPTIONAL_INFORMATIONS', 1);