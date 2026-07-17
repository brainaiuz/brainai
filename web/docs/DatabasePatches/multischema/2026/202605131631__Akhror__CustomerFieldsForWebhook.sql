update "anv".modelfield
set usablebyworkflow = true
where form_id = 'CLIENT_FORM'
  and field_id in ('CRM_ACCOUNT_NAME', 'CRM_ACCOUNT_BILLING_ADDRESS');