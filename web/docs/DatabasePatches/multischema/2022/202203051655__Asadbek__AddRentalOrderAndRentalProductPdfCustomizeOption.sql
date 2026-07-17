
delete from "anv".pdfreference where code = 'RENTAL_PRODUCT';
insert into "anv".pdfreference (code, name) values ('RENTAL_PRODUCT', 'Rental Product');

delete from "anv".pdfreference where code = 'RENTAL_ORDER';
insert into "anv".pdfreference (code, name) values ('RENTAL_ORDER', 'Rental Order');
