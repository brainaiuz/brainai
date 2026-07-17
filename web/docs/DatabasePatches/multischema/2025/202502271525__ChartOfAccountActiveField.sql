-- from the start all account must be active
update "anv".account
set active = true;

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder)
values ('CHART_OF_ACCOUNT_FORM', 'STATUS_OF_ACCOUNT', false, 'COL_2', 'GENERAL_INFORMATION', 1);