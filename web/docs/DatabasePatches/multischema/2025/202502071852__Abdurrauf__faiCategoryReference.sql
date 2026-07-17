insert into "anv".reference(code) values('_FAI_CATEGORY');

insert into "anv".reference(name, sorder, parentid) values ('Goods', 1, (select id from "anv".reference where code = '_FAI_CATEGORY'));
insert into "anv".reference(name, sorder, parentid) values ('Services', 2, (select id from "anv".reference where code = '_FAI_CATEGORY'));
insert into "anv".reference(name, sorder, parentid) values ('Medical Treatment for Saudis', 3, (select id from "anv".reference where code = '_FAI_CATEGORY'));
insert into "anv".reference(name, sorder, parentid) values ('Qualified Medical Equipment', 4, (select id from "anv".reference where code = '_FAI_CATEGORY'));
insert into "anv".reference(name, sorder, parentid) values ('Qualified Medicines', 5, (select id from "anv".reference where code = '_FAI_CATEGORY'));
insert into "anv".reference(name, sorder, parentid) values ('Educational Services for Saudis', 6, (select id from "anv".reference where code = '_FAI_CATEGORY'));
insert into "anv".reference(name, sorder, parentid) values ('International transport of Goods', 16, (select id from "anv".reference where code = '_FAI_CATEGORY'));
insert into "anv".reference(name, sorder, parentid) values ('Eligible transportation', 19, (select id from "anv".reference where code = '_FAI_CATEGORY'));
insert into "anv".reference(name, sorder, parentid) values ('Eligible Precious Metals', 20, (select id from "anv".reference where code = '_FAI_CATEGORY'));
insert into "anv".reference(name, sorder, parentid) values ('Eligible Financial Services', 21, (select id from "anv".reference where code = '_FAI_CATEGORY'));
insert into "anv".reference(name, sorder, parentid) values ('Eligible Securities', 22, (select id from "anv".reference where code = '_FAI_CATEGORY'));
insert into "anv".reference(name, sorder, parentid) values ('Eligible Insurance', 23, (select id from "anv".reference where code = '_FAI_CATEGORY'));
insert into "anv".reference(name, sorder, parentid) values ('Services relating to Goods or passenger transportation', 54, (select id from "anv".reference where code = '_FAI_CATEGORY'));
insert into "anv".reference(name, sorder, parentid) values ('International Passenger Transport', 55, (select id from "anv".reference where code = '_FAI_CATEGORY'));
