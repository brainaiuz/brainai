--AWS
UPDATE public.hostbasedsetting SET stripe_public_key='pk_test_B7miOMoOzUYoOyBv4kXW1K2s',
stripe_secret_key='sk_test_TaXnavXCDJ3nMycvuZFHFXtU' -- WHERE hostname='aws.kpi.com';
--LIVE
UPDATE public.hostbasedsetting SET stripe_public_key='pk_live_g1eTESt6VWJ18AOt5SuEb77I',
stripe_secret_key='sk_live_rrbzM4EQYnWmoW9Sbv44zQte' WHERE hostname='app.kpi.com';