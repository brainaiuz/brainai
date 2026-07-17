  delete from  modelfield where form_ID ='PAYROLL_CASH_ADVANCE_FORM';
  insert into modelfield(sorder, mandatory,    hide,  systemmandatory,   field_ID,                   section,         widget,                  noLabelFor,                      form_ID) values
                        (01,          true,   false,             true,   'DRIVER_NUMBER',          'DETAILS',       'LOOKUP',                          '',  'PAYROLL_CASH_ADVANCE_FORM'),
                        (02,          true,   false,             true,   'EMPLOYEE',               'DETAILS',       'LOOKUP',                          '',  'PAYROLL_CASH_ADVANCE_FORM'),
                        (03,          true,   false,             true,   'CATEGORY',               'DETAILS',      'UNKNOWN',                          '',  'PAYROLL_CASH_ADVANCE_FORM'),
                        (04,          true,   false,             true,   'REQUESTED_AMOUNT',       'DETAILS',      'TextBox',                          '',  'PAYROLL_CASH_ADVANCE_FORM'),
                        (05,         false,   false,            false,   'PAYMENT_TERMS',          'DETAILS',     'DropDown',                          '',  'PAYROLL_CASH_ADVANCE_FORM'),
                        (06,         false,   false,            false,   'PAYMENT_AMOUNT',   	     'DETAILS',      'TextBox',                          '',  'PAYROLL_CASH_ADVANCE_FORM'),
                        (07,          true,   false,             true,   'REQUESTED_DATE',         'DETAILS',   'DatePicker',                          '',  'PAYROLL_CASH_ADVANCE_FORM'),
                        (08,         false,   false,            false,   'PAYMENT_METHOD',         'DETAILS',     'DropDown',                          '',  'PAYROLL_CASH_ADVANCE_FORM'),
                        (09,         false,   false,            false,   'PAY_FROM',               'DETAILS',       'LOOKUP',                          '',  'PAYROLL_CASH_ADVANCE_FORM'),
                        (10,         false,   false,            false,   'EXCHANGE_RATE',          'DETAILS',      'UNKNOWN',                          '',  'PAYROLL_CASH_ADVANCE_FORM'),
                        (11,         false,   false,            false,   'CASH_ADVANCE_ACCOUNT',   'DETAILS',       'LOOKUP',                          '',  'PAYROLL_CASH_ADVANCE_FORM'),
                        (12,         false,   false,            false,   'APPROVER',               'DETAILS',      'UNKNOWN',                          '',  'PAYROLL_CASH_ADVANCE_FORM'),
                        (13,         false,   false,            false,   'PURPOSE',                'PURPOSE',      'UNKNOWN', 'addForm,editForm,viewForm',  'PAYROLL_CASH_ADVANCE_FORM'),
                        (14,         false,   false,            false,   'ATTACHMENTS',        'ATTACHMENTS',      'UNKNOWN', 'addForm,editForm,viewForm',  'PAYROLL_CASH_ADVANCE_FORM');

UPDATE modelfield set sectionStyle = 'slideDown-box  group expand hideCustomField' where form_id='PAYROLL_CASH_ADVANCE_FORM';
UPDATE modelfield set fieldSetStyle = 'slideDown-content group labelLine' where form_id='PAYROLL_CASH_ADVANCE_FORM';
UPDATE modelfield set fieldSetStyle = 'slideDown-content group nobrd' WHERE noLabelFor is not null AND noLabelFor != '' AND form_id='PAYROLL_CASH_ADVANCE_FORM';
UPDATE modelfield set halfSetStyle = 'halfSet-1 left' where form_id='PAYROLL_CASH_ADVANCE_FORM';
UPDATE modelfield set halfSetStyle = '' WHERE noLabelFor is not null AND noLabelFor != '' AND form_id='PAYROLL_CASH_ADVANCE_FORM';
UPDATE modelfield set rowStyle = 'row hideCustomField' where form_id='PAYROLL_CASH_ADVANCE_FORM';
UPDATE modelfield set fieldStyle = 'field' where form_id='PAYROLL_CASH_ADVANCE_FORM';
