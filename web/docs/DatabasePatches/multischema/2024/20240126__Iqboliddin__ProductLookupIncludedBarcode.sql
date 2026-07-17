delete
from "anv".genericSettings
where key = 'PRODUCT_LOOKUP_BARCODE_INCLUDED';

insert into "anv".genericSettings (key, value)
values ('PRODUCT_LOOKUP_BARCODE_INCLUDED', 'YES');