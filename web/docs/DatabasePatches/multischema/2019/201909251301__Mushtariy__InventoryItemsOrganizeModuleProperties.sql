delete from "0".property where objectName = 'inventoryitems';
insert into "0".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('inventoryitems', 'Inventory Item', 'Inventory Item', 'Inventory Items', 'II', 'accounting', false);

delete from "0_template".property where objectName = 'inventoryitems';
insert into "0_template".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('inventoryitems', 'Inventory Item', 'Inventory Item', 'Inventory Items', 'II', 'accounting', false);

delete from "anv".property where objectName = 'inventoryitems';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('inventoryitems', 'Inventory Item', 'Inventory Item', 'Inventory Items', 'II', 'accounting', false);