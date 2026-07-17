ALTER TABLE public.apiaccesstoken
    ADD COLUMN modulecode character varying;


update public.apiaccesstoken set modulecode='HRMS_MODULE' where token='511171d4-9c52-4d09-812c-ac351c80c51e';
update public.apiaccesstoken set modulecode='ACCOUNTING_MODULE' where token='a8962d55-427f-4adc-8db0-0d8d69e9734f';
update public.apiaccesstoken set modulecode='PM' whrere token='dcfd5ead-cd18-417e-9f77-b5757a65d8a6';
update public.apiaccesstoken set modulecode='ACCOUNTING_MODULE' where token='a0cf7104-6507-411d-b673-a371a5921f56';
update public.apiaccesstoken set modulecode='ACCOUNTING_MODULE' where token='190ea109-c549-47fd-8663-8d2fc115bfef';
update public.apiaccesstoken set modulecode='HRMS_MODULE' where token='8972548b-ec32-4ddf-bf81-81b01121cd3c';