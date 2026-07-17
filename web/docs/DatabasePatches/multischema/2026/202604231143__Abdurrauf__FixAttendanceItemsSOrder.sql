update "anv".container_item
set sorder = 6
where propertyID = (select id from "anv".property where objectName = 'attendanceMarksList' limit 1);
