update "anv".container_item set sorder = sorder+1 where moduleCode = 'hrms' and
containerId = (select id from "anv".container where code='recruitment' limit 1);



 insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('position', 'Position', 'Position', 'Positions', 'Po', 'hrms', false);

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code = 'HRMS_MODULE' limit 1),
       (select id from "anv".property where objectName='position' limit 1),
       (select id from "anv".container where code='recruitment' limit 1), 1, 'hrms');




