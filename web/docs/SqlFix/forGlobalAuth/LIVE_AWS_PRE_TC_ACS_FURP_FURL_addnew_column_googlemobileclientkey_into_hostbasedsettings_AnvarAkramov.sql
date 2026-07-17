ALTER TABLE public.hostbasedsetting
  ADD COLUMN google_client_mobile_key character varying(255);

--AWS
UPDATE public.hostbasedsetting SET google_client_mobile_key='95827898397-u20i3mf21hsacunnbcasm0p6nrqul4o0.apps.googleusercontent.com' WHERE hostname='aws.kpi.com'; -- Клиент iOS Beta
--LIVE
UPDATE public.hostbasedsetting SET google_client_mobile_key='95827898397-u20i3mf21hsacunnbcasm0p6nrqul4o0.apps.googleusercontent.com' WHERE hostname='app.kpi.com'; --we need to use Клиент iOS Beta or create new one
