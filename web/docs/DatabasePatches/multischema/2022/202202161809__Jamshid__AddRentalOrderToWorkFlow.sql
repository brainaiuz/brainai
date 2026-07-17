update "anv".modelfield  set usableByWorkflow = true where form_id = 'RENTAL_ORDER_FORM' and field_id != 'ITEMS';
insert into "anv".reference(code, name, sorder, parentid) values ('_WORKFLOW_MODULE_RENTAL_ORDER', 'Rental Order', 19, (select id from "anv".reference where code = '_WORKFLOW_MODULE' limit 1));

update "anv".modelfield set source = 'ACCOUNTING@RFQ_CUSTOMER', widget = 'LOOKUP' where form_id = 'RENTAL_ORDER_FORM' and field_id = 'CUSTOMER';
update "anv".modelfield set source = 'ACCOUNTING@CLIENT_INVOICE_TERM', widget = 'DROPDOWN' where form_id = 'RENTAL_ORDER_FORM' and field_id = 'CLIENT_INVOICE_TERM';
update "anv".modelfield set source = 'ACCOUNTING@SALE_INVOICE_AMOUNT', widget = 'DROPDOWN' where form_id = 'RENTAL_ORDER_FORM' and field_id = 'TAX_CALC_TYPE';