--initial

CREATE TABLE public.push_notification_token
(
   id serial,
   userid integer,
   token character varying(500),
   devicetype character varying(100),
   deleted boolean NOT NULL DEFAULT FALSE,
    CONSTRAINT push_notification_token_pkey PRIMARY KEY (id),
   FOREIGN KEY (userid) REFERENCES public.userauth (id) ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS = FALSE
)
;
ALTER TABLE public.push_notification_token
  OWNER TO wftauth;