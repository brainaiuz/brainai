update "anv".modelfield  set usableByWorkflow = true where form_id = 'RENTAL_PRODUCT_FORM' and field_id not in ('ITEMS', 'IMAGE_UPLOAD', 'ATTACHMENTS') ;
delete from "anv".reference where code = '_WORKFLOW_MODULE_RENTAL_PRODUCT';
insert into "anv".reference(code, name, sorder, parentid) values ('_WORKFLOW_MODULE_RENTAL_PRODUCT', 'Rental Product', 19, (select id from "anv".reference where code = '_WORKFLOW_MODULE' limit 1));

update "anv".modelfield set source = 'ACCOUNTING@RENTAL_PRODUCT_BRAND', widget = 'DROPDOWN' where form_id = 'RENTAL_PRODUCT_FORM' and field_id = 'BRAND';
update "anv".modelfield set source = 'ACCOUNTING@PURCHASE_ORDER_SUPPLIER', widget = 'LOOKUP' where form_id = 'RENTAL_PRODUCT_FORM' and field_id = 'SUPPLIERS';
update "anv".modelfield set source = 'ACCOUNTING@PRODUCT_CATEGORY', widget = 'DROPDOWN' where form_id = 'RENTAL_PRODUCT_FORM' and field_id = 'CATEGORY';
