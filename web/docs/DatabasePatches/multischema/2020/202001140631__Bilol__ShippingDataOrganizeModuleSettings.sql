delete from "0".property where objectName = 'goodsreceivednotes';
insert into "0".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('goodsreceivednotes', 'Goods Received Notes', 'Goods Received Note', 'Goods Received Notes', 'GRN', 'accounting', false);

delete from "0_template".property where objectName = 'goodsreceivednotes';
insert into "0_template".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('goodsreceivednotes', 'Goods Received Notes', 'Goods Received Note', 'Goods Received Notes', 'GRN', 'accounting', false);

delete from "anv".property where objectName = 'goodsreceivednotes';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('goodsreceivednotes', 'Goods Received Notes', 'Goods Received Note', 'Goods Received Notes', 'GRN', 'accounting', false);

delete from "0".property where objectName = 'goodsdeliverednotes';
insert into "0".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('goodsdeliverednotes', 'Goods Delivered Notes', 'Goods Delivered Note', 'Goods Delivered Notes', 'GDN', 'accounting', false);

delete from "0_template".property where objectName = 'goodsdeliverednotes';
insert into "0_template".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('goodsdeliverednotes', 'Goods Delivered Notes', 'Goods Delivered Note', 'Goods Delivered Notes', 'GDN', 'accounting', false);

delete from "anv".property where objectName = 'goodsdeliverednotes';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('goodsdeliverednotes', 'Goods Delivered Notes', 'Goods Delivered Note', 'Goods Delivered Notes', 'GDN', 'accounting', false);