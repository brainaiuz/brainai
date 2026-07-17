ALTER TABLE public.hostbasedsetting
  ADD COLUMN stripe_public_key character varying(255);
ALTER TABLE public.hostbasedsetting
  ADD COLUMN stripe_secret_key character varying(255);

--AWS
UPDATE public.hostbasedsetting SET stripe_public_key='pk_test_1TTzcqsxivyda68MwsgYd28g',
stripe_secret_key='sk_test_LjJzzD0OQE9RydPRgocD5oQf' WHERE hostname='aws.kpi.com';
--LIVE
UPDATE public.hostbasedsetting SET stripe_public_key='pk_live_g1eTESt6VWJ18AOt5SuEb77I',
stripe_secret_key='sk_live_rrbzM4EQYnWmoW9Sbv44zQte' WHERE hostname='app.kpi.com';