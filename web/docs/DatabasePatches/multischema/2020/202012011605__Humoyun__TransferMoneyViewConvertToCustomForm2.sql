
insert into "anv".model (active, formid, title, viewname)  values
(true,  'TRANSFER_MONEY_VIEW',  'Transfer Money', 'TransferMoney');

insert into "anv".customformsection
(form_id,            section) values
('TRANSFER_MONEY_VIEW',	  'INFORMATION');

delete from "anv".modelfield where form_id = 'TRANSFER_MONEY_VIEW' ;

insert into "anv".modelfield
(form_id,                 fsection,           section,          nolabelfor,   fieldstyle,      columntype,      mandatory,    widget,          forder,     field_id) values
('TRANSFER_MONEY_VIEW',	  'INFORMATION',      'INFORMATION',      '',         'field',         'COL_1',	        false,        'DatePicker',     1,	       'TRANSFER_MONEY_DATE'),
('TRANSFER_MONEY_VIEW',	  'INFORMATION',      'INFORMATION',      '',         'field',         'COL_1',	        false,        'LOOKUP',         2,         'FROM_ACCOUNT_LOOKUP'),
('TRANSFER_MONEY_VIEW',	  'INFORMATION',      'INFORMATION',      '',         'field',         'COL_1',	        false,        'LOOKUP',         3,         'TO_ACCOUNT_LOOKUP'),
('TRANSFER_MONEY_VIEW',	  'INFORMATION',      'INFORMATION',      '',         'field',         'COL_1',	        false,        'TextBox',        4,         'REFERENCE'),
('TRANSFER_MONEY_VIEW',	  'INFORMATION',      'INFORMATION',      '',         'field',         'COL_2',	        false,        'TextBox',        5,         'TRANSFER_MONEY_AMOUNT'),
('TRANSFER_MONEY_VIEW',	  'INFORMATION',      'INFORMATION',      '',         'field',         'COL_2',	        false,        'DataListBox',    6 ,        'TRANSFER_MONEY_AMOUNT_CURRENCY'),
('TRANSFER_MONEY_VIEW',	  'INFORMATION',      'INFORMATION',      '',         'field',         'COL_2',	        false,        'CurrencyWidget', 7 ,        'TRANSFER_MONEY_CURRENCY');
