DELETE FROM "anv".container_item
WHERE propertyID = (SELECT id FROM "anv".property WHERE objectName = 'courseBooking' LIMIT 1)
  AND containerId = (SELECT id FROM "anv".container WHERE code = 'operations' LIMIT 1)
  AND sorder = 9
  AND moduleCode = 'trainingcenter';

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='COURSE_BOOKING' limit 1), (select id from "anv".property where objectName='courseBooking' limit 1), (select id from "anv".container where code='operations' limit 1), 9, 'trainingcenter');