insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('STOCK_OUT', 'Stock Out', 'Stock Out', 'Stock Outs', 'SO', 'accounting', false) on conflict do nothing;

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='INVENTORY_MANAGEMENT' limit 1), (select id from "anv".property where objectName='STOCK_OUT' limit 1), (select id from "anv".container where code='accounting' limit 1), 18, 'accounting');
