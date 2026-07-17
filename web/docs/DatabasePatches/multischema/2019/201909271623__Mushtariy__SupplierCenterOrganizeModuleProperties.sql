delete from "0".property where objectName = 'supplierList';
insert into "0".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('supplierList', 'Supplier Center', 'Supplier', 'Supplier Center', 'SC', 'accounting', false);

delete from "0_template".property where objectName = 'supplierList';
insert into "0_template".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('supplierList', 'Supplier Center', 'Supplier', 'Supplier Center', 'SC', 'accounting', false);

delete from "anv".property where objectName = 'supplierList';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('supplierList', 'Supplier Center', 'Supplier', 'Supplier Center', 'SC', 'accounting', false);