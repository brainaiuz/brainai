delete
from "anv".modelfield
where field_id = 'DISCOUNT_TYPE'
  and form_id = 'PRODUCT';

delete
from "anv".modelfield
where field_id = 'DISCOUNT_AMOUNT'
  and form_id = 'PRODUCT';


insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('PRODUCT', 'DISCOUNT_TYPE', false, false, 'COL_2', 'FINANCIAL_INFORMATION', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('PRODUCT', 'DISCOUNT_AMOUNT', false, false, 'COL_2', 'FINANCIAL_INFORMATION', 1);