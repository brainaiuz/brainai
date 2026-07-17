delete from "0".property where objectName = 'saleorder';
insert into "0".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('saleorder', 'Sales Order', 'Sales Order', 'Sales Orders', 'SO', 'accounting', false);

delete from "0_template".property where objectName = 'saleorder';
insert into "0_template".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('saleorder', 'Sales Order', 'Sales Order', 'Sales Orders', 'SO', 'accounting', false);

delete from "anv".property where objectName = 'saleorder';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('saleorder', 'Sales Order', 'Sales Order', 'Sales Orders', 'SO', 'accounting', false);