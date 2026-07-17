insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('leave_planner_list', 'Leave Planner', 'Leave Planner', 'Leave Planner', 'LP', 'hrms', false);

insert into "anv".container_item (isactive,modulecode,sorder,containerid,moduleid,propertyid)
values('false','hrms',5,(select id from "anv".container where code = 'availability'
),(select id from "anv".mymodule where code = 'CORE'),(select id from "anv".property where objectName='leave_planner_list'))

insert into "anv".genericsettings (key,value)
values('HRMS_LEAVE_PLANNER','NO')