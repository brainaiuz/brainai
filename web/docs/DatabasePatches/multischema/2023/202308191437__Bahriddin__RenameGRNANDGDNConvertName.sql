UPDATE permission
SET name = 'Convert'
WHERE code = 'ACCOUNTING_GDN_CONVERT_TO_INVOICE'
  AND context = 'ACCOUNTING'
  AND modulecode = 'SALES_ORDERS';


UPDATE permission
SET name = 'Convert'
WHERE code = 'ACCOUNTING_GRN_CONVERT_TO_INVOICE'
  AND context = 'ACCOUNTING'
  AND modulecode = 'PURCHASE_ORDERS';
