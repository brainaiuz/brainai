update "anv".modelfield
set forder = forder * 3
where forder > 1;

delete
from "anv".modelfield
where form_id = 'PRODUCT_CATEGORY_FORM'
  and field_id = 'CODE';

insert into "anv".modelfield (form_id, fsection, section, fieldstyle, fullwidth, hide, columntype, mandatory, sectionstyle, widget, forder, field_id)
values ('PRODUCT_CATEGORY_FORM', 'PRODUCT_CATEGORY_TITLE', 'PRODUCT_CATEGORY_TITLE', 'field', false, false, 'COL_1', false, '', 'UNKNOWN', 1, 'CODE');

