

insert into "anv".mymodule (code,name,section,active) values ('COURSES','Courses','operations',true);
insert into "anv".mymodule (code,name,section,active) values ('SCHEDULED_COURSES','Scheduled Courses','operations',true);
insert into "anv".mymodule (code,name,section,active) values ('COURSE_SUBJECTS','Courses','operations',true);
insert into "anv".mymodule (code,name,section,active) values ('TC_BOOKING_ITEMS','Booking Items','operations',true);
insert into "anv".mymodule (code,name,section,active) values ('STAFF_TRAINERS','Staff/Trainers','operations',true);
insert into "anv".mymodule (code,name,section,active) values ('STUDENTS','Students','operations',true);
insert into "anv".mymodule (code,name,section,active) values ('ATTENDANCE_SHEET','Attendance Sheet','operations',true);
insert into "anv".mymodule (code,name,section,active) values ('INSTRUCTOR_REASSIGN','Instructor Reassign','operations',true);
insert into "anv".mymodule (code,name,section,active) values ('TRAINING_CONTRACT','Training Contract','operations',true);
insert into "anv".mymodule (code,name,section,active) values ('INVOICE_GENERATOR','Courses','consolidatedinvoice',true);
insert into "anv".mymodule (code,name,section,active) values ('SCHEDULE_INVOICE','Schedule invoice','consolidatedinvoice',true);

insert into "anv".container(code, defaultName, moduleCode, sorder, changed, preparedView)
values ('operations', 'operations', 'trainingcenter', 1, false, 'operations');


insert into "anv".container(code, defaultName, moduleCode, sorder, changed, preparedView)
values ('consolidatedinvoice', 'consolidated invoice', 'trainingcenter', 2, false, 'consolidated invoice');



insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='SCHEDULED_COURSES' limit 1), (select id from "anv".property where objectName='scheduledcourses' limit 1), (select id from "anv".container where code='operations' limit 1), 1, 'trainingcenter');
insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='COURSES' limit 1), (select id from "anv".property where objectName='course' limit 1), (select id from "anv".container where code='operations' limit 1), 2, 'trainingcenter');


insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='COURSE_SUBJECTS' limit 1), (select id from "anv".property where objectName='coursesubject' limit 1), (select id from "anv".container where code='operations' limit 1), 3, 'trainingcenter');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='TC_BOOKING_ITEMS' limit 1), (select id from "anv".property where objectName='bookingItemsList' limit 1), (select id from "anv".container where code='operations' limit 1), 4, 'trainingcenter');


insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='STAFF_TRAINERS' limit 1), (select id from "anv".property where objectName='tc_employee' limit 1), (select id from "anv".container where code='operations' limit 1), 5, 'trainingcenter');


insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='STUDENTS' limit 1), (select id from "anv".property where objectName='students' limit 1), (select id from "anv".container where code='operations' limit 1), 6, 'trainingcenter');


insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='ATTENDANCE_SHEET' limit 1), (select id from "anv".property where objectName='attendenceSheet' limit 1), (select id from "anv".container where code='operations' limit 1), 7, 'trainingcenter');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='INSTRUCTOR_REASSIGN' limit 1), (select id from "anv".property where objectName='instructorReassignList' limit 1), (select id from "anv".container where code='operations' limit 1), 8, 'trainingcenter');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='COURSE_BOOKING' limit 1), (select id from "anv".property where objectName='courseBooking' limit 1), (select id from "anv".container where code='operations' limit 1), 9, 'trainingcenter');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='TRAINING_CONTRACT' limit 1), (select id from "anv".property where objectName='trainingContract' limit 1), (select id from "anv".container where code='operations' limit 1), 10, 'trainingcenter');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='INVOICE_GENERATOR' limit 1), (select id from "anv".property where objectName='invoicegenerator' limit 1), (select id from "anv".container where code='consolidatedinvoice' limit 1), 1, 'trainingcenter');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='SCHEDULE_INVOICE' limit 1), (select id from "anv".property where objectName='scheduleinvoice' limit 1), (select id from "anv".container where code='consolidatedinvoice' limit 1), 2, 'trainingcenter');
