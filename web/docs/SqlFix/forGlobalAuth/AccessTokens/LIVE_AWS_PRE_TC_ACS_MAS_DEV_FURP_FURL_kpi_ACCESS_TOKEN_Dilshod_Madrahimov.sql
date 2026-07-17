CREATE TABLE apiaccesstoken
(
  id                    SERIAL                 NOT NULL,
  token           CHARACTER VARYING(255)        UNIQUE,
  description             CHARACTER VARYING(255) NOT NULL,
  blocked              boolean                 DEFAULT FALSE
) WITH (
OIDS = FALSE
);

ALTER TABLE apiaccesstoken OWNER TO wftauth;