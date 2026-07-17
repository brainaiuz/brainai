delete from "anv".reference where parentid = (select id from "anv".reference where code = 'SALES_ORDER_REJECTION_REASON');
delete from "anv".reference where parentid = (select id from "anv".reference where code = 'SALES_QUOTE_REJECTION_REASON');
delete from "anv".reference where code = 'SALES_ORDER_REJECTION_REASON';
delete from "anv".reference where code = 'SALES_QUOTE_REJECTION_REASON';
insert into "anv".reference(antonym, code, deleted, isactive, issystemreference, name)
values ('Sales Order Lost Reasons', 'SALES_ORDER_REJECTION_REASON', false, true, true, 'Sales Order Lost Reasons');


insert into "anv".reference(antonym, code, deleted, isactive, issystemreference, name, parentid, sorder, requiredComment)
values
('Undefined reason', 'SALES_ORDER_REJECTION_REASON_UNDEFINED_REASON', false, true, true, 'Undefined reason',(select id from "anv".reference where code = 'SALES_ORDER_REJECTION_REASON'), 1, true),
('Insufficient budget', 'SALES_ORDER_REJECTION_REASON_INSUFFICIENT_BUDGET', false, true, true, 'Insufficient budget',(select id from "anv".reference where code = 'SALES_ORDER_REJECTION_REASON'), 2, true),
('Product does not fit need', 'SALES_ORDER_REJECTION_REASON_PRODUCT_DOES_NOT_FIT_NEED', false, true, true,'Product does not fit need', (select id from "anv".reference where code = 'SALES_ORDER_REJECTION_REASON'), 3, true),
('Not satisfied with conditions', 'SALES_ORDER_REJECTION_REASON_NOT_SATISFIED_CONDITION', false, true, true,'Not satisfied with conditions', (select id from "anv".reference where code = 'SALES_ORDER_REJECTION_REASON'), 4, true),
('Bought from competitor', 'SALES_ORDER_REJECTION_REASON_BOUGHT_FROM_COMPETITOR', false, true, true,'Bought from competitor', (select id from "anv".reference where code = 'SALES_ORDER_REJECTION_REASON'), 5, true);



insert into "anv".reference(antonym, code, deleted, isactive, issystemreference, name)
values ('Sales Quote Lost Reasons', 'SALES_QUOTE_REJECTION_REASON', false, true, true, 'Sales Quote Lost Reasons');


insert into "anv".reference(antonym, code, deleted, isactive, issystemreference, name, parentid, sorder, requiredComment)
values
('Undefined reason', 'SALES_QUOTE_REJECTION_REASON_UNDEFINED_REASON', false, true, true, 'Undefined reason',(select id from "anv".reference where code = 'SALES_QUOTE_REJECTION_REASON'), 1, true),
('Insufficient budget', 'SALES_QUOTE_REJECTION_REASON_INSUFFICIENT_BUDGET', false, true, true, 'Insufficient budget',(select id from "anv".reference where code = 'SALES_QUOTE_REJECTION_REASON'), 2, true),
('Product does not fit need', 'SALES_QUOTE_REJECTION_REASON_PRODUCT_DOES_NOT_FIT_NEED', false, true, true,'Product does not fit need', (select id from "anv".reference where code = 'SALES_QUOTE_REJECTION_REASON'), 3, true),
('Not satisfied with conditions', 'SALES_QUOTE_REJECTION_REASON_NOT_SATISFIED_CONDITION', false, true, true,'Not satisfied with conditions', (select id from "anv".reference where code = 'SALES_QUOTE_REJECTION_REASON'), 4, true),
('Bought from competitor', 'SALES_QUOTE_REJECTION_REASON_BOUGHT_FROM_COMPETITOR', false, true, true,'Bought from competitor', (select id from "anv".reference where code = 'SALES_QUOTE_REJECTION_REASON'), 5, true);
