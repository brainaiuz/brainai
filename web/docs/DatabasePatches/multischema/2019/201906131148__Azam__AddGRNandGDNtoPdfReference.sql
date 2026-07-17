delete from "0".pdfreference where code = 'GOODS_RECEIVED_NOTES';
delete from "0".pdfreference where code = 'GOODS_DELIVERED_NOTES';

delete from "anv".pdfreference where code = 'GOODS_RECEIVED_NOTES';
delete from "anv".pdfreference where code = 'GOODS_DELIVERED_NOTES';

insert into "0".pdfreference (code, name) values ('GOODS_RECEIVED_NOTES', 'GRN');
insert into "0".pdfreference (code, name) values ('GOODS_DELIVERED_NOTES', 'GDN');

insert into "anv".pdfreference (code, name) values ('GOODS_RECEIVED_NOTES', 'GRN');
insert into "anv".pdfreference (code, name) values ('GOODS_DELIVERED_NOTES', 'GDN');