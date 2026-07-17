/*Run it before updating schema*/

ALTER TABLE public.paypalitem
  RENAME TO subscription_payment;

ALTER SEQUENCE public.paypalitem_id_seq
  RENAME TO subscription_payment_id_seq;

ALTER TABLE public.subscription_payment
   ALTER COLUMN id SET DEFAULT nextval('subscription_payment_id_seq'::regclass);