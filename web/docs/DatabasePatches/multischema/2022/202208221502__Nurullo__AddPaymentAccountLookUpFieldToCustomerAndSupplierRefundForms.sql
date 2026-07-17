update "anv".modelfield
set field_id='BANK_ACCOUNT'
where field_id = 'PAYMENT_ACCOUNT_LOOKUP'
  and form_id = 'CUSTOMER_REFUND';
update "anv".modelfield
set field_id='BANK_ACCOUNT'
where field_id = 'PAYMENT_ACCOUNT_LOOKUP'
  and form_id = 'SUPPLIER_REFUND';