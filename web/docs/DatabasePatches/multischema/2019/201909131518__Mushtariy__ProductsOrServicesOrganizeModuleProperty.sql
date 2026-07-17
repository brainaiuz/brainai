delete from "0".property where objectName = 'productsOrServices';
insert into "0".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('productsOrServices', 'Product/Service', 'Product/Service', 'Products/Services', 'P/S', 'accounting', false);

delete from "0_template".property where objectName = 'productsOrServices';
insert into "0_template".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('productsOrServices', 'Product/Service', 'Product/Service', 'Products/Services', 'P/S', 'accounting', false);

delete from "anv".property where objectName = 'productsOrServices';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('productsOrServices', 'Product/Service', 'Product/Service', 'Products/Services', 'P/S', 'accounting', false);