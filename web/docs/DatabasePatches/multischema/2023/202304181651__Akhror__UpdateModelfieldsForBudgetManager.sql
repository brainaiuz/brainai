update "anv".modelfield
set widgetforbm = 'DATE'
where form_id = 'PURCHASEORDER_FORM'
  and field_id in ('DUE_DATE', 'INVOICE_DATE');
update "anv".modelfield
set widgetforbm = 'AMOUNT'
where form_id = 'PURCHASEORDER_FORM'
  and field_id in ('SUB_TOTAL', 'TOTAL', 'TOTAL_DISCOUNT', 'TOTAL_INVOICE_CURRENCY');
update "anv".modelfield
set widgetforbm = 'AMOUNT'
where form_id = 'PRODUCT_FORM'
  and field_id in ('COST_PRICE', 'SELLING_PRICE');
update "anv".modelfield
set widgetforbm = 'DATE'
where form_id = 'HRMS_EMPLOYEE_FORM'
  and field_id in ('BIRTH_DAY', 'HIRE_DATE', 'INSURANCE_EXPIRY_DATE', 'PASSPORT_EXPIRY_DATE', 'PASSPORT_ISSUE_DATE',
                   'RESIGNATION_DATE', 'VISA_EXPIRATION_DATE', 'VISA_ISSUE_DATE');
update "anv".modelfield
set widgetforbm = 'AMOUNT'
where form_id = 'HRMS_EMPLOYEE_FORM'
  and field_id in ('CLIENT_CHARGE_RATE', 'SALARY_AMOUNT', 'SALARY_TOTAL_AMOUNT', 'WAGE_RATE');
update "anv".modelfield
set widgetforbm = 'DATE'
where form_id = 'CLIENT_FORM'
  and field_id in ('CLIENT_AS_OF_DATE');
update "anv".modelfield
set widgetforbm = 'AMOUNT'
where form_id = 'CLIENT_FORM'
  and field_id in ('CLIENT_AMOUNT', 'CLIENT_DISCOUNT');
update "anv".modelfield
set widgetforbm = 'DATE'
where form_id = 'OPPORTUNITY_FORM'
  and field_id in ('CRM_OPPORTUNITY_CLOSING_DATE');
update "anv".modelfield
set widgetforbm = 'AMOUNT'
where form_id = 'OPPORTUNITY_FORM'
  and field_id in ('CRM_OPPORTUNITY_AMOUNT', 'CRM_OPPORTUNITY_EXPECTED_REVENUE', 'CRM_OPPORTUNITY_PROBABILITY');
