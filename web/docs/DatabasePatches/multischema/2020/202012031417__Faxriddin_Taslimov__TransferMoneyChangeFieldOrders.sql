delete
from "anv".modelfield
where form_id = 'TRANSFER_MONEY_VIEW';

insert into "anv".modelfield (form_id, fsection, section, nolabelfor, fieldstyle, columntype, mandatory, widget, forder, field_id)
values ('TRANSFER_MONEY_VIEW', 'INFORMATION', 'INFORMATION', '', 'field', 'COL_1', false, 'LOOKUP', 1, 'FROM_ACCOUNT_LOOKUP'),
       ('TRANSFER_MONEY_VIEW', 'INFORMATION', 'INFORMATION', '', 'field', 'COL_1', false, 'LOOKUP', 2, 'TO_ACCOUNT_LOOKUP'),
       ('TRANSFER_MONEY_VIEW', 'INFORMATION', 'INFORMATION', '', 'field', 'COL_2', false, 'DatePicker', 3, 'TRANSFER_MONEY_DATE'),
       ('TRANSFER_MONEY_VIEW', 'INFORMATION', 'INFORMATION', '', 'field', 'COL_1', false, 'TextBox', 4, 'TRANSFER_MONEY_AMOUNT'),
       ('TRANSFER_MONEY_VIEW', 'INFORMATION', 'INFORMATION', '', 'field', 'COL_1', false, 'DataListBox', 5, 'TRANSFER_MONEY_AMOUNT_CURRENCY'),
       ('TRANSFER_MONEY_VIEW', 'INFORMATION', 'INFORMATION', '', 'field', 'COL_2', false, 'TextBox', 6, 'REFERENCE'),
       ('TRANSFER_MONEY_VIEW', 'INFORMATION', 'INFORMATION', '', 'field', 'COL_2', false, 'CurrencyWidget', 7, 'TRANSFER_MONEY_CURRENCY');
