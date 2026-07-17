
CREATE SEQUENCE IF NOT EXISTS "anv".transaction_id_man_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;
SELECT setval('"anv".transaction_id_man_seq', (SELECT MAX(id) FROM  "anv".transaction));


ALTER TABLE "anv".uploadamazonsettings ALTER COLUMN accessKey TYPE TEXT;

ALTER TABLE "anv".uploadamazonsettings ALTER COLUMN fileLink TYPE TEXT;