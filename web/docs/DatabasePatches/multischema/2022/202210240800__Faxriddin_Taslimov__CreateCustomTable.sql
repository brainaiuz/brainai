



CREATE TABLE "anv".balancedaterates
(
   id serial,
   currencyid integer,
   balancedate date,
   rate numeric(25,15),
   CONSTRAINT balancedaterates_pkey PRIMARY KEY (id)
)
WITH (
  OIDS = FALSE
)
;
ALTER TABLE "anv".balancedaterates
  OWNER TO wfmtest;
