delete from "0".property where objectName = 'purchaseorder';
insert into "0".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('purchaseorder', 'Purchase Order', 'Purchase Order', 'Purchase Orders', 'PO', 'accounting', false);

delete from "0_template".property where objectName = 'purchaseorder';
insert into "0_template".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('purchaseorder', 'Purchase Order', 'Purchase Order', 'Purchase Orders', 'PO', 'accounting', false);

delete from "anv".property where objectName = 'purchaseorder';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('purchaseorder', 'Purchase Order', 'Purchase Order', 'Purchase Orders', 'PO', 'accounting', false);